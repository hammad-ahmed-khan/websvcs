package com.logicinfo.oms.model;
import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
/**
 * <p>Java class for customerOrderCancellationTenders complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="customerOrderCancellationTenders">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="tender_type">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="CASH"/>
 *               &lt;enumeration value="DCARD"/>
 *               &lt;enumeration value="CCARD"/>
 *               &lt;enumeration value="SADAD"/>
 *               &lt;enumeration value="CSHOD"/>
 *               &lt;enumeration value="CHECK"/>
 *               &lt;enumeration value="CREDIT"/>
 *               &lt;enumeration value="GIFTCARD"/>
 *               &lt;enumeration value="VOUCHER"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="tender_type_id">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedInt">
 *               &lt;minExclusive value="0"/>
 *               &lt;totalDigits value="6"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="sadad_pymt_status_ind" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="P"/>
 *               &lt;enumeration value="S"/>
 *               &lt;enumeration value="E"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="tender_amount">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;minExclusive value="0"/>
 *               &lt;fractionDigits value="04"/>
 *               &lt;totalDigits value="20"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="gift_card_id" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedInt">
 *               &lt;minExclusive value="0"/>
 *               &lt;totalDigits value="20"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="voucher_id" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedInt">
 *               &lt;minExclusive value="0"/>
 *               &lt;totalDigits value="20"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="cc_no">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="40"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="cc_auth_no">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="16"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="cc_auth_src">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="6"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="cc_cardholder_verf">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="6"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="cc_exp_date" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="cc_entry_mode">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="6"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="cc_term_id">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="5"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="cc_spec_cond">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="6"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="cheque_no">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedInt">
 *               &lt;minExclusive value="0"/>
 *               &lt;totalDigits value="10"/>
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
@XmlType(name="customerOrderCancellationTenders",
         propOrder={ "tenderType","tenderTypeId","pymtStatusInd","tenderAmount","ccNo","ccAuthNo","ccAuthSrc",
                     "ccCardholderVerf","ccExpDate","ccEntryMode","ccTermId","ccSpecCond","tenderRefId" })
public class CustomerOrderCancellationTenders
{
@XmlElement(name="tender_type",required=true)
protected String tenderType;
@XmlElement(name="tender_type_id")
protected long tenderTypeId;
@XmlElement(name="tender_amount",required=true)
protected BigDecimal tenderAmount;
@XmlElement(name="cc_no")
protected String ccNo;
@XmlElement(name="cc_auth_no")
protected String ccAuthNo;
@XmlElement(name="cc_auth_src")
protected String ccAuthSrc;
@XmlElement(name="cc_cardholder_verf")
protected String ccCardholderVerf;
@XmlSchemaType(name="dateTime")
@XmlElement(name="cc_exp_date")
protected XMLGregorianCalendar ccExpDate;
@XmlElement(name="cc_entry_mode")
protected String ccEntryMode;
@XmlElement(name="cc_term_id")
protected String ccTermId;
@XmlElement(name="cc_spec_cond")
protected String ccSpecCond;
@XmlElement(name="pymt_status_ind")
protected String pymtStatusInd;
@XmlElement(name="tender_ref_id")
protected Long tenderRefId;

/**
 * Gets the value of the tenderType property.
 *
 * @return
 *     possible object is
 *     {@link String }
 *
 */
public String getTenderType()
{
  return tenderType;
}

/**
 * Sets the value of the tenderType property.
 *
 * @param value
 *     allowed object is
 *     {@link String }
 *
 */
public void setTenderType(String value)
{
  this.tenderType=value;
}

/**
 * Gets the value of the tenderTypeId property.
 *
 */
public long getTenderTypeId()
{
  return tenderTypeId;
}

/**
 * Sets the value of the tenderTypeId property.
 *
 */
public void setTenderTypeId(long value)
{
  this.tenderTypeId=value;
}

/**
 * Gets the value of the tenderAmount property.
 *
 * @return
 *     possible object is
 *     {@link BigDecimal }
 *
 */
public BigDecimal getTenderAmount()
{
  return tenderAmount;
}

/**
 * Sets the value of the tenderAmount property.
 *
 * @param value
 *     allowed object is
 *     {@link BigDecimal }
 *
 */
public void setTenderAmount(BigDecimal value)
{
  this.tenderAmount=value;
}

