package com.extra.oms.dao;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;

import com.extra.oms.model.OrderCancelDetailRequest;
import com.extra.oms.model.POSOrderCancelRequest;

@Repository
public class ServiceOrderDetailsDAO {

	private static final Logger log = LogManager.getLogger(ServiceOrderDetailsDAO.class);

	private static final String ORDER_SELECT_QUERY = "SELECT S.*, H.APPLICATION_ID, ENTITY_ID, ORDER_REQUESTOR_ID, DELIVERY_TYPE FROM "
			+ "XX_CC_SERVICE_CANCEL_DTL S, OMS_CUST_ORD_HEAD H WHERE H.CUST_ORDER_NO = S.ORDER_NO AND S.STATUS = 'N'";
	
	@Autowired
	private NamedParameterJdbcOperations jdbcTemplate;

	public Collection<POSOrderCancelRequest> getServiceOrderDetails() {
		return jdbcTemplate.query(ORDER_SELECT_QUERY, new ServiceOrderRowMapper());
	}

	private static class ServiceOrderRowMapper implements ResultSetExtractor<Collection<POSOrderCancelRequest>> {
		@Override
		public Collection<POSOrderCancelRequest> extractData(ResultSet rs) throws SQLException {

			Map<String, POSOrderCancelRequest> orderMap = new HashMap<String, POSOrderCancelRequest>();
			while (rs.next()) {				
				String orderNo = rs.getString("ORDER_NO");
				POSOrderCancelRequest order = orderMap.get(orderNo);
				if (order == null) {
					order = new POSOrderCancelRequest();
					order.setApplicationId(rs.getString("APPLICATION_ID"));
					order.setEntityId(rs.getString("ENTITY_ID"));
					order.setCancellationDate(new Date());
					order.setCancellationId(rs.getString("OMS_CANCEL_ID"));
					order.setCancellationRequestorId(rs.getString("ORDER_REQUESTOR_ID"));
					order.setComments("Service Order Cancellation");
					order.setCustOrderNo(orderNo);
					order.setLogSeqId(rs.getLong("SEQUENCE_ID"));
					order.setOmsCancelId(rs.getLong("OMS_CANCEL_ID"));
					order.setReason("Service Order Cancellation");
					order.setRefundAmount(BigDecimal.ZERO);
					order.setRefundPreference(rs.getString("REFUND_OPTION"));
					order.setRequestDatetimestamp(new Date());
					order.setCancellationItems(new ArrayList<OrderCancelDetailRequest>());
					orderMap.put(orderNo, order);
				}
				order.setRefundAmount(rs.getBigDecimal("UNIT_CANCEL_AMOUNT").add(order.getRefundAmount()));
				OrderCancelDetailRequest detail = new OrderCancelDetailRequest();
				detail.setCancelQtySuom(rs.getBigDecimal("CANCEL_QTY"));
				detail.setItem(rs.getString("ITEM"));
				detail.setLineNo(rs.getBigDecimal("LINE_NO"));
				detail.setItemComments("Service Order Cancellation");
				order.getCancellationItems().add(detail);
			}
			return orderMap.values();
		}
	}

	public void updateCancelRequestStatus(String orderNo) {
		String query = "UPDATE XX_CC_SERVICE_CANCEL_DTL SET STATUS = 'S' WHERE ORDER_NO = :orderNo";
		jdbcTemplate.update(query, Collections.singletonMap("orderNo", orderNo));
		log.info("Order status updated");
	}
}
