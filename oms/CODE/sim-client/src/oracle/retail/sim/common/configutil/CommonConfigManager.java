package oracle.retail.sim.common.configutil;

import oracle.retail.sim.common.business.BOFactoryInterface;
import oracle.retail.sim.common.business.ClientCommandFactoryInterface;
import oracle.retail.sim.common.format.MoneyMaskFactoryInterface;
import oracle.retail.sim.common.format.PhoneMaskFactoryInterface;
import oracle.retail.sim.common.logging.LogService;
import oracle.retail.sim.common.security.CredentialStoreProvider;
import oracle.retail.sim.service.core.ServiceFactoryInterface;

public class CommonConfigManager {
  public static final String COMPRESSION_OFF = "COMPRESSION_OFF";
  
  public static final String BO_FACTORY_IMPL = "BO_FACTORY_IMPL";
  
  public static final String CLIENT_COMMAND_FACTORY_IMPL = "CLIENT_COMMAND_FACTORY_IMPL";
  
  public static final String CLIENT_SERVICE_FACTORY_IMPL = "CLIENT_SERVICE_FACTORY_IMPL";
  
  public static final String SERVER_SERVICE_FACTORY_IMPL = "SERVER_SERVICE_FACTORY_IMPL";
  
  public static final String CURRENCY_DEFAULT_TYPE = "CURRENCY_DEFAULT_TYPE";
  
  public static final String MONEY_MASK_FACTORY = "MONEY_MASK_FACTORY";
  
  public static final String PHONE_MASK_FACTORY = "PHONE_MASK_FACTORY";
  
  public static final String CREDENTIAL_STORE_PROVIDER = "CREDENTIAL_STORE_PROVIDER";
  
  public static final String CREDENTIAL_STORE_MAP = "CREDENTIAL_STORE_MAP";
  
  public static final String REFRESH_RATE_ACTIVE_INV_ADJ_REASON = "REFRESH_RATE_ACTIVE_INV_ADJ_REASON";
  
  public static final String REFRESH_RATE_BARCODE_PROCESSOR_LABEL = "REFRESH_RATE_BARCODE_PROCESSOR_LABEL";
  
  public static final String REFRESH_RATE_CARRIER_SERVICE = "REFRESH_RATE_CARRIER_SERVICE";
  
  public static final String REFRESH_RATE_CARTON_TYPE = "REFRESH_RATE_CARTON_TYPE";
  
  public static final String REFRESH_RATE_CONFIG = "REFRESH_RATE_CONFIG";
  
  public static final String REFRESH_RATE_CONTEXT_TYPE = "REFRESH_RATE_CONTEXT_TYPE";
  
  public static final String REFRESH_RATE_CUSTOMER_RESERVE_TYPES_LABEL = "REFRESH_RATE_CUSTOMER_RESERVE_TYPES_LABEL";
  
  public static final String REFRESH_RATE_DELIVERY_TIMESLOT = "REFRESH_RATE_DELIVERY_TIMESLOT";
  
  public static final String REFRESH_RATE_FINISHER_RETURN_REASON = "REFRESH_RATE_FINISHER_RETURN_REASON";
  
  public static final String REFRESH_RATE_AGSN_TICKET_FORMAT = "REFRESH_RATE_AGSN_TICKET_FORMAT";
  
  public static final String REFRESH_RATE_ITEM_TICKET_FORMAT = "REFRESH_RATE_ITEM_TICKET_FORMAT";
  
  public static final String REFRESH_RATE_ITEM_TICKET_TYPE = "REFRESH_RATE_ITEM_TICKET_TYPE";
  
  public static final String REFRESH_RATE_MERCH_HIERARCHY = "REFRESH_RATE_MERCH_HIERARCHY";
  
  public static final String REFRESH_RATE_NONSELLABLE_QTY_TYPE = "REFRESH_RATE_NONSELLABLE_QTY_TYPE";
  
  public static final String REFRESH_RATE_PRICE_HISTORY = "REFRESH_RATE_PRICE_HISTORY";
  
