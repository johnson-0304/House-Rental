package com.fyp.houserental.service;

import com.fyp.houserental.domain.House;
import com.fyp.houserental.domain.Transaction;
import com.fyp.houserental.domain.User;

import java.util.List;

public interface UserService {
    boolean register(User user);
    User login(User user);


    User findEditAccountInfo(String id);

    void updateAccountInfo(User user);


    void topupWallet(int id1, int amount1);

    void deductBalanceById(int totalPayment, int id);

    User adminLogin(User user);

    void recordTransaction(Transaction t1);

    List<Transaction> findTransactionListById(int id);
}
