package com.fyp.houserental.dao.impl;

import com.fyp.houserental.dao.OrderDao;
import com.fyp.houserental.domain.Appointment;
import com.fyp.houserental.domain.House;
import com.fyp.houserental.domain.OrderDetails;
import com.fyp.houserental.domain.OrderInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Component
public class OrderDaoImpl implements OrderDao {

    @Autowired
    private JdbcTemplate template;


    @Override
    public void save(OrderInfo order) {
        String sql = "insert into orderInfo (StartDate,EndDate,Duration,OwnerId,OwnerName,TenantId,TenantName,Deposit,Rental,OwnerPhone,TenantPhone,Status,Agreement,HouseId,image_url,building_name) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        template.update(sql,order.getStartDate(),order.getEndDate(),order.getDuration(),order.getOwnerId(),order.getOwnerName(),order.getTenantId(),order.getTenantName(),order.getDeposit(),order.getRental(),order.getOnwerPhone(),order.getTenantPhone(),order.getStatus(),order.getAgreement(),order.getHouseId(),order.getImage_url(),order.getBuilding_name());
    }

    @Override
    public List<OrderInfo> findHouseRentedForUser(int id) {
        String sql = "select * from orderInfo where OwnerId = ? ORDER BY OrderId DESC";
        return template.query(sql,new BeanPropertyRowMapper<OrderInfo>(OrderInfo.class),id);
    }

    @Override
    public List<OrderInfo> findUserRentedHouse(int id) {
        String sql = "select * from orderInfo where TenantId = ? ORDER BY OrderId DESC";
        return template.query(sql,new BeanPropertyRowMapper<OrderInfo>(OrderInfo.class),id);
    }

    @Override
    public OrderInfo getOrderById(String id) {
        String sql = "select * from orderInfo where orderid = ?";
        OrderInfo order = template.queryForObject(sql,new BeanPropertyRowMapper<OrderInfo>(OrderInfo.class),id);
        return order;
    }

    @Override
    public void registerOrderDetailsList(List<String> dateList, OrderInfo order) {
        for (int i = 0; i < dateList.size(); i++) {
            String sql = "insert into orderDetails (Month,OrderId,Sort,billImgUrl,RentStatus) values(?,?,?,?,?)";
            int sort = i+ 1;
            if (i == 0){
                template.update(sql,dateList.get(i),order.getOrderId(),sort,"0",1);
            } else {
                template.update(sql,dateList.get(i),order.getOrderId(),sort,"0",0);
            }

        }

    }

    @Override
    public void cancelWaitingOrder(int houseId, int status) {
        String sql2 = "update orderInfo set status = ? where houseid = ? and status = ?";
        template.update(sql2,status,houseId,1);
    }

    @Override
    public void setOrderStatusById(int orderId, int i) {
        String sql2 = "update orderInfo set status = ? where orderid = ?";
        template.update(sql2,i,orderId);
    }

    @Override
    public List<OrderDetails> findOrderDetailsListById(String id) {
        String sql = "select * from orderDetails where OrderId = ? ORDER BY Sort Asc";
        return template.query(sql,new BeanPropertyRowMapper<OrderDetails>(OrderDetails.class),id);
    }

    @Override
    public void updateBillImg(Integer id, String s) {
        String sql1 = "select billImgUrl from orderDetails where DetailsId = ?";
        String imagePath = template.queryForObject(sql1, String.class, id);
        System.out.println("imagre url in database："+imagePath);
        String path[] = imagePath.split(",");
        String property = System.getProperty("user.dir");
        property = property+"\\src\\main\\resources\\static\\";
        for (int i = 0; i < path.length; i++) {
            String p = property+path[i].replace("/","\\");
            System.out.println("regenerate imagre url："+p);
            File file = new File(p);
            if (file.exists()){
                file.delete();
            }

        }
        String sql2 = "update orderDetails set billImgUrl = ? where DetailsId = ?";
        template.update(sql2,s,id);
    }

    @Override
    public OrderDetails findOrderDetailsById(String orderDetailsId) {
        String sql = "select * from orderDetails where DetailsId = ?";
        OrderDetails orderDetails = template.queryForObject(sql,new BeanPropertyRowMapper<OrderDetails>(OrderDetails.class),orderDetailsId);
        return orderDetails;
    }

    @Override
    public void updateOrderDetailsStatusById(String orderDetailsId) {
        String sql = "update orderDetails set rentstatus = ? where DetailsId = ?";
        template.update(sql,1,orderDetailsId);
    }

    @Override
    public int countRentStatus(int orderId) {
        int count;

        try {
            String sql = "select count(*) from orderDetails where OrderId = ? and rentstatus = 0";
            count = template.queryForObject(sql,Integer.class,orderId);
        } catch (DataAccessException e) {
            count = 0;
        }
        return count;

    }

    @Override
    public void setOrderDetailsStatusByOrderId(String orderId, int i) {
        String sql = "update orderDetails set rentstatus = ? where OrderId = ? and rentstatus = 0";
        template.update(sql,i,orderId);
    }

    @Override
    public void addDepositToOrderInfo(String orderId, int rentalPay) {
        String sql = "update orderInfo set Deposit = Deposit + ?  where OrderId = ?";
        template.update(sql,rentalPay,orderId);
    }

    @Override
    public List<OrderInfo> findAllOrders() {
        String sql = "select * from orderInfo ORDER BY OrderId DESC";
        return template.query(sql,new BeanPropertyRowMapper<OrderInfo>(OrderInfo.class));
    }
}
