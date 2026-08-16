package com.logicinfo.oms.model;

import com.logicinfo.oms.ejb.OmsSparePartFulfill;
import com.logicinfo.oms.ejb.OmsSparePartHeader;

import java.util.ArrayList;
import java.util.List;

public class SparePartsCancellationProcessedObjects {
    
    private List<SparePartsCancellationProcessedFulfillmentObjects> processedFulfillObjs;
    private OmsSparePartHeader sparePartsHeaderObj;

    
    public SparePartsCancellationProcessedObjects() {
        super();
        this.processedFulfillObjs =  new ArrayList <SparePartsCancellationProcessedFulfillmentObjects>();
        this.sparePartsHeaderObj = null;
    }


    public void setProcessedFulfillObjs(List<SparePartsCancellationProcessedFulfillmentObjects> processedFulfillObjs) {
        this.processedFulfillObjs = processedFulfillObjs;
    }

    public List<SparePartsCancellationProcessedFulfillmentObjects> getProcessedFulfillObj() {
        return processedFulfillObjs;
    }

    public void setSparePartsHeaderObj(OmsSparePartHeader sparePartsHeaderObj) {
        this.sparePartsHeaderObj = sparePartsHeaderObj;
    }

    public OmsSparePartHeader getSparePartsHeaderObj() {
        return sparePartsHeaderObj;
    }
}
