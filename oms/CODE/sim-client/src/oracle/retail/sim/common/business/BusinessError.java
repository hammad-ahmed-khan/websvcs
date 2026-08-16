package oracle.retail.sim.common.business;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class BusinessError implements Serializable {
  private static final long serialVersionUID = -5688062419965344942L;
  
  private final MessageText messageText;
  
  private final Object[] messageValues;
  
  private final List<Object> dataList;
  
  public BusinessError(MessageText paramMessageText) {
    this.messageText = paramMessageText;
    this.messageValues = null;
    this.dataList = Collections.emptyList();
  }
  
  public BusinessError(MessageText paramMessageText, String paramString) {
    this.messageText = paramMessageText;
    this.messageValues = new Object[] { paramString };
    this.dataList = Collections.emptyList();
  }
  
  public BusinessError(MessageText paramMessageText, Number paramNumber) {
    this.messageText = paramMessageText;
    this.messageValues = new Object[] { paramNumber };
    this.dataList = Collections.emptyList();
  }
  
  public BusinessError(MessageText paramMessageText, Object[] paramArrayOfObject) {
    this.messageText = paramMessageText;
    this.messageValues = paramArrayOfObject;
    this.dataList = Collections.emptyList();
  }
  
  public BusinessError(MessageText paramMessageText, List<Object> paramList) {
    this.messageText = paramMessageText;
    this.messageValues = null;
    this.dataList = paramList;
  }
  
  public BusinessError(MessageText paramMessageText, Object[] paramArrayOfObject, List<Object> paramList) {
    this.messageText = paramMessageText;
    this.messageValues = paramArrayOfObject;
    this.dataList = paramList;
  }
  
  public MessageText getMessageText() {
    return this.messageText;
  }
  
  public Object[] getMessageValues() {
    return this.messageValues;
  }
  
  public List<Object> getDataList() {
    return this.dataList;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\business\BusinessError.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */