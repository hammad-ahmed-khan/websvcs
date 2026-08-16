package com.extra.restservice.dao;

import java.sql.SQLException;
import com.extra.restservice.bean.Customer;



public interface ExtraReturnStoreDao {

	public String checkCustomerData(Customer customer) throws SQLException;

	public String insertCustomerData(Customer customer);

	public void insertRequestData(Customer customer);

	public void insertItemData(Customer customer, String RMAID);

	public void insertOMSDetails(Customer customer, String RMAID);
}
