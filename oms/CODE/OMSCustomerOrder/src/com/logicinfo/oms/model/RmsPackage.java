package com.logicinfo.oms.model;


import com.logicinfo.oms.ejb.OMSUtilSessionEJB;
import com.logicinfo.oms.ejb.OmsCustOrdHead;
import com.logicinfo.oms.ejb.OmsCustOrdItem;
import com.logicinfo.oms.ejb.OmsCustOrdReserve;
import com.logicinfo.oms.ejb.OmsResUnresvCustOrderLog;
import com.logicinfo.oms.ejb.OmsTempCoFo;
import com.logicinfo.oms.util.OMSUtil;

import java.math.BigDecimal;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;

import javax.sql.DataSource;

import javax.xml.soap.SOAPException;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.log4j.Logger;


public class RmsPackage {

    public final static Logger log = Logger.getLogger(com.logicinfo.oms.model.RmsPackage.class.getName());

    int numberOfRetry = 1;

    public static void persistOmsCustOrdReserve(BigDecimal custOrdHeadSeqNo,
                                                Map<BigDecimal, ArrayList<OmsTempCoFo>> fulfillDetailMap) throws SOAPException {
        log.info("***Start-persistOmsCustOrdReserve***");
        OMSUtilSessionEJB session = OMSUtil.doLookup();
        log.info("keyset" + fulfillDetailMap.keySet());
        for (BigDecimal key : fulfillDetailMap.keySet()) {
            ArrayList<OmsTempCoFo> list = fulfillDetailMap.get(key);
            for (OmsTempCoFo omsTempCoFo : list) {

                OmsCustOrdItem omsCustOrdItem =
                    session.getOmsCustOrdItemFindByItem(custOrdHeadSeqNo, omsTempCoFo.getItem(),
                                                        omsTempCoFo.getLineNo());

                log.info("omsCustOrdNo " + custOrdHeadSeqNo + "Persisting for item=" + omsTempCoFo.getItem() +
                         "order qty=" + omsTempCoFo.getOrderQty() + "Fulfill orde no" +
                         omsTempCoFo.getFulfillOrderNo());
                log.info("omsCustOrdNo" + omsTempCoFo.getOmsCustOrdNo() + "line no" + omsTempCoFo.getLineNo() +
                         "Source loc" + omsTempCoFo.getSourceLocId());
                OmsCustOrdReserve omsCustOrdReserve = null;
                boolean found = false;
                try {
                    omsCustOrdReserve =
                            session.getOmsCustOrdReserveFindByOmsCustOrdNoAndItemAndResvLoc(custOrdHeadSeqNo,
                                                                                            omsTempCoFo.getItem(),
                                                                                            omsTempCoFo.getLineNo(),
                                                                                            omsTempCoFo.getSourceLocId());
                    found = true;
                } catch (Exception e) {
                    log.info("No record in oms_cust_ord_reserve");
                }
                if (found == true) {
                    log.info("Previous value is of resv qty us" + omsCustOrdReserve.getRmsResvQty() + "New value is " +
                             omsCustOrdReserve.getRmsResvQty().add(omsTempCoFo.getOrderQty()));
                    omsCustOrdReserve.setRmsResvQty(omsCustOrdReserve.getRmsResvQty().add(omsTempCoFo.getOrderQty()));
                    omsCustOrdReserve.setQty(omsCustOrdReserve.getQty().add(omsTempCoFo.getOrderQty()));
                    session.mergeOmsCustOrdReserve(omsCustOrdReserve);
                } else {
                    log.info("omsCustOrdNo" + omsTempCoFo.getOmsCustOrdNo() +
                             "Creating new recoed in oms_cust_ord_reserve");
                    log.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                    log.info("omsCustOrdNo" + custOrdHeadSeqNo + "lineNo " + omsTempCoFo.getLineNo());
                    log.info("omsCustOrdNo" + custOrdHeadSeqNo + "item " + omsTempCoFo.getItem());
                    log.info("omsCustOrdNo" + custOrdHeadSeqNo + "fulfilOrdNo " + omsTempCoFo.getFulfillOrderNo());
                    log.info("omsCustOrdNo" + custOrdHeadSeqNo + "item " + omsTempCoFo.getOrderQty());
                    log.info("omsCustOrdNo" + custOrdHeadSeqNo + "srcLoctyp+tem" +
                             omsTempCoFo.getSourceLocationType());
                    log.info("omsCustOrdNo" + custOrdHeadSeqNo + "item " + omsTempCoFo.getSourceLocId());
                    log.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                    omsCustOrdReserve = new OmsCustOrdReserve();
                    omsCustOrdReserve.setItem(omsTempCoFo.getItem());
                    omsCustOrdReserve.setLineNo(omsTempCoFo.getLineNo());
                    omsCustOrdReserve.setLoc(omsTempCoFo.getFulfillLocId()); //fulfilloc
                    omsCustOrdReserve.setOmsCustOrdNo(custOrdHeadSeqNo); //cust head sequence
                    omsCustOrdReserve.setFulfillOrderNo(omsTempCoFo.getFulfillOrderNo());
                    omsCustOrdReserve.setQty(omsTempCoFo.getOrderQty()); //order qty
                    omsCustOrdReserve.setPaymentStatus("P");
                    omsCustOrdReserve.setResvStatus("RES");
                    omsCustOrdReserve.setLocType(omsTempCoFo.getFulfillLocationType());
                    omsCustOrdReserve.setInitiateTs(new Timestamp(new Date().getTime()));
                    omsCustOrdReserve.setRmsResvLoc(omsTempCoFo.getSourceLocId()); //source loc
                    log.info("omsCustOrdNo " + custOrdHeadSeqNo + "omsTempCoFo.getSourceLocationType() " +
                             omsTempCoFo.getSourceLocationType());
                    omsCustOrdReserve.setRmsResvLocType(omsTempCoFo.getSourceLocationType());
                    omsCustOrdReserve.setRmsResvQty(omsTempCoFo.getOrderQty()); //order qty
                    omsCustOrdReserve.setCombinationId(omsTempCoFo.getCombinationId());
                    log.info("omsTempCoFo.getVirtualWH() " + omsTempCoFo.getVirtualWH());
                    omsCustOrdReserve.setVirtualWH(omsTempCoFo.getVirtualWH());
                    omsCustOrdReserve.setCreatedBy("User");
                    omsCustOrdReserve.setCreateDatetime(new Timestamp(new Date().getTime()));
                    Date d1 = new Date();
                    log.info("omsCustOrdNo" + omsTempCoFo.getOmsCustOrdNo() +
                             "while persisting into omsCustOrdReserve " + d1);
                    session.persistOmsCustOrdReserve(omsCustOrdReserve);
                    Date d2 = new Date();
                    log.info("omsCustOrdNo" + omsTempCoFo.getOmsCustOrdNo() +
                             "after persisting into omsCustOrdReserve " + (d2.getTime() - d1.getTime()) +
                             " in milliseconds");
                    log.info("persistOmsCustOrdReserve success");
                }
            }
        }
        log.info("success");
    }

