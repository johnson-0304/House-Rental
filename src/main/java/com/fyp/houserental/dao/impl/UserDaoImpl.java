package com.fyp.houserental.dao.impl;


import com.fyp.houserental.dao.UserDao;
import com.fyp.houserental.domain.House;
import com.fyp.houserental.domain.Transaction;
import com.fyp.houserental.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Component
public class UserDaoImpl implements UserDao {

    @Autowired
    private JdbcTemplate template;
    @Autowired
    private DataSource dataSource;

    public List<User> findAllUser(){

        List<User> UserList;
        String sql = "select * from user";
        UserList = template.query(sql,new BeanPropertyRowMapper<User>(User.class));
        return UserList;
    }


    @Override
    public boolean checkUsername(String username) {
        User user = null;
        try {

            String sql = "select * from user where username = ?";
            user = template.queryForObject(sql,new BeanPropertyRowMapper<User>(User.class),username);
        } catch (Exception e) {

            return true;
        }
        if (user == null){
            return true;
        } else {
            return false;
        }


    }

    @Override
    public boolean checkEmail(String email) {
        try {
            User user = null;
            String sql = "select * from user where email = ?";
            user = template.queryForObject(sql,new BeanPropertyRowMapper<User>(User.class),email);

            if (user == null){
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {

            return true;
        }
    }

    @Override
    public void save(User user) {
        int balance = 0;
        String sql = "insert into user (username,password,email,type,phone,balance) values(?,?,?,?,?,?)";
        template.update(sql,user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                user.getType(),
                user.getPhone(),
                balance);
    }

    @Override
    public List<User> findUserByName() {
        String sql = "select * from user";
        List<User> userList = template.query(sql, new BeanPropertyRowMapper<>(User.class));
        return  userList;
    }

    @Override
    public List<Map<String, Object>> queryAll() {
        String sql = "select * from user";
        List<Map<String, Object>> userList = template.queryForList(sql);
        return userList;
    }

    @Override
    public User findUserByNameAndPassword(String username, String password) {
        User user = null;

        try {
            String sql = "select * from user where email = ? and password = ?";
            user = template.queryForObject(sql,new BeanPropertyRowMapper<User>(User.class),username,password);
        } catch (Exception e){

        }


        return user;
    }

    @Override
    public User findEditAccountInfo(String id) {
        String sql = "select * from user where id = ?";
        User user = template.queryForObject(sql,new BeanPropertyRowMapper<User>(User.class),id);
        return user;
    }

    @Override
    public void updateAccountInfo(User user) {
        String sql = "update user set phone = ?, password = ? where id = ?";
        template.update(sql,user.getPhone(),user.getPassword(),user.getId());
    }

    @Override
    public void topupWallet(int id1, int amount1) {
        String sql = "update user set balance = balance + ?  where id = ?";
        template.update(sql,amount1,id1);
    }

    @Override
    public void deductBalanceById(int totalPayment, int id) {
        String sql = "update user set balance = balance - ?  where id = ?";
        template.update(sql,totalPayment,id);
    }

    @Override
    public User adminLogin(String username, String password) {
        User user = null;

        try {
            String sql = "select * from user where email = ? and password = ? and type = 'a'";
            user = template.queryForObject(sql,new BeanPropertyRowMapper<User>(User.class),username,password);
        } catch (Exception e){
        }
        return user;
    }

    @Override
    public void recordTransaction(Transaction t1) {
        int balance = 0;
        String sql = "insert into transaction (UserId,Description,DateTime) values(?,?,?)";
        template.update(sql,t1.getUserId(),t1.getDescription(),t1.getDateTime());
    }

    @Override
    public List<Transaction> findTransactionListById(int id) {
        String sql = "select * from transaction where UserId = ? ORDER BY id DESC";
        List<Transaction> tList = template.query(sql, new BeanPropertyRowMapper<>(Transaction.class),id);
        return tList;
    }

    @Override
    public boolean checkPhone(String phone) {
        User user = null;
        try {

            String sql = "select * from user where phone = ?";
            user = template.queryForObject(sql,new BeanPropertyRowMapper<User>(User.class),phone);
        } catch (Exception e) {

            return true;
        }
        if (user == null){
            return true;
        } else {
            return false;
        }
    }


}
