package oracle.retail.sim.common.configutil;

import java.util.HashMap;
import java.util.Map;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.core.DeviceType;
import oracle.retail.sim.common.core.JvmLocation;
import oracle.retail.sim.common.core.UniversalContext;
import oracle.retail.sim.common.date.SimDateUtil;
import oracle.retail.sim.common.item.ItemDescriptionType;
import oracle.retail.sim.common.lineitem.UOMMode;
import oracle.retail.sim.common.util.CacheRefreshStrategy;
import oracle.retail.sim.common.util.SystemTimestampProvider;
import oracle.retail.sim.common.util.TimedCacheRefreshStrategy;
import oracle.retail.sim.common.util.TimestampProvider;
import oracle.retail.sim.service.core.NativeServiceFactory;

public class SimConfigManager {
  public static final String AUDIT_STOCK_COUNT_COMPLETED = "AUDIT_STOCK_COUNT_COMPLETED";
  
  public static final String AUDIT_STOCK_COUNT_PROCESSED = "AUDIT_STOCK_COUNT_PROCESSED";
  
  public static final String AUDIT_PRICE_ADJUSTMENT = "AUDIT_PRICE_ADJUSTMENT";
  
  public static final String AUDIT_INV_ADJ_DISPATCH = "AUDIT_INV_ADJ_DISPATCH";
  
  public static final String AUDIT_INV_ADJ_UPDATE = "AUDIT_INV_ADJ_UPDATE";
  
  public static final String AUDIT_INV_ADJ_CREATE = "AUDIT_INV_ADJ_CREATE";
  
  public static final String AUDIT_RETURN_STOCK_UPDATE = "AUDIT_RETURN_STOCK_UPDATE";
  
  public static final String AUDIT_DIRECT_STORE_DELIVERY = "AUDIT_DIRECT_STORE_DELIVERY";
  
  public static final String AUDIT_TRANSFER_DISPATCH = "AUDIT_TRANSFER_DISPATCH";
  
  public static final String AUDIT_TRANSFER_UPDATE = "AUDIT_TRANSFER_UPDATE";
  
  public static final String AUDIT_TRANSFER_RECEIVING = "AUDIT_TRANSFER_RECEIVING";
  
  public static final String AUDIT_PUBLISH_MESSAGE = "AUDIT_PUBLISH_MESSAGE";
  
  public static final String AUDIT_RECEIVE_MESSAGE = "AUDIT_RECEIVE_MESSAGE";
  
  public static final String AUDIT_ITEM_REQUEST = "AUDIT_ITEM_REQUEST";
  
  public static final String AUDIT_SESSION_TIMEOUT = "AUDIT_SESSION_TIMEOUT";
  
  public static final String AUDIT_SECURITY = "AUDIT_SECURITY";
  
  public static final String DAYS_ALLOWED_FOR_ADJUSTMENTS_TO_TRANSFERS = "DAYS_ALLOWED_FOR_ADJUSTMENTS_TO_TRANSFERS";
  
  public static final String DAYS_ALLOWED_FOR_ADJUSTMENTS_TO_DIRECT_DELIVERYS = "DAYS_ALLOWED_FOR_ADJUSTMENTS_TO_DIRECT_DELIVERYS";
  
  public static final String DAYS_ALLOWED_FOR_ADJUSTMENTS_TO_WAREHOUSE_DELIVERYS = "DAYS_ALLOWED_FOR_ADJUSTMENTS_TO_WAREHOUSE_DELIVERYS";
  
  public static final String DAYS_TO_HOLD_CANCELLED_ITEM_REQUESTS = "DAYS_TO_HOLD_CANCELLED_ITEM_REQUESTS";
  
  public static final String DAYS_TO_HOLD_AUDIT_RECORDS = "DAYS_TO_HOLD_AUDIT_RECORDS";
  
  public static final String DAYS_TO_HOLD_CUSTOMER_ORDERS = "DAYS_TO_HOLD_CUSTOMER_ORDERS";
  
  public static final String DAYS_TO_HOLD_RCVD_TRANSFERS_RECORDS = "DAYS_TO_HOLD_RCVD_TRANSFERS_RECORDS";
  
  public static final String DAYS_TO_HOLD_PURCHASE_ORDERS = "DAYS_TO_HOLD_COMPLETED_PURCHASE_ORDERS";
  
  public static final String DAYS_TO_HOLD_COMPLETED_INV_ADJ = "DAYS_TO_HOLD_COMPLETED_INV_ADJ";
  
  public static final String DAYS_TO_HOLD_COMPLETED_STOCK_COUNTS = "DAYS_TO_HOLD_COMPLETED_STOCK_COUNTS";
  
  public static final String DAYS_TO_HOLD_ITEM_TICKETS = "DAYS_TO_HOLD_ITEM_TICKETS";
  
  public static final String DAYS_TO_HOLD_RECEIVED_SHIPMENTS = "DAYS_TO_HOLD_RECEIVED_SHIPMENTS";
  
  public static final String DAYS_TO_HOLD_RETURNS = "DAYS_TO_HOLD_RETURNS";
  
  public static final String DAYS_TO_HOLD_SHELF_REPLENISHMENTS = "DAYS_TO_HOLD_SHELF_REPLENISHMENTS";
  
  public static final String DAYS_TO_HOLD_PRICE_CHANGE_WORKSHEET = "DAYS_TO_HOLD_PRICE_CHANGE_WORKSHEET";
  
  public static final String DAYS_TO_HOLD_DISPATCHED_TRANSFER_BEFORE_SENDING_EMAIL_ALERT = "DAYS_TO_HOLD_DISPATCHED_TRANSFER_BEFORE_SENDING_EMAIL_ALERT";
  
  public static final String DAYS_TO_SEND_EMAIL_ALERT_BEFORE_NOT_AFTER_DATE_FOR_RETURN_REQUESTS = "DAYS_TO_SEND_EMAIL_ALERT_BEFORE_NOT_AFTER_DATE_FOR_RETURN_REQUESTS";
  
  public static final String DAYS_TO_SEND_EMAIL_ALERT_BEFORE_NOT_AFTER_DATE_FOR_TRANSFER_REQUESTS = "DAYS_TO_SEND_EMAIL_ALERT_BEFORE_NOT_AFTER_DATE_FOR_TRANSFER_REQUESTS";
  
  public static final String DAYS_TO_LOCKOUT_ALL_LOCATIONS_STOCK_COUNT = "DAYS_TO_LOCKOUT_ALL_LOCATIONS_STOCK_COUNT";
  
  public static final String DAYS_TO_HOLD_COMPLETED_UINS = "DAYS_TO_HOLD_COMPLETED_UINS";
  
  public static final String DAYS_TO_HOLD_RESOLVED_UIN_EXCEPTIONS = "DAYS_TO_HOLD_RESOLVED_UIN_EXCEPTIONS";
  
  public static final String DAYS_TO_HOLD_UIN_AUDIT_INFORMATION = "DAYS_TO_HOLD_UIN_AUDIT_INFORMATION";
  
  public static final String DAYS_TO_HOLD_RELATED_ITEMS = "DAYS_TO_HOLD_RELATED_ITEMS";
  
  public static final String DAYS_TO_HOLD_COMPLETED_STAGING_RECORDS = "DAYS_TO_HOLD_COMPLETED_STAGING_RECORDS";
  
  public static final String MINUTES_TO_HOLD_NEW_CUSTOMER_ORDER_BEFORE_SENDING_EMAIL_ALERT = "MINUTES_TO_HOLD_NEW_CUSTOMER_ORDER_BEFORE_SENDING_EMAIL_ALERT";
  
  public static final String MINUTES_TO_HOLD_OPEN_CUSTOMER_ORDER_PICK_BEFORE_SENDING_EMAIL_ALERT = "MINUTES_TO_HOLD_OPEN_CUSTOMER_ORDER_PICK_BEFORE_SENDING_EMAIL_ALERT";
  
  public static final String ALLOW_NON_RANGE_ITEM = "ALLOW_NON_RANGE_ITEM";
  
  public static final String ALLOW_UNEXPECTED_UINS = "ALLOW_UNEXPECTED_UINS";
  
  public static final String AUTO_DEFAULT_UIN_ATTRIBUTES = "AUTO_DEFAULT_UIN_ATTRIBUTES";
  
  public static final String DEFAULT_TICKET_TYPE = "DEFAULT_TICKET_TYPE";
  
  public static final String DISABLE_DISCREPANCIES = "DISABLE_DISCREPANCIES";
  
  public static final String DEFAULT_UOM = "DEFAULT_UOM";
  
  public static final String DISABLE_DAMAGES = "DISABLE_DAMAGES";
  
  public static final String DISABLE_CASE_SIZE = "DISABLE_PACK_SIZE";
  
  public static final String DISPLAY_ITEM_IMAGE_BUTTON = "DISPLAY_ITEM_IMAGE_BUTTON";
  
  public static final String EXTERNAL_FINISHER_ENABLED = "EXTERNAL_FINISHER_ENABLE_DISABLE";
  
  public static final String END_OF_DAY_MAX_FILL = "END_OF_DAY_MAX_FILL";
  
  public static final String ITEM_BASKET_PRINTING = "ITEM_BASKET_PRINTING";
  
  public static final String ITEM_OUT_OF_STOCK_UNITS = "ITEM_OUT_OF_STOCK_UNITS";
  
  public static final String ITEM_OUT_OF_STOCK_PERCENT = "ITEM_OUT_OF_STOCK_PERCENT";
  
  public static final String RESTRICT_STORE_ORDERABLE_ITEMS = "RESTRICT_STORE_ORDERABLE_ITEMS";
  
  public static final String WITHIN_DAY_MAX_FILL = "WITHIN_DAY_MAX_FILL";
  
  public static final String ONLINE_HELP_URL = "ONLINE_HELP_URL";
  
  public static final String DEFAULT_FILE_DATE_FORMAT = "DEFAULT_FILE_DATE_FORMAT";
  
  public static final String EMAIL_FROM_NAME = "EMAIL_FROM_NAME";
  
  public static final String REPORTING_TOOL_URL = "REPORTING_TOOL_URL";
  
  public static final int MAX_CHAR_SCREEN_LENGTH = 21;
  
  public static final String MULTI_SET_OF_BOOKS = "MULTI_SET_OF_BOOKS";
  
  public static final String MULTI_SET_OF_BOOKS_ENABLED = "Enabled";
  
  public static final String MULTI_SET_OF_BOOKS_DISABLED = "Disabled";
  
  public static final String MULTI_SET_OF_BOOKS_SIMONLY = "SIM Only";
  
  public static final String PRODUCT_GROUP_ITEM_REQUEST_LIMIT = "ITEM_REQUEST_UI_LIMIT";
  
  public static final String PRODUCT_GROUP_SHELF_REPLENISHMENT_LIMIT = "SHELF_REPLENISHMENT_UI_LIMIT";
  
  public static final String PRODUCT_GROUP_PROBLEM_LINE_LIMIT = "PROBLEM_LINE_UI_LIMIT";
  
  public static final String PRODUCT_GROUP_UNIT_LIMIT = "UNIT_COUNT_UI_LIMIT";
  
  public static final String PRODUCT_GROUP_UNIT_AMOUNT_LIMIT = "UNIT_AND_AMOUNT_COUNT_UI_LIMIT";
  
  public static final String SECURITY_AUTHENTICATION_METHOD = "SECURITY_AUTHENTICATION_METHOD";
  
