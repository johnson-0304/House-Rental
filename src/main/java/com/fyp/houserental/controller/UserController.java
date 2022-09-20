package com.fyp.houserental.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fyp.houserental.dao.UserDao;
import com.fyp.houserental.domain.House;
import com.fyp.houserental.domain.ResultInfo;
import com.fyp.houserental.domain.Transaction;
import com.fyp.houserental.domain.User;
import com.fyp.houserental.service.UserService;
import com.fyp.houserental.service.impl.UserServiceImpl;
import com.fyp.houserental.util.CommonUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

@Controller
public class UserController {

    @Autowired
    private UserDao check;
    @Autowired
    private UserService service;
    @Autowired
    private CommonUtils commonUtils;

    @RequestMapping("/findUsernameServlet")
    public void FindUsername(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Object user = null;
        user = request.getSession().getAttribute("user");


        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json");
        mapper.writeValue(response.getOutputStream(), user);
    }

    @RequestMapping("/loginServlet")
    public void Login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, String[]> map = request.getParameterMap();

        User user = new User();
        try {
            BeanUtils.populate(user, map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        User u = service.login(user);
        ResultInfo info = new ResultInfo();
        if (u == null) {
            info.setFlag(false);
            info.setErrorMsg("Wrong Email or Password");

        } else if (u.getType().equals("a")) {
            info.setFlag(false);
            info.setErrorMsg("Wrong Email or Password");
        } else {
            HttpSession session = request.getSession();
            session.setAttribute("user", u);
            info.setFlag(true);
        }


        //convert info to json

        ObjectMapper mapper = new ObjectMapper();

        //response json to client
        response.setContentType("application/json");
        mapper.writeValue(response.getOutputStream(), info);

    }

    @RequestMapping("/adminLoginServlet")
    public void adminLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, String[]> map = request.getParameterMap();

        User user = new User();
        try {
            BeanUtils.populate(user, map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        User u = service.adminLogin(user);
        ResultInfo info = new ResultInfo();
        if (u == null) {
            info.setFlag(false);
            info.setErrorMsg("Wrong Email or Password");

        } else {
            HttpSession session = request.getSession();
            session.setAttribute("user", u);
            info.setFlag(true);
        }

        //convert info to json

        ObjectMapper mapper = new ObjectMapper();

        //response json to client
        response.setContentType("application/json");
        mapper.writeValue(response.getOutputStream(), info);

    }

    @RequestMapping("/registerServlet")
    public void register(HttpServletRequest request, HttpServletResponse response) throws IOException {


        //get param
        Map<String, String[]> map = request.getParameterMap();


        //process user info
        User user = new User();
        try {
            BeanUtils.populate(user, map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        //service

        boolean checkUsername = check.checkUsername(user.getUsername());
        boolean checkEmail = check.checkEmail(user.getEmail());
        boolean checkPhone = check.checkPhone(user.getPhone());
        ResultInfo info = new ResultInfo();

        if (!checkPhone){
            info.setFlag(false);
            info.setErrorMsg("Phone Is Registered!");
            commonUtils.writeValue(info, response);
            return;
        }

        if (checkUsername & checkEmail) {
            boolean flag = service.register(user);
            if (flag) {
                //register success

                info.setFlag(true);
            } else {
                //register failed
                info.setFlag(false);
                info.setErrorMsg("register failed, please chceck your info");
            }

        } else if (checkEmail == false) {
            info.setErrorMsg("Email already registered!");
        } else if (checkUsername == false) {
            info.setErrorMsg("Username already registered!");
        } else {
            info.setErrorMsg("register failed, please check your info");
        }


        //convert info to json

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(info);

        //response json to client
        response.setContentType("application/json");
        response.getWriter().write(json);


    }

    @RequestMapping("/editAccountPage")
    public String editAccountPage(Model model, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        User user = null;
        try {
            user = (User) request.getSession().getAttribute("user");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (user != null) {
            int id = user.getId();
            model.addAttribute("userId", id);
            return "editAccount";
        } else {
            return "login";
        }

    }

    @RequestMapping("/findEditAccountInfo")
    public void findEditAccountInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User userSession = null;
        String id = request.getParameter("id");
        try {
            userSession = (User) request.getSession().getAttribute("user");
        } catch (Exception e) {
            e.printStackTrace();
        }

        User user = null;
        if (userSession != null) {
            String idSession = Integer.toString(userSession.getId());
            if (idSession.equals(id)) {
                user = service.findEditAccountInfo(id);
                commonUtils.writeValue(user, response);
            } else {
                commonUtils.writeValue(user, response);
            }
        } else {

        }
    }

    @RequestMapping("/updateAccountInfo")
    public void updateAccountInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String[]> map = request.getParameterMap();
        int id = Integer.parseInt(request.getParameter("id"));
        String phone = request.getParameter("phone");

        User user = new User();
        try {
            BeanUtils.populate(user, map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        User userSession = null;
        try {
            userSession = (User) request.getSession().getAttribute("user");
            user.getPhone();
        } catch (Exception e) {
            e.printStackTrace();
        }

        User getUser = service.findEditAccountInfo(Integer.toString(id));

        ResultInfo info = new ResultInfo();
        user.setId(id);
        boolean checkPhone = check.checkPhone(user.getPhone());

        if (!checkPhone){
            if (phone.equals(getUser.getPhone())){

            } else {
                info.setFlag(false);
                info.setErrorMsg("Phone Is Registered!");
                commonUtils.writeValue("p", response);
                return;
            }
        }
        service.updateAccountInfo(user);
        info.setFlag(true);
        commonUtils.writeValue(info, response);

    }

    @RequestMapping("/walletPage")
    public String walletPage(HttpServletRequest request, HttpServletResponse response, Model model) throws ServletException, IOException {

        User user = null;
        try {
            user = (User) request.getSession().getAttribute("user");
        } catch (Exception e) {

        }

        int id = user.getId();
        model.addAttribute("userId", id);
        return "wallet";
    }

    @RequestMapping("/getWalletAmount")
    public void getWalletAmount(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ResultInfo info = new ResultInfo();
        User user = null;
        try {
            user = (User) request.getSession().getAttribute("user");
        } catch (Exception e) {
            info.setFlag(false);
            info.setErrorMsg("Please Login First");
            commonUtils.writeValue(info, response);
            return;
        }


        User user1 = service.findEditAccountInfo(Integer.toString(user.getId()));

        int amount = user1.getBalance();

        commonUtils.writeValue(amount, response);

    }


    @RequestMapping("/getWalletTransaction")
    public void getWalletTransaction(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ResultInfo info = new ResultInfo();
        User user = null;
        try {
            user = (User) request.getSession().getAttribute("user");
        } catch (Exception e) {
            info.setFlag(false);
            info.setErrorMsg("Please Login First");
            commonUtils.writeValue(info, response);
            return;
        }


        User user1 = service.findEditAccountInfo(Integer.toString(user.getId()));
        List<Transaction> transactionList = service.findTransactionListById(user1.getId());

        commonUtils.writeValue(transactionList, response);

    }

    @RequestMapping("/topupWallet")
    public void topupWallet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String amount = request.getParameter("amount");
        String id = request.getParameter("id");
        ResultInfo info = new ResultInfo();
        User user = null;
        try {
            user = (User) request.getSession().getAttribute("user");
        } catch (Exception e) {
            info.setFlag(false);
            info.setErrorMsg("Please Login First");
            commonUtils.writeValue(info, response);
            return;
        }
        int id1;
        int amount1;


        try {
            id1 = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            info.setFlag(false);
            info.setErrorMsg("Please Login First");
            commonUtils.writeValue(info, response);
            return;
        }

        try {
            amount1 = Integer.parseInt(amount);
        } catch (NumberFormatException e) {
            info.setFlag(false);
            info.setErrorMsg("Error!Please try again");
            commonUtils.writeValue(info, response);
            return;
        }

        if (!(user.getId() == id1)) {
            info.setFlag(false);
            info.setErrorMsg("You can only topup your wallet!");
            commonUtils.writeValue(info, response);
            return;
        }

        User user1 = service.findEditAccountInfo(Integer.toString(user.getId()));

        int balance = user1.getBalance();
        if (balance > 9999999) {
            info.setFlag(false);
            info.setErrorMsg("You balance is too much! cannot topup anymore!");
            commonUtils.writeValue(info, response);
            return;
        }
        service.topupWallet(id1, amount1);
        Transaction t1 = new Transaction();
        t1.setUserId(Integer.toString(id1));
        t1.setDateTime(new SimpleDateFormat("yyyy/MMM/dd HH:mm:ss").format(Calendar.getInstance().getTime()));
        t1.setDescription("Topup RM" + amount1);
        service.recordTransaction(t1);

        info.setFlag(true);
        commonUtils.writeValue(info, response);

    }

    @RequestMapping("/logoutServlet")
    public void Logout(HttpServletRequest request, HttpServletResponse response) throws IOException {

        //remove session
        request.getSession().invalidate();

        ObjectMapper mapper = new ObjectMapper();
        ResultInfo info = new ResultInfo();
        info.setFlag(true);
        //response json to client
        response.setContentType("application/json");
        mapper.writeValue(response.getOutputStream(), info);
    }

    @RequestMapping("/")
    public String login1() {
        return "index";
    }


}
