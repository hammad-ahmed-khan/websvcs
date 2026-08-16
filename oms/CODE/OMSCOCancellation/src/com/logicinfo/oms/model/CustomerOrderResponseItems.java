package com.logicinfo.oms.model;
import java.math.BigDecimal;

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
 *               &lt;minExclusive value="0"/>
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
@XmlType(name="customerOrderResponseItems",propOrder={ "item","cancelQtySuom" })
public class CustomerOrderResponseItems
{
@XmlElement(required=true,nillable=true)
protected String item;
@XmlElement(name="cancel_qty_suom",required=true,nillable=true)
protected BigDecimal cancelQtySuom;

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
}
