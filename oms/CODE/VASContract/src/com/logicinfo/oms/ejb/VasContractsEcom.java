package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

import java.sql.Timestamp;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQueries( { @NamedQuery(name = "VasContractsEcom.findAll", query = "select o from VasContractsEcom o") })
@Table(name = "VAS_CONTRACTS_ECOM")
@IdClass(VasContractsEcomPK.class)
public class VasContractsEcom implements Serializable {
    @Column(name = "ALLOW_LOANER", nullable = false, length = 1)
    private String allowLoaner;
    @Column(name = "BACKUP_CD", length = 30)
    private String backupCd;
    @Column(name = "COMPENSATION_TYPE_TEXT", length = 22)
    private String compensationTypeText;
    @Column(name = "CONTRACT_NO", nullable = false, length = 100)
    private String contractNo;
    @Column(nullable = false, length = 12)
    private String country;
    @Column(name = "COVERED_PRODUCT", nullable = false, length = 250)
    private String coveredProduct;
    @Column(name = "DIVISION_STORE_NAME", nullable = false, length = 100)
    private String divisionStoreName;
    @Column(name = "DP_YEAR", nullable = false, length = 5)
    private String dpYear;
    @Column(name = "EXTRA_ITEM", nullable = false)
    private BigDecimal extraItem;
    @Column(name = "FIRST_BRAND")
    private BigDecimal firstBrand;
    @Column(name = "FIRST_NAME", nullable = false, length = 50)
    private String firstName;
    @Column(name = "FREE_LABOR", nullable = false, length = 1)
    private String freeLabor;
    @Column(name = "FREE_SPARE_PARTS_TEXT", length = 4)
    private String freeSparePartsText;
    @Column(name = "ITEM_INV_COM", nullable = false, length = 100)
    private String itemInvCom;
    @Temporal(TemporalType.DATE)
    @Column(name = "ITEM_INV_DATE", nullable = false)
    private Date itemInvDate;
    @Column(name = "ITEM_INV_SOURCE", nullable = false)
    private BigDecimal itemInvSource;
    @Column(name = "ITEM_INVOICE_LINE_NO", nullable = false)
    private BigDecimal itemInvoiceLineNo;
    @Column(name = "ITEM_SERIAL_1", length = 40)
    private String itemSerial1;
    @Column(name = "ITEM_SERIAL_2")
    private BigDecimal itemSerial2;
    @Column(name = "ITEM_SKU", nullable = false, length = 25)
    private String itemSku;
    @Column(name = "LAST_NAME", nullable = false, length = 50)
    private String lastName;
    @Column(name = "LAST_UPDATE_DATE", nullable = false)
    private Timestamp lastUpdateDate;
    @Column(nullable = false, length = 40)
    private String mobile;
    @Column(name = "NO_OF_VISITS")
    private BigDecimal noOfVisits;
    @Column(name = "NO_OF_YEARS", nullable = false)
    private BigDecimal noOfYears;
    @Column(name = "NUM_OF_PREV_MAINTENANCE_AVAIL", nullable = false)
    private BigDecimal numOfPrevMaintenanceAvail;
    @Column(name = "NUM_OF_RE_INSTALLATION_AVAIL", nullable = false)
    private BigDecimal numOfReInstallationAvail;
    @Column(name = "NUMBER_OF_CLEANING_VISITS", nullable = false)
    private BigDecimal numberOfCleaningVisits;
    @Column(name = "NUMBER_OF_OTHER_VISITS", nullable = false)
    private BigDecimal numberOfOtherVisits;
    @Column(name = "ON_SITE", nullable = false, length = 1)
    private String onSite;
    @Column(name = "ON_SITE_VISIT_FLAG", nullable = false, length = 1)
    private String onSiteVisitFlag;
    @Column(name = "OPER_UNIT_ID", nullable = false)
    private BigDecimal operUnitId;
    @Column(name = "ORG_ID", nullable = false, length = 5)
    private String orgId;
    @Column(name = "ORG_SW_VERSION", length = 30)
    private String orgSwVersion;
    @Column(name = "REPLACEMENT_GUARANTEE", nullable = false, length = 1)
    private String replacementGuarantee;
    @Column(name = "RETURN_REASON", length = 40)
    private String returnReason;
    @Column(name = "RETURNED_BSN_DATE", nullable = false, length = 10)
    private String returnedBsnDate;
    @Column(name = "RQST_COMMENTS", length = 30)
    private String rqstComments;
    @Column(name = "SALESMAN_ID", nullable = false, length = 10)
    private String salesmanId;
    @Column(name = "SEC_PHN_BRAND")
    private BigDecimal secPhnBrand;
    @Column(name = "SEC_PHN_MOD_NO")
    private BigDecimal secPhnModNo;
    @Column(name = "SERVICE_PACKAGE_NAME", nullable = false, length = 100)
    private String servicePackageName;
    @Column(name = "SERVICE_STATUS", nullable = false, length = 12)
    private String serviceStatus;
    @Column(name = "SOFTWARE_KEY", length = 100)
    private String softwareKey;
    @Temporal(TemporalType.DATE)
    @Column(name = "SRV_END_DATE", nullable = false)
    private Date srvEndDate;
    @Id
    @Column(name = "SRV_ID", nullable = false)
    private BigDecimal srvId;
    @Column(name = "SRV_INV_COM", nullable = false, length = 100)
    private String srvInvCom;
    @Column(name = "SRV_INV_NO", nullable = false, length = 100)
    private String srvInvNo;
    @Column(name = "SRV_INV_SOURCE", nullable = false)
    private BigDecimal srvInvSource;
    @Column(name = "SRV_INVOICE_LINE_NO", nullable = false, length = 100)
    private String srvInvoiceLineNo;
    @Column(name = "SRV_PRICE", nullable = false)
    private BigDecimal srvPrice;
    @Column(name = "SRV_SKU", nullable = false, length = 25)
    private String srvSku;
    @Temporal(TemporalType.DATE)
    @Column(name = "SRV_START_DATE", nullable = false)
    private Date srvStartDate;
    @Column(name = "STATUS_DESC", nullable = false, length = 30)
    private String statusDesc;
    @Column(name = "TOTAL_VISITS", nullable = false)
    private BigDecimal totalVisits;
    @Temporal(TemporalType.DATE)
    @Column(name = "VAS_CREATED_DATE", nullable = false)
    private Date vasCreatedDate;
    @Column(name = "VAS_GROUP", nullable = false, length = 50)
    private String vasGroup;
    @Column(name = "VAS_TYPE", nullable = false, length = 50)
    private String vasType;

