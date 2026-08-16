package oracle.retail.sim.common.business;

import java.io.IOException;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.logging.LogService;
import oracle.retail.sim.common.rules.RuleEngine;
import oracle.retail.sim.common.util.SimObjectUtils;

public class Wrapper implements Cloneable {
  private boolean viewOnly;
  
  public boolean isViewOnly() {
    return this.viewOnly;
  }
  
  public void setViewOnly(boolean paramBoolean) {
    this.viewOnly = paramBoolean;
  }
  
  public void executeRule(String paramString, Object... paramVarArgs) throws BusinessException {
    RuleEngine.getInstance().executePropertyRule(paramString, this, paramVarArgs);
  }
  
  public void checkForNullParameter(String paramString, Object paramObject) throws BusinessException {
    if (paramObject == null || (paramObject instanceof String && StringHelper.isNullOrEmpty((String)paramObject)))
      throw new BusinessException(new BusinessError(CommonMessageText.BLANK_VALUE_INVALID, new Object[] { paramString })); 
  }
  
  public boolean isPropertyModifiable(String paramString) throws Exception {
    return !this.viewOnly;
  }
  
  public Object clone() {
    try {
      return SimObjectUtils.cloneBySerialization(this, false);
    } catch (IOException iOException) {
      LogService.error(this, "IOException occurred, unable to clone(); Will return null.", iOException);
    } catch (ClassNotFoundException classNotFoundException) {
      LogService.error(this, "ClassNotFoundException occurred, unable to clone(); Will return null.", classNotFoundException);
    } 
    return null;
  }
  
  public Object cloneShallow() {
    try {
      return super.clone();
    } catch (CloneNotSupportedException cloneNotSupportedException) {
      LogService.error(this, "Exception occurred unable to cloneShallow(); Will return null. ", cloneNotSupportedException);
      return null;
    } 
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\business\Wrapper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */