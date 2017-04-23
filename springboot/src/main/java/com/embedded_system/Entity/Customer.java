package com.embedded_system.Entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by onion on 4/11/2017.
 */
@Entity
@Table(name = "customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long customer_id;
    @NotNull
    private String name;
    @NotNull
    private String surname;
    @NotNull
    private String tel_number;
    @NotNull
    private String rfid_data;

    public Customer(String name, String surname, String tel_number, String rfid_data) {
        this.name = name;
        this.surname = surname;
        this.tel_number = tel_number;
        this.rfid_data = rfid_data;
    }

    public Customer() {
    }

    public String getRfid_data() {
        return rfid_data;
    }

    public void setRfid_data(String rfid_data) {
        this.rfid_data = rfid_data;
    }

    public long getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(long customer_id) {
        this.customer_id = customer_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getTel_number() {
        return tel_number;
    }

    public void setTel_number(String tel_number) {
        this.tel_number = tel_number;
    }
}
