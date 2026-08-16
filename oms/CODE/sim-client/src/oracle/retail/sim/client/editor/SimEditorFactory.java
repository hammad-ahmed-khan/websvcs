package oracle.retail.sim.client.editor;

import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.displayer.OrdinalDisplayer;
import oracle.retail.sim.client.displayer.StoreDisplayer;
import oracle.retail.sim.client.displayer.StoreNameComparator;
import oracle.retail.sim.client.swing.displayer.AttributeComparator;
import oracle.retail.sim.client.swing.displayer.DayOfWeekDisplayer;
import oracle.retail.sim.client.swing.displayer.DualAttributeDisplayer;
import oracle.retail.sim.client.swing.displayer.MonthDisplayer;
import oracle.retail.sim.client.swing.editor.RComboBoxEditor;
import oracle.retail.sim.client.swing.editor.RDateFieldEditor;
import oracle.retail.sim.client.swing.editor.RSearchComboEditor;
import oracle.retail.sim.client.swing.editor.RSearchFieldEditor;
import oracle.retail.sim.client.util.SimEnumUtility;
import oracle.retail.sim.common.business.MessageText;
import oracle.retail.sim.common.date.SimDateUtil;

/********************************************************************************************************
 * This static factory helps build common editors.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class SimEditorFactory {
    /****************************************************************************************************
     * Private Constructor For Static Class
     ***************************************************************************************************/
    private SimEditorFactory() {
    }

    /****************************************************************************************************
     * Creates a supplier search field editor.
     ***************************************************************************************************/
    public static RSearchFieldEditor createSupplierSearchFieldEditor() {
        return createSupplierSearchFieldEditor("Supplier");
    }

    /****************************************************************************************************
     * Creates a supplier search field editor.
     ***************************************************************************************************/
    public static RSearchFieldEditor createSupplierSearchFieldEditor(String title) {
        RSearchFieldEditor editor = new RSearchFieldEditor(title);
        editor.setIdentifier(SimName.SUPPLIER_ID);
        editor.setSearchProcessor(new SupplierSearchProcessor());
        return editor;
    }

    /****************************************************************************************************
     * Creates a supplier search field editor for active suppliers only
     ***************************************************************************************************/
    public static RSearchFieldEditor createActiveSupplierSearchFieldEditor() {
        RSearchFieldEditor editor = new RSearchFieldEditor("Supplier");
        editor.setIdentifier(SimName.SUPPLIER_ID);
        editor.setSearchProcessor(new SupplierSearchProcessor(false));
        return editor;
    }

    /****************************************************************************************************
     * Creates a finisher search field editor.
     ***************************************************************************************************/
    public static RSearchFieldEditor createFinisherSearchFieldEditor() {
        return createFinisherSearchFieldEditor("Finisher");
    }

    /****************************************************************************************************
     * Creates a finisher search field editor.
     ***************************************************************************************************/
    public static RSearchFieldEditor createFinisherSearchFieldEditor(String title) {
        RSearchFieldEditor editor = new RSearchFieldEditor(title);
        editor.setIdentifier(SimName.FINISHER_ID);
        editor.setSearchProcessor(new FinisherSearchProcessor());
        return editor;
    }

    /****************************************************************************************************
     * Creates a promotion search field editor.
     ***************************************************************************************************/
    public static RSearchFieldEditor createPromotionSearchFieldEditor() {
        return createPromotionSearchFieldEditor("Context Value");
    }

    /****************************************************************************************************
     * Creates a promotion search field editor.
     ***************************************************************************************************/
    public static RSearchFieldEditor createPromotionSearchFieldEditor(String title) {
        RSearchFieldEditor editor = new RSearchFieldEditor(title);
        editor.setIdentifier(SimName.PROMOTION_ID);
        editor.setSearchProcessor(new PromotionSearchProcessor());
        return editor;
    }

    /****************************************************************************************************
     * Creates a user search field editor.
     ***************************************************************************************************/
    public static RSearchFieldEditor createUserSearchFieldEditor() {
        return createUserSearchFieldEditor("User");
    }

    /****************************************************************************************************
     * Creates a supplier search field editor.
     ***************************************************************************************************/
    public static RSearchFieldEditor createUserSearchFieldEditor(String title) {
        RSearchFieldEditor editor = new RSearchFieldEditor(title);
        editor.setIdentifier(SimName.USER_USERNAME);
        editor.setSearchProcessor(new UserSearchProcessor());
        return editor;
    }

    /****************************************************************************************************
     * Creates an local item search field editor.
     ***************************************************************************************************/
    public static RSearchFieldEditor createItemSearchFieldEditor(String title) {
        RSearchFieldEditor editor = new RSearchFieldEditor(title);
        editor.setIdentifier(SimName.ITEM_ID);
        editor.setSearchProcessor(new StockItemSearchProcessor());
        return editor;
    }

    public static RSearchFieldEditor createItemSearchFieldEditor(String title, MessageText rangeItemConfirmMessage) {
        RSearchFieldEditor editor = new RSearchFieldEditor(title);
        editor.setIdentifier(SimName.ITEM_ID);
        editor.setSearchProcessor(new StockItemSearchProcessor(rangeItemConfirmMessage));
        return editor;
    }

    /****************************************************************************************************
     * Creates an item VO search field editor.
     ***************************************************************************************************/

    public static RSearchFieldEditor createItemVOSearchFieldEditor(boolean allowNonInventoryItems) {
        RSearchFieldEditor editor = new RSearchFieldEditor("Item");
        editor.setIdentifier(SimName.ITEM_ID);
        editor.setSearchProcessor(new ItemVOSearchProcessor(allowNonInventoryItems));
        return editor;
    }

    /****************************************************************************************************
     * Creates a Product Group Item search field editor.
     ***************************************************************************************************/
    public static RSearchFieldEditor createProductItemSearchFieldEditor() {
        RSearchFieldEditor editor = new RSearchFieldEditor("Item");
        editor.setIdentifier(SimName.ITEM_ID);
        editor.setSearchProcessor(new ProductItemSearchProcessor());
        return editor;
    }

    /****************************************************************************************************
     * Creates a Retail Item search field editor.
     ***************************************************************************************************/
    public static RSearchFieldEditor createRetailItemSearchFieldEditor() {
        RSearchFieldEditor editor = new RSearchFieldEditor("Item");
        editor.setIdentifier(SimName.ITEM_ID);
        editor.setSearchProcessor(new RetailItemSearchProcessor(false));
        return editor;
    }

    public static RSearchFieldEditor createRetailItemSearchFieldEditor(boolean isItemPriceArea) {
        RSearchFieldEditor editor = new RSearchFieldEditor("Item");
        editor.setIdentifier(SimName.ITEM_ID);
        editor.setSearchProcessor(new RetailItemSearchProcessor(isItemPriceArea));
        return editor;
    }

    /****************************************************************************************************
     * Creates a store location search combo
     ***************************************************************************************************/
    public static RSearchComboEditor createStoreSearchComboEditor(String title) {
        RSearchComboEditor editor = new RSearchComboEditor(title);
        editor.setComparator(new StoreNameComparator());
        editor.setDisplayer(new StoreDisplayer());
        return editor;
    }

    /****************************************************************************************************
     * Month Combo Box
     ***************************************************************************************************/
    public static RComboBoxEditor createMonthEditor(String title) {
        RComboBoxEditor editor = new RComboBoxEditor(title);
        editor.setDisplayer(new MonthDisplayer());
        editor.setSortEnabled(false);
        editor.setItems(SimEnumUtility.findMonths());
        editor.removeEmptySelection();
        return editor;
    }

    /****************************************************************************************************
     * Month Combo Box
     ***************************************************************************************************/
    public static RComboBoxEditor createDayOfWeekEditor(String title) {
        RComboBoxEditor editor = new RComboBoxEditor(title);
        editor.setDisplayer(new DayOfWeekDisplayer());
        editor.setSortEnabled(false);
        editor.setItems(SimEnumUtility.findDaysOfWeek());
        editor.removeEmptySelection();
        return editor;
    }

    /****************************************************************************************************
     * Month Combo Box
     ***************************************************************************************************/
    public static RComboBoxEditor createOrdinalEditor(String title, int count) {
        RComboBoxEditor editor = new RComboBoxEditor(title);
        editor.setDisplayer(new OrdinalDisplayer());
        for (int i = 0; i < count; i++) {
            editor.addItem(i + 1);
        }
        editor.setSortEnabled(false);
        editor.removeEmptySelection();
        return editor;
    }

    /****************************************************************************************************
     * Creates a store based combo box.
     ***************************************************************************************************/
    public static RComboBoxEditor createStoreComboEditor(String title) {
        RComboBoxEditor editor = new RComboBoxEditor(title);
        editor.setDisplayer(new DualAttributeDisplayer("id", "name"));
        editor.setComparator(new AttributeComparator("id"));
        return editor;
    }

    /****************************************************************************************************
     * Creates an ID/Description combo box editor. This sorts by ID by default because it displays first.
     ***************************************************************************************************/
    public static RComboBoxEditor createIdDescriptionComboEditor(String title) {
        RComboBoxEditor editor = new RComboBoxEditor(title);
        editor.setDisplayer(new DualAttributeDisplayer("id", "description"));
        editor.setComparator(new AttributeComparator("id"));
        return editor;
    }

    /****************************************************************************************************
     * Creates a date editor.
     ***************************************************************************************************/
    public static RDateFieldEditor createDateEditor(String title) {
        RDateFieldEditor editor = new RDateFieldEditor(title);
        editor.setOffset(SimDateUtil.getClientServerOffsetMillis());
        return editor;
    }
}
