package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQueries( { @NamedQuery(name = "CodeDetail.findAll", query = "select o from CodeDetail o"),@NamedQuery(name = "CodeDetail.findCode", query = "select o.code from CodeDetail o where o.codeType=:codeType") })
@Table(name = "CODE_DETAIL")
@IdClass(CodeDetailPK.class)
public class CodeDetail implements Serializable {
    @Id
    @Column(nullable = false, length = 6)
    private String code;
    @Column(name = "CODE_DESC", nullable = false, length = 250)
    private String codeDesc;
    @Column(name = "CODE_SEQ", nullable = false, unique = true)
    private BigDecimal codeSeq;
    @Id
    @Column(name = "CODE_TYPE", nullable = false, unique = true, length = 4)
    private String codeType;
    @Column(name = "REQUIRED_IND", nullable = false, length = 1)
    private String requiredInd;

    public CodeDetail() {
    }

    public CodeDetail(String code, String codeDesc, BigDecimal codeSeq, String codeType, String requiredInd) {
        this.code = code;
        this.codeDesc = codeDesc;
        this.codeSeq = codeSeq;
        this.codeType = codeType;
        this.requiredInd = requiredInd;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCodeDesc() {
        return codeDesc;
    }

    public void setCodeDesc(String codeDesc) {
        this.codeDesc = codeDesc;
    }

    public BigDecimal getCodeSeq() {
        return codeSeq;
    }

    public void setCodeSeq(BigDecimal codeSeq) {
        this.codeSeq = codeSeq;
    }

    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }

    public String getRequiredInd() {
        return requiredInd;
    }

    public void setRequiredInd(String requiredInd) {
        this.requiredInd = requiredInd;
    }
}
