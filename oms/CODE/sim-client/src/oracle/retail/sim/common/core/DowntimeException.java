package oracle.retail.sim.common.core;

public class DowntimeException extends Exception {
  private static final long serialVersionUID = -7840341184754154352L;
  
  public DowntimeException() {
    super("Client downtime exception has occurred!");
  }
  
  public DowntimeException(String paramString) {
    super(paramString);
  }
  
  public DowntimeException(String paramString, Throwable paramThrowable) {
    super(paramString, paramThrowable);
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\core\DowntimeException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */