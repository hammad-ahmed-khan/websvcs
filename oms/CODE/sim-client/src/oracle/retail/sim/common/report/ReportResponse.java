package oracle.retail.sim.common.report;

import oracle.retail.sim.common.business.BusinessObject;
import oracle.retail.sim.common.business.MessageText;

public class ReportResponse extends BusinessObject {
  private static final long serialVersionUID = -750139886226681214L;
  
  private MessageText message;
  
  private String messageValue;
  
  private String printResponse;
  
  private boolean printFailed;
  
  public MessageText getMessage() {
    return this.message;
  }
  
  public void setMessage(MessageText paramMessageText) {
    this.message = paramMessageText;
  }
  
  public String getMessageValue() {
    return this.messageValue;
  }
  
  public void setMessageValue(String paramString) {
    this.messageValue = paramString;
  }
  
  public String getPrintResponse() {
    return this.printResponse;
  }
  
  public void setPrintResponse(String paramString) {
    this.printResponse = paramString;
  }
  
  public boolean isFailedState() {
    return this.printFailed;
  }
  
  public void setFailedState() {
    this.printFailed = true;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\report\ReportResponse.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */