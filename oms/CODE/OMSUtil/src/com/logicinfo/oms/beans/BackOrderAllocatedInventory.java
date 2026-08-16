package com.logicinfo.oms.beans;


import com.logicinfo.oms.util.OMSConstants;
import com.logicinfo.oms.util.OMSUtil;

import java.math.BigDecimal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;


public class BackOrderAllocatedInventory {
    private final static Logger log = Logger.getLogger(BackOrderAllocatedInventory.class.getName());
    private final Object lock = new Object();

    public BackOrderAllocatedInventory() {
        super();
    }


    public BigDecimal getAllocatedInventoryfromBackOrderDTL(BigDecimal location, String item) throws Exception {
        log.info("inside getAllocatedInventoryfromBackOrderDTL");
        BigDecimal allocatedInventory = BigDecimal.ZERO;
        log.info("location " + location);
        log.info("item " + item);
        log.info("Thread.current thread " + Thread.currentThread().getName());
        String query =
            " select SUM(SOURCE_QTY-FULFILL_QTY) from oms_back_order_dtl  where SOURCE_LOC=? and ITEM=? and  BACKORDER_STATUS='N' ";
        log.info("query " + query);
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            conn = OMSUtil.createDBConnection(OMSConstants.DS_OMS_STRING);
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setBigDecimal(1, location);
            preparedStatement.setString(2, item);
            rs = preparedStatement.executeQuery();

            while (rs.next()) {
                log.info("equal");
                allocatedInventory = rs.getBigDecimal(1);
                if (allocatedInventory == null) {
                    log.info("allocatedInventory is null ");
                    allocatedInventory = BigDecimal.ZERO;
                }
                log.info(" allocatedInventory from query " + allocatedInventory);
            }
        } catch (Exception e1) {
            log.info(e1.getMessage());
            log.info("e1 " + e1);
        } finally {
            try {
                OMSUtil.closeDBConnection(conn, preparedStatement, rs);
            } catch (Exception e2) {
                log.error(e2.getMessage());

            }
        }
        log.info("allocatedInventory while returning " + allocatedInventory);
        return allocatedInventory;
    }
}
