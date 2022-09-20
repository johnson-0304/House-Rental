package com.fyp.houserental.controller;

import com.fyp.houserental.domain.*;
import com.fyp.houserental.service.HouseService;
import com.fyp.houserental.service.OrderService;
import com.fyp.houserental.service.UserService;
import com.fyp.houserental.util.CommonUtils;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private CommonUtils commonUtils;

    @Autowired
    private HouseService houseService;

    @Autowired
    private UserService userService;


    @RequestMapping("/rentHousePage")
    public String rentHousePage(HttpServletRequest request, HttpServletResponse response, Model model) throws ServletException, IOException {
        String houseId = request.getParameter("id");
        model.addAttribute("houseId", houseId);
        User user = null;
        try {
            user = (User) request.getSession().getAttribute("user");
            User user1 = userService.findEditAccountInfo(Integer.toString(user.getId()));
            user = user1;

        } catch (Exception e) {
            return "login";
        }

        if (user.getType().equals("a")) {
            return "adminPage";
        }

        return "orderPage";
    }

    @RequestMapping("/makeOrder")
    //return false, 1: no login session,9:Unknow error, 2 : not enough money, 3 self renting. 4 date problem, 5: house not availalble
    public void makeOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ParseException {
        Map<String, String[]> map = request.getParameterMap();

        User user = null;
        ResultInfo info = new ResultInfo();
        String date = request.getParameter("date");
        String totalPayment = request.getParameter("totalPayment");
        String agreement = request.getParameter("agreement");
        String houseId = request.getParameter("id");
        String duration1 = request.getParameter("duration");


        int totalPaymentInt = 0;
        House house = houseService.findEditHouseInfo(houseId);
        try {
            totalPaymentInt = Integer.parseInt(totalPayment);
        } catch (NumberFormatException e) {
            info.setFlag(false);
            info.setErrorMsg("9");
            commonUtils.writeValue(info, response);
            return;
        }


        try {
            user = (User) request.getSession().getAttribute("user");
            User user1 = userService.findEditAccountInfo(Integer.toString(user.getId()));
            user = user1;

        } catch (Exception e) {
            info.setFlag(false);
            info.setErrorMsg("1");
            commonUtils.writeValue(info, response);
            return;
        }


        //wallet problem /no money / payment inaccurate/deduct money
        if (totalPaymentInt != house.getRental() * 4) {
            info.setFlag(false);
            info.setErrorMsg("9");
            commonUtils.writeValue(info, response);
            return;
        }

        if (user.getBalance() < totalPaymentInt) {
            info.setFlag(false);
            info.setErrorMsg("2");
            commonUtils.writeValue(info, response);
            return;
        }


        //self renting
        if (user.getId() == house.getHouseOwnerId()) {
            info.setFlag(false);
            info.setErrorMsg("3");
            commonUtils.writeValue(info, response);
            return;
        }

        //house not available
        if (!house.getStatus().equals("ready")) {
            info.setFlag(false);
            info.setErrorMsg("5");
            commonUtils.writeValue(info, response);
            return;
        }

        //date problem

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.UK);

        Date date3 = sdf.parse(date);
        Date currentDate = new Date();
        if (!date3.after(currentDate)) {
            info.setFlag(false);
            info.setErrorMsg("4");
            commonUtils.writeValue(info, response);
            return;
        }


        int duration = 0;

        try {
            duration = Integer.parseInt(duration1);
        } catch (Exception e) {
            info.setFlag(false);
            info.setErrorMsg("9");
            commonUtils.writeValue(info, response);
            return;
        }


        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MMM-dd", Locale.UK);
        Date dt = sdf.parse(date);
        String date1 = formatter.format(dt);
        List<String> dateList = new ArrayList<>();
        Calendar rightNow = Calendar.getInstance();

        for (int i = 0; i < duration; i++) {
            rightNow.setTime(dt);
            rightNow.add(Calendar.MONTH, (i + 1));

            Date dt1 = rightNow.getTime();
            String date2 = formatter.format(dt1);
            dateList.add(date2);
        }


        OrderInfo order = new OrderInfo();
        order.setStatus("1");
        order.setAgreement(agreement);
        order.setRental(house.getRental());
        order.setDeposit(house.getRental() * 3);
        order.setDuration(duration);
        order.setEndDate(dateList.get(dateList.size() - 1));
        order.setStartDate(date1);
        order.setOnwerPhone(house.getPhone());
        System.out.println(house.getPhone());
        System.out.println("get owner id" + Integer.toString(house.getHouseOwnerId()));
        order.setOwnerId(Integer.toString(house.getHouseOwnerId()));
        order.setOwnerName(house.getOwner_name());
        order.setTenantId(Integer.toString(user.getId()));
        order.setTenantPhone(user.getPhone());
        order.setTenantName(user.getUsername());
        order.setHouseId(Integer.parseInt(houseId));
        order.setImage_url(house.getMainImageUrl());
        order.setBuilding_name(house.getBuildingName());

        orderService.registerOrder(order);
        userService.deductBalanceById(totalPaymentInt, user.getId());

        Transaction t1 = new Transaction();
        t1.setUserId(Integer.toString(user.getId()));
        t1.setDateTime(new SimpleDateFormat("yyyy/MMM/dd HH:mm:ss").format(Calendar.getInstance().getTime()));
        t1.setDescription("Paid RM" + totalPayment + " for house rental and deposit");
        userService.recordTransaction(t1);

        info.setFlag(true);
        commonUtils.writeValue(info, response);
    }


    @RequestMapping("/findHouseRentedForUser")
    public void findHouseRentedForUser(HttpServletRequest request, HttpServletResponse response) throws IOException {

        User user = null;
        ResultInfo info = new ResultInfo();
        try {
            user = (User) request.getSession().getAttribute("user");
        } catch (Exception e) {
            info.setFlag(false);
            info.setErrorMsg("0");
            commonUtils.writeValue(info, response);
            return;
        }
        int id = user.getId();

        List<OrderInfo> orderList = orderService.findHouseRentedForUser(id);
        commonUtils.writeValue(orderList, response);
    }

    @RequestMapping("/findUserRentedHouse")
    public void findUserRentedHouse(HttpServletRequest request, HttpServletResponse response) throws IOException {

        User user = null;
        ResultInfo info = new ResultInfo();
        try {
            user = (User) request.getSession().getAttribute("user");
        } catch (Exception e) {
            info.setFlag(false);
            info.setErrorMsg("0");
            commonUtils.writeValue(info, response);
            return;
        }

        int id = user.getId();

        List<OrderInfo> appointmentList = orderService.findUserRentedHouse(id);
        commonUtils.writeValue(appointmentList, response);
    }


    @RequestMapping("/acceptOrder")
    public void acceptOrder(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
        String id = request.getParameter("id");
        System.out.println(id);
        orderService.acceptOrder(id);
        ResultInfo info = new ResultInfo();
        OrderInfo order = orderService.findOrderInfoById(id);

        Transaction t1 = new Transaction();
        t1.setUserId(order.getOwnerId());
        t1.setDateTime(new SimpleDateFormat("yyyy/MMM/dd HH:mm:ss").format(Calendar.getInstance().getTime()));
        t1.setDescription("Received RM" + order.getRental() + " house rental from " + order.getTenantName());
        userService.recordTransaction(t1);


        info.setFlag(true);
        commonUtils.writeValue(info, response);
    }


    @RequestMapping("/viewOrderTenant")
    public String viewOrderTenant(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException, ParseException {
        User user = null;
        try {
            user = (User) request.getSession().getAttribute("user");
            user.getId();

        } catch (Exception e) {
            return "login";
        }

        String id = request.getParameter("id");
        model.addAttribute("orderId", id);
        return "viewOrderTenant";
    }


    @RequestMapping("/viewOrderOwner")
    public String viewOrderOwner(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException, ParseException {
        User user = null;
        try {
            user = (User) request.getSession().getAttribute("user");
            user.getId();

        } catch (Exception e) {
            return "login";
        }
        String id = request.getParameter("id");
        model.addAttribute("orderId", id);
        return "viewOrderOwner";
    }


    @RequestMapping("/findOrderDetailsById")
    public void findOrderDetailsById(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
        User user = null;
        ResultInfo info = new ResultInfo();
        try {
            user = (User) request.getSession().getAttribute("user");
        } catch (Exception e) {
            String a = null;
            commonUtils.writeValue(a, response);
            return;
        }

        if (user == null) {
            String a = null;
            commonUtils.writeValue(a, response);
            return;
        }
        String id = request.getParameter("id");
        OrderInfo orderInfo = null;
        try {
            orderInfo = orderService.findOrderInfoById(id);
        } catch (Exception e) {
            commonUtils.writeValue(null, response);
            return;
        }
        if (user.getType().equals("a")) {
            List<OrderDetails> orderDetailsList = orderService.findOrderDetailsListById(id);
            commonUtils.writeValue(orderDetailsList, response);
        } else if (Integer.parseInt(orderInfo.getTenantId()) == user.getId() || Integer.parseInt(orderInfo.getOwnerId()) == user.getId()) {
            List<OrderDetails> orderDetailsList = orderService.findOrderDetailsListById(id);
            commonUtils.writeValue(orderDetailsList, response);
            return;
        } else {
            commonUtils.writeValue(null, response);
            return;
        }


    }


    @RequestMapping("/findOrderInfoById")
    public void findOrderInfoById(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
        String id = request.getParameter("id");
        User user = null;
        try {
            user = (User) request.getSession().getAttribute("user");
        } catch (Exception e) {
            String a = null;
            commonUtils.writeValue(a, response);
            return;
        }

        if (user == null) {
            String a = null;
            commonUtils.writeValue(a, response);
            return;
        }
        OrderInfo order = orderService.findOrderInfoById(id);
        commonUtils.writeValue(order, response);
    }


    @RequestMapping("/payRental")
    public void payRental(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
        String orderDetailsId = request.getParameter("id");
        User user = null;
        ResultInfo info = new ResultInfo();
        try {
            user = (User) request.getSession().getAttribute("user");
        } catch (Exception e) {
            String a = null;
            commonUtils.writeValue(a, response);
            return;
        }

        if (user == null) {
            String a = null;
            commonUtils.writeValue(a, response);
            return;
        }

        OrderDetails orderDetails = orderService.findOrderDetailsById(orderDetailsId);

        OrderInfo orderInfo = orderService.findOrderInfoById(Integer.toString(orderDetails.getOrderId()));

        if (!orderInfo.getTenantId().equals(Integer.toString(user.getId()))) {
            info.setFlag(false);
            info.setErrorMsg("Error, Please Try Again!");
            return;
        }


        Boolean bool = orderService.payRental(orderDetailsId, orderInfo, user);

        if (bool) {
            Transaction t1 = new Transaction();
            t1.setUserId(orderInfo.getTenantId());
            t1.setDateTime(new SimpleDateFormat("yyyy/MMM/dd HH:mm:ss").format(Calendar.getInstance().getTime()));
            t1.setDescription("Paid RM" + orderInfo.getRental() + " for house rental to " + orderInfo.getOwnerName());
            userService.recordTransaction(t1);

            Transaction t2 = new Transaction();
            t2.setUserId(orderInfo.getOwnerId());
            t2.setDateTime(new SimpleDateFormat("yyyy/MMM/dd HH:mm:ss").format(Calendar.getInstance().getTime()));
            t2.setDescription("Received RM" + orderInfo.getRental() + " for house rental from " + orderInfo.getTenantName());
            userService.recordTransaction(t2);
        } else {
            return;
        }

        commonUtils.writeValue(bool, response);


//        commonUtils.writeValue(order,response);

    }

    @RequestMapping("/cancelOrderOwner")
    public void cancelOrderOwner(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
        String orderId = request.getParameter("id");
        System.out.println(orderId);
        OrderInfo orderInfo = orderService.findOrderInfoById(orderId);
        User user = null;
        ResultInfo info = new ResultInfo();//1 error owner 2// not enough money
        try {
            user = (User) request.getSession().getAttribute("user");
        } catch (Exception e) {
            String a = null;
            commonUtils.writeValue(a, response);
            return;
        }

        if (user == null) {
            String a = null;
            commonUtils.writeValue(a, response);
            return;
        }

        if (!Integer.toString(user.getId()).equals(orderInfo.getOwnerId())) {
            info.setFlag(false);
            info.setErrorMsg("1");
            return;
        }
        User user1 = userService.findEditAccountInfo(Integer.toString(user.getId()));
        user = user1;

        int rentalPay = orderInfo.getRental() * 3;
        if (user.getBalance() < rentalPay) {
            info.setFlag(false);
            info.setErrorMsg("2");
            commonUtils.writeValue(info, response);

            return;
        }

        orderService.cancelOrderOwner(orderId, user, rentalPay);
        info.setFlag(true);

        Transaction t1 = new Transaction();
        t1.setUserId(Integer.toString(user.getId()));
        t1.setDateTime(new SimpleDateFormat("yyyy/MMM/dd HH:mm:ss").format(Calendar.getInstance().getTime()));
        t1.setDescription("Pay RM" + rentalPay + " for deposit to cancel order");
        userService.recordTransaction(t1);

        commonUtils.writeValue(info, response);
    }

    @RequestMapping("/cancelOrderTenant")
    public void cancelOrderTenant(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
        String orderId = request.getParameter("id");
        System.out.println(orderId);
        OrderInfo orderInfo = orderService.findOrderInfoById(orderId);
        User user = null;
        ResultInfo info = new ResultInfo();//1 error owner
        try {
            user = (User) request.getSession().getAttribute("user");
        } catch (Exception e) {
            String a = null;
            commonUtils.writeValue(a, response);
            return;
        }

        if (user == null) {
            String a = null;
            commonUtils.writeValue(a, response);
            return;
        }

        if (!Integer.toString(user.getId()).equals(orderInfo.getTenantId())) {
            info.setFlag(false);
            info.setErrorMsg("1");
            return;
        }

        User user1 = userService.findEditAccountInfo(Integer.toString(user.getId()));
        user = user1;

        orderService.cancelOrderTenant(orderId, user);
        info.setFlag(true);
        commonUtils.writeValue(info, response);
//      set order status
    }


    @RequestMapping("/adminManageOrders")
    public void adminManageOrders(HttpServletRequest request, HttpServletResponse response) throws IOException {

        User user = null;

        ResultInfo info = new ResultInfo();
        try {
            user = (User) request.getSession().getAttribute("user");
        } catch (Exception e) {
            String nullValue = null;
            commonUtils.writeValue(nullValue, response);
            return;
        }

        if (!user.getType().equals("a")) {
            String nullValue = null;
            commonUtils.writeValue(nullValue, response);
            return;
        }
        List<OrderInfo> allOrders = orderService.findAllOrders();

        commonUtils.writeValue(allOrders, response);
    }


    @RequestMapping("/viewOrderAdmin")
    public String viewOrderAdmin(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException, ParseException {
        String id = request.getParameter("id");
        model.addAttribute("orderId", id);
        return "viewOrderAdmin";
    }


    @RequestMapping("/depositToOwner")
    public void depositToOwner(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException, ParseException {
        String id = request.getParameter("id");
        OrderInfo order = orderService.findOrderInfoById(id);
        int deposit = order.getDeposit();
        String ownerId = order.getOwnerId();
        userService.topupWallet(Integer.parseInt(ownerId), deposit);
        orderService.setStatusByOrderId(id, 8);
        houseService.setHouseStatusById(Integer.toString(order.getHouseId()), "ready");
        ResultInfo info = new ResultInfo();
        info.setFlag(true);

        Transaction t1 = new Transaction();
        t1.setUserId(order.getOwnerId());
        t1.setDateTime(new SimpleDateFormat("yyyy/MMM/dd HH:mm:ss").format(Calendar.getInstance().getTime()));
        t1.setDescription("Received RM" + deposit + " deposit from admin");
        userService.recordTransaction(t1);

        commonUtils.writeValue(info, response);

    }

    @RequestMapping("/depositToTenant")
    public void depositToTenant(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException, ParseException {
        String id = request.getParameter("id");
        OrderInfo order = orderService.findOrderInfoById(id);
        int deposit = order.getDeposit();
        String tenantId = order.getTenantId();
        userService.topupWallet(Integer.parseInt(tenantId), deposit);
        orderService.setStatusByOrderId(id, 7);
        houseService.setHouseStatusById(Integer.toString(order.getHouseId()), "ready");
        ResultInfo info = new ResultInfo();
        info.setFlag(true);

        Transaction t1 = new Transaction();
        t1.setUserId(order.getTenantId());
        t1.setDateTime(new SimpleDateFormat("yyyy/MMM/dd HH:mm:ss").format(Calendar.getInstance().getTime()));
        t1.setDescription("Received RM" + deposit + " deposit from admin");
        userService.recordTransaction(t1);


        commonUtils.writeValue(info, response);

    }


    @RequestMapping("/declineOrder")
    public void declineOrder(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
        String id = request.getParameter("id");
        System.out.println(id);
        orderService.declineOrder(id);
        ResultInfo info = new ResultInfo();

        OrderInfo order = orderService.findOrderInfoById(id);
        Transaction t1 = new Transaction();
        t1.setUserId(order.getTenantId());
        t1.setDateTime(new SimpleDateFormat("yyyy/MMM/dd HH:mm:ss").format(Calendar.getInstance().getTime()));
        t1.setDescription("Received RM" + order.getRental() * 4 + " from rejected house rental and deposit");
        userService.recordTransaction(t1);

        info.setFlag(true);
        commonUtils.writeValue(info, response);
    }
}
