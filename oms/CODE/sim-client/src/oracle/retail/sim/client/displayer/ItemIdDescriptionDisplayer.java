package oracle.retail.sim.client.displayer;

import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.type.AbstractDisplayer;
import oracle.retail.sim.common.item.ItemVO;
import oracle.retail.sim.common.item.ProductGroupItem;
import oracle.retail.sim.common.item.RetailItem;
import oracle.retail.sim.common.item.StockCountItem;
import oracle.retail.sim.common.item.StockItem;

/********************************************************************************************************
 * Displays a short or long description of a stockable object based on the configuration settings.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ItemIdDescriptionDisplayer extends AbstractDisplayer {

    private static final String DASH = " - ";

    public String getDisplayText(Object value) {
        if (value instanceof StockItem) {
            return getDescription((StockItem) value);
        } else if (value instanceof StockCountItem) {
            return getDescription((StockCountItem) value);
        } else if (value instanceof ItemVO) {
            return getDescription((ItemVO) value);
        } else if (value instanceof ProductGroupItem) {
            return getDescription((ProductGroupItem) value);
        } else if (value instanceof RetailItem) {
            return getDescription((RetailItem) value);
        }
        return StringConstants.EMPTY;
    }

    private String getDescription(StockItem item) {
        StringBuilder buffer = new StringBuilder();
        buffer.append(item.getId());
        buffer.append(DASH);
        if (SimConfigManager.isItemShortDescription()) {
            buffer.append(item.getShortDescription());
        } else {
            buffer.append(item.getLongDescription());
        }
        return buffer.toString();
    }

    private String getDescription(StockCountItem item) {
        StringBuilder buffer = new StringBuilder();
        buffer.append(item.getId());
        buffer.append(DASH);
        if (SimConfigManager.isItemShortDescription()) {
            buffer.append(item.getShortDescription());
        } else {
            buffer.append(item.getLongDescription());
        }
        return buffer.toString();
    }

    private String getDescription(ItemVO item) {
        StringBuilder buffer = new StringBuilder();
        buffer.append(item.getId());
        buffer.append(DASH);
        if (SimConfigManager.isItemShortDescription()) {
            buffer.append(item.getShortDescription());
        } else {
            buffer.append(item.getLongDescription());
        }
        return buffer.toString();
    }

    private String getDescription(ProductGroupItem item) {
        StringBuilder buffer = new StringBuilder();
        buffer.append(item.getId());
        buffer.append(DASH);
        if (SimConfigManager.isItemShortDescription()) {
            buffer.append(item.getShortDescription());
        } else {
            buffer.append(item.getLongDescription());
        }
        return buffer.toString();
    }

    private String getDescription(RetailItem item) {
        StringBuilder buffer = new StringBuilder();
        buffer.append(item.getId());
        buffer.append(DASH);
        if (SimConfigManager.isItemShortDescription()) {
            buffer.append(item.getShortDescription());
        } else {
            buffer.append(item.getLongDescription());
        }
        return buffer.toString();
    }
}
