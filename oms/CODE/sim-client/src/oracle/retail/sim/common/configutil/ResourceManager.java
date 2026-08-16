package oracle.retail.sim.common.configutil;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import oracle.retail.sim.common.logging.LogService;

public final class ResourceManager {
  public static BufferedReader getBufferedReader(String paramString) {
    checkPathForProblems(paramString);
    return new BufferedReader(new InputStreamReader(getInputStream(paramString)));
  }
  
  public static InputStream getInputStream(String paramString) {
    checkPathForProblems(paramString);
    return Thread.currentThread().getContextClassLoader().getResourceAsStream(paramString);
  }
  
  private static void checkPathForProblems(String paramString) {
    if (paramString.indexOf('\\') != -1)
      throw new IllegalArgumentException("Resource location cannot contain back-slash: " + paramString); 
  }
  
  private static void closeInputStream(InputStream paramInputStream, String paramString) {
    if (paramInputStream != null)
      try {
        paramInputStream.close();
      } catch (IOException iOException) {
        LogService.warn(ResourceManager.class, "Could not close input stream for: " + paramString);
      }  
  }
  
  public static ImageIcon getImageIcon(String paramString) {
    String str = "images/" + paramString;
    InputStream inputStream = null;
    try {
      inputStream = getInputStream(str);
      return new ImageIcon(ImageIO.read(inputStream));
    } catch (Exception exception) {
      LogService.warn(ResourceManager.class, "unable to load resource on classpath: " + str);
      return null;
    } finally {
      closeInputStream(inputStream, paramString);
    } 
  }
  
  public static Properties getProperties(String paramString) {
    String str = "conf/" + paramString;
    InputStream inputStream = null;
    try {
      inputStream = getInputStream(str);
      Properties properties = new Properties();
      properties.load(inputStream);
      return properties;
    } catch (Exception exception) {
      LogService.warn(ResourceManager.class, "unable to load resource on classpath: " + str);
      return null;
    } finally {
      closeInputStream(inputStream, paramString);
    } 
  }
  
  public static byte[] loadBytesFromURL(URL paramURL) {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    InputStream inputStream = null;
    try {
      char c = '†';
      URLConnection uRLConnection = paramURL.openConnection();
      uRLConnection.setConnectTimeout(c);
      uRLConnection.setReadTimeout(c);
      inputStream = uRLConnection.getInputStream();
      byte[] arrayOfByte = new byte[4096];
      int i;
      while ((i = inputStream.read(arrayOfByte)) > 0)
        byteArrayOutputStream.write(arrayOfByte, 0, i); 
    } catch (IOException iOException) {
      LogService.error(ResourceManager.class, "Failed while reading bytes from " + paramURL.toExternalForm() + " " + iOException.getMessage());
    } finally {
      closeInputStream(inputStream, paramURL.toString());
    } 
    return byteArrayOutputStream.toByteArray();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\configutil\ResourceManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */