package extra.retail.sim.service.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import oracle.retail.sim.common.logging.LogService;

import extra.retail.sim.service.imei.ExtraIMEIServices;
import extra.retail.sim.service.imei.ExtraIMEIWebServices;

public class ExtraWSClientServiceFactoryImpl implements ExtraWebServiceFactoryInterface {

	private static Map<Class<?>, Object> cache = new ConcurrentHashMap<Class<?>, Object>();

	private static <T> T getService(Class<T> paramClass) {
		Object object = cache.get(paramClass);
		if (object == null)
			try {
				object = paramClass.newInstance();
				cache.put(paramClass, object);
			} catch (Throwable throwable) {
				LogService.error(ExtraClientServiceFactoryImpl.class, "Could not create extra client service: " + paramClass, throwable);
				throw new RuntimeException("Could not create extra client service: " + paramClass);
			}
		return (T) object;
	}

	@Override
	public ExtraIMEIServices getIMEIServices() {
		return getService(ExtraIMEIWebServices.class);
	}
}
