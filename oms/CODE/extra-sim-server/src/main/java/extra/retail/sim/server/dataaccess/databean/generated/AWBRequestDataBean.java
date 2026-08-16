package extra.retail.sim.server.dataaccess.databean.generated;

import java.sql.ResultSet;
import java.sql.SQLException;

import oracle.retail.sim.server.dataaccess.SelectDataBean;

/**
 * AWBRequestDataBean.java
 * aibrahim
 * 2023
 */
public class AWBRequestDataBean extends SelectDataBean<AWBRequestDataBean> {

	public static final String SELECT_SQL = getSelectSqlText();

	public static final String TABLE_NAME = "XX_AWB_DETAILS AWB";

	private Long storeId;

	private String awbNumber;

	private Long pickId;

	private Long fullfilmentOrdId;

	private Long deliveryId;

	private Long pickLineItemId;

	private String item;

	private Integer qty;

	private String cartonNumber;

	private String awbStatus;

	private String handOverInd;

	public Long getStoreId() {
		return storeId;
	}

	public String getAwbNumber() {
		return awbNumber;
	}

	public Long getPickId() {
		return pickId;
	}

	public Long getFullfilmentOrdId() {
		return fullfilmentOrdId;
	}

	public Long getDeliveryId() {
		return deliveryId;
	}

	public Long getPickLineItemId() {
		return pickLineItemId;
	}

	public String getItem() {
		return item;
	}

	public Integer getQty() {
		return qty;
	}

	public String getCartonNumber() {
		return cartonNumber;
	}

	public String getAwbStatus() {
		return awbStatus;
	}

	public String getHandOverInd() {
		return handOverInd;
	}

	private static String getSelectSqlText() {
		return "SELECT AWB.STORE, AWB.AWB_NBR, AWB.PICK_ID, AWB.FUL_ORD_ID, AWB.DELIVERY_ID, AWB.PICK_LINE_ITEM_ID, AWB.ITEM, AWB.QTY, AWB.CARTON_ID, AWB.AWB_STATUS, AWB.HAND_OVER_IND FROM XX_AWB_DETAILS AWB ";
	}

	@Override
	public String getSelectSql() {
		return SELECT_SQL;
	}

	@Override
	public AWBRequestDataBean read(ResultSet rs) throws SQLException {
		AWBRequestDataBean bean = new AWBRequestDataBean();
		bean.awbNumber = getString(rs, "AWB_NBR");
		bean.awbStatus = getString(rs, "AWB_STATUS");
		bean.cartonNumber = getString(rs, "CARTON_ID");
		bean.deliveryId = getLong(rs, "DELIVERY_ID");
		bean.fullfilmentOrdId = getLong(rs, "FUL_ORD_ID");
		bean.handOverInd = getString(rs, "HAND_OVER_IND");
		bean.item = getString(rs, "ITEM");
		bean.pickId = getLong(rs, "PICK_ID");
		bean.pickLineItemId = getLong(rs, "PICK_LINE_ITEM_ID");
		bean.qty = getInteger(rs, "QTY");
		bean.storeId = getLong(rs, "STORE");
		return bean;
	}
}
