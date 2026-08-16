package extra.retail.sim.server.dataaccess.databean.generated;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import oracle.retail.sim.server.dataaccess.FullDataBean;

/**
 * ItemBinLocationDataBean.java aibrahim 2024
 */
public class ItemBinLocationDataBean extends FullDataBean<ItemBinLocationDataBean> {

	public static final String TABLE_NAME = "XX_SIM_ITEM_BIN_LOC";

	public static final String SELECT_SQL = getSelectSqlText();

	public static final String INSERT_SQL = getInsertSqlText();

	public static final String UPDATE_SQL = getUpdateSqlText();

	public static final String DELETE_SQL = getDeleteSqlText();

	private Long storeId;

	private String item;

	private String bin;

	@Override
	public String getDeleteSql() {
		return DELETE_SQL;
	}

	@Override
	public String getInsertSql() {
		return INSERT_SQL;
	}

	@Override
	public String getUpdateSql() {
		return UPDATE_SQL;
	}

	@Override
	public List<Object> toList(boolean insert) {
		List<Object> arrayList = new ArrayList<>();
		addToList(arrayList, this.storeId, Types.NUMERIC);
		addToList(arrayList, this.item, Types.VARCHAR);
		addToList(arrayList, this.bin, Types.VARCHAR);
		return arrayList;
	}

	@Override
	public String getSelectSql() {
		return SELECT_SQL;
	}

	@Override
	public ItemBinLocationDataBean read(ResultSet resultSet) throws SQLException {
		ItemBinLocationDataBean bean = new ItemBinLocationDataBean();
		bean.storeId = getLong(resultSet, "STORE_ID");
		bean.item = getString(resultSet, "ITEM");
		bean.bin = getString(resultSet, "BIN");
		return bean;
	}

	private static String getSelectSqlText() {
		return "SELECT BIN FROM XX_SIM_ITEM_BIN_LOC ";
	}

	private static String getInsertSqlText() {
		return "INSERT INTO XX_SIM_ITEM_BIN_LOC (STORE_ID, ITEM, BIN, CREATE_DATE, LAST_UPDATE_DATETIME) values (?, ?, ?, SYSDATE, SYSDATE)";
	}

	private static String getUpdateSqlText() {
		return "UPDATE XX_SIM_ITEM_BIN_LOC SET BIN = ?, LAST_UPDATE_DATETIME = SYSDATE ";
	}

	private static String getDeleteSqlText() {
		return "DELETE FROM XX_SIM_ITEM_BIN_LOC ";
	}

	public Long getStoreId() {
		return storeId;
	}

	public String getItem() {
		return item;
	}

	public String getBin() {
		return bin;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public void setBin(String bin) {
		this.bin = bin;
	}
}
