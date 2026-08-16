package com.logicinfo.oms.model;

import java.util.List;

public class TransferRequest {
    private String src_id;
    private String dest_id;
    private String ref_no;
    private List<TransferItems> customerItems;
    private int src_loc;
    private int ful_loc;
    private int ful_ord_no;
    private String cust_ord_no;
    
    

    public String getSrc_id() {
        return src_id;
    }

    public void setSrc_id(String src_id) {
        this.src_id = src_id;
    }

    public String getDest_id() {
        return dest_id;
    }

    public void setDest_id(String dest_id) {
        this.dest_id = dest_id;
    }

    public String getRef_no() {
        return ref_no;
    }

    public void setRef_no(String ref_no) {
        this.ref_no = ref_no;
    }

    public List<TransferItems> getCustomerItems() {
        return customerItems;
    }

    public void setCustomerItems(List<TransferItems> customerItems) {
        this.customerItems = customerItems;
    }

    public void setSrc_loc(int src_loc) {
        this.src_loc = src_loc;
    }

    public int getSrc_loc() {
        return src_loc;
    }

    public void setFul_loc(int ful_loc) {
        this.ful_loc = ful_loc;
    }

    public int getFul_loc() {
        return ful_loc;
    }

    public void setFul_ord_no(int ful_ord_no) {
        this.ful_ord_no = ful_ord_no;
    }

    public int getFul_ord_no() {
        return ful_ord_no;
    }

    public void setCust_ord_no(String cust_ord_no) {
        this.cust_ord_no = cust_ord_no;
    }

    public String getCust_ord_no() {
        return cust_ord_no;
    }
}
