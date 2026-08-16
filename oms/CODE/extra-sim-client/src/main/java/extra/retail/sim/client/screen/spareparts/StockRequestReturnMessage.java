package extra.retail.sim.client.screen.spareparts;

import oracle.retail.sim.common.business.MessageText;

/**
 * TransferReturnApprovalMessage.java
 * aibrahim
 * 2024
 */
public enum StockRequestReturnMessage implements MessageText {

	SELECT_STORE("Select Store"), SELECT_APPROVAL_TYPE("Select Request Type"), SELECT_ITEMS("Select Items"), REQUEST_REJECT_NA("Request can't be rejected"), APPROVE_SUCCESS("Selected items approved successfully"), REJECT_SUCCESS("Selected items rejected successfully"),REPORT_DATE_ERROR("From Date' must be earlier than or equal to 'To Date")
	;

	private final String message;

	StockRequestReturnMessage(String paramString1) {
		this.message = paramString1;
	}

	@Override
	public String getCode() {
		return name();
	}

	@Override
	public String getText() {
		return this.message;
	}
}
