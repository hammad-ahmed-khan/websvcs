package com.logicinfo.oms.model;


import com.logicinfo.oms.ejb.OmsTempCoFo;

import com.oracle.retail.integration.base.bo.fulfilordcfmcol.v1.FulfilOrdCfmCol;

import java.util.ArrayList;

public class ProcessedObject {
    public ProcessedObject() {
        super();
    }
    ArrayList<OmsTempCoFo> omsTempCoFoList;
    FulfilOrdCfmCol fulfilOrdCfmCol;
    int currentFulFilOrderNo;
    String tsf_no;

    public void setOmsTempCoFoList(ArrayList<OmsTempCoFo> omsTempCoFoList) {
        this.omsTempCoFoList = omsTempCoFoList;
    }

    public ArrayList<OmsTempCoFo> getOmsTempCoFoList() {
        return omsTempCoFoList;
    }

    public void setFulfilOrdCfmCol(FulfilOrdCfmCol fulfilOrdCfmCol) {
        this.fulfilOrdCfmCol = fulfilOrdCfmCol;
    }

    public FulfilOrdCfmCol getFulfilOrdCfmCol() {
        return fulfilOrdCfmCol;
    }

    public void setCurrentFulFilOrderNo(int currentFulFilOrderNo) {
        this.currentFulFilOrderNo = currentFulFilOrderNo;
    }

    public int getCurrentFulFilOrderNo() {
        return currentFulFilOrderNo;
    }


    public void setTsf_no(String tsf_no) {
        this.tsf_no = tsf_no;
    }

    public String getTsf_no() {
        return tsf_no;
    }
}
