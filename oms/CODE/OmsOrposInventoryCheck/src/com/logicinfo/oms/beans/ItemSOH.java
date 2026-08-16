package com.logicinfo.oms.beans;

import java.math.BigDecimal;

public class ItemSOH {
    public ItemSOH() {
        super();
    }
    BigDecimal location;
    BigDecimal soh;
    int lastPriority;
    int counter;

    public void setLocation(BigDecimal location) {
        this.location = location;
    }

    public BigDecimal getLocation() {
        return location;
    }

    public void setSoh(BigDecimal soh) {
        this.soh = soh;
    }

    public BigDecimal getSoh() {
        return soh;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public int getCounter() {
        return counter;
    }

    public void setLastPriority(int lastPriority) {
        this.lastPriority = lastPriority;
    }

    public int getLastPriority() {
        return lastPriority;
    }
}
