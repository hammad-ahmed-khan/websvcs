package org.datacontract.schemas._2004._07.extra_services_shipping.repo;

public class ExtraCouierResponse {
    
    private Boolean  packageResult;
    private String  labelData;
    private String airwaryBillno;
    private String errorMessage;
    
    public ExtraCouierResponse() {
        
    }

    public ExtraCouierResponse(String labelData, String airwaryBillno,
                               String errorMessage, Boolean packageResult) {
        this.labelData = labelData;
        this.airwaryBillno = airwaryBillno;
        this.errorMessage = errorMessage;
        this.packageResult =packageResult;
    }


    public void setLabelData(String labelData) {
        this.labelData = labelData;
    }

    public String getLabelData() {
        return labelData;
    }

    public void setAirwaryBillno(String airwaryBillno) {
        this.airwaryBillno = airwaryBillno;
    }

    public String getAirwaryBillno() {
        return airwaryBillno;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setPackageResult(Boolean packageResult) {
        this.packageResult = packageResult;
    }

    public Boolean getPackageResult() {
        return packageResult;
    }
}