/**
 * Gets the value of the ccNo property.
 *
 * @return
 *     possible object is
 *     {@link String }
 *
 */
public String getCcNo()
{
  return ccNo;
}

/**
 * Sets the value of the ccNo property.
 *
 * @param value
 *     allowed object is
 *     {@link String }
 *
 */
public void setCcNo(String value)
{
  this.ccNo=value;
}

/**
 * Gets the value of the ccAuthNo property.
 *
 * @return
 *     possible object is
 *     {@link String }
 *
 */
public String getCcAuthNo()
{
  return ccAuthNo;
}

/**
 * Sets the value of the ccAuthNo property.
 *
 * @param value
 *     allowed object is
 *     {@link String }
 *
 */
public void setCcAuthNo(String value)
{
  this.ccAuthNo=value;
}

/**
 * Gets the value of the ccAuthSrc property.
 *
 * @return
 *     possible object is
 *     {@link String }
 *
 */
public String getCcAuthSrc()
{
  return ccAuthSrc;
}

/**
 * Sets the value of the ccAuthSrc property.
 *
 * @param value
 *     allowed object is
 *     {@link String }
 *
 */
public void setCcAuthSrc(String value)
{
  this.ccAuthSrc=value;
}

/**
 * Gets the value of the ccCardholderVerf property.
 *
 * @return
 *     possible object is
 *     {@link String }
 *
 */
public String getCcCardholderVerf()
{
  return ccCardholderVerf;
}

/**
 * Sets the value of the ccCardholderVerf property.
 *
 * @param value
 *     allowed object is
 *     {@link String }
 *
 */
public void setCcCardholderVerf(String value)
{
  this.ccCardholderVerf=value;
}

/**
 * Gets the value of the ccExpDate property.
 *
 * @return
 *     possible object is
 *     {@link XMLGregorianCalendar }
 *
 */
public XMLGregorianCalendar getCcExpDate()
{
  return ccExpDate;
}

/**
 * Sets the value of the ccExpDate property.
 *
 * @param value
 *     allowed object is
 *     {@link XMLGregorianCalendar }
 *
 */
public void setCcExpDate(XMLGregorianCalendar value)
{
  this.ccExpDate=value;
}

/**
 * Gets the value of the ccEntryMode property.
 *
 * @return
 *     possible object is
 *     {@link String }
 *
 */
public String getCcEntryMode()
{
  return ccEntryMode;
}

/**
 * Sets the value of the ccEntryMode property.
 *
 * @param value
 *     allowed object is
 *     {@link String }
 *
 */
public void setCcEntryMode(String value)
{
  this.ccEntryMode=value;
}

/**
 * Gets the value of the ccTermId property.
 *
 * @return
 *     possible object is
 *     {@link String }
 *
 */
public String getCcTermId()
{
  return ccTermId;
}

/**
 * Sets the value of the ccTermId property.
 *
 * @param value
 *     allowed object is
 *     {@link String }
 *
 */
public void setCcTermId(String value)
{
  this.ccTermId=value;
}

/**
 * Gets the value of the ccSpecCond property.
 *
 * @return
 *     possible object is
 *     {@link String }
 *
 */
public String getCcSpecCond()
{
  return ccSpecCond;
}

/**
 * Sets the value of the ccSpecCond property.
 *
 * @param value
 *     allowed object is
 *     {@link String }
 *
 */
public void setCcSpecCond(String value)
{
  this.ccSpecCond=value;
}

/**
 * Gets the value of the pymtStatusInd property.
 *
 * @return
 *     possible object is
 *     {@link String }
 *
 */
public String getPymtStatusInd()
{
  return pymtStatusInd;
}

/**
 * Gets the value of the tenderRefId property.
 *
 * @return
 *     possible object is
 *     {@link Long }
 *
 */
public Long getTenderRefId()
{
  return tenderRefId;
}

/**
 * Sets the value of the pymtStatusInd property.
 *
 * @param value
 *     allowed object is
 *     {@link String }
 *
 */
public void setPymtStatusInd(String value)
{
  this.pymtStatusInd=value;
}

/**
 * Sets the value of the tenderRefId property.
 *
 * @param value
 *     allowed object is
 *     {@link Long }
 *
 */
public void setTenderRefId(Long value)
{
  this.tenderRefId=value;
}
}
