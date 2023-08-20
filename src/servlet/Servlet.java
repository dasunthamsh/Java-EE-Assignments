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
import java.sql.*;

@WebServlet(urlPatterns = "/index")
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
}
