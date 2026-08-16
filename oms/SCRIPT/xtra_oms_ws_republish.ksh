#!/bin/ksh
#####################################################################################
#        Name: xtra_oms_ws_publish.ksh
# Description: This script will call the Process webservice by executing 
#              OMS_MSG_WS_PUBLISHER package
#     Company: Logic Information Systems
#
#  Modification History:
#  Date           Name                    Modification Description
#  -------------  ---------------------   ------------------------
#  28-APR-2021    Imran Hemani        Initial version
#####################################################################################
# Script variables
#CONNECT=$UP
pgmName=`basename $0`
pgmName=${pgmName##*/}    # remove the path
pgmExt=${pgmName##*.}     # get the extension
pgmName=${pgmName%.*}     # get the program name
pgmPID=$$                 # get the process ID
exeDate=`date +"%h_%d"`   # get the execution date
LOGFILE="${MMHOME}/log/$pgmName.$exeDate.log"
ERRORFILE="${MMHOME}/error/err.$pgmName.$exeDate"
DAYERRORFILE="${MMHOME}/error/err_1.$pgmName.$exeDate"
OK=0
FATAL=255
NON_FATAL=1

USAGE="Usage: `basename $0`  <connect> "
#-------------------------------------------------------------------------
# Function Name: LOG_MESSAGE
# Purpose      : Log the  messages to the log file.
#-------------------------------------------------------------------------
function LOG_MESSAGE
{
logMsg=`echo $1`
logFunc=$2
retCode=$3
dtStamp=`date +"%a %b %e %T"`
echo "$dtStamp Program: $pgmName: PID=$pgmPID: $logMsg $logFunc" >> ${LOGFILE}
return $retCode
}

#-------------------------------------------------------------------------
# Function Name: OMS_REPROCESS
# Purpose      : This script will call the package for reprocess the webservice
#-------------------------------------------------------------------------
function OMS_WS_PROCESS
{
   echo "set serveroutput on
         set feedback off
         set heading off
         set trimspool on
         set pagesize 0

         BEGIN
            OMS_MSG_WS_PUBLISHER.PUBLISH_MESSAGES();
         END;
         /" | sqlplus -s ${CONNECT} > ${ERRORFILE}
}

#-----------------------------------------------
# Main program starts
# Parse the command line
#-----------------------------------------------

# Test for the number of input arguments
if [ $# -lt 1 ]
then
   echo ${USAGE}
   exit ${NON_FATAL}
fi

CONNECT=$1
dt=$2
echo ${dt}
export arg=${dt}

USER=${CONNECT%/*}

LOG_MESSAGE "Process Started..."

#Error file creation
touch ${ERRORFILE}
touch ${DAYERRORFILE}

$ORACLE_HOME/bin/sqlplus -s $UP <<-EOF >${ERRORFILE}
EOF


if [ `cat ${ERRORFILE} | wc -l` -gt 0 ]
then
   LOG_MESSAGE "Exiting due to ORA/LOGIN Error. Check error file "
    cat ${ERRORFILE}>> ${DAYERRORFILE}
    exit ${NON_FATAL};
else
    cat ${ERRORFILE}>> ${DAYERRORFILE}
    rm -f ${ERRORFILE}
fi

#Error file creation
touch ${ERRORFILE}
touch ${DAYERRORFILE}

LOG_MESSAGE "Started by ${USER}"

#Calls to the function based on the thread count
OMS_WS_PROCESS
if [[ $? -ne ${OK} ]]; then
   exit ${FATAL}
fi

# Check for any Oracle errors from the SQLPLUS process
if [ `grep "ORA-" ${ERRORFILE} | wc -l` -gt 0 ]
then
   LOG_MESSAGE "Exiting due to ORA Error. Check error file"
   cat ${ERRORFILE}>> ${DAYERRORFILE}
   exit ${NON_FATAL}
else
   cat ${ERRORFILE}>> ${LOGFILE}
   cat ${ERRORFILE}>> ${DAYERRORFILE}
   rm -f ${ERRORFILE}
fi

LOG_MESSAGE "Process Completed..."
exit 0

