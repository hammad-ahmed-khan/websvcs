package com.logicinfo.oms.model;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * <p>
 * Java class for customerOrderCancellation complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 *
 * <pre>
 * &lt;complexType name="customerOrderCancellation">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="entity_id">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="30"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="application_id">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="30"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="comments" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="240"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="request_datetimestamp" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="customer_order_no">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedInt">
 *               &lt;minExclusive value="0"/>
 *               &lt;totalDigits value="30"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="cancellation_id">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedInt">
 *               &lt;minExclusive value="0"/>
 *               &lt;totalDigits value="30"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="cancellation_date" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="cancellation_items" type="{http://com.logicinfo.oms/model/}customerOrderCancellationItems" maxOccurs="100"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "customerOrderCancellation", propOrder = { "entityId", "applicationId", "comments", "cancellationRequestorId", "requestDatetimestamp", "custOrderNo", "subCustOrderNo",
		"cancellationId", "refundPreference", "refundAmount", "cancellationDate", "cancellationItems" })
public class CustomerOrderCancellation {
	@XmlElement(name = "entity_id", required = true)
	protected String entityId;
	@XmlElement(name = "application_id", required = true)
	protected String applicationId;
	protected String comments;
	@XmlElement(name = "request_datetimestamp", required = true)
	@XmlSchemaType(name = "dateTime")
	protected XMLGregorianCalendar requestDatetimestamp;
	@XmlElement(name = "cancellation_id", required = true)
	protected String cancellationId;
	@XmlElement(name = "cancellation_date", required = true)
	@XmlSchemaType(name = "date")
	protected XMLGregorianCalendar cancellationDate;
	@XmlElement(name = "cancellation_items", required = true)
	protected List<CustomerOrderCancellationItems> cancellationItems;
	@XmlElement(name = "cust_order_no", required = true)
	protected String custOrderNo;
	@XmlElement(name = "sub_cust_order_no")
	protected String subCustOrderNo;
	@XmlElement(name = "refund_amount", required = true)
	protected BigDecimal refundAmount;
	@XmlElement(name = "refund_preference", required = true)
	protected String refundPreference;
	@XmlElement(name = "cancellation_requestor_id", required = true)
	protected String cancellationRequestorId;

	/**
	 * Gets the value of the entityId property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getEntityId() {
		return entityId;
	}

	/**
	 * Sets the value of the entityId property.
	 *
	 * @param value allowed object is {@link String }
	 *
	 */
	public void setEntityId(String value) {
		this.entityId = value;
	}

	/**
	 * Gets the value of the applicationId property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getApplicationId() {
		return applicationId;
	}

	/**
	 * Sets the value of the applicationId property.
	 *
	 * @param value allowed object is {@link String }
	 *
	 */
	public void setApplicationId(String value) {
		this.applicationId = value;
	}

	/**
	 * Gets the value of the comments property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getComments() {
		return comments;
	}

	/**
	 * Sets the value of the comments property.
	 *
	 * @param value allowed object is {@link String }
	 *
	 */
	public void setComments(String value) {
		this.comments = value;
	}

	/**
	 * Gets the value of the requestDatetimestamp property.
	 *
	 * @return possible object is {@link XMLGregorianCalendar }
	 *
	 */
	public XMLGregorianCalendar getRequestDatetimestamp() {
		return requestDatetimestamp;
	}

	/**
	 * Sets the value of the requestDatetimestamp property.
	 *
	 * @param value allowed object is {@link XMLGregorianCalendar }
	 *
	 */
	public void setRequestDatetimestamp(XMLGregorianCalendar value) {
		this.requestDatetimestamp = value;
	}

	/**
	 * Gets the value of the cancellationId property.
	 *
	 */
	public String getCancellationId() {
		return cancellationId;
	}

	/**
	 * Sets the value of the cancellationId property.
	 *
	 */
	public void setCancellationId(String value) {
		this.cancellationId = value;
	}

	/**
	 * Gets the value of the cancellationDate property.
	 *
	 * @return possible object is {@link XMLGregorianCalendar }
	 *
	 */
	public XMLGregorianCalendar getCancellationDate() {
		return cancellationDate;
	}

	/**
	 * Sets the value of the cancellationDate property.
	 *
	 * @param value allowed object is {@link XMLGregorianCalendar }
	 *
	 */
	public void setCancellationDate(XMLGregorianCalendar value) {
		this.cancellationDate = value;
	}

	/**
	 * Gets the value of the cancellationItems property.
	 *
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot.
	 * Therefore any modification you make to the returned list will be present
	 * inside the JAXB object. This is why there is not a <CODE>set</CODE> method
	 * for the cancellationItems property.
	 *
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getCancellationItems().add(newItem);
	 * </pre>
	 *
	 *
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link CustomerOrderCancellationItems }
	 *
	 *
	 */
	public List<CustomerOrderCancellationItems> getCancellationItems() {
		if (cancellationItems == null) {
			cancellationItems = new ArrayList<CustomerOrderCancellationItems>();
		}
		return this.cancellationItems;
	}

	/**
	 * Gets the value of the custOrderNo property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getCustOrderNo() {
		return custOrderNo;
	}

	/**
	 * Sets the value of the custOrderNo property.
	 *
	 * @param value allowed object is {@link String }
	 *
	 */
	public void setCustOrderNo(String value) {
		this.custOrderNo = value;
	}

	/**
	 * Gets the value of the subCustOrderNo property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getSubCustOrderNo() {
		return subCustOrderNo;
	}

	/**
	 * Sets the value of the subCustOrderNo property.
	 *
	 * @param value allowed object is {@link String }
	 *
	 */
	public void setSubCustOrderNo(String value) {
		this.subCustOrderNo = value;
	}

	/**
	 * Gets the value of the refundAmount property.
	 *
	 * @return possible object is {@link BigDecimal }
	 *
	 */
	public BigDecimal getRefundAmount() {
		return refundAmount;
	}

	/**
	 * Gets the value of the refundPreference property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getRefundPreference() {
		return refundPreference;
	}

	/**
	 * Sets the value of the refundAmount property.
	 *
	 * @param value allowed object is {@link BigDecimal }
	 *
	 */
	public void setRefundAmount(BigDecimal value) {
		this.refundAmount = value;
	}

	/**
	 * Sets the value of the refundPreference property.
	 *
	 * @param value allowed object is {@link String }
	 *
	 */
	public void setRefundPreference(String value) {
		this.refundPreference = value;
	}

	/**
	 * Gets the value of the cancellationRequestorId property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getCancellationRequestorId() {
		return cancellationRequestorId;
	}

	/**
	 * Sets the value of the cancellationRequestorId property.
	 *
	 * @param value allowed object is {@link String }
	 *
	 */
	public void setCancellationRequestorId(String value) {
		this.cancellationRequestorId = value;
	}
}
