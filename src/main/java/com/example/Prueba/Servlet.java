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
        HttpSession session = request.getSession(false);
        if (session == null) { //si no tiene sesion comprobamos que tenga los parametros
            if(request.getParameter("uname") == null || request.getParameter("psw")==null ) { //si no tiene los parametros a tomar por culo
                request.getSession().setAttribute("pls", "si");
                request.getRequestDispatcher("index.jsp").forward(request, response);//si no tiene sesion le devolvemos
            }else{ //si tiene parametros miramos a ver si coinciden
                String usuario = request.getParameter("uname");
                String contra = request.getParameter("psw");
                System.out.println("aaaaaaaaaaaaaa");
                try{
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection con = DriverManager.getConnection(url,user,pass);
                    PreparedStatement st=con.prepareStatement("select name from user where name=? and pass = ?");
                    st.setString(1, usuario);
                    st.setString(2, contra);
                    System.out.println(st);
                    ResultSet rs = st.executeQuery();
                    con.close();
                    if(rs.next()==false){
                        request.getSession().setAttribute("incorrecto","si");
                        request.getRequestDispatcher("index.jsp").forward(request, response);
                    }else{
                        HttpSession sesionNueva = request.getSession();
                        cosas(response);
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }else{
                cosas(response);
            }
        }

    private void cosas(HttpServletResponse response) throws IOException {

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        try (PrintWriter writer = response.getWriter()) {
            if (!done) {
                try {
                    cargarCosas(writer);
                    done = true;
                } catch (SQLException | ClassNotFoundException throwables) {
                    throwables.printStackTrace();
                    done = false;
                }
            }
            writer.println("<!DOCTYPE html><html>");
            writer.println("<head>");
            writer.println("<meta charset=\"UTF-8\" />");
            writer.println("<title>MyServlet.java:doGet(): Servlet code!</title>");
            writer.println("</head>");
            writer.println("<body>");
            try {

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

    private void cargarCosas(PrintWriter writer) throws IOException, SQLException, ClassNotFoundException {
                String user = System.getenv("JDBC_USER");
                String pass = System.getenv("JDBC_PASS");
                String url = System.getenv("JDBC_URL");
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection(url,user,pass);
                writer.write("estamos en el metodo<br>");
                try {
                    File f = new File("/usr/local/tomcat/dump.sql"); // source path is the absolute path of dumpfile.
                    writer.write("file abierto<br>");
                    Statement stmt = con.createStatement();
                    BufferedReader bf = new BufferedReader(new FileReader(f));
                    String line = null,old="";
                    line = bf.readLine();
                    writer.write("vamos al while<br>");
                    while (line != null) {
                        if(line.endsWith(";")){
                            writer.write(old+line+"<br>");
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
                    writer.write(ex.getMessage());
                    ex.printStackTrace();
                }

    }


    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doGet(request, response);
    }
}
