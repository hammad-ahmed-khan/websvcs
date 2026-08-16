package oracle.retail.sim.client.swing.displaytable;

import oracle.retail.sim.client.swing.util.UIProblem;

/******************************************************************************************
 * This class represents a single UIProblem mapped to a display table row and column.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ******************************************************************************************/

class RDisplayTableProblem {

    private UIProblem problem;
    private int row = -1;
    private int column = -1;

    /******************************************************************************************
     * Constructs a new RDisplayTableProblem.
     * <p>
     * @param problem The UIProblem to display.
     * @param row The row the problem resides at.
     * @param column The column the problem resides at.
     ******************************************************************************************/
    public RDisplayTableProblem(UIProblem problem, int row, int column) {
        this.problem = problem;
        this.row = row;
        this.column = column;
    }

    /******************************************************************************************
     * Retrieves the UIProblem.
     * <p>
     * @return The UIProblem.
     ******************************************************************************************/
    public UIProblem getProblem() {
        return problem;
    }

    /******************************************************************************************
     * Retrieves the row.
     * <p>
     * @return The row.
     ******************************************************************************************/
    public int getRow() {
        return row;
    }

    /******************************************************************************************
     * Retrieves the column.
     * <p>
     * @return The column.
     ******************************************************************************************/
    public int getColumn() {
        return column;
    }
}
