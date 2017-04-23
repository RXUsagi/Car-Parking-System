package com.embedded_system.Controllers;

import com.embedded_system.Dao.CarDao;
import com.embedded_system.Dao.CustomerDao;
import com.embedded_system.Dao.LoggerDao;
import com.embedded_system.Dao.RfidDao;
import com.embedded_system.Entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.Console;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Created by onion on 4/11/2017.
 */
@Controller
@RequestMapping("/")
public class HtmlController {
    @Autowired
    private CarDao carDao;
    @Autowired
    private CustomerDao customerDao;
    @Autowired
    private LoggerDao loggerDao;
    @Autowired
    private RfidDao rfidDao;
    private int carslot = 10;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/main",method = RequestMethod.GET)
    public String htmlmain(ModelMap modelMap){
        String user_fullname = "Thanit Wongmasa";
        //String user_url = "http://images5.fanpop.com/image/photos/31300000/beautiful-heart-pic-beautiful-pictures-31395948-333-500.jpg";
        String customer_count = "" + rfidDao.count();
        int car_slot_left = (int) (carslot - rfidDao.count());
        modelMap.addAttribute("user_fullname",user_fullname);
        //modelMap.addAttribute("user_url",user_url);
        modelMap.addAttribute("customer_count",customer_count);
        modelMap.addAttribute("car_slot",carslot+"");
        modelMap.addAttribute("car_slot_left",car_slot_left+"");

        // car slot table
        List<Rfid> rfids = new ArrayList<>();
        rfidDao.findAll().forEach(rfids::add);
        List<Customer> customers = new ArrayList<>();
        customerDao.findAll().forEach(customers::add);
        List<Car> cars = new ArrayList<>();
        carDao.findAll().forEach(cars::add);
        List<Carslot> carslots = new ArrayList<>();
        int index = 0;
        while(index < rfids.size()){
            Rfid rfid_token = rfids.get(index);
            String carNumber = cars.get((int) rfid_token.getCar_id()-1).getCar_num();
            String fullname = customers.get((int) rfid_token.getCustomer_id()-1).getName() + " " + customers.get((int) rfid_token.getCustomer_id()-1).getSurname();
            String tel = customers.get((int) rfid_token.getCustomer_id()-1).getTel_number();
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            String date = df.format(rfid_token.getTime_in());
            Carslot n_carslot = new Carslot(carNumber,fullname,tel,date);
            n_carslot.index = (index+1)+"";
            carslots.add(n_carslot);
            index++;
        }
        modelMap.addAttribute("carslots",carslots);

        return "main";
    }

    @RequestMapping(value = "/history",method = RequestMethod.GET)
    public String htmlhistory(ModelMap modelMap){
        String user_fullname = "Thanit Wongmasa";
        //String user_url = "http://images5.fanpop.com/image/photos/31300000/beautiful-heart-pic-beautiful-pictures-31395948-333-500.jpg";
        String customer_count = "" + rfidDao.count();
        int car_slot_left = (int) (carslot - rfidDao.count());
        modelMap.addAttribute("user_fullname",user_fullname);
        List<com.embedded_system.Entity.Logger> loggers = new ArrayList<>();
        loggerDao.findAll().forEach(loggers::add);
        List<Logslot> logslots = new ArrayList<>();
        int index = 0;
        while(index < loggers.size()){
            com.embedded_system.Entity.Logger log = loggers.get(index);
            String fullname = customerDao.findOne(log.getCustomer_id()).getName() + " " + customerDao.findOne(log.getCustomer_id()).getSurname();
            String carnum = carDao.findOne(log.getCar_id()).getCar_num();
            String telnum = customerDao.findOne(log.getCustomer_id()).getTel_number();
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            String t_in = df.format(log.getTime_in());
            String t_out = df.format(log.getTime_out());
            Logslot n_logslot = new Logslot(fullname,carnum,telnum,t_in,t_out);
            logslots.add(n_logslot);
            index++;
        }
        modelMap.addAttribute("logslots",logslots);
        return "history";
    }

    @RequestMapping(value = "/problem",method = RequestMethod.GET)
    public String htmlproblem(ModelMap modelMap){
        return "problem";
    }
}
