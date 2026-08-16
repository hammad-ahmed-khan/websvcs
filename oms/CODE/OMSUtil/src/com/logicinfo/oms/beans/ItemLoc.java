package com.logicinfo.oms.beans;

import java.math.BigDecimal;

public class ItemLoc implements Comparable<ItemLoc> {
    
    private BigDecimal location;
    
    private String  item;


    public void setLocation(BigDecimal location) {
        this.location = location;
    }

    public BigDecimal getLocation() {
        return location;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getItem() {
        return item;
    }


    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof ItemLoc)) {
            return false;
        }
        final ItemLoc other = (ItemLoc)object;
        if (!(location == null ? other.location == null : location.equals(other.location))) {
            return false;
        }
        if (!(item == null ? other.item == null : item.equals(other.item))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int PRIME = 37;
        int result = 1;
        result = PRIME * result + ((location == null) ? 0 : location.hashCode());
        result = PRIME * result + ((item == null) ? 0 : item.hashCode());
        return result;
    }

    @Override
    public int compareTo(ItemLoc o) {
         //return this.location.subtract(o.getLocation()).intValue();
       // return((this.location.equals(o.getLocation()) && this.item.equals(o.getItem()))? 0:1) ;
        
       if (this.getItem().equals(o.getItem())){
           if(this.location.equals(o.getLocation())){
               return 0;
           }else{
               return this.getLocation().compareTo(o.getLocation());
           }
           
       }else {
          return this.getItem().compareTo(o.getItem());
       }
    }
}
