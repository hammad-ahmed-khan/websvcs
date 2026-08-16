package oracle.retail.sim.client.screen.tranhistory;

import java.util.Date;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.tranhistory.TransactionHistoryVO;
import oracle.retail.sim.common.tranhistory.TransactionType;

/********************************************************************************************************
 * Transaction History VO Wrapper
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TransactionHistoryVOWrapper {

    private TransactionHistoryVO historyVO;

    public TransactionHistoryVOWrapper(TransactionHistoryVO historyVO) {
        this.historyVO = historyVO;
    }

    public Date getTimestamp() {
        return historyVO.getTimestamp();
    }

    public TransactionType getType() {
        return historyVO.getType();
    }

    public String getTransactionId() {
        return historyVO.getTransactionId();
    }

    public String getUsername() {
        return historyVO.getUsername();
    }

    public String getItemId() {
        return historyVO.getItemId();
    }

    public String getItemDescription() {
        return historyVO.getItemDescription();
    }

    public String getTransactionDescription() {
        boolean appendReasonCode = false;
        if(TransactionType.getValidTypesForTransactionHistory().contains(historyVO.getType())) {
            appendReasonCode = true;
            if(historyVO.getType() == TransactionType.POS_TRANSACTION) {
                if(historyVO.getNonSellableMovement() == null || !historyVO.getNonSellableMovement().isPositive()) {
                    appendReasonCode = false;
                }
            }
        }
        if(appendReasonCode) {
            return historyVO.getReasonCode() + " - " + Translator.getText(historyVO.getDescription());
        }
        return Translator.getText(historyVO.getDescription());
    }

    public Quantity getStockOnHandMovement() {
        return historyVO.getStockOnHandMovement();
    }

    public Quantity getNonSellableMovement() {
        return historyVO.getNonSellableMovement();
    }
}
