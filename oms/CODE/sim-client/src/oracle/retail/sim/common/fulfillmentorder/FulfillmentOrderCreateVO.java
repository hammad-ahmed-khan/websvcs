package oracle.retail.sim.common.fulfillmentorder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class FulfillmentOrderCreateVO implements Serializable {
  private static final long serialVersionUID = -4827931382287941310L;
  
  private String carrierCode;
  
  private String carrierServiceCode;
  
  private String comments;
  
  private String customerOrderId;
  
  private BigDecimal deliveryCharges;
  
  private String deliveryChargesCurrency;
  
  private Date deliveryDate;
  
  private String deliveryType;
  
  private Long fulfillLocId;
  
  private String fulfillmentOrderId;
  
  private String partialDeliveryIndicator;
  
  private FulfillmentOrderCreateCustomerVO customerVO;
  
  private List<FulfillmentOrderCreateLineItemVO> lineItemVOs = new ArrayList<>();
  
  public String getCarrierCode() {
    return this.carrierCode;
  }
  
  public void doSetCarrierCode(String paramString) {
    this.carrierCode = paramString;
  }
  
  public String getCarrierServiceCode() {
    return this.carrierServiceCode;
  }
  
  public void doSetCarrierServiceCode(String paramString) {
    this.carrierServiceCode = paramString;
  }
  
  public String getComments() {
    return this.comments;
  }
  
  public void doSetComments(String paramString) {
    this.comments = paramString;
  }
  
  public String getCustomerOrderId() {
    return this.customerOrderId;
  }
  
  public void doSetCustomerOrderId(String paramString) {
    this.customerOrderId = paramString;
  }
  
  public BigDecimal getDeliveryCharges() {
    return this.deliveryCharges;
  }
  
  public void doSetDeliveryCharges(BigDecimal paramBigDecimal) {
    this.deliveryCharges = paramBigDecimal;
  }
  
  public String getDeliveryChargesCurrency() {
    return this.deliveryChargesCurrency;
  }
  
  public void doSetDeliveryChargesCurrency(String paramString) {
    this.deliveryChargesCurrency = paramString;
  }
  
  public Date getDeliveryDate() {
    return this.deliveryDate;
  }
  
  public void doSetDeliveryDate(Date paramDate) {
    this.deliveryDate = paramDate;
  }
  
  public String getDeliveryType() {
    return this.deliveryType;
  }
  
  public void doSetDeliveryType(String paramString) {
    this.deliveryType = paramString;
  }
  
  public Long getFulfillLocId() {
    return this.fulfillLocId;
  }
  
  public void doSetFulfillLocId(Long paramLong) {
    this.fulfillLocId = paramLong;
  }
  
  public String getFulfillmentOrderId() {
    return this.fulfillmentOrderId;
  }
  
  public void doSetFulfillmentOrderId(String paramString) {
    this.fulfillmentOrderId = paramString;
  }
  
  public String getPartialDeliveryIndicator() {
    return this.partialDeliveryIndicator;
  }
  
  public void doSetPartialDeliveryIndicator(String paramString) {
    this.partialDeliveryIndicator = paramString;
  }
  
  public FulfillmentOrderCreateCustomerVO getCustomerVO() {
    return this.customerVO;
  }
  
  public void doSetCustomerVO(FulfillmentOrderCreateCustomerVO paramFulfillmentOrderCreateCustomerVO) {
    this.customerVO = paramFulfillmentOrderCreateCustomerVO;
  }
  
  public List<FulfillmentOrderCreateLineItemVO> getLineItemVOs() {
    return this.lineItemVOs;
  }
  
  public void doSetLineItemVOs(List<FulfillmentOrderCreateLineItemVO> paramList) {
    this.lineItemVOs = paramList;
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (paramObject == null || paramObject.getClass() != getClass())
      return false; 
    FulfillmentOrderCreateVO fulfillmentOrderCreateVO = (FulfillmentOrderCreateVO)paramObject;
    EqualsBuilder equalsBuilder = new EqualsBuilder();
    equalsBuilder.append(this.customerOrderId, fulfillmentOrderCreateVO.customerOrderId);
    equalsBuilder.append(this.fulfillmentOrderId, this.fulfillmentOrderId);
    return equalsBuilder.isEquals();
  }
  
  public int hashCode() {
    HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
    hashCodeBuilder.append(this.customerOrderId);
    hashCodeBuilder.append(this.fulfillmentOrderId);
    return hashCodeBuilder.hashCode();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\fulfillmentorder\FulfillmentOrderCreateVO.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */