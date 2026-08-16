package com.logicinfo.oms.model;
import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
/**
 * <p>Java class for customerOrderCancelResponseItems complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="customerOrderCancelResponseItems">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="item">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="25"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="cancel_qty_suom">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;fractionDigits value="04"/>
 *               &lt;totalDigits value="12"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="customerOrderCancelResponseItems",
         propOrder={ "lineNo","item","cancelQtySuom","messageCode","messageDesc" })
public class CustomerOrderCancelResponseItems
{
@XmlElement(required=true,nillable=true)
protected String item;
@XmlElement(name="cancel_qty_suom",required=true,nillable=true)
protected BigDecimal cancelQtySuom;
@XmlElement(name="line_no")
protected long lineNo;
@XmlElement(name="message_code",required=true,nillable=true)
protected String messageCode;
@XmlElement(name="message_desc",required=true,nillable=true)
protected String messageDesc;

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
 * Gets the value of the cancelQtySuom property.
 *
 * @return
 *     possible object is
 *     {@link BigDecimal }
 *
 */
public BigDecimal getCancelQtySuom()
{
  return cancelQtySuom;
}

/**
 * Sets the value of the cancelQtySuom property.
 *
 * @param value
 *     allowed object is
 *     {@link BigDecimal }
 *
 */
public void setCancelQtySuom(BigDecimal value)
{
  this.cancelQtySuom=value;
}

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
