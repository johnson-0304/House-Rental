package com.fyp.houserental.service.impl;

import com.fyp.houserental.dao.HouseDao;
import com.fyp.houserental.dao.OrderDao;
import com.fyp.houserental.dao.UserDao;
import com.fyp.houserental.domain.Appointment;
import com.fyp.houserental.domain.OrderDetails;
import com.fyp.houserental.domain.OrderInfo;
import com.fyp.houserental.domain.User;
import com.fyp.houserental.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderDao orderDao;

    @Autowired
    UserDao userDao;

    @Autowired
    HouseDao houseDao;

    @Override
    public void registerOrder(OrderInfo order) throws ParseException {
        orderDao.save(order);

    }

    @Override
    public void acceptOrder(String id) throws ParseException {
        OrderInfo order = orderDao.getOrderById(id);
        String date = order.getStartDate();
        int duration = order.getDuration();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd", Locale.UK);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MMM-dd", Locale.UK);
        Date dt = sdf.parse(date);
        String date1 = formatter.format(dt);
        List<String> dateList = new ArrayList<>();
        Calendar rightNow = Calendar.getInstance();

        for (int i = 0; i < duration; i++) {
            rightNow.setTime(dt);
            rightNow.add(Calendar.MONTH, (i+1));

            Date dt1 = rightNow.getTime();
            String date2 = formatter.format(dt1);
            dateList.add(date2);
        }

        orderDao.registerOrderDetailsList(dateList,order);
        //add money to house owner
        userDao.topupWallet(Integer.parseInt(order.getOwnerId()), order.getRental());
        int status = 0;
        orderDao.cancelWaitingOrder(order.getHouseId(),status);
        orderDao.setOrderStatusById(order.getOrderId(),2);

        //set house to renting
        houseDao.setHouseStatusById(Integer.toString(order.getHouseId()),"Renting");
    }

    @Override
    public void cancelOrder(OrderInfo order) throws ParseException {
        //send money to tenant
    }

    @Override
    public List<OrderInfo> findHouseRentedForUser(int id) {
        return orderDao.findHouseRentedForUser(id);
    }

    @Override
    public List<OrderInfo> findUserRentedHouse(int id) {
        return orderDao.findUserRentedHouse(id);
    }

    @Override
    public List<OrderDetails> findOrderDetailsListById(String id) {
        return orderDao.findOrderDetailsListById(id);
    }

    @Override
    public OrderInfo findOrderInfoById(String id) {
        return orderDao.getOrderById(id);
    }

    @Override
    public void updateBillImg(Integer id, String s) {
        s = s.replace("\\","/");
        orderDao.updateBillImg(id,s);
    }

    @Override
    public OrderDetails findOrderDetailsById(String orderDetailsId) {
        return orderDao.findOrderDetailsById(orderDetailsId);
    }

    @Override
    public Boolean payRental(String orderDetailsId, OrderInfo order, User user) {

        User tenant = userDao.findEditAccountInfo(Integer.toString(user.getId()));
        int rental = order.getRental();
        if (tenant.getBalance() < rental){
            return false;
        }

        userDao.deductBalanceById(rental,tenant.getId());
        userDao.topupWallet(Integer.parseInt(order.getOwnerId()),rental);
        orderDao.updateOrderDetailsStatusById(orderDetailsId);
        int rentcount = orderDao.countRentStatus(order.getOrderId());
        System.out.println("rentCount"+rentcount);
        if (rentcount == 0){
            orderDao.setOrderStatusById(order.getOrderId(),4);
        }
        return true;
    }

    @Override
    public void cancelOrderOwner(String orderId, User user,int rentalPay ) {
        orderDao.setOrderDetailsStatusByOrderId(orderId,2);
        orderDao.setOrderStatusById(Integer.parseInt(orderId),0);
        userDao.deductBalanceById(rentalPay,user.getId());
        orderDao.addDepositToOrderInfo(orderId,rentalPay);
    }

    @Override
    public void cancelOrderTenant(String orderId, User user) {
        orderDao.setOrderDetailsStatusByOrderId(orderId,2);
        orderDao.setOrderStatusById(Integer.parseInt(orderId),9);
    }

    @Override
    public List<OrderInfo> findAllOrders() {
        return orderDao.findAllOrders();
    }

    @Override
    public void setStatusByOrderId(String id, int i) {
        orderDao.setOrderStatusById(Integer.parseInt(id),i);
    }

    @Override
    public void declineOrder(String id) {
        OrderInfo order = orderDao.getOrderById(id);
        // return money
        userDao.topupWallet(Integer.parseInt(order.getTenantId()),order.getRental()*4);

        // set order status
        orderDao.setOrderStatusById(order.getOrderId(),3);
        //set house stauts
        houseDao.setHouseStatusById(Integer.toString(order.getHouseId()),"ready");
    }


}
