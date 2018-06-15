package com.sap.cloud.sample.connectivity;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.sap.conn.jco.AbapException;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoRepository;
import com.sap.service.XCDService;

public class ConnectivityServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String REGISTER_PRODUCT = "prodRegister";
    private static final String UPSELL_PRODUCT = "upsellMaterial";

    public ConnectivityServlet() {
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	String path = request.getContextPath(); // always /connectivity
    	String uri = request.getRequestURI();
    	if(uri.contains(REGISTER_PRODUCT)) {
    		XCDService.registerProduct(request, response);
    	}
    	else if( uri.contains(UPSELL_PRODUCT)) {
    		XCDService.getUpsellProduct(request, response);
    	}
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        XCDService.generateDummyResponse(response);
    }
}
