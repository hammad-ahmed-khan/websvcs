package oracle.retail.sim.client.screen.stockcount;

import java.math.BigDecimal;
import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.lineitem.UnitOfMeasureWrapper;
import oracle.retail.sim.common.stockcount.StockCountLineItemAreaBreakdownVO;
import oracle.retail.sim.common.storesequence.StoreSequenceAreaType;
import oracle.retail.sim.common.storesequence.StoreSequenceConstants;

/********************************************************************************************************
 * Stock Count Line Item Authorize Wrapper - Wraps a StockCountLineItemAuthVO object specifically
 * to handle the functionality of authorization on the PC UI client side.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class StockCountLineItemAuthWrapper extends UnitOfMeasureWrapper {

    private StockCountLineItemAreaBreakdownVO lineItemVO;

    public StockCountLineItemAuthWrapper(StockCountLineItemAreaBreakdownVO vo) {
        lineItemVO = vo;
    }

    public String getItemId() {
        return lineItemVO.getItemId();
    }

    public String getLocation() {
        String description = lineItemVO.getLocationDescription();
        if (description == null) {
            return StringConstants.EMPTY;
        }
        if (StoreSequenceConstants.NO_SEQUENCE_DESCRIPTION.equals(description)) {
            return Translator.getText(description);
        }
        StringHelper helper = StringHelper.getInstance(LocaleManager.getLanguageLocale());
        StringBuilder buffer = new StringBuilder(helper.replace(description, ":::", " - "));
        StoreSequenceAreaType area = lineItemVO.getLocationArea();
        if (area != null) {
            buffer.append(" - ");
            buffer.append(Translator.getText(area.toString()));
        }
        return buffer.toString();
    }

    public String getStandardUnitOfMeasure() {
        return lineItemVO.getUnitOfMeasure();
    }

    public Quantity getStockCounted() {
        return lineItemVO.getStockCounted();
    }

    public Quantity getStockRecounted() {
        return lineItemVO.getStockRecounted();
    }

    public Quantity getStockCountedBasedOnUOM() {
        return rationalizeQuantityBasedOnUom(lineItemVO.getStockCounted());
    }

    public Quantity getStockRecountedBasedOnUOM() {
        return rationalizeQuantityBasedOnUom(lineItemVO.getStockRecounted());
    }

    public Quantity getCaseSize() {
        return lineItemVO.getCaseSize();
    }

    public boolean isPreferredUomConversionAvailable() {
        return false;
    }

    public String getPreferredUnitOfMeasure() {
        return null;
    }

    public BigDecimal getPreferredUomConversionFactor() {
        return null;
    }
}