/**
 * 
 */
package extra.retail.sim.webservice.shipTrailer.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import extra.retail.sim.client.common.model.BaseLV;
import extra.retail.sim.client.screen.shipTrailer.DMShipTrailer;

/**
 * @author aibrahim
 *
 */
public class DMShipTrailerCaptureClient {

	private static final DMShipTrailerCaptureClient CLIENT = new DMShipTrailerCaptureClient();

	private String getExtraBaseWebServiceURL() {
		// String defaultURL = "http://localhost:7101/extra-imei-capture";
		String defaultURL ="http://exvm-qaadfapp01.extrastores.com:7511/extra-imei-capture";
		// String defaultURL = "http://prodsimapp.extrastores.com:7511/extra-imei-capture";
		return defaultURL;
	}

	public DMShipTrailer getDMShipTrailer(Long transReturnId, Boolean isTransfer) {

		DMShipTrailer shipTrailer = null;

		try {
			Gson gson = new Gson();
			String url = getExtraBaseWebServiceURL() + "/ship-trailer";
			String response = fetchShipTrailer(transReturnId, isTransfer, url);
			shipTrailer = gson.fromJson(response, DMShipTrailer.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return shipTrailer;
	}

	private String fetchShipTrailer(Long transReturnId, Boolean isTransfer, String url) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("transferReturnId", transReturnId);
		params.put("isTransfer", isTransfer);
		return sendRequest(url, "GET", null, "application/json", params);
	}

	private String sendRequest(String url, String method, String body, String contentType, Map<String, ? extends Object> params) {
		String response = null;

		try {
			if (params != null) {
				StringBuilder queryBuilder = new StringBuilder();
				for (Entry<String, ? extends Object> param : params.entrySet()) {
					if (queryBuilder.length() > 0) {
						queryBuilder.append("&");
					}
					queryBuilder.append(param.getKey()).append("=").append(URLEncoder.encode(param.getValue().toString(), "UTF-8"));
				}
				if (queryBuilder.length() > 0) {
					url += ("?" + queryBuilder.toString());
				}
			}
			HttpURLConnection connection_ = (HttpURLConnection) new URL(url).openConnection();

			if (method != null) {
				connection_.setRequestMethod(method);
			}
			connection_.setDoOutput(true);
			connection_.setRequestProperty("Content-Type", contentType);
			connection_.connect();
			// Sending the request to Remote server
			if (body != null) {
				OutputStreamWriter writer = new OutputStreamWriter(connection_.getOutputStream());
				writer.write(body);
				writer.flush();
				writer.close();
			}
			int _responseCode = connection_.getResponseCode();
			System.out.println("Response Code :" + _responseCode);
			// reading the response
			if (_responseCode != 200) {
				throw new Exception();
			}
			StringBuilder sb = new StringBuilder();
			BufferedReader br = new BufferedReader(new InputStreamReader(connection_.getInputStream(), "utf-8"));
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line + "\n");
			}
			br.close();
			System.out.println("" + sb.toString());
			response = sb.toString();
		} catch (Exception e) {
			System.err.println("Exception occured while sending message : " + e.toString());
		}
		return response;
	}

	public Map<String, List<BaseLV>> getBaseLVs() {
		String response = sendRequest(getExtraBaseWebServiceURL() + "/util/baselv", "GET", null, "application/json", Collections.singletonMap("listCode", "TRANSPORTER,TRUCK_LOAD,TRAILER_TYPE,TRAILER_PICK_TYPE"));
		Gson gson = new Gson();
		return gson.fromJson(response, new TypeToken<Map<String, List<BaseLV>>>() {}.getType());
	}

	public static DMShipTrailerCaptureClient getInstance() {
		return CLIENT;
	}

	public void savaShipTrailer(DMShipTrailer shipTrailer) {
		Gson gson = new Gson();
		String reponse = sendRequest(getExtraBaseWebServiceURL() + "/ship-trailer", "POST", gson.toJson(shipTrailer), "application/json", null);
		if (reponse != null && !reponse.trim().equals("OK")) {
			throw new RuntimeException("Error while saving the ship trailer. Please try again");
		}
	}

	public void updateShipTrailer(DMShipTrailer shipTrailer) {
		Gson gson = new Gson();
		String reponse = sendRequest(getExtraBaseWebServiceURL() + "/ship-trailer", "PUT", gson.toJson(shipTrailer), "application/json", null);
		if (reponse != null && !reponse.trim().equals("OK")) {
			throw new RuntimeException("Error while updating the ship trailer. Please try again");
		}
	}

	public boolean hasShipTrailer(Long id) {
		String reponse = sendRequest(getExtraBaseWebServiceURL() + "/ship-trailer/" + id + "/exists", "GET", null, "application/json", null);
		return reponse != null && Boolean.parseBoolean(reponse.trim());
	}

	public boolean isShipTrailerEnabled(Long storeId) {
		String reponse = sendRequest(getExtraBaseWebServiceURL() + "/ship-trailer/" + storeId + "/config", "GET", null, "application/json", null);
		return reponse != null && Boolean.parseBoolean(reponse.trim());
	}
}
