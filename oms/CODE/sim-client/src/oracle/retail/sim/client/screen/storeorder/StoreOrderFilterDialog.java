package oracle.retail.sim.client.screen.storeorder;

import java.awt.Component;
import java.awt.GridBagLayout;
import java.util.Comparator;
import javax.swing.JFrame;
import javax.swing.JPanel;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.displayer.IdNameDisplayer;
import oracle.retail.sim.client.editor.SimEditorFactory;
import oracle.retail.sim.client.screen.supplier.SupplierSearchListener;
import oracle.retail.sim.client.swing.dialog.RDialog;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.RComboBoxEditor;
import oracle.retail.sim.client.swing.editor.RDateFieldEditor;
import oracle.retail.sim.client.swing.editor.RDateFieldRangeUtility;
import oracle.retail.sim.client.swing.editor.RNumericIdEditor;
import oracle.retail.sim.client.swing.editor.RRadioButtonEditor;
import oracle.retail.sim.client.swing.editor.RSearchFieldEditor;
import oracle.retail.sim.client.swing.editor.SearchListener;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.panel.RDivider;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.LayoutUtility;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.client.swing.widget.RComboBoxEmptyType;
import oracle.retail.sim.client.swing.widget.RLabel;
import oracle.retail.sim.common.core.type.BasicDisplayer;
import oracle.retail.sim.common.source.Source;
import oracle.retail.sim.common.source.SourceType;
import oracle.retail.sim.common.source.Supplier;
import oracle.retail.sim.common.source.Warehouse;
import oracle.retail.sim.common.source.WarehouseComparator;
import oracle.retail.sim.common.storeorder.StoreOrderQueryFilter;
import oracle.retail.sim.common.storeorder.StoreOrderStatus;

public class StoreOrderFilterDialog extends RDialog implements REventListener {
  private static final long serialVersionUID = 7486735345533727593L;
  
  private StoreOrderFilterDialogModel model = new StoreOrderFilterDialogModel();
  
  private RDateFieldEditor startDateEditor = new RDateFieldEditor("From Date");
  
  private RDateFieldEditor finalDateEditor = new RDateFieldEditor("To Date");
  
  private RNumericIdEditor orderEditor = new RNumericIdEditor("Order Id", "Order");
  
  private RComboBoxEditor statusEditor = new RComboBoxEditor("Status");
  
  private RRadioButtonEditor sourceEditor = new RRadioButtonEditor("Source");
  
  private RComboBoxEditor warehouseEditor = new RComboBoxEditor("Warehouse");
  
  private RSearchFieldEditor supplierEditor = SimEditorFactory.createActiveSupplierSearchFieldEditor();
  
  private static final String WAREHOUSE = "Warehouse";
  
  private static final String SUPPLIER = "Supplier";
  
  private static String[] radioButtons = new String[] { "Warehouse", "Supplier" };
  
  private static final String SOURCE_MODIFIED = "Source.modified";
  
  private RButton applyButton = new RButton("Apply");
  
  private RButton resetButton = new RButton("Reset");
  
  private RButton cancelButton = new RButton("Cancel");
  
  public StoreOrderFilterDialog() {
    super((JFrame)Application.getFrame());
    setStatusBarVisible(false);
    setTitle("Store Order List Filter");
    setSize(500, 300);
    initContent();
    layoutContent();
    centerOnOwner();
  }
  
