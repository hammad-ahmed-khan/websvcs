package oracle.retail.sim.common.notes;

import java.util.Date;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.BusinessObject;
import oracle.retail.sim.common.business.FunctionalArea;

public class Note extends BusinessObject {
  private static final long serialVersionUID = 4095409636720243331L;
  
  private Long id;
  
  private Date date;
  
  private FunctionalArea functionalArea;
  
  private Long functionalId;
  
  private String text;
  
  private String user;
  
  public Long getId() {
    return this.id;
  }
  
  public void doSetId(Long paramLong) {
    this.id = paramLong;
  }
  
  public Date getDate() {
    return this.date;
  }
  
  public void setDate(Date paramDate) throws BusinessException {
    checkForNullParameter("Date", paramDate);
    executeRule("setDate", new Object[] { paramDate });
    doSetDate(paramDate);
  }
  
  public void doSetDate(Date paramDate) {
    this.date = paramDate;
  }
  
  public FunctionalArea getFunctionalArea() {
    return this.functionalArea;
  }
  
  public void setFunctionalArea(FunctionalArea paramFunctionalArea) throws BusinessException {
    checkForNullParameter("FunctionalArea", paramFunctionalArea);
    executeRule("setFunctionalArea", new Object[] { paramFunctionalArea });
    doSetFunctionalArea(paramFunctionalArea);
  }
  
  public void doSetFunctionalArea(FunctionalArea paramFunctionalArea) {
    this.functionalArea = paramFunctionalArea;
  }
  
  public Long getFunctionalId() {
    return this.functionalId;
  }
  
  public void setFunctionalId(Long paramLong) throws BusinessException {
    checkForNullParameter("FunctionalId", paramLong);
    executeRule("setFunctionalId", new Object[] { paramLong });
    doSetFunctionalId(paramLong);
  }
  
  public void doSetFunctionalId(Long paramLong) {
    this.functionalId = paramLong;
  }
  
  public String getUser() {
    return this.user;
  }
  
  public void setUser(String paramString) throws BusinessException {
    checkForNullParameter("User", paramString);
    executeRule("setUser", new Object[] { paramString });
    doSetUser(paramString);
  }
  
  public void doSetUser(String paramString) {
    this.user = paramString;
  }
  
  public String getText() {
    return this.text;
  }
  
  public void setText(String paramString) throws BusinessException {
    checkForNullParameter("Text", paramString);
    executeRule("setText", new Object[] { paramString });
    doSetText(paramString);
  }
  
  public void doSetText(String paramString) {
    this.text = paramString;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\notes\Note.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */