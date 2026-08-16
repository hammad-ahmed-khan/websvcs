package org.logicinfo.hybriscancellation.service;

import java.net.MalformedURLException;
import java.sql.SQLException;

import org.logicinfo.hybriscancellation.model.HybrisCancellationMainModel;
import org.logicinfo.hybriscancellation.model.HybrisCancellationResModel;

public interface HybrisCancellationService {

	HybrisCancellationResModel checkvalidation(HybrisCancellationMainModel request, HybrisCancellationResModel res)
			throws SQLException;

	HybrisCancellationResModel processingRefund(HybrisCancellationMainModel request) throws SQLException;

	HybrisCancellationResModel processingCencellation(HybrisCancellationMainModel request,
			HybrisCancellationResModel res) throws SQLException;

	HybrisCancellationResModel checkRefundValue(HybrisCancellationMainModel request) throws SQLException;

	HybrisCancellationResModel insertRequestInHybrisTables(HybrisCancellationMainModel request) throws SQLException;

	void updateHybrisHeadTableErrorMessage(HybrisCancellationMainModel request, HybrisCancellationResModel res)
			throws SQLException;

	long generateOmsCancelId() throws SQLException;
}
