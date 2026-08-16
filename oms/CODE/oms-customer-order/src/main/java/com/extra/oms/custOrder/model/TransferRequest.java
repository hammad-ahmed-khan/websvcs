package com.extra.oms.custOrder.model;

import java.util.List;

public class TransferRequest {
    private int src_id;
    private int dest_id;
    private String ref_no;
    private int src_loc;
    private int ful_loc;
    private int ful_ord_no;
    private String cust_ord_no;
    private List<TransferItems> customerItems;


    public void setSrc_id(int src_id) {
        this.src_id = src_id;
    }

    public int getSrc_id() {
        return src_id;
    }

    public void setDest_id(int dest_id) {
        this.dest_id = dest_id;
    }

    public int getDest_id() {
        return dest_id;
    }

    public void setRef_no(String ref_no) {
        this.ref_no = ref_no;
    }

    public String getRef_no() {
        return ref_no;
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

    public void setCustomerItems(List<TransferItems> customerItems) {
        this.customerItems = customerItems;
    }

    public List<TransferItems> getCustomerItems() {
        return customerItems;
    }
}
