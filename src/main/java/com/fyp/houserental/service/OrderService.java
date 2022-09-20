package com.fyp.houserental.service;

import com.fyp.houserental.domain.Appointment;
import com.fyp.houserental.domain.OrderDetails;
import com.fyp.houserental.domain.OrderInfo;
import com.fyp.houserental.domain.User;

import java.text.ParseException;
import java.util.List;

public interface OrderService {

    void registerOrder(OrderInfo order) throws ParseException;
    void acceptOrder(String id) throws ParseException;
    void cancelOrder(OrderInfo order) throws ParseException;


    List<OrderInfo> findHouseRentedForUser(int id);

    List<OrderInfo> findUserRentedHouse(int id);

    List<OrderDetails> findOrderDetailsListById(String id);

    OrderInfo findOrderInfoById(String id);

    void updateBillImg(Integer id, String s);

    OrderDetails findOrderDetailsById(String orderDetailsId);

    Boolean payRental(String orderDetailsId, OrderInfo order, User user);

    void cancelOrderOwner(String orderId, User user, int rentalPay);

    void cancelOrderTenant(String orderId, User user);

    List<OrderInfo> findAllOrders();

    void setStatusByOrderId(String id, int i);

    void declineOrder(String id);
}
