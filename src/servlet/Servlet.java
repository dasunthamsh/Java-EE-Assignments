package servlet;/*
    @author Dasun
*/

import javax.json.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.sql.*;

@WebServlet(urlPatterns = "/customer")
public class Servlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");


            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/shop","root","1234");
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM customer");
            ResultSet rst = pstm.executeQuery();


            JsonArrayBuilder allCustomer = Json.createArrayBuilder();

            while (rst.next()){

                JsonObjectBuilder customer = Json.createObjectBuilder(); //  values tika json ekak widiyata bild karala denawa

                String id = rst.getString("id");
                String name = rst.getString("name");
                String address = rst.getString("address");
                String salary = rst.getString("salary");

                customer.add("id",id);
                customer.add("name",name);
                customer.add("address",address);
                customer.add("salary",salary);
                allCustomer.add(customer.build());

            }



            resp.addHeader("Content-Type","application/json");

            JsonObjectBuilder job = Json.createObjectBuilder();
            job.add("status","OK");
            job.add("message","successfully loaded");
            job.add("data",allCustomer.build());
            resp.getWriter().print(job.build());





        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String cusId = req.getParameter ("id");
        String cusName = req.getParameter ("name");
        String cusAddress = req.getParameter ("address");
        String salary = req.getParameter ("salary");

        PrintWriter writer = resp.getWriter ();


        try {
            Class.forName ("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection ("jdbc:mysql://localhost:3306/shop", "root", "1234");
            PreparedStatement pstm = connection.prepareStatement ("insert into customer values(?,?,?,?)");

            pstm.setObject (1, cusId);
            pstm.setObject (2, cusName);
            pstm.setObject (3, cusAddress);
            pstm.setObject (4, salary);
            if (pstm.executeUpdate () > 0) {

                resp.addHeader("Content-Type","application/json");
                JsonObjectBuilder cussAdd=Json.createObjectBuilder ();
                cussAdd.add ("state","200");
                cussAdd.add ("massage"," Customer Added Succuss");
                cussAdd.add ("data","");
                resp.setStatus (200);
                writer.print(cussAdd.build());

            }
        } catch (SQLException | ClassNotFoundException e) {

            resp.addHeader("Content-Type","application/json");
            JsonObjectBuilder obj=Json.createObjectBuilder ();
            obj.add ("state","");
            obj.add ("massage",e.getMessage ());
            obj.add ("data","");
            resp.setStatus (400);
            writer.print(obj.build());
        }

    }


    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        JsonReader reader = Json.createReader(req.getReader());
        JsonObject customer = reader.readObject();
        String id = customer.getString("id");
        String name = customer.getString("name");
        String address = customer.getString("address");
        String salary = customer.getString("salary");

        try {

            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/shop","root","1234");

            PreparedStatement pstm = connection.prepareStatement("UPDATE customer SET name=?,address=?,salary=? WHERE id =? ");
            pstm.setObject(4, id);
            pstm.setObject(1, name);
            pstm.setObject(2, address);
            pstm.setObject(3, salary);
            boolean b = pstm.executeUpdate() > 0;

            if(b){
                JsonObjectBuilder addCustomer = Json.createObjectBuilder(); // customer updated message
                addCustomer.add("state","ok");
                addCustomer.add("message","update successful");
                addCustomer.add("data","");

                resp.getWriter().print(addCustomer.build());

            }

        }catch (ClassNotFoundException | RuntimeException | SQLException e) {
            JsonObjectBuilder addCustomer = Json.createObjectBuilder(); // customer updated message
            addCustomer.add("state", "ok");
            addCustomer.add("message", e.getLocalizedMessage());
            addCustomer.add("data", "");
            resp.setStatus(500);

        }
    }


    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");
        String id = req.getParameter("id");
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/shop","root","1234");
            PreparedStatement pstm = connection.prepareStatement("DELETE FROM customer WHERE id = ?");
            pstm.setObject(1, id);
            boolean b = pstm.executeUpdate() > 0;

            if(b){
                JsonObjectBuilder deleteCustomer = Json.createObjectBuilder();
                deleteCustomer.add("state","ok");
                deleteCustomer.add("message","customer delete successful");
                deleteCustomer.add("data","");
            } else {
                throw new RemoteException("something wrong try agne");
            }

        }catch (RuntimeException e){
            JsonObjectBuilder deleteCustomer = Json.createObjectBuilder();
            deleteCustomer.add("state","ok");
            deleteCustomer.add("message","customer delete successful");
            deleteCustomer.add("data","");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print(deleteCustomer.build());

        }catch (ClassNotFoundException | SQLException e){
            JsonObjectBuilder deleteCustomer = Json.createObjectBuilder();
            deleteCustomer.add("state","ok");
            deleteCustomer.add("message","customer delete successful");
            deleteCustomer.add("data","");
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().print(deleteCustomer.build());
        }
    }
}
