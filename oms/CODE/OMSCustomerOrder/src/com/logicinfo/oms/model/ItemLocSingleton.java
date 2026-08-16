package com.logicinfo.oms.model;

import com.logicinfo.oms.beans.ItemLoc;

import java.util.concurrent.ConcurrentHashMap;

public  class ItemLocSingleton {
    private static ItemLocSingleton itemLocSingletonObj =null;
    private static ConcurrentHashMap<ItemLoc, String> itemLocHashMap = new ConcurrentHashMap<ItemLoc, String>();
   
    
    public static ItemLocSingleton getItemLocSingletonInstance() {
        if(itemLocSingletonObj==null){
            itemLocSingletonObj= new ItemLocSingleton();
            return itemLocSingletonObj;
        }
        else{
            return itemLocSingletonObj;
        }
    }

    public static void setItemLocConcurrentHashMap(ConcurrentHashMap<ItemLoc, String> itemlocHashmap) {
        ItemLocSingleton.itemLocHashMap = itemlocHashmap;
    }

    public static ConcurrentHashMap<ItemLoc, String> getItemLocHashMap() {
        return itemLocHashMap;
    }
}
