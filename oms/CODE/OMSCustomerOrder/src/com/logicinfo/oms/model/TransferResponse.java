package com.logicinfo.oms.model;

public class TransferResponse {
    private int code;
    private String success;
    private String tsf_No;
    private String message;


    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getSuccess() {
        return success;
    }

    public void setTsf_No(String tsf_No) {
        this.tsf_No = tsf_No;
    }

    public String getTsf_No() {
        return tsf_No;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
