#!/bin/ksh
#############################################################################################################################################
#Script name : xtra_non_inv_item.ksh
#Description : The script will insert the ORD details for the confirmed orders, ORI and ORD for the received orders containing non inventory items.
#Developed By: Bhuvana
#version     : 1.0
#Date        : 19-FEB-2019
#############################################################################################################################################

LOGDIR=/integration/mesh360custom/log
ERRDIR=/integration/mesh360custom/error

############ Script variables###########
CONNECT=$1
scriptName=`basename $0`
scriptPID=$$
tDay=$(date '+%b_%d')
logFile=${LOGDIR}/$tDay.log
errorFile=${ERRDIR}/err.$(echo "$scriptName" | cut -f 1 -d '.').$tDay


date=`date +"%a %b %d %H:%M:%S"`
echo "$date Program: $scriptName: PID=${scriptPID}: Started by default user" >> ${logFile}

##################checking number of prompts#########################

if [ $# -ne 1 ] ; then
echo prompt
echo $#
echo "$date Program: $scriptName: PID=${scriptPID}: Aborted in Prompt verification" >> ${logFile}
exit -1
fi
#####################################################################

sqlplus -s ${CONNECT}  <<EOF>$errorFile

SET SERVEROUTPUT ON
set serveroutput on;

declare 
L_oms_cust_ord_no NUMBER(12);
L_fulfill_loc NUMBER(12);
L_LAST_UPDATE_DATETIME TIMESTAMP(6);
L_from_loc_type VARCHAR2(50);
L_line_no varchar2(50);
L_item varchar2(50);
L_qty_cancelled NUMBER(10);
L_fulfill_order_no NUMBER(10);


cursor get_oms_cust_ord_no  is 
select oms_cust_ord_no,order_create_reserve_ind
  from OMS_CUST_ORD_HEAD 
 where oms_cust_ord_no NOT IN (select oms_cust_ord_no 
                                 from oms_cust_ord_item  
                                where item in(select item 
                                                from item_master 
                                               where inventory_ind = 'Y')) 
   and STATUS ='S' 
   and ORD_PAYMENT_STATUS = 'S' 
   and CLOSE_DATETIME IS NULL 
order by CREATE_DATETIME;

cursor fulfill_detail(L_oms_cust_ord_no NUMBER )  is 
select ocf.FULFILL_LOC, ocf.LAST_UPDATE_DATETIME,ocf.SOURCE_LOC_TYPE , FULFILL_ORDER_NO 
from oms_co_fulfill_detail ocf , item_master im 
where im .item = ocf.item 
and im.INVENTORY_IND='Y' 
and ((SOURCE_LOC=FULFILL_LOC) or (FULFILL_LOC<>SOURCE_LOC and FULFILL_LOC_TYPE='V'))
and rownum='1'
and oms_cust_ord_no=L_oms_cust_ord_no ;

cursor getnoninv_item(L_oms_cust_ord_no NUMBER) is 
select oci.line_no , oci.item , QTY_ORDERED_SUOM from oms_cust_ord_item  oci , item_master im 
where im .item = oci.item 
and im.INVENTORY_IND='N' 
and oms_cust_ord_no=L_oms_cust_ord_no ;

cursor getrtlog_details(L_oms_cust_ord_no NUMBER) is
select oms_rtlog_pub_seq_no from oms_rtlog_publish_log
where oms_cust_ord_no = L_oms_cust_ord_no;

cursor getqty_cancelled_val(L_oms_cust_ord_no NUMBER, L_line_no NUMBER, L_item VARCHAR2) is
select qty_cancelled from 
oms_cust_ord_item 
where oms_cust_ord_no = L_oms_cust_ord_no
and line_no = L_line_no
and item = L_item;

begin 
FOR oms_cust_ord_rec IN get_oms_cust_ord_no
loop 
dbms_output.put_line('OmsCustOrdNo'||oms_cust_ord_rec.oms_cust_ord_no);
open fulfill_detail(oms_cust_ord_rec.oms_cust_ord_no);
fetch fulfill_detail into L_fulfill_loc,L_LAST_UPDATE_DATETIME,L_from_loc_type,L_fulfill_order_no;
close fulfill_detail;
dbms_output.put_line('OmsCustOrdNo'||oms_cust_ord_rec.oms_cust_ord_no||'L_fulfill_loc'||L_fulfill_loc||'L_LAST_UPDATE_DATETIME'||L_LAST_UPDATE_DATETIME||'L_from_loc_type'||L_from_loc_type||'L_fulfill_order_no'||L_fulfill_order_no);

FOR  oms_cust_ord_rec_item  IN getnoninv_item(oms_cust_ord_rec.oms_cust_ord_no)
LOOP
dbms_output.put_line('OmsCustOrdNo'||oms_cust_ord_rec.oms_cust_ord_no||'Line_no'||oms_cust_ord_rec_item.LINE_NO ||'L_item'||oms_cust_ord_rec_item.ITEM ||'L_order_qty'||oms_cust_ord_rec_item.QTY_ORDERED_SUOM);

open getqty_cancelled_val(oms_cust_ord_rec.oms_cust_ord_no,oms_cust_ord_rec_item.LINE_NO,oms_cust_ord_rec_item.ITEM);
fetch getqty_cancelled_val into L_qty_cancelled;
close getqty_cancelled_val;

if (L_qty_Cancelled = 0) then 

dbms_output.put_line('not cancelled');

UPDATE OMS_CUST_ORD_ITEM
               SET CUM_QTY_DELIVERED = nvl(CUM_QTY_DELIVERED,0) + QTY_ORDERED_SUOM,
                   LAST_UPDATE_DATETIME = SYSDATE
             WHERE oms_cust_ord_no = oms_cust_ord_rec.oms_cust_ord_no
               AND item = oms_cust_ord_rec_item.item  
               AND line_no = oms_cust_ord_rec_item.line_no 
               AND NVL(CUM_QTY_DELIVERED,0) = 0;
 
------Commented below part to avoid ORI duplicate OMS_RTLOG_PUBLISH_LOG table  
/* if oms_cust_ord_rec.order_create_reserve_ind = 'R' then
                 -- Insert ORI into OMS_RTLOG_PUBLISH_LOG  
             INSERT INTO OMS_RTLOG_PUBLISH_LOG(OMS_RTLOG_PUB_SEQ_NO,
                                              OMS_CUST_ORD_NO,
                                              FULFILL_ORDER_NO,
                                              ITEM,
                                              LINE_NO,
                                              QTY,
                                              LOCATION,
                                              TRAN_TYPE,
                                              PUBLISHED_IND,
                                              ERROR_MESSAGE,
                                              CREATE_DATETIME,
                                              LAST_UPDATE_DATETIME,
                                              OMS_CANCEL_ID)
                                       VALUES(OMS_RTLOG_PUB_SEQ_NO_SEQ.nextval,
                                              oms_cust_ord_rec.oms_cust_ord_no,
                                              L_fulfill_order_no,
                                              oms_cust_ord_rec_item.ITEM ,
                                              oms_cust_ord_rec_item.LINE_NO,
                                              oms_cust_ord_rec_item.QTY_ORDERED_SUOM,
                                              '19010',
                                              'ORI',
                                              'N',
                                              NULL,
                                              SYSDATE,
                                              SYSDATE,
                                              NULL); 
                -- Insert into OMS_RTLOG_PUBLISH_LOG
            INSERT INTO OMS_RTLOG_PUBLISH_LOG(OMS_RTLOG_PUB_SEQ_NO,
                                              OMS_CUST_ORD_NO,
                                              FULFILL_ORDER_NO,
                                              ITEM,
                                              LINE_NO,
                                              QTY,
                                              LOCATION,
                                              TRAN_TYPE,
                                              PUBLISHED_IND,
                                              ERROR_MESSAGE,
                                              CREATE_DATETIME,
                                              LAST_UPDATE_DATETIME,
                                              OMS_CANCEL_ID)
                                       VALUES(OMS_RTLOG_PUB_SEQ_NO_SEQ.nextval,
                                              oms_cust_ord_rec.oms_cust_ord_no,
                                              L_fulfill_order_no,
                                              oms_cust_ord_rec_item.ITEM ,
                                              oms_cust_ord_rec_item.LINE_NO,
                                              oms_cust_ord_rec_item.QTY_ORDERED_SUOM,
                                              19010,
                                             'ORD',
                                              'N',
                                              NULL,
                                              SYSDATE,
                                              SYSDATE,
                                              NULL);
else
                -- Insert into OMS_RTLOG_PUBLISH_LOG
            INSERT INTO OMS_RTLOG_PUBLISH_LOG(OMS_RTLOG_PUB_SEQ_NO,
                                              OMS_CUST_ORD_NO,
                                              FULFILL_ORDER_NO,
                                              ITEM,
                                              LINE_NO,
                                              QTY,
                                              LOCATION,
                                              TRAN_TYPE,
                                              PUBLISHED_IND,
                                              ERROR_MESSAGE,
                                              CREATE_DATETIME,
                                              LAST_UPDATE_DATETIME,
                                              OMS_CANCEL_ID)
                                       VALUES(OMS_RTLOG_PUB_SEQ_NO_SEQ.nextval,
                                              oms_cust_ord_rec.oms_cust_ord_no,
                                              L_fulfill_order_no,
                                              oms_cust_ord_rec_item.ITEM ,
                                              oms_cust_ord_rec_item.LINE_NO,
                                              oms_cust_ord_rec_item.QTY_ORDERED_SUOM,
                                              19010,
                                             'ORD',
                                              'N',
                                              NULL,
                                              SYSDATE,
                                              SYSDATE,
                                              NULL);
END IF;
*/
--------Added the below part because only ORD value is required
INSERT INTO OMS_RTLOG_PUBLISH_LOG(OMS_RTLOG_PUB_SEQ_NO,
                                              OMS_CUST_ORD_NO,
                                              FULFILL_ORDER_NO,
                                              ITEM,
                                              LINE_NO,
                                              QTY,
                                              LOCATION,
                                              TRAN_TYPE,
                                              PUBLISHED_IND,
                                              ERROR_MESSAGE,
                                              CREATE_DATETIME,
                                              LAST_UPDATE_DATETIME,
                                              OMS_CANCEL_ID)
                                       VALUES(OMS_RTLOG_PUB_SEQ_NO_SEQ.nextval,
                                              oms_cust_ord_rec.oms_cust_ord_no,
                                              L_fulfill_order_no,
                                              oms_cust_ord_rec_item.ITEM ,
                                              oms_cust_ord_rec_item.LINE_NO,
                                              oms_cust_ord_rec_item.QTY_ORDERED_SUOM,
                                              19010,
                                             'ORD',
                                              'N',
                                              NULL,
                                              SYSDATE,
                                              SYSDATE,
                                              NULL);
UPDATE OMS_CUST_ORD_HEAD ocoh
                  SET CLOSE_DATETIME = sysdate
                   WHERE NOT EXISTS (SELECT 1
                                       FROM oms_cust_ord_item ocoi
                                      WHERE NVL(ocoi.QTY_ORDERED_SUOM,0) > NVL(ocoi.CUM_QTY_DELIVERED,0) + NVL(ocoi.QTY_CANCELLED,0)
                                        AND ocoi.OMS_CUST_ORD_NO = oms_cust_ord_rec.oms_cust_ord_no)
                     AND ocoh.OMS_CUST_ORD_NO = oms_cust_ord_rec.oms_cust_ord_no;
                     
else
dbms_output.put_line('already cancelled');
end if; 

END LOOP;

 


                     
--update awaiting_shipment_ECOMM_ord set flag='Y' where oms_cust_ord_no=oms_cust_ord_rec.oms_cust_ord_no;

end loop;
commit;

EXCEPTION
WHEN OTHERS THEN
DBMS_OUTPUT.PUT_LINE(SQLERRM);

END;
/
EOF


errCheck=$(grep 'ORA' $errorFile | wc -l)

if [ ${errCheck} -ne 0 ]
  then
  echo "$date Program: $scriptName: PID=${scriptPID}: Aborted. Please check the error file." >> ${logFile}
  exit -1
else
   echo "$date Program: $scriptName: PID=${scriptPID}: Terminated Successfully" >> ${logFile}
   rm $errorFile
   exit 0
fi
