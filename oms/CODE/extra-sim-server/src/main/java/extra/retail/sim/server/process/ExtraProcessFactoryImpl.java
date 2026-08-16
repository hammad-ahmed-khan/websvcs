package extra.retail.sim.server.process;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import oracle.retail.sim.common.logging.LogService;

/**
 * ExtraProcessFactoryImpl.java
 * aibrahim
 * 2024
 */
public class ExtraProcessFactoryImpl implements ExtraProcessFactoryInterface {

	private static Map<Class<?>, Object> cache = new ConcurrentHashMap<>();

	private static <T> T getProcess(Class<T> paramClass) {
		Object object = cache.get(paramClass);
		if (object == null)
			try {
				object = paramClass.newInstance();
				cache.put(paramClass, object);
			} catch (Throwable throwable) {
				LogService.error(ExtraProcessFactoryImpl.class, "Could not create extra process: " + paramClass, throwable);
				throw new RuntimeException("Could not create extra process: " + paramClass);
			}
		return (T) object;
	}

	@Override
	public ShipmentOrderProcess getShipmentOrderProcess() {
		return getProcess(ShipmentOrderScriptProcess.class);
	}
}
