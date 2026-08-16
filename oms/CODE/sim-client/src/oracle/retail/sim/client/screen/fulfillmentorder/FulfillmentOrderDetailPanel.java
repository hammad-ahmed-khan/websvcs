package oracle.retail.sim.client.screen.fulfillmentorder;

import java.awt.Component;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.displayer.GenericIdDisplayer;
import oracle.retail.sim.client.displayer.IdNameDisplayer;
import oracle.retail.sim.client.screen.notes.NotesDialog;
import oracle.retail.sim.client.swing.displayer.BooleanDisplayer;
import oracle.retail.sim.client.swing.displayer.DateTimeDisplayer;
import oracle.retail.sim.client.swing.displayer.MediumDateTimeDisplayer;
import oracle.retail.sim.client.swing.displayer.QuantityDisplayer;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.RDisplayLabelEditor;
import oracle.retail.sim.client.swing.editor.RLongTextFieldEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.panel.RDivider;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTableEditor;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.table.SimTableSortAttribute;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.LayoutUtility;
import oracle.retail.sim.client.uom.LineItemQuantityTableEditor;
import oracle.retail.sim.client.uom.UomModeDisplayer;
import oracle.retail.sim.client.uom.UomModeTableEditor;
import oracle.retail.sim.common.business.FunctionalArea;
import oracle.retail.sim.common.core.type.BasicDisplayer;
import oracle.retail.sim.common.core.type.Displayer;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrder;

public class FulfillmentOrderDetailPanel extends ScreenPanel implements REventListener {
  private static final long serialVersionUID = 1141265939491581578L;
  
  private FulfillmentOrderDetailModel model = new FulfillmentOrderDetailModel();
  
  private RDisplayLabelEditor customerOrderIdEditor = new RDisplayLabelEditor("Customer Order ID");
  
  private RDisplayLabelEditor fulfillmentOrderIdEditor = new RDisplayLabelEditor("Fulfillment Order ID");
  
  private RDisplayLabelEditor simFulfilOrderIdEditor = new RDisplayLabelEditor("SIM Customer Order ID");
  
  private RDisplayLabelEditor resvTypeEditor = new RDisplayLabelEditor("Reservation Type");
  
  private RDisplayLabelEditor orderStatusEditor = new RDisplayLabelEditor("Order Status");
  
  private RDisplayLabelEditor createDateEditor = new RDisplayLabelEditor("Order Create Date");
  
  private RDisplayLabelEditor releaseDateEditor = new RDisplayLabelEditor("Order Release Date");
  
  private RDisplayLabelEditor deliveryDateEditor = new RDisplayLabelEditor("Order Delivery Date");
  
  private RDisplayLabelEditor deliveryTypeEditor = new RDisplayLabelEditor("Delivery Type");
  
  private RDisplayLabelEditor carrierEditor = new RDisplayLabelEditor("Carrier");
  
  private RDisplayLabelEditor serviceEditor = new RDisplayLabelEditor("Service");
  
  private RDisplayLabelEditor partialDeliveryEditor = new RDisplayLabelEditor("Allow Partial Delivery");
  
  private RLongTextFieldEditor commentsEditor = new RLongTextFieldEditor("Comments");
  
  private SimTable lineItemTable = new SimTable(new CustomerOrderItemsDefinition());
  
  private SimTablePane lineItemPane = new SimTablePane(this.lineItemTable);
  
  public FulfillmentOrderDetailPanel() {
    initializeScreen();
    layoutScreen();
  }
  
