package oracle.retail.sim.common.core;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import oracle.retail.sim.common.core.locale.StringHelper;

public class SimServerException extends Exception {
  private static final long serialVersionUID = -5984044707480206929L;
  
  private static int ID_COUNTER = 1;
  
  private int id;
  
  private String userId;
  
  private long timestamp;
  
  public SimServerException() {
    super("An Exception has occurred on the server!");
    initException();
  }
  
  public SimServerException(String paramString) {
    super(paramString);
    initException();
  }
  
  public SimServerException(String paramString, Throwable paramThrowable) {
    super(paramString, paramThrowable);
    initException();
  }
  
  public SimServerException(Throwable paramThrowable) {
    super(paramThrowable);
    initException();
  }
  
  private void initException() {
    if (this.id < Integer.MAX_VALUE) {
      this.id = ID_COUNTER++;
    } else {
      this.id = 1;
    } 
    this.userId = UniversalContext.getUserName();
    this.timestamp = System.currentTimeMillis();
  }
  
  public String getId() {
    return String.valueOf(this.id);
  }
  
  public String getUserId() {
    return this.userId;
  }
  
  public long getTimestamp() {
    return this.timestamp;
  }
  
  public String getLogMessage() {
    StringBuilder stringBuilder = new StringBuilder("\nERROR-");
    stringBuilder.append(getId());
    stringBuilder.append("  User: ");
    stringBuilder.append(getUserId());
    stringBuilder.append("  Time: ");
    stringBuilder.append(formatDate(new Date(getTimestamp())));
    stringBuilder.append("  Type: ");
    stringBuilder.append(getType());
    stringBuilder.append("   Message: ");
    String str = getMessage();
    if (StringHelper.isNullOrEmpty(str)) {
      stringBuilder.append("No primary message.");
    } else {
      stringBuilder.append(str);
    } 
    Throwable throwable = getCause();
    if (throwable != null) {
      stringBuilder.append("   Root Cause: ");
      stringBuilder.append(throwable.getMessage());
    } 
    return stringBuilder.toString();
  }
  
  private String formatDate(Date paramDate) {
    return DateFormat.getDateTimeInstance(3, 3, Locale.US).format(paramDate);
  }
  
  protected String getType() {
    return "SimServerException";
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\core\SimServerException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */