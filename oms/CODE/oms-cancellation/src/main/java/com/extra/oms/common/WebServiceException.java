/**
 * 
 */
package com.extra.oms.common;

/**
 * @author aibrahim
 *
 */
public class WebServiceException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4812918078055065254L;

	private String wsSoapXML;

	public WebServiceException(String wsSoapXML) {
		super();
		this.wsSoapXML = wsSoapXML;
	}

	public WebServiceException(String wsSoapXML, Throwable t) {
		super(t);
		this.wsSoapXML = wsSoapXML;
	}

	public String getWsSoapXML() {
		return wsSoapXML;
	}
}
