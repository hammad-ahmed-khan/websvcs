package oracle.retail.sim.common.business;

import java.io.IOException;
import java.io.Serializable;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.logging.LogService;
import oracle.retail.sim.common.rules.RuleEngine;
import oracle.retail.sim.common.util.SimObjectUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public abstract class BusinessObject implements Serializable, Cloneable {
  private static final long serialVersionUID = 5067515273280038462L;
  
  public boolean isAttributesEqual(Object paramObject1, Object paramObject2) {
    EqualsBuilder equalsBuilder = new EqualsBuilder();
    equalsBuilder.append(paramObject1, paramObject2);
    return equalsBuilder.isEquals();
  }
  
  public boolean isAttributesNotEqual(Object paramObject1, Object paramObject2) {
    EqualsBuilder equalsBuilder = new EqualsBuilder();
    equalsBuilder.append(paramObject1, paramObject2);
    return !equalsBuilder.isEquals();
  }
  
  public boolean isPropertyModifiable(String paramString) {
    try {
      executeRule("isPropertyModifiable", new Object[] { paramString, this });
    } catch (BusinessException businessException) {
      return false;
    } 
    return true;
  }
  
  public boolean isCoherent() throws BusinessException {
    executeRule("isCoherent", new Object[0]);
    return true;
  }
  
  public void executeRule(String paramString, Object... paramVarArgs) throws BusinessException {
    RuleEngine.getInstance().executePropertyRule(paramString, this, paramVarArgs);
  }
  
  public void checkForNullParameter(String paramString, Object paramObject) throws BusinessException {
    if (paramObject == null || (paramObject instanceof String && StringHelper.isNullOrEmpty((String)paramObject)))
      throw new BusinessException(new BusinessError(CommonMessageText.BLANK_VALUE_INVALID, new Object[] { paramString })); 
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
    } catch (Exception exception) {
      LogService.error(this, "Exception occurred unable to cloneShallow(); Will return null. ", exception);
      return null;
    } 
  }
  
  public String toString() {
    try {
      return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    } catch (Throwable throwable) {
      return "ERROR: toString() failed!";
    } 
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\business\BusinessObject.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */