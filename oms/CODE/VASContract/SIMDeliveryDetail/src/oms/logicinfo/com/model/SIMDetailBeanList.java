package oms.logicinfo.com.model;

import java.util.ArrayList;
import java.util.List;

public class SIMDetailBeanList {
    public SIMDetailBeanList() {
        super();
    }
    List<SIMDetailBean> simDetailBeanList  = new ArrayList<SIMDetailBean>();


    public void setSimDetailBeanList(List<SIMDetailBean> simDetailBeanList) {
        this.simDetailBeanList = simDetailBeanList;
    }

    public List<SIMDetailBean> getSimDetailBeanList() {
        return simDetailBeanList;
    }
}
