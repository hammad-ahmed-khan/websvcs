package com.logicinfo.oms.model;
import java.math.BigDecimal;
public class ErrorListResponse
{
public ErrorListResponse()
{
  super();
}
public String item;
public BigDecimal cancelQtySuom;
public long lineNo;
public String messageCode;
public String messageDesc;
public void setItem(String item)
{
  this.item=item;
}
public String getItem()
{
  return item;
}
public void setCancelQtySuom(BigDecimal cancelQtySuom)
{
  this.cancelQtySuom=cancelQtySuom;
}
public BigDecimal getCancelQtySuom()
{
  return cancelQtySuom;
}
public void setLineNo(long lineNo)
{
  this.lineNo=lineNo;
}
public long getLineNo()
{
  return lineNo;
}
public void setMessageCode(String messageCode)
{
  this.messageCode=messageCode;
}
public String getMessageCode()
{
  return messageCode;
}
public void setMessageDesc(String messageDesc)
{
  this.messageDesc=messageDesc;
}
public String getMessageDesc()
{
  return messageDesc;
}
}
