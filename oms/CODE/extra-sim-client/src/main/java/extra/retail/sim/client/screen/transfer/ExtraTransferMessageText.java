package extra.retail.sim.client.screen.transfer;

import oracle.retail.sim.common.business.MessageText;

/**
 * ExtraTransferMessageText.java
 * aibrahim
 * 2024
 */
public enum ExtraTransferMessageText implements MessageText {
	
	RESTRICTED_STORE("You cannot transfer to this store.");

	ExtraTransferMessageText(String message) {
		this.message = message;
	}

	private final String message;

	@Override
	public String getCode() {
		return name();
	}

	@Override
	public String getText() {
		return this.message;
	}
}
