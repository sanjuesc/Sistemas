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

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Cookie c[]=request.getCookies();
        if (c.length<=1) { //mira me da pereza, si no tiene cookies fuera si las tiene dentro
            if(request.getParameter("uname") == null || request.getParameter("psw")==null ) { //si no tiene los parametros a tomar por culo
                response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/?badLogin=true"));
            }else{ //si tiene parametros miramos a ver si coinciden
                System.out.println("tienes los parametros");
                if (!done) {
                    try {
                        cargarCosas();
                        done = true;
                    } catch (SQLException | ClassNotFoundException throwables) {
                        throwables.printStackTrace();
                        done = false;
                    }
                }
                String usuario = request.getParameter("uname");
                String contra = request.getParameter("psw");
                System.out.println("aaaaaaaaaaaaaa");
                try{
                    System.out.println("venga a intenar loggear");
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection con = DriverManager.getConnection(url,user,pass);
                    PreparedStatement st=con.prepareStatement("select name from user where name=? and pass = ?");
                    st.setString(1, usuario);
                    st.setString(2, contra);
                    ResultSet rs = st.executeQuery();
                    if(!rs.next()){
                        request.setAttribute("a", "b");
                        response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/?badLogin=true\""));
                    }else{
                        System.out.println("2");
                        Cookie c1=new Cookie("userName",usuario);
                        response.addCookie(c1);
                        cosas(response, request);
                    }
                } catch (ClassNotFoundException e) {
                    System.out.println(e.getMessage());
                } catch (SQLException throwables) {
                    System.out.println(throwables.getMessage());
                }
            }
        }else{
                cosas(response, request);
            }
        }

    private void cosas(HttpServletResponse response, HttpServletRequest request) throws IOException {
        System.out.println("hemos entrado a cosas");
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        try (PrintWriter writer = response.getWriter()) {
            writer.println("<!DOCTYPE html><html>");
            writer.println("<head>");
            writer.println("<meta charset=\"UTF-8\" />");
            writer.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\"/>");
            writer.println("<title>MyServlet.java:doGet(): Servlet code!</title>");
            writer.println("</head>");
            writer.println("<body>");
            try {
                writer.println("<button class=\"accordion\">Section 1</button>\n" +
                        "<div class=\"panel\">\n" +
                        "  <p>Lorem ipsum...</p>\n" +
                        "</div>");
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(url, user, pass);
                PreparedStatement st = con.prepareStatement("select now()");
                ResultSet rs = st.executeQuery();
                if (rs.next()) {
                    Date fecha = rs.getDate("now()");
                    writer.println("<h1>" + fecha + "</h1>");
                    writer.println("Fecha obtenida de la base de datos con el usuario " + System.getenv("JDBC_USER"));
                } else {
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

    private void cargarCosas() throws IOException, SQLException, ClassNotFoundException {
                System.out.println("cargamos todo");
                String user = System.getenv("JDBC_USER");
                String pass = System.getenv("JDBC_PASS");
                String url = System.getenv("JDBC_URL");
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(url,user,pass);
                try {
                    File f = new File("/usr/local/tomcat/dump.sql"); // source path is the absolute path of dumpfile.
                    Statement stmt = con.createStatement();
                    BufferedReader bf = new BufferedReader(new FileReader(f));
                    String line = null,old="";
                    line = bf.readLine();
                    while (line != null) {
                        if(line.endsWith(";")){
                            stmt.executeUpdate(old+line);
                            old="";
                        }
                        else
                            if(!line.startsWith("--")){
                                old=old+"\n"+line;
                        }

                        line = bf.readLine();
                    }
                    con.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

    }


    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doGet(request, response);
    }
}
