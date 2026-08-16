create or replace PACKAGE OMSSUB_RECEIVING AS
----------------------------------------------------------------------------
   -- PACKAGE VARIABLES
----------------------------------------------------------------------------
  -- LP_cre_type       VARCHAR2(15) := 'RECEIPTCRE';
----------------------------------------------------------------------------
--------------------------------------------------------------------------------
RECEIPT_ADD            CONSTANT  VARCHAR2(30) := 'receiptcre';
RECEIPT_ORDADD         CONSTANT  VARCHAR2(30) := 'receiptordcre';

PROCEDURE CONSUME (O_status_code          IN OUT  VARCHAR2,
                   O_error_message        IN OUT  VARCHAR2,
                   I_message              IN      RIB_OBJECT,
                   I_message_type         IN      VARCHAR2,
                   O_rib_otbdesc_rec         OUT  RIB_OBJECT,
                   O_rib_error_tbl           OUT  RIB_ERROR_TBL);

END OMSSUB_RECEIVING;