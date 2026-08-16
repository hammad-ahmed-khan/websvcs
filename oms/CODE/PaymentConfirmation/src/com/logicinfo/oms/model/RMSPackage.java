package com.logicinfo.oms.model;


import com.logicinfo.oms.beans.OMSUtilCommons;
import com.logicinfo.oms.beans.OmsErrorCodesConstant;
import com.logicinfo.oms.ejb.OMSUtilSessionEJB;
import com.logicinfo.oms.ejb.OmsCustOrdHead;
import com.logicinfo.oms.ejb.OmsCustOrdReserve;
import com.logicinfo.oms.ejb.OmsResUnresvCustOrderLog;
import com.logicinfo.oms.util.OMSConstants;
import com.logicinfo.oms.util.OMSUtil;

import java.math.BigDecimal;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.soap.SOAPException;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.log4j.Logger;


public class RMSPackage
{

    public final static Logger log=Logger.getLogger(com.logicinfo.oms.model.RMSPackage.class.getName());
    int numberOfRetry=1;

    void rollbackRmsPackageCall(CoPaymentConf input,BigDecimal omsCustOrdNo) throws SOAPException
    {
	log.info(omsCustOrdNo+"***Rollback -rmsPackageCall-started***");
	BigDecimal custOrdHeadSeqNo=omsCustOrdNo;
	int numberOfRetry=1;
	Connection con=null;
	CallableStatement pstmt=null;
	int result=0;
	OMSUtilSessionEJB session=OMSUtil.doLookup();
	for(OmsCustOrdReserve custOrdItems:session.getOmsCustOrdReserveFindByOmsCustOrdNo(omsCustOrdNo))
	{
	    log.info("inside for loop ");
	    if(custOrdItems.getRmsResvLocType().equals("SU")==false&&
	       custOrdItems.getRmsResvLocType().equals("ST")==false)
	    {
		log.info("custOrdItems.getRmsResvLocType() "+custOrdItems.getRmsResvLocType()+"inside if condition");
		if(custOrdItems.getQty().intValue()>0)
		{
		    try
		    {
			con=OMSUtil.createDBConnection(OMSConstants.DS_OMS_STRING);
			pstmt=con.prepareCall("{call OMS_INVADJ_STATUS_UNAVIALINV(?,?,?,?,?,?,?,?,?)}");

			pstmt.setString(1,custOrdItems.getItem());
			int i_inv_staus=Integer.parseInt(session.getOmsSystemParametersFindIndValue("RESV_INV_STATUS","OMS_SYSTEM_OPTION"));
			pstmt.setInt(2,i_inv_staus); //read from system parameter table I_inv_status
			log.info("i_inv_staus "+i_inv_staus);
			log.info("custOrdItems.getRmsResvLocType() "+custOrdItems.getRmsResvLocType());
			if(custOrdItems.getRmsResvLocType().equals("ST"))
			{
			    log.info("ST is equal");
			    pstmt.setString(3,"S"); //loc type S,W ,source loc type
			}
			else
			{
			    log.info("ST is not equal");
			    pstmt.setString(3,"W");
			}

			log.info("ResvLoc "+custOrdItems.getRmsResvLoc());
			pstmt.setInt(4,(custOrdItems.getRmsResvLoc().intValue()));
			int qty=(custOrdItems.getRmsResvQty().intValueExact());
			log.info("qty "+qty);
			pstmt.setInt(5,qty);

			int reason_code=Integer.parseInt(session.getOmsSystemParametersFindIndValue("REASON_CODE","OMS_SYSTEM_OPTION"));
			pstmt.setInt(6,reason_code); //reason_id=13
			log.info("reason_code "+reason_code);
			OmsCustOrdHead omsCustOrdHead=session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
			pstmt.setString(7,omsCustOrdHead.getCustOrderNo()+"_PY::");
			pstmt.registerOutParameter(8,Types.INTEGER);
			pstmt.registerOutParameter(9,Types.VARCHAR);
			pstmt.executeUpdate();
			result=pstmt.getInt(8);
			System.out.println("plsql call ::"+result);
			String err_msg=pstmt.getString(9);
			log.info("err_msg "+err_msg);
			if(result!=1)
			{
			    log.error("-->Unable to process the record in RMS package.Error message is "+err_msg);
			    // Added code to Support Error in multi language Error Description as part of 3209 Bug.
			    String languageCode=OMSUtilCommons.getLanguageCodeOfOmsCustomerOrderNo(omsCustOrdNo);
			    Boolean flag=
	  OMSUtilCommons.checkErrorCodeExistOrNot(OmsErrorCodesConstant.errorUnHandledExceptionCode,languageCode);
			    String errString=null;
			    if(Boolean.FALSE==flag)
			    {
				languageCode=OmsErrorCodesConstant.baseLanguageCodeValue;
			    }
			    errString=
		 OMSUtilCommons.formErrorDescription(OmsErrorCodesConstant.errorUnHandledExceptionCode,languageCode,new String[]{ });
			    if(err_msg.contains("TABLE_LOCKED"))
			    {
				throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("TABLE_LOCKED"));
			    }
			    else
			    {
				throw new SOAPException(errString);
			    }
			}
		    }
		    catch(Exception e)
		    {
			log.error("-->Unable to process the record in RMS package.Error message is "+e.getMessage());
			String languageCode=OMSUtilCommons.getLanguageCodeOfOmsCustomerOrderNo(omsCustOrdNo);
			Boolean flag=
	  OMSUtilCommons.checkErrorCodeExistOrNot(OmsErrorCodesConstant.errorUnHandledExceptionCode,languageCode);
			String errString=null;
			if(Boolean.FALSE==flag)
			{
			    languageCode=OmsErrorCodesConstant.baseLanguageCodeValue;
			}
			OMSUtilCommons.formErrorDescription(OmsErrorCodesConstant.errorUnHandledExceptionCode,languageCode,
							    new String[]{ });

			throw new SOAPException(errString);
		    }
		    finally
		    {
			try
			{
			    pstmt.close();
			    con.close();

			}
			catch(SQLException e)
			{
			    e.printStackTrace();
			}
		    }
		}

	    }
	}
    }

    void rmsPackageCall(CoPaymentConf input,BigDecimal omsCustOrdNo) throws SOAPException
    {

	log.info(omsCustOrdNo+"<---------Begin of the RMS package call started-------------->");
	log.info(" omsCustOrdNo "+omsCustOrdNo+" rms package call started");
	OMSUtilSessionEJB session=OMSUtil.doLookup();
	Connection con=null;
	CallableStatement pstmt=null;
	int result=0;
	int loopCount=0;
	OmsResUnresvCustOrderLog omsResUnresvCustOrderLog=null;
	List<OmsCustOrdReserve> omsCustOrdReserveList=session.getOmsCustOrdReserveFindByOmsCustOrdNo(omsCustOrdNo);
	log.info(omsCustOrdNo+"reservereList "+omsCustOrdReserveList.size());
	for(OmsCustOrdReserve omsCustOrdReserveItem:omsCustOrdReserveList)
	{
	    if(omsCustOrdReserveItem.getRmsResvLocType().equals("SU")==false&&
	       omsCustOrdReserveItem.getRmsResvLocType().equals("ST")==false)
	    {
		if(omsCustOrdReserveItem.getQty().intValue()>0)
		{
		    loopCount++;
		    try
		    {
			con=OMSUtil.createDBConnection(OMSConstants.DS_OMS_STRING);
			pstmt=con.prepareCall("{call OMS_INVADJ_STATUS_UNAVIALINV(?,?,?,?,?,?,?,?,?)}");
			pstmt.setString(1,omsCustOrdReserveItem.getItem());
			int i_inv_staus=Integer.parseInt(session.getOmsSystemParametersFindIndValue("RESV_INV_STATUS","OMS_SYSTEM_OPTION"));
		        log.info(omsCustOrdNo+"i_inv_staus "+i_inv_staus);
			pstmt.setInt(2,i_inv_staus);
			if(omsCustOrdReserveItem.getRmsResvLocType().equals("ST"))
			{
			    pstmt.setString(3,"S");
			}
			else
			{
			    pstmt.setString(3,"W");
			}
			pstmt.setInt(4,(omsCustOrdReserveItem.getRmsResvLoc().intValue()));
			int qty=-(omsCustOrdReserveItem.getRmsResvQty().intValueExact());
		        log.info(omsCustOrdNo+"qty "+qty);
			pstmt.setInt(5,qty);
			int reason_code=Integer.parseInt(session.getOmsSystemParametersFindIndValue("REASON_CODE","OMS_SYSTEM_OPTION"));
		        log.info(omsCustOrdNo+"reason_code "+reason_code);
			pstmt.setInt(6,reason_code);
			OmsCustOrdHead omsCustOrdHead=session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
			pstmt.setString(7,omsCustOrdHead.getCustOrderNo()+"_PY");
			pstmt.registerOutParameter(8,Types.INTEGER);
			pstmt.registerOutParameter(9,Types.VARCHAR);
			for(int i=1;i<=10;i++)
			{
			    try
			    {
				pstmt.executeUpdate();
				result=pstmt.getInt(8);
				String err_msg=pstmt.getString(9);
				if(result!=1)
				{
				    log.info(omsCustOrdNo+"----------------Error occured rms packgae ------------"+err_msg);
				    if(i==10)
					throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("TABLE_LOCKED"));
				}
				else
				{
				    log.info(omsCustOrdNo+"Sucessfull attempet no"+i);
				    break;
				}
			    }
			    catch(Exception e)
			    {
				log.info(omsCustOrdNo+"----------------------Error Occured----------------");
				// If any Exception  or retry occured  for 10 times then action need to be taken..
				if(i==10)
				{
				    log.info("omsCustOrdNo"+omsCustOrdNo+ "going to perisist record into res and unresv table");
				    omsResUnresvCustOrderLog=new OmsResUnresvCustOrderLog();
				    omsResUnresvCustOrderLog.setOmsCustOrdNo(omsCustOrdNo);
				    omsResUnresvCustOrderLog.setCreateTimestamp((new Timestamp(new Date().getTime())));
				    omsResUnresvCustOrderLog.setAdjQty(new BigDecimal(-(omsCustOrdReserveItem.getRmsResvQty().intValueExact())));
				    omsResUnresvCustOrderLog.setLocation(new BigDecimal((omsCustOrdReserveItem.getRmsResvLoc().intValue())));
				    omsResUnresvCustOrderLog.setItem(omsCustOrdReserveItem.getItem());
				    omsResUnresvCustOrderLog.setStatus("N");
				    try
				    {
					log.info(" omscustOrdNo"+omsCustOrdNo+
						 "Insert the record intoomsResUnresvCustOrderLog table ");
					session.persistOmsResUnresvCustOrderLog(omsResUnresvCustOrderLog);
				    }
				    catch(Exception e1)
				    {
					log.info(e1);
					log.info(" omscustOrdNo"+omsCustOrdNo+
						 "error occuured  record into omsResUnresvCustOrderLog table ");
				    }
				}
			    }
			}
		    }
		    catch(Exception e)
		    {
			log.error(e);
		    }
		    finally
		    {
			try
			{
			    pstmt.close();
			    con.close();
			}
			catch(SQLException e)
			{
			    log.info(e);
			}
		    }
		}
	    }
	}
	log.info("<---------omscustord No ------------->"+omsCustOrdNo+"------>");
	log.info("<---------End  of the RMS package call started-------------->");
    }

    public void updateOmsCustordResv(BigDecimal rmsResvLoc,String rmsResvLocType,BigDecimal loc,String locType,
				     BigDecimal fulOrdNo,BigDecimal omsCustOrdno) throws SOAPException
    {
	log.info("***Start updateOmsCustordResv***");
	Connection conn=null;
	PreparedStatement preparedStatement=null;
	log.info("rmsResvLoc "+rmsResvLoc);
	log.info("fulOrdNo "+fulOrdNo);
	log.info("omsCustOrdno "+omsCustOrdno);
	String query=
		   "UPDATE oms_cust_ord_reserve set rms_resv_loc= ? , rms_resv_loc_type= ?, loc= ? ,loc_type= ? where fulfill_order_no= ? and oms_cust_ord_no= ?";

	try
	{
	    conn=OMSUtil.createDBConnection(OMSConstants.DS_OMS_STRING);
	    log.info("Before connectoion establish");
	    preparedStatement=conn.prepareStatement(query);
	    log.info("After connectoion establish");
	    preparedStatement.setInt(1,(rmsResvLoc.intValue()));
	    preparedStatement.setString(2,(rmsResvLocType));
	    preparedStatement.setInt(3,(loc.intValue()));
	    preparedStatement.setString(4,(locType));
	    preparedStatement.setInt(5,fulOrdNo.intValue());
	    preparedStatement.setBigDecimal(6,omsCustOrdno);
	    log.info("passing query ");
	    preparedStatement.executeUpdate();
	    log.info("Updated");
	}
	catch(Exception e)
	{
	    log.info(e.getMessage());
	}
	finally
	{
	    try
	    {
		preparedStatement.close();
		conn.close();
	    }
	    catch(SQLException e)
	    {
		e.printStackTrace();
	    }
	}
    }
}
