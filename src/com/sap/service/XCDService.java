package com.sap.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Base64;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.sap.conn.jco.AbapException;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoRepository;
import com.sap.conn.jco.JCoTable;

public class XCDService {
	static private final String ABAP_DURATION = "abapLayerDuration";
	static private final String UPSELL_PRODUCT = "upsellProducts";
	static private final String PRODUCT_IMAGES = "ProductImages";
	static private final String PRODUCT_ID = "productID";
	static private final String PRODUCT_TEXT = "productText";
	
	static private final String FILE_ID = "fileID";
	static private final String FILE_OWNER = "fileOwner";
	static private final String FILE_CDATE = "fileCreationDate";
	static private final String FILE_NAME = "fileName";
	static private final String FILE_CONTENT = "fileContent";
	
	static private final String RESULT = "result";
	static private final String MESSAGE = "message";
	
	public static void generateDummyResponse(HttpServletResponse response) {
    	PrintWriter responseWriter;

		try {
			responseWriter = response.getWriter();
			response.addHeader("Content-type", "text/html");
	        responseWriter.println("<html><body>");
	        responseWriter.println("<h1>Jerry dummy response</h1>");
	        responseWriter.println("</body></html>");
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	
	static private String getFileContent(JCoTable codes) {
		InputStream is = codes.getBinaryStream("FILECONTENT");
		String result = "";
    	try {

    		byte[] bytes = new byte[is.available()];
    		is.read(bytes);
    		
    		result = Base64.getEncoder().encodeToString(bytes);
    		is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return result;
	}
	
	static public void getProductImages(HttpServletRequest request, HttpServletResponse response) {
		PrintWriter responseWriter = null;
		try {
    		responseWriter = response.getWriter();
    		JCoDestination destination = JCoDestinationManager.getDestination("my-backend-system-destination");

    		JCoRepository repo = destination.getRepository();
    		JCoFunction stfcConnection = repo.getFunction("ZDIS_GET_MATERIAL_IMAGES");

    		JCoParameterList imports = stfcConnection.getImportParameterList();
        
    		String productID = request.getHeader("productID");

    		imports.setValue("IV_MATERIAL_ID", productID);

    		stfcConnection.execute(destination);
        
    		JCoParameterList exports = stfcConnection.getExportParameterList();
    		
    		int abapDuration = exports.getInt("EV_DURATION");
    		response.addHeader("Content-type", "application/json");
    	    
    	    StringBuilder sb = new StringBuilder();
    	    sb.append("{ \"" + ABAP_DURATION + "\": " + abapDuration + ",");
    	    
    	    sb.append("\"" + PRODUCT_IMAGES + "\":[");
    	    
    	    JCoTable codes = exports.getTable("ET_IMAGES");
    	    
    	    int row = codes.getNumRows();
    	    
    	    for( int i = 0; i < row; i++){
    	    	codes.setRow(i);
                sb.append("{\"" + FILE_ID   + "\":\"" + codes.getString("FILEID") + "\"" + ","
                		+ "\"" + FILE_OWNER + "\":\"" + codes.getString("OWNER")  + "\"" + ",");
                sb.append("\"" + FILE_CDATE + "\":\"" + codes.getString("CREATION_DATE") + "\"" + ","
                		+ "\"" + FILE_NAME    + "\":\"" + codes.getString("FILENAME") + "\"" + "," 
                		+ "\"" + FILE_CONTENT + "\":\"" + getFileContent(codes) + "\""); 
                
                if( i < row - 1){
                	sb.append("},");
                }
                else{
                	sb.append("}");
                }
    	    }
    	    sb.append("]}");
    	    
    		responseWriter.println(sb.toString());
    	}

    	catch (AbapException ae){
    	}
    	catch (Exception e){
    		printJCOError(e, responseWriter, response, "getProductImages");
    	}
    }
	
	static public void submitSurvey(HttpServletRequest request, HttpServletResponse response) {
		PrintWriter responseWriter = null;
		try {
    		responseWriter = response.getWriter();
    		JCoDestination destination = JCoDestinationManager.getDestination("my-backend-ag3");

    		JCoRepository repo = destination.getRepository();
    		JCoFunction stfcConnection = repo.getFunction("ZDIS_SUBMIT_QUESTION");

    		JCoParameterList imports = stfcConnection.getImportParameterList();
    		String answer = request.getHeader("answer");

    		imports.setValue("IV_ANSWER_LIST", answer);

    		stfcConnection.execute(destination);
        
    		JCoParameterList exports = stfcConnection.getExportParameterList();
    		
    		int abapDuration = exports.getInt("EV_DURATION");
    		int result = exports.getInt("EV_RESULT");
    		String message = exports.getString("EV_MESSAGE");
    		response.addHeader("Content-type", "application/json");
    	    
    	    StringBuilder sb = new StringBuilder();
    	    sb.append("{ \"" + ABAP_DURATION + "\": " + abapDuration + ",");
    	    
    	    sb.append("\"" + RESULT  + "\":\"" + result + "\",");
    	    
    	    sb.append("\"" + MESSAGE + "\":\"" + message + "\"}");
    	    
    		responseWriter.println(sb.toString());
    	}

    	catch (AbapException ae){
    	}
    	catch (Exception e){
    		printJCOError(e, responseWriter, response, "submitSurvey");
    	}
    }
	
	static private void printJCOError(Exception e, PrintWriter responseWriter, HttpServletResponse response, String methodName ) {
		response.addHeader("Content-type", "text/html");
		responseWriter.println("<html><body>");
		responseWriter.println("<h1>Exception occurred in method: " + methodName + "</h1>");
		responseWriter.println("<pre>");
		e.printStackTrace(responseWriter);
		responseWriter.println("</pre>");
		responseWriter.println("</body></html>");
	}
	
	static public void registerProduct(HttpServletRequest request, HttpServletResponse response) {
		PrintWriter responseWriter = null;
		try {
    		responseWriter = response.getWriter();
    		JCoDestination destination = JCoDestinationManager.getDestination("my-backend-system-destination");

    		JCoRepository repo = destination.getRepository();
    		JCoFunction stfcConnection = repo.getFunction("ZDIS_EQUIPMENT_REGISTER");

    		JCoParameterList imports = stfcConnection.getImportParameterList();
        
    		String serialNumber = request.getHeader("serial_number");
    		String wechatOpenID = request.getHeader("wechat_openid");

    		imports.setValue("IV_SERIAL_NUMBER", serialNumber);
    		imports.setValue("IV_OPEN_ID", wechatOpenID);

    		stfcConnection.execute(destination);
        
    		JCoParameterList exports = stfcConnection.getExportParameterList();
    		
    		int result = exports.getInt("EV_RESULT");
    		int abapDuration = exports.getInt("EV_DURATION");
    		response.addHeader("Content-type", "application/json");
    		String resultString = "{ \"result\": " + result + 
    				", \"abapLayerDuration\": " + abapDuration + "}";
    		responseWriter.println(resultString);
    	}

    	catch (AbapException ae){
        //just for completeness: As this function module does not have an exception
        //in its signature, this exception cannot occur. However,you should always
        //take care of AbapExceptions
    	}
    	catch (Exception e){
    		printJCOError(e, responseWriter, response, "registerProduct");
    	}
    }

	static public void getUpsellProduct(HttpServletRequest request, HttpServletResponse response) {
		PrintWriter responseWriter = null;
		try {
    		responseWriter = response.getWriter();
    		
    		JCoDestination destination = JCoDestinationManager.getDestination("my-backend-system-destination");

    		JCoRepository repo = destination.getRepository();
    		JCoFunction stfcConnection = repo.getFunction("ZDIS_GET_UPSELL_MATERIALS");

    		JCoParameterList imports = stfcConnection.getImportParameterList();
        
    		String customerID = request.getHeader("open_id");
    		String materialID = request.getHeader("material_id");

    		imports.setValue("IV_OPEN_ID", customerID);
    		imports.setValue("IV_MATERIAL_ID", materialID);

    		stfcConnection.execute(destination);
        
    		JCoParameterList exports = stfcConnection.getExportParameterList();
    		
    	    int abapDuration = exports.getInt("EV_DURATION");
    	    
    	    StringBuilder sb = new StringBuilder();
    	    sb.append("{ \"" + ABAP_DURATION + "\": " + abapDuration + ",");
    	    
    	    sb.append("\"" + UPSELL_PRODUCT + "\":[");
    	    
    	    JCoTable codes = exports.getTable("ET_MATERIALS");
    	    
    	    int row = codes.getNumRows();
  
    	    for( int i = 0; i < row; i++){
    	    	codes.setRow(i);
                System.out.println(codes.getString("MATERIAL_ID") + '\t' + codes.getString("MATERIAL_TEXT"));
                sb.append("{\"" + PRODUCT_ID + "\":" + codes.getString("MATERIAL_ID") + ","
                		+ "\"" + PRODUCT_TEXT + "\":\"" + codes.getString("MATERIAL_TEXT") + "\"");
                if( i < row - 1){
                	sb.append("},");
                }
                else{
                	sb.append("}");
                }
    	    }
    	    sb.append("]}");
    	    
    	    System.out.println("Final json: " + sb.toString());
    	    
    		responseWriter.println(sb.toString());
    	}
    	catch (AbapException ae){
    	}
    	catch (Exception e){
    		printJCOError(e, responseWriter, response, "getUpsellProduct");
    	}
	}
}