  private void initContent() {
    this.orderEditor.setIdentifier("StoreOrder.id");
    this.orderEditor.setSizeType(12);
    this.statusEditor.setSizeType(12);
    this.statusEditor.setDisplayer((BasicDisplayer)new TranslatedObjectDisplayer());
    this.statusEditor.setEmptyType(RComboBoxEmptyType.ALL);
    this.sourceEditor.setRadioTextPosition(4);
    this.sourceEditor.setRadioButtons(radioButtons, 2, 1);
    this.sourceEditor.getRadioButton("Supplier").setMnemonic(65);
    this.sourceEditor.getRadioButton("Warehouse").setMnemonic(66);
    this.sourceEditor.registerAction(this, "Source.modified");
    this.warehouseEditor.setMinimumWidth(250);
    this.warehouseEditor.setDisplayer((BasicDisplayer)new IdNameDisplayer());
    this.warehouseEditor.setComparator((Comparator)WarehouseComparator.getInstance());
    this.warehouseEditor.setEnabled(false);
    this.supplierEditor.setSearchListener((SearchListener)buildSupplierSearchListener());
    this.supplierEditor.setEnabled(false);
    this.startDateEditor.setSizeType(13);
    this.finalDateEditor.setSizeType(13);
    RDateFieldRangeUtility.setDateRangeEditors(this.startDateEditor, this.finalDateEditor);
    this.applyButton.registerAction(this, "Apply");
    this.resetButton.registerAction(this, "Reset");
    this.cancelButton.registerAction(this, "Cancel");
  }
  
