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

    public boolean redigeraElev(int ID, String namn, String tfnr, String email,
            int klass, int handledar_id) {

        try {
            Connection conn = ConnectionFactory.getConnection();
            Statement stmt = conn.createStatement();
            String sql = String.format("UPDATE skolans_användare SET namn = '%s', "
                    + "Telefonnummer = '%s', "
                    + "email = '%s', "
                    + "handledare_ID = %d, "
                    + "klass = %d "
                    + "WHERE ID = %d", namn, tfnr, email, handledar_id, klass, ID);
            stmt.executeUpdate(sql);

            conn.close();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean redigeraHandledare(int ID, String namn, String tfnr,
            String email, String företag, String användarnamn, String lösenord) {
        try {
            Connection conn = ConnectionFactory.getConnection();
            Statement stmt = conn.createStatement();
            String sql = String.format("UPDATE handledare SET namn = '%s', "
                    + "telefonnummer = '%s', "
                    + "email = '%s', "
                    + "företag = '%s', "
                    + "användarnamn = '%s' ",
                    namn, tfnr, email, företag, användarnamn);
            if (!lösenord.equals("")) {
                String encrypted_lösenord = BCrypt.hashpw(lösenord, BCrypt.gensalt());
                sql += String.format(", lösenord = '%s' ", encrypted_lösenord);
            }
            sql += String.format("WHERE ID = %d", ID);
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
            String sqlbase = "UPDATE skolans_användare SET handledare_ID = %d WHERE ID = %d;";
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
