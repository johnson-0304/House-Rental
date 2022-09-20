package com.fyp.houserental.service.impl;

import com.fyp.houserental.dao.HouseDao;
import com.fyp.houserental.dao.OrderDao;
import com.fyp.houserental.domain.Appointment;
import com.fyp.houserental.domain.House;
import com.fyp.houserental.domain.User;
import com.fyp.houserental.service.HouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HouseServiceImpl implements HouseService {

    @Autowired
    private HouseDao houseDao;
    @Autowired
    private OrderDao orderDao;

    @Override
    public void addNewHouse(House house) {
        houseDao.save(house);
    }

    @Override
    public List<House> findAll() {
        return houseDao.findAll();
    }

    @Override
    public List<House> findAllUserHouse(String username) {
        return houseDao.findAllUserHouse(username);
    }

    @Override
    public House findEditHouseInfo(String id) {
        return houseDao.findEditHouseInfo(id);
    }

    @Override
    public void updateHouseInfo(House house) {
        houseDao.updateHouseInfo(house);
    }

    @Override
    public String checkOwnerNameById(int id) {
        return houseDao.checkOwnerNameById(id);
    }

    @Override
    public void updateHouseAgreement(House house) {
        houseDao.updateHouseAgreement(house);
    }

    /**
     * upload image url
     * @param id
     * @param s
     */
    @Override
    public void updateHouse(Integer id, String s) {
        System.out.println();
        s = s.replace("\\","/");
        houseDao.updateHouseImageById(id,s);
    }

    @Override
    public void updateHouseMainImg(Integer id, String s) {
        s = s.replace("\\","/");
        houseDao.updateHouseMainImageById(id,s);
        String status = "ready";
        houseDao.setStautsTo(status,id);
    }

    @Override
    public int findActiveAppointmentByUserId(int id) {
        return houseDao.findActiveAppointmentByUserId(id);
    }

    @Override
    public void registerAppointment(Appointment appointment) {
        houseDao.registerAppointment(appointment);
    }

    @Override
    public List<Appointment> findAppointmentForUser(int id) {
        return houseDao.findAppointmentForUser(id);
    }

    @Override
    public List<Appointment> findUserAppointment(int id) {
        return houseDao.findUserAppointment(id);
    }

    @Override
    public void setHouseStatusById(String id, String status) {
        houseDao.setHouseStatusById(id,status);
    }

    @Override
    public void setAppointmentStatusById(String id, int status) {
        houseDao.setAppointmentStatusById(id,status);
    }

    @Override
    public List<House> searchHouse(String keyword, String state, String minRental, String maxRental, String furnish, String bedroom, String bathroom) {
        return houseDao.searchHouse(keyword,state,minRental,maxRental,furnish,bedroom,bathroom);
    }


    @Override
    public House findHouseById(Integer id) {
        return houseDao.findHouseById(id);
    }

}
