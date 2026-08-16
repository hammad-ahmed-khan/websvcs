package com.logicinfo.oms.model;

public class TransferResponse {
    private String code;
    private String success;
    private String tsf_no;
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getTsf_no() {
        return tsf_no;
    }

    public void setTsf_no(String tsf_no) {
        this.tsf_no = tsf_no;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
