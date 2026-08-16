package oms.logicinfo.com.model;
import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
/**
 * <p>Java class for CustOrdItmDesc complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="CustOrdItmDesc">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="line_no">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedInt">
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="item_id">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="25"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="requested_qty">
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
@XmlType(name="CustOrdItmDesc",propOrder={ "lineNo","itemId","requestedQty" })
public class CustOrdItmDesc
{
@XmlElement(name="line_no")
protected long lineNo;
@XmlElement(name="item_id",required=true)
protected String itemId;
@XmlElement(name="requested_qty",required=true)
protected BigDecimal requestedQty;

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
 * Gets the value of the itemId property.
 *
 * @return
 *     possible object is
 *     {@link String }
 *
 */
public String getItemId()
{
  return itemId;
}

/**
 * Sets the value of the itemId property.
 *
 * @param value
 *     allowed object is
 *     {@link String }
 *
 */
public void setItemId(String value)
{
  this.itemId=value;
}

/**
 * Gets the value of the requestedQty property.
 *
 * @return
 *     possible object is
 *     {@link BigDecimal }
 *
 */
public BigDecimal getRequestedQty()
{
  return requestedQty;
}

/**
 * Sets the value of the requestedQty property.
 *
 * @param value
 *     allowed object is
 *     {@link BigDecimal }
 *
 */
public void setRequestedQty(BigDecimal value)
{
  this.requestedQty=value;
}
}
