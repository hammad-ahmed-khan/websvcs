package oracle.retail.sim.client.swing.test;

import java.util.Date;
import oracle.retail.sim.common.business.Quantity;

/********************************************************************************************************
 * Test Sim Table Date
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TestSimTableData {

    private String value1;
    private String value2;
    private Date value3;
    private Quantity value4;
    private boolean value5 = true;

    public TestSimTableData(String text1, String text2, Date text3, Quantity text4, boolean text5) {
        value1 = text1;
        value2 = text2;
        value3 = text3;
        value4 = text4;
        value5 = text5;
    }

    public String getValue1() {
        return value1;
    }

    public String getValue2() {
        return value2;
    }

    public Date getValue3() {
        return value3;
    }

    public Quantity getValue4() {
        return value4;
    }

    public boolean getValue5() {
        return value5;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

    public void setValue3(Date value3) {
        this.value3 = value3;
    }

    public void setValue4(Quantity value4) {
        this.value4 = value4;
    }

    public void setValue5(boolean value5) {
        this.value5 = value5;
    }
}
