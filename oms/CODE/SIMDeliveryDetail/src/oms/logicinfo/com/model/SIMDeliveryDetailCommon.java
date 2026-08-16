package oms.logicinfo.com.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.logicinfo.oms.util.OMSConstants;
import com.logicinfo.oms.util.OMSUtil;

public class SIMDeliveryDetailCommon {
	private final static Logger log = Logger.getLogger(SIMDeliveryDetailCommon.class.getName());

	public SIMDeliveryDetailCommon() {
		super();
	}

	public ArrayList<OrderInformation> processReadyToShipDetails(Long NoOfOrders) {
		SIMDetailBean sIMDetailBean = null;
		List<SIMDetailBean> sIMDetailBeanList = new ArrayList<SIMDetailBean>();
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		String customerOrderId;
		Map<String, ArrayList<ItemListBean>> map = new HashMap<String, ArrayList<ItemListBean>>();
		ArrayList<ItemListBean> itemBean = null;
//        String query1= null;

		OrderInformation orderInformation = null;
		ArrayList<OrderInformation> OrderInformationList = new ArrayList<OrderInformation>();

		String query = " select * from SIM_DEL_DETAILS_RTS where rownum <= ? ";
		log.info(query);

		try {
			log.info("connecting to SIM Schema");
			log.info("OMSConstants.DS_SIM_STRING " + OMSConstants.DS_SIM_STRING);
			conn = OMSUtil.createDBConnection(OMSConstants.DS_SIM_STRING);
			log.info("conn : " + conn);
			preparedStatement = conn.prepareStatement(query);
			preparedStatement.setLong(1, NoOfOrders);

			// execute the java preparedstatement

			// log.info("ROWNUM / No of Orders count : " +input.getNumberOfOrders());
			// preparedStatement.setLong(1, input.getNumberOfOrders());
			rs = preparedStatement.executeQuery();

			log.info("after executing the query RTS" + rs.isBeforeFirst());

			if (rs.isBeforeFirst()) {
				while (rs.next()) {
					log.info("Inside while loop of database");
					sIMDetailBean = new SIMDetailBean();
					ItemListBean itemListBean = new ItemListBean();

					orderInformation = new OrderInformation();
					orderInformation.setCarrier(rs.getString(10));
					orderInformation.setIsCod(null);
					orderInformation.setOrderValue(null);
					// orderInformation.setShipmentId(rs.getString(2));
					orderInformation.setStoreId(rs.getInt(1));

					orderInformation.setOrderNo(rs.getString(3));

					orderInformation.setItemListBeanDetail(null);

					if (OrderInformationList.size() == 0) {
						OrderInformationList.add(orderInformation);
					} else {

						Boolean orderInformationcheck = OrderInformationList.contains(orderInformation);

						if (Boolean.FALSE == orderInformationcheck) {
							OrderInformationList.add(orderInformation);
						}
					}
					log.info("Size of OrderInformationList :" + OrderInformationList.size());
					customerOrderId = itemListBean.getCustomerOrderId();

					itemListBean.setCustomerOrderId(rs.getString(3));
					log.info("Customer Order Number : " + rs.getString(3));
					log.info("Line_no : " + rs.getString(4));
					itemListBean.setLineNo(rs.getBigDecimal(4));
					itemListBean.setItem(rs.getString(5));
					log.info("Item : " + rs.getString(5));
					itemListBean.setQuantity(rs.getInt(6));
					log.info("Quantity : " + rs.getString(6));
					itemListBean.setDeliveryId(rs.getInt(2));
					log.info("Delivery Id : " + rs.getInt(2));

					itemListBean.setDescription(rs.getString(10));
					log.info("Description : " + rs.getString(10));
					itemListBean.setUnitCostValue(rs.getBigDecimal(7));
					log.info("Unit Cost : " + rs.getBigDecimal(7));
					itemListBean.setUnitCostCurrency(rs.getString(8));
					log.info("Unit Currency :" + rs.getString(8));
					if (map.get(itemListBean.getCustomerOrderId()) == null || map.get(itemListBean.getCustomerOrderId()).size() == 0) {
						itemBean = new ArrayList<ItemListBean>();
						itemBean.add(itemListBean);
						log.info("Customer id ----" + rs.getString(3));
						map.put(rs.getString(3), itemBean);
					} else {
						ArrayList<ItemListBean> existingList = map.get(itemListBean.getCustomerOrderId());
						existingList.add(itemListBean);
						log.info("Customer id ----" + rs.getString(3));
						map.put(rs.getString(3), existingList);
					}
					log.info("Map size " + map.size());
					//// sIMDetailBean.setItemListBeanMap(map);
				}

			}
			log.info("Size of OrderInformationList :" + OrderInformationList.size());
			log.info("Iterate the map : " + map.size());
			for (String cusId : map.keySet()) {
				log.info("Customer Order No : " + cusId);
				ArrayList<ItemListBean> itemDetailList = map.get(cusId);

				OrderInformation orderInformationListCheck = new OrderInformation();

				orderInformationListCheck.setOrderNo(cusId);
				Boolean CustomerOrderNo = OrderInformationList.contains(orderInformationListCheck);

				if (CustomerOrderNo == Boolean.TRUE) {

					int index = OrderInformationList.indexOf(orderInformationListCheck);
					OrderInformation orderInformationNewToSetMAp = OrderInformationList.get(index);
					log.info("----------- Before removing the size is " + OrderInformationList.size());
					OrderInformationList.remove(index);

					log.info("----------- After removing the size is " + OrderInformationList.size());
					orderInformationNewToSetMAp.setItemListBeanDetail(itemDetailList);

					OrderInformationList.add(orderInformationNewToSetMAp);
					log.info("----------- After setting the map  " + OrderInformationList.size());
				}

			}

			/// -------------------------------- Iteetaring the
			/// OrderinfomationList-----------------

			log.info("++++++++++++++ORDER LIST++++++++++++++");
			log.info("****Size of the Order Header List : " + OrderInformationList.size());

//            for (OrderInformation orderinformation : OrderInformationList) {
//            
//                log.info("******Customer Number : " +orderinformation.getOrderNo());
//                log.info("*****Store Id : " +orderinformation.getStoreId());
//                log.info("******Carrier : " +orderinformation.getCarrier());
//              
//                for(ItemListBean itemListBean :orderinformation.getItemListBeanDetail()) 
//                {
//                    log.info("****Size of the Item Level List " +orderinformation.getItemListBeanDetail().size());
//                    log.info("*****Item id : " +itemListBean.getItem());
//                    log.info("*****Quantity : " +itemListBean.getQuantity());
//                    log.info("******Unit Retail : " +itemListBean.getUnitCostValue());
//                    
//                }
//            }
		} catch (Exception e) {
			log.info("Exception occured while connecting to data base : " + e.getMessage());
			log.error(e.getMessage());
		} finally {
			try {
				OMSUtil.closeDBConnection(conn, preparedStatement, rs);
			} catch (Exception e) {
				log.error(e.getMessage());
				// throw new SOAPException(e.getMessage());
			}
		}
		log.info("Returning from SIMDetailBeanList : " + OrderInformationList.size());
		return OrderInformationList;
	} // End of method processReadyToShipDetails

