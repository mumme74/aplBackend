/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.t4.beans;

import com.mysql.jdbc.Connection;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Iterator;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;

/**
 *
 * @author Daniel Nilsson
 */
@Stateless
public class GetLoggElevManager {

    public JsonArray getLoggar(int elev_id) {
        try {
            Connection conn = ConnectionFactory.getConnection();
            com.mysql.jdbc.Statement stmt = (com.mysql.jdbc.Statement) conn.createStatement();
            String sql = String.format("SELECT * FROM loggbokvy WHERE "
                    + "loggbokvy.elev_id = %d ORDER BY loggbokvy.datum DESC", elev_id);
            ResultSet data = stmt.executeQuery(sql);
            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
            while (data.next()) {
                String stringIntryck = "";
                int intryck = data.getInt("intryck");
                if (intryck == 0) {
                    stringIntryck = "dålig";
                } else if (intryck == 1) {
                    stringIntryck = "sådär";
                } else if (intryck == 2) {
                    stringIntryck = "bra";
                } else {
                    stringIntryck = "ERROR";
                }
                JsonObjectBuilder obuilder = Json.createObjectBuilder();
                obuilder.add("ID", data.getInt("ID"))
                        .add("elev_id", data.getInt("elev_id"))
                        .add("innehall", data.getString("innehåll"))
                        .add("intryck", stringIntryck)
                        .add("datum", data.getString("datum"))
                        .add("namn", data.getString("namn"));
                //Hanterar om "bild" är null i databasen
                String bild = data.getString("bild");
                if (data.wasNull()) {
                    obuilder.add("bild", JsonObject.NULL);
                } else {
                    obuilder.add("bild", bild);
                }
                arrayBuilder.add(obuilder.build());
            }
            JsonArray array1 = arrayBuilder.build();
            Iterator<JsonValue> iterator = array1.iterator();
            while (iterator.hasNext()) {
                JsonObject obj = (JsonObject) iterator.next();
                JsonArrayBuilder arrayBuilder2 = Json.createArrayBuilder();
                int logg_id = obj.getInt("ID");
                sql = "SELECT * FROM kommentarvy WHERE loggbok_id =" + logg_id;
                ResultSet data2 = stmt.executeQuery(sql);
                JsonArrayBuilder jsonArray = Json.createArrayBuilder();
                while (data2.next()) {
                    String datum = data2.getString("datum").substring(0,16);
                    JsonObjectBuilder obuilder = Json.createObjectBuilder();
                    obuilder.add("innehall", data2.getString("innehåll"))
                            .add("datum", datum)
                            .add("namn", data2.getString("namn"));
                    arrayBuilder2.add(obuilder.build());
                }
                arrayBuilder.add(Json.createObjectBuilder()
                        .add("ID", logg_id)
                        .add("elev_id", obj.getInt("elev_id"))
                        .add("innehall", obj.getString("innehall"))
                        .add("intryck", obj.getString("intryck"))
                        .add("datum", obj.getString("datum"))
                        .add("namn", obj.getString("namn"))
                        .add("bild", obj.get("bild"))
                        .add("kommentarer", arrayBuilder2.build())
                        .build());
            }
            conn.close();
            return arrayBuilder.build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
