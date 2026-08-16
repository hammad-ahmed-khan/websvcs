package oracle.retail.sim.client.uom;

import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.screen.storesequence.StoreSequenceItemWrapper;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.type.AbstractDisplayer;
import oracle.retail.sim.common.item.ItemDetailVO;
import oracle.retail.sim.common.lineitem.UOMConstants;
import oracle.retail.sim.common.lineitem.UOMMode;
import oracle.retail.sim.common.lineitem.UnitOfMeasureWrapper;
import oracle.retail.sim.common.storesequence.StoreSequenceItem;

/********************************************************************************************************
 * Displays the correct UOM of the object input.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class UomModeDisplayer extends AbstractDisplayer {
    private Object displayerModel;

    public void setModel(Object model) {
        displayerModel = model;
    }

    public String getDisplayText(Object value, Object model) {
        return getUomDescription(value, model);
    }

    public String getDisplayText(Object value) {
        return getUomDescription(value, displayerModel);
    }

    private String getUomDescription(Object value, Object model) {
        if (value == null) {
            return StringConstants.EMPTY;
        }
        // Do the special stuff for standard uom:
        if (value == UOMMode.STANDARD) {
            if (model instanceof UnitOfMeasureWrapper) {
                UnitOfMeasureWrapper wrapper = (UnitOfMeasureWrapper) model;
                return getItemUom(wrapper.getStandardUnitOfMeasure());
            }
            if (model instanceof ItemDetailVO) {
                ItemDetailVO vo = (ItemDetailVO) model;
                return getItemUom(vo.getUnitOfMeasure());
            }
            if (model instanceof StoreSequenceItem) {
                StoreSequenceItem sequenceItem = (StoreSequenceItem) model;
                return getItemUom(sequenceItem.getUnitOfMeasure());
            }
            if (model instanceof StoreSequenceItemWrapper) {
                StoreSequenceItem sequenceItem = ((StoreSequenceItemWrapper) model).getStoreSequenceItem();
                return getItemUom(sequenceItem.getUnitOfMeasure());
            }
            return Translator.getText(UOMConstants.UNITS);
        }
        if (value == UOMMode.PREFERRED) {
            if (model instanceof UnitOfMeasureWrapper) {
                UnitOfMeasureWrapper wrapper = (UnitOfMeasureWrapper) model;
                return getItemUom(wrapper.getPreferredUnitOfMeasure());
            }
            return Translator.getText(UOMConstants.PREFERRED);
        }
        return Translator.getText(UOMConstants.CASES);
    }

    private String getItemUom(String value) {
        String unitOfMeasure = StringUtility.trimToNull(value);
        if (unitOfMeasure == null || UOMConstants.EACHES.equals(unitOfMeasure)) {
            return Translator.getText(UOMConstants.UNITS);
        }
        return unitOfMeasure;
    }
}
