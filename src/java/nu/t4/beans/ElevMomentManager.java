/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.t4.beans;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import java.sql.ResultSet;
import java.util.Iterator;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;

/**
 *
 * @author maikwagner
 */

@Stateless
public class ElevMomentManager {
    public JsonArray getMomentElev(int id) {
        try {
            Connection conn = ConnectionFactory.getConnection();
            Statement stmt = (Statement) conn.createStatement();
            String sql = String.format(
                    "SELECT moment.innehåll, tilldela_moment.godkänd "
                   +"FROM moment, tilldela_moment "
                   +"WHERE moment.id = tilldela_moment.moment_id AND tilldela_moment.användar_id =%d",id
            );
            
            ResultSet data = stmt.executeQuery(sql);

            JsonArrayBuilder elever = Json.createArrayBuilder();

            while (data.next()) {
                elever.add(Json.createObjectBuilder()
                        .add("innehall", data.getString("innehåll"))
                        .add("godkand", data.getInt("godkänd"))
                        .build());
            }

            conn.close();
            return elever.build();

        } catch (Exception e) {
            System.out.println("MomentManager - getMomentElev()");
            System.out.println(e.getMessage());
            return null;
        }

    }
    
    
    
    public JsonArray getMomentPerHandledare() {
        try {
            int anv_id = 1;
            Connection conn = ConnectionFactory.getConnection();
            Statement stmt = (Statement) conn.createStatement();
            String sql = String.format("SELECT * FROM moment WHERE användar_ID = %d", anv_id);
            ResultSet data = stmt.executeQuery(sql);

            
            
            JsonArrayBuilder elever = Json.createArrayBuilder();

            while (data.next()) {
                elever.add(Json.createObjectBuilder()
                        .add("ID", data.getInt("ID"))
                        .add("anvandar_id", data.getInt("användar_ID"))
                        .add("innehall", data.getString("innehåll"))
                        .add("godkant", data.getInt("godkänt"))
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
    
    
    
    public boolean skickaMomentTillHandledare(int id) {
        try {
            Connection conn = ConnectionFactory.getConnection();
            Statement stmt = (Statement) conn.createStatement();
            String sqlbase = String.format("UPDATE moment SET godkänt = 1 WHERE ID = %d", id);
            String sql = "";
            stmt.executeBatch();
            conn.close();
            return true;

        } catch (Exception e) {
            System.out.println("MomentManager - skickaMomentTillHandledare()");
            System.out.println(e.getMessage());
            return false;
        }
    }
    
    
    
    
    public boolean skickaMomentTillElev(int id) {
        try {
            Connection conn = ConnectionFactory.getConnection();
            Statement stmt = (Statement) conn.createStatement();
            String sqlbase = String.format("UPDATE moment SET godkänt = 2 WHERE ID = %d", id);
            String sql = "";
            stmt.executeBatch();
            conn.close();
            return true;

        } catch (Exception e) {
            System.out.println("MomentManager - skickaMomentTillElev()");
            System.out.println(e.getMessage());
            return false;
        }
    }

}
