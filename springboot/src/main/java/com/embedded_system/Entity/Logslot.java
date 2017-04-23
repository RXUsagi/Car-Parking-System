package com.embedded_system.Entity;

import java.sql.Timestamp;

/**
 * Created by onion on 4/15/2017.
 */
public class Logslot {
    public String fullname;
    public String carnum;
    public String telnum;
    public String time_in;
    public String time_out;

    public Logslot(String fullname, String carnum, String telnum, String time_in, String time_out) {
        this.fullname = fullname;
        this.carnum = carnum;
        this.telnum = telnum;
        this.time_in = time_in;
        this.time_out = time_out;
    }
}
