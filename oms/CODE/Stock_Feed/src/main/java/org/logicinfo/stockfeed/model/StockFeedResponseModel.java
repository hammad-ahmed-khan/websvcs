package org.logicinfo.stockfeed.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class StockFeedResponseModel {

	@JsonInclude(value = Include.NON_NULL)
	private String status;

	@JsonInclude(value = Include.NON_NULL)
	private String error;

	@JsonInclude(value = Include.NON_NULL)
	private List<StockFeed> feed;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public List<StockFeed> getFeed() {
		return feed;
	}

	public void setFeed(List<StockFeed> feed) {
		this.feed = feed;
	}
}