  public static final String SECURITY_USER_AUTHENTICATION_CACHE_HOURS = "SECURITY_USER_AUTHENTICATION_CACHE_HOURS";
  
  public static final String SECURITY_USER_AUTHORIZATION_CACHE_HOURS = "SECURITY_USER_AUTHORIZATION_CACHE_HOURS";
  
  public static final String SECURITY_MAX_LOGIN_ATTEMPTS = "SECURITY_MAX_LOGIN_ATTEMPTS";
  
  public static final String SECURITY_LOGIN_FAILURE_EXPIRATION_HOURS = "SECURITY_LOGIN_FAILURE_EXPIRATION_HOURS";
  
  public static final String SECURITY_MAX_DAYS_FOR_TEMP_USER_END_DATE = "SECURITY_MAX_DAYS_FOR_TEMP_USER_END_DATE";
  
  public static final String SECURITY_DAYS_TO_HOLD_DELETED_USERS = "SECURITY_DAYS_TO_HOLD_DELETED_USERS";
  
  public static final String SECURITY_DAYS_TO_HOLD_EXPIRED_USER_ROLES = "SECURITY_DAYS_TO_HOLD_EXPIRED_USER_ROLES";
  
  public static final String PASSWORD_DAYS_UNTIL_EXPIRES = "PASSWORD_DAYS_UNTIL_EXPIRES";
  
  public static final String PASSWORD_DAYS_BEFORE_EXPIRES_TO_NOTIFY = "PASSWORD_DAYS_BEFORE_EXPIRES_TO_NOTIFY";
  
  public static final String PASSWORD_INITIAL_CHANGE = "PASSWORD_INITIAL_CHANGE";
  
  public static final String PASSWORD_MAXIMUM_LENGTH = "PASSWORD_MAXIMUM_LENGTH";
  
  public static final String PASSWORD_MINIMUM_LENGTH = "PASSWORD_MINIMUM_LENGTH";
  
  public static final String PASSWORD_MUST_CONTAIN_ALHPA_NUMERIC = "PASSWORD_MUST_CONTAIN_ALHPA_NUMERIC";
  
  public static final String PASSWORD_MUST_CONTAIN_CAPITAL = "PASSWORD_MUST_CONTAIN_CAPITAL";
  
  public static final String PASSWORD_MUST_CONTAIN_NUMERIC = "PASSWORD_MUST_CONTAIN_NUMERIC";
  
  public static final String PASSWORD_MUST_CONTAIN_SPECIAL_CHARACTER = "PASSWORD_MUST_CONTAIN_SPECIAL_CHARACTER";
  
  public static final String PASSWORD_NUMBER_OF_PREVIOUS_TO_DISALLOW = "PASSWORD_NUMBER_OF_PREVIOUS_TO_DISALLOW";
  
  public static final String PASSWORD_SPECIAL_CHARACTERS = "PASSWORD_SPECIAL_CHARACTERS";
  
  public static final String PASSWORD_ASSIGNMENT_EMAIL_USER = "PASSWORD_ASSIGNMENT_EMAIL_USER";
  
  public static final String STOCK_COUNT_AUTO_SAVE = "STOCK_COUNT_AUTO_SAVE";
  
  public static final String STOCK_COUNT_DISPLAY_DEFAULT_TIMEFRAME = "STOCK_COUNT_DISPLAY_DEFAULT_TIMEFRAME";
  
  public static final String STOCK_COUNT_LOCKOUT_DAYS = "STOCK_COUNT_LOCKOUT_DAYS";
  
  public static final Integer STOCK_COUNT_LOCKOUT_DAYS_DEFAULT = Integer.valueOf(1);
  
  public static final String STOCK_COUNT_NULL_QUANTITY = "STOCK_COUNT_NULL_QUANTITY";
  
  public static final String STOCK_COUNT_SALES_PROCESS_UNIT = "STOCK_COUNT_SALES_PROCESS_UNIT";
  
  public static final String STOCK_COUNT_SALES_PROCESS_UA = "STOCK_COUNT_SALES_PROCESS_UA";
  
  public static final String STOCK_COUNT_UPDATE_ALL_SOH = "STOCK_COUNT_UPDATE_ALL_SOH";
  
  public static final String UNGUIDED_STOCK_COUNTS_ALLOW_MULTIPLE_USERS = "UNGUIDED_STOCK_COUNTS_ALLOW_MULTIPLE_USERS";
  
  public static final String CUSTOMER_ORDER_NEW_EMAIL_ALERT = "CUSTOMER_ORDER_NEW_EMAIL_ALERT";
  
  public static final String CUSTOMER_ORDER_RECEIPT_EMAIL_ALERT = "CUSTOMER_ORDER_RECEIPT_EMAIL_ALERT";
  
  public static final String TRANSFER_DAMAGED_EMAIL_ALERT = "TRANSFER_DAMAGED_EMAIL_ALERT";
  
  public static final String TRANSFER_DISPATCH_EMAIL_ALERT = "TRANSFER_DISPATCH_EMAIL_ALERT";
  
  public static final String TRANSFER_OVER_UNDER_EMAIL_ALERT = "TRANSFER_OVER_UNDER_EMAIL_ALERT";
  
  public static final String TRANSFER_REQUEST_APPROVE_EMAIL_ALERT = "TRANSFER_REQUEST_APPROVE_EMAIL_ALERT";
  
  public static final String TRANSFER_REQUEST_EMAIL_ALERT = "TRANSFER_REQUEST_EMAIL_ALERT";
  
  public static final String TRANSFER_REQUEST_REJECT_EMAIL_ALERT = "TRANSFER_REQUEST_REJECT_EMAIL_ALERT";
  
  public static final String RECORD_ADJUSTMENT_TO_DELIVERY = "RECORD_ADJUSTMENT_TO_DELIVERY";
  
  public static final String ADD_ITEM_TO_DELIVERY_ON_RECEIVE = "ADD_ITEM_TO_DELIVERY_ON_RECEIVE";
  
  public static final String DIRECT_DELIVERY_DEFAULT_IDENTIFY_PO = "DIRECT_DELIVERY_DEFAULT_IDENTIFY_PO";
  
  public static final String DIRECT_DELIVERY_PREFERRED_CURRENCY = "DIRECT_DELIVERY_PREFERRED_CURRENCY";
  
  public static final String DIRECT_DELIVERY_SEND_NULL_UNIT_COST = "DIRECT_DELIVERY_SEND_NULL_UNIT_COST";
  
  public static final String DISPLAY_UNIT_COST_FOR_DIRECT_DELIVERIES = "DISPLAY_UNIT_COST_FOR_DIRECT_DELIVERIES";
  
  public static final String ENABLE_DSD_PACK_RECEIVING = "ENABLE_DSD_PACK_RECEIVING";
  
  public static final String DISABLE_SUPPLIER_INDICATOR_FOR_PURCHASE_ORDER_CREATION = "DISABLE_SUPPLIER_INDICATOR_FOR_PURCHASE_ORDER_CREATION";
  
  public static final String DEXNEX_INPUT_DIR = "DEXNEX_INPUT_DIR";
  
  public static final String DEXNEX_ERROR_DIR = "DEXNEX_ERROR_DIR";
  
  public static final String ADD_ITEM_TO_CARTON_ON_RECEIVE = "ADD_ITEM_TO_CARTON_ON_RECEIVE";
  
  public static final String QUICK_WH_RECEIVING = "QUICK_WH_RECEIVING";
  
  public static final String QUICK_WH_RECEIVING_AUTO_CONFIRM = "QUICK_WH_RECEIVING_AUTO_CONFIRM";
  
