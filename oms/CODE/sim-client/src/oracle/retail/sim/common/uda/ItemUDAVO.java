package oracle.retail.sim.common.uda;

import java.io.Serializable;
import java.util.Date;

public class ItemUDAVO implements Serializable {
  private static final long serialVersionUID = -7138021010609604311L;
  
  private String itemId = null;
  
  private long udaId;
  
  private UDAType type = null;
  
  private String description = null;
  
  private Date udaDate = null;
  
  private String udaText = null;
  
  private String udaValue = null;
  
  public ItemUDAVO(String paramString) {
    this.itemId = paramString;
  }
  
  public String getItemId() {
    return this.itemId;
  }
  
  public long getUdaId() {
    return this.udaId;
  }
  
  public void doSetUdaId(long paramLong) {
    this.udaId = paramLong;
  }
  
  public UDAType getType() {
    return this.type;
  }
  
  public void doSetType(UDAType paramUDAType) {
    this.type = paramUDAType;
  }
  
  public String getDescription() {
    return this.description;
  }
  
  public void doSetDescription(String paramString) {
    this.description = paramString;
  }
  
  public Date getUdaDate() {
    return this.udaDate;
  }
  
  public void doSetUdaDate(Date paramDate) {
    this.udaDate = paramDate;
  }
  
  public String getUdaText() {
    return this.udaText;
  }
  
  public void doSetUdaText(String paramString) {
    this.udaText = paramString;
  }
  
  public String getUdaValue() {
    return this.udaValue;
  }
  
  public void doSetUdaValue(String paramString) {
    this.udaValue = paramString;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\commo\\uda\ItemUDAVO.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */