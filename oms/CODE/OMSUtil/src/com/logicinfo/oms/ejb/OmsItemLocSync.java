package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQueries({ @NamedQuery(name = "OmsItemLocSync.findAll", query = "select o from OmsItemLocSync o"),
		@NamedQuery(name = "OmsItemLocSync.findItemLoc", query = "select o.item from OmsItemLocSync o where o.item=:item and o.loc=:loc"),
		@NamedQuery(name = "OmsItemLocSync.removeById", query = "delete from OmsItemLocSync o where o.item=:item and o.loc=:loc")})
@Table(name = "OMS_ITEM_LOC_SYNC")
@IdClass(OmsItemLocSyncPK.class)
public class OmsItemLocSync implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3598790669152501443L;

	@Column(name = "CREATE_DATETIME", nullable = false)
	private Timestamp createDatetime;
	@Id
	@Column(nullable = false, length = 25)
	private String item;
	@Id
	@Column(nullable = false)
	private BigDecimal loc;

	@Column(nullable = true, name = "CUST_ORDER_NO")
	private String orderNo;

	public OmsItemLocSync() {
	}

	public OmsItemLocSync(Timestamp createDatetime, String item, BigDecimal loc) {
		this.createDatetime = createDatetime;
		this.item = item;
		this.loc = loc;
	}

	public Timestamp getCreateDatetime() {
		return createDatetime;
	}

	public void setCreateDatetime(Timestamp createDatetime) {
		this.createDatetime = createDatetime;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public BigDecimal getLoc() {
		return loc;
	}

	public void setLoc(BigDecimal loc) {
		this.loc = loc;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

}
