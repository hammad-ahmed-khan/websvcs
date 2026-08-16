package extra.retail.sim.service.imei;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.core.SimServerException;
import oracle.retail.sim.common.logging.LogNames;
import oracle.retail.sim.common.logging.LogService;

import extra.retail.sim.common.configutil.ExtraCommonConfigManager;
import extra.retail.sim.common.imei.UniqueSerialNumber;

/**
 * ExtraIMEIWSServices.java
 * aibrahim
 * 2023
 */
public class ExtraIMEIWebServices extends ExtraIMEIServices {

	private static final String BASE_URL = "IMEI_WEB_SERVICE_BASE_URL";

	private HttpURLConnection connection_ = null;

	private String baseURL;

	int _responseCode = -1;

	public ExtraIMEIWebServices() {
		baseURL = ExtraCommonConfigManager.getString(BASE_URL);
	}

	@Override
	public void saveIMEI(List<UniqueSerialNumber> imeiSerialNumbers) throws Exception {
		sendReceiveResponse(new Gson().toJson(imeiSerialNumbers), "/imei", "POST");
	}

	@Override
	public void cancelIMEI(List<UniqueSerialNumber> imeiSerialNumbers) throws Exception {
		sendReceiveResponse(new Gson().toJson(imeiSerialNumbers), "/cancel-imei", "POST");
	}

	@SuppressWarnings("unchecked")
	private String sendReceiveResponse(String jsonRequest, String path, String method) throws Exception {

		String response = null;
		try {
			URL url = new URL(baseURL + path);
			String msgtype = "application/json";

			response = sendRequest(url, method, jsonRequest, msgtype);
			if (LogService.isDebugEnabled(LogNames.SERIALIZED_OBJECT_SIZES)) {
				LogService.debug(LogNames.SERIALIZED_OBJECT_SIZES, "response: " + response);
			}
			Map<String, ?> responseMap = new Gson().fromJson(response, Map.class);
			if (Boolean.FALSE.equals(responseMap.get("success"))) {
				String errorMessage = responseMap.get("message").toString();
				if (errorMessage.contains("unique constraint")) {
					throw new BusinessException(CommonMessageText.IMEI_EXISTS);
				} else if (errorMessage.contains("IMEI Qty cannot be more than Picked Quantity")) {
					throw new BusinessException(CommonMessageText.IMEI_QUANTITY_GREATER_THAN_PICKED_QUANTITY);
				} else if (errorMessage.contains("Invalid IMEI Number")) {
					throw new BusinessException(CommonMessageText.IMEI_INVALID_NUMBER);
				} else {
					throw new BusinessException(CommonMessageText.IMEI_GENERAL);
				}
			}
		} catch (BusinessException be) {
			LogService.error(LogNames.INTEGRATION, "Business Error: " + response, be);
			throw be;
		} catch (Exception e) {
			LogService.error(LogNames.INTEGRATION, "Errored Response: " + response, e);
			throw new SimServerException(e);
		}
		return response;
	}

	public String sendRequest(URL url, String method, String message, String msgtype) throws Exception {
		String response = null;

		try {
			if (checkConnection(url, method, msgtype)) {
				// Sending the request to Remote server
				connection_ = (HttpURLConnection) url.openConnection();
				connection_.setDoOutput(true);
				connection_.setRequestMethod(method);
				connection_.setRequestProperty("Content-Type", msgtype);
				System.out.println("connection_.getOutputStream(): " + connection_.getOutputStream());
				OutputStreamWriter writer = new OutputStreamWriter(connection_.getOutputStream());
				writer.write(message);
				writer.flush();
				writer.close();
				_responseCode = connection_.getResponseCode();
				System.out.println("Response Code :" + _responseCode);
				// reading the response
				if (_responseCode != 200) {
					throw new Exception();
				}
				InputStreamReader reader = new InputStreamReader(connection_.getInputStream());
				StringBuilder buf = new StringBuilder();
				char[] cbuf = new char[2048];
				int num;
				while (-1 != (num = reader.read(cbuf))) {
					buf.append(cbuf, 0, num);
				}
				response = buf.toString();
			}
		} finally {
			try {
				connection_.disconnect();
			} catch(Exception e) {
				// Ignore Exception
			}
		}
		return response;
	}

	private boolean checkConnection(URL url, String method, String msgtype) throws Exception {
		try {
			connection_ = (HttpURLConnection) url.openConnection();
			if (method == "POST") {
				connection_.setRequestMethod(method);
			}
			connection_.setDoOutput(true);
			connection_.setRequestProperty("Content-Type", msgtype);
			connection_.connect();
			return true;
		} catch (Exception e) {
			LogService.error(LogNames.INTEGRATION, "An error occurred accessing IMEIService . Please contact your system administrator.",  e);
			throw e;
		}
	}
}
