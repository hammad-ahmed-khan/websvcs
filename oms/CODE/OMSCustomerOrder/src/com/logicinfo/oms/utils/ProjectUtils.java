package com.logicinfo.oms.utils;

import java.math.BigDecimal;

import org.apache.log4j.Logger;


public class ProjectUtils {
    public ProjectUtils() {
        super();
    }
    private final static Logger log =  Logger.getLogger(com.logicinfo.oms.utils.ProjectUtils.class.getName());
    private static BigDecimal combinationID;
    private static BigDecimal custOrdHeadSeqNo;


     public static void setCombinationID(BigDecimal combinationID) {
        ProjectUtils.combinationID = combinationID;
        log.info("CombId in POJO :" + ProjectUtils.combinationID);
    }

    public static BigDecimal getCombinationID() {
        return combinationID;
    }

  /*  public static void setCustOrdHeadSeqNo(BigDecimal custOrdHeadSeqNo) {
        ProjectUtils.custOrdHeadSeqNo = custOrdHeadSeqNo;
       log.info("custOrdHeadSeqNo in POJO :" + ProjectUtils.custOrdHeadSeqNo);
    }

    public static BigDecimal getCustOrdHeadSeqNo() {
        return custOrdHeadSeqNo;
    } */

    public static Logger getLog() {
        return log;
    }
}
