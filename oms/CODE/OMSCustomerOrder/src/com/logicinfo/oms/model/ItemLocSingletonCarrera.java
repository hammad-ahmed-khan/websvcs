package com.logicinfo.oms.model;

import com.logicinfo.oms.beans.ItemLoc;

import java.util.concurrent.ConcurrentHashMap;

public class ItemLocSingletonCarrera
{
    private static ItemLocSingletonCarrera itemLocSingletonObjCarrera = null;
    private static ConcurrentHashMap<ItemLoc, String> itemlocHashmapCarrera = new ConcurrentHashMap<ItemLoc, String>();
    
    
    public static ItemLocSingletonCarrera getItemLocSingletonInstance() 
    {
        if (itemLocSingletonObjCarrera == null) {
                    itemLocSingletonObjCarrera = new ItemLocSingletonCarrera();           
                return itemLocSingletonObjCarrera;
        }
        else{
            return itemLocSingletonObjCarrera;
        }
    }

    public static void setItemlocHashmapCarrera(ConcurrentHashMap<ItemLoc, String> itemlocHashmappCarrera)
    {
        ItemLocSingletonCarrera.itemlocHashmapCarrera = itemlocHashmappCarrera;
    }

    public static ConcurrentHashMap<ItemLoc, String> getItemlocHashmapCarrera() 
    {
        return itemlocHashmapCarrera;
    }
}
