/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

/**
 *
 * @author Rewind
 */
public class Ip {
    private String name;
    //private byte log;
    
    public Ip(){}

    public Ip(String name) {
        this.name = name;
        //this.log = log;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /*public byte getLog() {
        return log;
    }

    public void setLog(byte log) {
        this.log = log;
    }*/
   
    @Override
    public String toString(){
        return name;
    }
}
