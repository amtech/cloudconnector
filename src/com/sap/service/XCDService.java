package com.sap.service;

import java.io.IOException;
import java.io.PrintWriter;
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
    		String resultString = "{ \"abapLayerDuration\": " + abapDuration + "}";
    		responseWriter.println(resultString);
    	    
    	    StringBuilder sb = new StringBuilder();
    	    sb.append("{ \"" + ABAP_DURATION + "\": " + abapDuration + ",");
    	    
    	    sb.append("\"" + PRODUCT_IMAGES + "\":[");
    	    
    	    JCoTable codes = exports.getTable("ET_IMAGES");
    	    
    	    int row = codes.getNumRows();
    	    System.out.println("Total rows: " + row);
    	    
    	    System.out.println("ABAP duration: " + abapDuration);
    	    
    	    for( int i = 0; i < row; i++){
    	    	codes.setRow(i);
                sb.append("{\"" + FILE_ID + "\":" + codes.getString("FILEID") + ","
                		+ "\"" + FILE_OWNER + "\":\"" + codes.getString("OWNER") + "\"" + ",");
                sb.append("{\"" + FILE_CDATE + "\":" + codes.getString("CREATION_DATE") + ","
                		+ "\"" + FILE_NAME + "\":\"" + codes.getString("FILENAME") + "\""); 
                
                storeLocalFile(codes);
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
        //just for completeness: As this function module does not have an exception
        //in its signature, this exception cannot occur. However,you should always
        //take care of AbapExceptions
    	}
    	catch (Exception e){
    		response.addHeader("Content-type", "text/html");
    		responseWriter.println("<html><body>");
    		responseWriter.println("<h1>Exception occurred in RegisterProduct method</h1>");
    		responseWriter.println("<pre>");
    		e.printStackTrace(responseWriter);
    		responseWriter.println("</pre>");
    		responseWriter.println("</body></html>");
    	}
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
    		response.addHeader("Content-type", "text/html");
    		responseWriter.println("<html><body>");
    		responseWriter.println("<h1>Exception occurred in RegisterProduct method</h1>");
    		responseWriter.println("<pre>");
    		e.printStackTrace(responseWriter);
    		responseWriter.println("</pre>");
    		responseWriter.println("</body></html>");
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
        
    		String customerID = request.getHeader("customer_id");
    		String materialID = request.getHeader("material_id");

    		imports.setValue("IV_CUSTOMER_ID", customerID);
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
        //just for completeness: As this function module does not have an exception
        //in its signature, this exception cannot occur. However,you should always
        //take care of AbapExceptions
    	}
    	catch (Exception e){
    		response.addHeader("Content-type", "text/html");
    		responseWriter.println("<html><body>");
    		responseWriter.println("<h1>Exception occurred in Servlet Post method</h1>");
    		responseWriter.println("<pre>");
    		e.printStackTrace(responseWriter);
    		responseWriter.println("</pre>");
    		responseWriter.println("</body></html>");
    	}
	}
}
