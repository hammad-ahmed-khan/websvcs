package com.logicinfo.oms.ejb;

import java.io.Serializable;

public class OmsOrposCheckTenderPK implements Serializable {
    public String accountNumber;
    public String bankId;
    public String micrNumber;

    public OmsOrposCheckTenderPK() {
    }

    public OmsOrposCheckTenderPK(String accountNumber, String bankId, String micrNumber) {
        this.accountNumber = accountNumber;
        this.bankId = bankId;
        this.micrNumber = micrNumber;
    }

    public boolean equals(Object other) {
        if (other instanceof OmsOrposCheckTenderPK) {
            final OmsOrposCheckTenderPK otherOmsOrposCheckTenderPK = (OmsOrposCheckTenderPK)other;
            final boolean areEqual =
                (otherOmsOrposCheckTenderPK.accountNumber.equals(accountNumber) && otherOmsOrposCheckTenderPK.bankId.equals(bankId) &&
                 otherOmsOrposCheckTenderPK.micrNumber.equals(micrNumber));
            return areEqual;
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getMicrNumber() {
        return micrNumber;
    }

    public void setMicrNumber(String micrNumber) {
        this.micrNumber = micrNumber;
    }
}
