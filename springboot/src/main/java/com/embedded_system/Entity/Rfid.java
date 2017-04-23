package com.embedded_system.Entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * Created by onion on 4/11/2017.
 */
@Entity
@Table(name = "rfid")
public class Rfid {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long rfid_id;
    @NotNull
    private long car_id;
    @NotNull
    private long customer_id;
    @NotNull
    private Timestamp time_in;

    public Rfid(long car_id, long customer_id, Timestamp time_in) {
        this.car_id = car_id;
        this.customer_id = customer_id;
        this.time_in = time_in;
    }

    public Rfid() {
    }

    public long getRfid_id() {
        return rfid_id;
    }

    public void setRfid_id(long rfid_id) {
        this.rfid_id = rfid_id;
    }

    public long getCar_id() {
        return car_id;
    }

    public void setCar_id(long car_id) {
        this.car_id = car_id;
    }

    public long getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(long customer_id) {
        this.customer_id = customer_id;
    }

    public Timestamp getTime_in() {
        return time_in;
    }

    public void setTime_in(Timestamp time_in) {
        this.time_in = time_in;
    }
}
