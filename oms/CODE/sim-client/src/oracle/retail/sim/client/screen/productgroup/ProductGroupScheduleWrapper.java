package oracle.retail.sim.client.screen.productgroup;

import java.util.Date;
import java.util.TimeZone;
import oracle.retail.sim.common.date.SimDateUtil;
import oracle.retail.sim.common.schedule.ProductGroupScheduleVO;

/********************************************************************************************************
 * Product Group Schedule List screen wrapper used to convert nextDate and lastDate to the store's
 * timezone, because of the unique way we are storing the Product Group Schedule dates.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ProductGroupScheduleWrapper {

    private ProductGroupScheduleVO scheduleVO;
    private TimeZone storeTimeZone;

    public ProductGroupScheduleWrapper(ProductGroupScheduleVO scheduleVO, TimeZone timeZone) {
        this.scheduleVO = scheduleVO;
        storeTimeZone = timeZone;
    }

    public ProductGroupScheduleVO getScheduleVO() {
        return scheduleVO;
    }

    public Date getNextDate() {
        Date nextDate = scheduleVO.getSchedule().getNextDate(storeTimeZone);
        return SimDateUtil.convertDateFromUTC(storeTimeZone, nextDate);
    }

    public Date getLastDate() {
        Date lastDate = scheduleVO.getSchedule().getLastDate();
        return SimDateUtil.convertDateFromUTC(storeTimeZone, lastDate);
    }
}