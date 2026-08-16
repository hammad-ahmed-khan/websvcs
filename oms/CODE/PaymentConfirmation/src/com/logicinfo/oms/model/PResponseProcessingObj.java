package com.logicinfo.oms.model;


import com.logicinfo.oms.ejb.OmsTempCoFo;

import com.oracle.retail.integration.base.bo.fulfilordcfmcol.v1.FulfilOrdCfmCol;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.TreeMap;


public class PResponseProcessingObj 
{
    public PResponseProcessingObj()
    {
        super();
    }
    TreeMap<BigDecimal, ArrayList<OmsTempCoFo>> newFulfillMap;
    FulfilOrdCfmCol fulfilOrdCfmCol;
    BigDecimal newSourceLocId ;
    String responseStatus;
    
    public void setNewFulfillMap(TreeMap<BigDecimal, ArrayList<OmsTempCoFo>> newFulfillMap) {
        this.newFulfillMap = newFulfillMap;
    }

    public TreeMap<BigDecimal, ArrayList<OmsTempCoFo>> getNewFulfillMap() {
        return newFulfillMap;
    }

    public void setFulfilOrdCfmCol(FulfilOrdCfmCol fulfilOrdCfmCol) {
        this.fulfilOrdCfmCol = fulfilOrdCfmCol;
    }

    public FulfilOrdCfmCol getFulfilOrdCfmCol() {
        return fulfilOrdCfmCol;
    }


    public void setResponseStatus(String responseStatus) {
        this.responseStatus = responseStatus;
    }

    public String getResponseStatus() {
        return responseStatus;
    }
}
