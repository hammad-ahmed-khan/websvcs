package extra.retail.sim.webservice.imeifulfillmentorderdelivery.client;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;

import com.google.gson.Gson;

import extra.retail.sim.webservice.imeifulfillmentorderdelivery.model.LookupUIN;

public class LookupImeiEnabledWebserviceClient {

	HttpURLConnection connection_ = null;

	int _responseCode = -1;

	public LookupImeiEnabledWebserviceClient() {
	}

	public String getExtraDoaWebServiceURL() {
		// String defaultURL =
		// "http://exvm-tstadfapp01.extrastores.com:7511/extra-imei-capture/sim/lookup-imei";
		//String defaultURL="http://localhost:7001/extra-imei-capture/sim/lookup-uin-enabled";
		String defaultURL = "http://qasimapp.extrastores.com:7511/extra-imei-capture/sim/lookup-uin-enabled";
		// String defaultURL = "http://prodsimapp.extrastores.com:7511/extra-imei-capture/sim/lookup-uin-enabled";
		return defaultURL;
	}

	public String lookupUINEnabled(LookupUIN request) {

		String response = null;
		try {
			System.out.println("Calling Webservice");
			LookupUIN vor = request;
			Gson gson = new Gson();
			String gsonString = gson.toJson(vor, LookupUIN.class);
			String doaUrl = getExtraDoaWebServiceURL();
			String method = "POST";

			response = sendReceiveResponse(gsonString, doaUrl, method);
		} catch (Exception e) {
			// TODO Auto-generated catch block

		}
		return response;
	}

	private String sendReceiveResponse(String jsonRequest, String doaUrl, String method) throws Exception {

		String response = "";
		try {
			URL url = new URL(doaUrl);

			String msgtype = "application/json";

			response = sendRequest(url, method, jsonRequest, msgtype);
			System.out.println("response: " + response);

		} catch (MalformedURLException e) {
			System.out.println("Malforemed URL Exception while sending request/receiving response from web service: "
					+ e.toString());
			System.out.println("Errored Response: " + response);
			throw e;
		} catch (IOException e) {
			System.out
					.println("IO Exception while sending request/receiving response from web service: " + e.toString());
			System.out.println("Errored Response: " + response);
			throw e;
		} catch (Exception e) {
			System.out.println("Exception while sending request/receiving response from web service: " + e.toString());
			System.out.println("Errored Response: " + response);
			throw e;
		}
		return response;
	}

	public String sendRequest(URL url, String method, String message, String msgtype) {
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
				InputStreamReader reader = new InputStreamReader(connection_.getInputStream());
				StringBuilder buf = new StringBuilder();
				char[] cbuf = new char[2048];
				int num;
				while (-1 != (num = reader.read(cbuf))) {
					buf.append(cbuf, 0, num);
				}
				response = buf.toString();
			}
		} catch (Exception e) {
			System.out.println("Exception occured while sending message : " + e.toString());
		}

		// releaseConnection();
		return response;
	}

	private boolean checkConnection(URL url, String method, String msgtype) {
		try {

			connection_ = (HttpURLConnection) url.openConnection();

			if (method == "POST")
				connection_.setRequestMethod(method);
			connection_.setDoOutput(true);
			connection_.setRequestProperty("Content-Type", msgtype);
			connection_.connect();

			TimeoutTimer rtt = new TimeoutTimer(connection_);
			Timer timer = new Timer();
			timer.schedule(rtt, 10000);

			return true;
		} catch (Exception e) {
			System.out.println(
					"Exception occurred while establishing connection to sim server. Error :" + e.getMessage());
			connection_.disconnect();
			connection_ = null;
			return false;
		}

	}

	private class TimeoutTimer extends java.util.TimerTask {
		private HttpURLConnection m_connection;

		public void run() {
			try {
				m_connection.disconnect();
			} catch (Exception ioe) {
				System.out.println("Unknown Exception in TimeOutTimer::run() : " + ioe.getMessage());
			}
		}

		public boolean cancel() {
			return super.cancel();
		}

		public TimeoutTimer(HttpURLConnection conn) {
			m_connection = conn;
		}
	}

}
