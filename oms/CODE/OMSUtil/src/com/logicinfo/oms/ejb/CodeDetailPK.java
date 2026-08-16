package com.logicinfo.oms.ejb;

import java.io.Serializable;

public class CodeDetailPK implements Serializable {
    public String code;
    public String codeType;

    public CodeDetailPK() {
    }

    public CodeDetailPK(String code, String codeType) {
        this.code = code;
        this.codeType = codeType;
    }

    public boolean equals(Object other) {
        if (other instanceof CodeDetailPK) {
            final CodeDetailPK otherCodeDetailPK = (CodeDetailPK)other;
            final boolean areEqual =
                (otherCodeDetailPK.code.equals(code) && otherCodeDetailPK.codeType.equals(codeType));
            return areEqual;
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }
}
