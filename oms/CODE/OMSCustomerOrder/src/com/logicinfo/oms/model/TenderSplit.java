package com.logicinfo.oms.model;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;

import javax.xml.soap.SOAPException;

import org.apache.log4j.Logger;

import com.logicinfo.oms.ejb.OMSUtilSessionEJB;
import com.logicinfo.oms.ejb.OmsCustOrdItem;
import com.logicinfo.oms.ejb.OmsCustOrdTender;
import com.logicinfo.oms.ejb.OmsOrdItemTenderSplit;
import com.logicinfo.oms.util.OMSConstants;
import com.logicinfo.oms.util.OMSUtil;

public class TenderSplit {
    public TenderSplit() {
        super();
    }
    private final static Logger log = Logger.getLogger(TenderSplit.class.getName());

    public void tenderSplit(BigDecimal omsCustOrdNo) throws SOAPException {
        log.info("Tender Split started");
        OMSUtilSessionEJB session = OMSUtil.doLookup();
        OmsCustOrdTender omsCustOrdTender = session.getOmsCustOrdTenderFindByTenderType(omsCustOrdNo, "VOUCH");
        BigDecimal totalCredit = BigDecimal.ZERO;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
		ResultSet rs = null;
        try {
        	connection = OMSUtil.createDBConnection(OMSConstants.DS_OMS_STRING);
        	preparedStatement = connection.prepareStatement("SELECT SUM (O.TENDER_AMT) FROM OMS_CUST_ORD_TENDER O WHERE O.OMS_CUST_ORD_NO= ? AND O.TENDER_TYPE_GROUP=?");
        	preparedStatement.setBigDecimal(1, omsCustOrdNo);
        	preparedStatement.setString(2, "VOUCH");
        	
			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				totalCredit = rs.getBigDecimal(1);
			}
        } catch (Exception e) {
			log.error("Error while fetching the tender amount", e);
			throw new SOAPException(e);
		} finally {
			try {
				OMSUtil.closeDBConnection(connection, preparedStatement, rs);
			} catch (Exception e) {
				log.warn("Error while closing the connection", e);
			}
		}
        		
        log.info("totalCredit" + totalCredit);
        BigDecimal tenderSpread = new BigDecimal(0);
        int count = 0;
        while (totalCredit.compareTo(new BigDecimal(0)) > 0) {
            if (count > 0) {
                log.info("Function returning to the caller function");
                return;
            }
            for (OmsCustOrdItem item : session.getOmsCustOrdItemFindByOmsCustOrdNo(omsCustOrdNo)) {
                BigDecimal UnitDiscount = getUnitDiscountAmountOfanItem(omsCustOrdNo, item.getLineNo());
                log.info(" Unit Discount Amount " + UnitDiscount);
                if(null== UnitDiscount)
                UnitDiscount= new BigDecimal(0);
                BigDecimal UnitRetailAfterDiscount = item.getUnitRetail().subtract(UnitDiscount);
                log.info("UnitRetailAfterDiscount " + UnitRetailAfterDiscount);
                log.info("totalCredit.compareTo(UnitRetailAfterDiscount.multiply(item.getQtyOrderedSuom()))" +
                         totalCredit.compareTo(UnitRetailAfterDiscount.multiply(item.getQtyOrderedSuom())));
                if (totalCredit.compareTo(UnitRetailAfterDiscount.multiply(item.getQtyOrderedSuom())) > 0) {
                    tenderSpread = totalCredit.subtract(UnitRetailAfterDiscount).multiply(item.getQtyOrderedSuom());
                }else {
                    tenderSpread = UnitRetailAfterDiscount;
                }
                log.info("tenderSpread" + tenderSpread + "-----");
                log.info("tenderSpread=" + tenderSpread + "for item=" + item.getItem());
                OmsOrdItemTenderSplit omsOrdItemTenderSplit = new OmsOrdItemTenderSplit();
                omsOrdItemTenderSplit.setTenderSeqNo(omsCustOrdTender.getTenderSeqNo());
                omsOrdItemTenderSplit.setOmsCustOrdNo(omsCustOrdNo);
                omsOrdItemTenderSplit.setItem(item.getItem());
                omsOrdItemTenderSplit.setLineNo(item.getLineNo());
                omsOrdItemTenderSplit.setTenderSpread(tenderSpread);
                omsOrdItemTenderSplit.setCreateDatetime(new Timestamp(new Date().getTime()));
                omsOrdItemTenderSplit.setCreatedBy("OMSUSER");
                session.persistOmsOrdItemTenderSplit(omsOrdItemTenderSplit);
                totalCredit = totalCredit.subtract(tenderSpread);
                log.info("totalCredit=" + totalCredit);
            }
            count++;
        }
    }

    private BigDecimal getUnitDiscountAmountOfanItem(BigDecimal omsCustOrdNo, BigDecimal lineNo) throws SOAPException {
        OMSUtilSessionEJB session = OMSUtil.doLookup();
        BigDecimal unitDiscountAmount =
            session.getOmsCustOrdItemDiscfindByOmsCustordNoLineNoUnitDiscnt(omsCustOrdNo, lineNo);
        return unitDiscountAmount;
    }
}
