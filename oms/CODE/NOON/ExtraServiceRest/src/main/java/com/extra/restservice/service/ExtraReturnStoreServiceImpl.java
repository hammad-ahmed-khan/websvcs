package com.extra.restservice.service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.extra.restservice.bean.Customer;
import com.extra.restservice.util.ConstantSchema;
import com.oracle.www.retail.integration.base.bo.InvocationSuccess.v1.InvocationSuccess;
import com.oracle.www.retail.integration.base.bo.PosTrnColDesc.v1.PosTrnColDesc;
import com.oracle.www.retail.integration.base.bo.PosTrnDesc.v1.PosTrnDesc;
import com.oracle.www.retail.integration.base.bo.PosTrnDesc.v1.PosTrnItm;
import com.oracle.www.retail.integration.base.bo.PosTrnDesc.v1.PosTrnItmTranCode;
import com.oracle.www.retail.integration.base.bo.PosTrnDesc.v1.PosTrnOrdResvType;
import com.oracle.www.retail.sim.integration.services.POSTransactionService.v1.POSTransactionServiceLocator;

@Service
public class ExtraReturnStoreServiceImpl implements ExtraReturnStoreService {

	ExtraReturnStoreServiceImpl() {
		System.out.println("Inside ExtraReturnStoreServiceImpl");
	}

	@Autowired
	NamedParameterJdbcTemplate namedjdbcTemplate;

	@Value("${pos.url}")
	String url;

	@Autowired
	JdbcTemplate jdbcTemplate;

	private String fetchNoonRmaSeq() {
		Map<String, Object> map = new HashMap<String, Object>();
		String seq_num = namedjdbcTemplate.queryForObject(ConstantSchema.NOON_RMA_SEQ_NEXT, map, String.class);
		return seq_num;
	}

	public void callWebservice(Customer customer) {
		System.out.println("Inside ExtraReturnStoreServiceImpl callWebservice");
		try {
			// POSTransactionService loc = new POSTransactionServiceLocator();
//			 POSTransactionServiceLocator loc1 = new POSTransactionServiceLocator();

			POSTransactionServiceLocator loc = new POSTransactionServiceLocator();
			loc.setPOSTransactionPortEndpointAddress(url);
			System.out.println("Inside ExtraReturnStoreServiceImpl Try" + loc.getPOSTransactionPort().ping("test"));
			PosTrnItm posTrnItm = new PosTrnItm();
			PosTrnDesc posTrnDesc = new PosTrnDesc();
			PosTrnColDesc posTrnColDesc = new PosTrnColDesc();
			System.out.println("Inside ExtraReturnStoreServiceImpl Try*****");
			posTrnDesc.setStore_id(Long.parseLong(customer.getReturnLocId()));
			String tranSeq = fetchNoonRmaSeq();
			String tranId = customer.getReturnLocId() + "101000" + tranSeq;
			posTrnDesc.setTransaction_id(tranId);
			posTrnDesc.setTransaction_timestamp(Calendar.getInstance());
			posTrnDesc.setCust_order_id(customer.getCustomerOrderNo());
			System.out.println("After posTrnDesc:" + posTrnDesc.getStore_id() + ":" + posTrnDesc.getCust_order_id());
//Charan_PROD			 
//			 posTrnItm.setItem_id(customer.getItems().get(0).getItemCode());
//			 posTrnItm.setQuantity(new BigDecimal(customer.getItems().get(0).getOrderQty()));
			posTrnItm.setItem_id(customer.getItemCode());
			posTrnItm.setQuantity(new BigDecimal(customer.getOrderQty()));
			System.out.println("After posTrnItm::" + posTrnItm.getItem_id() + ":" + posTrnItm.getQuantity());
			posTrnItm.setUnit_of_measure("EA");
			posTrnItm.setDrop_ship(false);
			posTrnItm.setComments("NOONRET" + "#" + customer.getCustomerOrderNo());
			posTrnItm.setReservation_type(PosTrnOrdResvType.NO_VALUE);
			posTrnItm.setTransaction_code(PosTrnItmTranCode.RETURN);
			System.out.println("After posTrnItm");
			posTrnColDesc.setCollection_size(1);
			PosTrnItm[] posTrnItmArr = { posTrnItm };
			posTrnDesc.setPosTrnItm(posTrnItmArr);
			System.out.println("After set posTrnItm");
			PosTrnDesc posTrnDescArr[] = { posTrnDesc };
			posTrnColDesc.setPosTrnDesc(posTrnDescArr);
			System.out.println("Inside ExtraReturnStoreServiceImpl Try");

			InvocationSuccess invocationSuccess = loc.getPOSTransactionPort().processPOSTransactions(posTrnColDesc);

			System.out.println("Call to service response : " + invocationSuccess.getSuccess_message());

		} catch (Exception e) {
			System.out.println("Inside ExtraReturnStoreServiceImpl Exception***" + e.getMessage());
			e.printStackTrace();
		}
	}

}