    public VasContractsEcom() {
    }

    public VasContractsEcom(String allowLoaner, String backupCd, String compensationTypeText, String contractNo,
                            String country, String coveredProduct, String divisionStoreName, String dpYear,
                            BigDecimal extraItem, BigDecimal firstBrand, String firstName, String freeLabor,
                            String freeSparePartsText, String itemInvCom, Date itemInvDate, BigDecimal itemInvSource,
                            BigDecimal itemInvoiceLineNo, String itemSerial1, BigDecimal itemSerial2, String itemSku,
                            String lastName, Timestamp lastUpdateDate, String mobile, BigDecimal noOfVisits,
                            BigDecimal noOfYears, BigDecimal numOfPrevMaintenanceAvail,
                            BigDecimal numOfReInstallationAvail, BigDecimal numberOfCleaningVisits,
                            BigDecimal numberOfOtherVisits, String onSite, String onSiteVisitFlag,
                            BigDecimal operUnitId, String orgId, String orgSwVersion, String replacementGuarantee,
                            String returnReason, String returnedBsnDate, String rqstComments, String salesmanId,
                            BigDecimal secPhnBrand, BigDecimal secPhnModNo, String servicePackageName,
                            String serviceStatus, String softwareKey, Date srvEndDate, BigDecimal srvId,
                            String srvInvCom, String srvInvNo, BigDecimal srvInvSource, String srvInvoiceLineNo,
                            BigDecimal srvPrice, String srvSku, Date srvStartDate, String statusDesc,
                            BigDecimal totalVisits, Date vasCreatedDate, String vasGroup, String vasType) {
        this.allowLoaner = allowLoaner;
        this.backupCd = backupCd;
        this.compensationTypeText = compensationTypeText;
        this.contractNo = contractNo;
        this.country = country;
        this.coveredProduct = coveredProduct;
        this.divisionStoreName = divisionStoreName;
        this.dpYear = dpYear;
        this.extraItem = extraItem;
        this.firstBrand = firstBrand;
        this.firstName = firstName;
        this.freeLabor = freeLabor;
        this.freeSparePartsText = freeSparePartsText;
        this.itemInvCom = itemInvCom;
        this.itemInvDate = itemInvDate;
        this.itemInvSource = itemInvSource;
        this.itemInvoiceLineNo = itemInvoiceLineNo;
        this.itemSerial1 = itemSerial1;
        this.itemSerial2 = itemSerial2;
        this.itemSku = itemSku;
        this.lastName = lastName;
        this.lastUpdateDate = lastUpdateDate;
        this.mobile = mobile;
        this.noOfVisits = noOfVisits;
        this.noOfYears = noOfYears;
        this.numOfPrevMaintenanceAvail = numOfPrevMaintenanceAvail;
        this.numOfReInstallationAvail = numOfReInstallationAvail;
        this.numberOfCleaningVisits = numberOfCleaningVisits;
        this.numberOfOtherVisits = numberOfOtherVisits;
        this.onSite = onSite;
        this.onSiteVisitFlag = onSiteVisitFlag;
        this.operUnitId = operUnitId;
        this.orgId = orgId;
        this.orgSwVersion = orgSwVersion;
        this.replacementGuarantee = replacementGuarantee;
        this.returnReason = returnReason;
        this.returnedBsnDate = returnedBsnDate;
        this.rqstComments = rqstComments;
        this.salesmanId = salesmanId;
        this.secPhnBrand = secPhnBrand;
        this.secPhnModNo = secPhnModNo;
        this.servicePackageName = servicePackageName;
        this.serviceStatus = serviceStatus;
        this.softwareKey = softwareKey;
        this.srvEndDate = srvEndDate;
        this.srvId = srvId;
        this.srvInvCom = srvInvCom;
        this.srvInvNo = srvInvNo;
        this.srvInvSource = srvInvSource;
        this.srvInvoiceLineNo = srvInvoiceLineNo;
        this.srvPrice = srvPrice;
        this.srvSku = srvSku;
        this.srvStartDate = srvStartDate;
        this.statusDesc = statusDesc;
        this.totalVisits = totalVisits;
        this.vasCreatedDate = vasCreatedDate;
        this.vasGroup = vasGroup;
        this.vasType = vasType;
    }

