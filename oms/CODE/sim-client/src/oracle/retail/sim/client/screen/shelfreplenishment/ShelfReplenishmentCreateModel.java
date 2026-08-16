package oracle.retail.sim.client.screen.shelfreplenishment;

import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.lineitem.UOMMode;
import oracle.retail.sim.common.productgroup.ProductGroup;
import oracle.retail.sim.common.productgroup.ProductGroupQueryFilter;
import oracle.retail.sim.common.productgroup.ProductGroupType;
import oracle.retail.sim.common.productgroup.ProductGroupVO;
import oracle.retail.sim.common.shelfreplenishment.ShelfReplenishment;
import oracle.retail.sim.common.shelfreplenishment.ShelfReplenishmentProperty;
import oracle.retail.sim.common.shelfreplenishment.ShelfReplenishmentType;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Shelf Replenishment List Create Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ShelfReplenishmentCreateModel extends SimScreenModel {
    private ShelfReplenishment shelfReplenishment;

    public List<ProductGroupVO> findProductGroups() throws Exception {
        ProductGroupQueryFilter filter = BOFactory.createProductGroupQueryFilter();
        filter.doSetProductGroupType(ProductGroupType.SHELF_REPLENISHMENT);
        filter.doSetStoreId(getStoreId());

        return ClientServiceFactory.getProductGroupServices().findProductGroupVOs(filter);
    }

    public ProductGroup readShelfReplenishmentProductGroup(ProductGroupVO productGroupVO) throws Exception {
        return ClientServiceFactory.getProductGroupServices().readProductGroup(productGroupVO.getId());
    }

    public List<ShelfReplenishmentType> findShelfReplenishmentTypes() {
        List<ShelfReplenishmentType> typeList = new ArrayList<>(2);
        typeList.add(ShelfReplenishmentType.WITHIN_DAY);
        typeList.add(ShelfReplenishmentType.END_OF_DAY);
        return typeList;
    }

    public ShelfReplenishment createShelfReplenishment() throws Exception {
        return ClientServiceFactory.getShelfReplenishmentServices().createShelfReplenishment(shelfReplenishment);
    }

    public void createShelfReplenishment(ShelfReplenishmentType shelfReplenishmentType) {
        shelfReplenishment = BOFactory.createShelfReplenishment(getStoreId());
        shelfReplenishment.doSetType(shelfReplenishmentType);
    }

    public void clearShelfReplenishment() {
        shelfReplenishment = null;
    }

    public ShelfReplenishment getShelfReplenishment() {
        return shelfReplenishment;
    }

    public UOMMode getUOMMode() {
        if (shelfReplenishment != null) {
            return shelfReplenishment.getUnitOfMeasureMode();
        }
        return null;
    }

    public boolean isProductGroupEditable() {
        return shelfReplenishment != null && shelfReplenishment.getProductGroupId() == null;
    }

    public boolean isAmountEditable() {
        return shelfReplenishment != null && shelfReplenishment.isPropertyModifiable(ShelfReplenishmentProperty.QUANTITY_TO_REPLENISH);
    }
}