  public static final String REFRESH_RATE_SERIALIZATION_LABEL = "REFRESH_RATE_SERIALIZATION_LABEL";
  
  public static final String REFRESH_RATE_SHELF_LABEL_FORMAT = "REFRESH_RATE_SHELF_LABEL_FORMAT";
  
  public static final String REFRESH_RATE_STORE = "REFRESH_RATE_STORE";
  
  public static final String REFRESH_RATE_SUPPLIER = "REFRESH_RATE_SUPPLIER";
  
  public static final String REFRESH_RATE_SUPPLIER_RETURN_REASON = "REFRESH_RATE_SUPPLIER_RETURN_REASON";
  
  public static final String REFRESH_RATE_TRANSLATION = "REFRESH_RATE_TRANSLATION";
  
  public static final String REFRESH_RATE_UDA_DETAILS = "REFRESH_RATE_UDA_DETAILS";
  
  public static final String REFRESH_RATE_UDA_LOV = "REFRESH_RATE_UDA_DETAILS";
  
  public static final String REFRESH_RATE_UIN_CONTAINER = "REFRESH_RATE_UIN_CONTAINER";
  
  public static final String REFRESH_RATE_UOM_CONVERSION = "REFRESH_RATE_UOM_CONVERSION";
  
  public static final String REFRESH_RATE_WAREHOUSE = "REFRESH_RATE_WAREHOUSE";
  
  public static final String REFRESH_RATE_WAREHOUSE_RETURN_REASON = "REFRESH_RATE_WAREHOUSE_RETURN_REASON";
  
  public static final String REFRESH_RATE_WIRELESS_ITEM_DIFF = "REFRESH_RATE_WIRELESS_ITEM_DIFF";
  
  private static ConfigManager configManager;
  
  public static boolean getCompressionOff() {
    return configManager.getBoolean("COMPRESSION_OFF", false);
  }
  
  public static BOFactoryInterface getBOFactoryImpl() {
    return configManager.<BOFactoryInterface>getObject("BO_FACTORY_IMPL", BOFactoryInterface.class);
  }
  
  public static ServiceFactoryInterface getServerServiceFactoryImpl() {
    return configManager.<ServiceFactoryInterface>getObject("SERVER_SERVICE_FACTORY_IMPL", ServiceFactoryInterface.class);
  }
  
  public static ServiceFactoryInterface getClientServiceFactoryImpl() {
    return configManager.<ServiceFactoryInterface>getObject("CLIENT_SERVICE_FACTORY_IMPL", ServiceFactoryInterface.class);
  }
  
  public static ClientCommandFactoryInterface getClientCommandFactoryImpl() {
    return configManager.<ClientCommandFactoryInterface>getObject("CLIENT_COMMAND_FACTORY_IMPL", ClientCommandFactoryInterface.class);
  }
  
  public static String getDefaultCurrencyCode() {
    return configManager.getString("CURRENCY_DEFAULT_TYPE");
  }
  
  public static MoneyMaskFactoryInterface getMoneyMaskFactory() {
    return configManager.<MoneyMaskFactoryInterface>getObject("MONEY_MASK_FACTORY", MoneyMaskFactoryInterface.class);
  }
  
  public static PhoneMaskFactoryInterface getPhoneMaskFactory() {
    return configManager.<PhoneMaskFactoryInterface>getObject("PHONE_MASK_FACTORY", PhoneMaskFactoryInterface.class);
  }
  
  public static CredentialStoreProvider getCredentialStoreProvider() {
    return configManager.<CredentialStoreProvider>getObject("CREDENTIAL_STORE_PROVIDER", CredentialStoreProvider.class);
  }
  
  public static String getCredentialStoreMap() {
    return configManager.getString("CREDENTIAL_STORE_MAP");
  }
  
  public static long getLong(String paramString, long paramLong) {
    return configManager.getLong(paramString, paramLong);
  }
  
  static {
    try {
      configManager = new ConfigManager("common.cfg");
    } catch (Throwable throwable) {
      LogService.error(CommonConfigManager.class, "Failed loading: common.cfg", throwable);
    } 
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\configutil\CommonConfigManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */