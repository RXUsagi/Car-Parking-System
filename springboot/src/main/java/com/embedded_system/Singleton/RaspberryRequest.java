package com.embedded_system.Singleton;

import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Created by onion on 4/12/2017.
 */
public class RaspberryRequest {
    private static RaspberryRequest raspberryRequest = null;
    private static ArrayList<RaspberryQuene> quenes_in = null;
    private static ArrayList<RaspberryQuene> quenes_out = null;
    private static boolean imagecheck = false;
    private static int picindex = 0;
    private final org.slf4j.Logger lock = LoggerFactory.getLogger(this.getClass());
    public static RaspberryRequest instance(){
        if(RaspberryRequest.raspberryRequest == null){
            raspberryRequest = new RaspberryRequest();
            quenes_in = new ArrayList<RaspberryQuene>();
            quenes_out = new ArrayList<RaspberryQuene>();
            picindex = 1;
            imagecheck = false;
        }
        return raspberryRequest;
    }

    public void deletebyindex_In(int index){
        quenes_in.remove(index);
    }

    public static boolean isImagecheck() {
        return imagecheck;
    }

    public static void setImagecheck(boolean imagecheck) {
        RaspberryRequest.imagecheck = imagecheck;
    }

    public void deletebyindex_Out(int index){
        quenes_out.remove(index);
    }

    public boolean isQueneInEmpty(){
        //lock.warn(quenes_in.size()+"");
        if(quenes_in.size() == 0){
            return true;
        }else{
            return false;
        }
    }

    public boolean isQueneOutEmpty(){
        if(quenes_out.size() == 0){
            return true;
        }else{
            return false;
        }
    }

    public String getInFIFO(){
        if(quenes_in.size() == 0){
            return "";
        }else{
            return quenes_in.get(0).getPicindex();
        }
    }

    public String getOutFIFO(){
        if(quenes_out.size() == 0){
            return "";
        }else{
            return quenes_out.get(0).getPicindex();
        }
    }

    public RaspberryQuene getfifoqueneIn(){
        return quenes_in.get(0);
    }

    public RaspberryQuene getfifoqueneOut(){
        return quenes_out.get(0);
    }

    public void deleteFIFOIN(){
        quenes_in.remove(0);
    }
    public void deleteFIFOOUT(){
        quenes_out.remove(0);
    }

    public int getPicindex(){
        return picindex;
    }

    public void createQueneIn(String rfid_data,String pic_index,long customerid){
        RaspberryQuene n_quene = new RaspberryQuene(rfid_data,pic_index,customerid);
        quenes_in.add(n_quene);
        picindex++;
    }

    public void createQueneOut(String rfid_data,String pic_index,long customerid){
        RaspberryQuene n_quene = new RaspberryQuene(rfid_data,pic_index,customerid);
        quenes_out.add(n_quene);
        picindex++;
    }
}
