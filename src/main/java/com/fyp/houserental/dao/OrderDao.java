package com.fyp.houserental.dao;

import com.fyp.houserental.domain.OrderDetails;
import com.fyp.houserental.domain.OrderInfo;

import java.util.List;

public interface OrderDao {


    void save(OrderInfo order);

    List<OrderInfo> findHouseRentedForUser(int id);

    List<OrderInfo> findUserRentedHouse(int id);

    OrderInfo getOrderById(String id);

    void registerOrderDetailsList(List<String> dateList, OrderInfo order);

    void cancelWaitingOrder(int houseId, int status);

    void setOrderStatusById(int orderId, int i);

    List<OrderDetails> findOrderDetailsListById(String id);

    void updateBillImg(Integer id, String s);

    OrderDetails findOrderDetailsById(String orderDetailsId);

    void updateOrderDetailsStatusById(String orderDetailsId);

    int countRentStatus(int orderId);

    void setOrderDetailsStatusByOrderId(String orderId, int i);

    void addDepositToOrderInfo(String orderId, int rentalPay);

    List<OrderInfo> findAllOrders();
}
