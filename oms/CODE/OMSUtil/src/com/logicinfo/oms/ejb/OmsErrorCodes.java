package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQueries( { @NamedQuery(name = "OmsErrorCodes.findAll", query = "select o from OmsErrorCodes o"),
                 @NamedQuery(name = "OmsErrorCodes.findByErrorCode",query = "select o from OmsErrorCodes o where o.omsErrorCode=:omsErrorCode and o.langCode=:langCode"),
                 @NamedQuery(name = "OmsErrorCodes.findByindByonlyErrorCode",query = "select o.omsErrLangDesc from OmsErrorCodes o where o.omsErrorCode=:omsErrorCode")
              })
@Table(name = "OMS_ERROR_CODES")
public class OmsErrorCodes implements Serializable {
    @Column(name = "CREATE_DATETIME", nullable = false)
    private Timestamp createDatetime;
    @Column(name = "CREATED_BY", length = 40)
    private String createdBy;
    @Column(name = "LANG_CODE", nullable = false, length = 3)
    private String langCode;
    @Column(name = "OMS_ERR_LANG_DESC", nullable = false)
    private String omsErrLangDesc;
    @Id
    @Column(name = "OMS_ERROR_CODE", nullable = false, length = 20)
    private String omsErrorCode;

    public OmsErrorCodes() {
        this.setOmsErrLangDesc("");
        this.setOmsErrorCode("");
    }

    public OmsErrorCodes(Timestamp createDatetime, String createdBy, String langCode, String omsErrLangDesc,
                         String omsErrorCode) {
        this.createDatetime = createDatetime;
        this.createdBy = createdBy;
        this.langCode = langCode;
        this.omsErrLangDesc = omsErrLangDesc;
        this.omsErrorCode = omsErrorCode;
    }

    public Timestamp getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Timestamp createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLangCode() {
        return langCode;
    }

    public void setLangCode(String langCode) {
        this.langCode = langCode;
    }

    public String getOmsErrLangDesc() {
        return omsErrLangDesc;
    }

    public void setOmsErrLangDesc(String omsErrLangDesc) {
        this.omsErrLangDesc = omsErrLangDesc;
    }

    public String getOmsErrorCode() {
        return omsErrorCode;
    }

    public void setOmsErrorCode(String omsErrorCode) {
        this.omsErrorCode = omsErrorCode;
    }
}
