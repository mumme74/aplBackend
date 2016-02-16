/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.t4.beans;

import java.sql.Connection;
import java.sql.Statement;
import javax.ejb.Stateless;
import javax.json.JsonObject;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author Daniel Nilsson
 */
@Stateless
public class LärareRedigeraAnvManager {

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
            String email, int program_id, String företag, String användarnamn, String lösenord) {
        try {
            Connection conn = ConnectionFactory.getConnection();
            Statement stmt = conn.createStatement();
            String sql = String.format("UPDATE handledare SET namn = '%s', "
                    + "telefonnummer = '%s', "
                    + "email = '%s', "
                    + "program_id = %d, "
                    + "företag = '%s', "
                    + "användarnamn = '%s' ",
                    namn, tfnr, email, program_id, företag, användarnamn);
            if (!lösenord.equals("")) {
                String encrypted_lösenord = BCrypt.hashpw(lösenord, BCrypt.gensalt());
                sql += String.format(", lösenord = '%s' ", encrypted_lösenord);
            }
            sql += String.format("WHERE ID = %d", ID);
            System.out.println(sql);
            stmt.executeUpdate(sql);

            conn.close();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
