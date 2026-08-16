package oracle.retail.sim.client.swing.lov;

import java.util.Collection;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.swing.util.RErrorSeverity;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.swing.util.UIMessageText;

/******************************************************************************************
 * This default implementation of the ListOfValuesModel interface handles the retrieval of
 * selectable values and the setting of selected values on a model. In the constructor,
 * the data type defines the method call on the model that must be implemented.  For example,
 * if the data type is "dept" then the data model must implement getSelectableDept() and
 * setSelectedDept(Collection collection). An attribute of a data type is an identifier
 * used for comparison. For example, the attribute "name" means that when someone types
 * in the list of values editor, dept.getName() is called to determine a match.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ******************************************************************************************/

public class DefaultListOfValuesModel implements ListOfValuesModel {

    private Object screenModel;
    private String dataType;
    private String identifier;

    /******************************************************************************************
     * Constructs a new DefaultListOfValuesModel.
     * <p>
     * @param screenModel The model to retrieve and set data on.
     * @param dataType The data type that is being retrieved and set.
     * @param identifier This represents an attribute within the data type that is used for
     * comparison when a value is typed in the text field portion of the editor.
     ******************************************************************************************/
    public DefaultListOfValuesModel(Object screenModel, String dataType, String identifier) {
        if (screenModel == null) {
            throw new IllegalArgumentException("ScreenModel cannot be null!");
        }
        if (StringUtility.isNullOrEmpty(dataType)) {
            throw new IllegalArgumentException("Data type cannot be null or empty!");
        }
        if (StringUtility.isNullOrEmpty(identifier)) {
            throw new IllegalArgumentException("Data type identifier cannot be null or empty!");
        }
        this.screenModel = screenModel;
        this.dataType = dataType;
        this.identifier = identifier;
    }

    /******************************************************************************************
     * Retrieves the assigned identifier of the page model.
     ******************************************************************************************/
    public String getIdentifier() {
        return identifier;
    }

    /******************************************************************************************
     * This method retrieves the selectable values from the model.
     * <p>
     * @return The full collection of selectable data objects.
     * <p>
     * @throws OldUIException If an error occurs retrieving the selectable values.
     ******************************************************************************************/
    public Collection getSelectableValues() throws UIException {
        return ListOfValuesUtility.getSelectableValues(screenModel, dataType);
    }

    /******************************************************************************************
     * This method retrieves all the selected values for a string array of input values. This
     * method retrieves all the selectable values and uses the data type identifier to determine
     * which values should be consider selected. If a value exists in the parameter but not
     * in the selectable values, then an exception is thrown.
     * <p>
     * @param values A string array of values to find in the selectable objects.
     * <p>
     * @return All selectable objects that contain the values passed in as parameters.
     * <p>
     * @throws OldUIException Thrown if a selectable object does not exist for the input value.
     ******************************************************************************************/
    public Collection getSelectedValues(String[] values) throws UIException {
        Collection selectedValues = ListOfValuesUtility.getSelectedValues(screenModel, dataType, values);
        if (values.length != selectedValues.size()) {
            throw new UIException(UIMessageText.LOV_INVALID_SELECTED_INPUT, RErrorSeverity.ERROR);
        }
        return selectedValues;
    }

    /******************************************************************************************
     * This method is called whenever the selected values changes inside an editor. It uses the
     * screen model, data type and input collection to set the selected values on the screen model.
     * In other words, it effectively calls dataModel.setDataType(collection).
     * <p>
     * @param collection The collection of selected values.
     * <p>
     * @throws OldUIException Throws if an error occurs attempts to set the selected values.
     ******************************************************************************************/
    public void setSelectedValues(Collection collection) throws UIException {
        ListOfValuesUtility.setSelectedValues(screenModel, dataType, collection);
    }
}
