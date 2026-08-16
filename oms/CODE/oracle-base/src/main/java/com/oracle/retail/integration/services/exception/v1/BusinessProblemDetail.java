
package com.oracle.retail.integration.services.exception.v1;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="problemDescription" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded"/&gt;
 *         &lt;element ref="{http://www.oracle.com/retail/integration/services/exception/v1}ProblemDetailEntry" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "problemDescription",
    "problemDetailEntry"
})
@XmlRootElement(name = "BusinessProblemDetail")
public class BusinessProblemDetail {

    @XmlElement(required = true)
    protected List<String> problemDescription;
    @XmlElement(name = "ProblemDetailEntry")
    protected List<ProblemDetailEntry> problemDetailEntry;

    /**
     * Gets the value of the problemDescription property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the problemDescription property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProblemDescription().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getProblemDescription() {
        if (problemDescription == null) {
            problemDescription = new ArrayList<String>();
        }
        return this.problemDescription;
    }

    /**
     * Gets the value of the problemDetailEntry property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the problemDetailEntry property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProblemDetailEntry().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ProblemDetailEntry }
     * 
     * 
     */
    public List<ProblemDetailEntry> getProblemDetailEntry() {
        if (problemDetailEntry == null) {
            problemDetailEntry = new ArrayList<ProblemDetailEntry>();
        }
        return this.problemDetailEntry;
    }

}
