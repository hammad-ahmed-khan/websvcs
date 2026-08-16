
package com.logicinfo.oms.model;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RSADetails", propOrder = {
    "rsaBrand",
    "rsaYear",
    "rsaNumber"
})
public class RSADetails {

   
    @XmlElement(name = "RSABrand")
    protected String rsaBrand;
    @XmlElement(name = "RSAYear")
    protected long rsaYear;
    @XmlElement(name = "RSANumber")
    protected String rsaNumber;


    public void setRsaBrand(String rsaBrand) {
        this.rsaBrand = rsaBrand;
    }

    public String getRsaBrand() {
        return rsaBrand;
    }

    public void setRsaYear(long rsaYear) {
        this.rsaYear = rsaYear;
    }

    public long getRsaYear() {
        return rsaYear;
    }

    public void setRsaNumber(String rsaNumber) {
        this.rsaNumber = rsaNumber;
    }

    public String getRsaNumber() {
        return rsaNumber;
    }
}
