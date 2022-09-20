package com.fyp.houserental.domain;

public class OrderDetails {
    private int DetailsId;
    private String Month;
    private int OrderId;
    private int Sort;
    private String billImgUrl;
    private int RentStatus;//0unpaid 1 paid 2 cancel

    public int getDetailsId() {
        return DetailsId;
    }

    public void setDetailsId(int detailsId) {
        DetailsId = detailsId;
    }

    public String getMonth() {
        return Month;
    }

    public void setMonth(String month) {
        Month = month;
    }

    public int getOrderId() {
        return OrderId;
    }

    public void setOrderId(int orderId) {
        OrderId = orderId;
    }

    public int getSort() {
        return Sort;
    }

    public void setSort(int sort) {
        Sort = sort;
    }

    public String getBillImgUrl() {
        return billImgUrl;
    }

    public void setBillImgUrl(String billImgUrl) {
        this.billImgUrl = billImgUrl;
    }

    public int getRentStatus() {
        return RentStatus;
    }

    public void setRentStatus(int rentStatus) {
        RentStatus = rentStatus;
    }
}
