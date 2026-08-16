package oracle.retail.sim.client.swing.table;

/********************************************************************************************************
 * This exception class is unchecked so that is can be thrown from within the table model. It is meant to
 * be thrown when the table cell editor has an exception during processing or when other errors occur
 * within the SimTable.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class SimTableException extends RuntimeException {
    private static final long serialVersionUID = -6018171214113628971L;

    private Object value;

    /****************************************************************************************************
     * Constructor.
     ***************************************************************************************************/
    public SimTableException() {
    }

    /****************************************************************************************************
     * Constructor.
     * @param exception The original cause of the exception.
     ***************************************************************************************************/
    public SimTableException(Throwable exception) {
        super(exception);
    }

    /****************************************************************************************************
     * Constructor.
     * @param exception The original cause of the exception.
     * @param value A value that triggered the exception (often the piece of data being set by an
     *            editor).
     ***************************************************************************************************/
    public SimTableException(Throwable exception, Object value) {
        super(exception);
        setValue(value);
    }

    /****************************************************************************************************
     * Assigns a data object to the table exception.
     ***************************************************************************************************/
    public void setValue(Object value) {
        this.value = value;
    }

    /****************************************************************************************************
     * Retrieves the data object from the table exception.
     ***************************************************************************************************/
    public Object getValue() {
        return value;
    }
}
