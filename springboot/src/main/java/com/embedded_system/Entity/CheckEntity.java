package com.embedded_system.Entity;

/**
 * Created by onion on 4/13/2017.
 */
public class CheckEntity {
    private boolean check;
    private String picurl;

    public CheckEntity(boolean check, String picname) {
        this.check = check;
        this.picurl = picname;
    }

    public CheckEntity() {
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getPicname() {
        return picurl;
    }

    public void setPicname(String picname) {
        this.picurl = picname;
    }
}
