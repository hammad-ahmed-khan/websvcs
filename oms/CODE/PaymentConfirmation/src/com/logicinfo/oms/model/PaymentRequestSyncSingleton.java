package com.logicinfo.oms.model;

import java.util.HashMap;
import java.util.Map;

public class PaymentRequestSyncSingleton {
    
    private static PaymentRequestSyncSingleton paymentRequestSyncSingleton = new PaymentRequestSyncSingleton(); 
    private static Map<String ,String> inputRequestMap = new HashMap<String,String>();

    public static void setPaymentRequestSyncSingleton(PaymentRequestSyncSingleton paymentRequestSyncSingleton) {
        PaymentRequestSyncSingleton.paymentRequestSyncSingleton = paymentRequestSyncSingleton;
    }

    public static PaymentRequestSyncSingleton getPaymentRequestSyncSingleton() {
        return paymentRequestSyncSingleton;
    }

    public static void setInputRequestMap(Map<String, String> inputRequestMap) {
        PaymentRequestSyncSingleton.inputRequestMap = inputRequestMap;
    }

    public static Map<String, String> getInputRequestMap() {
        return inputRequestMap;
    }
}