  private void initializeScreen() {
    this.simFulfilOrderIdEditor.setDisplayer((BasicDisplayer)new IdNameDisplayer());
    this.customerOrderIdEditor.setDisplayer((BasicDisplayer)new IdNameDisplayer());
    this.fulfillmentOrderIdEditor.setDisplayer((BasicDisplayer)new IdNameDisplayer());
    this.resvTypeEditor.setDisplayer((BasicDisplayer)new TranslatedObjectDisplayer());
    this.orderStatusEditor.setDisplayer((BasicDisplayer)new TranslatedObjectDisplayer());
    this.deliveryTypeEditor.setDisplayer((BasicDisplayer)new TranslatedObjectDisplayer());
    this.carrierEditor.setDisplayer((BasicDisplayer)new TranslatedObjectDisplayer());
    this.serviceEditor.setDisplayer((BasicDisplayer)new TranslatedObjectDisplayer());
    this.createDateEditor.setDisplayer((BasicDisplayer)new DateTimeDisplayer());
    this.deliveryDateEditor.setDisplayer((BasicDisplayer)new DateTimeDisplayer());
    this.releaseDateEditor.setDisplayer((BasicDisplayer)new DateTimeDisplayer());
    this.partialDeliveryEditor.setDisplayer((BasicDisplayer)new BooleanDisplayer());
    this.commentsEditor.setEnabled(true, false);
    this.lineItemTable.setTableEditable(false);
    this.lineItemTable.setColumnSize("remainingQty", 0);
    this.lineItemTable.setColumnSize("orderedQty", 0);
    this.lineItemTable.setColumnSize("pickedQty", 0);
    this.lineItemTable.setColumnSize("deliveredQty", 0);
    this.lineItemTable.setColumnSize("canceledQty", 0);
  }
  