    public static Connection createConnection() {
        log.info("***Start createConnection***");
        Connection connection = null;
        try {
            Context initContext = new InitialContext();
            DataSource ds = (DataSource)initContext.lookup("jdbc/oms");
            connection = ds.getConnection();
            log.info("connected to db");
        } catch (Exception e) {
            log.info("unable to connect to database");
        }
        return connection;

    }

    void rollbackRmsPackageCall(Map<BigDecimal, ArrayList<OmsTempCoFo>> fulfillDetailMap) throws SOAPException {
        log.info("*** Begin Rollback -rmsPackageCall-started***");
        OMSUtilSessionEJB session = OMSUtil.doLookup();
        Connection con = null;
        CallableStatement pstmt = null;
        int result = 0;
        OmsResUnresvCustOrderLog omsResUnresvCustOrderLog = null;
        List<OmsResUnresvCustOrderLog> omsResUnresvCustOrderLoglist = new ArrayList<OmsResUnresvCustOrderLog>();
        for (BigDecimal key : fulfillDetailMap.keySet()) {
            ArrayList<OmsTempCoFo> list = fulfillDetailMap.get(key);
            for (OmsTempCoFo omsTempCoFo : list) {
                if (omsTempCoFo.getSourceLocationType().equals("SU") == false &&
                    "ST".equals(omsTempCoFo.getSourceLocationType()) == false &&
                    "S".equals(omsTempCoFo.getRmsErrorMsg())) {
                    try {
                        con = createConnection();
                        pstmt = con.prepareCall("{call OMS_INVADJ_STATUS_UNAVIALINV(?,?,?,?,?,?,?,?,?)}");
                        pstmt.setString(1, omsTempCoFo.getItem());
                        int i_inv_staus =
                            Integer.parseInt(session.getOmsSystemParametersFindIndValue("RESV_INV_STATUS",
                                                                                        "OMS_SYSTEM_OPTION"));
                        pstmt.setInt(2, i_inv_staus);
                        if (omsTempCoFo.getSourceLocationType().equals("ST")) {
                            pstmt.setString(3, "S");
                        } else {
                            pstmt.setString(3, "W");
                        }
                        pstmt.setInt(4, (omsTempCoFo.getSourceLocId().intValueExact()));
                        int qty = -(omsTempCoFo.getOrderQty().intValueExact());
                        pstmt.setInt(5, qty);
                        int reason_code =
                            Integer.parseInt(session.getOmsSystemParametersFindIndValue("REASON_CODE", "OMS_SYSTEM_OPTION"));
                        pstmt.setInt(6, reason_code);
                        OmsCustOrdHead OmsCustOrdHead =
                            session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsTempCoFo.getOmsCustOrdNo());
                        pstmt.setString(7, OmsCustOrdHead.getCustOrderNo() + "_R::");
                        // R:: Order create Order Rollback ....
                        pstmt.registerOutParameter(8, Types.INTEGER);
                        pstmt.registerOutParameter(9, Types.VARCHAR);

                        log.info(" Before calling the packageOmsCustOrdNo--" + omsTempCoFo.getOmsCustOrdNo());
                        for (int i = 1; i <= 10; i++) {
                            try {
                                pstmt.executeUpdate();
                                result = pstmt.getInt(8);
                                String err_msg = pstmt.getString(9);
                                log.info("result is " + result + "err_msg" + err_msg);
                                if (result != 1) {
                                    if (i == 10) {
                                        throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("SYSTEM_ERROR"));
                                    }
                                } else {
                                    log.info("Success Package call happened at Attempt no omsCustOrdNo is " +
                                             omsTempCoFo.getOmsCustOrdNo() + "Attempt no" + (i + 1));
                                    break;
                                }
                            } catch (Exception e) {
                                log.error("Error occured while calling the Package");
                                if (i == 10) {
                                    log.info("-- Inserting the record into omsCustResunresvCustorderlog table----" +
                                             omsTempCoFo.getOmsCustOrdNo());
                                    omsResUnresvCustOrderLog = new OmsResUnresvCustOrderLog();
                                    omsResUnresvCustOrderLog.setAdjQty(new BigDecimal(-(omsTempCoFo.getOrderQty().intValueExact())));
                                    omsResUnresvCustOrderLog.setCreateTimestamp(new Timestamp(new Date().getTime()));
                                    omsResUnresvCustOrderLog.setItem(omsTempCoFo.getItem());
                                    omsResUnresvCustOrderLog.setLocation(new BigDecimal(omsTempCoFo.getSourceLocId().intValueExact()));
                                    omsResUnresvCustOrderLog.setOmsCustOrdNo(omsTempCoFo.getOmsCustOrdNo());
                                    omsResUnresvCustOrderLog.setStatus("N");
                                    try {
                                        log.info("--calling perisist method record into omsCustResunresvCustorderlog table----" +
                                                 omsTempCoFo.getOmsCustOrdNo());
                                        session.persistOmsResUnresvCustOrderLog(omsResUnresvCustOrderLog);
                                    } catch (Exception e1) {
                                        log.info(e1);
                                        log.info("-- Error in inserting the record into omsCustResunresvCustorderlog table----" +
                                                 omsTempCoFo.getOmsCustOrdNo());
                                        throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("SYSTEM_ERROR"));
                                    }
                                    omsResUnresvCustOrderLoglist.add(omsResUnresvCustOrderLog);
                                }
                            }
                        }
                        log.info("Sucess----Come out from the loop-------------");
                    } catch (Exception e) {
                        log.error(e);
                    } finally {
                        try {
                            pstmt.close();
                            con.close();
                        } catch (SQLException e) {
                            log.error(e);
                        }
                    }
                }
            }
        }
        log.info("------------End of Rollback rmsPackageCall-started-----------------------");
    }

    void rmsPackageCall(Map<BigDecimal, ArrayList<OmsTempCoFo>> fulfillDetailMap) throws SOAPException {
        log.info("----------------------Begin of rmsPackageCall method--------------------");
        OMSUtilSessionEJB session = OMSUtil.doLookup();
        Connection con = null;
        CallableStatement pstmt = null;
        int result = 0;
        for (BigDecimal key : fulfillDetailMap.keySet()) {
            ArrayList<OmsTempCoFo> list = fulfillDetailMap.get(key);
            for (OmsTempCoFo omsTempCoFo : list) {
                if (omsTempCoFo.getSourceLocationType().equals("SU") == false &&
                    omsTempCoFo.getSourceLocationType().equals("ST") == false) {
                    try {
                        con = createConnection();
                        pstmt = con.prepareCall("{call OMS_INVADJ_STATUS_UNAVIALINV(?,?,?,?,?,?,?,?,?)}");
                        pstmt.setString(1, omsTempCoFo.getItem());
                        int i_inv_staus =
                            Integer.parseInt(session.getOmsSystemParametersFindIndValue("RESV_INV_STATUS",
                                                                                        "OMS_SYSTEM_OPTION"));
                        pstmt.setInt(2, i_inv_staus);
                        if (omsTempCoFo.getSourceLocationType().equals("ST")) {
                            pstmt.setString(3, "S");
                        } else {
                            pstmt.setString(3, "W");
                        }
                        pstmt.setInt(4, (omsTempCoFo.getSourceLocId().intValueExact()));
                        pstmt.setInt(5, omsTempCoFo.getOrderQty().intValueExact());
                        int reason_code =
                            Integer.parseInt(session.getOmsSystemParametersFindIndValue("REASON_CODE", "OMS_SYSTEM_OPTION"));
                        pstmt.setInt(6, reason_code);
                        OmsCustOrdHead omsCustOrdHead =
                            session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsTempCoFo.getOmsCustOrdNo());
                        pstmt.setString(7, omsCustOrdHead.getCustOrderNo() + "_R");
                        // _R Create the Order creation time...
                        pstmt.registerOutParameter(8, Types.INTEGER);
                        pstmt.registerOutParameter(9, Types.VARCHAR);

                        for (int i = 1; i <= 10; i++) {
                            try {
                                pstmt.executeUpdate();
                                result = pstmt.getInt(8);
                                String err_msg = pstmt.getString(9);
                                log.info("result" + result + "---" + err_msg);
                                // There are two possbilites in this after calling the  package.Package error  and package returns 0
                                if (result != 1) {
                                    if (i == 10) {
                                        log.info(omsTempCoFo.getOmsCustOrdNo()+" Retried 10 times to call rms package, so throwing system error");
                                        throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("SYSTEM_ERROR"));
                                    }
                                } else {
                                    log.info("Success Package call happened at Attempt no omsCustOrdNo is " + omsTempCoFo.getOmsCustOrdNo() + "Attempt no" + (i + 1));
                                    break;
                                }
                                omsTempCoFo.setRmsErrorMsg("S");
                            } catch (Exception ex) {
                                throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("SYSTEM_ERROR"));
                            }
                        }
                    } catch (Exception e) {
                        omsTempCoFo.setRmsErrorMsg("F");
                        rollbackRmsPackageCall(fulfillDetailMap);
                        OmsCustOrdHead omsCustOrdHead =
                            session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsTempCoFo.getOmsCustOrdNo());
                        omsCustOrdHead.setStatus("F");
                        session.mergeOmsCustOrdHead(omsCustOrdHead);
                        throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("SYSTEM_ERROR"));
                    } finally {
                        try {
                            pstmt.close();
                            con.close();
                        } catch (SQLException e) {
                            log.error(e);
                        }
                    }
                }
            } //else end
        } //for ends
        log.info("----------------------End of rmsPackageCall method--------------------");
    }
}
