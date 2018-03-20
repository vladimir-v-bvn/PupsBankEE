package com.PupsBankEE.Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
//import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

//import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.*;

//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;

@WebServlet(description = "AjaxServlet01 For HomePageOffers", urlPatterns = { "/AjaxServlet01" })
public class AjaxServlet01 extends HttpServlet {
	private static final long serialVersionUID = 1L;

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

//  try {
//    Node n = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(request.getInputStream()).getFirstChild();
//    System.out.println(((Element)n).getElementsByTagName("CONTRACT_NUMBER").item(0).getTextContent());
//    System.out.println(((Element)n).getElementsByTagName("ACCOUNTING_ENTRIES_ID").item(0).getTextContent());
//    System.out.println(((Element)n).getElementsByTagName("DEBIT").item(0).getTextContent());
//    System.out.println(((Element)n).getElementsByTagName("CREDIT").item(0).getTextContent());
//    System.out.println(((Element)n).getElementsByTagName("AMOUNT").item(0).getTextContent());
//    System.out.println(((Element)n).getElementsByTagName("CURRENCY").item(0).getTextContent());
//    for (int i = 0; i < n.getChildNodes().getLength(); i++){
//      System.out.println(n.getChildNodes().item(i).getNodeName());
//      System.out.println(n.getChildNodes().item(i).getTextContent());
//    };
//  } catch (IOException | ParserConfigurationException | SAXException e) {
//    System.out.println(e);
//    System.out.println("xml.parse exception");
//  }

//    String xml = null;
//    try {
//      byte[] xmlData = new byte[request.getContentLength()];
//      InputStream sis = request.getInputStream();
//      BufferedInputStream bis = new BufferedInputStream(sis);
//      bis.read(xmlData, 0, xmlData.length);
//      if (request.getCharacterEncoding() != null) {
//        xml = new String(xmlData, request.getCharacterEncoding());
//      } else {
//        xml = new String(xmlData);
//      }
//    } catch (IOException ioe) {
//      System.out.println("IOException");
//    }
//    System.out.println(xml);

    response.setContentType("text/xml");
    response.setCharacterEncoding("UTF-8");
    PrintWriter out = response.getWriter();

    Context initContext;
    Connection conn = null;
	  
    try {
      initContext = new InitialContext();
      Context envContext  = (Context)initContext.lookup("java:/comp/env");
      DataSource ds = (DataSource)envContext.lookup("jdbc/myoracle");
      System.out.println("Oracle DataSource created");
      conn = ds.getConnection();
      System.out.println("Connected! localhost:1521:ORCL pups");
    } catch (NamingException e1) {
      System.out.println("No Oracle DataSource!");
      e1.printStackTrace();
      out.write("No Oracle DataSource!");
	} catch (SQLException e) {
      System.out.println("Connection Failed! localhost:1521:ORCL pups");
      e.printStackTrace();
      out.write("Connection Failed! localhost:1521:ORCL pups");
	}
 
    try {
      Node n = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(request.getInputStream()).getFirstChild();
      String updateSQL = "UPDATE PUPS.ACCOUNTING_ENTRIES t SET t.DEBIT = ?,t.CREDIT = ?,t.AMOUNT = ?,t.CURRENCY = ? WHERE t.CONTRACT_NUMBER = ? AND t.ACCOUNTING_ENTRIES_ID = ?";
//      String updateSQL1 = "UPDATE PUPS.ACCOUNTING_ENTRIES t SET t.DEBIT = '1001156',t.CREDIT = '1006652',t.AMOUNT = '5424422',t.CURRENCY = 'USD' WHERE t.CONTRACT_NUMBER = '436N-2343BCV' AND t.ACCOUNTING_ENTRIES_ID = '5'";
//      Statement stmt = conn.createStatement();
//      stmt.execute(updateSQL1);
      PreparedStatement pstmt = conn.prepareStatement(updateSQL);
      pstmt.setString(1, ((Element)n).getElementsByTagName("DEBIT").item(0).getTextContent());
      pstmt.setString(2, ((Element)n).getElementsByTagName("CREDIT").item(0).getTextContent());
      pstmt.setFloat(3, Float.parseFloat(((Element)n).getElementsByTagName("AMOUNT").item(0).getTextContent()));
      pstmt.setString(4, ((Element)n).getElementsByTagName("CURRENCY").item(0).getTextContent());
      pstmt.setString(5, ((Element)n).getElementsByTagName("CONTRACT_NUMBER").item(0).getTextContent());
      pstmt.setInt(6, Integer.parseInt(((Element)n).getElementsByTagName("ACCOUNTING_ENTRIES_ID").item(0).getTextContent()));
      pstmt.executeUpdate();
      pstmt.close();

      out.write("Record is updated");
    } catch (IOException | ParserConfigurationException | SAXException e) {
        System.out.println(e);
        System.out.println("xml.parse exception");
        out.write("xml.parse exception");
    } catch (SQLException e) {
        System.out.println(e.getErrorCode() + ' ' + e.getMessage());
        out.write(e.getErrorCode() + ' ' + e.getMessage());
        response.sendError(e.getErrorCode(), e.getMessage());
    }
}

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    response.setContentType("text/xml");
    response.setCharacterEncoding("UTF-8");
    PrintWriter out = response.getWriter();

    Context initContext;
    Connection conn = null;
	  
    try {
      initContext = new InitialContext();
      Context envContext  = (Context)initContext.lookup("java:/comp/env");
      DataSource ds = (DataSource)envContext.lookup("jdbc/myoracle");
      System.out.println("Oracle DataSource created");
      conn = ds.getConnection();
      System.out.println("Connected! localhost:1521:ORCL pups");
    } catch (NamingException e1) {
      System.out.println("No Oracle DataSource!");
      e1.printStackTrace();
	} catch (SQLException e) {
      System.out.println("Connection Failed! localhost:1521:ORCL pups");
      e.printStackTrace();
	}
 
    try {
      Statement stmt = conn.createStatement();
      ResultSet rs = stmt.executeQuery("select * from (select t.*, rownum as rn from PUPS.ACCOUNTING_ENTRIES t) WHERE rn BETWEEN 2 AND 7");
      String xmlArray = convertToXML(rs);
      out.write(xmlArray);
    } catch (SQLException e) {
      System.out.println("SQL exception");
      e.printStackTrace();
      } catch (Exception e) {
      System.out.println("JSON exception");
      e.printStackTrace();
	}
  }
  private String convertToXML(ResultSet resultSet) throws Exception {
    String xml = new String("<results>");
    while (resultSet.next()) {
      xml = xml + "<result>";
      for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {
        xml = xml + "<" + resultSet.getMetaData().getColumnLabel(i + 1) + ">" + resultSet.getObject(i + 1) + "</" + resultSet.getMetaData().getColumnLabel(i + 1) + ">";
        }
      xml = xml + "</result>";
      }
    xml = xml + "</results>";
    return xml;
  }

/* !!! JSON JNDI
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");
      PrintWriter out = response.getWriter();

      Context initContext;
	  Connection conn = null;
	  
	try {
        initContext = new InitialContext();
        Context envContext  = (Context)initContext.lookup("java:/comp/env");
        DataSource ds = (DataSource)envContext.lookup("jdbc/myoracle");
        System.out.println("Oracle DataSource created");
        conn = ds.getConnection();
        System.out.println("Connected! localhost:1521:ORCL pups");
	} catch (NamingException e1) {
        System.out.println("No Oracle DataSource!");
		e1.printStackTrace();
	} catch (SQLException e) {
        System.out.println("Connection Failed! localhost:1521:ORCL pups");
		e.printStackTrace();
	}
 
    try {
    	Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select * from (select t.*, rownum as rn from PUPS.ACCOUNTING_ENTRIES t) WHERE rn BETWEEN 2 AND 5");
        String jsonArray = convertToJSON(rs);
	    out.write(jsonArray);
	  } catch (SQLException e) {
		    System.out.println("SQL exception");
	        e.printStackTrace();
      } catch (Exception e) {
		    System.out.println("JSON exception");
            e.printStackTrace();
	}
	}
	private String convertToJSON(ResultSet resultSet) throws Exception {
      JSONArray jsonArray = new JSONArray();
        while (resultSet.next()) {
          JSONObject obj = new JSONObject();
          for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {
            obj.put(resultSet.getMetaData().getColumnLabel(i+1), resultSet.getObject(i + 1));
          }
          jsonArray.put(obj);
	    }
      return jsonArray.toString();
	}
!!! JSON JNDI
*/
	
/* !!! Direct connect to Oracle without JNDI and
   !!! Direct HTML data format passing to client
   
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

      response.setContentType("text/html;charset=UTF-8");
      PrintWriter out = response.getWriter();

	  try {
          Class.forName("oracle.jdbc.driver.OracleDriver");
          System.out.println("Oracle JDBC Driver is loaded");
          out.write("Oracle JDBC Driver is loaded</br>");
        } catch (ClassNotFoundException e) {
          System.out.println("No Oracle JDBC Driver");
          out.write("No Oracle JDBC Driver!");
          e.printStackTrace();
          return;
        }
	  Connection connection = null;
      try {
          connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:ORCL", "pups", "oracle");
          System.out.println("Connected! localhost:1521:ORCL pups");
          out.write("Connected! localhost:1521:ORCL pups</br>");
        } catch (SQLException e) {
          System.out.println("Connection Failed! localhost:1521:ORCL pups");
          out.write("Connection Failed! localhost:1521:ORCL pups");
          e.printStackTrace();
          return;
        }

      try {
    	Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("select * from (select t.*, rownum as rn from PUPS.ACCOUNTING_ENTRIES t) WHERE rn BETWEEN 2 AND 5");
        out.println("<table border=1");
        out.println("<tr><th>CONTRACT</th><th>ID</th><th>DEBIT</th><th>CREDIT</th><th>AMOUNT</th><th>CUR</th></tr>");
        while (rs.next()) {
            out.println("<tr><td>"
                    + rs.getString("CONTRACT_NUMBER") + "</td><td>"
                    + rs.getInt("ACCOUNTING_ENTRIES_ID") + "</td><td>"
                    + rs.getString("DEBIT") + "</td><td>"
                    + rs.getString("CREDIT") + "</td><td>"
                    + rs.getInt("AMOUNT") + "</td><td>"
                    + rs.getString("CURRENCY") + "</td></tr>"
            		); 
        }
        out.println("</table>");
	  } catch (SQLException e) {
	    System.out.println("SQL exception");
	    out.write("SQL exception");
        e.printStackTrace();
      }
      
      try {
         connection.close();
	   } catch (SQLException e) {
         System.out.println("Connection closing Failed!");
         out.write("Connection closing Failed!");
         e.printStackTrace();
	}
		
		
	  String text = "some ff text";
	  response.getWriter().write(text);
	}
!!! Direct connect to Oracle without JNDI and
!!! Direct HTML data format passing to client
*/
}
