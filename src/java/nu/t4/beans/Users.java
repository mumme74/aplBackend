/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.t4.beans;

/**
 *
 * @author Daniel Nilsson
 */
public class Users {
    private String namn;
    private String email;
    private String behörighet;

    public String getNamn() {
        return namn;
    }

    public void setNamn(String namn) {
        this.namn = namn;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBehörighet() {
        return behörighet;
    }

    public void setBehörighet(String behörighet) {
        this.behörighet = behörighet;
    }
}
