package servlet;/*
    @author Dasun
*/

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

@WebServlet(urlPatterns = "/customer")
public class Servlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        String id = req.getParameter("id");
        String name = req.getParameter("customer");
        String address = req.getParameter("address");
        String salary = req.getParameter("salary");


        try {

            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/CustomerManagement","root","1234");

            PreparedStatement pstm = connection.prepareStatement("INSERT INTO customer VALUES (?,?,?,?)");
            pstm.setObject(1,id);
            pstm.setObject(2,name);
            pstm.setObject(3,address);
            pstm.setObject(4,salary);

            boolean b = pstm.executeUpdate()>0;


        }catch (Exception e){

        }
    }
}
