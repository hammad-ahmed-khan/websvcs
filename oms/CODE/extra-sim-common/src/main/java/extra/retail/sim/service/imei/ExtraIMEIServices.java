package extra.retail.sim.service.imei;

import java.util.List;

import extra.retail.sim.common.imei.UniqueSerialNumber;

/**
 * ExtraIMEIServices.java
 * aibrahim
 * 2023
 */
public abstract class ExtraIMEIServices {

	public abstract void saveIMEI(List<UniqueSerialNumber> imeiSerialNumbers) throws Exception;

	public abstract void cancelIMEI(List<UniqueSerialNumber> imeiSerialNumbers) throws Exception;
}
