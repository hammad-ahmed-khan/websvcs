package oracle.retail.sim.common.business;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BusinessException extends Exception {
  private static final long serialVersionUID = 1547310964777714384L;
  
  private static final String MESSAGE_NULL = "Message cannot be null!";
  
  private final List<BusinessError> errors;
  
  public BusinessException(MessageText paramMessageText) {
    if (paramMessageText == null)
      throw new IllegalArgumentException("Message cannot be null!"); 
    this.errors = Collections.singletonList(new BusinessError(paramMessageText));
  }
  
  public BusinessException(MessageText paramMessageText, Throwable paramThrowable) {
    super(null, paramThrowable);
    if (paramMessageText == null)
      throw new IllegalArgumentException("Message cannot be null!"); 
    this.errors = Collections.singletonList(new BusinessError(paramMessageText));
  }
  
  public BusinessException(MessageText paramMessageText, String paramString) {
    if (paramMessageText == null)
      throw new IllegalArgumentException("Message cannot be null!"); 
    this.errors = Collections.singletonList(new BusinessError(paramMessageText, paramString));
  }
  
  public BusinessException(MessageText paramMessageText, Integer paramInteger) {
    if (paramMessageText == null)
      throw new IllegalArgumentException("Message cannot be null!"); 
    this.errors = Collections.singletonList(new BusinessError(paramMessageText, paramInteger));
  }
  
  public BusinessException(MessageText paramMessageText, Object[] paramArrayOfObject) {
    if (paramMessageText == null)
      throw new IllegalArgumentException("Message cannot be null!"); 
    this.errors = Collections.singletonList(new BusinessError(paramMessageText, paramArrayOfObject));
  }
  
  public BusinessException(MessageText paramMessageText, Object[] paramArrayOfObject, Throwable paramThrowable) {
    super(null, paramThrowable);
    if (paramMessageText == null)
      throw new IllegalArgumentException("Message cannot be null!"); 
    this.errors = Collections.singletonList(new BusinessError(paramMessageText, paramArrayOfObject));
  }
  
  public BusinessException(MessageText paramMessageText, List<String> paramList) {
    if (paramMessageText == null)
      throw new IllegalArgumentException("Message cannot be null!"); 
    if (paramList == null || paramList.isEmpty())
      throw new IllegalArgumentException("Data list must contain data!"); 
    ArrayList<String> arrayList = new ArrayList(paramList.size());
    for (String str : paramList)
      arrayList.add(str); 
    this.errors = Collections.singletonList(new BusinessError(paramMessageText, (List)arrayList));
  }
  
  public BusinessException(BusinessError paramBusinessError) {
    if (paramBusinessError == null)
      throw new IllegalArgumentException("Error cannot be null!"); 
    this.errors = Collections.singletonList(paramBusinessError);
  }
  
  public BusinessException(List<BusinessError> paramList) {
    if (paramList == null || paramList.isEmpty())
      throw new IllegalArgumentException("Errors cannot be null or empty!"); 
    this.errors = paramList;
  }
  
  public String getMessage() {
    return getPrimaryMessageText().getText();
  }
  
  public String getLocalizedMessage() {
    return getPrimaryMessageText().getText();
  }
  
  public List<BusinessError> getBusinessErrors() {
    return this.errors;
  }
  
  public MessageText getPrimaryMessageText() {
    return ((BusinessError)this.errors.get(0)).getMessageText();
  }
  
  public Object[] getPrimaryMessageValues() {
    return ((BusinessError)this.errors.get(0)).getMessageValues();
  }
  
  public List<Object> getPrimaryDataList() {
    return ((BusinessError)this.errors.get(0)).getDataList();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\business\BusinessException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */