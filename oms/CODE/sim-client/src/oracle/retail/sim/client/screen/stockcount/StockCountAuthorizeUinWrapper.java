package oracle.retail.sim.client.screen.stockcount;

import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.FunctionalArea;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.business.Wrapper;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.stockcount.StockCountAuthorizeSerialNumber;
import oracle.retail.sim.common.stockcount.StockCountMessageText;
import oracle.retail.sim.common.stockcount.StockCountProperty;
import oracle.retail.sim.common.stockcount.StockCountSerialNumber;
import oracle.retail.sim.common.storesequence.StoreSequenceAreaType;
import oracle.retail.sim.common.storesequence.StoreSequenceConstants;
import oracle.retail.sim.common.uin.UINStatus;
import oracle.retail.sim.common.uin.UINType;

/********************************************************************************************************
 * Stock Count Line Item Authorize Wrapper - Wraps a StockCountAuthorizeSerialNumber object specifically
 * to handle the functionality of authorization on the PC UI client side.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class StockCountAuthorizeUinWrapper extends Wrapper implements StockCountUinInterface {

    private StockCountAuthorizeSerialNumber lineItemSerialNumber;
    private UINType type;
    private String label;

    public StockCountAuthorizeUinWrapper(StockCountAuthorizeSerialNumber serialNumber, UINType type, String label) {
        lineItemSerialNumber = serialNumber;
        setUINType(type);
        setLabel(label);
    }

    public StockCountAuthorizeSerialNumber getLineItem() {
        return lineItemSerialNumber;
    }

    public String getItemId() {
        return lineItemSerialNumber.getItemId();
    }

    public String getLocation() {
        String description = lineItemSerialNumber.getLocationDescription();
        if (description == null) {
            return StringConstants.EMPTY;
        }
        if (StoreSequenceConstants.NO_SEQUENCE_DESCRIPTION.equals(description)) {
            return Translator.getText(description);
        }
        StringHelper helper = StringHelper.getInstance(LocaleManager.getLanguageLocale());
        StringBuilder buffer = new StringBuilder(helper.replace(description, ":::", " - "));
        StoreSequenceAreaType area = lineItemSerialNumber.getLocationArea();
        if (area != null) {
            buffer.append(" - ");
            buffer.append(Translator.getText(area.toString()));
        }
        return buffer.toString();
    }

    public Quantity getCountQuantity() {
        return lineItemSerialNumber.isCounted() ? Quantity.ONE : Quantity.ZERO;
    }

    public Quantity getRecountQuantity() {
        return lineItemSerialNumber.isRecounted() ? Quantity.ONE : Quantity.ZERO;
    }

    public Integer getApprovedQuantity() {
        return lineItemSerialNumber.isApproved() ? new Integer(1) : new Integer(0);
    }

    public FunctionalArea getFunctionalArea() {
        return FunctionalArea.STOCK_COUNT_AUTHORIZED;
    }

    public UINStatus getUINStatus() {
        return lineItemSerialNumber.getUINStatus();
    }

    public UINType getType() {
        return type;
    }

    public String getLabel() {
        return label;
    }

    public StockCountSerialNumber getSerialNumber() {
        return lineItemSerialNumber.getSerialNumber();
    }

    public void setUINType(UINType type) {
        this.type = type;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setSerialNumber(StockCountSerialNumber serialNumber) throws BusinessException {
        lineItemSerialNumber.doSetSerialNumber(serialNumber);
    }

    public void setApprovedQuantity(Integer quantity) throws BusinessException {
        checkForNullParameter("Authorized Quantity", quantity);
        executeRule("setApprovedQuantity", quantity);
        if (quantity < 0 || quantity > 1) {
            throw new BusinessException(StockCountMessageText.NUMBER_NOT_0_OR_1);
        }
        lineItemSerialNumber.setIsApproved(quantity == 1);
    }

    public boolean isPropertyModifiable(String property) {
        if (property.equals(StockCountProperty.SERIAL_NUMBER_VALUE)) {
            StockCountSerialNumber serialNumber = lineItemSerialNumber.getSerialNumber();
            if (serialNumber == null) {
                return true;
            }
            return StringUtility.isNullOrEmpty(serialNumber.getSerialNumber());
        }
        return property.equals(StockCountProperty.UIN_AUTHORIZED_QTY);
    }
}