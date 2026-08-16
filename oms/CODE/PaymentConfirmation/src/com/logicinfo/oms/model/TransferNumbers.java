package com.logicinfo.oms.model;


import java.util.HashSet;

public class TransferNumbers
{
   
    private HashSet<String> transferSet;
    
    private String tsfNo;

    
    public void setTransferSet(HashSet<String> transferSet)
    {
	this.transferSet=transferSet;
    }

    public  HashSet<String> getTransferSet()
    {
	return transferSet;
    }

    public void setTsfNo(String tsfNo)
    {
	this.tsfNo=tsfNo;
    }

    public String getTsfNo()
    {
	return tsfNo;
    }
}