    public String getAllowLoaner() {
        return allowLoaner;
    }

    public void setAllowLoaner(String allowLoaner) {
        this.allowLoaner = allowLoaner;
    }

    public String getBackupCd() {
        return backupCd;
    }

    public void setBackupCd(String backupCd) {
        this.backupCd = backupCd;
    }

    public String getCompensationTypeText() {
        return compensationTypeText;
    }

    public void setCompensationTypeText(String compensationTypeText) {
        this.compensationTypeText = compensationTypeText;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCoveredProduct() {
        return coveredProduct;
    }

    public void setCoveredProduct(String coveredProduct) {
        this.coveredProduct = coveredProduct;
    }

    public String getDivisionStoreName() {
        return divisionStoreName;
    }

    public void setDivisionStoreName(String divisionStoreName) {
        this.divisionStoreName = divisionStoreName;
    }

    public String getDpYear() {
        return dpYear;
    }

    public void setDpYear(String dpYear) {
        this.dpYear = dpYear;
    }

    public BigDecimal getExtraItem() {
        return extraItem;
    }

    public void setExtraItem(BigDecimal extraItem) {
        this.extraItem = extraItem;
    }

    public BigDecimal getFirstBrand() {
        return firstBrand;
    }

    public void setFirstBrand(BigDecimal firstBrand) {
        this.firstBrand = firstBrand;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFreeLabor() {
        return freeLabor;
    }

    public void setFreeLabor(String freeLabor) {
        this.freeLabor = freeLabor;
    }

    public String getFreeSparePartsText() {
        return freeSparePartsText;
    }

    public void setFreeSparePartsText(String freeSparePartsText) {
        this.freeSparePartsText = freeSparePartsText;
    }

    public String getItemInvCom() {
        return itemInvCom;
    }

    public void setItemInvCom(String itemInvCom) {
        this.itemInvCom = itemInvCom;
    }

    public Date getItemInvDate() {
        return itemInvDate;
    }

    public void setItemInvDate(Date itemInvDate) {
        this.itemInvDate = itemInvDate;
    }

    public BigDecimal getItemInvSource() {
        return itemInvSource;
    }

    public void setItemInvSource(BigDecimal itemInvSource) {
        this.itemInvSource = itemInvSource;
    }

    public BigDecimal getItemInvoiceLineNo() {
        return itemInvoiceLineNo;
    }

    public void setItemInvoiceLineNo(BigDecimal itemInvoiceLineNo) {
        this.itemInvoiceLineNo = itemInvoiceLineNo;
    }

    public String getItemSerial1() {
        return itemSerial1;
    }

    public void setItemSerial1(String itemSerial1) {
        this.itemSerial1 = itemSerial1;
    }

    public BigDecimal getItemSerial2() {
        return itemSerial2;
    }

    public void setItemSerial2(BigDecimal itemSerial2) {
        this.itemSerial2 = itemSerial2;
    }

    public String getItemSku() {
        return itemSku;
    }

    public void setItemSku(String itemSku) {
        this.itemSku = itemSku;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Timestamp getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Timestamp lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public BigDecimal getNoOfVisits() {
        return noOfVisits;
    }

    public void setNoOfVisits(BigDecimal noOfVisits) {
        this.noOfVisits = noOfVisits;
    }

    public BigDecimal getNoOfYears() {
        return noOfYears;
    }

    public void setNoOfYears(BigDecimal noOfYears) {
        this.noOfYears = noOfYears;
    }

    public BigDecimal getNumOfPrevMaintenanceAvail() {
        return numOfPrevMaintenanceAvail;
    }

    public void setNumOfPrevMaintenanceAvail(BigDecimal numOfPrevMaintenanceAvail) {
        this.numOfPrevMaintenanceAvail = numOfPrevMaintenanceAvail;
    }

    public BigDecimal getNumOfReInstallationAvail() {
        return numOfReInstallationAvail;
    }

    public void setNumOfReInstallationAvail(BigDecimal numOfReInstallationAvail) {
        this.numOfReInstallationAvail = numOfReInstallationAvail;
    }

    public BigDecimal getNumberOfCleaningVisits() {
        return numberOfCleaningVisits;
    }

    public void setNumberOfCleaningVisits(BigDecimal numberOfCleaningVisits) {
        this.numberOfCleaningVisits = numberOfCleaningVisits;
    }

    public BigDecimal getNumberOfOtherVisits() {
        return numberOfOtherVisits;
    }

    public void setNumberOfOtherVisits(BigDecimal numberOfOtherVisits) {
        this.numberOfOtherVisits = numberOfOtherVisits;
    }

    public String getOnSite() {
        return onSite;
    }

    public void setOnSite(String onSite) {
        this.onSite = onSite;
    }

    public String getOnSiteVisitFlag() {
        return onSiteVisitFlag;
    }

    public void setOnSiteVisitFlag(String onSiteVisitFlag) {
        this.onSiteVisitFlag = onSiteVisitFlag;
    }

    public BigDecimal getOperUnitId() {
        return operUnitId;
    }

    public void setOperUnitId(BigDecimal operUnitId) {
        this.operUnitId = operUnitId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgSwVersion() {
        return orgSwVersion;
    }

    public void setOrgSwVersion(String orgSwVersion) {
        this.orgSwVersion = orgSwVersion;
    }

    public String getReplacementGuarantee() {
        return replacementGuarantee;
    }

    public void setReplacementGuarantee(String replacementGuarantee) {
        this.replacementGuarantee = replacementGuarantee;
    }

    public String getReturnReason() {
        return returnReason;
    }

    public void setReturnReason(String returnReason) {
        this.returnReason = returnReason;
    }

    public String getReturnedBsnDate() {
        return returnedBsnDate;
    }

    public void setReturnedBsnDate(String returnedBsnDate) {
        this.returnedBsnDate = returnedBsnDate;
    }

    public String getRqstComments() {
        return rqstComments;
    }

    public void setRqstComments(String rqstComments) {
        this.rqstComments = rqstComments;
    }

    public String getSalesmanId() {
        return salesmanId;
    }

    public void setSalesmanId(String salesmanId) {
        this.salesmanId = salesmanId;
    }

    public BigDecimal getSecPhnBrand() {
        return secPhnBrand;
    }

    public void setSecPhnBrand(BigDecimal secPhnBrand) {
        this.secPhnBrand = secPhnBrand;
    }

    public BigDecimal getSecPhnModNo() {
        return secPhnModNo;
    }

    public void setSecPhnModNo(BigDecimal secPhnModNo) {
        this.secPhnModNo = secPhnModNo;
    }

    public String getServicePackageName() {
        return servicePackageName;
    }

    public void setServicePackageName(String servicePackageName) {
        this.servicePackageName = servicePackageName;
    }

    public String getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(String serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public String getSoftwareKey() {
        return softwareKey;
    }

    public void setSoftwareKey(String softwareKey) {
        this.softwareKey = softwareKey;
    }

    public Date getSrvEndDate() {
        return srvEndDate;
    }

    public void setSrvEndDate(Date srvEndDate) {
        this.srvEndDate = srvEndDate;
    }

    public BigDecimal getSrvId() {
        return srvId;
    }

    public void setSrvId(BigDecimal srvId) {
        this.srvId = srvId;
    }

    public String getSrvInvCom() {
        return srvInvCom;
    }

    public void setSrvInvCom(String srvInvCom) {
        this.srvInvCom = srvInvCom;
    }

    public String getSrvInvNo() {
        return srvInvNo;
    }

    public void setSrvInvNo(String srvInvNo) {
        this.srvInvNo = srvInvNo;
    }

    public BigDecimal getSrvInvSource() {
        return srvInvSource;
    }

    public void setSrvInvSource(BigDecimal srvInvSource) {
        this.srvInvSource = srvInvSource;
    }

    public String getSrvInvoiceLineNo() {
        return srvInvoiceLineNo;
    }

    public void setSrvInvoiceLineNo(String srvInvoiceLineNo) {
        this.srvInvoiceLineNo = srvInvoiceLineNo;
    }

    public BigDecimal getSrvPrice() {
        return srvPrice;
    }

    public void setSrvPrice(BigDecimal srvPrice) {
        this.srvPrice = srvPrice;
    }

    public String getSrvSku() {
        return srvSku;
    }

    public void setSrvSku(String srvSku) {
        this.srvSku = srvSku;
    }

    public Date getSrvStartDate() {
        return srvStartDate;
    }

    public void setSrvStartDate(Date srvStartDate) {
        this.srvStartDate = srvStartDate;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public BigDecimal getTotalVisits() {
        return totalVisits;
    }

    public void setTotalVisits(BigDecimal totalVisits) {
        this.totalVisits = totalVisits;
    }

    public Date getVasCreatedDate() {
        return vasCreatedDate;
    }

    public void setVasCreatedDate(Date vasCreatedDate) {
        this.vasCreatedDate = vasCreatedDate;
    }

    public String getVasGroup() {
        return vasGroup;
    }

    public void setVasGroup(String vasGroup) {
        this.vasGroup = vasGroup;
    }

    public String getVasType() {
        return vasType;
    }

    public void setVasType(String vasType) {
        this.vasType = vasType;
    }
}
