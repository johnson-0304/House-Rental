package com.fyp.houserental.dao;

import com.fyp.houserental.domain.Transaction;
import com.fyp.houserental.domain.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


public interface UserDao {

    boolean checkEmail(String name);

    boolean checkUsername(String username);

    void save (User user);

    List<User> findUserByName();

    List<Map<String,Object>> queryAll();

    User findUserByNameAndPassword(String username, String password);

    User findEditAccountInfo(String id);

    void updateAccountInfo(User user);

    void topupWallet(int id1, int amount1);

    void deductBalanceById(int totalPayment, int id);

    User adminLogin(String username, String password);

    void recordTransaction(Transaction t1);

    List<Transaction> findTransactionListById(int id);

    boolean checkPhone(String phone);
}
