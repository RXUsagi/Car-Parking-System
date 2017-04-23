package com.embedded_system.Entity;

/**
 * Created by onion on 4/14/2017.
 */
public class CheckRequest {
    private boolean check;

    public CheckRequest(boolean check) {
        this.check = check;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
