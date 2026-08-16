/**
 * 
 */
package extra.retail.sim.client.screen.shipTrailer;

import java.awt.GridBagLayout;
import java.util.List;
import java.util.Map;

import extra.retail.sim.client.common.model.BaseLV;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.RComboBoxEditor;
import oracle.retail.sim.client.swing.editor.RDisplayLabelEditor;
import oracle.retail.sim.client.swing.editor.RNumericIdEditor;
import oracle.retail.sim.client.swing.editor.RTextFieldEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.panel.RDivider;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.core.type.BasicDisplayer;

/**
 * @author aibrahim
 *
 */
public class DMShipTrailerCapturePanel extends ScreenPanel implements REventListener {

	private static final long serialVersionUID = 7112694115250404208L;

    private RDisplayLabelEditor shipmentBOLId = new RDisplayLabelEditor("Shipment BOL ID");

    private RDisplayLabelEditor transferReturnId = new RDisplayLabelEditor("Return ID");

	private RComboBoxEditor transporterDD = new RComboBoxEditor("Transporter", true);

	private RComboBoxEditor truckLoadDD = new RComboBoxEditor("Truck Load", true);

	private RTextFieldEditor truckNoField = new RTextFieldEditor("Truck Number", true);

	private RComboBoxEditor trailerTypeDD = new RComboBoxEditor("Trailer Type", true);

	private RTextFieldEditor wayBillNo = new RTextFieldEditor("Way Bill Number", true);

	private RNumericIdEditor noOfPallet = new RNumericIdEditor("No Of Pallets(CBM Value)", "No Of Pallets(CBM Value)", false);

	private RComboBoxEditor pickTypeDD = new RComboBoxEditor("Pick Type", true);

	private RNumericIdEditor noOfQty = new RNumericIdEditor("No. Of Quantity", "No. Of Quantity", true);

	private DMShipTrailerCaptureModel model = new DMShipTrailerCaptureModel();
	
	public DMShipTrailerCapturePanel() {
		initializePanel();
		layoutScreen();
	}

	@Override
	public void performActionEvent(RActionEvent event) {
	}

	@Override
	public void start() throws Throwable {
		model.loadShipmentBOLID();
		populateScreen();
	}

	private void populateScreen() {
		Map<String, List<BaseLV>> baseLVs = model.getBaseLVs();
		transporterDD.setItems(baseLVs.get("TRANSPORTER"));
		truckLoadDD.setItems(baseLVs.get("TRUCK_LOAD"));
		trailerTypeDD.setItems(baseLVs.get("TRAILER_TYPE"));
		pickTypeDD.setItems(baseLVs.get("TRAILER_PICK_TYPE"));
		DMShipTrailer shipTrailer = model.getShipTrailerDetail();
		if (shipTrailer != null) {
			transferReturnId.setData(shipTrailer.getTransferReturnId());
			shipmentBOLId.setData(shipTrailer.getShipmentBOLID());
			if (shipTrailer.getTransporter() != null) {
				transporterDD.setSelectedItem(BaseLV.findItem(baseLVs.get("TRANSPORTER"), shipTrailer.getTransporter()));
				truckLoadDD.setSelectedItem(BaseLV.findItem(baseLVs.get("TRUCK_LOAD"), shipTrailer.getTruckLoad()));
				trailerTypeDD.setSelectedItem(BaseLV.findItem(baseLVs.get("TRAILER_TYPE"), shipTrailer.getTrailerType()));
				truckNoField.setText(shipTrailer.getTruckNo());
				wayBillNo.setText(shipTrailer.getWayBillNo());
				pickTypeDD.setSelectedItem(BaseLV.findItem(baseLVs.get("TRAILER_PICK_TYPE"), shipTrailer.getPickType()));
				noOfQty.setInteger(shipTrailer.getNoOfQty());
			}
		}
	}

	@Override
	public SimScreenModel getScreenModel() {
		return null;
	}

	@Override
	public SimTable getScreenTable() {
		return null;
	}

	private void layoutScreen() {

		REditorPanel headerPanel = new REditorPanel(1, 2);
		headerPanel.add(shipmentBOLId);
		headerPanel.add(transferReturnId);

		RDivider divider = new RDivider(RDivider.HORIZONTAL);

		REditorPanel detailPanel = new REditorPanel(4, 3);
		detailPanel.add(transporterDD);
		detailPanel.add(truckLoadDD, 0, 1);
		detailPanel.add(truckNoField, 0, 2);
		detailPanel.add(trailerTypeDD, 1, 0);
		detailPanel.add(wayBillNo, 1, 1);
		detailPanel.add(noOfPallet, 1, 2);
		detailPanel.add(pickTypeDD, 2, 0);
		detailPanel.add(noOfQty, 2, 1);
		
		RPanel mainPanel = new RPanel(new GridBagLayout());
		mainPanel.add(headerPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 3, 3, 0, 0, 5, 0));
		mainPanel.add(divider, GridTool.constraints(0, 1, 1, 1, 1, 0, 3, 3, 0, 0, 5, 0));
		mainPanel.add(detailPanel, GridTool.constraints(0, 2, 1, 1, 1, 1, 3, 3, 0, 0, 5, 0));

