package com.fyp.houserental.service;

import com.fyp.houserental.domain.Appointment;
import com.fyp.houserental.domain.House;
import com.fyp.houserental.domain.User;

import java.util.List;

public interface HouseService {

    void addNewHouse(House house);

    List<House> findAll();

    List<House> findAllUserHouse(String username);

    House findEditHouseInfo(String id);

    void updateHouseInfo(House house);

    String checkOwnerNameById(int id);

    void updateHouseAgreement(House house);

    void updateHouse(Integer id, String s);

    House findHouseById(Integer id);

    void updateHouseMainImg(Integer id, String s);

    int findActiveAppointmentByUserId(int id);

    void registerAppointment(Appointment appointment);

    List<Appointment> findAppointmentForUser(int id);

    List<Appointment> findUserAppointment(int id);

    void setHouseStatusById(String id, String status);

    void setAppointmentStatusById(String id, int status);

    List<House> searchHouse(String keyword, String state, String minRental, String maxRental, String furnish, String bedroom, String bathroom);
}
