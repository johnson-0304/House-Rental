package com.fyp.houserental.dao.impl;

import com.fyp.houserental.dao.HouseDao;
import com.fyp.houserental.domain.Appointment;
import com.fyp.houserental.domain.House;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Component
public class HouseDaoImpl implements HouseDao {

    @Autowired
    private JdbcTemplate template;

    @Override
    public void save(House house) {
        String sql = "insert into House (building_Name,address,rental,state,description,type,square_ft,furnish,car_park,bedroom,bathroom,image_url,owner_name,status,house_owner_id,main_image_url,phone,agreement) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        template.update(sql, house.getBuildingName(), house.getAddress(), house.getRental(), house.getState(), house.getDescription(), house.getType(), house.getSquareFt(), house.getFurnish(), house.getCarPark(), house.getBedroom(), house.getBathroom(), house.getImageUrl(), house.getOwner_name(), house.getStatus(), house.getHouseOwnerId(), house.getMainImageUrl(), house.getPhone(),house.getAgreement());
    }

    @Override
    public List<House> findAll() {
        String sql = "select * from house where status='ready' ORDER BY id ASC";
        return template.query(sql, new BeanPropertyRowMapper<House>(House.class));
    }

    @Override
    public List<House> findAllUserHouse(String username) {
        String sql = "select * from house where owner_name = ?";

        return template.query(sql, new BeanPropertyRowMapper<House>(House.class), username);
    }

    @Override
    public House findEditHouseInfo(String id) {
        String sql = "select * from house where id = ?";
        House house = template.queryForObject(sql, new BeanPropertyRowMapper<House>(House.class), id);
        return house;
    }

    @Override
    public void updateHouseInfo(House house) {

        String sql = "update house set building_Name = ?,address = ?,rental = ?,state = ?,description = ?,type = ?,square_ft = ?,furnish = ?,car_park = ?,bedroom = ?,bathroom = ? where id = ?";
        template.update(sql, house.getBuildingName(), house.getAddress(), house.getRental(), house.getState(), house.getDescription(), house.getType(), house.getSquareFt(), house.getFurnish(), house.getCarPark(), house.getBedroom(), house.getBathroom(), house.getID());
    }

    @Override
    public String checkOwnerNameById(int id) {
        String sql = "select * from house where id = ?";
        House house = template.queryForObject(sql, new BeanPropertyRowMapper<House>(House.class), id);
        String ownerName = house.getOwner_name();
        return ownerName;
    }

    @Override
    public void updateHouseAgreement(House house) {
        String sql = "update house set agreement = ? where id = ?";
        template.update(sql, house.getAgreement(), house.getID());
    }

    @Override
    public void updateHouseImageById(Integer id, String s) {
        String sql1 = "select image_url from house where id =?";
        String imagePath = template.queryForObject(sql1, String.class, id);
        System.out.println("imagre url in database：" + imagePath);
        String path[] = imagePath.split(",");
        String property = System.getProperty("user.dir");
        property = property + "\\src\\main\\resources\\static\\";
        for (int i = 0; i < path.length; i++) {
            String p = property + path[i].replace("/", "\\");
            System.out.println("regenerate imagre url：" + p);
            File file = new File(p);
            if (file.exists()) {
                file.delete();
            }

        }
        String sql2 = "update house set image_url = ? where id = ?";
        template.update(sql2, s, id);
    }

    @Override
    public void updateHouseMainImageById(Integer id, String s) {
        String sql1 = "select main_image_url from house where id =?";
        String imagePath = template.queryForObject(sql1, String.class, id);
        System.out.println("imagre url in database：" + imagePath);
        String path[] = imagePath.split(",");
        String property = System.getProperty("user.dir");
        property = property + "\\src\\main\\resources\\static\\";
        for (int i = 0; i < path.length; i++) {
            String p = property + path[i].replace("/", "\\");
            System.out.println("regenerate imagre url：" + p);
            File file = new File(p);
            if (file.exists()) {
                file.delete();
            }

        }
        String sql2 = "update house set main_image_url = ? where id = ?";
        String sql3 = "update appointment set image_url = ? where houseID = ?";
        String sql4 = "update orderinfo set image_url = ? where houseId = ?";

        template.update(sql2, s, id);
        template.update(sql3, s, id);
        template.update(sql4, s, id);
    }

