package oms.logicinfo.com.model;
import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
/**
 * <p>Java class for customerOrderAddress complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="customerOrderAddress">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="deliver_first_name">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="120"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="deliver_last_name">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="120"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="deliver_address_1">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="240"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="deliver_address_2" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="240"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="deliver_address_3" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="240"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="deliver_city">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="120"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="deliver_state" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="3"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="deliver_country" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="3"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="deliver_postal">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="30"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="deliver_phone_no" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="15"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="latitude" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;fractionDigits value="15"/>
 *               &lt;totalDigits value="20"/>
 *               &lt;minInclusive value="0"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="longitude" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;fractionDigits value="15"/>
 *               &lt;totalDigits value="20"/>
 *               &lt;minInclusive value="0"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="bill_first_name">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="120"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="bill_last_name">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="120"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="bill_address_1">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="240"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="bill_address_2" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="240"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="bill_address_3" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="240"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="bill_city">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="120"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="bill_state" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="3"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="bill_country" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="3"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="bill_postal">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="30"/>
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
@XmlType(name="customerOrderAddress",
         propOrder={ "deliverFirstName","deliverLastName","deliverAddress1","deliverAddress2","deliverAddress3",
                     "deliverCity","deliverState","deliverCountry","deliverPostal","deliverPhoneNo","latitude",
                     "longitude","billFirstName","billLastName","billAddress1","billAddress2","billAddress3",
                     "billCity","billState","billCountry","billPostal" })
public class CustomerOrderAddress
{
@XmlElement(name="deliver_first_name",required=true)
protected String deliverFirstName;
@XmlElement(name="deliver_last_name",required=true)
protected String deliverLastName;
@XmlElement(name="deliver_address_1",required=true)
protected String deliverAddress1;
@XmlElement(name="deliver_address_2")
protected String deliverAddress2;
@XmlElement(name="deliver_address_3")
protected String deliverAddress3;
@XmlElement(name="deliver_city",required=true)
protected String deliverCity;
@XmlElement(name="deliver_state")
protected String deliverState;
@XmlElement(name="deliver_country")
protected String deliverCountry;
@XmlElement(name="deliver_postal",required=true)
protected String deliverPostal;
@XmlElement(name="deliver_phone_no")
protected String deliverPhoneNo;
protected BigDecimal latitude;
protected BigDecimal longitude;
@XmlElement(name="bill_first_name",required=true)
protected String billFirstName;
@XmlElement(name="bill_last_name",required=true)
protected String billLastName;
@XmlElement(name="bill_address_1",required=true)
protected String billAddress1;
@XmlElement(name="bill_address_2")
protected String billAddress2;
@XmlElement(name="bill_address_3")
protected String billAddress3;
@XmlElement(name="bill_city",required=true)
protected String billCity;
@XmlElement(name="bill_state")
protected String billState;
@XmlElement(name="bill_country")
protected String billCountry;
@XmlElement(name="bill_postal",required=true)
protected String billPostal;

/**
 * Gets the value of the deliverFirstName property.
 *
 * @return
 *     possible object is
 *     {@link String }
 *
 */
public String getDeliverFirstName()
{
  return deliverFirstName;
}

/**
 * Sets the value of the deliverFirstName property.
 *
 * @param value
 *     allowed object is
 *     {@link String }
 *
 */
public void setDeliverFirstName(String value)
{
  this.deliverFirstName=value;
}

/**
 * Gets the value of the deliverLastName property.
 *
 * @return
 *     possible object is
 *     {@link String }
 *
 */
public String getDeliverLastName()
{
  return deliverLastName;
}

/**
 * Sets the value of the deliverLastName property.
 *
 * @param value
 *     allowed object is
 *     {@link String }
 *
 */
public void setDeliverLastName(String value)
{
  this.deliverLastName=value;
}

/**
 * Gets the value of the deliverAddress1 property.
 *
 * @return
 *     possible object is
 *     {@link String }
 *
 */
public String getDeliverAddress1()
{
  return deliverAddress1;
}

/**
 * Sets the value of the deliverAddress1 property.
 *
 * @param value
 *     allowed object is
 *     {@link String }
 *
 */
public void setDeliverAddress1(String value)
{
  this.deliverAddress1=value;
}

/**
 * Gets the value of the deliverAddress2 property.
 *
 * @return
 *     possible object is
 *     {@link String }
 *
 */
public String getDeliverAddress2()
{
  return deliverAddress2;
}

/**
 * Sets the value of the deliverAddress2 property.
 *
 * @param value
 *     allowed object is
 *     {@link String }
 *
 */
public void setDeliverAddress2(String value)
{
  this.deliverAddress2=value;
}

/**
 * Gets the value of the deliverAddress3 property.
 *
 * @return
 *     possible object is
 *     {@link String }
 *
 */
public String getDeliverAddress3()
{
  return deliverAddress3;
}

/**
 * Sets the value of the deliverAddress3 property.
 *
 * @param value
 *     allowed object is
 *     {@link String }
 *
 */
public void setDeliverAddress3(String value)
{
  this.deliverAddress3=value;
}

/**
 * Gets the value of the deliverCity property.
 *
 * @return
 *     possible object is
 *     {@link String }
 *
 */
public String getDeliverCity()
{
  return deliverCity;
}

/**
 * Sets the value of the deliverCity property.
 *
 * @param value
 *     allowed object is
 *     {@link String }
 *
 */
public void setDeliverCity(String value)
{
  this.deliverCity=value;
}

/**
 * Gets the value of the deliverState property.
 *
 * @return
 *     possible object is
 *     {@link String }
 *
 */
public String getDeliverState()
{
  return deliverState;
}