  private void layoutContent() {
    addButton(this.applyButton);
    addButton(this.resetButton);
    addButton(this.cancelButton);
    REditorPanel rEditorPanel1 = new REditorPanel(2);
    rEditorPanel1.setTitleBorder("Date Filters");
    rEditorPanel1.add((Component)this.startDateEditor);
    rEditorPanel1.add((Component)this.finalDateEditor);
    REditorPanel rEditorPanel2 = new REditorPanel(2, 2);
    rEditorPanel2.add((Component)this.orderEditor);
    rEditorPanel2.add((Component)this.statusEditor);
    RDivider rDivider = new RDivider(0);
    REditorPanel rEditorPanel3 = new REditorPanel(2);
    rEditorPanel3.add((Component)this.warehouseEditor);
    rEditorPanel3.add((Component)this.supplierEditor);
    RPanel rPanel1 = new RPanel(new GridBagLayout());
    rPanel1.setTitleBorder("Additional Filters");
    rPanel1.add((Component)rEditorPanel2, GridTool.constraints(0, 0, 3, 1, 1, 0, 0, 3, 0, 0, 0, 0));
    rPanel1.add((Component)rDivider, GridTool.constraints(0, 1, 3, 1, 1, 0, 0, 3, 0, 0, 0, 0));
    rPanel1.add((Component)this.sourceEditor, GridTool.constraints(0, 2, 1, 1, 0, 0, 0, 3, 0, 0, 0, 0));
    rPanel1.add((Component)rEditorPanel3, GridTool.constraints(1, 2, 1, 1, 0, 0, 0, 3, 0, 0, 0, 0));
    rPanel1.add((Component)new RLabel(), GridTool.constraints(2, 2, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
    RPanel rPanel2 = new RPanel(new GridBagLayout());
    rPanel2.add((Component)rEditorPanel1, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
    rPanel2.add((Component)rPanel1, GridTool.constraints(0, 1, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));
    LayoutUtility.alignEditorsInGridBag((JPanel)rPanel1);
    setContentPane(rPanel2);
  }
  
  public void setFilter(StoreOrderQueryFilter paramStoreOrderQueryFilter) throws Exception {
    this.model.setFilter(paramStoreOrderQueryFilter);
    this.warehouseEditor.setItems(this.model.findAllWarehouses());
    this.statusEditor.setItems(this.model.findAllStatus());
    this.startDateEditor.setDate(paramStoreOrderQueryFilter.getNotBeforeDate());
    this.finalDateEditor.setDate(paramStoreOrderQueryFilter.getNotAfterDate());
    this.orderEditor.setLong(paramStoreOrderQueryFilter.getStoreOrderNumber());
    this.statusEditor.setSelectedItem(paramStoreOrderQueryFilter.getStatus());
    this.sourceEditor.clearSelection();
    Source source = paramStoreOrderQueryFilter.getFromLocation();
    if (source == null) {
      this.supplierEditor.clear();
      this.warehouseEditor.setEmptySelection();
      this.supplierEditor.setEnabled(false);
      this.warehouseEditor.setEnabled(false);
    } else if (paramStoreOrderQueryFilter.getSourceType() == SourceType.SUPPLIER) {
      this.sourceEditor.setSelected("Supplier", true);
      this.supplierEditor.setText(source.getId());
      validateEnabledState();
    } else if (paramStoreOrderQueryFilter.getSourceType() == SourceType.WAREHOUSE) {
      this.sourceEditor.setSelected("Warehouse", true);
      this.warehouseEditor.setSelectedItem(this.model.getWarehouse(source));
      validateEnabledState();
    } 
    setDefaultButton(this.applyButton);
  }
  
  private SupplierSearchListener buildSupplierSearchListener() {
    return new SupplierSearchListener() {
        public void assignSupplier(Supplier param1Supplier) {
          if (param1Supplier != null)
            StoreOrderFilterDialog.this.supplierEditor.setData(param1Supplier); 
        }
      };
  }
  
  public void performActionEvent(RActionEvent paramRActionEvent) {
    String str = paramRActionEvent.getEventCommand();
    try {
      if (str.equals("Source.modified")) {
        validateEnabledState();
      } else if (str.equals("Reset")) {
        doReset();
      } else if (str.equals("Apply")) {
        doApply();
      } else if (str.equals("Cancel")) {
        doCancel();
      } 
    } catch (Throwable throwable) {
      displayException(throwable);
    } 
  }
  
  private void validateEnabledState() {
    if (this.sourceEditor.isSelected("Warehouse")) {
      this.warehouseEditor.setEnabled(true);
      this.supplierEditor.setEnabled(false);
      this.supplierEditor.clear();
      this.finalDateEditor.setEnabled(false);
      this.finalDateEditor.clear();
    } else {
      this.warehouseEditor.setEnabled(false);
      this.warehouseEditor.setEmptySelection();
      this.supplierEditor.setEnabled(true);
      this.finalDateEditor.setEnabled(true);
    } 
  }
  
  private void doReset() throws Exception {
    setFilter(this.model.resetFilter());
  }
  
  private void doApply() throws Exception {
    StoreOrderQueryFilter storeOrderQueryFilter = this.model.resetFilter();
    if (!this.orderEditor.isEmpty())
      storeOrderQueryFilter.setStoreOrderNumber(this.orderEditor.getLong()); 
    if (this.sourceEditor.isSelected("Warehouse")) {
      storeOrderQueryFilter.setSourceType(SourceType.WAREHOUSE);
      Warehouse warehouse = (Warehouse)this.warehouseEditor.getSelectedItem();
      if (warehouse != null)
        storeOrderQueryFilter.setFromLocation((Source)warehouse); 
    } else if (this.sourceEditor.isSelected("Supplier")) {
      storeOrderQueryFilter.setSourceType(SourceType.SUPPLIER);
      Supplier supplier = (Supplier)this.supplierEditor.getData();
      if (supplier != null)
        storeOrderQueryFilter.setFromLocation((Source)supplier); 
    } 
    storeOrderQueryFilter.setStatus((StoreOrderStatus)this.statusEditor.getSelectedItem());
    storeOrderQueryFilter.setNotBeforeDate(this.startDateEditor.getDateAtStartOfDay());
    storeOrderQueryFilter.setNotAfterDate(this.finalDateEditor.getDateAtEndOfDay());
    RepositoryManager.addStateObject("STORE_ORDER_FILTER", storeOrderQueryFilter);
    notifyREventListeners(new RActionEvent(this, "STORE_ORDER_FILTER_MODIFIED", storeOrderQueryFilter));
    closeWindow();
  }
  
  private void doCancel() {
    closeWindow();
  }
}
