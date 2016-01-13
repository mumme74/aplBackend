package nu.t4.beans;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author Daniel Nilsson
 */
public class menuBean {
    private String menuString;

    public String getMenuString() {
        return menuString;
    }

    public void setMenuString(String menuString) {
        this.menuString = menuString;
    }
    
    public void addClassFunction(){
        menuString ="";
    }
}
