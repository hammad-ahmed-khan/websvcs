package oracle.retail.sim.common.shipment;

import java.io.Serializable;
import java.util.Date;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.BusinessObject;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.person.AddressType;
import oracle.retail.sim.common.rules.core.QuantityMustBePositiveRule;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class BillOfLading extends BusinessObject implements Serializable {
  private static final long serialVersionUID = 1915518387547457396L;
  
  private Long id;
  
  private String motiveId;
  
  private ShipmentCarrierRole carrierRole;
  
  private ShipmentCarrier carrier;
  
  private ShipmentCarrierService carrierService;
  
  private String altCarrierName;
  
  private String altCarrierAddress;
  
  private Date requestedPickupDate;
  
  private AddressType shipToAddressType;
  
  private String altShipToAddress;
  
  private String trackingNumber;
  
  private String taxId;
  
  private Quantity weight;
  
  private String weightUom;
  
  private ShipmentCartonType cartonType;
  
  private boolean dirty;
  
  public Long getId() {
    return this.id;
  }
  
  public void doSetId(Long paramLong) {
    this.id = paramLong;
  }
  
  public String getMotiveId() {
    return this.motiveId;
  }
  
  public void setMotiveId(String paramString) throws BusinessException {
    checkForNullParameter("Motive", paramString);
    executeRule("setMotiveId", new Object[] { paramString });
    doSetMotiveId(paramString);
    doSetDirty();
  }
  
  public void doSetMotiveId(String paramString) {
    this.motiveId = paramString;
  }
  
  public ShipmentCarrierRole getCarrierRole() {
    return this.carrierRole;
  }
  
  public void setCarrierRole(ShipmentCarrierRole paramShipmentCarrierRole) throws BusinessException {
    checkForNullParameter("Carrier Role", paramShipmentCarrierRole);
    executeRule("setCarrierType", new Object[] { paramShipmentCarrierRole });
    doSetCarrierRole(paramShipmentCarrierRole);
    doSetDirty();
  }
  
  public void doSetCarrierRole(ShipmentCarrierRole paramShipmentCarrierRole) {
    this.carrierRole = paramShipmentCarrierRole;
  }
  
  public ShipmentCarrier getCarrier() {
    return this.carrier;
  }
  
  public void setCarrier(ShipmentCarrier paramShipmentCarrier) throws BusinessException {
    executeRule("setCarrier", new Object[] { paramShipmentCarrier });
    doSetCarrier(paramShipmentCarrier);
    doSetDirty();
  }
  
  public void doSetCarrier(ShipmentCarrier paramShipmentCarrier) {
    this.carrier = paramShipmentCarrier;
  }
  
  public ShipmentCarrierService getCarrierService() {
    return this.carrierService;
  }
  
  public void setCarrierService(ShipmentCarrierService paramShipmentCarrierService) throws BusinessException {
    executeRule("setDeliveryService", new Object[] { paramShipmentCarrierService });
    doSetCarrierService(paramShipmentCarrierService);
    doSetDirty();
  }
  
  public void doSetCarrierService(ShipmentCarrierService paramShipmentCarrierService) {
    this.carrierService = paramShipmentCarrierService;
  }
  
  public String getAlternateCarrierName() {
    return this.altCarrierName;
  }
  
  public void setAlternateCarrierName(String paramString) throws BusinessException {
    executeRule("setAlternateCarrierName", new Object[] { paramString });
    doSetAlternateCarrierName(paramString);
    doSetDirty();
  }
  
  public void doSetAlternateCarrierName(String paramString) {
    this.altCarrierName = paramString;
  }
  
  public String getAlternateCarrierAddress() {
    return this.altCarrierAddress;
  }
  
  public void setAlternateCarrierAddress(String paramString) throws BusinessException {
    executeRule("setAlternateCarrierAddress", new Object[] { paramString });
    doSetAlternateCarrierAddress(paramString);
    doSetDirty();
  }
  
  public void doSetAlternateCarrierAddress(String paramString) {
    this.altCarrierAddress = paramString;
  }
  
  public AddressType getShipToAddressType() {
    return this.shipToAddressType;
  }
  
  public void setShipToAddressType(AddressType paramAddressType) throws BusinessException {
    checkForNullParameter("Address Type", paramAddressType);
    executeRule("shipToAddressType", new Object[] { paramAddressType });
    doSetShipToAddressType(paramAddressType);
    doSetDirty();
  }
  
  public void doSetShipToAddressType(AddressType paramAddressType) {
    this.shipToAddressType = paramAddressType;
  }
  
  public String getAlternateShipToAddress() {
    return this.altShipToAddress;
  }
  
  public void setAlternateShipToAddress(String paramString) throws BusinessException {
    executeRule("setAlternateShipToAddress", new Object[] { paramString });
    doSetAlternateShipToAddress(paramString);
    doSetDirty();
  }
  
  public void doSetAlternateShipToAddress(String paramString) {
    this.altShipToAddress = paramString;
  }
  
  public Date getRequestedPickupDate() {
    return this.requestedPickupDate;
  }
  
  public void setRequestedPickupDate(Date paramDate) throws BusinessException {
    executeRule("setRequestedPickupDate", new Object[] { paramDate });
    doSetRequestedPickupDate(paramDate);
    doSetDirty();
  }
  
  public void doSetRequestedPickupDate(Date paramDate) {
    this.requestedPickupDate = paramDate;
  }
  
  public String getTrackingNumber() {
    return this.trackingNumber;
  }
  
  public void setTrackingNumber(String paramString) throws BusinessException {
    executeRule("setTrackingNumber", new Object[] { paramString });
    doSetTrackingNumber(paramString);
    doSetDirty();
  }
  
  public void doSetTrackingNumber(String paramString) {
    this.trackingNumber = paramString;
  }
  
  public String getTaxId() {
    return this.taxId;
  }
  
  public void setTaxId(String paramString) throws BusinessException {
    executeRule("setTaxId", new Object[] { paramString });
    doSetTaxId(paramString);
    doSetDirty();
  }
  
  public void doSetTaxId(String paramString) {
    this.taxId = paramString;
  }
  
  public Quantity getWeight() {
    return this.weight;
  }
  
  public void setWeight(Quantity paramQuantity) throws BusinessException {
    executeRule("setWeight", new Object[] { paramQuantity });
    QuantityMustBePositiveRule.execute(paramQuantity);
    doSetWeight(paramQuantity);
    doSetDirty();
  }
  
  public void doSetWeight(Quantity paramQuantity) {
    this.weight = paramQuantity;
  }
  
  public String getWeightUom() {
    return this.weightUom;
  }
  
  public void setWeightUom(String paramString) throws BusinessException {
    executeRule("setWeightUom", new Object[] { paramString });
    doSetWeightUom(paramString);
    doSetDirty();
  }
  
  public void doSetWeightUom(String paramString) {
    this.weightUom = paramString;
  }
  
  public ShipmentCartonType getCartonType() {
    return this.cartonType;
  }
  
  public void setCartonType(ShipmentCartonType paramShipmentCartonType) throws BusinessException {
    executeRule("setCartonType", new Object[] { paramShipmentCartonType });
    doSetCartonType(paramShipmentCartonType);
    doSetDirty();
  }
  
  public void doSetCartonType(ShipmentCartonType paramShipmentCartonType) {
    this.cartonType = paramShipmentCartonType;
  }
  
  public boolean isDirty() {
    return this.dirty;
  }
  
  public void doSetDirty() {
    this.dirty = true;
  }
  
  public void doSetClean() {
    this.dirty = false;
  }
  
  public int hashCode() {
    HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
    hashCodeBuilder.append(this.id);
    return hashCodeBuilder.toHashCode();
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (paramObject == null || paramObject.getClass() != getClass())
      return false; 
    BillOfLading billOfLading = (BillOfLading)paramObject;
    EqualsBuilder equalsBuilder = new EqualsBuilder();
    equalsBuilder.append(this.id, billOfLading.id);
    return equalsBuilder.isEquals();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\shipment\BillOfLading.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */