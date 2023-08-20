package servlet;/*
    @author Dasun
*/

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
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


        String id = req.getParameter("id");
        String name = req.getParameter("name");
        String address = req.getParameter("address");
        String salary = req.getParameter("salary");

        try {

            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/shop","root","1234");

            PreparedStatement pstm = connection.prepareStatement("INSERT INTO customer VALUES (?,?,?,?)");
            pstm.setObject(1, id);
            pstm.setObject(2, name);
            pstm.setObject(3, address);
            pstm.setObject(4, salary);

            boolean b = pstm.executeUpdate()>0;

            if (b){


                resp.addHeader("Content-Type","application/json");
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                objectBuilder.add("state","ok");
                objectBuilder.add("message","successfully save");
                objectBuilder.add("data","");
                resp.getWriter().print(objectBuilder.build());


            }


        }catch (SQLException e){

            JsonObjectBuilder error = Json.createObjectBuilder();
            error.add("state","error");
            error.add("message",e.getLocalizedMessage());
            error.add("data","");

            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            resp.getWriter().print(error.build());


        }catch (ClassNotFoundException e){

            JsonObjectBuilder error = Json.createObjectBuilder();
            error.add("state","error");
            error.add("message",e.getLocalizedMessage());
            error.add("data","");
            resp.getWriter().print(error.build());
            resp.setStatus(500);
        }
    }
}
