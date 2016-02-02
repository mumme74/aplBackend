package nu.t4.beans;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import java.sql.DriverManager;
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
public class NarvaroManager {

    public JsonArray getNarvaro() {
        try {
            Connection conn = ConnectionFactory.getConnection();
            Statement stmt = (Statement) conn.createStatement();
            String sql = "SELECT * FROM närvaro";
            ResultSet data = stmt.executeQuery(sql);

            JsonArrayBuilder elever = Json.createArrayBuilder();

            while (data.next()) {
                elever.add(Json.createObjectBuilder()
                        .add("narvaro_id", data.getInt("närvaro_id"))
                        .add("anvandar_id", data.getInt("användar_id"))
                        .add("trafikljus", data.getInt("trafikljus"))
                        .add("godkant", data.getInt("godkänt"))
                        .add("datum", data.getString("datum"))
                        .build());
            }

            conn.close();
            return elever.build();

        } catch (Exception e) {
            System.out.println("NarvaroManagar - getNarvaro()");
            System.out.println(e.getMessage());
            return null;
        }

    }

    public boolean setNarvaro(JsonObject array, int id) {
        try {
            Connection conn = ConnectionFactory.getConnection();
            Statement stmt = (Statement) conn.createStatement();
            String sqlbase = "INSERT INTO närvaro VALUES(null, %d, %d, 0, '%s')";
            String sql = "";

            JsonObject item = array;
            int trafikljus = item.getInt("trafikljus");
            String datum = item.getString("datum");
            sql = String.format(sqlbase, id, trafikljus, datum);
            System.out.println(sql);
            stmt.executeUpdate(sql);
            conn.close();
            return true;

        } catch (Exception e) {
            System.out.println("elevhandledare - setElevHandledare()");
            System.out.println(e.getMessage());
            return false;
        }
    }
};
