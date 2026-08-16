package com.extra.sim.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.extra.sim.model.BaseLV;

/**
 * @author aibrahim
 *
 */
@Repository
public class UtilDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public Map<String, List<BaseLV>> getBaseLVs(String listCode) {
		return jdbcTemplate.query("SELECT * FROM XX_SIM_BASL_LOV WHERE LIST_CODE IN ('" + StringUtils.collectionToDelimitedString(Arrays.asList(listCode.split(",")), "', '") + "')", new ResultSetExtractor<Map<String, List<BaseLV>>>() {

			@Override
			public Map<String, List<BaseLV>> extractData(ResultSet rs) throws SQLException, DataAccessException {
				Map<String, List<BaseLV>> baseLVMap = new HashMap<String, List<BaseLV>>();
				while (rs.next()) {
					String code = rs.getString(2);
					List<BaseLV> baseLVs = baseLVMap.get(code);
					if (baseLVs == null) {
						baseLVs = new ArrayList<BaseLV>();
						baseLVMap.put(code, baseLVs);
					}
					BaseLV baseLV = new BaseLV();
					baseLV.setId(rs.getLong(1));
					baseLV.setListCode(code);
					baseLV.setKey(rs.getString(3));
					baseLV.setValue(rs.getString(4));
					baseLVs.add(baseLV);
				}
				return baseLVMap;
			}
		});
	}
}
