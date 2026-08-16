package extra.retail.sim.server.dataaccess.databean.generated;

import java.sql.ResultSet;
import java.sql.SQLException;

import oracle.retail.sim.server.dataaccess.SelectDataBean;

/**
 * ItemBinLocationDataBean.java aibrahim 2024
 */
public class StoreBinLocationDataBean extends SelectDataBean<StoreBinLocationDataBean> {

	public static final String TABLE_NAME = "XX_SIM_STORE_BIN_LOC";

	public static final String SELECT_SQL = getSelectSqlText();

	private Long storeId;

	private Integer bin;

	@Override
	public String getSelectSql() {
		return SELECT_SQL;
	}

	@Override
	public StoreBinLocationDataBean read(ResultSet resultSet) throws SQLException {
		StoreBinLocationDataBean bean = new StoreBinLocationDataBean();
		bean.storeId = getLong(resultSet, "STORE_ID");
		bean.bin = getInteger(resultSet, "BIN");
		return bean;
	}

	private static String getSelectSqlText() {
		return "SELECT BIN FROM XX_SIM_STORE_BIN_LOC ";
	}

	public Long getStoreId() {
		return storeId;
	}

	public Integer getBin() {
		return bin;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public void setBin(Integer bin) {
		this.bin = bin;
	}
}
