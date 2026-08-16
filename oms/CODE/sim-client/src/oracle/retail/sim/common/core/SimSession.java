package oracle.retail.sim.common.core;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.net.InetAddress;
import java.util.Locale;
import java.util.UUID;
import oracle.retail.sim.common.logging.LogService;

public final class SimSession implements Externalizable {
  private static final long serialVersionUID = 3317755158879372328L;
  
  private String sessionId;
  
  private String userName;
  
  private DeviceType deviceType;
  
  private Locale locale;
  
  public SimSession() {}
  
  protected SimSession(String paramString, DeviceType paramDeviceType, Locale paramLocale) {
    this.userName = paramString;
    this.deviceType = paramDeviceType;
    this.locale = paramLocale;
    this.sessionId = generateSessionId();
  }
  
  private static String generateSessionId() {
    String str = UUID.randomUUID().toString();
    try {
      str = InetAddress.getLocalHost().toString() + "###" + str;
    } catch (Exception exception) {
      LogService.warn(SimSession.class, exception.getMessage());
    } 
    return str;
  }
  
  public String getSessionId() {
    return this.sessionId;
  }
  
  public String getUserName() {
    return this.userName;
  }
  
  public DeviceType getDeviceType() {
    return this.deviceType;
  }
  
  public Locale getLocale() {
    return this.locale;
  }
  
  public void readExternal(ObjectInput paramObjectInput) throws IOException, ClassNotFoundException {
    this.userName = (String)paramObjectInput.readObject();
    this.locale = (Locale)paramObjectInput.readObject();
    this.sessionId = (String)paramObjectInput.readObject();
    this.deviceType = (DeviceType)paramObjectInput.readObject();
  }
  
  public void writeExternal(ObjectOutput paramObjectOutput) throws IOException {
    paramObjectOutput.writeObject(this.userName);
    paramObjectOutput.writeObject(this.locale);
    paramObjectOutput.writeObject(this.sessionId);
    paramObjectOutput.writeObject(this.deviceType);
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\core\SimSession.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */