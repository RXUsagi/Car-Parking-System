package com.embedded_system.Controllers;

import com.embedded_system.Dao.CarDao;
import com.embedded_system.Dao.CustomerDao;
import com.embedded_system.Dao.LoggerDao;
import com.embedded_system.Dao.RfidDao;
import com.embedded_system.Entity.*;
import com.embedded_system.Singleton.RaspberryRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.env.Environment;

import javax.imageio.ImageIO;
import javax.validation.Valid;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;


/**
 * Created by onion on 4/11/2017.
 */
@RestController
@RequestMapping("/restful")
public class RestfulController {
    @Autowired
    private CarDao carDao;

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private LoggerDao loggerDao;

    @Autowired
    private RfidDao rfidDao;

    @Autowired
    private Environment env;
    private final Logger lock = LoggerFactory.getLogger(this.getClass());
    // car in
    // car out
    // raspberry recieve in out
    @RequestMapping(value = "/create",method = RequestMethod.POST)
    public Customer createAccount(@Valid @RequestBody Person person){
        //lock.warn(person.getName() + " " + person.getSurname() + " " + person.getTel_number());
        Customer customer = new Customer(person.getName(),person.getSurname(),person.getTel_number(), person.getRfid_data());
        customerDao.save(customer);
        return customer;
    }

    @RequestMapping(value = "/checkcarin",method = RequestMethod.GET)
    public CheckEntity checkcarin(){
        RaspberryRequest raspberryRequest = RaspberryRequest.instance();
        CheckEntity check = new CheckEntity(!raspberryRequest.isQueneInEmpty(),raspberryRequest.getInFIFO());
        //mock
        //check.setCheck(false);
        check.setPicname(check.getPicname()+".jpg");
        return check;
    }

    @RequestMapping(value = "/checkcarout",method = RequestMethod.GET)
    public CheckEntity checkcarout(){
        RaspberryRequest raspberryRequest = RaspberryRequest.instance();
        CheckEntity check = new CheckEntity(!raspberryRequest.isQueneOutEmpty(),raspberryRequest.getOutFIFO());
        //mock
        //check.setPicname("1.png");
        //check.setCheck(true);
        check.setPicname(check.getPicname()+".jpg");
        return check;
    }

    @RequestMapping(value = "/callcar",method = RequestMethod.POST)
    @ResponseBody
    public SubmitA modalsubmit(
        @RequestParam("car") String car_num,@RequestParam("call") String direction){
        lock.warn(car_num + " " + direction);
        RaspberryRequest raspberryRequest = RaspberryRequest.instance();
        List<Rfid> rfids = new ArrayList<>();
        rfidDao.findAll().forEach(rfids::add);
        List<Car> cars = new ArrayList<>();
        carDao.findAll().forEach(cars::add);
        List<Customer> customers = new ArrayList<>();
        customerDao.findAll().forEach(customers::add);
        List<com.embedded_system.Entity.Logger> loggers = new ArrayList<>();
        loggerDao.findAll().forEach(loggers::add);
        if(direction.equals("in")){
            boolean check = true;
            int index = 0;
            while(index < cars.size() && check){
                if(cars.get(index).getCustomer_id() == raspberryRequest.getfifoqueneIn().getCustomer_id()){
                    if(cars.get(index).getCar_num().equals(car_num)){
                        // create rfid index

                        Rfid rfid = new Rfid(cars.get(index).getCar_id(),raspberryRequest.getfifoqueneIn().getCustomer_id(),new Timestamp(System.currentTimeMillis()));
                        rfidDao.save(rfid);
                        check = false;
                        lock.warn("m0"+raspberryRequest.getfifoqueneIn().getCustomer_id()+"");
                    }
                }
                index++;
            }
            if(check){
                long customer_id = raspberryRequest.getfifoqueneIn().getCustomer_id();
                String car_url = raspberryRequest.getfifoqueneIn().getPicindex();
                Car car = new Car(customer_id,car_url,car_num);
                carDao.save(car);
                cars = new ArrayList<>();
                carDao.findAll().forEach(cars::add);
                Rfid rfid = new Rfid(cars.get(index).getCar_id(),raspberryRequest.getfifoqueneIn().getCustomer_id(),new Timestamp(System.currentTimeMillis()));
                rfidDao.save(rfid);
                lock.warn("m1"+raspberryRequest.getfifoqueneIn().getCustomer_id()+"");
                // create rfid
            }
            raspberryRequest.deleteFIFOIN();
            RaspberryRequest.setImagecheck(true);
        }else{
            int index = 0;
            while(index < rfids.size()){
                if(raspberryRequest.getfifoqueneOut().getCustomer_id() == rfids.get(index).getCustomer_id()){
                    int index2 = 0;
                    while(index2 < cars.size()) {
                        if (rfids.get(index).getCar_id() == cars.get(index2).getCar_id()){
                            if(cars.get(index2).getCar_num().equals(car_num)){
                                long customer_id = raspberryRequest.getfifoqueneOut().getCustomer_id();
                                long car_id = rfids.get(index).getCar_id();
                                Timestamp date_in = rfids.get(index).getTime_in();
                                Timestamp date_out = new Timestamp(System.currentTimeMillis());
                                com.embedded_system.Entity.Logger logger = new com.embedded_system.Entity.Logger(customer_id,car_id,date_in,date_out);
                                loggerDao.save(logger);
                                Rfid d_rfid = new Rfid();
                                d_rfid.setRfid_id(rfids.get(index).getRfid_id());
                                lock.warn(rfids.get(index).getRfid_id()+"delete index");
                                rfidDao.delete(d_rfid);
                                raspberryRequest.deleteFIFOOUT();
                                RaspberryRequest.setImagecheck(true);
                                return new SubmitA("Success","");
                            }else{
                                return new SubmitA("Fail",cars.get(index2).getCar_num());
                            }
                        }
                        index2++;
                    }
                }
                index++;
            }
        }
        return new SubmitA("Success","");
    }

    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> uploadFile(
            @RequestParam("uploadfile") MultipartFile uploadfile,@RequestParam("isCarIn") String carIn,@RequestParam("rfid") String rfid_data) {

        try {
            RaspberryRequest raspberryRequest = RaspberryRequest.instance();
            // Get the filename and build the local file path
            //String filename = uploadfile.getOriginalFilename();
            String filename = raspberryRequest.getPicindex()+".jpg";
            String directory = env.getProperty("netgloo.paths.uploadedFiles");
            String filepath = Paths.get(directory, filename).toString();
            // Save the file locally
            File file = new File("G:\\Project4\\springboot\\target\\classes\\static\\images\\upload\\x1.jpg");
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(file));
            stream.write(uploadfile.getBytes());
            stream.close();
            BufferedImage img = null;
            try{
                img = ImageIO.read(file);
                BufferedImage n_img = new BufferedImage(img.getWidth(),img.getHeight(),BufferedImage.TYPE_INT_ARGB);
                int imageBinary[][] = new int[img.getHeight()][img.getWidth()];
                int imageBinary_n[][] = new int[img.getHeight()][img.getWidth()];
                int imageBinary_n2[][] = new int[img.getHeight()][img.getWidth()];
                ArrayList<ArrayList<Integer>> imageBinary_n3 = new ArrayList<ArrayList<Integer>>();
                int i ;
                int j;
                i = 0;
                while(i < img.getHeight()){
                    j = 0;
                    ArrayList<Integer> newA = new ArrayList<Integer>();
                    while(j < img.getWidth()) {
                        Color c = new Color(img.getRGB(j, i));
                        if(c.getRed() + c.getBlue() + c.getGreen() / 3 < 150) {
                            imageBinary[i][j] = 1;
                        }else{
                            imageBinary[i][j] = 0;
                        }
                        imageBinary_n[i][j] = 0;
                        imageBinary_n2[i][j] = 0;
                        newA.add(0);
                        j++;
                    }
                    imageBinary_n3.add(newA);
                    i++;
                }
                i = 1;
                while(i < img.getHeight()-1){
                    j = 1;
                    while(j < img.getWidth()-1){
                        if(imageBinary[i-1][j-1] + imageBinary[i-1][j] + imageBinary[i-1][j+1] + imageBinary[i][j-1] + imageBinary[i][j] + imageBinary[i][j+1] + imageBinary[i+1][j-1] + imageBinary[i+1][j] +imageBinary[i+1][j+1] < 8){
                            imageBinary_n[i][j] = 0;
                        }else{
                            imageBinary_n[i][j] = 1;
                        }
                        j = j + 1;
                    }
                    i = i + 1;
                }
                imageBinary = imageBinary_n;
                i = 1;
                while(i < img.getHeight()-1){
                    j = 1;
                    while(j < img.getWidth()-1){
                        if(imageBinary[i][j] == 1){
                            imageBinary_n2[i-1][j-1] = 1;
                            imageBinary_n2[i-1][j] = 1;
                            imageBinary_n2[i-1][j+1] = 1;
                            imageBinary_n2[i][j-1] = 1;
                            imageBinary_n2[i][j] = 1;
                            imageBinary_n2[i][j+1] = 1;
                            imageBinary_n2[i+1][j-1] = 1;
                            imageBinary_n2[i+1][j] = 1;
                            imageBinary_n2[i+1][j+1] = 1;
                        }
                        j = j + 1;
                    }
                    i = i + 1;
                }
                /*
                i = 1;
                SearchOBJ token = new SearchOBJ();
                token.itop = img.getHeight();
                token.ilow = 0;
                token.jtop = img.getWidth();
                token.jlow = 0;
                int count = 0;
                //lock.warn("ts 4");
                while(i < img.getHeight()-1){
                    j = 1;
                    while(j < img.getWidth()-1){
                        //lock.warn("i,j x6 : "+i+", "+j + " " + imageBinary_n3.size() + " " + imageBinary_n3.get(0).size());
                        //lock.warn(imageBinary_n3.get(i).get(j) + "");
                        //lock.warn(imageBinary_n2[i][j] + "");
                        if(imageBinary_n3.get(i).get(j) != 1 && imageBinary_n2[i][j] == 1){
                            lock.warn("i,j x7 : "+i+", "+j);
                            SearchOBJ a = new SearchOBJ(imageBinary_n3,0);
                            a.itop = img.getHeight();
                            a.ilow = 0;
                            a.jtop = img.getWidth();
                            a.jlow = 0;
                            //lock.warn("abcd");
                            a = countSearchOBJ(a,imageBinary_n2,i,j,img.getHeight(),img.getWidth());
                            //lock.warn("abcef");
                            imageBinary_n3 = a.arrayList;
                            lock.warn("count : " + a.count);
                            if(a.count > count){
                                count = a.count;
                                token = a;
                            }
                        }
                        j = j + 1;
                    }
                    i = i + 1;
                }
                lock.warn("count 1: " + token.count);
                lock.warn("count 2: " + token.itop);
                lock.warn("count 3: " + token.ilow);
                lock.warn("count 4: " + token.jtop);
                lock.warn("count 5: " + token.jlow);
                lock.warn("ts end");
                ImageIO.write(img.getSubimage(token.itop,token.jtop,token.ilow-token.itop,token.jlow-token.jtop),"jpeg",new File("G:\\Project4\\springboot\\target\\classes\\static\\images\\upload\\x2.jpg"));
                 */
                i = 1;
                int axisline[] = new int[4];
                axisline[0] = img.getHeight(); // x axis start
                axisline[1] = 0; // x axis stop
                axisline[2] = img.getWidth(); // y axis start
                axisline[3] = 0; // y axis stop

                while( i < img.getHeight()-1){
                    j = 1;
                    while(j < img.getWidth()-1){
                        if(imageBinary_n2[i][j] == 1){
                            if(axisline[0] > i){
                                axisline[0] = i;
                            }
                            if(axisline[1] < i){
                                axisline[1] = i;
                            }
                            if(axisline[2] > j){
                                axisline[2] = j;
                            }
                            if(axisline[3] < j){
                                axisline[3] = j;
                            }
                        }
                        j++;
                    }
                    i++;
                }
                ImageIO.write(img.getSubimage(axisline[2],axisline[0],axisline[3]-axisline[2],axisline[1]-axisline[0]),"jpeg",new File(filepath));
                //ImageIO.write(img.getSubimage(0,0,img.getWidth(),img.getHeight()),"jpeg",new File("G:\\Project4\\springboot\\target\\classes\\static\\images\\upload\\x2.jpg"));
            }catch (IOException e){
                lock.warn(e.getMessage());
                throw e;
            }

            if(checkinout(rfid_data)){
                // carin
                List<Customer> customers = new ArrayList<>();
                customerDao.findAll().forEach(customers::add);
                int index = 0;
                int customer_id = 0;
                while(index < customers.size()){
                    if(rfid_data.equals(customers.get(index).getRfid_data())){
                        customer_id = (int) customers.get(index).getCustomer_id();
                    }
                    index++;
                }
                raspberryRequest.createQueneIn(rfid_data,raspberryRequest.getPicindex()+"",customer_id);
                RaspberryRequest.setImagecheck(false);
            }else{
                // carout
                List<Customer> customers = new ArrayList<>();
                customerDao.findAll().forEach(customers::add);
                int index = 0;
                int customer_id = 0;
                while(index < customers.size()){
                    if(rfid_data.equals(customers.get(index).getRfid_data())){
                        customer_id = (int) customers.get(index).getCustomer_id();
                    }
                    index++;
                }
                raspberryRequest.createQueneOut(rfid_data,raspberryRequest.getPicindex()+"",customer_id);
                RaspberryRequest.setImagecheck(false);
            }
            lock.warn(filepath);
            lock.warn("sucessful");
        }
        catch (Exception e) {
            lock.warn("error exception e :"+e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    } // method uploadFile

    @RequestMapping(value = "/confirmimg",method = RequestMethod.GET)
    public CheckRequest checkimgconfirm(){
        CheckRequest checkRequest = new CheckRequest(RaspberryRequest.isImagecheck());
        return  checkRequest;
    }

    private boolean checkinout(String rfid){
        List<Customer> customers = new ArrayList<>();
        customerDao.findAll().forEach(customers::add);
        List<Rfid> rfids = new ArrayList<>();
        rfidDao.findAll().forEach(rfids::add);
        int customerindex = 0;
        int customerid = -1;
        while(customerindex < customers.size()){
            if(customers.get(customerindex).getRfid_data().equals(rfid)){
                customerid = customerindex;
            }
            customerindex++;
        }
        if(customerid == -1){
            Customer customer = new Customer("-","-","-", rfid);
            customerDao.save(customer);
            lock.warn("create acc");
            return true;
        }else{
            int index = 0;
            while(index < rfids.size()){
                lock.warn("customer :"+customers.get(customerid).getCustomer_id());
                lock.warn("rfid"+rfids.get(index).getCustomer_id());
                lock.warn("end");
                if(customers.get(customerid).getCustomer_id() == rfids.get(index).getCustomer_id()){
                    return false;
                }
                index++;
            }
            lock.warn("cannotfind rfid but find acc mean go in");
            return true;
        }
    }

    @RequestMapping(value = "/cutFile", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> cut(
            @RequestParam("uploadfile") MultipartFile uploadfile,@RequestParam("isCarIn") String carIn,@RequestParam("rfid") String rfid_data) {

        try {
            String filename = "x1.jpg";
            String directory = env.getProperty("netgloo.paths.uploadedFiles");
            String filepath = Paths.get(directory, filename).toString();
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filepath)));
            stream.write(uploadfile.getBytes());
            stream.close();

        }
        catch (Exception e) {
            lock.warn("error exception e :"+e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    } // method uploadFile

    private SearchOBJ countSearchOBJ(SearchOBJ a,int[][] b,int i ,int j,int sizei,int sizej){
        lock.warn("i,j : "+i+", "+j);
        SearchOBJ n_s = a;
        lock.warn("tsx 1");
        if(i-1 > 0) {
            if (n_s.arrayList.get(i-1).get(j) != 1 && b[i-1][j] == 1) {
                n_s.count++;
                n_s.arrayList.get(i-1).set(j,1);
                if(n_s.itop < i-1){
                    n_s.itop = i - 1;
                }
                n_s = countSearchOBJ(n_s,b,i-1,j,sizei,sizej);
            }
        }
        lock.warn("tsx 2");
        if(j-1 > 0) {
            lock.warn("tsx 21");
            if (n_s.arrayList.get(i).get(j-1) != 1 && b[i][j-1] == 1) {
                lock.warn("tsx 22");
                n_s.count++;
                lock.warn("tsx 23");
                n_s.arrayList.get(i).set(j-1,1);
                lock.warn("tsx 24");
                if(n_s.jtop < j-1){
                    lock.warn("tsx 25");
                    n_s.jtop = j - 1;
                    lock.warn("tsx 26");
                }
                lock.warn("tsx 27");
                n_s = countSearchOBJ(n_s,b,i,j-1,sizei,sizej);
            }
        }
        lock.warn("tsx 3");
        if(j+1 < sizej) {
            if (n_s.arrayList.get(i).get(j+1) != 1 && b[i][j+1] == 1) {
                n_s.count++;
                n_s.arrayList.get(i).set(j+1,1);
                if(n_s.jlow > j+1){
                    n_s.jlow = j + 1;
                }
                n_s = countSearchOBJ(n_s,b,i,j+1,sizei,sizej);
            }
        }
        lock.warn("tsx 4");
        if(i+1 < sizei) {
            if (n_s.arrayList.get(i+1).get(j) != 1 && b[i+11][j] == 1) {
                n_s.count++;
                n_s.arrayList.get(i+1).set(j,1);
                if(n_s.ilow > i+1){
                    n_s.ilow = i + 1;
                }
                n_s = countSearchOBJ(n_s,b,i+1,j,sizei,sizej);
            }
        }
        lock.warn("tsx r");
        return n_s;
    }
}
