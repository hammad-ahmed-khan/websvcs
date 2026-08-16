package com.extra.oms.spareParts.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TransferCreationResponse {

	 @JsonProperty("tsf_cre_seq_id")
	    protected String tsfCreSeqId;

	    @JsonProperty("success")
	    protected String success;

	    @JsonProperty("tsf_no")
	    protected Long tsfNo;

	    @JsonProperty("message")
	    protected String message;

		public String getTsfCreSeqId() {
			return tsfCreSeqId;
		}

		public void setTsfCreSeqId(String tsfCreSeqId) {
			this.tsfCreSeqId = tsfCreSeqId;
		}

		public String getSuccess() {
			return success;
		}

		public void setSuccess(String success) {
			this.success = success;
		}

		public Long getTsfNo() {
			return tsfNo;
		}

		public void setTsfNo(Long tsfNo) {
			this.tsfNo = tsfNo;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}
	


}
