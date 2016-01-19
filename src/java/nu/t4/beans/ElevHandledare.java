/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.t4.beans;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;

/**
 *
 * @author maikwagner
 */
@Stateless
public class ElevHandledare {

    public JsonArray getElev() {
        try {
            Connection conn = (Connection) DriverManager.getConnection("jdbc:mysql://10.97.72.5/aplapp", "aplapp", "Teknikum123");
            Statement stmt = (Statement) conn.createStatement();
            String sql = "SELECT * FROM skolans_användare WHERE behörighet=0";
            ResultSet data = stmt.executeQuery(sql);

            JsonArrayBuilder elever = Json.createArrayBuilder();

            while (data.next()) {
                elever.add(Json.createObjectBuilder()
                        .add("ID", data.getInt("ID"))
                        .add("namn", data.getString("namn"))
                        .add("Telefonnummer", data.getString("Telefonnummer"))
                        .add("email", data.getString("email"))
                        .add("klass", data.getInt("klass"))
                        .add("handledare_ID", data.getInt("handledare_ID"))
                        .add("senast_inloggad", data.getInt("senast_inloggad"))
                        .add("behörighet", false)
                        .build());
            }
    
            conn.close();
            return elever.build();
          
        } catch (Exception e) {
            System.out.println("elevhandledare - getElev()");
            System.out.println(e.getMessage());
            return null;
        }

    }
    public JsonArray getHandledare() {
        try {
            Connection conn = (Connection) DriverManager.getConnection("jdbc:mysql://10.97.72.5/aplapp", "aplapp", "Teknikum123");
            Statement stmt = (Statement) conn.createStatement();
            String sql = "SELECT * FROM handledare";
            ResultSet data = stmt.executeQuery(sql);

            JsonArrayBuilder handledare = Json.createArrayBuilder();

            while (data.next()) {
                handledare.add(Json.createObjectBuilder()
                        .add("ID", data.getInt("ID"))
                        .add("namn", data.getString("namn"))
                        .add("telefonnummer", data.getString("telefonnummer"))
                        .add("email", data.getString("email"))
                        .add("användarnamn", data.getString("användarnamn"))
                        .build());
            }
    
            conn.close();
            return handledare.build();
          
        } catch (Exception e) {
            System.out.println("elevhandledare - getHandledare()");
            System.out.println(e.getMessage());
            return null;
        }

    }
}
