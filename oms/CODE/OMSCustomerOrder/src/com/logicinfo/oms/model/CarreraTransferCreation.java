package com.logicinfo.oms.model;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;

import javax.xml.soap.SOAPException;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.logicinfo.oms.ejb.OMSUtilSessionEJB;
import com.logicinfo.oms.util.OMSUtil;

public class CarreraTransferCreation
{
    HttpURLConnection connection_=null;
    int _responseCode=-1;
    private final static Logger log=Logger.getLogger(CarreraTransferCreation.class.getName());

    public CarreraTransferCreation()
    {
	super();
    }

    public String getCarreraTsfWebServiceURL() throws SOAPException
    {
	OMSUtilSessionEJB session=OMSUtil.doLookup();
	String webserviceURL=session.getOmsWebserviceUriDetailFindByWebserviceName("CARRERA_TRANSFER_CREATION");
	// String webserviceURL ="http://localhost:7001/transferCreation/NewTransferCreation";
	return webserviceURL;
    }

    public TransferResponse createTransfer(TransferRequest request)
    {

	String response=null;
	TransferResponse tsfResponse=null;
	try
	{
	    log.info("Calling Carrera Transfer Creation Webservice for the order number "+request.getCust_ord_no());
	    TransferRequest req=request;
	    Gson gson=new Gson();
	    String gsonString=gson.toJson(req,TransferRequest.class);
	    String tsfURL=getCarreraTsfWebServiceURL();
	    String method="POST";

	    response=sendReceiveResponse(gsonString,tsfURL,method);
	    tsfResponse=getJsonTransferResponse(response);

	}
	catch(Exception e)
	{
	    log.info("Exception occurred while calling carrera transfer creation "+e.getMessage());

	}
	return tsfResponse;
    }

    private String sendReceiveResponse(String jsonRequest,String tsfCreURL,String method) throws Exception
    {

	String response="";
	try
	{
	    URL url=new URL(tsfCreURL);

	    String msgtype="application/json";

	    response=sendRequest(url,method,jsonRequest,msgtype);
	    log.info("response: "+response);

	}
	catch(MalformedURLException e)
	{
	    log.info("Malforemed URL Exception while sending request/receiving response from web service: "+
		     e.toString());
	    log.info("Errored Response: "+response);
	    throw e;
	}
	catch(Exception e)
	{
	    log.info("Exception while sending request/receiving response from web service: "+e.toString());
	    log.info("Errored Response: "+response);
	    throw e;
	}
	return response;
    }

    public String sendRequest(URL url,String method,String message,String msgtype)
    {
	String response=null;

	try
	{
	    if(checkConnection(url,method,msgtype))
	    {
		// Sending the request to Remote server
		connection_=(HttpURLConnection)url.openConnection();
		connection_.setDoOutput(true);
		connection_.setRequestMethod(method);
		connection_.setRequestProperty("Content-Type",msgtype);
		log.info("connection_.getOutputStream(): "+connection_.getOutputStream());
		OutputStreamWriter writer=new OutputStreamWriter(connection_.getOutputStream());
		writer.write(message);
		writer.flush();
		writer.close();
		_responseCode=connection_.getResponseCode();
		log.info("Response Code :"+_responseCode);
		// reading the response
		InputStreamReader reader=new InputStreamReader(connection_.getInputStream());
		StringBuilder buf=new StringBuilder();
		char[] cbuf=new char[2048];
		int num;
		while(-1!=(num=reader.read(cbuf)))
		{
		    buf.append(cbuf,0,num);
		}
		response=buf.toString();
	    }
	}
	catch(Exception e)
	{
	    log.info("Exception occured while sending message : "+e.toString());
	}

	// releaseConnection();
	return response;
    }

    private boolean checkConnection(URL url,String method,String msgtype)
    {
	try
	{

	    connection_=(HttpURLConnection)url.openConnection();

	    if(method=="POST")
		connection_.setRequestMethod(method);
	    connection_.setDoOutput(true);
	    connection_.setRequestProperty("Content-Type",msgtype);
	    connection_.connect();

	    TimeoutTimer rtt=new TimeoutTimer(connection_);
	    Timer timer=new Timer();
	    timer.schedule(rtt,10000);

	    return true;
	}
	catch(Exception e)
	{
	    log.info("Exception occurred while establishing connection to carrera transfer creation server. Error :"+
		     e.getMessage());
	    connection_.disconnect();
	    connection_=null;
	    return false;
	}

    }

    private TransferResponse getJsonTransferResponse(String jsonResponseString)
    {
	TransferResponse transferResponse=null;
	try
	{
	    log.info("response from carrera transfer creation web service "+jsonResponseString);
	    //            JsonObject json = new JsonParser().parse(jsonResponseString).getAsJsonObject();
	    //            JsonElement responseCode=json.get("code");
	    //            transferResponse.setCode(json.get("code").getAsString());
	    //            transferResponse.setSuccess(json.get("success").getAsString());
	    //            JsonElement jsontsfNo=json.get("tsf_No");
	    //            if (jsontsfNo != null && !jsontsfNo.isJsonNull()){
	    //            transferResponse.setTsf_no(json.get("tsf_No").getAsString());
	    //            }
	    //            JsonElement jsonMsg=json.get("message");
	    //            if (jsonMsg != null && !jsonMsg.isJsonNull()){
	    //            transferResponse.setMessage(json.get("message").getAsString());
	    //            }
	    Gson gsonObj=new Gson();
	    transferResponse=gsonObj.fromJson(jsonResponseString,TransferResponse.class);

	}
	catch(Exception e)
	{
	    e.printStackTrace();
	    log.info("Exception in getJsonCustomeResponse "+e.getMessage());
	}

	return transferResponse;
    }

    private class TimeoutTimer extends java.util.TimerTask
    {
	private HttpURLConnection m_connection;

	public void run()
	{
	    try
	    {
		m_connection.disconnect();
	    }
	    catch(Exception ioe)
	    {
		System.out.println("Unknown Exception in TimeOutTimer::run() : "+ioe.getMessage());
	    }
	}

	public boolean cancel()
	{
	    return super.cancel();
	}

	public TimeoutTimer(HttpURLConnection conn)
	{
	    m_connection=conn;
	}
    }


}
