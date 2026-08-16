package com.extra.jood.bean;

import java.util.List;

public class OrderDetail {
	
	 private String orderId;
	 
     private List<TransactionDetails> transactions;

	 public String getOrderId() {
		 return orderId;
	 }

	 public void setOrderId(String orderId) {
		 this.orderId = orderId;
	 }

	 public List<TransactionDetails> getTransactions() {
		 return transactions;
	 }

	 public void setTransactions(List<TransactionDetails> transactions) {
		 this.transactions = transactions;
	 }

     
     
}
