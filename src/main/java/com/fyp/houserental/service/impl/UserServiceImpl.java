package com.fyp.houserental.service.impl;

import com.fyp.houserental.dao.UserDao;
import com.fyp.houserental.dao.impl.UserDaoImpl;
import com.fyp.houserental.domain.House;
import com.fyp.houserental.domain.Transaction;
import com.fyp.houserental.domain.User;
import com.fyp.houserental.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    @Override
    public boolean register(User user) {
        //check user exists
        User u = null;// userDao.findUserByName(user.getUsername());
        if (u != null){
            return false;
        }
        userDao.save(user);

        return true;
    }

    @Override
    public User login(User user) {
        return userDao.findUserByNameAndPassword(user.getUsername(),user.getPassword());
    }

    @Override
    public User findEditAccountInfo(String id) {
        return userDao.findEditAccountInfo(id);
    }

    @Override
    public void updateAccountInfo(User user) {
        userDao.updateAccountInfo(user);
    }

    @Override
    public void topupWallet(int id1, int amount1) {
        userDao.topupWallet(id1,amount1);
    }

    @Override
    public void deductBalanceById(int totalPayment, int id) {
        userDao.deductBalanceById(totalPayment,id);
    }

    @Override
    public User adminLogin(User user) {
        return userDao.adminLogin(user.getUsername(),user.getPassword());
    }

    @Override
    public void recordTransaction(Transaction t1) {
        userDao.recordTransaction(t1);
    }

    @Override
    public List<Transaction> findTransactionListById(int id) {
        return userDao.findTransactionListById(id);
    }

}
