package oracle.retail.sim.client.screen.productgroup;

import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.editor.SimEditorFactory;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.screen.item.ItemHierarchyPanel;
import oracle.retail.sim.client.screen.item.ItemSearchListener;
import oracle.retail.sim.client.screen.supplier.SupplierSearchListener;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RSearchFieldEditor;
import oracle.retail.sim.client.swing.editor.RTextFieldEditor;
import oracle.retail.sim.client.swing.panel.RCardPanel;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.common.item.ItemVO;
import oracle.retail.sim.common.item.ProductGroupItem;
import oracle.retail.sim.common.mdsehierarchy.MdseHierarchyNode;
import oracle.retail.sim.common.source.Supplier;

/********************************************************************************************************
 * Product Group Element Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ProductGroupElementPanel extends RCardPanel {
    private static final long serialVersionUID = -4340719949154655887L;

    private ItemHierarchyPanel hierarchyPanel = new ItemHierarchyPanel();
    private REditorPanel itemPanel = new REditorPanel(1);
    private REditorPanel supplierPanel = new REditorPanel(1);
    private REditorPanel promoPanel = new REditorPanel(1);
    private RPanel emptyPanel = new RPanel();

    private RSearchFieldEditor itemEditor = SimEditorFactory.createProductItemSearchFieldEditor();
    private RSearchFieldEditor supplierEditor = SimEditorFactory.createSupplierSearchFieldEditor();
    private RTextFieldEditor promotionIdEditor = new RTextFieldEditor("Promotion ID");

    public ProductGroupElementPanel() {
        initElementPanel();
        layoutElementPanel();
    }

    private void initElementPanel() {
        hierarchyPanel.setSizeType(EditorConstants.LARGE);
        promotionIdEditor.setSizeType(EditorConstants.LARGE);
        promotionIdEditor.setEnabled(true);
        promotionIdEditor.setIdentifier(SimName.PROMOTION_ID);
    }

    private void layoutElementPanel() {
        itemPanel.add(itemEditor);
        supplierPanel.add(supplierEditor);
        promoPanel.add(promotionIdEditor);

        addCard("HierarcyPanel", hierarchyPanel);
        addCard("ItemName", itemPanel);
        addCard("SupplierName", supplierPanel);
        addCard("PromoName", promoPanel);
        addCard("EmptyName", emptyPanel);
    }

    public void setItemSearchListener(ItemSearchListener listener) {
        itemEditor.setSearchListener(listener);
    }

    public void setSupplierSearchListener(SupplierSearchListener listener) {
        supplierEditor.setSearchListener(listener);
    }

    protected void showHierarchyPanel() {
        showCard(hierarchyPanel);
    }

    protected void showItemPanel() {
        showCard(itemPanel);
    }

    protected void showSupplierPanel() {
        showCard(supplierPanel);
    }

    protected void showPromoPanel() {
        showCard(promoPanel);
    }

    protected void showEmptyPanel() {
        showCard(emptyPanel);
    }

    protected void loadHierarchyPanel() throws Exception {
        hierarchyPanel.loadDepartments();
    }

    protected void setProductGroupItem(ItemVO item) {
        itemEditor.clear();
        itemEditor.setData(item);
    }

    protected void setSupplier(Supplier supplier) {
        supplierEditor.clear();

        if (supplier != null) {
            supplierEditor.setData(supplier);
        }
    }

    public MdseHierarchyNode getHierarchy() {
        return hierarchyPanel.getHierarchyNode();
    }

    public void clearHierarchyNode() {
        hierarchyPanel.clearSelection();
    }

    protected ProductGroupItem getProductGroupItem() {
        return (ProductGroupItem) itemEditor.getData();
    }

    protected void clearItem() {
        itemEditor.clear();
    }

    public Long getPromotionId() {
        String promotionId = promotionIdEditor.getTextOrNull();
        if (promotionId == null) {
            return null;
        }
        return Long.valueOf(StringUtility.collapseToInteger(promotionId));
    }

    public void clearPromotionId() {
        promotionIdEditor.clear();
    }

    protected Supplier getSupplier() {
        return (Supplier) supplierEditor.getData();
    }

    protected void clearSupplier() {
        supplierEditor.clear();
    }
}
