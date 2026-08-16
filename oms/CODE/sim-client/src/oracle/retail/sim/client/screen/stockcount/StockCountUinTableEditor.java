package oracle.retail.sim.client.screen.stockcount;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JComponent;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.core.SimRepository;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
import oracle.retail.sim.client.swing.dialog.RErrorDialog;
import oracle.retail.sim.client.swing.displayer.AttributeDisplayer;
import oracle.retail.sim.client.swing.logging.UILog;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableEditor;
import oracle.retail.sim.client.swing.table.SimTableEditorEventAdaptor;
import oracle.retail.sim.client.swing.table.SimTableEditorListener;
import oracle.retail.sim.client.swing.util.UIMessageText;
import oracle.retail.sim.client.swing.widget.RTextField;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.MessageText;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.type.AbstractDisplayer;
import oracle.retail.sim.common.item.ItemMessageText;
import oracle.retail.sim.common.stockcount.StockCountMessageText;
import oracle.retail.sim.common.stockcount.StockCountPhase;
import oracle.retail.sim.common.stockcount.StockCountSerialNumber;
import oracle.retail.sim.common.uin.SerialNumberValue;
import oracle.retail.sim.common.uin.UINMessageText;
import oracle.retail.sim.common.uin.UINStatus;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Table Editor for editing a UINValue business object. The model for the table row that uses this editor
 * must be a UINWrapper.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StockCountUinTableEditor extends RTextField implements SimTableEditor, FocusListener {
    private static final long serialVersionUID = -2875102018706589406L;

    private AbstractDisplayer displayer;
    private SimTableEditorEventAdaptor eventAdaptor;
    private StockCountUinInterface serialNumberModel;
    private StockCountSerialNumber serialNumber;
    private String lastCheckedSerialNumber = "";
    private boolean isAuthorizationMode;
    private boolean isErrorState;
    private int row = -1;
    private int column = -1;

    public StockCountUinTableEditor() {
        this(false);
    }

    public StockCountUinTableEditor(boolean isAuthorizationMode) {
        eventAdaptor = new SimTableEditorEventAdaptor(this);
        displayer = new AttributeDisplayer("serialNumber");
        this.isAuthorizationMode = isAuthorizationMode;
        setIdentifier(SimName.SERIAL_NUMBER);
        setMargin(null);
        setBorder(null);
        addFocusListener(this);
    }

    public Class getValueClass() {
        return StockCountSerialNumber.class;
    }

    public void setValueClass(Class valueClass) {
        // Ignore
    }

    public void setModel(Object model) {
        serialNumberModel = (StockCountUinInterface) model;
    }

    public JComponent getComponent() {
        return this;
    }

    /****************************************************************************************************
     * Assign coordinates to the editor.
     ***************************************************************************************************/
    public void setCoordinates(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /****************************************************************************************************
     * Reactivate editing within the table cell.
     ***************************************************************************************************/
    private void reactivateEditing(Object object) {
        if (object instanceof SimTable) {
            SimTable table = (SimTable) object;
            try {
                if (table.getSelectedRow() != row) {
                    table.setRowSelectionInterval(row, row);
                }
                table.editCellAt(row, column);
            } catch (Throwable ex) {
                UILog.debug(getClass(), ex);
            }
        }
    }

    /****************************************************************************************************
     * Get and Set Values
     ***************************************************************************************************/

    public Object getValue() {
        if (isErrorState()) {
            return null;
        }
        return serialNumber;
    }

    public void setData(Object value) {
        if (value instanceof StockCountSerialNumber) {
            setValue(value);
            return;
        }
        throw new IllegalArgumentException("StockCountSerialNumTableEditor only edits StockCountSerialNumber!");
    }

    public void setValue(Object value) {
        if (value instanceof String) {
            setText((String) value);
            checkValue();
        } else if (value instanceof StockCountSerialNumber) {
            serialNumber = (StockCountSerialNumber) value;
            setText(displayer.getDisplayText(serialNumber));
        } else if (value == null) {
            serialNumber = null;
            clear();
        }
        eventAdaptor.fireTypeEditorEvent();
    }

    /****************************************************************************************************
     * Check The Value
     ***************************************************************************************************/

    public boolean checkValue() {
        String enteredSerialNumber = getText();

        setErrorState(false);

        if (StringUtility.isNullOrEmpty(enteredSerialNumber)) {
            if (serialNumber == null) {
                return true;
            }
            displayErrorWithReset(ItemMessageText.ITEM_BLANK_ERROR, null);
            return false;
        }

        try {
            if (serialNumber != null && enteredSerialNumber.equals(serialNumber.getSerialNumber()) && enteredSerialNumber.equals(lastCheckedSerialNumber)) {
                return false;
            }

            serialNumber = null;

            if (isAuthorizationMode) {
                serialNumber = findAuthorizeSerialNumber(enteredSerialNumber);
            } else {
                serialNumber = findNormalCountSerialNumber(enteredSerialNumber);

                if (serialNumber == null) {
                    displaySpecificSerialNumberError(enteredSerialNumber);
                }
            }

            if (serialNumber == null) {
                clearTableEditor();
                return false;
            }

            if (isSerialNumberAlreadyCounted(enteredSerialNumber)) {
                Object[] params = new Object[] { serialNumberModel.getLabel() };
                displayError(UINMessageText.UIN_ALREADY_ENTERED, params);
                clearTableEditor();
                return false;
            }

            setText(displayer.getDisplayText(serialNumber));
            lastCheckedSerialNumber = enteredSerialNumber;
            return true;
        } catch (BusinessException exception) {
            displayError(exception.getPrimaryMessageText(), exception.getPrimaryMessageValues());
        } catch (Throwable exception) {
            displayError(UIMessageText.DEFAULT_FATAL_MESSAGE, null);
        }
        clearTableEditor();
        return false;
    }

    private StockCountSerialNumber findNormalCountSerialNumber(String enteredSerialNumber) throws Exception {
        StockCountWrapper stockCount = (StockCountWrapper) RepositoryManager.getStateObject(SimClientStateKey.SELECTED_STOCK_COUNT);
        String itemId = serialNumberModel.getItemId();
        return findStockCountSerialNumber(Long.valueOf(stockCount.getId()), itemId, enteredSerialNumber);
    }

    private StockCountSerialNumber findAuthorizeSerialNumber(String enteredSerialNumber) throws Exception {
        StockCountWrapper stockCount = (StockCountWrapper) RepositoryManager.getStateObject(SimClientStateKey.SELECTED_STOCK_COUNT);
        String itemId = serialNumberModel.getItemId();
        StockCountSerialNumber stockCountSerialNumber = findStockCountSerialNumber(Long.valueOf(stockCount.getId()), itemId, enteredSerialNumber);
        if (stockCountSerialNumber != null) {
            return stockCountSerialNumber;
        }
        SerialNumberValue serialNumberValue = findSerialNumberValue(stockCount.getStoreId(), itemId, enteredSerialNumber);
        if (serialNumberValue == null) {
            serialNumberValue = findSerialNumberValueOrCreate(stockCount.getStoreId(), itemId, enteredSerialNumber);
            return BOFactory.createStockCountSerialNumber(serialNumberValue.getUinId(), serialNumberValue.getUin());
        }
        if (isInvalidStatusForAuthorizationAtStore(serialNumberValue)) {
            Object[] params = new String[4];
            params[0] = serialNumberModel.getLabel();
            params[1] = enteredSerialNumber;
            params[2] = Translator.getText(serialNumberValue.getStatus().toString());
            params[3] = String.valueOf(serialNumberValue.getStoreId());
            displayError(StockCountMessageText.UIN_STATUS_INVALID_FOR_AUTHORIZE, params);
            return null;
        }
        if (serialNumberValue.getStoreId().equals(stockCount.getStoreId())) {
            return BOFactory.createStockCountSerialNumber(serialNumberValue.getUinId(), serialNumberValue.getUin());
        }
        if (isValidStatusForAuthorizationAtOtherStore(serialNumberValue)) {
            return BOFactory.createStockCountSerialNumber(serialNumberValue.getUinId(), serialNumberValue.getUin());
        }
        if (SimConfigManager.getBoolean(SimConfigManager.ALLOW_UNEXPECTED_UINS) && isInStock(serialNumberValue)) {
            if (RConfirmUtility.confirm("Stock Counts", UINMessageText.MOVE_STORE_CONFIRM, serialNumberModel.getLabel())) {
                return BOFactory.createStockCountSerialNumber(serialNumberValue.getUinId(), serialNumberValue.getUin());
            }
            return null;
        }
        Object[] params = new String[4];
        params[0] = serialNumberModel.getLabel();
        params[1] = serialNumberValue.getUin();
        params[2] = Translator.getText(serialNumberValue.getStatus().toString());
        displayError(UINMessageText.UIN_AT_ANOTHER_STORE, params);
        return null;
    }

    private StockCountSerialNumber findStockCountSerialNumber(Long stockCountId, String itemId, String uin) throws Exception {
        return ClientServiceFactory.getStockCountLineItemServices().findStockCountSerialNumber(stockCountId, itemId, uin);
    }

    private SerialNumberValue findSerialNumberValue(Long storeId, String itemId, String uin) throws Exception {
        return ClientServiceFactory.getUINServices().findSerialNumberValue(itemId, uin);
    }

    private SerialNumberValue findSerialNumberValueOrCreate(Long storeId, String itemId, String uin) throws Exception {
        return ClientServiceFactory.getUINServices().findSerialNumberValueOrCreate(storeId, itemId, uin, serialNumberModel.getType(), serialNumberModel.getFunctionalArea());
    }

    private boolean isInvalidStatusForAuthorizationAtStore(SerialNumberValue serialNumberValue) {
        Set<UINStatus> invalidStatusSet = new HashSet<>(3);
        invalidStatusSet.add(UINStatus.UNCONFIRMED);
        invalidStatusSet.add(UINStatus.IN_RECEIVING);
        invalidStatusSet.add(UINStatus.SHIPPED_TO_STORE);
        return invalidStatusSet.contains(serialNumberValue.getStatus());
    }

    private boolean isValidStatusForAuthorizationAtOtherStore(SerialNumberValue serialNumberValue) {
        Set<UINStatus> validStatusSet = new HashSet<>();
        validStatusSet.add(UINStatus.CUSTOMER_FULFILLED);
        validStatusSet.add(UINStatus.MISSING);
        validStatusSet.add(UINStatus.REMOVE_FROM_INVENTORY);
        validStatusSet.add(UINStatus.SHIPPED_TO_FINISHER);
        validStatusSet.add(UINStatus.SHIPPED_TO_VENDOR);
        validStatusSet.add(UINStatus.SHIPPED_TO_WAREHOUSE);
        validStatusSet.add(UINStatus.SOLD);
        return validStatusSet.contains(serialNumberValue.getStatus());
    }

    private boolean isInStock(SerialNumberValue serialNumberValue) {
        return serialNumberValue.getStatus() == UINStatus.IN_STOCK;
    }

    private boolean isSerialNumberAlreadyCounted(String serialNumber) throws Exception {
        StockCountWrapper stockCount = (StockCountWrapper) RepositoryManager.getStateObject(SimClientStateKey.SELECTED_STOCK_COUNT);
        if (stockCount.isBreakdownSequenced()) {
            StockCountPhase phase;
            if (stockCount.getStockCountChild() != null) {
                phase = stockCount.getLocationPhase();
            } else {
                phase = stockCount.getPhase();
            }
            return ClientServiceFactory.getStockCountLineItemServices().isSerialNumberAlreadyCounted(stockCount.getId(), phase, serialNumberModel.getItemId(), serialNumber);
        }
        return false;
    }

    private void displaySpecificSerialNumberError(String enteredSerialNumber) throws Exception {
        SerialNumberValue serialNumberVO = ClientServiceFactory.getUINServices().findSerialNumberValue(serialNumberModel.getItemId(), enteredSerialNumber);
        if (serialNumberVO == null) {
            Object[] values = new String[3];
            values[0] = serialNumberModel.getLabel();
            values[1] = enteredSerialNumber;
            values[2] = serialNumberModel.getItemId();
            displayError(UINMessageText.UIN_NOT_FOUND_FOR_ITEM, values);
        } else if (!UINStatus.getValidSetForStockCount().contains(serialNumberVO.getStatus())) {
            Object[] values = new String[2];
            values[0] = serialNumberModel.getLabel();
            values[1] = enteredSerialNumber;
            displayError(UINMessageText.UIN_STATUS_INVALID_FOR_ACTION, values);
        } else if (!serialNumberVO.getStoreId().equals(SimRepository.getStoreId())) {
            Object[] values = new String[3];
            values[0] = serialNumberModel.getLabel();
            values[1] = serialNumberVO.getUin();
            values[2] = serialNumberVO.getStatus().toString();
            displayError(UINMessageText.UIN_AT_ANOTHER_STORE, values);
        } else {
            Object[] values = new String[2];
            values[0] = serialNumberModel.getLabel();
            values[1] = enteredSerialNumber;
            displayError(StockCountMessageText.UIN_NOT_FOUND, values);
        }
    }

    private void clearTableEditor() {
        lastCheckedSerialNumber = StringConstants.EMPTY;
        serialNumber = null;
        clear();
    }

    /****************************************************************************************************
     * Add Remove Table Editor Listeners
     ***************************************************************************************************/

    public void addTableEditorListener(SimTableEditorListener listener) {
        eventAdaptor.addTableEditorListener(listener);
    }

    public void removeTableEditorListener(SimTableEditorListener listener) {
        eventAdaptor.removeTableEditorListener(listener);
    }

    /****************************************************************************************************
     * Error State
     ***************************************************************************************************/
    protected void setErrorState(boolean errorState) {
        isErrorState = errorState;
    }

    protected boolean isErrorState() {
        return isErrorState;
    }

    /****************************************************************************************************
     * Determines if keystroke is invalid for input into the field.
     ***************************************************************************************************/
    public boolean isInvalidKeystroke(KeyEvent event) {
        return false;
    }

    /****************************************************************************************************
     * Implement the focus listener methods to call do value modified when focus is lost.
     ***************************************************************************************************/
    public void focusGained(FocusEvent event) {
    }

    public void focusLost(FocusEvent event) {
        if (event.isTemporary()) {
            return;
        }
        if (checkValue()) {
            eventAdaptor.fireTypeEditorEvent(true);
            return;
        }
        reactivateEditing(event.getOppositeComponent());
    }

    /****************************************************************************************************
     * Display Error Methods
     ***************************************************************************************************/

    protected void displayErrorWithReset(MessageText message, Object[] values) {
        if (displayer != null) {
            setText(displayer.getDisplayText(serialNumber));
        }
        setErrorState(true);
        displayError(message, values);
    }

    protected void displayError(MessageText message, Object[] values) {
        RErrorDialog dialog = new RErrorDialog(Application.getFrame());
        dialog.setTitle("Table Editor Error");
        dialog.setMessage(message, values);
        dialog.activate();
    }
}