  private void layoutScreen() {
    REditorPanel rEditorPanel1 = new REditorPanel(5, 3);
    rEditorPanel1.add((Component)this.customerOrderIdEditor);
    rEditorPanel1.add((Component)this.fulfillmentOrderIdEditor);
    rEditorPanel1.add((Component)this.simFulfilOrderIdEditor);
    rEditorPanel1.add((Component)this.orderStatusEditor);
    rEditorPanel1.add((Component)this.resvTypeEditor);
    rEditorPanel1.add((Component)this.createDateEditor);
    rEditorPanel1.add((Component)this.releaseDateEditor);
    rEditorPanel1.add((Component)this.deliveryDateEditor);
    rEditorPanel1.skip();
    rEditorPanel1.skip();
    rEditorPanel1.add((Component)this.deliveryTypeEditor);
    rEditorPanel1.add((Component)this.carrierEditor);
    rEditorPanel1.add((Component)this.serviceEditor);
    rEditorPanel1.add((Component)this.partialDeliveryEditor);
    REditorPanel rEditorPanel2 = new REditorPanel(1, 1);
    rEditorPanel2.add((Component)this.commentsEditor);
    RDivider rDivider = new RDivider(0);
    RPanel rPanel = new RPanel(new GridBagLayout());
    rPanel.add((Component)rEditorPanel1, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
    rPanel.add((Component)rEditorPanel2, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
    rPanel.add((Component)rDivider, GridTool.constraints(0, 2, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
    rPanel.add((Component)this.lineItemPane, GridTool.constraints(0, 3, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));
    LayoutUtility.alignPanels(rEditorPanel1, rEditorPanel2);
    setContentPane(rPanel);
  }
  
  public SimScreenModel getScreenModel() {
    return this.model;
  }
  
  public SimTable getScreenTable() {
    return this.lineItemTable;
  }
  
  public void start() throws Exception {
    this.model.loadFulfillmentOrder();
    populateScreen();
  }
  
  public boolean isItemDetailOrigin() {
    return this.model.isItemDetailOrigin();
  }
  
  public boolean isWebOrder() {
    return this.model.isWebOrder();
  }
  
  private void populateScreen() throws Exception {
    FulfillmentOrder fulfillmentOrder = this.model.getFulfillmentOrder();
    this.simFulfilOrderIdEditor.setData(fulfillmentOrder.getId());
    this.customerOrderIdEditor.setData(fulfillmentOrder.getCustomerOrderId());
    this.fulfillmentOrderIdEditor.setData(fulfillmentOrder.getExternalId());
    this.orderStatusEditor.setData(fulfillmentOrder.getStatus().toString());
    this.resvTypeEditor.setData(fulfillmentOrder.getOrderType().toString());
    this.commentsEditor.setText(fulfillmentOrder.getComments());
    this.createDateEditor.setData(fulfillmentOrder.getCreateDate());
    this.releaseDateEditor.setData(fulfillmentOrder.getReleaseDate());
    this.deliveryDateEditor.setData(fulfillmentOrder.getDeliveryDate());
    this.deliveryTypeEditor.setData(fulfillmentOrder.getDeliveryType());
    this.carrierEditor.setData(fulfillmentOrder.getDeliveryCarrier());
    this.serviceEditor.setData(fulfillmentOrder.getDeliveryService());
    this.partialDeliveryEditor.setData(Boolean.valueOf(fulfillmentOrder.isAllowPartialDelivery()));
    this.lineItemTable.setRows(this.model.getCustomerOrderItems());
  }
  
  public void performActionEvent(RActionEvent paramRActionEvent) {}
  
  public void handleSave() {
    try {
      this.model.markFulfillmentOrderAsInProgress();
    } catch (Exception exception) {
      displayException(exception);
    } 
  }
  
  public void handlePrint() throws Exception {
    this.model.printCustomerOrder();
  }
  
  public void handleNotes() {
    try {
      NotesDialog notesDialog = new NotesDialog();
      notesDialog.setTitle("Customer Order Notes");
      notesDialog.loadNotes(FunctionalArea.CUSTOMER_ORDER, this.model.getFulfillmentOrder().getId(), this.model.isNotesEditable());
      notesDialog.setVisible(true);
    } catch (Exception exception) {
      displayException(exception);
    } 
  }
  
  private class CustomerOrderItemsDefinition extends SimTableDefinition {
    private CustomerOrderItemsDefinition() {}
    
    public Class<?> getDataClass() {
      return FulfillmentOrderLineItemWrapper.class;
    }
    
    public List<SimTableSortAttribute> getSortAttributes() {
      return Collections.singletonList(new SimTableSortAttribute("itemId", false));
    }
    
    public List<SimTableAttribute> getAttributes() {
      ArrayList<SimTableAttribute> arrayList = new ArrayList<SimTableAttribute>(11);
      arrayList.add(new SimTableAttribute("Item", "itemId", (Displayer)new GenericIdDisplayer()));
      arrayList.add(new SimTableAttribute("Description", "itemDescription", (Displayer)new TranslatedObjectDisplayer()));
      arrayList.add(new SimTableAttribute("UOM", "unitOfMeasureMode", (Displayer)new UomModeDisplayer(), (SimTableEditor)new UomModeTableEditor(true)));
      arrayList.add(new SimTableAttribute("Pack Size", "caseSize", (Displayer)new QuantityDisplayer(), (SimTableEditor)new LineItemQuantityTableEditor()));
      arrayList.add(new SimTableAttribute("Remaining Qty", "remainingQty", (Displayer)new QuantityDisplayer()));
      arrayList.add(new SimTableAttribute("Order Qty", "orderedQty", (Displayer)new QuantityDisplayer()));
      arrayList.add(new SimTableAttribute("Picked Qty", "pickedQty", (Displayer)new QuantityDisplayer()));
      arrayList.add(new SimTableAttribute("Delivered Qty", "deliveredQty", (Displayer)new QuantityDisplayer()));
      arrayList.add(new SimTableAttribute("Canceled Qty", "canceledQty", (Displayer)new QuantityDisplayer()));
      arrayList.add(new SimTableAttribute("Last Update Date", "updatedDate", (Displayer)new MediumDateTimeDisplayer()));
      arrayList.add(new SimTableAttribute("Comments", "comments"));
      arrayList.add(new SimTableAttribute("Substitute", "substituteItemId"));
      return arrayList;
    }
  }
}
