package com.fyp.houserental.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fyp.houserental.domain.Appointment;
import com.fyp.houserental.domain.House;
//import com.fyp.houserental.domain.Result;
import com.fyp.houserental.domain.ResultInfo;
import com.fyp.houserental.domain.User;
import com.fyp.houserental.service.HouseService;
import com.fyp.houserental.service.UserService;
import com.fyp.houserental.service.impl.UserServiceImpl;
import com.fyp.houserental.util.CommonUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class HouseController {
    @Autowired
    private HouseService houseService;
    @Autowired
    private UserService userService;
    @Autowired
    private CommonUtils commonUtils;

    @RequestMapping("/findAllHouse")
    public void findAllHouse(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<House> houseList = houseService.findAll();
        commonUtils.writeValue(houseList,response);
    }



    @RequestMapping("/findAllUserHouse")
    public void findAllUserHouse(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = null;
        user = (User)request.getSession().getAttribute("user");
        List<House> houseList = houseService.findAllUserHouse(user.getUsername());
        commonUtils.writeValue(houseList,response);
    }

    @RequestMapping("/viewMyHouse")
    public String viewMyHouse(Model model, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = null;
        try {
            user = (User)request.getSession().getAttribute("user");
            int userID = user.getId();
        } catch (Exception e) {
            return "login";
        }

        String id = request.getParameter("id");
        model.addAttribute("houseId", id);
        return "editHouse";

    }

//    new

    @RequestMapping("/findEditHouseInfo")
    public void findEditHouseInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        House house = null;
        User user = null;
        try {
            user = (User)request.getSession().getAttribute("user");
            user.getId();

        } catch (Exception e) {
            commonUtils.writeValue(house,response);
            return;
        }

        house = houseService.findEditHouseInfo(id);
        if (user.getId() != house.getHouseOwnerId()){
            house=null;
            commonUtils.writeValue(house,response);
            return;
        }

        commonUtils.writeValue(house,response);
    }

    @RequestMapping("/updateHouseInfo")
    public void updateHouseInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String[]> map = request.getParameterMap();
        int id = Integer.parseInt(request.getParameter("id"));

        House house = new House();
        try {
            BeanUtils.populate(house, map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        User user = null;
        ResultInfo info = new ResultInfo();
        house.setID(id);

        try {
            user = (User)request.getSession().getAttribute("user");
        } catch (Exception e) {
            e.printStackTrace();
        }

        house.setOwner_name(houseService.checkOwnerNameById(id));

        if (user!=null){
            if (!user.getUsername().equals(house.getOwner_name())){
                info.setFlag(false);
                info.setErrorMsg("You can only edit your house! Please Login");
            } else {
                houseService.updateHouseInfo(house);
                info.setFlag(true);
            }
        } else {
            info.setFlag(false);
            info.setErrorMsg("You can only edit your house! Please Login");
        }
        commonUtils.writeValue(info,response);
    }

    @RequestMapping("/updateHouseAgreement")
    public void updateHouseAgreement(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        Map<String, String[]> map = request.getParameterMap();
        int id = Integer.parseInt(request.getParameter("id"));

        House house = new House();
        try {
            BeanUtils.populate(house, map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        User user = null;
        ResultInfo info = new ResultInfo();
        house.setID(id);

        try {

            user = (User)request.getSession().getAttribute("user");

        } catch (Exception e) {
            e.printStackTrace();
        }

        house.setOwner_name(houseService.checkOwnerNameById(id));

        if (user!=null){
            if (!user.getUsername().equals(house.getOwner_name())){
                info.setFlag(false);
                info.setErrorMsg("You can only edit your house! Please Login");
            } else {
                houseService.updateHouseAgreement(house);
                info.setFlag(true);
            }
        } else {
            info.setFlag(false);
            info.setErrorMsg("You can only edit your house! Please Login");
        }
        commonUtils.writeValue(info,response);

    }

    @RequestMapping("/viewDetails")
    public String viewHouseDetails(HttpServletRequest request, HttpServletResponse response,Model model) throws ServletException, IOException{
        String id = request.getParameter("id");
        model.addAttribute("houseId", id);
        return "houseDetails";
    }

    @RequestMapping("/getHouseInfoById")
    public void getHouseInfoById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        House house = null;
        try {
            house = houseService.findEditHouseInfo(id);
        } catch (Exception e) {
            house = null;
        }
        commonUtils.writeValue(house,response);
    }

    @RequestMapping("/bookAppointment")
    /**
     * info false, return 0 = not login, return 1 = too many request, return 2 = self-book, retun 3 date problem
     */
    public void bookAppointment(@RequestParam("id")String id,@RequestParam("date") String date,@RequestParam("time") String time, HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
        User user = null;
        ResultInfo info = new ResultInfo();
        try {
            user = (User)request.getSession().getAttribute("user");
            User user1 = userService.findEditAccountInfo(Integer.toString(user.getId()));
            user = user1;
        } catch (Exception e) {
            info.setFlag(false);
            info.setErrorMsg("0");
            commonUtils.writeValue(info,response);
            return;
        }

        if (user.getType().equals("a")){
            info.setFlag(false);
            info.setErrorMsg("4");
            commonUtils.writeValue(info,response);
            return;
        }


        House house = houseService.findEditHouseInfo(id);

        if(house.getHouseOwnerId() == user.getId()){
            info.setFlag(false);
            info.setErrorMsg("2");
            commonUtils.writeValue(info,response);
            return;
        }

        if (!house.getStatus().equals("ready")){
            info.setFlag(false);
            info.setErrorMsg("5");
            commonUtils.writeValue(info,response);
            return;
        }

        int activeCount = houseService.findActiveAppointmentByUserId(user.getId());
        if (activeCount >= 3){
            info.setFlag(false);
            info.setErrorMsg("1");
            commonUtils.writeValue(info,response);
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy",Locale.UK);

        Date date1 = sdf.parse(date);
        Date currentDate = new Date();
        if(!date1.after(currentDate)){
            info.setFlag(false);
            info.setErrorMsg("3");
            commonUtils.writeValue(info,response);
            return;
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MMM-dd",Locale.UK);
        date = formatter.format(date1);


        Appointment appointment = new Appointment();
        appointment.setTime(time);
        appointment.setDate(date);
        appointment.setTenant_id(user.getId());
        appointment.setOwner_name(house.getOwner_name());
        appointment.setOwner_id(house.getHouseOwnerId());
        appointment.setTenant_name(user.getUsername());
        appointment.setOwner_phone(house.getPhone());
        appointment.setTenant_phone(user.getPhone());
        appointment.setImage_url(house.getMainImageUrl());
        appointment.setBuilding_name(house.getBuildingName());
        appointment.setHouseID(house.getID());
//        status: 1:waiting,2:booked,3:decline,0:cancelled
        appointment.setStatus(1);

        houseService.registerAppointment(appointment);
        info.setFlag(true);
        commonUtils.writeValue(info,response);
    }


    @RequestMapping("/findAppointmentForUser")
    public void findAppointmentForUser(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {

        User user = null;
        ResultInfo info = new ResultInfo();
        try {
            user = (User)request.getSession().getAttribute("user");
        } catch (Exception e) {
            info.setFlag(false);
            info.setErrorMsg("0");
            commonUtils.writeValue(info,response);
            return;
        }

        int id = user.getId();

        List<Appointment> appointmentList = houseService.findAppointmentForUser(id);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd",Locale.UK);
        for (int i = 0; i < appointmentList.size(); i++) {
            String date = appointmentList.get(i).getDate();
            Date date1 = sdf.parse(date);
            Date currentDate = new Date();
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_YEAR,-1);
            currentDate = cal.getTime();

            if(date1.before(currentDate)){
                appointmentList.get(i).setStatus(4);
            }

        }
        commonUtils.writeValue(appointmentList,response);
    }


    @RequestMapping("/findUserAppointment")
    public void findUserAppointment(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {

        User user = null;
        ResultInfo info = new ResultInfo();
        try {
            user = (User)request.getSession().getAttribute("user");
        } catch (Exception e) {
            info.setFlag(false);
            info.setErrorMsg("0");
            commonUtils.writeValue(info,response);
            return;
        }

        int id = user.getId();

        List<Appointment> appointmentList = houseService.findUserAppointment(id);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd",Locale.UK);
        for (int i = 0; i < appointmentList.size(); i++) {
            String date = appointmentList.get(i).getDate();
            Date date1 = sdf.parse(date);
            Date currentDate = new Date();
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_YEAR,-1);
            currentDate = cal.getTime();

            if(date1.before(currentDate)){
                appointmentList.get(i).setStatus(4);
            }

        }
        commonUtils.writeValue(appointmentList,response);
    }

    @RequestMapping("/continueRentHouse")
    public void continueRentHouse(HttpServletRequest request, HttpServletResponse response) throws IOException{


        String id = request.getParameter("id");
        String status = "ready";
        houseService.setHouseStatusById(id,status);
        response.sendRedirect("myHouselist.html");

    }


    @RequestMapping("/stopRentHouse")
    public void stopRentHouse(HttpServletRequest request, HttpServletResponse response) throws IOException{


        String id = request.getParameter("id");
        String status = "stop";
        houseService.setHouseStatusById(id,status);
        response.sendRedirect("myHouselist.html");

    }


    @RequestMapping("/acceptAppointment")
    /**
     *
     * @param status
     * status: 1:waiting,2:booked,3:decline,0:cancelled
     */
    public void acceptAppointment(HttpServletRequest request, HttpServletResponse response) throws IOException{

        String id = request.getParameter("id");
        int status = 2;
        houseService.setAppointmentStatusById(id,status);
        response.sendRedirect("myAppointment.html");

    }
    @RequestMapping("/declineAppointment")
    public void declineAppointment(HttpServletRequest request, HttpServletResponse response) throws IOException{

        String id = request.getParameter("id");
        int status = 3;
        houseService.setAppointmentStatusById(id,status);
        response.sendRedirect("myAppointment.html");

    }

    @RequestMapping("/cancelAppointment")
    public void  cancelAppointment(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String id = request.getParameter("id");
        int status = 0;
        houseService.setAppointmentStatusById(id,status);
        response.sendRedirect("myAppointment.html");

    }

    @RequestMapping("/searchHouse")
    public void searchHouse(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String keyword = request.getParameter("keyword").isEmpty() ? null : request.getParameter("keyword");

        String state = request.getParameter("state");

        String minRental = request.getParameter("minRental").isEmpty() ? "0" : request.getParameter("minRental");

        String maxRental = request.getParameter("maxRental").isEmpty() ? "999999999" : request.getParameter("maxRental");

        String furnish = request.getParameter("furnish");

        String bedroom = request.getParameter("bedroom").isEmpty() ? "bedroom" : request.getParameter("bedroom");

        String bathroom = request.getParameter("bathroom").isEmpty() ? "bathroom" : request.getParameter("bathroom");

        List<House> houseList = houseService.searchHouse(keyword,state,minRental,maxRental,furnish,bedroom,bathroom);

        commonUtils.writeValue(houseList,response);




//        List<House> houseList = houseService.findAll();
//        commonUtils.writeValue(houseList,response);
    }



}

