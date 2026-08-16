package oms.logicinfo.com.model;
import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
/**
 * <p>Java class for customerOrderResponseItems complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="customerOrderResponseItems">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="line_no">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedInt">
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="item">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="25"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="order_qty_suom">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;minExclusive value="0"/>
 *               &lt;fractionDigits value="04"/>
 *               &lt;totalDigits value="12"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="fulfill_qty_suom">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;fractionDigits value="04"/>
 *               &lt;totalDigits value="12"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="available_qty">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;fractionDigits value="04"/>
 *               &lt;totalDigits value="12"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="status" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="AVAILABLE"/>
 *               &lt;enumeration value="FAILED"/>
 *               &lt;enumeration value="UNAVAILABLE"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="status_message" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="200"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="customerOrderResponseItemFulfillment" type="{http://com.logicinfo.oms/model/}customerOrderResponseItemFulfillment" maxOccurs="100" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="customerOrderResponseItems",
         propOrder={ "lineNo","item","orderQtySuom","fulfillQtySuom","availableQty","status","statusMessage",
                     "customerOrderResponseItemFulfillment" })
public class CustomerOrderResponseItems
{
@XmlElement(name="line_no")
protected long lineNo;
@XmlElement(required=true,nillable=true)
protected String item;
@XmlElement(name="order_qty_suom",required=true,nillable=true)
protected BigDecimal orderQtySuom;
@XmlElement(name="fulfill_qty_suom",required=true,nillable=true)
protected BigDecimal fulfillQtySuom;
@XmlElement(name="available_qty",required=true,nillable=true)
protected BigDecimal availableQty;
protected String status;
@XmlElement(name="status_message")
protected String statusMessage;
@XmlElement(nillable=true)
protected List<CustomerOrderResponseItemFulfillment> customerOrderResponseItemFulfillment;

/**
 * Gets the value of the lineNo property.
 *
 */
public long getLineNo()
{
  return lineNo;
}

/**
 * Sets the value of the lineNo property.
 *
 */
public void setLineNo(long value)
{
  this.lineNo=value;
}

/**
 * Gets the value of the item property.
 *
 * @return
 *     possible object is
 *     {@link String }
 *
 */
public String getItem()
{
  return item;
}

/**
 * Sets the value of the item property.
 *
 * @param value
 *     allowed object is
 *     {@link String }
 *
 */
public void setItem(String value)
{
  this.item=value;
}

/**
 * Gets the value of the orderQtySuom property.
 *
 * @return
 *     possible object is
 *     {@link BigDecimal }
 *
 */
public BigDecimal getOrderQtySuom()
{
  return orderQtySuom;
}

/**
 * Sets the value of the orderQtySuom property.
 *
 * @param value
 *     allowed object is
 *     {@link BigDecimal }
 *
 */
public void setOrderQtySuom(BigDecimal value)
{
  this.orderQtySuom=value;
}

/**
 * Gets the value of the fulfillQtySuom property.
 *
 * @return
 *     possible object is
 *     {@link BigDecimal }
 *
 */
public BigDecimal getFulfillQtySuom()
{
  return fulfillQtySuom;
}

/**
 * Sets the value of the fulfillQtySuom property.
 *
 * @param value
 *     allowed object is
 *     {@link BigDecimal }
 *
 */
public void setFulfillQtySuom(BigDecimal value)
{
  this.fulfillQtySuom=value;
}

/**
 * Gets the value of the availableQty property.
 *
 * @return
 *     possible object is
 *     {@link BigDecimal }
 *
 */
public BigDecimal getAvailableQty()
{
  return availableQty;
}

/**
 * Sets the value of the availableQty property.
 *
 * @param value
 *     allowed object is
 *     {@link BigDecimal }
 *
 */
public void setAvailableQty(BigDecimal value)
{
  this.availableQty=value;
}

/**
 * Gets the value of the status property.
 *
 * @return
 *     possible object is
 *     {@link String }
 *
 */
public String getStatus()
{
  return status;
}

/**
 * Sets the value of the status property.
 *
 * @param value
 *     allowed object is
 *     {@link String }
 *
 */
public void setStatus(String value)
{
  this.status=value;
}

/**
 * Gets the value of the statusMessage property.
 *
 * @return
 *     possible object is
 *     {@link String }
 *
 */
public String getStatusMessage()
{
  return statusMessage;
}

/**
 * Sets the value of the statusMessage property.
 *
 * @param value
 *     allowed object is
 *     {@link String }
 *
 */
public void setStatusMessage(String value)
{
  this.statusMessage=value;
}

/**
 * Gets the value of the customerOrderResponseItemFulfillment property.
 *
 * <p>
 * This accessor method returns a reference to the live list,
 * not a snapshot. Therefore any modification you make to the
 * returned list will be present inside the JAXB object.
 * This is why there is not a <CODE>set</CODE> method for the customerOrderResponseItemFulfillment property.
 *
 * <p>
 * For example, to add a new item, do as follows:
 * <pre>
 *    getCustomerOrderResponseItemFulfillment().add(newItem);
 * </pre>
 *
 *
 * <p>
 * Objects of the following type(s) are allowed in the list
 * {@link CustomerOrderResponseItemFulfillment }
 *
 *
 */
public List<CustomerOrderResponseItemFulfillment> getCustomerOrderResponseItemFulfillment()
{
  if(customerOrderResponseItemFulfillment==null)
  {
    customerOrderResponseItemFulfillment=new ArrayList<CustomerOrderResponseItemFulfillment>();
  }
  return this.customerOrderResponseItemFulfillment;
}
}
