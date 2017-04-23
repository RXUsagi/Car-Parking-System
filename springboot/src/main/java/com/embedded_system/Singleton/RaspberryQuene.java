package com.embedded_system.Singleton;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by onion on 4/12/2017.
 */
public class RaspberryQuene {
    private String rfid_data;
    private String picindex;
    private long customer_id;

    public RaspberryQuene(String rfid_data, String picindex, long customer_id) {
        this.rfid_data = rfid_data;
        this.picindex = picindex;
        this.customer_id = customer_id;
    }

    public RaspberryQuene() {
    }

    public String getRfid_data() {
        return rfid_data;
    }


    public long getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(long customer_id) {
        this.customer_id = customer_id;
    }

    public void setRfid_data(String rfid_data) {
        this.rfid_data = rfid_data;
    }

    public String getPicindex() {
        return picindex;
    }

    public void setPicindex(String picindex) {
        this.picindex = picindex;
    }
}
