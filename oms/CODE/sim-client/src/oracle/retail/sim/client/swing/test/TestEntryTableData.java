package oracle.retail.sim.client.swing.test;

import java.math.BigDecimal;
import java.util.Date;

/*********************************************************************************************
 * Test Entry Table Data
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *********************************************************************************************/

public class TestEntryTableData {

    private Date tx1;
    private BigDecimal tx2;
    private String tx3;

    public TestEntryTableData(Date t1, BigDecimal t2, String t3) {
        tx1 = t1;
        tx2 = t2;
        tx3 = t3;
    }

    public Date getTestOne() {
        return tx1;
    }

    public BigDecimal getTestTwo() {
        return tx2;
    }

    public String getTestThree() {
        return tx3;
    }

    public String getTestFour() {
        return "Label";
    }

    public void setTestOne(Date s) {
        tx1 = s;
    }

    public void setTestTwo(BigDecimal x) {
        tx2 = x;
    }

    public void setTestThree(String x) {
        tx3 = x;
    }
}
