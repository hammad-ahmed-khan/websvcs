package oracle.retail.sim.client.uom;

import java.math.BigDecimal;
import oracle.retail.sim.client.util.ClientDataCacheUtility;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.item.StockItem;
import oracle.retail.sim.common.lineitem.UOMConstants;

/**
 * Unit Of Measure Wireless Utility
 * <p>
 * Contains helper methods for handling unit of measure tasks.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 */
public class UomUtility {

    /**
     * Attempts to the conversion factor between standard unit of measure of an item and a target unit of measure.
     * If the method is unable to determine a valid conversion factor, it returns BigDecimal.ONE.
     */
    public static BigDecimal getStandardUomToTargetUom(StockItem stockItem, String targetUom) throws Exception {
        if (StringHelper.isNullOrEmpty(targetUom) || StringHelper.isNullOrEmpty(stockItem.getUnitOfMeasure())) {
            return BigDecimal.ONE;
        }
        if (stockItem.getUnitOfMeasure().equals(targetUom)) {
            return BigDecimal.ONE;
        }
        if (UOMConstants.EACHES.equals(stockItem.getUnitOfMeasure())) {
            return BigDecimal.ONE;
        }
        if (UOMConstants.EACHES.equals(targetUom)) {
            BigDecimal factor = stockItem.getEachToUomConversion();
            if (factor != null && factor.compareTo(BigDecimal.ZERO) > 0) {
                return Quantity.ONE.divide(factor).getBigDecimal();
            }
            return BigDecimal.ONE;
        }
        BigDecimal factor = ClientDataCacheUtility.getUomConversionFactor(stockItem.getUnitOfMeasure(), targetUom);
        return factor != null ? factor : BigDecimal.ONE;
    }
}
