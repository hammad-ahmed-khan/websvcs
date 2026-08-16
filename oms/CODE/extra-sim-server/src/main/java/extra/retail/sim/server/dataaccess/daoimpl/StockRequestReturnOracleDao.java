package extra.retail.sim.server.dataaccess.daoimpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.core.SimServerException;
import oracle.retail.sim.common.core.UniversalContext;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.store.Store;
import oracle.retail.sim.server.dataaccess.BaseOracleDao;
import oracle.retail.sim.server.dataaccess.BatchParametricStatement;
import oracle.retail.sim.server.dataaccess.databean.generated.StoreDataBean;

import extra.retail.sim.common.spareparts.StockRequestReportQueryFilter;
import extra.retail.sim.common.spareparts.StockRequestReturnDetailVO;
import extra.retail.sim.common.spareparts.StockRequestReturnVO;
import extra.retail.sim.server.dataaccess.dao.StockRequestReturnDao;
import extra.retail.sim.server.dataaccess.databean.generated.StockRequestReturnDataBean;
import extra.retail.sim.server.dataaccess.databean.generated.StockRquestReportDatabean;

/**
 * StockRequestReturnOracleDao.java
 * aibrahim
 * 2024
 */
public class StockRequestReturnOracleDao extends BaseOracleDao implements StockRequestReturnDao {

	@Override
	public List<StockRequestReturnVO> getPendingRequestReturn(String userName, String requestType, Long storeId) throws SimServerException {
		List<Object> paramList = new ArrayList<>();
		StringBuilder queryBuilder = new StringBuilder(StockRequestReturnDataBean.SELECT_SQL);
		addWhere(queryBuilder, "TYPE", requestType, paramList);
		addAnd(queryBuilder, "REQ_LOC", storeId, paramList);
		List<StockRequestReturnDataBean> list = query(new StockRequestReturnDataBean(), queryBuilder.toString(), paramList);
		if (list.isEmpty()) {
			return Collections.emptyList();
		}
		Map<Long, StockRequestReturnVO> binList = new HashMap<>();
		for (StockRequestReturnDataBean bean : list) {
			StockRequestReturnVO requestReturnVO = binList.get(bean.getSequenceId());
			if (requestReturnVO == null) {
				requestReturnVO = fromBeanToValueStockHeader(bean);
				binList.put(bean.getSequenceId(), requestReturnVO);
			}
			requestReturnVO.getDetails().add(fromBeanToValueStockDetail(bean));
		}
		return Collections.unmodifiableList(new ArrayList<>(binList.values()));
	}

	private StockRequestReturnVO fromBeanToValueStockHeader(StockRequestReturnDataBean bean) throws SimServerException {
		StockRequestReturnVO approvalVO = new StockRequestReturnVO();
		approvalVO.setRequestLoc(bean.getRequestLoc());
		approvalVO.setTsfSequenceNo(bean.getSequenceNo());
		approvalVO.setSerialNo(bean.getSerialNo());
		approvalVO.setTechnicianId(bean.getTechnicianId());
		approvalVO.setSequenceId(bean.getSequenceId());
		approvalVO.setRequestDate(bean.getRequestDate());
		approvalVO.setLastDate(bean.getLastDate());
		approvalVO.setComments(bean.getComments());
		return approvalVO;
	}

	private StockRequestReturnDetailVO fromBeanToValueStockDetail(StockRequestReturnDataBean bean) throws SimServerException {
		StockRequestReturnDetailVO detailVO = new StockRequestReturnDetailVO();
		detailVO.setItem(bean.getItem());
		detailVO.setQty(bean.getQty());
		detailVO.setItemDescription(bean.getItemDescription());
		detailVO.setBrandName(bean.getBrandName());
		detailVO.setAvailable(bean.getAvailable());
		detailVO.setTechSubBucket(bean.getTechSubBucket());
		detailVO.setBin(bean.getBin());
		return detailVO;
	}

