package extra.retail.sim.server.dataaccess.databean.generated;

import java.sql.ResultSet;
import java.sql.SQLException;

import oracle.retail.sim.server.dataaccess.SelectDataBean;

/**
 * 
 */
public class BaseLVDataBean extends SelectDataBean<BaseLVDataBean> {

	public static final String SELECT_SQL = getSelectSqlText();

	private Long id;

	private String listCode;

	private String key;

	private String value;

	@Override
	public String getSelectSql() {
		return SELECT_SQL;
	}

	private static String getSelectSqlText() {
		return "SELECT ID, LIST_CODE, KEY, VALUE, STATUS FROM SIM_BASL_LOV";
	}

	@Override
	public BaseLVDataBean read(ResultSet resultSet) throws SQLException {
		BaseLVDataBean dataBean = new BaseLVDataBean();
		dataBean.id = getLong(resultSet, "ID");
		dataBean.key = getString(resultSet, "KEY");
		dataBean.value = getString(resultSet, "VALUE");
		dataBean.listCode = getString(resultSet, "LIST_CODE");
		return dataBean;
	}

	public Long getId() {
		return id;
	}

	public String getListCode() {
		return listCode;
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setListCode(String listCode) {
		this.listCode = listCode;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
