/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.t4.beans;

import com.mysql.jdbc.Connection;
import java.sql.Statement;
import javax.ejb.Stateless;
import javax.json.JsonArray;

/**
 *
 * @author Daniel Nilsson
 */
@Stateless
public class KommentarManager {

    public boolean postKommentar(int användar_id, int loggbok_id,
                                    String innehåll, String datum) {
        try {
            Connection conn = ConnectionFactory.getConnection();
            Statement stmt = conn.createStatement();
            String sql = String.format("INSERT INTO kommentar VALUES"
                    + "(NULL,%d,%d,'%s','%s')",
                    användar_id, loggbok_id, innehåll, datum);
            stmt.executeUpdate(sql);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
