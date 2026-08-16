package com.extra.sim.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
public class DMShipTrailer {

	private Long transferReturnId;

	private boolean transfer;

	private String transporter;
	
	private String truckLoad;

	private String truckNo;

	private String trailerType;

	private String wayBillNo;

	private Integer noOfPallets;

	private Integer noOfQty;

	private String pickType;

	private Long shipmentBOLID;

	private String createdUser;

	private Date createdDate;

	private String updatedUser;

	private Date updatedDate;

	public Long getTransferReturnId() {
		return transferReturnId;
	}

	public void setTransferReturnId(Long transferReturnId) {
		this.transferReturnId = transferReturnId;
	}

	public String getTransporter() {
		return transporter;
	}

	public void setTransporter(String transporter) {
		this.transporter = transporter;
	}

	public String getTruckLoad() {
		return truckLoad;
	}

	public void setTruckLoad(String truckLoad) {
		this.truckLoad = truckLoad;
	}

	public String getTruckNo() {
		return truckNo;
	}

	public void setTruckNo(String truckNo) {
		this.truckNo = truckNo;
	}

	public String getTrailerType() {
		return trailerType;
	}

	public void setTrailerType(String trailerType) {
		this.trailerType = trailerType;
	}

	public String getWayBillNo() {
		return wayBillNo;
	}

	public void setWayBillNo(String wayBillNo) {
		this.wayBillNo = wayBillNo;
	}

	public Integer getNoOfPallets() {
		return noOfPallets;
	}

	public void setNoOfPallets(Integer noOfPallets) {
		this.noOfPallets = noOfPallets;
	}

	public Integer getNoOfQty() {
		return noOfQty;
	}

	public void setNoOfQty(Integer noOfQty) {
		this.noOfQty = noOfQty;
	}

	public boolean isTransfer() {
		return transfer;
	}

	public void setTransfer(boolean transfer) {
		this.transfer = transfer;
	}

	public void setShipmentBOLID(Long shipmentBOLID) {
		this.shipmentBOLID = shipmentBOLID;
	}

	public Long getShipmentBOLID() {
		return shipmentBOLID;
	}

	public String getCreatedUser() {
		return createdUser;
	}

	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getUpdatedUser() {
		return updatedUser;
	}

	public void setUpdatedUser(String updatedUser) {
		this.updatedUser = updatedUser;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getPickType() {
		return pickType;
	}

	public void setPickType(String pickType) {
		this.pickType = pickType;
	}
}