  public static final String QUICK_WH_RECEIVING_PROMPT_FOR_RECEIVED_CARTONS = "QUICK_WH_RECEIVING_PROMPT_FOR_RECEIVED_CARTONS";
  
  public static final String QUICK_WH_RECEIVING_ALLOW_MISSING_CARTONS = "QUICK_WH_RECEIVING_ALLOW_MISSING_CARTONS";
  
  public static final String ADD_ITEM_TO_TRANSFER_ON_RECEIVE = "ADD_ITEM_TO_TRANSFER_ON_RECEIVE";
  
  public static final String RECEIVE_ENTIRE_TRANSFER = "RECEIVE_ENTIRE_TRANSFER";
  
  public static final String TSF_FORCE_CLOSE_IND = "TSF_FORCE_CLOSE_IND";
  
  public static final String PURGE_RCVD_TRANSFERS = "PURGE_RCVD_TRANSFERS";
  
  public static final String TRANSFER_DISPATCH_VALIDATE = "TRANSFER_DISPATCH_VALIDATE";
  
  public static final String ADD_ITEM_TO_RETURN_REQUESTS = "ADD_ITEM_TO_RETURN_REQUESTS";
  
  public static final String DSD_DELIVERY_SUPPLIER_FOR_RTV = "DSD_DELIVERY_SUPPLIER_FOR_RTV";
  
  public static final String DISPLAY_ITEM_DESCRIPTION = "DISPLAY_ITEM_DESCRIPTION";
  
  public static final String DISPLAY_LENGTH_DIFF1 = "DISPLAY_LENGTH_DIFF1";
  
  public static final String DISPLAY_LENGTH_DIFF2 = "DISPLAY_LENGTH_DIFF2";
  
  public static final String DISPLAY_LENGTH_DIFF3 = "DISPLAY_LENGTH_DIFF3";
  
  public static final String DISPLAY_LENGTH_DIFF4 = "DISPLAY_LENGTH_DIFF4";
  
  public static final int MAX_PRODUCT_GROUP_ITEMS = 100000;
  
  public static final Quantity MAX_AGSN_QTY = new Quantity(999L);
  
  public static final String ENABLE_GMT_DAILY_BATCH_RUN = "ENABLE_GMT_DAILY_BATCH_RUN";
  
  public static final String ENABLE_GMT_FOR_CUSTOMER_ORDERS = "ENABLE_GMT_FOR_CUSTOMER_ORDERS";
  
  public static final String ENABLE_GMT_FOR_DEXNEX = "ENABLE_GMT_FOR_DEXNEX";
  
  public static final String ENABLE_GMT_FOR_DIRECT_DELIVERIES = "ENABLE_GMT_FOR_DIRECT_DELIVERIES";
  
  public static final String ENABLE_GMT_FOR_FOUNDATION_DATA = "ENABLE_GMT_FOR_FOUNDATION_DATA";
  
  public static final String ENABLE_GMT_FOR_INVENTORY_ADJUSTMENTS = "ENABLE_GMT_FOR_INVENTORY_ADJUSTMENTS";
  
  public static final String ENABLE_GMT_FOR_ITEM_REQUESTS = "ENABLE_GMT_FOR_ITEM_REQUESTS";
  
  public static final String ENABLE_GMT_FOR_PRICE_CHANGES = "ENABLE_GMT_FOR_PRICE_CHANGES";
  
  public static final String ENABLE_GMT_FOR_RECEIVING = "ENABLE_GMT_FOR_RECEIVING";
  
  public static final String ENABLE_GMT_FOR_RESA_IMPORT = "ENABLE_GMT_FOR_RESA_IMPORT";
  
  public static final String ENABLE_GMT_FOR_POS_IMPORT = "ENABLE_GMT_FOR_POS_IMPORT";
  
  public static final String ENABLE_GMT_FOR_RTVS = "ENABLE_GMT_FOR_RTVS";
  
  public static final String ENABLE_GMT_FOR_STORE_ORDERS = "ENABLE_GMT_FOR_STORE_ORDERS";
  
  public static final String ENABLE_GMT_FOR_STORE_TRANSFERS = "ENABLE_GMT_FOR_STORE_TRANSFERS";
  
  public static final String ENABLE_GMT_FOR_WAREHOUSE_TRANSFERS = "ENABLE_GMT_FOR_WAREHOUSE_TRANSFERS";
  
  public static final String ENABLE_RSL_CALL = "ENABLE_RSL_CALL";
  
  public static final String ENABLE_SUB_BUCKETS = "ENABLE_SUB_BUCKETS";
  
  public static final String DAYS_TO_HOLD_CANCELLED_TEMPLATES = "DAYS_TO_HOLD_CANCELED_TEMPLATES";
  
  public static final String DAYS_TO_HOLD_TRANSACTION_HISTORY = "DAYS_TO_HOLD_TRANSACTION_HISTORY";
  
  public static final String SEARCH_LIMIT_CONTAINER_LOOKUP = "SEARCH_LIMIT_CONTAINER_LOOKUP";
  
  public static final String SEARCH_LIMIT_INV_ADJUSTMENT = "SEARCH_LIMIT_INV_ADJUSTMENT";
  
  public static final String SEARCH_LIMIT_ITEM_LOOKUP = "SEARCH_LIMIT_ITEM_LOOKUP";
  
  public static final String SEARCH_LIMIT_PRICE_CHANGE = "SEARCH_LIMIT_PRICE_CHANGE";
  
  public static final String SEARCH_LIMIT_SUPPLIER_LOOKUP = "SEARCH_LIMIT_SUPPLIER_LOOKUP";
  
  public static final String SEARCH_LIMIT_STORE_SEQUENCE = "SEARCH_LIMIT_SEQUENCING";
  
  public static final String SEARCH_LIMIT_STAGED_MESSAGE = "SEARCH_LIMIT_STAGED_MESSAGE";
  
  public static final String SEARCH_LIMIT_TRANSACTION_HISTORY = "SEARCH_LIMIT_TRANSACTION_HISTORY";
  
  public static final String SEARCH_LIMIT_UIN_RESOLUTION = "SEARCH_LIMIT_UIN_RESOLUTION";
  
  public static final String SEARCH_LIMIT_CUSTOMER_ORDER_MGMT = "SEARCH_LIMIT_CUSTOMER_ORDER_MGMT";
  
  public static final int SEARCH_LIMIT_MAX_VALUE = 999;
  
  public static final String TEMP_SERVER_TIME = "TEMP_SERVER_TIME";
  
  private static TimestampProvider timestampProvider = (TimestampProvider)new SystemTimestampProvider();
  
  private static CacheRefreshStrategy configCacheTimer = (CacheRefreshStrategy)new TimedCacheRefreshStrategy(timestampProvider, "REFRESH_RATE_CONFIG", 3600000L);
  
  private static CacheRefreshStrategy storeConfigCacheTimer = (CacheRefreshStrategy)new TimedCacheRefreshStrategy(timestampProvider, "REFRESH_RATE_STORE", 3600000L);
  
  private static Map<String, Object> configMap = new HashMap<>();
  
  private static Map<Long, Map<String, Object>> storeConfigMap = new HashMap<>();
  
  private static Integer defaultUom = UOMMode.CASES.getCode();
  
  private static boolean isOverride;
  
  public static boolean getBoolean(String paramString) {
    try {
      Boolean bool = (Boolean)getConfigMap().get(paramString);
      return (bool != null && bool.booleanValue());
    } catch (Throwable throwable) {
      return false;
    } 
  }
  
  public static Integer getInteger(String paramString) {
    try {
      return (Integer)getConfigMap().get(paramString);
    } catch (Throwable throwable) {
      return null;
    } 
  }
  
