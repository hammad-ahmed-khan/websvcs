package oms.logicinfo.com.model;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
/**
 * <p>Java class for backOrderRequest complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="backOrderRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="nothing">
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
@XmlType(name="backOrderRequest",propOrder={ "nothing" })
public class BackOrderRequest
{
@XmlElement(required=true)
protected String nothing;

/**
 * Gets the value of the nothing property.
 *
 * @return
 *     possible object is
 *     {@link String }
 *
 */
public String getNothing()
{
  return nothing;
}

/**
 * Sets the value of the nothing property.
 *
 * @param value
 *     allowed object is
 *     {@link String }
 *
 */
public void setNothing(String value)
{
  this.nothing=value;
}
}
