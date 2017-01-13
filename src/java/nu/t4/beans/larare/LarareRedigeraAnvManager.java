/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.t4.beans.larare;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Iterator;
import javax.ejb.Stateless;
import javax.json.JsonArray;
import javax.json.JsonObject;
import nu.t4.beans.ConnectionFactory;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author Daniel Nilsson
 */
@Stateless
public class LarareRedigeraAnvManager {

    public boolean redigeraElev(int id, String namn, String tfnr, String email,
            int klass, int handledar_id) {

        try {
            Connection conn = ConnectionFactory.getConnection();
            Statement stmt = conn.createStatement();
            String sql = String.format("UPDATE google_anvandare SET namn = '%s', "
                    + "telefonnummer = '%s', "
                    + "email = '%s', "
                    + "handledare_id = %d, "
                    + "klass = %d "
                    + "WHERE id = %d", namn, tfnr, email, handledar_id, klass, id);
            stmt.executeUpdate(sql);

            conn.close();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean redigeraHandledare(int id, String namn, String tfnr,
            String email, String foretag, String anvandarnamn, String losenord) {
        try {
            Connection conn = ConnectionFactory.getConnection();
            Statement stmt = conn.createStatement();
            String sql = String.format("UPDATE handledare SET namn = '%s', "
                    + "telefonnummer = '%s', "
                    + "email = '%s', "
                    + "foretag = '%s', "
                    + "anvandarnamn = '%s' ",
                    namn, tfnr, email, foretag, anvandarnamn);
            if (!losenord.equals("")) {
                String encrypted_losenord = BCrypt.hashpw(losenord, BCrypt.gensalt());
                sql += String.format(", losenord = '%s' ", encrypted_losenord);
            }
            sql += String.format("WHERE id = %d", id);
            stmt.executeUpdate(sql);

            conn.close();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean setElevHandledare(JsonArray array) {
        try {
            com.mysql.jdbc.Connection conn = ConnectionFactory.getConnection();
            com.mysql.jdbc.Statement stmt = (com.mysql.jdbc.Statement) conn.createStatement();
            String sqlbase = "UPDATE google_anvandare SET handledare_id = %d WHERE id = %d;";
            String sql = "";
            Iterator iterator = array.iterator();
            while (iterator.hasNext()) {
                JsonObject item = (JsonObject) iterator.next();
                int e_id = item.getInt("elev_id");
                int h_id = item.getInt("handledare_id");
                sql = String.format(sqlbase, h_id, e_id);
                stmt.addBatch(sql);
            }
            stmt.executeBatch();
            conn.close();
            return true;

        } catch (Exception e) {
            System.out.println("elevhandledare - setElevHandledare()");
            System.out.println(e.getMessage());
            return false;
        }
    }
}
