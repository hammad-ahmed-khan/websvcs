package com.logicinfo.oms.beans;

import java.util.TreeMap;

public  class ItemLocSingleton {
    private static ItemLocSingleton itemLocSingletonObj = new ItemLocSingleton(); 
    private static TreeMap<ItemLoc, String> itemlocTreemap = new TreeMap<ItemLoc, String>();
   
    
    public static ItemLocSingleton getItemLocSingletonInstance() {
        return itemLocSingletonObj;
    }

    public static void setItemlocTreemap(TreeMap<ItemLoc, String> itemlocTreemap) {
        ItemLocSingleton.itemlocTreemap = itemlocTreemap;
    }

    public static TreeMap<ItemLoc, String> getItemlocTreemap() {
        return itemlocTreemap;
    }
}
