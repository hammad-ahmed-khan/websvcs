create or replace PACKAGE OMSSUB_ASNOUT AS

----------------------------------------------------------------------------
   -- PACKAGE VARIABLES
----------------------------------------------------------------------------
   LP_cre_type       VARCHAR2(15) := 'asnoutcre';
----------------------------------------------------------------------------
----------------------------------------------------------------------------
-- Procedure Name: CONSUME
-- Purpose: This public procedure is to be called from the RIB.
----------------------------------------------------------------------------
PROCEDURE CONSUME(O_status_code       IN OUT  VARCHAR2,
                  O_error_message     IN OUT  VARCHAR2,
                  I_message           IN      RIB_OBJECT,
                  I_message_type      IN      VARCHAR2);
----------------------------------------------------------------------------
-- Procedure Name: CONSUME_SHIPMENT
-- Purpose: This public procedure is to be called from the dequeue process
--          (notify_asnout_message), the error retry process (ribapi_aq_reprocess_sql)
--          and from RFM's shipment consume function (fm_asnout_consume_sql.consume
--          and fm_auto_shipping_sql.consume).
----------------------------------------------------------------------------
PROCEDURE CONSUME_SHIPMENT(O_status_code       IN OUT  VARCHAR2,
                           O_error_message     IN OUT  VARCHAR2,
                           I_message           IN      RIB_OBJECT,
                           I_message_type      IN      VARCHAR2,
                           I_check_l10n_ind    IN      VARCHAR2);
----------------------------------------------------------------------------
-- Function Name: CONSUME_SHIPMENT
-- Purpose: This public function is the RMS base function called through the
--          L10N_SQL layer.
----------------------------------------------------------------------------
FUNCTION CONSUME_SHIPMENT (O_error_message     IN OUT  VARCHAR2,
                           IO_L10N_RIB_REC     IN OUT  L10N_OBJ)
RETURN BOOLEAN;
----------------------------------------------------------------------------
END OMSSUB_ASNOUT;