	// Code for Undelivered Order
	public ArrayList<OrderInformation> processUndeliveredDetails(Long NoOfOrders) {
		SIMDetailBean sIMDetailBean = null;
		List<SIMDetailBean> sIMDetailBeanList = new ArrayList<SIMDetailBean>();
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		String customerOrderId;
		Map<String, ArrayList<ItemListBean>> map = new HashMap<String, ArrayList<ItemListBean>>();
		ArrayList<ItemListBean> itemBean = null;
		String query2 = null;

		OrderInformation orderInformation = new OrderInformation();
		ArrayList<OrderInformation> OrderInformationList = new ArrayList<OrderInformation>();

		String query = "select * from SIM_DEL_DETAILS where rownum <=?";
		log.info(query);

		try {
			log.info("connecting to SIM Schema");
			log.info("OMSConstants.DS_SIM_STRING " + OMSConstants.DS_SIM_STRING);
			conn = OMSUtil.createDBConnection(OMSConstants.DS_SIM_STRING);
			log.info("conn : " + conn);
			preparedStatement = conn.prepareStatement(query);
			preparedStatement.setLong(1, NoOfOrders);

			log.info("No of order valuess:" + NoOfOrders);
			log.info("No of order value:" + NoOfOrders);
			log.info("before preparedStatement");

			log.info("after prepared Statement");
			rs = preparedStatement.executeQuery();

			log.info("after executing the query" + rs.isBeforeFirst());

			if (rs.isBeforeFirst()) {
				log.info("in between if and while");
				while (rs.next()) {
					log.info("Inside while loop of database");
					sIMDetailBean = new SIMDetailBean();
					ItemListBean itemListBean = new ItemListBean();

					orderInformation = new OrderInformation();
					orderInformation.setCarrier(rs.getString(11));
					orderInformation.setIsCod(null);

					orderInformation.setOrderValue(null);
					orderInformation.setShipmentId(rs.getString(12));
					orderInformation.setStoreId(rs.getInt(1));
					orderInformation.setOrderNo(rs.getString(4));
					orderInformation.setItemListBeanDetail(null);

					if (OrderInformationList.size() == 0) {
						OrderInformationList.add(orderInformation);

					} else {

						Boolean orderInformationcheck = OrderInformationList.contains(orderInformation);
						if (Boolean.FALSE == orderInformationcheck) {
							OrderInformationList.add(orderInformation);
						}
					}
					log.info("Size of OrderInformationList :" + OrderInformationList.size());
					customerOrderId = itemListBean.getCustomerOrderId();
					itemListBean.setCustomerOrderId(rs.getString(4));
					log.info("Customer Order Number : " + rs.getString(4));
					itemListBean.setLineNo(rs.getBigDecimal(5));
					log.info("Line No : " + rs.getString(5));
					itemListBean.setItem(rs.getString(6));
					log.info("Item : " + rs.getString(6));
					itemListBean.setQuantity(rs.getInt(7));
					log.info("Quantity : " + rs.getString(7));
					itemListBean.setDeliveryId(rs.getInt(2));
					log.info("Delivery Id : " + rs.getInt(2));

					itemListBean.setDescription(rs.getString(11));
					log.info("Description : " + rs.getString(11));
					itemListBean.setUnitCostValue(rs.getBigDecimal(8));
					log.info("Unit Cost : " + rs.getBigDecimal(8));
					itemListBean.setUnitCostCurrency(rs.getString(9));
					log.info("Unit Currency :" + rs.getString(9));
					if (map.get(itemListBean.getCustomerOrderId()) == null || map.get(itemListBean.getCustomerOrderId()).size() == 0) {
						itemBean = new ArrayList<ItemListBean>();
						itemBean.add(itemListBean);
						log.info("Customer id ----" + rs.getString(3));
						map.put(rs.getString(4), itemBean);
					} else {
						ArrayList<ItemListBean> existingList = map.get(itemListBean.getCustomerOrderId());
						existingList.add(itemListBean);
						log.info("Customer id ----" + rs.getString(3));
						map.put(rs.getString(4), existingList);
					}
					log.info("Map size " + map.size());
					//// sIMDetailBean.setItemListBeanMap(map);
				}
			}
			log.info("Size of OrderInformationList :" + OrderInformationList.size());
			log.info("Iterate the map : " + map.size());
			for (String cusId : map.keySet()) {
				log.info("Customer Order No : " + cusId);
				ArrayList<ItemListBean> itemDetailList = map.get(cusId);

				OrderInformation orderInformationListCheck = new OrderInformation();

				orderInformationListCheck.setOrderNo(cusId);
				Boolean CustomerOrderNo = OrderInformationList.contains(orderInformationListCheck);

				if (CustomerOrderNo == Boolean.TRUE) {

					int index = OrderInformationList.indexOf(orderInformationListCheck);
					OrderInformation orderInformationNewToSetMAp = OrderInformationList.get(index);
					log.info("----------- Before removing the size is " + OrderInformationList.size());
					OrderInformationList.remove(index);

					log.info("----------- After removing the size is " + OrderInformationList.size());
					orderInformationNewToSetMAp.setItemListBeanDetail(itemDetailList);

					OrderInformationList.add(orderInformationNewToSetMAp);
					log.info("----------- After setting the map  " + OrderInformationList.size());
				}

			}

			log.info("++++++++++++++ORDER LIST++++++++++++++");
			log.info("****Size of the Order Header List : " + OrderInformationList.size());

//            for (OrderInformation orderinformation : OrderInformationList) {
//            
//                log.info("******Customer Number : " +orderinformation.getOrderNo());
//                log.info("*****Store Id : " +orderinformation.getStoreId());
//                log.info("******Carrier : " +orderinformation.getCarrier());
//                
//                for(ItemListBean itemListBean :orderinformation.getItemListBeanDetail()) 
//                {
//                    log.info("****Size of the Item Level List " +orderinformation.getItemListBeanDetail().size());
//                    log.info("*****Item id : " +itemListBean.getItem());
//                    log.info("*****Quantity : " +itemListBean.getQuantity());
//                    log.info("******Unit Retail : " +itemListBean.getUnitCostValue());
//                }
//            }
		} catch (Exception e) {
			log.info("Exception occured while connecting to data base : " + e.getMessage());
			log.error(e.getMessage());
		} finally {
			try {
				OMSUtil.closeDBConnection(conn, preparedStatement, rs);
			} catch (Exception e) {
				log.error(e.getMessage());
				// throw new SOAPException(e.getMessage());
			}
		}
		log.info("Returning from SIMDetailBeanList : " + sIMDetailBeanList.size());
		return OrderInformationList;
	}

} // End of class SIMDeliveryDetailCommon
