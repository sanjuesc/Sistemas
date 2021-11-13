<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.DriverManager" %>
<%@ page import="java.io.File" %>
<%@ page import="java.io.BufferedReader" %>
<%@ page import="java.io.FileReader" %>
<%@ page import="java.sql.Statement" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>JSP - Hello World</title>
</head>
<body>



<%
    Runtime.getRuntime().exec("/bin/bash -c wget https://raw.githubusercontent.com/sanjuesc/Sistemas/master/dump.sql");
    String user = System.getenv("JDBC_USER");
    String pass = System.getenv("JDBC_PASS");
    String url = System.getenv("JDBC_URL");
    Connection con = DriverManager.getConnection(url,user,pass);
/* Note that con is a connection to database, and not the server.
if You have a connection to the server, the first command in the dumpfile should be the
USE db_name; */
    //String q = "";
    try {
        File f = new File("dump.sql"); // source path is the absolute path of dumpfile.
        Statement stmt = con.createStatement();
        stmt.executeUpdate("CREATE DATABASE sistemas");
        stmt.executeUpdate("user sistemas");
        BufferedReader bf = new BufferedReader(new FileReader(f));
        String line = null,old="";
        line = bf.readLine();
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
        ex.printStackTrace();
    }
%>

<h1><%= "Hello World!" %>
</h1>
<br/>
<a href="MyServlet">Hello Servlet</a>
</body>
</html>