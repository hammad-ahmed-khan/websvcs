package oracle.retail.sim.service.theme;

import java.util.List;
import java.util.Map;
import oracle.retail.sim.common.theme.CustomColor;
import oracle.retail.sim.common.theme.CustomFont;
import oracle.retail.sim.common.theme.CustomIcon;
import oracle.retail.sim.common.theme.CustomTheme;

public abstract class CustomThemeServices {
  public abstract List<CustomTheme> findAllCustomThemes() throws Exception;
  
  public abstract List<CustomTheme> findActiveCustomThemes() throws Exception;
  
  public abstract Map<String, Object> findThemeConfiguration(Long paramLong) throws Exception;
  
  public abstract void insert(CustomTheme paramCustomTheme) throws Exception;
  
  public abstract void update(CustomTheme paramCustomTheme) throws Exception;
  
  public abstract void saveFonts(Long paramLong, List<CustomFont> paramList) throws Exception;
  
  public abstract void saveColors(Long paramLong, List<CustomColor> paramList) throws Exception;
  
  public abstract void saveIcons(Long paramLong, List<CustomIcon> paramList) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\theme\CustomThemeServices.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */