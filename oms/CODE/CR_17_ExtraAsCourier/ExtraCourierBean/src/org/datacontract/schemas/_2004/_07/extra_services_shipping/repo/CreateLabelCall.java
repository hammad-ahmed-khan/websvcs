package org.datacontract.schemas._2004._07.extra_services_shipping.repo;



import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;

public class CreateLabelCall {
    public CreateLabelCall() {
        super();
    }
    
    public  ExtraCouierResponse    callWebService( String xmlInput , String userId){
        
        System.out.println("************** callWebService*************** ");
        ExtraCouierResponse executeResponseObj = new ExtraCouierResponse();
        Connection conn=null;
        String errorMessage= null;
        String airawaryBillNo = null;
/*      String proc3StoredProcedure = "{ call XX_MAKE_LABEL(?, ?, ?,?,?) }";*/
        String proc3StoredProcedure = "{ call XX_HB_MAKE_LABEL(?, ?, ?,?,?) }";
        String labeldataObj = null;
        Boolean packageResult= Boolean.TRUE;

        try {
            conn= OMSUtilDBConnection.createDBConnection("jdbc/wms");
            CallableStatement cs = conn.prepareCall(proc3StoredProcedure);
            cs.setString(1,userId);
            cs.setString(2, xmlInput);
            cs.registerOutParameter(3, java.sql.Types.CLOB);
            cs.registerOutParameter(4, java.sql.Types.VARCHAR);
            cs.registerOutParameter(5, java.sql.Types.VARCHAR);
            cs.execute();
            Clob labelDataClobObj = cs.getClob(3);
            labeldataObj = labelDataClobObj.getSubString(1, (int) labelDataClobObj.length());
             airawaryBillNo = cs.getString(4);
             errorMessage = cs.getString(5);

        } catch (Exception e) {
              packageResult =Boolean.FALSE;
        }

        
        executeResponseObj.setAirwaryBillno(airawaryBillNo);
        executeResponseObj.setErrorMessage(errorMessage);
        executeResponseObj.setLabelData(labeldataObj);
        executeResponseObj.setPackageResult(packageResult);
        
        System.out.println("************** End of the callWebService *************** ");
        
        System.out.println("Airway bill no"+executeResponseObj.getAirwaryBillno());
        
        System.out.println("Label data "+executeResponseObj.getLabelData());
        
        System.out.println(" error message is "+executeResponseObj.getErrorMessage());
        
        System.out.println("Package Result is "+executeResponseObj.getPackageResult());
        
        return executeResponseObj;
    }
    

    
    
}
