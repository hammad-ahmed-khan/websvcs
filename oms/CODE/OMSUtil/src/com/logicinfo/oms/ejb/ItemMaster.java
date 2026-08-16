package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "ITEM_MASTER")
@NamedQueries( { @NamedQuery(name = "ItemMaster.findAll", query = "select o from ItemMaster o"),
                 @NamedQuery(name = "ItemMaster.findStandardUom", query = "select o.standardUom from ItemMaster o where o.item=:item") ,
                 @NamedQuery(name = "ItemMaster.findDept", query = "select o.dept from ItemMaster o where o.item=:item"),
                 @NamedQuery(name = "ItemMaster.findItemDesc",query = "select o.itemDesc from ItemMaster o where o.item=:item"),
                 @NamedQuery(name = "ItemMaster.findItemStatus",query = "select o.status from ItemMaster o where o.item=:item"),
                 @NamedQuery(name="ItemMaster.findItemInventoryInd",query="select o.inventory_Ind from ItemMaster o where o.dept=:dept and o.item=:item"),
                 @NamedQuery(name = "ItemMaster.findItemDescSecondary",query = "select o.item_Desc_Secondary from ItemMaster o where o.item=:item")
                 })
public class ItemMaster implements Serializable 
{
    @Id
    @Column(name = "ITEM", nullable = false)
    private String item;
    @Column(name = "STANDARD_UOM", nullable = false)
    private String standardUom;
    @Column(name = "DEPT", nullable = false)
      private BigDecimal dept;
    @Column(name = "ITEM_DESC", nullable = false)
    private String itemDesc;
    @Column(name = "STATUS", nullable = false)
    private String status;
    @Column(name = "INVENTORY_IND", nullable = false)
        
    private String inventory_Ind;
    @Column(name = "ITEM_DESC_SECONDARY", nullable = false)
        
    private String item_Desc_Secondary ;
    public ItemMaster() 
    {
    }

    public String getItem()
    {
        return item;
    }

    public void setItem(String item)
    {

        this.item = item;
    }

    public String getStandardUom()
    {
        return standardUom;
    }

    public void setStandardUom(String standardUom) 
    {
        this.standardUom = standardUom;
    }

    public void setDept(BigDecimal dept)
    {
        this.dept = dept;
    }

    public BigDecimal getDept() 
    {
        return dept;
    }

    public void setItemDesc(String itemDesc)
    {
        this.itemDesc = itemDesc;
    }

    public String getItemDesc()
    {
        return itemDesc;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }
    
    public void setInventory_Ind(String inventory_Ind)
    {
        this.inventory_Ind = inventory_Ind;
    }

    public String getInventory_Ind()
    {
        return inventory_Ind;
    }
    
    public void setItem_Desc_Secondary(String item_Desc_Secondary)
    {
        this.item_Desc_Secondary = item_Desc_Secondary;
    }

    public String getItem_Desc_Secondary()
    {
        return item_Desc_Secondary;
    }
}
