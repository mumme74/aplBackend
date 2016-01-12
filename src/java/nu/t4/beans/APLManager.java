/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.t4.beans;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Base64;
import java.util.List;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.ws.rs.core.HttpHeaders;

/**
 *
 * @author Daniel Nilsson
 */
@Stateless
public class APLManager {

    public JsonArray getUser() {

        try {
            //koppla upp mot databas
            Class.forName("com.mysql.jdbc.Driver");
            com.mysql.jdbc.Connection conn = (com.mysql.jdbc.Connection) DriverManager.getConnection("jdbc:mysql://localhost/aplapp", "root", "");
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM user";
            ResultSet data = stmt.executeQuery(sql);

            //skapa jsonArray
            JsonArrayBuilder aplApp = Json.createArrayBuilder();

            while (data.next()) {
                aplApp.add(Json.createObjectBuilder()
                        .add("sub", data.getObject("sub").toString())
                        .add("namn", data.getObject("namn").toString())
                        .add("klass", data.getObject("klass").toString())
                        .add("larare", data.getObject("larare").toString())
                        .add("tfnr", data.getObject("tfnr").toString())
                        .add("email", data.getObject("email").toString())
                        .add("handledare", data.getObject("handledare").toString())
                        .build()
                );
            }
            //skicka tillbaka den
            conn.close();//Stänger kopplingen till databasen
            return aplApp.build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        //om det misslyckas skickas inget tillbaka
        return null;
    }

    public boolean registerUser(String googleID, String namn, int klass, int tfnr, String email) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = (Connection) DriverManager
                    .getConnection("jdbc:mysql://localhost/aplapp", "root", "");
            Statement stmt = conn.createStatement();
            String sql = String.format(
                    "INSERT INTO skolans_användare VALUES"
                            + "('%s',null,'%s',%d,'%s',%d,0,0,0)",
                    googleID, namn, tfnr, email, klass
            );
            stmt.executeUpdate(sql);

            conn.close();
            return true; //Matchen är tillagd
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false; //Matchen kunde ej läggas till
        }
    }

}
