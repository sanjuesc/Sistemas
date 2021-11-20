package com.example.Prueba;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;

public class Dump extends HttpServlet {

    String user = System.getenv("JDBC_USER");
    String pass = System.getenv("JDBC_PASS");
    String url = System.getenv("JDBC_URL");

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        try (PrintWriter writer = response.getWriter()) {
            writer.println("<head>\n" +
                    "  <meta http-equiv=\"refresh\" content=\"5; URL=/Prueba-1.0-SNAPSHOT/MyServlet\" />\n" +
                    "</head>\n" +
                    "<body>\n");
            System.out.println("vamos a hacer el dump");
            //https://stackoverflow.com/questions/5711084/java-runtime-getruntime-getting-output-from-executing-a-command-line-program
            Process process =Runtime.getRuntime().exec(new String[] {"sh", "-c", "mysqldump  --column-statistics=0 -h db -u root -ppassword SISTEMAS > /usr/local/tomcat/misArchivos/dump.sql"}); //no lo he probado con la linea esa, en teoria ahora funciona
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;

            System.out.printf("Output is:");
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            writer.println("  <p>If you are not redirected in five seconds, <a href=\"/Prueba-1.0-SNAPSHOT/MyServlet\">click here</a>.</p>\n" +
                    "</body>");
        }
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

}
