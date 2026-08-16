package com.logicinfo.oms.model;

import com.logicinfo.oms.util.OMSUtil;

import java.math.BigDecimal;


import java.util.List;
import org.apache.log4j.Logger;

public class TransferDetails
{
    public final static Logger logger=Logger.getLogger(com.logicinfo.oms.model.TransferDetails.class.getName());

    private String tsfNo;
    private String src_loc_type;
    private String ful_loc_type;
    private BigDecimal ful_ord_no;
    private String cust_ord_no;
    private BigDecimal src_loc_id;
    private BigDecimal fulfill_loc_id;
    private BigDecimal quantity;
    private String item;

    private List<TransferDetails> transferDetailsList;


    public String getSrc_loc_type()
    {
	return src_loc_type;
    }

    public void setSrc_loc_type(String src_loc_type)
    {
	this.src_loc_type=src_loc_type;
    }

    public String getFul_loc_type()
    {
	return ful_loc_type;
    }

    public void setFul_loc_type(String ful_loc_type)
    {
	this.ful_loc_type=ful_loc_type;
    }

    public BigDecimal getFul_ord_no()
    {
	return ful_ord_no;
    }

    public void setFul_ord_no(BigDecimal ful_ord_no)
    {
	this.ful_ord_no=ful_ord_no;
    }

    public BigDecimal getQuantity()
    {
	return quantity;
    }

    public void setQuantity(BigDecimal quantity)
    {
	this.quantity=quantity;
    }

    public String getCust_ord_no()
    {
	return cust_ord_no;
    }

    public void setCust_ord_no(String cust_ord_no)
    {
	this.cust_ord_no=cust_ord_no;
    }

    public BigDecimal getSrc_loc_id()
    {
	return src_loc_id;
    }

    public void setSrc_loc_id(BigDecimal src_loc_id)
    {
	this.src_loc_id=src_loc_id;
    }

    public BigDecimal getfulfill_loc_id()
    {
	return fulfill_loc_id;
    }

    public void setfulfill_loc_id(BigDecimal fulfill_loc_id)
    {
	this.fulfill_loc_id=fulfill_loc_id;
    }


    public void setTsfNo(String tsfNo)
    {
	this.tsfNo=tsfNo;
    }

    public String getTsfNo()
    {
	return tsfNo;
    }


    public void setTransferDetailsList(List<TransferDetails> transferDetailsList)
    {
	this.transferDetailsList=transferDetailsList;
    }

    public List<TransferDetails> getTransferDetailsList()
    {
	return transferDetailsList;
    }

    public void setItem(String item)
    {
	this.item=item;
    }

    public String getItem()
    {
	return item;
    }
}
