package extra.retail.sim.server.dataaccess.databean.generated;

import java.sql.ResultSet;
import java.sql.SQLException;

import oracle.retail.sim.server.dataaccess.SelectDataBean;

/**
 * aibrahim
 * 2023
 */
public class ShipmentOrderPrintDataBean extends SelectDataBean<ShipmentOrderPrintDataBean> {

	public static final String SELECT_SQL = getSelectSqlText();

	private static String getSelectSqlText() {
		return "SELECT PRINTER_QUEUE, LABEL_FILE FROM XX_SIM_AWB_PATH WHERE STORE = ? AND AWB_NBR = ? ";
	}

	private String queue;

	private String labelPath;

	@Override
	public String getSelectSql() {
		return SELECT_SQL;
	}

	@Override
	public ShipmentOrderPrintDataBean read(ResultSet resultSet) throws SQLException {
		ShipmentOrderPrintDataBean bean = new ShipmentOrderPrintDataBean();
		bean.queue = getString(resultSet, "PRINTER_QUEUE");
		bean.labelPath = getString(resultSet, "LABEL_FILE");
		return bean;
	}

	public String getQueue() {
		return queue;
	}

	public String getLabelPath() {
		return labelPath;
	}
}
