package com.fyp.houserental.dao;

import com.fyp.houserental.domain.Appointment;
import com.fyp.houserental.domain.House;

import java.util.List;

public interface HouseDao {

    void save (House house);

    List<House> findAll();

    List<House> findAllUserHouse(String username);

    House findEditHouseInfo(String id);

    void updateHouseInfo(House house);

    String checkOwnerNameById(int id);

    void updateHouseAgreement(House house);

    void updateHouseImageById(Integer id, String s);

    void setStautsTo(String status, Integer id);

    House findHouseById(Integer id);

    void updateHouseMainImageById(Integer id, String s);

    int findActiveAppointmentByUserId(int id);

    void registerAppointment(Appointment appointment);

    List<Appointment> findAppointmentForUser(int id);

    List<Appointment> findUserAppointment(int id);

    void setHouseStatusById(String id, String status);

    void setAppointmentStatusById(String id, int status);

    void updateAppointmentHouseMainImageById(Integer id, String s);

    List<House> searchHouse(String keyword, String state, String minRental, String maxRental, String furnish, String bedroom, String bathroom);
}
