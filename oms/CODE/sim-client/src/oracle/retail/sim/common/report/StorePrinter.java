package oracle.retail.sim.common.report;

import oracle.retail.sim.common.business.BusinessObject;
import oracle.retail.sim.common.core.type.Displayable;

public class StorePrinter extends BusinessObject implements Displayable {
  private static final long serialVersionUID = 2484472981022493937L;
  
  public static final Long TYPE_POSTSCRIPT = Long.valueOf(1L);
  
  public static final Long TYPE_TICKET = Long.valueOf(2L);
  
  public static final Long TYPE_FILE = Long.valueOf(98L);
  
  public static final Long TYPE_NULL = Long.valueOf(99L);
  
  private long id;
  
  private long type;
  
  private String uri;
  
  private String description;
  
  private Long storeId;
  
  public Long getId() {
    return Long.valueOf(this.id);
  }
  
  public void setId(Long paramLong) {
    this.id = paramLong.longValue();
  }
  
  public String getDescription() {
    return this.description;
  }
  
  public void setDescription(String paramString) {
    this.description = paramString;
  }
  
  public long getType() {
    return this.type;
  }
  
  public void setType(long paramLong) {
    this.type = paramLong;
  }
  
  public String getUri() {
    return this.uri;
  }
  
  public void setUri(String paramString) {
    this.uri = paramString;
  }
  
  public Long getStoreId() {
    return this.storeId;
  }
  
  public void setStoreId(Long paramLong) {
    this.storeId = paramLong;
  }
  
  public String toString() {
    return getDescription();
  }
  
  public String toDisplayString() {
    return this.description;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\report\StorePrinter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */