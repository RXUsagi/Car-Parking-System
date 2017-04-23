package com.embedded_system.Entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by onion on 4/11/2017.
 */

@Entity
@Table(name = "car")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long car_id;

    @NotNull
    private long customer_id;

    @NotNull
    private String car_url;

    @NotNull
    private String car_num;


    public Car(long customer_id, String car_url, String car_num) {
        this.customer_id = customer_id;
        this.car_url = car_url;
        this.car_num = car_num;
    }

    public Car(){}

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

    public String getCar_url() {
        return car_url;
    }

    public void setCar_url(String car_url) {
        this.car_url = car_url;
    }

    public String getCar_num() {
        return car_num;
    }

    public void setCar_num(String car_num) {
        this.car_num = car_num;
    }
}