    @Override
    public int findActiveAppointmentByUserId(int id) {
        int count;

        try {
            String sql = "select count(*) from appointment where tenant_id = ? and status = 1";
            count = template.queryForObject(sql, Integer.class, id);
        } catch (DataAccessException e) {
            count = 0;
        }

        return count;
    }

    @Override
    public void registerAppointment(Appointment appointment) {
        String sql = "insert into appointment (time,owner_name,owner_id,tenant_name,tenant_id,status,owner_phone,tenant_phone,date,image_url,building_name,houseID) values(?,?,?,?,?,?,?,?,?,?,?,?)";
        template.update(sql, appointment.getTime(), appointment.getOwner_name(), appointment.getOwner_id(), appointment.getTenant_name(), appointment.getTenant_id(), appointment.getStatus(), appointment.getOwner_phone(), appointment.getTenant_phone(), appointment.getDate(), appointment.getImage_url(), appointment.getBuilding_name(), appointment.getHouseID());
    }

    @Override
    public List<Appointment> findAppointmentForUser(int id) {
        String sql = "select * from appointment where owner_id = ? ORDER BY appointmentID DESC";

        return template.query(sql, new BeanPropertyRowMapper<Appointment>(Appointment.class), id);
    }

    @Override
    public List<Appointment> findUserAppointment(int id) {
        String sql = "select * from appointment where tenant_id = ? ORDER BY appointmentID DESC";
        return template.query(sql, new BeanPropertyRowMapper<Appointment>(Appointment.class), id);
    }

    @Override
    public void setHouseStatusById(String id, String status) {
        String sql = "update house set status = ? where id = ?";
        template.update(sql, status, id);
    }

    @Override
    public void setAppointmentStatusById(String id, int status) {
        String sql = "update appointment set status = ? where appointmentID = ?";
        template.update(sql, status, id);
    }

    @Override
    public void updateAppointmentHouseMainImageById(Integer id, String s) {

    }

    @Override
    public List<House> searchHouse(String keyword, String state, String minRental, String maxRental, String furnish, String bedroom, String bathroom) {
        System.out.println("---" + keyword + "---" + state + "---" + minRental + "---" + maxRental + "---" + furnish + "---" + bedroom + "---" + bathroom);


        String sql = "select * from house where";

        if (keyword == null){

        } else {
            keyword = "'%" + keyword + "%'";
            sql += "(building_name like"+keyword+" or address like "+keyword+") and ";
        }


        if(state.equalsIgnoreCase("state")){
            sql += "(state = " + state + ")";
        } else {
            state = "'"+state+"'";
            sql += "(state = " + state + ")";
        }
        sql += "and" + "(rental between " + minRental + " and " + maxRental + " )";
        if(furnish.equalsIgnoreCase("furnish")){
            sql += "and" + "(furnish = " + furnish + " )";
        } else {
            furnish = "'"+furnish+"'";
            sql += "and" + "(furnish = " + furnish + " )";
        }

        if(bedroom.equalsIgnoreCase("bedroom")){
            sql += "and" + "(bedroom = " + bedroom + " )";
        } else {
            bedroom = "'"+bedroom+"'";
            sql += "and" + "(bedroom = " + bedroom + " )";
        }

        if(bathroom.equalsIgnoreCase("bathroom")){
            sql += "and" + "(bathroom = " + bathroom + " )";
        } else {
            bathroom = "'"+bathroom+"'";
            sql += "and" + "(bathroom = " + bathroom + " )";
        }
        System.out.println(sql);

        return template.query(sql, new BeanPropertyRowMapper<House>(House.class));


//        return template.query(sql,new BeanPropertyRowMapper<House>(House.class));
    }

    @Override
    public void setStautsTo(String status, Integer id) {
        String sql = "update house set status = ? where id = ?";
        template.update(sql, status, id);
    }

    @Override
    public House findHouseById(Integer id) {
        String sql = "select * from house where id = ?";
        House house = template.queryForObject(sql, new BeanPropertyRowMapper<House>(House.class), id);
        return house;
    }


}
