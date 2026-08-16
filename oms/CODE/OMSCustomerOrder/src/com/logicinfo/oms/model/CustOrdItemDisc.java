
package com.logicinfo.oms.model;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CustOrdItemDisc complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="CustOrdItemDisc">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="disc_line_no">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedInt">
 *               &lt;minExclusive value="0"/>
 *               &lt;totalDigits value="04"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="rms_promo_type">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="06"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="disc_ref_no">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedInt">
 *               &lt;totalDigits value="10"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="discount_type">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="06"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="unit_discount_amount">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;fractionDigits value="04"/>
 *               &lt;totalDigits value="20"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="promo_comp_id">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedInt">
 *               &lt;totalDigits value="10"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *          &lt;element name="simple_promo_ind">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="12"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="employee_id">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="10"/>
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
@XmlType(name = "CustOrdItemDisc", propOrder = {
    "discLineNo",
    "rmsPromoType",
    "discRefNo",
    "discountType",
    "unitDiscountAmount",
    "promoCompId",
    "simplePromoInd",
    "employeeId"
})
public class CustOrdItemDisc {

    @XmlElement(name = "disc_line_no")
    protected long discLineNo;
    @XmlElement(name = "rms_promo_type", required = true)
    protected String rmsPromoType;
    @XmlElement(name = "disc_ref_no")
    protected Long discRefNo;
    @XmlElement(name = "discount_type", required = true)
    protected String discountType;
    @XmlElement(name = "unit_discount_amount", required = true)
    protected BigDecimal unitDiscountAmount;
    @XmlElement(name = "promo_comp_id")
    protected Long promoCompId;
    @XmlElement(name = "employee_id")
    protected String employeeId;
    @XmlElement(name = "simple_promo_ind")
    protected String simplePromoInd;

    /**
     * Gets the value of the discLineNo property.
     *
     */
    public long getDiscLineNo() {
        return discLineNo;
    }

    /**
     * Sets the value of the discLineNo property.
     * 
     */
    public void setDiscLineNo(long value) {
        this.discLineNo = value;
    }

    /**
     * Gets the value of the rmsPromoType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRmsPromoType() {
        return rmsPromoType;
    }

    /**
     * Sets the value of the rmsPromoType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRmsPromoType(String value) {
        this.rmsPromoType = value;
    }

    /**
     * Gets the value of the discRefNo property.
     *
     */
    public Long getDiscRefNo() {
        return discRefNo;
    }

    /**
     * Sets the value of the discRefNo property.
     * 
     */
    public void setDiscRefNo(Long value) {
        this.discRefNo = value;
    }

    /**
     * Gets the value of the discountType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDiscountType() {
        return discountType;
    }

    /**
     * Sets the value of the discountType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDiscountType(String value) {
        this.discountType = value;
    }

    /**
     * Gets the value of the unitDiscountAmount property.
     *
     */
    public BigDecimal getUnitDiscountAmount() {
        return unitDiscountAmount;
    }

    /**
     * Sets the value of the unitDiscountAmount property.
     * 
     */
    public void setUnitDiscountAmount(BigDecimal value) {
        this.unitDiscountAmount = value;
    }

    /**
     * Gets the value of the promoCompId property.
     *
     */
    public Long getPromoCompId() {
        return promoCompId;
    }

    /**
     * Sets the value of the promoCompId property.
     * 
     */
    public void setPromoCompId(Long value) {
        this.promoCompId = value;
    }

    /**
     * Gets the value of the employeeId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmployeeId() {
        return employeeId;
    }

    /**
     * Sets the value of the employeeId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmployeeId(String value) {
        this.employeeId = value;
    }

    /**
     * Gets the value of the simplePromoInd property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getSimplePromoInd() {
        return simplePromoInd;
    }

    /**
     * Sets the value of the simplePromoInd property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setSimplePromoInd(String value) {
        this.simplePromoInd = value;
    }

}