		setContentPane(mainPanel);
		
		truckLoadDD.registerAction(this, "CBCHANGE");
	
		initializePanel();
	}

	private void initializePanel() {
		noOfPallet.setVisible(false);
		
		truckNoField.setLength(20);
		wayBillNo.setLength(20);
		noOfPallet.setLength(20);
		noOfQty.setLength(2);
		
		truckNoField.setEnabled(true);
		wayBillNo.setEnabled(true);
		noOfPallet.setEnabled(true);
		noOfQty.setEnabled(true);

		BasicDisplayer basicDisplayer = new TranslatedObjectDisplayer();
		transporterDD.setDisplayer(basicDisplayer);
		truckLoadDD.setDisplayer(basicDisplayer);
		trailerTypeDD.setDisplayer(basicDisplayer);
	}

	public boolean isDraft() {
		return model.isDraft();
	}

	public boolean draftShipTrailer() throws Exception {
		DMShipTrailer shipTrailer = populateShipTrailer();
		model.draftShipTrailer(shipTrailer);
		return true;
	}

	private DMShipTrailer populateShipTrailer() throws Exception {
		validateShipTrailerForm();
		DMShipTrailer shipTrailer = model.getShipTrailerDetail();
		if (shipTrailer == null) {
			shipTrailer = new DMShipTrailer();
			shipTrailer.setNew(true);
		}
		shipTrailer.setTransporter(((BaseLV) transporterDD.getSelectedItem()).getKey());
		shipTrailer.setTruckLoad(((BaseLV) truckLoadDD.getSelectedItem()).getKey());
		shipTrailer.setTruckNo(truckNoField.getText());
		shipTrailer.setTrailerType(((BaseLV) trailerTypeDD.getSelectedItem()).getKey());
		shipTrailer.setWayBillNo(wayBillNo.getText());
		shipTrailer.setNoOfQty(noOfQty.getInteger());
		shipTrailer.setNoOfPallets(noOfPallet.getInteger());
		shipTrailer.setPickType(((BaseLV) pickTypeDD.getSelectedItem()).getKey());
		return shipTrailer;
	}

	private void validateShipTrailerForm() throws Exception {
		if (transporterDD.isEmptySelection()) {
			throw new BusinessException(CommonMessageText.SHIP_TRAILER_FIELD_REQUIRED, new String[] {"Transporter"});
		}
		if (truckLoadDD.isEmptySelection()) {
			throw new BusinessException(CommonMessageText.SHIP_TRAILER_FIELD_REQUIRED, new String[] {"Truck Load"});
		}
		if (truckNoField.isEmpty()) {
			throw new BusinessException(CommonMessageText.SHIP_TRAILER_FIELD_REQUIRED, new String[] {"Truck Number"});
		}
		if (trailerTypeDD.isEmptySelection()) {
			throw new BusinessException(CommonMessageText.SHIP_TRAILER_FIELD_REQUIRED, new String[] {"Trailer Type"});
		}
		if (wayBillNo.isEmpty()) {
			throw new BusinessException(CommonMessageText.SHIP_TRAILER_FIELD_REQUIRED, new String[] {"Way Bill No#"});
		}
		if (pickTypeDD.isEmptySelection()) {
			throw new BusinessException(CommonMessageText.SHIP_TRAILER_FIELD_REQUIRED, new String[] {"Pick Type"});
		}
		if (noOfQty.isEmpty()) {
			throw new BusinessException(CommonMessageText.SHIP_TRAILER_FIELD_REQUIRED, new String[] {"No of Boxes"});
		} else if (noOfQty.getIntegerValue() > 30 || noOfQty.getIntegerValue() < 1) {
			throw new BusinessException(CommonMessageText.NUMBER_NOT_NOT_IN_RANGE, new String[] {"1", "30", "No of Boxes"});
		}
	}

	public boolean updateShipTrailer() throws Exception {
		DMShipTrailer shipTrailer = populateShipTrailer();
		model.updateShipTrailer(shipTrailer);
		return true;
	}

	public boolean isNew() {
		return model.isNew();
	}
}
