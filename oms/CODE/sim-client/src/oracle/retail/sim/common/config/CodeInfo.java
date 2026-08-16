package oracle.retail.sim.common.config;

import oracle.retail.sim.common.business.BusinessObject;

public class CodeInfo extends BusinessObject {
  private static final long serialVersionUID = 5377546754160395763L;
  
  private String codeType;
  
  private String id;
  
  private String description;
  
  private boolean required;
  
  private Long sequence;
  
  public CodeInfo(String paramString) {
    this.codeType = paramString;
  }
  
  public String getCodeType() {
    return this.codeType;
  }
  
  public String getId() {
    return this.id;
  }
  
  public void doSetId(String paramString) {
    this.id = paramString;
  }
  
  public String getDescription() {
    return this.description;
  }
  
  public void doSetDescription(String paramString) {
    this.description = paramString;
  }
  
  public boolean isRequired() {
    return this.required;
  }
  
  public void doSetRequired(boolean paramBoolean) {
    this.required = paramBoolean;
  }
  
  public Long getSequence() {
    return this.sequence;
  }
  
  public void doSetSequence(Long paramLong) {
    this.sequence = paramLong;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\config\CodeInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */