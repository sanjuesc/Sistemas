package com.example.Prueba;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;


public class Servlet extends HttpServlet {
String url = "jdbc:mysql://db:3306/example_db?connectTimeout=0&amp;socketTimeout=0&amp;autoReconnect=true";
String username = "user";
String password = "password";


    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        try (PrintWriter writer = response.getWriter()) {
            writer.println("<!DOCTYPE html><html>");
            writer.println("<head>");
            writer.println("<meta charset=\"UTF-8\" />");
            writer.println("<title>MyServlet.java:doGet(): Servlet code!</title>");
            writer.println("</head>");
            writer.println("<body>");
            try{

                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(url,username,password);
                PreparedStatement st=con.prepareStatement("select now()");
                ResultSet rs = st.executeQuery();
                Date fecha = rs.getDate("now()");
                writer.println("<h1>"+fecha+"</h1>");
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
                writer.println("<h1>aaaaaaaaaaaa</h1>");
            }

            writer.println("<h1>This is a simple java servlet.</h1>");
            writer.println("</body>");
            writer.println("</html>");
        }
    }


    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request, response);
    }
}
