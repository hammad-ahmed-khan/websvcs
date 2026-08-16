package org.logicinfo.hybriscancellation.model;

import java.math.BigDecimal;

public class CoCancellationItemModel {

	protected String item;
	protected BigDecimal cancelQtySuom;
	protected long lineNo;
	protected String messageCode;
	protected String messageDesc;

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public BigDecimal getCancelQtySuom() {
		return cancelQtySuom;
	}

	public void setCancelQtySuom(BigDecimal cancelQtySuom) {
		this.cancelQtySuom = cancelQtySuom;
	}

	public long getLineNo() {
		return lineNo;
	}

	public void setLineNo(long lineNo) {
		this.lineNo = lineNo;
	}

	public String getMessageCode() {
		return messageCode;
	}

	public void setMessageCode(String messageCode) {
		this.messageCode = messageCode;
	}

	public String getMessageDesc() {
		return messageDesc;
	}

	public void setMessageDesc(String messageDesc) {
		this.messageDesc = messageDesc;
	}

}
