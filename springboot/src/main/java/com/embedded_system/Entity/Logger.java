package com.embedded_system.Entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

/**
 * Created by onion on 4/11/2017.
 */
@Entity
@Table(name = "logger")
public class Logger {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long log_id;

    @NotNull
    private long customer_id;

    @NotNull
    private long car_id;

    @NotNull
    private Timestamp time_in;

    @NotNull
    private Timestamp time_out;

    public Logger(long customer_id, long car_id, Timestamp time_in, Timestamp time_out) {
        this.customer_id = customer_id;
        this.car_id = car_id;
        this.time_in = time_in;
        this.time_out = time_out;
    }

    public Logger() {
    }

    public long getLog_id() {
        return log_id;
    }

    public void setLog_id(long log_id) {
        this.log_id = log_id;
    }

    public long getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(long customer_id) {
        this.customer_id = customer_id;
    }

    public long getCar_id() {
        return car_id;
    }

    public void setCar_id(long car_id) {
        this.car_id = car_id;
    }

    public Timestamp getTime_in() {
        return time_in;
    }

    public void setTime_in(Timestamp time_in) {
        this.time_in = time_in;
    }

    public Timestamp getTime_out() {
        return time_out;
    }

    public void setTime_out(Timestamp time_out) {
        this.time_out = time_out;
    }
}
