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
            Connection conn = ConnectionFactory.getConnection();
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
            Connection conn = ConnectionFactory.getConnection();
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
                        .add("program_id", data.getInt("program_id"))
                        .add("företag", data.getString("företag"))
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
    public JsonArray getProgram() {
        try {
            Connection conn = ConnectionFactory.getConnection();
            Statement stmt = (Statement) conn.createStatement();
            String sql = "SELECT * FROM handledareprogram";
            ResultSet data = stmt.executeQuery(sql);

            JsonArrayBuilder program = Json.createArrayBuilder();

            while (data.next()) {
                program.add(Json.createObjectBuilder()
                        .add("ID", data.getInt("ID"))
                        .add("handledare", data.getString("handledare"))
                        .add("telefonnummer", data.getString("telefonnummer"))
                        .add("email", data.getString("email"))
                        .add("namn", data.getString("namn"))//programnamn
                        .add("foretag", data.getString("företag"))
                        .build());
            }
    
            conn.close();
            return program.build();
          
        } catch (Exception e) {
            System.out.println("elevhandledare - getHandledare()");
            System.out.println(e.getMessage());
            return null;
        }

    }
}
