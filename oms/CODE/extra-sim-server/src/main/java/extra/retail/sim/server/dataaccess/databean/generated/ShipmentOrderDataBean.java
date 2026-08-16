package extra.retail.sim.server.dataaccess.databean.generated;

import java.sql.ResultSet;
import java.sql.SQLException;

import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.server.dataaccess.SelectDataBean;

/**
 * aibrahim
 * 2023
 */
public class ShipmentOrderDataBean extends SelectDataBean<ShipmentOrderDataBean> {

	public static final String SELECT_SQL = getSelectSqlText();

	private Long fulfillmentOrderLineItemId;

	private String itemId;

	private Quantity pendingQuantity;

	private static String getSelectSqlText() {
		return "SELECT FOL.ID, FOL.ITEM_ID, FOL.QUANTITY_PICKED - NVL(FDL.QUANTITY, 0) PENDING_QUANTITY FROM FUL_ORD_LINE_ITEM FOL, (SELECT DL.FUL_ORD_LINE_ITEM_ID, SUM(DL.QUANTITY) QUANTITY FROM FUL_ORD_DLV_LINE_ITEM DL, FUL_ORD_DLV D WHERE D.FUL_ORD_ID = 225045 AND D.ID = DL.FUL_ORD_DLV_ID GROUP BY DL.FUL_ORD_LINE_ITEM_ID) FDL";
	}

	@Override
	public String getSelectSql() {
		return SELECT_SQL;
	}

	@Override
	public ShipmentOrderDataBean read(ResultSet resultSet) throws SQLException {
		ShipmentOrderDataBean bean = new ShipmentOrderDataBean();
		bean.fulfillmentOrderLineItemId = getLong(resultSet, "ID");
		bean.itemId = getString(resultSet, "ITEM_ID");
		bean.pendingQuantity = new Quantity(getDouble(resultSet, "PENDING_QUANTITY"));
		return bean;
	}

	public Long getFulfillmentOrderLineItemId() {
		return fulfillmentOrderLineItemId;
	}

	public String getItemId() {
		return itemId;
	}

	public Quantity getPendingQuantity() {
		return pendingQuantity;
	}

	public void setFulfillmentOrderLineItemId(Long fulfillmentOrderLineItemId) {
		this.fulfillmentOrderLineItemId = fulfillmentOrderLineItemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public void setPendingQuantity(Quantity pendingQuantity) {
		this.pendingQuantity = pendingQuantity;
	}
}