	@Override
	public List<Store> getUserApprovalLocations(String userName) throws SimServerException {
		String str = StoreDataBean.SELECT_SQL + " WHERE " + StoreDataBean.COL_ID + " IN ( SELECT LOCATION FROM XX_SP_INV_LOC_MAP WHERE INV_COORD_ID = ?)";
		List<String> list = Collections.singletonList(userName);
		List<StoreDataBean> storeBeans = query(new StoreDataBean(), str, list);
		return !storeBeans.isEmpty() ? fromBeansToStores(storeBeans) : Collections.<Store>emptyList();
	}

	private List<Store> fromBeansToStores(List<StoreDataBean> storeBeans) {
		List<Store> arrayList = new ArrayList<>();
		for (StoreDataBean storeDataBean : storeBeans) {
			arrayList.add(fromBeanToStore(storeDataBean));
		}
		return arrayList;
	}

	private Store fromBeanToStore(StoreDataBean paramStoreDataBean) {
		Store store = BOFactory.createStore(paramStoreDataBean.getId());
		store.doSetName(paramStoreDataBean.getName());
		store.doSetLanguage(paramStoreDataBean.getLocaleLanguage());
		store.doSetCountry(paramStoreDataBean.getLocaleCountry());
		store.doSetCurrencyCode(paramStoreDataBean.getCurrencyCode());
		store.doSetTransferZone(paramStoreDataBean.getTransferZoneId());
		store.doSetSimFlag(Boolean.valueOf(ynStringToBoolean(StringHelper.trim(paramStoreDataBean.getSimStore()))));
		if (paramStoreDataBean.getTimezone() != null) {
			store.doSetTimezone(TimeZone.getTimeZone(paramStoreDataBean.getTimezone()));
		}
		return store;
	}

	@Override
	public void rejectTransfer(List<StockRequestReturnVO> selectedVOs) throws SimServerException {
		BatchParametricStatement batchParametricStatement = new BatchParametricStatement(StockRequestReturnDataBean.UPDATE_SQL);
		for (StockRequestReturnVO approvalVO : selectedVOs) {
			batchParametricStatement.addParams(fromObjectToBean(approvalVO, "R").toList(false));
		}
		executeBatch(batchParametricStatement);
	}

	@Override
	public void approveTransfer(List<StockRequestReturnVO> selectedVOs) throws SimServerException {
		BatchParametricStatement batchParametricStatement = new BatchParametricStatement(StockRequestReturnDataBean.UPDATE_SQL);
		for (StockRequestReturnVO approvalVO : selectedVOs) {
			batchParametricStatement.addParams(fromObjectToBean(approvalVO, "P").toList(false));
		}
		executeBatch(batchParametricStatement);
	}

	private StockRequestReturnDataBean fromObjectToBean(StockRequestReturnVO approvalVO, String status) {
		StockRequestReturnDataBean bean = new StockRequestReturnDataBean();
		bean.setSequenceNo(approvalVO.getTsfSequenceNo());
		bean.setStatus(status);
		bean.setComments(approvalVO.getComments());
		bean.setUpdatedBy(UniversalContext.getUserName());
		return bean;
	}

	@Override
	public Integer getTechBucketCode(String technician) throws SimServerException {
		try {
			return queryForLong("SELECT TECH_TO_AVAIL FROM XX_TECH_REAS_CODE_V WHERE DESCRIPTION = ? ", Collections.singletonList(technician)).intValue();
		} catch (Exception e) {
			throw new SimServerException("Technician bucket code is missing for the user " + technician, e);
		}
	}

	private StockRequestReturnVO fromBeanToValueStockHeaderReport(StockRquestReportDatabean bean) throws SimServerException {
		StockRequestReturnVO approvalVO = new StockRequestReturnVO();
		approvalVO.setRequestLoc(bean.getRequestLoc());
		approvalVO.setTsfSequenceNo(bean.getSequenceNo());
		approvalVO.setSerialNo(bean.getSerialNo());
		approvalVO.setTechnicianId(bean.getTechnicianId());
		approvalVO.setSequenceId(bean.getSequenceId());
		approvalVO.setRequestDate(bean.getRequestDate());
		approvalVO.setLastDate(bean.getAppRejTime());
		approvalVO.setComments(bean.getComments());
		approvalVO.setStatus(bean.getStatus());
		approvalVO.setRequestType(bean.getRequestType());
		return approvalVO;
	}

	private StockRequestReturnDetailVO fromBeanToValueStockDetailReport(StockRquestReportDatabean bean) throws SimServerException {
		StockRequestReturnDetailVO detailVO = new StockRequestReturnDetailVO();
		detailVO.setItem(bean.getItem());
		detailVO.setQty(bean.getQty());
		detailVO.setItemDescription(bean.getItemDescription());
		detailVO.setBrandName(bean.getBrandName());
		detailVO.setBin(bean.getBin());
		return detailVO;
	}

	@Override
	public List<StockRequestReturnVO> selectStockRequestReport(long storeId, StockRequestReportQueryFilter stockRequestReportQueryFilter) throws SimServerException {
	    List<Object> paramList = new ArrayList<>();
	    StringBuilder queryBuilder = new StringBuilder(StockRquestReportDatabean.SELECT_REPORT_SQL);
	    queryBuilder.append(" WHERE REQ_LOC = ? ");
	    paramList.add(storeId);


	    addConditions(queryBuilder, paramList, stockRequestReportQueryFilter);

	    queryBuilder.append(" AND ROWNUM <= ? ");
	    paramList.add(stockRequestReportQueryFilter.getSearchLimit());

	    List<StockRquestReportDatabean> list = query(new StockRquestReportDatabean(), queryBuilder.toString(), paramList);
	    if (list.isEmpty()) {
	        return Collections.emptyList();
	    }

	    Map<Long, StockRequestReturnVO> binList = new HashMap<>();
	    for (StockRquestReportDatabean bean : list) {
	        StockRequestReturnVO requestReportVO = binList.get(bean.getSequenceId());
	        if (requestReportVO == null) {
	            requestReportVO = fromBeanToValueStockHeaderReport(bean);
	            binList.put(bean.getSequenceId(), requestReportVO);
	        }
	        requestReportVO.getDetails().add(fromBeanToValueStockDetailReport(bean));
	    }
	    return Collections.unmodifiableList(new ArrayList<>(binList.values()));
	}
	
	private void addConditions(StringBuilder queryBuilder, List<Object> paramList, StockRequestReportQueryFilter filter) {
	    if (filter.getFromDate() != null && filter.getToDate() != null) {
	        queryBuilder.append(" AND APPR_REJ_TIME BETWEEN ? AND ? ");
	        paramList.add(filter.getFromDate());
	        paramList.add(filter.getToDate());
	    } else if (filter.getFromDate() != null) {
	        queryBuilder.append(" AND APPR_REJ_TIME >= ? ");
	        paramList.add(filter.getFromDate());
	    } else if (filter.getToDate() != null) {
	        queryBuilder.append(" AND APPR_REJ_TIME <= ? ");
	        paramList.add(filter.getToDate());
	    }

	    if (filter.getSerialNo() != null && !filter.getSerialNo().isEmpty()) {
	        queryBuilder.append(" AND SRV_REQ_ID = ? ");
	        paramList.add(filter.getSerialNo());
	    }
	    if (filter.getRequestType() != null && !filter.getRequestType().isEmpty()) {
	        queryBuilder.append(" AND TYPE = ? ");
	        paramList.add(filter.getRequestType());
	    }
	    if (filter.getTechnicianId() != null && !filter.getTechnicianId().isEmpty()) {
	        queryBuilder.append(" AND REQ_TECH_ID = ? ");
	        paramList.add(filter.getTechnicianId());
	    }
	    if (filter.getItemId() != null && !filter.getItemId().isEmpty()) {
	        queryBuilder.append(" AND ITEM = ? ");
	        paramList.add(filter.getItemId());
	    }
	    if (filter.getStatus() != null && !filter.getStatus().isEmpty()) {
	        queryBuilder.append(" AND STATUS = ? ");
	        paramList.add(filter.getStatus());
	    }
	}
}
