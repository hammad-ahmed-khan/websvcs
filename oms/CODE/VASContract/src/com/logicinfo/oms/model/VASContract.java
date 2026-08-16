
package com.logicinfo.oms.model;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for VASContract complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="VASContract">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="srv_id" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedInt">
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="service_package_name">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="100"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="oper_unit_id" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="KSA"/>
 *               &lt;maxLength value="100"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="contract_no">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="100"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="org_id">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="100"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="item_sku">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="100"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="item_serial_1" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="100"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="item_serial_2" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="100"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="srv_sku">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="100"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="srv_price">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;totalDigits value="22"/>
 *               &lt;fractionDigits value="7"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="srv_start_date" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="srv_end_date" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="no_of_years" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;totalDigits value="22"/>
 *               &lt;fractionDigits value="10"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="status_desc">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="30"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="srv_inv_no">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="100"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="srv_inv_source">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="30"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="item_inv_source">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="30"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="extra_item">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="return_reason">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="1000"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="first_brand">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="30"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="no_of_visits">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;totalDigits value="22"/>
 *               &lt;fractionDigits value="10"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="mobile">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="40"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="salesman_id">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="15"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="srv_inv_com">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="100"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="item_inv_com">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="100"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="item_inv_date" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="on_site">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="100"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="srv_invoice_line_no">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;totalDigits value="22"/>
 *               &lt;fractionDigits value="10"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="item_invoice_line_no">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;totalDigits value="22"/>
 *               &lt;fractionDigits value="10"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="software_key">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="100"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="total_visits">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="100"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="free_labor">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="100"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="allow_loaner">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="100"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="compensation_type_text">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="100"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="free_spare_parts_text">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="100"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="num_of_re_installation_avail">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="100"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="num_of_prev_installation_avail">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="100"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="on_site_visit_flag">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="100"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="replacement_guarantee">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="100"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="number_of_cleaning_visits">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="100"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="number_of_other_visits">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="100"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="vas_group">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="100"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="first_name">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="50"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="last_name">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="50"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="covered_product">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="255"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="division_store_name">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="100"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="country">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="30"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="vas_created_date" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="service_type">
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
@XmlType(name = "VASContract", propOrder = {
    "srvId",
    "servicePackageName",
    "operUnitId",
    "contractNo",
    "orgId",
    "itemSku",
    "itemSerial1",
    "itemSerial2",
    "srvSku",
    "srvPrice",
    "srvStartDate",
    "srvEndDate",
    "noOfYears",
    "statusDesc",
    "srvInvNo",
    "srvInvSource",
    "itemInvSource",
    "extraItem",
    "returnReason",
    "firstBrand",
    "noOfVisits",
    "mobile",
    "dpYear",
    "salesmanId",
    "srvInvCom",
    "itemInvCom",
    "itemInvDate",
    "onSite",
    "srvInvoiceLineNo",
    "itemInvoiceLineNo",
    "softwareKey",
    "totalVisits",
    "freeLabor",
    "allowLoaner",
    "compensationTypeText",
    "freeSparePartsText",
    "numOfReInstallationAvail",
    "numOfPrevInstallationAvail",
    "onSiteVisitFlag",
    "replacementGuarantee",
    "numberOfCleaningVisits",
    "numberOfOtherVisits",
    "vasGroup",
    "vasType",
    "firstName",
    "lastName",
    "coveredProduct",
    "divisionStoreName",
    "country",
    "vasCreatedDate",
    "serviceType"
})
public class VASContract {

