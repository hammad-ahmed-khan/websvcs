package oracle.retail.sim.common.mps;

import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.BusinessObject;
import oracle.retail.sim.common.business.QueryFilter;
import oracle.retail.sim.common.integration.SimMessageFamily;
import oracle.retail.sim.common.integration.SimMessageType;
import oracle.retail.sim.common.rules.core.NumberBetween1And999Rule;

public class MpsStagedMessageQueryFilter extends BusinessObject implements QueryFilter {
  private static final long serialVersionUID = -5879659231339637987L;
  
  private Long messageId;
  
  private SimMessageType messageType;
  
  private SimMessageFamily messageFamily;
  
  private Boolean inbound;
  
  private boolean showPending;
  
  private boolean showRetry = true;
  
  private int searchLimit = 99;
  
  public Long getMessageId() {
    return this.messageId;
  }
  
  public void setMessageId(Long paramLong) throws BusinessException {
    executeRule("setMessageId", new Object[] { paramLong });
    doSetMessageId(paramLong);
  }
  
  private void doSetMessageId(Long paramLong) {
    this.messageId = paramLong;
  }
  
  public SimMessageFamily getMessageFamily() {
    return this.messageFamily;
  }
  
  public void setMessageFamily(SimMessageFamily paramSimMessageFamily) throws BusinessException {
    executeRule("setMessageFamily", new Object[] { paramSimMessageFamily });
    doSetMessageFamily(paramSimMessageFamily);
  }
  
  private void doSetMessageFamily(SimMessageFamily paramSimMessageFamily) {
    this.messageFamily = paramSimMessageFamily;
  }
  
  public SimMessageType getMessageType() {
    return this.messageType;
  }
  
  public void setMessageType(SimMessageType paramSimMessageType) throws BusinessException {
    executeRule("setMessageType", new Object[] { paramSimMessageType });
    doSetMessageType(paramSimMessageType);
  }
  
  private void doSetMessageType(SimMessageType paramSimMessageType) {
    this.messageType = paramSimMessageType;
  }
  
  public Boolean isInbound() {
    return this.inbound;
  }
  
  public void setInbound(Boolean paramBoolean) throws BusinessException {
    executeRule("setInbound", new Object[] { paramBoolean });
    doSetInbound(paramBoolean);
  }
  
  public void doSetInbound(Boolean paramBoolean) {
    this.inbound = paramBoolean;
  }
  
  public Boolean isShowPending() {
    return Boolean.valueOf(this.showPending);
  }
  
  public void setShowPending(boolean paramBoolean) throws BusinessException {
    executeRule("setShowPending", new Object[] { Boolean.valueOf(paramBoolean) });
    doSetShowPending(paramBoolean);
  }
  
  public void doSetShowPending(boolean paramBoolean) {
    this.showPending = paramBoolean;
  }
  
  public Boolean isShowRetry() {
    return Boolean.valueOf(this.showRetry);
  }
  
  public void setShowRetry(boolean paramBoolean) throws BusinessException {
    executeRule("setShowRetry", new Object[] { Boolean.valueOf(paramBoolean) });
    doSetShowRetry(paramBoolean);
  }
  
  public void doSetShowRetry(boolean paramBoolean) {
    this.showRetry = paramBoolean;
  }
  
  public int getSearchLimit() {
    return this.searchLimit;
  }
  
  public void setSearchLimit(int paramInt) throws BusinessException {
    NumberBetween1And999Rule.execute(Integer.valueOf(paramInt));
    executeRule("setSearchLimit", new Object[] { Integer.valueOf(paramInt) });
    doSetSearchLimit(paramInt);
  }
  
  public void doSetSearchLimit(int paramInt) {
    this.searchLimit = paramInt;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\mps\MpsStagedMessageQueryFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */