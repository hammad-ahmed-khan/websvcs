package com.extra.restservice.controller;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface LookUpInventoryService {

	public RealTimeInventoryResponse lookupInventory(RealTimeInventoryRequest request);

	public List<ItemAvailability> checkFutureInventoryQuantity(Collection<String> futureLocationset, Collection<String> futureItemList, List<ItemAvailability> itemAvailabilityList);

	public List<ItemAvailability> checkWHInventoryQuantity(List<Long> locations, Set<String> items, List<ItemAvailability> itemAvailablityList, final Map<Long, Locations> whLocMap);

	public BigDecimal getUnFulFilledQtyFromOmsBackOrderDTLTable(String item, String location, String locType, BigDecimal availableQty);
}