  public static String getString(String paramString) {
    try {
      return (String)getConfigMap().get(paramString);
    } catch (Throwable throwable) {
      return null;
    } 
  }
  
  public static Integer getDefaultUom() {
    return defaultUom;
  }
  
  public static boolean isRslEnabled() {
    return getBoolean("ENABLE_RSL_CALL");
  }
  
  public static boolean isItemShortDescription() {
    return (ItemDescriptionType.toValue(getString("DISPLAY_ITEM_DESCRIPTION")) == ItemDescriptionType.SHORT);
  }
  
  private static Map<String, Object> getConfigMap() throws Exception {
    if (!isOverride && (configMap.isEmpty() || configCacheTimer.isCacheStale()))
      reloadConfigMap(); 
    return configMap;
  }
  
  public static boolean getStoreBoolean(String paramString, Long paramLong) {
    try {
      Map<String, Object> map = getStoreConfigMap(paramLong);
      if (map != null) {
        Boolean bool = (Boolean)map.get(paramString);
        return (bool != null && bool.booleanValue());
      } 
      return false;
    } catch (Throwable throwable) {
      return false;
    } 
  }
  
  public static String getStoreString(String paramString, Long paramLong) {
    try {
      Map<String, Object> map = getStoreConfigMap(paramLong);
      return (map != null) ? (String)map.get(paramString) : null;
    } catch (Throwable throwable) {
      return null;
    } 
  }
  
  public static Double getStoreDouble(String paramString, Long paramLong) {
    try {
      Map<String, Object> map = getStoreConfigMap(paramLong);
      return (map != null) ? (Double)map.get(paramString) : null;
    } catch (Throwable throwable) {
      return null;
    } 
  }
  
  public static Integer getStoreInteger(String paramString, Long paramLong) {
    try {
      Map<String, Object> map = getStoreConfigMap(paramLong);
      return (map != null) ? (Integer)map.get(paramString) : null;
    } catch (Throwable throwable) {
      return null;
    } 
  }
  
  public static void updateStoreConfigMap(Long paramLong, Map<String, Object> paramMap) throws Exception {
    getStoreConfigMap(paramLong).putAll(paramMap);
  }
  
  private static Map<String, Object> getStoreConfigMap(Long paramLong) throws Exception {
    if (!isOverride && storeConfigCacheTimer.isCacheStale())
      storeConfigMap.clear(); 
    if (!storeConfigMap.containsKey(paramLong) || ((Map)storeConfigMap.get(paramLong)).isEmpty())
      storeConfigMap.put(paramLong, NativeServiceFactory.getConfigServices().readStoreConfigSettings(paramLong, getConfigDeviceType())); 
    return storeConfigMap.get(paramLong);
  }
  
  public static void reloadConfigMap() throws Exception {
    Map<String, Object> map = NativeServiceFactory.getConfigServices().readConfigSettings(getConfigDeviceType());
    SimDateUtil.setClientServerOffsetMillis((Long)map.remove("TEMP_SERVER_TIME"));
    configMap = map;
    defaultUom = (Integer)configMap.get("DEFAULT_UOM");
  }
  
  private static DeviceType getConfigDeviceType() {
    return JvmLocation.isServer() ? DeviceType.SERVER : UniversalContext.getDeviceType();
  }
  
  public static void doOverrideValue(String paramString, Object paramObject) throws Exception {
    isOverride = true;
    getConfigMap().put(paramString, paramObject);
    if ("DEFAULT_UOM".equals(paramString))
      defaultUom = (Integer)paramObject; 
  }
  
  public static void doOverrideStoreValue(Long paramLong, String paramString, Object paramObject) throws Exception {
    isOverride = true;
    getStoreConfigMap(paramLong).put(paramString, paramObject);
  }
  
  public static void clearOverrideValues() {
    isOverride = false;
    configMap.clear();
    storeConfigMap.clear();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\configutil\SimConfigManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */