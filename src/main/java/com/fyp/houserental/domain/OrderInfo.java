package com.fyp.houserental.domain;

public class OrderInfo {
    private int OrderId;
    private String StartDate;
    private String EndDate;
    private int Duration;
    private String OwnerId;
    private String OwnerName;
    private String TenantId;
    private String TenantName;
    private int Deposit;
    private int Rental;
    private String OwnerPhone;
    private String TenantPhone;
    //status, 1: waiting, 2: renting, 3:decline, 0:cancel by owner, 4: wait deposit, 9 cancelled by tenant, 7: deposit to tenant, 8: deposit to owner
    private String Status;
    private String Agreement;
    private int HouseId;
    private String image_url;
    private String building_name;

    public String getOwnerPhone() {
        return OwnerPhone;
    }

    public void setOwnerPhone(String ownerPhone) {
        OwnerPhone = ownerPhone;
    }

    public String getBuilding_name() {
        return building_name;
    }

    public void setBuilding_name(String building_name) {
        this.building_name = building_name;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public int getHouseId() {
        return HouseId;
    }

    public void setHouseId(int houseId) {
        HouseId = houseId;
    }

    public int getDuration() {
        return Duration;
    }

    public void setDuration(int duration) {
        Duration = duration;
    }

    public String getAgreement() {
        return Agreement;
    }

    public void setAgreement(String agreement) {
        Agreement = agreement;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public int getOrderId() {
        return OrderId;
    }

    public void setOrderId(int orderId) {
        OrderId = orderId;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public String getOwnerId() {
        return OwnerId;
    }

    public void setOwnerId(String ownerId) {
        OwnerId = ownerId;
    }

    public String getOwnerName() {
        return OwnerName;
    }

    public void setOwnerName(String ownerName) {
        OwnerName = ownerName;
    }

    public String getTenantId() {
        return TenantId;
    }

    public void setTenantId(String tenantId) {
        TenantId = tenantId;
    }

    public String getTenantName() {
        return TenantName;
    }

    public void setTenantName(String tenantName) {
        TenantName = tenantName;
    }

    public int getDeposit() {
        return Deposit;
    }

    public void setDeposit(int deposit) {
        Deposit = deposit;
    }

    public int getRental() {
        return Rental;
    }

    public void setRental(int rental) {
        Rental = rental;
    }

    public String getOnwerPhone() {
        return OwnerPhone;
    }

    public void setOnwerPhone(String onwerPhone) {
        OwnerPhone = onwerPhone;
    }

    public String getTenantPhone() {
        return TenantPhone;
    }

    public void setTenantPhone(String tenantPhone) {
        TenantPhone = tenantPhone;
    }

    @Override
    public String toString() {
        return "Order{" +
                "OrderId='" + OrderId + '\'' +
                ", StartDate=" + StartDate +
                ", EndDate=" + EndDate +
                ", OwnerId='" + OwnerId + '\'' +
                ", OwnerName='" + OwnerName + '\'' +
                ", TenantId='" + TenantId + '\'' +
                ", TenantName='" + TenantName + '\'' +
                ", Deposit=" + Deposit +
                ", Rental=" + Rental +
                ", OnwerPhone='" + OwnerPhone + '\'' +
                ", TenantPhone='" + TenantPhone + '\'' +
                '}';
    }
}
