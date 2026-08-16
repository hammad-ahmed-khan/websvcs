package oms.logicinfo.com.model;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
/**
 * <p>Java class for CustOrdFulDesc complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="CustOrdFulDesc">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="delivery_type">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="S"/>
 *               &lt;enumeration value="C"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="pick_loc" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedInt">
 *               &lt;totalDigits value="10"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ship_city" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="120"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="CustOrdItmDesc" type="{http://com.logicinfo.oms/model/}CustOrdItmDesc" maxOccurs="100"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="CustOrdFulDesc",propOrder={ "deliveryType","pickLoc","shipCity","custOrdItmDesc" })
public class CustOrdFulDesc
{
@XmlElement(name="delivery_type",required=true)
protected String deliveryType;
@XmlElement(name="pick_loc")
protected Long pickLoc;
@XmlElement(name="ship_city")
protected String shipCity;
@XmlElement(name="CustOrdItmDesc",required=true)
protected List<CustOrdItmDesc> custOrdItmDesc;

/**
 * Gets the value of the deliveryType property.
 *
 * @return
 *     possible object is
 *     {@link String }
 *
 */
public String getDeliveryType()
{
  return deliveryType;
}

/**
 * Sets the value of the deliveryType property.
 *
 * @param value
 *     allowed object is
 *     {@link String }
 *
 */
public void setDeliveryType(String value)
{
  this.deliveryType=value;
}

/**
 * Gets the value of the pickLoc property.
 *
 * @return
 *     possible object is
 *     {@link Long }
 *
 */
public Long getPickLoc()
{
  return pickLoc;
}

/**
 * Sets the value of the pickLoc property.
 *
 * @param value
 *     allowed object is
 *     {@link Long }
 *
 */
public void setPickLoc(Long value)
{
  this.pickLoc=value;
}

/**
 * Gets the value of the shipCity property.
 *
 * @return
 *     possible object is
 *     {@link String }
 *
 */
public String getShipCity()
{
  return shipCity;
}

/**
 * Sets the value of the shipCity property.
 *
 * @param value
 *     allowed object is
 *     {@link String }
 *
 */
public void setShipCity(String value)
{
  this.shipCity=value;
}

/**
 * Gets the value of the custOrdItmDesc property.
 *
 * <p>
 * This accessor method returns a reference to the live list,
 * not a snapshot. Therefore any modification you make to the
 * returned list will be present inside the JAXB object.
 * This is why there is not a <CODE>set</CODE> method for the custOrdItmDesc property.
 *
 * <p>
 * For example, to add a new item, do as follows:
 * <pre>
 *    getCustOrdItmDesc().add(newItem);
 * </pre>
 *
 *
 * <p>
 * Objects of the following type(s) are allowed in the list
 * {@link CustOrdItmDesc }
 *
 *
 */
public List<CustOrdItmDesc> getCustOrdItmDesc()
{
  if(custOrdItmDesc==null)
  {
    custOrdItmDesc=new ArrayList<CustOrdItmDesc>();
  }
  return this.custOrdItmDesc;
}
}
