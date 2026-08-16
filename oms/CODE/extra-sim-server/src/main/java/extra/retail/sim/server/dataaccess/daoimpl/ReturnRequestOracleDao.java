package extra.retail.sim.server.dataaccess.daoimpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oracle.retail.sim.common.core.SimServerException;
import oracle.retail.sim.common.core.UniversalContext;
import oracle.retail.sim.server.dataaccess.BaseOracleDao;
import oracle.retail.sim.server.dataaccess.BatchParametricStatement;

import extra.retail.sim.common.spareparts.ReturnApprovalDetailVO;
import extra.retail.sim.common.spareparts.ReturnApprovalVO;
import extra.retail.sim.common.spareparts.ReturnRequestItem;
import extra.retail.sim.server.dataaccess.dao.ReturnRequestDao;
import extra.retail.sim.server.dataaccess.databean.generated.ReturnRequestItemDataBean;

/**
 * ReturnRequestOracleDao.java
 * aibrahim
 * 2024
 */
public class ReturnRequestOracleDao extends BaseOracleDao implements ReturnRequestDao {

	@Override
	public void saveReturnRequest(List<ReturnRequestItem> requestItems) throws SimServerException {
		BatchParametricStatement batchParametricStatement = new BatchParametricStatement(ReturnRequestItemDataBean.INSERT_SQL);
		for (ReturnRequestItem requestItem : requestItems) {
			batchParametricStatement.addParams(fromObjectToBean(requestItem).toList(true));
		}
		executeBatch(batchParametricStatement);
	}

	private ReturnRequestItemDataBean fromObjectToBean(ReturnRequestItem requestItem) {
		ReturnRequestItemDataBean bean = new ReturnRequestItemDataBean();
		bean.setCreatedBy(UniversalContext.getUserName());
		bean.setItemId(requestItem.getItemId());
		bean.setQty(requestItem.getQty());
		bean.setReturnId(requestItem.getReturnId());
		bean.setSourceId(requestItem.getSourceId());
		bean.setStatus(requestItem.getStatus());
		bean.setStoreId(requestItem.getStoreId());
		bean.setUpdatedBy(UniversalContext.getUserName());
		return bean;
	}

	@Override
	public String getReturnRequestStatus(Long returnId) throws SimServerException {
		try {
			return queryForString("SELECT DISTINCT STATUS FROM XX_SP_RTV_REQ WHERE ID = ? ", Collections.singletonList(returnId));
		} catch (Exception e) {
			throw new SimServerException("Return request is not available for the return id " + returnId, e);
		}
	}

	@Override
	public List<ReturnApprovalVO> getPendingReturnRequest(Long storeId) throws SimServerException {
		List<Object> paramList = new ArrayList<>();
		StringBuilder queryBuilder = new StringBuilder(ReturnRequestItemDataBean.SELECT_SQL);
		queryBuilder.append(" AND STORE_ID = ?");
		paramList.add(storeId);
		List<ReturnRequestItemDataBean> list = query(new ReturnRequestItemDataBean(), queryBuilder.toString(), paramList);
		if (list.isEmpty()) {
			return Collections.emptyList();
		}
		Map<Long, ReturnApprovalVO> binList = new HashMap<>();
		for (ReturnRequestItemDataBean bean : list) {
			ReturnApprovalVO requestReturnVO = binList.get(bean.getReturnId());
			if (requestReturnVO == null) {
				requestReturnVO = fromBeanToValueStockHeader(bean);
				binList.put(bean.getReturnId(), requestReturnVO);
			}
			requestReturnVO.getReturnApprovalDetailVO().add(fromBeanToValueStockDetail(bean));
		}
		return Collections.unmodifiableList(new ArrayList<>(binList.values()));
	}

	private ReturnApprovalVO fromBeanToValueStockHeader(ReturnRequestItemDataBean bean) throws SimServerException {
		ReturnApprovalVO approvalVO = new ReturnApprovalVO();
		approvalVO.setReturnId(bean.getReturnId());
		approvalVO.setStoreId(bean.getStoreId());
		approvalVO.setSourceId(bean.getSourceId());
		approvalVO.setSupplierName(bean.getSupplierName());
		approvalVO.setCreateUser(bean.getCreatedBy());
		approvalVO.setCreateDate(bean.getCreatedDate());
		return approvalVO;
	}

	private ReturnApprovalDetailVO fromBeanToValueStockDetail(ReturnRequestItemDataBean bean) throws SimServerException {
		ReturnApprovalDetailVO detailVO = new ReturnApprovalDetailVO();
		detailVO.setReturnId(bean.getReturnId());
		detailVO.setItemId(bean.getItemId());
		detailVO.setItemDescription(bean.getItemDescription());
		detailVO.setQty(bean.getQty());
		detailVO.setBrandName(bean.getBrandName());
		return detailVO;
	}

	@Override
	public void rejectReturn(List<Long> returnIds) throws SimServerException {
		BatchParametricStatement batchParametricStatement = new BatchParametricStatement(ReturnRequestItemDataBean.UPDATE_SQL);
		for (Long returnId : returnIds) {
			batchParametricStatement.addParams(fromObjectToBean(returnId, "REJECTED").toList(false));
		}
		executeBatch(batchParametricStatement);
	}

	@Override
	public void approveReturn(List<Long> returnIds) throws SimServerException {
		BatchParametricStatement batchParametricStatement = new BatchParametricStatement(ReturnRequestItemDataBean.UPDATE_SQL);
		for (Long returnId : returnIds) {
			batchParametricStatement.addParams(fromObjectToBean(returnId, "APPROVED").toList(false));
		}
		executeBatch(batchParametricStatement);
	}

	private ReturnRequestItemDataBean fromObjectToBean(Long returnId, String status) {
		ReturnRequestItemDataBean bean = new ReturnRequestItemDataBean();
		bean.setReturnId(returnId);
		bean.setStatus(status);
		bean.setUpdatedBy(UniversalContext.getUserName());
		return bean;
	}
}
