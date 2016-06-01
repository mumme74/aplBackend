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

    private int id;

    public String getFöretag() {
        return företag;
    }

    public void setFöretag(String företag) {
        this.företag = företag;
    }

    public int getProgram_id() {
        return program_id;
    }

    public void setProgram_id(int program_id) {
        this.program_id = program_id;
    }

    public String getAnvnamn() {
        return anvnamn;
    }

    public void setAnvnamn(String anvnamn) {
        this.anvnamn = anvnamn;
    }

    public String getLösenord() {
        return lösenord;
    }

    public void setLösenord(String lösenord) {
        this.lösenord = lösenord;
    }
    private String namn;
    private String tfnr;
    private String email;
    private int klass;
    private int hl_id;
    private String hl_namn;
    private int behörighet;
    private String företag;
    private int program_id;
    private String anvnamn;
    private String lösenord;
    private String namn_företag;

    public String getNamn_företag() {
        return namn_företag;
    }

    public void setNamn_företag(String namn_företag) {
        this.namn_företag = namn_företag;
    }

    public String getHl_namn(){
        return hl_namn;
    }
    
    public void setHl_namn(String hl_namn){
        this.hl_namn = hl_namn;
    }
    
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

    public int getBehörighet() {
        return behörighet;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTfnr() {
        return tfnr;
    }

    public void setTfnr(String tfnr) {
        this.tfnr = tfnr;
    }

    public int getKlass() {
        return klass;
    }

    public void setKlass(int klass) {
        this.klass = klass;
    }

    public int getHl_id() {
        return hl_id;
    }

    public void setHl_id(int hl_id) {
        this.hl_id = hl_id;
    }

    public void setBehörighet(int behörighet) {
        this.behörighet = behörighet;
    }
}
