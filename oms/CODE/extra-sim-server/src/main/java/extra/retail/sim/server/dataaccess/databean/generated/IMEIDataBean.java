package extra.retail.sim.server.dataaccess.databean.generated;

import java.sql.ResultSet;
import java.sql.SQLException;

import oracle.retail.sim.server.dataaccess.SelectDataBean;

/**
 * IMEIDataBean.java
 * aibrahim
 * 2023
 */
public class IMEIDataBean extends SelectDataBean<IMEIDataBean> {

	public static final String SELECT_SQL = getSelectSqlText();

	public static final String TABLE_NAME = "XX_FUL_ORD_DLV_LINE_ITEM_UIN UIN";

	private Long deliveryId;

	private Long deliveryLineItemId;

	private String itemId;

	private Integer qty;

	private String imei;

	@Override
	public String getSelectSql() {
		return SELECT_SQL;
	}

	private static String getSelectSqlText() {
		return "SELECT UIN.FUL_ORD_DLV_ID, UIN.FUL_ORD_LINE_ITEM_ID, UIN.ITEM_ID, UIN.QUANTITY, UIN.IMEI_NUMBER FROM XX_FUL_ORD_DLV_LINE_ITEM_UIN UIN";
	}

	@Override
	public IMEIDataBean read(ResultSet rs) throws SQLException {
		IMEIDataBean bean = new IMEIDataBean();
		bean.deliveryId = getLong(rs, "FUL_ORD_DLV_ID");
		bean.deliveryLineItemId = getLong(rs, "FUL_ORD_LINE_ITEM_ID");
		bean.itemId = getString(rs, "ITEM_ID");
		bean.qty = getInteger(rs, "QUANTITY");
		bean.imei = getString(rs, "IMEI_NUMBER");
		return bean;
	}

	public Long getDeliveryId() {
		return deliveryId;
	}

	public String getItemId() {
		return itemId;
	}

	public Integer getQty() {
		return qty;
	}

	public String getImei() {
		return imei;
	}

	public Long getDeliveryLineItemId() {
		return deliveryLineItemId;
	}
}
