package oms.logicinfo.com.model;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
/**
 * <p>Java class for inventoryCheckResponse complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="inventoryCheckResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="initiate_loc_id">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedInt">
 *               &lt;minExclusive value="0"/>
 *               &lt;totalDigits value="12"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="CustOrdFulDescResponse" type="{http://com.logicinfo.oms/model/}CustOrdFulDescResponse" maxOccurs="100"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="inventoryCheckResponse",propOrder={ "initiateLocId","custOrdFulDescResponse" })
public class InventoryCheckResponse
{
@XmlElement(name="initiate_loc_id")
protected long initiateLocId;
@XmlElement(name="CustOrdFulDescResponse",required=true)
protected List<CustOrdFulDescResponse> custOrdFulDescResponse;

/**
 * Gets the value of the initiateLocId property.
 *
 */
public long getInitiateLocId()
{
  return initiateLocId;
}

/**
 * Sets the value of the initiateLocId property.
 *
 */
public void setInitiateLocId(long value)
{
  this.initiateLocId=value;
}

/**
 * Gets the value of the custOrdFulDescResponse property.
 *
 * <p>
 * This accessor method returns a reference to the live list,
 * not a snapshot. Therefore any modification you make to the
 * returned list will be present inside the JAXB object.
 * This is why there is not a <CODE>set</CODE> method for the custOrdFulDescResponse property.
 *
 * <p>
 * For example, to add a new item, do as follows:
 * <pre>
 *    getCustOrdFulDescResponse().add(newItem);
 * </pre>
 *
 *
 * <p>
 * Objects of the following type(s) are allowed in the list
 * {@link CustOrdFulDescResponse }
 *
 *
 */
public List<CustOrdFulDescResponse> getCustOrdFulDescResponse()
{
  if(custOrdFulDescResponse==null)
  {
    custOrdFulDescResponse=new ArrayList<CustOrdFulDescResponse>();
  }
  return this.custOrdFulDescResponse;
}
}
