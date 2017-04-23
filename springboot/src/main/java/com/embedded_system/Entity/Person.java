package com.embedded_system.Entity;

/**
 * Created by onion on 4/12/2017.
 */
public class Person {
    private String name;
    private String surname;
    private String tel_number;
    private String rfid_data;

    public Person(String name, String surname, String tel_number, String rfid_data) {
        this.name = name;
        this.surname = surname;
        this.tel_number = tel_number;
        this.rfid_data = rfid_data;
    }

    public Person() {
    }

    public String getName() {
        return name;
    }

    public String getRfid_data() {
        return rfid_data;
    }

    public void setRfid_data(String rfid_data) {
        this.rfid_data = rfid_data;
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
