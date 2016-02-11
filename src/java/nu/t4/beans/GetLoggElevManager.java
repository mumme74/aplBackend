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
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 *
 * @author Daniel Nilsson
 */
@Stateless
public class GetLoggElevManager {

    public JsonArray getLoggar(int elev_id) {
        try {

            Connection conn = ConnectionFactory.getConnection();
            Statement stmt = conn.createStatement();
            String sql = String.format("SELECT * FROM loggbokvy WHERE "
                    + "loggbok.elev_id = %d ORDER BY loggbok.datum DESC", elev_id);
            ResultSet data = stmt.executeQuery(sql);
            JsonArrayBuilder jsonArray = Json.createArrayBuilder();
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
                    stringIntryck = "FEL";
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
                jsonArray.add(obuilder.build());
            }
            conn.close();
            return jsonArray.build();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