    @XmlElement(name = "srv_id")
    protected Long srvId;
    @XmlElement(name = "service_package_name", required = true)
    protected String servicePackageName;
    @XmlElement(name = "oper_unit_id")
    protected String operUnitId;
    @XmlElement(name = "contract_no", required = true)
    protected String contractNo;
    @XmlElement(name = "org_id", required = true)
    protected String orgId;
    @XmlElement(name = "item_sku")
    protected String itemSku;
    @XmlElement(name = "item_serial_1")
    protected String itemSerial1;
    @XmlElement(name = "item_serial_2")
    protected String itemSerial2;
    @XmlElement(name = "srv_sku", required = true)
    protected String srvSku;
    @XmlElement(name = "srv_price", required = true)
    protected BigDecimal srvPrice;
    @XmlElement(name = "srv_start_date", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar srvStartDate;
    @XmlElement(name = "srv_end_date", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar srvEndDate;
    @XmlElement(name = "no_of_years")
    protected BigDecimal noOfYears;
    @XmlElement(name = "status_desc", required = true)
    protected String statusDesc;
    @XmlElement(name = "srv_inv_no", required = true)
    protected String srvInvNo;
    @XmlElement(name = "srv_inv_source", required = true)
    protected String srvInvSource;
    @XmlElement(name = "item_inv_source", required = true)
    protected String itemInvSource;
    @XmlElement(name = "extra_item", required = true)
    protected String extraItem;
    @XmlElement(name = "return_reason", required = true)
    protected String returnReason;
    @XmlElement(name = "first_brand", required = true)
    protected String firstBrand;
    @XmlElement(name = "no_of_visits", required = true)
    protected BigDecimal noOfVisits;
    @XmlElement(required = true)
    protected String mobile;
    @XmlElement(name = "salesman_id", required = true)
    protected String salesmanId;
    @XmlElement(name = "srv_inv_com", required = true)
    protected String srvInvCom;
    @XmlElement(name = "item_inv_com", required = true)
    protected String itemInvCom;
    @XmlElement(name = "item_inv_date", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar itemInvDate;
    @XmlElement(name = "on_site", required = true)
    protected String onSite;
    @XmlElement(name = "srv_invoice_line_no", required = true)
    protected BigDecimal srvInvoiceLineNo;
    @XmlElement(name = "item_invoice_line_no")
    protected BigDecimal itemInvoiceLineNo;
    @XmlElement(name = "software_key", required = true)
    protected String softwareKey;
    @XmlElement(name = "total_visits", required = true)
    protected String totalVisits;
    @XmlElement(name = "free_labor", required = true)
    protected String freeLabor;
    @XmlElement(name = "allow_loaner", required = true)
    protected String allowLoaner;
    @XmlElement(name = "compensation_type_text", required = true)
    protected String compensationTypeText;
    @XmlElement(name = "free_spare_parts_text", required = true)
    protected String freeSparePartsText;
    @XmlElement(name = "num_of_re_installation_avail", required = true)
    protected String numOfReInstallationAvail;
    @XmlElement(name = "num_of_prev_installation_avail", required = true)
    protected String numOfPrevInstallationAvail;
    @XmlElement(name = "on_site_visit_flag", required = true)
    protected String onSiteVisitFlag;
    @XmlElement(name = "replacement_guarantee", required = true)
    protected String replacementGuarantee;
    @XmlElement(name = "number_of_cleaning_visits", required = true)
    protected String numberOfCleaningVisits;
    @XmlElement(name = "number_of_other_visits", required = true)
    protected String numberOfOtherVisits;
    @XmlElement(name = "vas_group", required = true)
    protected String vasGroup;
    @XmlElement(name = "first_name", required = true)
    protected String firstName;
    @XmlElement(name = "last_name", required = true)
    protected String lastName;
    @XmlElement(name = "covered_product")
    protected String coveredProduct;
    @XmlElement(name = "division_store_name", required = true)
    protected String divisionStoreName;
    @XmlElement(required = true)
    protected String country;
    @XmlElement(name = "vas_created_date", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar vasCreatedDate;
    @XmlElement(name = "service_type", required = true)
    protected String serviceType;
    @XmlElement(name = "dp_year", required = true)
    protected String dpYear;
    @XmlElement(name = "vas_type", required = true)
    protected String vasType;

    /**
     * Gets the value of the srvId property.
     *
     * @return
     *     possible object is
     *     {@link Long }
     *
     */
    public Long getSrvId() {
        return srvId;
    }

    /**
     * Sets the value of the srvId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setSrvId(Long value) {
        this.srvId = value;
    }

    /**
     * Gets the value of the servicePackageName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServicePackageName() {
        return servicePackageName;
    }

    /**
     * Sets the value of the servicePackageName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServicePackageName(String value) {
        this.servicePackageName = value;
    }

    /**
     * Gets the value of the operUnitId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOperUnitId() {
        return operUnitId;
    }

    /**
     * Sets the value of the operUnitId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOperUnitId(String value) {
        this.operUnitId = value;
    }

    /**
     * Gets the value of the contractNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContractNo() {
        return contractNo;
    }

    /**
     * Sets the value of the contractNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContractNo(String value) {
        this.contractNo = value;
    }

    /**
     * Gets the value of the orgId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrgId() {
        return orgId;
    }

    /**
     * Sets the value of the orgId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrgId(String value) {
        this.orgId = value;
    }

    /**
     * Gets the value of the itemSku property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getItemSku() {
        return itemSku;
    }

    /**
     * Sets the value of the itemSku property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setItemSku(String value) {
        this.itemSku = value;
    }

    /**
     * Gets the value of the itemSerial1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getItemSerial1() {
        return itemSerial1;
    }

    /**
     * Sets the value of the itemSerial1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setItemSerial1(String value) {
        this.itemSerial1 = value;
    }

    /**
     * Gets the value of the itemSerial2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getItemSerial2() {
        return itemSerial2;
    }

    /**
     * Sets the value of the itemSerial2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setItemSerial2(String value) {
        this.itemSerial2 = value;
    }

    /**
     * Gets the value of the srvSku property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSrvSku() {
        return srvSku;
    }

    /**
     * Sets the value of the srvSku property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSrvSku(String value) {
        this.srvSku = value;
    }

    /**
     * Gets the value of the srvPrice property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getSrvPrice() {
        return srvPrice;
    }

    /**
     * Sets the value of the srvPrice property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setSrvPrice(BigDecimal value) {
        this.srvPrice = value;
    }

    /**
     * Gets the value of the srvStartDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getSrvStartDate() {
        return srvStartDate;
    }

    /**
     * Sets the value of the srvStartDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setSrvStartDate(XMLGregorianCalendar value) {
        this.srvStartDate = value;
    }

    /**
     * Gets the value of the srvEndDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getSrvEndDate() {
        return srvEndDate;
    }

    /**
     * Sets the value of the srvEndDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setSrvEndDate(XMLGregorianCalendar value) {
        this.srvEndDate = value;
    }

    /**
     * Gets the value of the noOfYears property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getNoOfYears() {
        return noOfYears;
    }

    /**
     * Sets the value of the noOfYears property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setNoOfYears(BigDecimal value) {
        this.noOfYears = value;
    }

    /**
     * Gets the value of the statusDesc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatusDesc() {
        return statusDesc;
    }

    /**
     * Sets the value of the statusDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatusDesc(String value) {
        this.statusDesc = value;
    }

    /**
     * Gets the value of the srvInvNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSrvInvNo() {
        return srvInvNo;
    }

    /**
     * Sets the value of the srvInvNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSrvInvNo(String value) {
        this.srvInvNo = value;
    }

    /**
     * Gets the value of the srvInvSource property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSrvInvSource() {
        return srvInvSource;
    }

    /**
     * Sets the value of the srvInvSource property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSrvInvSource(String value) {
        this.srvInvSource = value;
    }

    /**
     * Gets the value of the itemInvSource property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getItemInvSource() {
        return itemInvSource;
    }

    /**
     * Sets the value of the itemInvSource property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setItemInvSource(String value) {
        this.itemInvSource = value;
    }

    /**
     * Gets the value of the extraItem property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExtraItem() {
        return extraItem;
    }

    /**
     * Sets the value of the extraItem property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExtraItem(String value) {
        this.extraItem = value;
    }

    /**
     * Gets the value of the returnReason property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReturnReason() {
        return returnReason;
    }

    /**
     * Sets the value of the returnReason property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReturnReason(String value) {
        this.returnReason = value;
    }

    /**
     * Gets the value of the firstBrand property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFirstBrand() {
        return firstBrand;
    }

    /**
     * Sets the value of the firstBrand property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFirstBrand(String value) {
        this.firstBrand = value;
    }

    /**
     * Gets the value of the noOfVisits property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getNoOfVisits() {
        return noOfVisits;
    }

    /**
     * Sets the value of the noOfVisits property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setNoOfVisits(BigDecimal value) {
        this.noOfVisits = value;
    }

    /**
     * Gets the value of the mobile property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * Sets the value of the mobile property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMobile(String value) {
        this.mobile = value;
    }

    /**
     * Gets the value of the salesmanId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSalesmanId() {
        return salesmanId;
    }

    /**
     * Sets the value of the salesmanId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSalesmanId(String value) {
        this.salesmanId = value;
    }

    /**
     * Gets the value of the srvInvCom property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSrvInvCom() {
        return srvInvCom;
    }

    /**
     * Sets the value of the srvInvCom property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSrvInvCom(String value) {
        this.srvInvCom = value;
    }

    /**
     * Gets the value of the itemInvCom property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getItemInvCom() {
        return itemInvCom;
    }

    /**
     * Sets the value of the itemInvCom property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setItemInvCom(String value) {
        this.itemInvCom = value;
    }

    /**
     * Gets the value of the itemInvDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getItemInvDate() {
        return itemInvDate;
    }

    /**
     * Sets the value of the itemInvDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setItemInvDate(XMLGregorianCalendar value) {
        this.itemInvDate = value;
    }

    /**
     * Gets the value of the onSite property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOnSite() {
        return onSite;
    }

    /**
     * Sets the value of the onSite property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOnSite(String value) {
        this.onSite = value;
    }

    /**
     * Gets the value of the srvInvoiceLineNo property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getSrvInvoiceLineNo() {
        return srvInvoiceLineNo;
    }

    /**
     * Sets the value of the srvInvoiceLineNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setSrvInvoiceLineNo(BigDecimal value) {
        this.srvInvoiceLineNo = value;
    }

    /**
     * Gets the value of the itemInvoiceLineNo property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getItemInvoiceLineNo() {
        return itemInvoiceLineNo;
    }

    /**
     * Sets the value of the itemInvoiceLineNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setItemInvoiceLineNo(BigDecimal value) {
        this.itemInvoiceLineNo = value;
    }

    /**
     * Gets the value of the softwareKey property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSoftwareKey() {
        return softwareKey;
    }

    /**
     * Sets the value of the softwareKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSoftwareKey(String value) {
        this.softwareKey = value;
    }

    /**
     * Gets the value of the totalVisits property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTotalVisits() {
        return totalVisits;
    }

    /**
     * Sets the value of the totalVisits property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTotalVisits(String value) {
        this.totalVisits = value;
    }

    /**
     * Gets the value of the freeLabor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFreeLabor() {
        return freeLabor;
    }

    /**
     * Sets the value of the freeLabor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFreeLabor(String value) {
        this.freeLabor = value;
    }

    /**
     * Gets the value of the allowLoaner property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAllowLoaner() {
        return allowLoaner;
    }

    /**
     * Sets the value of the allowLoaner property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAllowLoaner(String value) {
        this.allowLoaner = value;
    }

    /**
     * Gets the value of the compensationTypeText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCompensationTypeText() {
        return compensationTypeText;
    }

    /**
     * Sets the value of the compensationTypeText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCompensationTypeText(String value) {
        this.compensationTypeText = value;
    }

    /**
     * Gets the value of the freeSparePartsText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFreeSparePartsText() {
        return freeSparePartsText;
    }

    /**
     * Sets the value of the freeSparePartsText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFreeSparePartsText(String value) {
        this.freeSparePartsText = value;
    }

    /**
     * Gets the value of the numOfReInstallationAvail property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumOfReInstallationAvail() {
        return numOfReInstallationAvail;
    }

    /**
     * Sets the value of the numOfReInstallationAvail property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumOfReInstallationAvail(String value) {
        this.numOfReInstallationAvail = value;
    }

    /**
     * Gets the value of the numOfPrevInstallationAvail property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumOfPrevInstallationAvail() {
        return numOfPrevInstallationAvail;
    }

    /**
     * Sets the value of the numOfPrevInstallationAvail property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumOfPrevInstallationAvail(String value) {
        this.numOfPrevInstallationAvail = value;
    }

    /**
     * Gets the value of the onSiteVisitFlag property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOnSiteVisitFlag() {
        return onSiteVisitFlag;
    }

    /**
     * Sets the value of the onSiteVisitFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOnSiteVisitFlag(String value) {
        this.onSiteVisitFlag = value;
    }

    /**
     * Gets the value of the replacementGuarantee property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReplacementGuarantee() {
        return replacementGuarantee;
    }

    /**
     * Sets the value of the replacementGuarantee property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReplacementGuarantee(String value) {
        this.replacementGuarantee = value;
    }

    /**
     * Gets the value of the numberOfCleaningVisits property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumberOfCleaningVisits() {
        return numberOfCleaningVisits;
    }

    /**
     * Sets the value of the numberOfCleaningVisits property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumberOfCleaningVisits(String value) {
        this.numberOfCleaningVisits = value;
    }

    /**
     * Gets the value of the numberOfOtherVisits property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumberOfOtherVisits() {
        return numberOfOtherVisits;
    }

    /**
     * Sets the value of the numberOfOtherVisits property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumberOfOtherVisits(String value) {
        this.numberOfOtherVisits = value;
    }

    /**
     * Gets the value of the vasGroup property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVasGroup() {
        return vasGroup;
    }

    /**
     * Sets the value of the vasGroup property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVasGroup(String value) {
        this.vasGroup = value;
    }

    /**
     * Gets the value of the firstName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the value of the firstName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFirstName(String value) {
        this.firstName = value;
    }

    /**
     * Gets the value of the lastName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the value of the lastName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastName(String value) {
        this.lastName = value;
    }

    /**
     * Gets the value of the coveredProduct property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCoveredProduct() {
        return coveredProduct;
    }

    /**
     * Sets the value of the coveredProduct property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCoveredProduct(String value) {
        this.coveredProduct = value;
    }

    /**
     * Gets the value of the divisionStoreName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDivisionStoreName() {
        return divisionStoreName;
    }

    /**
     * Sets the value of the divisionStoreName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDivisionStoreName(String value) {
        this.divisionStoreName = value;
    }

    /**
     * Gets the value of the country property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the value of the country property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCountry(String value) {
        this.country = value;
    }

    /**
     * Gets the value of the vasCreatedDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getVasCreatedDate() {
        return vasCreatedDate;
    }

    /**
     * Sets the value of the vasCreatedDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setVasCreatedDate(XMLGregorianCalendar value) {
        this.vasCreatedDate = value;
    }

    /**
     * Gets the value of the serviceType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceType() {
        return serviceType;
    }

    /**
     * Sets the value of the serviceType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceType(String value) {
        this.serviceType = value;
    }

    /**
     * Gets the value of the dpYear property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getDpYear() {
        return dpYear;
    }

    /**
     * Gets the value of the vasType property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getVasType() {
        return vasType;
    }

    /**
     * Sets the value of the dpYear property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setDpYear(String value) {
        this.dpYear = value;
    }

    /**
     * Sets the value of the vasType property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setVasType(String value) {
        this.vasType = value;
    }

}
