package oracle.retail.sim.client.swing.test;

/********************************************************************************************************
 * Data Object for testing List Of Values and Table Of Values
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TestListOfValuesData {

    private String nameit;

    public TestListOfValuesData(String name) {
        nameit = name;
    }

    public String getName() {
        return nameit;
    }

    public boolean getTooLong() {
        return nameit.length() > 6;
    }

    public int getLength() {
        return nameit.length();
    }
}
