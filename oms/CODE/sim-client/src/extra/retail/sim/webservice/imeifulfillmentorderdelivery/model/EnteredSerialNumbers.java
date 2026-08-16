package extra.retail.sim.webservice.imeifulfillmentorderdelivery.model;

import java.util.List;

public class EnteredSerialNumbers {
	private int code;
	private boolean success;
	private String message;
	private boolean isDeliveryExists;
	private List<String> serialNumbers;

	public boolean isDeliveryExists() {
		return isDeliveryExists;
	}

	public void setDeliveryExists(boolean isDeliveryExists) {
		this.isDeliveryExists = isDeliveryExists;
	}

	public List<String> getSerialNumbers() {
		return serialNumbers;
	}

	public void setSerialNumbers(List<String> serialNumbers) {
		this.serialNumbers = serialNumbers;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	

}
