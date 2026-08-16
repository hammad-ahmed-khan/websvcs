package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQueries( { @NamedQuery(name = "OmsWebserviceUriDetail.findAll",
                             query = "select o from OmsWebserviceUriDetail o"),
                 @NamedQuery(name = "OmsWebserviceUriDetail.findByWebserviceName",
                             query = "select o.url from OmsWebserviceUriDetail o where o.webServiceName=:webServiceName"),
                                  @NamedQuery(name = "OmsWebserviceUriDetail.findWebServiceId",
                             query = "select o.webServiceId from OmsWebserviceUriDetail o where o.webServiceName=:webServiceName")})
@Table(name = "OMS_WEBSERVICE_URI_DETAIL")
public class OmsWebserviceUriDetail implements Serializable {
    @Column(name = "HOST_NAME", nullable = false, length = 150)
    private String hostName;
    @Column(name = "PARENT_ELEMENT", nullable = false, length = 200)
    private String parentElement;
    @Column(name = "PORT_NUMBER")
    private BigDecimal portNumber;
    @Column(name = "RESPONSE_NAMESPACE", nullable = false, length = 500)
    private String responseNamespace;
    @Column(name = "TARGET_NAMESPACE", nullable = false, length = 500)
    private String targetNamespace;
    @Column(nullable = false, length = 500)
    private String url;
    @Id
    @Column(name = "WEB_SERVICE_ID", nullable = false)
    private BigDecimal webServiceId;
    @Column(name = "WEB_SERVICE_NAME", nullable = false, length = 35)
    private String webServiceName;

    public OmsWebserviceUriDetail() {
    }

    public OmsWebserviceUriDetail(String hostName, String parentElement, BigDecimal portNumber,
                                  String responseNamespace, String targetNamespace, String url,
                                  BigDecimal webServiceId, String webServiceName) {
        this.hostName = hostName;
        this.parentElement = parentElement;
        this.portNumber = portNumber;
        this.responseNamespace = responseNamespace;
        this.targetNamespace = targetNamespace;
        this.url = url;
        this.webServiceId = webServiceId;
        this.webServiceName = webServiceName;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getParentElement() {
        return parentElement;
    }

    public void setParentElement(String parentElement) {
        this.parentElement = parentElement;
    }

    public BigDecimal getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(BigDecimal portNumber) {
        this.portNumber = portNumber;
    }

    public String getResponseNamespace() {
        return responseNamespace;
    }

    public void setResponseNamespace(String responseNamespace) {
        this.responseNamespace = responseNamespace;
    }

    public String getTargetNamespace() {
        return targetNamespace;
    }

    public void setTargetNamespace(String targetNamespace) {
        this.targetNamespace = targetNamespace;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public BigDecimal getWebServiceId() {
        return webServiceId;
    }

    public void setWebServiceId(BigDecimal webServiceId) {
        this.webServiceId = webServiceId;
    }

    public String getWebServiceName() {
        return webServiceName;
    }

    public void setWebServiceName(String webServiceName) {
        this.webServiceName = webServiceName;
    }
}
