package com.logicinfo.oms.model;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
/**
 * <p>Java class for customerOrderCancellationResponse complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="customerOrderCancellationResponse">
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
 *         &lt;element name="comments">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="240"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="request_datetimestamp" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="response_datetimestamp" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
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
 *         &lt;element name="response_message">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="2000"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="customerOrderResponseItems" type="{http://com.logicinfo.oms/model/}customerOrderResponseItems" maxOccurs="100" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="customerOrderCancellationResponse",
         propOrder={ "cancellationId","responseDatetimestamp","messageStatus","responseMessage","messageCode",
                     "messageDesc","customerOrderCancelResponseItems" })
public class CustomerOrderCancellationResponse
{
@XmlElement(name="response_datetimestamp",required=true,nillable=true)
@XmlSchemaType(name="dateTime")
protected XMLGregorianCalendar responseDatetimestamp;
@XmlElement(name="cancellation_id",required=true,nillable=true)
protected String cancellationId;
@XmlElement(name="message_status",required=true,nillable=true)
protected String messageStatus;
@XmlElement(name="response_message",required=true,nillable=true)
protected String responseMessage;
protected List<CustomerOrderCancelResponseItems> customerOrderCancelResponseItems;
@XmlElement(name="message_code",required=true,nillable=true)
protected String messageCode;
@XmlElement(name="message_desc",required=true,nillable=true)
protected String messageDesc;

/**
 * Gets the value of the responseDatetimestamp property.
 *
 * @return
 *     possible object is
 *     {@link XMLGregorianCalendar }
 *
 */
public XMLGregorianCalendar getResponseDatetimestamp()
{
  return responseDatetimestamp;
}

/**
 * Sets the value of the responseDatetimestamp property.
 *
 * @param value
 *     allowed object is
 *     {@link XMLGregorianCalendar }
 *
 */
public void setResponseDatetimestamp(XMLGregorianCalendar value)
{
  this.responseDatetimestamp=value;
}

/**
 * Gets the value of the cancellationId property.
 *
 * @return
 *     possible object is
 *     {@link Long }
 *
 */
public String getCancellationId()
{
  return cancellationId;
}

/**
 * Sets the value of the cancellationId property.
 *
 * @param value
 *     allowed object is
 *     {@link Long }
 *
 */
public void setCancellationId(String value)
{
  this.cancellationId=value;
}

/**
 * Gets the value of the messageStatus property.
 *
 * @return
 *     possible object is
 *     {@link String }
 *
 */
public String getMessageStatus()
{
  return messageStatus;
}

/**
 * Sets the value of the messageStatus property.
 *
 * @param value
 *     allowed object is
 *     {@link String }
 *
 */
public void setMessageStatus(String value)
{
  this.messageStatus=value;
}

/**
 * Gets the value of the responseMessage property.
 *
 * @return
 *     possible object is
 *     {@link String }
 *
 */
public String getResponseMessage()
{
  return responseMessage;
}

/**
 * Sets the value of the responseMessage property.
 *
 * @param value
 *     allowed object is
 *     {@link String }
 *
 */
public void setResponseMessage(String value)
{
  this.responseMessage=value;
}

/**
 * Gets the value of the customerOrderCancelResponseItems property.
 *
 * <p>
 * This accessor method returns a reference to the live list,
 * not a snapshot. Therefore any modification you make to the
 * returned list will be present inside the JAXB object.
 * This is why there is not a <CODE>set</CODE> method for the customerOrderCancelResponseItems property.
 *
 * <p>
 * For example, to add a new item, do as follows:
 * <pre>
 *    getCustomerOrderCancelResponseItems().add(newItem);
 * </pre>
 *
 *
 * <p>
 * Objects of the following type(s) are allowed in the list
 * {@link CustomerOrderCancelResponseItems }
 *
 *
 */
public List<CustomerOrderCancelResponseItems> getCustomerOrderCancelResponseItems()
{
  if(customerOrderCancelResponseItems==null)
  {
    customerOrderCancelResponseItems=new ArrayList<CustomerOrderCancelResponseItems>();
  }
  return this.customerOrderCancelResponseItems;
}

/**
 * Gets the value of the messageCode property.
 *
 * @return
 *     possible object is
 *     {@link String }
 *
 */
public String getMessageCode()
{
  return messageCode;
}

/**
 * Gets the value of the messageDesc property.
 *
 * @return
 *     possible object is
 *     {@link String }
 *
 */
public String getMessageDesc()
{
  return messageDesc;
}

/**
 * Sets the value of the messageCode property.
 *
 * @param value
 *     allowed object is
 *     {@link String }
 *
 */
public void setMessageCode(String value)
{
  this.messageCode=value;
}

/**
 * Sets the value of the messageDesc property.
 *
 * @param value
 *     allowed object is
 *     {@link String }
 *
 */
public void setMessageDesc(String value)
{
  this.messageDesc=value;
}
}