/**
 * Sets the value of the deliverState property.
 *
 * @param value
 *     allowed object is
 *     {@link String }
 *
 */
public void setDeliverState(String value)
{
  this.deliverState=value;
}

/**
 * Gets the value of the deliverCountry property.
 *
 * @return
 *     possible object is
 *     {@link String }
 *
 */
public String getDeliverCountry()
{
  return deliverCountry;
}

/**
 * Sets the value of the deliverCountry property.
 *
 * @param value
 *     allowed object is
 *     {@link String }
 *
 */
public void setDeliverCountry(String value)
{
  this.deliverCountry=value;
}

/**
 * Gets the value of the deliverPostal property.
 *
 * @return
 *     possible object is
 *     {@link String }
 *
 */
public String getDeliverPostal()
{
  return deliverPostal;
}

/**
 * Sets the value of the deliverPostal property.
 *
 * @param value
 *     allowed object is
 *     {@link String }
 *
 */
public void setDeliverPostal(String value)
{
  this.deliverPostal=value;
}

/**
 * Gets the value of the deliverPhoneNo property.
 *
 * @return
 *     possible object is
 *     {@link String }
 *
 */
public String getDeliverPhoneNo()
{
  return deliverPhoneNo;
}

/**
 * Sets the value of the deliverPhoneNo property.
 *
 * @param value
 *     allowed object is
 *     {@link String }
 *
 */
public void setDeliverPhoneNo(String value)
{
  this.deliverPhoneNo=value;
}

/**
 * Gets the value of the latitude property.
 *
 * @return
 *     possible object is
 *     {@link BigDecimal }
 *
 */
public BigDecimal getLatitude()
{
  return latitude;
}

/**
 * Sets the value of the latitude property.
 *
 * @param value
 *     allowed object is
 *     {@link BigDecimal }
 *
 */
public void setLatitude(BigDecimal value)
{
  this.latitude=value;
}

/**
 * Gets the value of the longitude property.
 *
 * @return
 *     possible object is
 *     {@link BigDecimal }
 *
 */
public BigDecimal getLongitude()
{
  return longitude;
}

/**
 * Sets the value of the longitude property.
 *
 * @param value
 *     allowed object is
 *     {@link BigDecimal }
 *
 */
public void setLongitude(BigDecimal value)
{
  this.longitude=value;
}

/**
 * Gets the value of the billFirstName property.
 *
 * @return
 *     possible object is
 *     {@link String }
 *
 */
public String getBillFirstName()
{
  return billFirstName;
}

/**
 * Sets the value of the billFirstName property.
 *
 * @param value
 *     allowed object is
 *     {@link String }
 *
 */
public void setBillFirstName(String value)
{
  this.billFirstName=value;
}

/**
 * Gets the value of the billLastName property.
 *
 * @return
 *     possible object is
 *     {@link String }
 *
 */
public String getBillLastName()
{
  return billLastName;
}

/**
 * Sets the value of the billLastName property.
 *
 * @param value
 *     allowed object is
 *     {@link String }
 *
 */
public void setBillLastName(String value)
{
  this.billLastName=value;
}

/**
 * Gets the value of the billAddress1 property.
 *
 * @return
 *     possible object is
 *     {@link String }
 *
 */
public String getBillAddress1()
{
  return billAddress1;
}

/**
 * Sets the value of the billAddress1 property.
 *
 * @param value
 *     allowed object is
 *     {@link String }
 *
 */
public void setBillAddress1(String value)
{
  this.billAddress1=value;
}

/**
 * Gets the value of the billAddress2 property.
 *
 * @return
 *     possible object is
 *     {@link String }
 *
 */
public String getBillAddress2()
{
  return billAddress2;
}

/**
 * Sets the value of the billAddress2 property.
 *
 * @param value
 *     allowed object is
 *     {@link String }
 *
 */
public void setBillAddress2(String value)
{
  this.billAddress2=value;
}

/**
 * Gets the value of the billAddress3 property.
 *
 * @return
 *     possible object is
 *     {@link String }
 *
 */
public String getBillAddress3()
{
  return billAddress3;
}

/**
 * Sets the value of the billAddress3 property.
 *
 * @param value
 *     allowed object is
 *     {@link String }
 *
 */
public void setBillAddress3(String value)
{
  this.billAddress3=value;
}

/**
 * Gets the value of the billCity property.
 *
 * @return
 *     possible object is
 *     {@link String }
 *
 */
public String getBillCity()
{
  return billCity;
}

/**
 * Sets the value of the billCity property.
 *
 * @param value
 *     allowed object is
 *     {@link String }
 *
 */
public void setBillCity(String value)
{
  this.billCity=value;
}

/**
 * Gets the value of the billState property.
 *
 * @return
 *     possible object is
 *     {@link String }
 *
 */
public String getBillState()
{
  return billState;
}

/**
 * Sets the value of the billState property.
 *
 * @param value
 *     allowed object is
 *     {@link String }
 *
 */
public void setBillState(String value)
{
  this.billState=value;
}

/**
 * Gets the value of the billCountry property.
 *
 * @return
 *     possible object is
 *     {@link String }
 *
 */
public String getBillCountry()
{
  return billCountry;
}

/**
 * Sets the value of the billCountry property.
 *
 * @param value
 *     allowed object is
 *     {@link String }
 *
 */
public void setBillCountry(String value)
{
  this.billCountry=value;
}

/**
 * Gets the value of the billPostal property.
 *
 * @return
 *     possible object is
 *     {@link String }
 *
 */
public String getBillPostal()
{
  return billPostal;
}

/**
 * Sets the value of the billPostal property.
 *
 * @param value
 *     allowed object is
 *     {@link String }
 *
 */
public void setBillPostal(String value)
{
  this.billPostal=value;
}
}
