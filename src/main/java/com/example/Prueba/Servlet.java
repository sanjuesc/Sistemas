package com.example.Prueba;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;


public class Servlet extends HttpServlet {

//String url = "jdbc:mysql://db:3306/example_db";
String user = System.getenv("JDBC_USER");
String pass = System.getenv("JDBC_PASS");
String url = System.getenv("JDBC_URL");
Boolean done = false;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        try (PrintWriter writer = response.getWriter()) {
            if (!done){
                try {
                    cargarCosas(writer);
                    done=true;
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                    done=false;
                }
            }
            writer.println("<!DOCTYPE html><html>");
            writer.println("<head>");
            writer.println("<meta charset=\"UTF-8\" />");
            writer.println("<title>MyServlet.java:doGet(): Servlet code!</title>");
            writer.println("</head>");
            writer.println("<body>");
            try{

                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(url,user,pass);
                PreparedStatement st=con.prepareStatement("select now()");
                ResultSet rs = st.executeQuery();
                if (rs.next()){
                    Date fecha = rs.getDate("now()");
                    writer.println("<h1>"+fecha+"</h1>");
                    writer.println("Fecha obtenida de la base de datos con el usuario " +System.getenv("JDBC_USER"));
                }else{
                    writer.println("La conexion y tal bien pero no habia nada en el rs bro");
                }
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
                writer.println(e.getMessage());
            }

            writer.println("<h1>This is a simple java servlet.</h1>");
            writer.println("</body>");
            writer.println("</html>");
        }
    }

    private void cargarCosas(PrintWriter writer) throws IOException, SQLException {
                String user = System.getenv("JDBC_USER");
                String pass = System.getenv("JDBC_PASS");
                String url = System.getenv("JDBC_URL");
                Connection con = DriverManager.getConnection(url,user,pass);
                writer.write("estamos en el metodo");
                try {
                    File f = new File("/usr/local/tomcat/dump.sql"); // source path is the absolute path of dumpfile.
                    writer.write("file abierto");
                    Statement stmt = con.createStatement();
                    stmt.executeUpdate("CREATE DATABASE sistemas");
                    stmt.executeUpdate("use sistemas");
                    BufferedReader bf = new BufferedReader(new FileReader(f));
                    String line = null,old="";
                    line = bf.readLine();
                    writer.write("vamos al while");
                    while (line != null) {
                        //q = q + line + "\n";
                        if(line.endsWith(";")){
                            stmt.executeUpdate(old+line);
                            old="";
                        }
                        else
                            old=old+"\n"+line;
                        line = bf.readLine();
                    }
                } catch (Exception ex) {
                    writer.write(ex.getMessage());
                    ex.printStackTrace();
                }

    }


    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request, response);
    }
}
