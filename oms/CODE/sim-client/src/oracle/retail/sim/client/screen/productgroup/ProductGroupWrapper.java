package oracle.retail.sim.client.screen.productgroup;

import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.lineitem.UOMMode;
import oracle.retail.sim.common.productgroup.ProductGroup;
import oracle.retail.sim.common.productgroup.ProductGroupType;
import oracle.retail.sim.common.stockcount.StockCountingMethod;
import oracle.retail.sim.common.store.Store;

/********************************************************************************************************
 * Product Group Wrapper
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ProductGroupWrapper {

    private ProductGroup productGroup;
    private final boolean isSuperUser;

    // private boolean isEditable = true;

    /****************************************************************************************************
     * Constructor
     ***************************************************************************************************/
    public ProductGroupWrapper(ProductGroup productGroup, boolean isSuperUser) {
        setProductGroup(productGroup);
        this.isSuperUser = isSuperUser;
    }

    public void setProductGroup(ProductGroup productGroup) {
        this.productGroup = productGroup;
    }

    public ProductGroup getProductGroup() {
        return productGroup;
    }

    /****************************************************************************************************
     * Basic Getters
     ***************************************************************************************************/

    public String getId() {
        if (productGroup != null && productGroup.getId() != null) {
            return String.valueOf(productGroup.getId());
        }
        return StringConstants.EMPTY;
    }

    public Store getStore() {
        if (productGroup != null) {
            return productGroup.getStore();
        }
        return null;
    }

    public String getDescription() {
        if (productGroup != null) {
            return productGroup.getDescription();
        }
        return null;
    }

    public ProductGroupType getGroupType() {
        if (productGroup == null) {
            return null;
        }
        return productGroup.getType();
    }

    public UOMMode getUomType() {
        if (productGroup == null) {
            return null;
        }
        if (productGroup.getType().isShelfReplenishment()) {
            return productGroup.getUnitOfMeasureMode();
        }
        return UOMMode.STANDARD;
    }

    public StockCountingMethod getCountingMethod() {
        return productGroup.getCountingMethod();
    }

    /****************************************************************************************************
     * Basic Type Queries
     ***************************************************************************************************/

    private boolean isStockCountUnitAmount() {
        if (productGroup != null) {
            return productGroup.getType().isStockCountUnitAmount();
        }
        return false;
    }

    private boolean isThirdPartyCountMethod() {
        return productGroup.getCountingMethod() == StockCountingMethod.THIRD_PARTY;
    }

    /****************************************************************************************************
     * IsEnabled Queries
     ***************************************************************************************************/

    private boolean isProductGroupEnabled() {
        return productGroup != null;
    }

    public boolean isGroupTypeEnabled() {
        return productGroup == null || productGroup.getId() == null;
    }

    public boolean isGroupDescriptionEnabled() {
        return isProductGroupEnabled();
    }

    public boolean isUOMEnabled() {
        if (productGroup != null) {
            return isProductGroupEnabled() && productGroup.getType().isShelfReplenishment();
        }
        return false;
    }

    public boolean isSingleStoreEnabled() {
        return isProductGroupEnabled() && isSuperUser;
    }

    public boolean isAllStoresEnabled() {
        return isProductGroupEnabled() && isSuperUser;
    }

    public boolean isHierarchyOptionEnabled() {
        return isProductGroupEnabled();
    }

    public boolean isItemOptionEnabled() {
        if (isStockCountUnitAmount()) {
            return false;
        }
        return isProductGroupEnabled();
    }

    public boolean isSupplierOptionEnabled() {
        if (isProductGroupEnabled()) {
            if (isStockCountUnitAmount() || isThirdPartyCountMethod()) {
                return false;
            }
            return true;
        }
        return false;
    }

    public boolean isPromotionIdOptionEnabled() {
        if (isProductGroupEnabled()) {
            if (isStockCountUnitAmount() || isThirdPartyCountMethod()) {
                return false;
            }
            return true;
        }
        return false;
    }

    public boolean isAllDepartmentOptionEnabled() {
        if (isProductGroupEnabled()) {
            ProductGroupType type = getGroupType();
            if (type.isShelfReplenishment() || type.isItemRequest() || type.isWastage()) {
                return false;
            }
            return true;
        }
        return false;
    }
}
