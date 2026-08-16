package oracle.retail.sim.common.source;

import java.math.BigDecimal;
import oracle.retail.sim.common.business.BusinessObject;

public class SupplierItemCountryDim extends BusinessObject {
  private static final long serialVersionUID = 8075420148866458303L;
  
  private String item;
  
  private String supplier;
  
  private String originCountry;
  
  private String dimObject;
  
  private String presentationMethod;
  
  private BigDecimal length;
  
  private BigDecimal width;
  
  private BigDecimal height;
  
  private String lwhUom;
  
  private BigDecimal weight;
  
  private BigDecimal netWeight;
  
  private String weightUom;
  
  private BigDecimal liquidVolume;
  
  private String liquidVolumeUom;
  
  private BigDecimal statCube;
  
  public String getDimObject() {
    return this.dimObject;
  }
  
  public void setDimObject(String paramString) {
    this.dimObject = paramString;
  }
  
  public BigDecimal getHeight() {
    return this.height;
  }
  
  public void setHeight(BigDecimal paramBigDecimal) {
    this.height = paramBigDecimal;
  }
  
  public String getItem() {
    return this.item;
  }
  
  public void setItem(String paramString) {
    this.item = paramString;
  }
  
  public BigDecimal getLength() {
    return this.length;
  }
  
  public void setLength(BigDecimal paramBigDecimal) {
    this.length = paramBigDecimal;
  }
  
  public BigDecimal getLiquidVolume() {
    return this.liquidVolume;
  }
  
  public void setLiquidVolume(BigDecimal paramBigDecimal) {
    this.liquidVolume = paramBigDecimal;
  }
  
  public String getLiquidVolumeUom() {
    return this.liquidVolumeUom;
  }
  
  public void setLiquidVolumeUom(String paramString) {
    this.liquidVolumeUom = paramString;
  }
  
  public String getLwhUom() {
    return this.lwhUom;
  }
  
  public void setLwhUom(String paramString) {
    this.lwhUom = paramString;
  }
  
  public BigDecimal getNetWeight() {
    return this.netWeight;
  }
  
  public void setNetWeight(BigDecimal paramBigDecimal) {
    this.netWeight = paramBigDecimal;
  }
  
  public String getOriginCountry() {
    return this.originCountry;
  }
  
  public void setOriginCountry(String paramString) {
    this.originCountry = paramString;
  }
  
  public String getPresentationMethod() {
    return this.presentationMethod;
  }
  
  public void setPresentationMethod(String paramString) {
    this.presentationMethod = paramString;
  }
  
  public BigDecimal getStatCube() {
    return this.statCube;
  }
  
  public void setStatCube(BigDecimal paramBigDecimal) {
    this.statCube = paramBigDecimal;
  }
  
  public String getSupplier() {
    return this.supplier;
  }
  
  public void setSupplier(String paramString) {
    this.supplier = paramString;
  }
  
  public BigDecimal getWeight() {
    return this.weight;
  }
  
  public void setWeight(BigDecimal paramBigDecimal) {
    this.weight = paramBigDecimal;
  }
  
  public String getWeightUom() {
    return this.weightUom;
  }
  
  public void setWeightUom(String paramString) {
    this.weightUom = paramString;
  }
  
  public BigDecimal getWidth() {
    return this.width;
  }
  
  public void setWidth(BigDecimal paramBigDecimal) {
    this.width = paramBigDecimal;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\source\SupplierItemCountryDim.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */