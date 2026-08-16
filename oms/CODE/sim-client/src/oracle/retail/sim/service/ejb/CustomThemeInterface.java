package oracle.retail.sim.service.ejb;

import java.util.List;
import java.util.Map;
import javax.ejb.Remote;
import oracle.retail.sim.common.core.CompressedObject;
import oracle.retail.sim.common.core.SimSession;
import oracle.retail.sim.common.theme.CustomColor;
import oracle.retail.sim.common.theme.CustomFont;
import oracle.retail.sim.common.theme.CustomIcon;
import oracle.retail.sim.common.theme.CustomTheme;

@Remote
public interface CustomThemeInterface {
  CompressedObject<List<CustomTheme>> findActiveCustomThemes(CompressedObject<SimSession> paramCompressedObject) throws Exception;
  
  CompressedObject<List<CustomTheme>> findAllCustomThemes(CompressedObject<SimSession> paramCompressedObject) throws Exception;
  
  CompressedObject<Map<String, Object>> findThemeConfiguration(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<?> insert(CompressedObject<CustomTheme> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<?> saveColors(CompressedObject<Long> paramCompressedObject, CompressedObject<List<CustomColor>> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<?> saveFonts(CompressedObject<Long> paramCompressedObject, CompressedObject<List<CustomFont>> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<?> saveIcons(CompressedObject<Long> paramCompressedObject, CompressedObject<List<CustomIcon>> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<?> update(CompressedObject<CustomTheme> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\ejb\CustomThemeInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */