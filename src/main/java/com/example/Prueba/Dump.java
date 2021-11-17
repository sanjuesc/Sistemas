package com.example.Prueba;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

public class Dump extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        try (PrintWriter writer = response.getWriter()) {
            writer.println("<head>\n" +
                    "  <meta http-equiv=\"refresh\" content=\"5; URL=/Servlet\" />\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "  <p>If you are not redirected in five seconds, <a href=\"/Servlet\">click here</a>.</p>\n" +
                    "</body>");
        }
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
