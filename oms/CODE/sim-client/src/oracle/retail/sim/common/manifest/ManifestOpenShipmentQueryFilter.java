package oracle.retail.sim.common.manifest;

import java.util.Date;
import java.util.List;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.BusinessObject;
import oracle.retail.sim.common.business.QueryFilter;

public class ManifestOpenShipmentQueryFilter extends BusinessObject implements QueryFilter {
  private static final long serialVersionUID = 3268918395601405051L;
  
  private String carrierCode;
  
  private String carrierServiceCode;
  
  private Date shipDate;
  
  private ManifestTranType manifestTranType;
  
  private List<String> trackingIds;
  
  public String getCarrierCode() {
    return this.carrierCode;
  }
  
  public void setCarrierCode(String paramString) throws BusinessException {
    executeRule("setCarrierCode", new Object[] { paramString });
    doSetCarrierCode(paramString);
  }
  
  public void doSetCarrierCode(String paramString) {
    this.carrierCode = paramString;
  }
  
  public String getCarrierServiceCode() {
    return this.carrierServiceCode;
  }
  
  public void setCarrierServiceCode(String paramString) throws BusinessException {
    executeRule("setCarrierServiceCode", new Object[] { paramString });
    doSetCarrierServiceCode(paramString);
  }
  
  public void doSetCarrierServiceCode(String paramString) {
    this.carrierServiceCode = paramString;
  }
  
  public Date getShipDate() {
    return this.shipDate;
  }
  
  public void setShipDate(Date paramDate) throws BusinessException {
    executeRule("setShipDate", new Object[] { paramDate });
    doSetShipDate(paramDate);
  }
  
  public void doSetShipDate(Date paramDate) {
    this.shipDate = paramDate;
  }
  
  public ManifestTranType getManifestTranType() {
    return this.manifestTranType;
  }
  
  public void setManifestTranType(ManifestTranType paramManifestTranType) throws BusinessException {
    executeRule("setManifestTranType", new Object[] { paramManifestTranType });
    doSetManifestTranType(paramManifestTranType);
  }
  
  public void doSetManifestTranType(ManifestTranType paramManifestTranType) {
    this.manifestTranType = paramManifestTranType;
  }
  
  public List<String> getTrackingIds() {
    return this.trackingIds;
  }
  
  public void setTrackingIds(List<String> paramList) throws BusinessException {
    executeRule("setTrackingIds", new Object[] { paramList });
    doSetTrackingIds(paramList);
  }
  
  public void doSetTrackingIds(List<String> paramList) {
    this.trackingIds = paramList;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\manifest\ManifestOpenShipmentQueryFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */