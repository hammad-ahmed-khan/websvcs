package oracle.retail.sim.common.core;

import java.io.Serializable;
import oracle.retail.sim.common.configutil.CommonConfigManager;
import oracle.retail.sim.common.logging.LogService;
import oracle.retail.sim.common.util.SimObjectUtils;

public class CompressedObject<T> implements Serializable {
  private static final long serialVersionUID = 5602468301462261768L;
  
  private static Boolean compressionOff;
  
  private byte[] dataToSend;
  
  private boolean isObjectCompressed;
  
  public CompressedObject(T paramT) throws SimServerException {
    if (paramT == null)
      return; 
    this.isObjectCompressed = !isCompressionOff();
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "The serialized object " + (this.isObjectCompressed ? "is" : "is NOT") + " compressed"); 
    try {
      this.dataToSend = this.isObjectCompressed ? SimObjectUtils.toCompressedByteArray(paramT) : SimObjectUtils.toByteArray(paramT);
    } catch (Throwable throwable) {
      this.dataToSend = null;
      String str = this.isObjectCompressed ? "Error compressing and serializing object!" : "Error serializing object!";
      LogService.error(this, str, throwable);
      throw new SimServerException(str);
    } 
  }
  
  public T recoverObject() throws SimServerException {
    if (this.dataToSend == null)
      return null; 
    try {
      return (T)(this.isObjectCompressed ? SimObjectUtils.fromCompressedByteArray(this.dataToSend) : SimObjectUtils.fromByteArray(this.dataToSend));
    } catch (ClassNotFoundException classNotFoundException) {
      String str = "Unable to deserialize object: Class not found!";
      LogService.error(this, str, classNotFoundException);
      throw new SimServerException(str);
    } catch (Throwable throwable) {
      String str = this.isObjectCompressed ? "Error decompressing and deserializing object!" : "Error deserializing object!";
      LogService.error(this, str, throwable);
      throw new SimServerException(str);
    } finally {
      this.dataToSend = null;
    } 
  }
  
  private boolean isCompressionOff() {
    if (compressionOff == null)
      compressionOff = Boolean.valueOf((System.getProperty("COMPRESSION_OFF") != null || CommonConfigManager.getCompressionOff())); 
    return compressionOff.booleanValue();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\core\CompressedObject.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */