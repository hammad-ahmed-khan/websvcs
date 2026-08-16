package com.logicinfo.oms.model;

import com.logicinfo.oms.ejb.OMSUtilSessionEJB;
import com.logicinfo.oms.ejb.OmsCoFulfillDetail;
import com.logicinfo.oms.util.OMSUtil;

import java.math.BigDecimal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;

import javax.xml.soap.SOAPException;

public class DCtoDCTransfer
{
    public final static Logger logger=Logger.getLogger(com.logicinfo.oms.model.DCtoDCTransfer.class.getName());
    List<TransferDetails> transferDetailsList=new ArrayList<TransferDetails>();

    public List<TransferDetails> getFulFillOrderNoFromOrdCust(String customerOrderNo)
    {
	Connection conn=null;
	PreparedStatement pstmt=null;
	ResultSet rs=null;

	logger.info(" customerOrderNo "+customerOrderNo);

	String transferQuery=
		   "select h.TSF_NO,FULFILL_ORDER_NO,d.item,SOURCE_LOC_TYPE,SOURCE_LOC_ID,FULFILL_LOC_TYPE,FULFILL_LOC_ID,QTY_ORDERED_SUOM from "+
		   "ordcust h,ordcust_detail d,tsfhead t where  h.ORDCUST_NO=d.ORDCUST_NO and h.CUSTOMER_ORDER_NO="+"'"+
		   customerOrderNo+"'"+" and h.TSF_NO=t.TSF_NO ";

	logger.info("transferQuery "+transferQuery);

	try
	{
	    conn=OMSUtil.createDBConnection("jdbc/oms");
	    pstmt=conn.prepareStatement(transferQuery);
	    rs=pstmt.executeQuery();

	    while(rs.next())
	    {
		if(rs.getString("TSF_NO")!=null&&rs.getBigDecimal("FULFILL_ORDER_NO")!=null&&rs.getString("item")!=null&&
		   rs.getString("SOURCE_LOC_TYPE")!=null&&rs.getBigDecimal("SOURCE_LOC_ID")!=null&&
		   rs.getString("FULFILL_LOC_TYPE")!=null&&rs.getBigDecimal("FULFILL_LOC_ID")!=null&&
		   rs.getBigDecimal("QTY_ORDERED_SUOM")!=null&&rs.getBigDecimal("QTY_ORDERED_SUOM").intValue()>0)
		{
		    TransferDetails transferDetails=new TransferDetails();
		    transferDetails.setTsfNo(rs.getString("TSF_NO"));
		    transferDetails.setItem(rs.getString("item"));
		    transferDetails.setSrc_loc_id(rs.getBigDecimal("SOURCE_LOC_ID"));
		    transferDetails.setSrc_loc_type(rs.getString("SOURCE_LOC_TYPE"));
		    transferDetails.setfulfill_loc_id(rs.getBigDecimal("FULFILL_LOC_ID"));
		    transferDetails.setFul_loc_type(rs.getString("FULFILL_LOC_TYPE"));
		    transferDetails.setFul_ord_no(rs.getBigDecimal("FULFILL_ORDER_NO"));
		    transferDetails.setQuantity(rs.getBigDecimal("QTY_ORDERED_SUOM"));

		    logger.info("==============TransferDetails  ================");
		    logger.info("TsfNo "+rs.getString("TSF_NO"));
		    logger.info("Item "+rs.getString("item"));
		    logger.info("Src_loc_id "+rs.getBigDecimal("SOURCE_LOC_ID"));
		    logger.info("Src_loc_type "+rs.getString("SOURCE_LOC_TYPE"));
		    logger.info("fulfill_loc_id "+rs.getBigDecimal("FULFILL_LOC_ID"));
		    logger.info("Ful_loc_type "+rs.getString("FULFILL_LOC_TYPE"));
		    logger.info("Ful_ord_no "+rs.getBigDecimal("FULFILL_ORDER_NO"));
		    logger.info("Quantity "+rs.getBigDecimal("QTY_ORDERED_SUOM"));

		    transferDetailsList.add(transferDetails);
		}
	    }

	    logger.info("transferDetailsList size "+transferDetailsList.size());
	}
	catch(Exception e)
	{
	    logger.info("Exception while retriving the data from ordcust for DC to DC "+e.getMessage());
	}
	finally
	{
	    closeDBConnection(conn,pstmt,rs);
	}
	return transferDetailsList;
    }

    private void closeDBConnection(Connection conn,PreparedStatement pstmt,ResultSet rs)
    {
	try
	{
	    if(conn!=null)
	    {
		conn.close();
	    }
	    if(pstmt!=null)
	    {
		pstmt.close();
	    }
	    if(rs!=null)
	    {
		rs.close();
	    }
	}
	catch(Exception e)
	{
	    logger.info("Exception occured while closing the connection "+e.getMessage());

	}

    }

    public void updateTsfNoandFulFillOrdNo(BigDecimal omsCustOrdNo,String customerOrderNo) throws Exception
    {
	logger.info(omsCustOrdNo+" inside updateTsfNoandFulFillOrdNo ");
	OMSUtilSessionEJB session=OMSUtil.doLookup();
	List<OmsCoFulfillDetail> omsCoFulfillDetailList=session.getOmsCoFulfillDetailFindByOmsCustOrdNo(omsCustOrdNo);
	logger.info(omsCustOrdNo+" omsCustOrdNo for omsCoFulfillDetail  size "+omsCoFulfillDetailList.size());
	List<TransferDetails> transferDetailsList=getFulFillOrderNoFromOrdCust(customerOrderNo);

	if(omsCoFulfillDetailList!=null&&omsCoFulfillDetailList.size()>0&&transferDetailsList!=null&&
	   transferDetailsList.size()>0)
	{
	    for(OmsCoFulfillDetail omsCoFulfillDetail:omsCoFulfillDetailList)
	    {
		for(TransferDetails transferDetails:transferDetailsList)
		{
		    logger.info(omsCustOrdNo+"==============omsCoFulfillDetail for transfer and fullfillorder update ================");
		    logger.info(omsCustOrdNo+"omsCoFulfillDetail.getFulfillOrderNo() "+omsCoFulfillDetail.getFulfillOrderNo());
		    logger.info(omsCustOrdNo+"omsCoFulfillDetail.getItem() "+omsCoFulfillDetail.getItem());
		    logger.info(omsCustOrdNo+"omsCoFulfillDetail.getSourceLocType() "+omsCoFulfillDetail.getSourceLocType());
		    logger.info(omsCustOrdNo+"omsCoFulfillDetail.getSourceLoc() "+omsCoFulfillDetail.getSourceLoc());
		    logger.info(omsCustOrdNo+"omsCoFulfillDetail.getFulfillLoc() "+omsCoFulfillDetail.getFulfillLoc());
		    logger.info(omsCustOrdNo+"omsCoFulfillDetail.getFulfillLocType() "+omsCoFulfillDetail.getFulfillLocType());
		    logger.info(omsCustOrdNo+"omsCoFulfillDetail.getFulfillConfQty() "+omsCoFulfillDetail.getFulfillConfQty());
		    logger.info(omsCustOrdNo+"omsCoFulfillDetail.getTsfNo() "+omsCoFulfillDetail.getTsfNo());
		    if(omsCoFulfillDetail.getTsfNo()==null&&omsCoFulfillDetail.getItem().equals(transferDetails.getItem())&&
		       omsCoFulfillDetail.getSourceLocType().equalsIgnoreCase(transferDetails.getSrc_loc_type())&&
		       omsCoFulfillDetail.getSourceLoc().intValue()==transferDetails.getSrc_loc_id().intValue()&&
		       omsCoFulfillDetail.getFulfillLoc().intValue()==transferDetails.getfulfill_loc_id().intValue()&&
		       omsCoFulfillDetail.getFulfillLocType().equalsIgnoreCase(transferDetails.getFul_loc_type())&&
		       omsCoFulfillDetail.getFulfillConfQty().intValue()==transferDetails.getQuantity().intValue()&&
		       omsCoFulfillDetail.getFulfillOrderNo().intValue()!=transferDetails.getFul_ord_no().intValue())
		    {
			logger.info(omsCustOrdNo+"TSFNo is null in omsCoFulfillDetailtable ");
			omsCoFulfillDetail.setTsfNo(new BigDecimal(transferDetails.getTsfNo()));
			session.mergeOmsCoFulfillDetail(omsCoFulfillDetail);
			updateFulFillOrderNo(omsCustOrdNo,transferDetails);
			logger.info(omsCustOrdNo+"updated the tsfno for lineNo "+omsCoFulfillDetail.getLineNo());
		    }
		}
	    }

	}
    }

    public void updateFulFillOrderNo(BigDecimal omsCustOrdNo,TransferDetails transferDetails) throws SOAPException
    {

	String query=" update oms_co_fulfill_detail set FULFILL_ORDER_NO="+transferDetails.getFul_ord_no()+" where TSF_NO is not null and TSF_NO="+transferDetails.getTsfNo();
	logger.info(omsCustOrdNo +"query "+query);
	Connection conn=null;
	PreparedStatement pstmt=null;
	try
	{
	    conn=OMSUtil.createDBConnection("jdbc/oms");
	    pstmt=conn.prepareStatement(query);
	    int i=pstmt.executeUpdate();
	    logger.info(omsCustOrdNo+"updated the table "+i);
	}
	catch(Exception e)
	{
	    logger.info(omsCustOrdNo+"Exception while updating the oms_co_fulfill_detail "+e.getMessage());
	}


    }
}
