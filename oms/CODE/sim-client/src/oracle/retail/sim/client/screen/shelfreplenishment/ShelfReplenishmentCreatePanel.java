package oracle.retail.sim.client.screen.shelfreplenishment;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
import oracle.retail.sim.client.swing.displayer.AttributeDisplayer;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RComboBoxEditor;
import oracle.retail.sim.client.swing.editor.RDisplayLabelEditor;
import oracle.retail.sim.client.swing.editor.RQuantityFieldEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.LayoutUtility;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.uom.UomModeDisplayer;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.productgroup.ProductGroup;
import oracle.retail.sim.common.productgroup.ProductGroupVO;
import oracle.retail.sim.common.shelfreplenishment.ShelfReplenishment;
import oracle.retail.sim.common.shelfreplenishment.ShelfReplenishmentMessageText;
import oracle.retail.sim.common.shelfreplenishment.ShelfReplenishmentType;

/********************************************************************************************************
 * Shelf Replenishment List Create Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ShelfReplenishmentCreatePanel extends ScreenPanel implements REventListener {
    private static final long serialVersionUID = -2558662749766759533L;

    private ShelfReplenishmentCreateModel model = new ShelfReplenishmentCreateModel();

    private RComboBoxEditor shelfReplenishmentTypeEditor = new RComboBoxEditor("Shelf Replenishment Type");
    private RComboBoxEditor productGroupEditor = new RComboBoxEditor("Product Group");
    private RQuantityFieldEditor shelfReplenishmentAmountEditor = new RQuantityFieldEditor("Amount to Replenish");
    private RDisplayLabelEditor uomEditor = new RDisplayLabelEditor(StringConstants.EMPTY);

    private static final String TYPE_MODIFIED = "Type.modified";
    private static final String GROUP_MODIFIED = "Group.modified";
    private static final String AMOUNT_MODIFIED = "Amount.modified";

    public ShelfReplenishmentCreatePanel() {
        initializeScreen();
        layoutScreen();
    }

    private void initializeScreen() {
        shelfReplenishmentAmountEditor.setIdentifier(SimName.SHELF_REPLENISHMENT_AMOUNT);

        shelfReplenishmentTypeEditor.setSizeType(EditorConstants.LARGE);
        productGroupEditor.setSizeType(EditorConstants.LARGE);
        shelfReplenishmentAmountEditor.setSizeType(EditorConstants.MEDIUM);

        shelfReplenishmentTypeEditor.setDisplayer(new TranslatedObjectDisplayer());
        productGroupEditor.setDisplayer(new AttributeDisplayer("description"));

        shelfReplenishmentTypeEditor.registerAction(this, TYPE_MODIFIED);
        productGroupEditor.registerAction(this, GROUP_MODIFIED);
        shelfReplenishmentAmountEditor.registerAction(this, AMOUNT_MODIFIED);
        uomEditor.setDisplayer(new UomModeDisplayer());
    }

    private void layoutScreen() {
        RPanel shelfReplenishmentPanel = new RPanel(new GridBagLayout());
        shelfReplenishmentPanel.setTitleBorder("Product Group Detail");
        shelfReplenishmentPanel.add(shelfReplenishmentTypeEditor, GridTool.constraints(0, 0, 2, 1, 0, 0, 0, 0, 0, 0, 5, 0));
        shelfReplenishmentPanel.add(productGroupEditor, GridTool.constraints(0, 1, 2, 1, 0, 0, 0, 0, 0, 0, 5, 0));
        shelfReplenishmentPanel.add(shelfReplenishmentAmountEditor, GridTool.constraints(0, 2, 1, 1, 0, 0, 0, 0, 0, 0, 5, 0));
        shelfReplenishmentPanel.add(uomEditor, GridTool.constraints(1, 2, 1, 1, 0, 0, 0, 0, 0, 0, 5, 0));

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(shelfReplenishmentPanel, GridTool.constraints(0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0));
        mainPanel.add(new JPanel(), GridTool.constraints(1, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
        mainPanel.add(new JPanel(), GridTool.constraints(0, 1, 2, 1, 1, 1, 0, 3, 0, 0, 0, 0));

        LayoutUtility.alignEditorsInGridBag(shelfReplenishmentPanel);

        setContentPane(mainPanel);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return null;
    }

    /****************************************************************************************************
     * Start
     ***************************************************************************************************/

    public void start() throws Exception {
        shelfReplenishmentTypeEditor.setItems(model.findShelfReplenishmentTypes());
        productGroupEditor.setItems(model.findProductGroups());
        shelfReplenishmentTypeEditor.setEmptySelection();
        shelfReplenishmentAmountEditor.clear();
    }

    /****************************************************************************************************
     * Screen Actions
     ***************************************************************************************************/

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        if (command.equals(TYPE_MODIFIED)) {
            doTypeModified();
        } else if (command.equals(GROUP_MODIFIED)) {
            doGroupModified();
        } else if (command.equals(AMOUNT_MODIFIED)) {
            doAmountModified();
        }
    }

    private void doTypeModified() {
        model.clearShelfReplenishment();

        ShelfReplenishmentType type = (ShelfReplenishmentType) shelfReplenishmentTypeEditor.getSelectedItem();
        if (type != null) {
            productGroupEditor.setEmptySelection();
            shelfReplenishmentAmountEditor.clear();
            model.createShelfReplenishment(type);
        }
        validateEnabledState();
    }

    private void doGroupModified() {
        ShelfReplenishment shelfReplenishment = model.getShelfReplenishment();

        if (shelfReplenishment != null) {
            ProductGroupVO groupVO = (ProductGroupVO) productGroupEditor.getSelectedItem();
            try {
                if (groupVO != null) {
                    ProductGroup productGroup = model.readShelfReplenishmentProductGroup(groupVO);
                    shelfReplenishment.setProductGroupId(productGroup.getId());
                    shelfReplenishment.doSetProductGroupDescription(productGroup.getDescription());
                    shelfReplenishment.doSetUnitOfMeasureMode(productGroup.getUnitOfMeasureMode());
                } else {
                    shelfReplenishment.doSetProductGroupId(null);
                    shelfReplenishment.doSetProductGroupDescription(null);
                    shelfReplenishment.doSetUnitOfMeasureMode(null);
                }
            } catch (Exception exception) {
                displayException(exception);
            }
            validateEnabledState();
        }
    }

    private void doAmountModified() {
        ShelfReplenishment shelfReplenishment = model.getShelfReplenishment();
        if (shelfReplenishment != null) {
            try {
                shelfReplenishment.setQuantityToReplenish(shelfReplenishmentAmountEditor.getQuantity());
            } catch (BusinessException be) {
                displayAmountException(Translator.getMessage(be.getPrimaryMessageText().getText(), be.getPrimaryMessageValues()), be);
            } catch (UIException uie) {
                displayAmountException(Translator.getMessage(uie.getPrimaryMessageText().getText(), uie.getPrimaryMessageValues()), uie);
            }
            validateEnabledState();
        }
    }

    private void displayAmountException(String message, Exception exception) {
        shelfReplenishmentAmountEditor.clear();
        shelfReplenishmentAmountEditor.setErrorState(true, message);
        displayException(exception);
        assignFocusInScreen(shelfReplenishmentAmountEditor);
    }

    private void validateEnabledState() {
        productGroupEditor.setEnabled(model.isProductGroupEditable());
        shelfReplenishmentAmountEditor.setEnabled(model.isAmountEditable());
        uomEditor.setData(model.getUOMMode());
    }

    /****************************************************************************************************
     * Handle Save
     ***************************************************************************************************/

    public boolean handleSave() throws Exception {
        ShelfReplenishment shelfReplenishment = model.getShelfReplenishment();
        if (shelfReplenishment == null) {
            displayWarning(ShelfReplenishmentMessageText.MISSING_TYPE);
            return false;
        }
        if (shelfReplenishment.getQuantityToReplenish() == null) {
            if (shelfReplenishment.getType() == ShelfReplenishmentType.END_OF_DAY) {
                // End of day pick will replenish to the percent set in the system parameters so the amount is not relevant but must be legal
                shelfReplenishment.setQuantityToReplenish(Quantity.ONE);
            } else {
                shelfReplenishment.setQuantityToReplenish(shelfReplenishmentAmountEditor.getQuantity());
            }
        }
        shelfReplenishment.setEmployeeId(model.getUserName());

        if (shelfReplenishment.isCoherent()) {
            if (RConfirmUtility.confirm("Confirmation", ShelfReplenishmentMessageText.CREATE_CONFIRM)) {
                ShelfReplenishment newShelfReplenishment = model.createShelfReplenishment();
                if (newShelfReplenishment == null || newShelfReplenishment.getLineItems().isEmpty()) {
                    displayException(new BusinessException(ShelfReplenishmentMessageText.REPLENISH_NOT_NEEDED));
                    navigateLater(SimNavigation.BACK);
                    return false;
                }
            }
            RepositoryManager.addStateObject(SimClientStateKey.SHELF_REPLENISHMENT_DETAIL_MODIFIED, Boolean.TRUE);
            return true;
        }
        return false;
    }
}
