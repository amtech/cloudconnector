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

public class XCDService {
	
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
    		
    		// int result = exports.getInt("EV_RESULT");
    	    int abapDuration = exports.getInt("EV_DURATION");
    	    
    		response.addHeader("Content-type", "application/json");
    		String resultString = "{ \"abapLayerDuration\": " + abapDuration + "}";
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
    		responseWriter.println("<h1>Exception occurred in Servlet Post method</h1>");
    		responseWriter.println("<pre>");
    		e.printStackTrace(responseWriter);
    		responseWriter.println("</pre>");
    		responseWriter.println("</body></html>");
    	}
	}
}
