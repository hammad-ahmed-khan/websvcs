package com.logicinfo.transfer.receive.model;

import org.w3c.dom.Document;

public class Response {

	private Document doc;
	private String res;

	private Items item;

	public Document getDoc() {
		return doc;
	}

	public void setDoc(Document doc) {
		this.doc = doc;
	}

	public String getRes() {
		return res;
	}

	public void setRes(String res) {
		this.res = res;
	}

	public Items getItem() {
		return item;
	}

	public void setItem(Items item) {
		this.item = item;
	}
}
