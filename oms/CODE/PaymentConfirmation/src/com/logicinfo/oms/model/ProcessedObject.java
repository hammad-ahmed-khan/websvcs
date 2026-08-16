package com.logicinfo.oms.model;


import com.logicinfo.oms.ejb.OmsCustOrdReserve;
import com.logicinfo.oms.ejb.OmsTempCoFo;

import com.oracle.retail.integration.base.bo.fulfilordcfmcol.v1.FulfilOrdCfmCol;

import java.util.ArrayList;


public class ProcessedObject {
    public ProcessedObject() {
        super();
        this.isReservation = "";
        this.processingApp="";
    }
    
   OmsCustOrdReserve omsCustOrdReserve;
   String isReservation;
   String processingApp;
   String status;
    FulfilOrdCfmCol fulfilOrdCfmCol;
    ArrayList<OmsTempCoFo> omsTempCoFoList;
    int currentFulFillOrderNo;
    String tsf_no;

    public ProcessedObject(OmsCustOrdReserve omsCustOrdReserve, String isReservation,String processingApp) {
        super();
        this.omsCustOrdReserve = omsCustOrdReserve;
        this.isReservation = "";
        this.processingApp="";
    }
   
    public void setOmsCustOrdReserve(OmsCustOrdReserve omsCustOrdReserve) {
        this.omsCustOrdReserve = omsCustOrdReserve;
    }

    public OmsCustOrdReserve getOmsCustOrdReserve() {
        return omsCustOrdReserve;
    }

    public void setIsReservation(String isReservation) {
        this.isReservation = isReservation;
    }

    public String getIsReservation() {
        return isReservation;
    }

    public void setProcessingApp(String processingApp) {
        this.processingApp = processingApp;
    }

    public String getProcessingApp() {
        return processingApp;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setFulfilOrdCfmCol(FulfilOrdCfmCol fulfilOrdCfmCol) {
        this.fulfilOrdCfmCol = fulfilOrdCfmCol;
    }

    public FulfilOrdCfmCol getFulfilOrdCfmCol() {
        return fulfilOrdCfmCol;
    }


    public void setOmsTempCoFoList(ArrayList<OmsTempCoFo> omsTempCoFoList) {
        this.omsTempCoFoList = omsTempCoFoList;
    }

    public ArrayList<OmsTempCoFo> getOmsTempCoFoList() {
        return omsTempCoFoList;
    }

    public void setCurrentFulFillOrderNo(int currentFulFillOrderNo) {
        this.currentFulFillOrderNo = currentFulFillOrderNo;
    }

    public int getCurrentFulFillOrderNo() {
        return currentFulFillOrderNo;
    }

    public void setTsf_no(String tsf_no) {
        this.tsf_no = tsf_no;
    }

    public String getTsf_no() {
        return tsf_no;
    }
}
