package ru.imagebook.server.service2.app.delivery;


public class DeliveryConfig {

    // MajorExpress
    private String majorCalcUrl;
    private String majorCitysUrl;
    private String majorClientName;
    private String majorCityName;
    private int majorWbCost;

    // PickPoint
    private int pickPointSendPeriodicity;
    private int pickPointTimeout;
    private String pickPointUrl;
    private String pickPointLogin;
    private String pickPointPassword;
    private String pickPointIkn;
    private String pickPointReturnAddressCity;
    private String pickPointReturnAddressRegion;
    private String pickPointReturnAddressStreetAddress;
    private String pickPointReturnAddressContactPerson;
    private String pickPointReturnAddressZip;
    private String pickPointReturnAddressOrganisation;
    private String pickPointReturnAddressPhone;
    private String pickPointReturnAddressComment;

    // SDEK
    private String sdekUrl;
    private String sdekAccount;
    private String sdekSecurePassword;
    private int sdekSendDelayInSec;
    private int sdekSendCityCode;
    private String sdekSellerName;
    private String sdekSellerPhone;

    public String getMajorCalcUrl() {
        return majorCalcUrl;
    }

    public void setMajorCalcUrl(String majorCalcUrl) {
        this.majorCalcUrl = majorCalcUrl;
    }

    public String getMajorCitysUrl() {
        return majorCitysUrl;
    }

    public void setMajorCitysUrl(String majorCitysUrl) {
        this.majorCitysUrl = majorCitysUrl;
    }

    public String getMajorClientName() {
        return majorClientName;
    }

    public void setMajorClientName(String majorClientName) {
        this.majorClientName = majorClientName;
    }

    public String getMajorCityName() {
        return majorCityName;
    }

    public void setMajorCityName(String majorCityName) {
        this.majorCityName = majorCityName;
    }

    public int getMajorWbCost() {
        return majorWbCost;
    }

    public void setMajorWbCost(int majorWbCost) {
        this.majorWbCost = majorWbCost;
    }

    public int getPickPointSendPeriodicity() {
        return pickPointSendPeriodicity;
    }

    public void setPickPointSendPeriodicity(int pickPointSendPeriodicity) {
        this.pickPointSendPeriodicity = pickPointSendPeriodicity;
    }

    public int getPickPointTimeout() {
        return pickPointTimeout;
    }

    public void setPickPointTimeout(int pickPointTimeout) {
        this.pickPointTimeout = pickPointTimeout;
    }

    public String getPickPointUrl() {
        return pickPointUrl;
    }

    public void setPickPointUrl(String pickPointUrl) {
        this.pickPointUrl = pickPointUrl;
    }

    public String getPickPointLogin() {
        return pickPointLogin;
    }

    public void setPickPointLogin(String pickPointLogin) {
        this.pickPointLogin = pickPointLogin;
    }

    public String getPickPointPassword() {
        return pickPointPassword;
    }

    public void setPickPointPassword(String pickPointPassword) {
        this.pickPointPassword = pickPointPassword;
    }

    public String getPickPointIkn() {
        return pickPointIkn;
    }

    public void setPickPointIkn(String pickPointIkn) {
        this.pickPointIkn = pickPointIkn;
    }

    public void setPickPointReturnAddressCity(String pickPointReturnAddressCity) {
        this.pickPointReturnAddressCity = pickPointReturnAddressCity;
    }

    public String getPickPointReturnAddressCity() {
        return pickPointReturnAddressCity;
    }

    public void setPickPointReturnAddressRegion(String pickPointReturnAddressRegion) {
        this.pickPointReturnAddressRegion = pickPointReturnAddressRegion;
    }

    public String getPickPointReturnAddressRegion() {
        return pickPointReturnAddressRegion;
    }

    public void setPickPointReturnAddressStreetAddress(String pickPointReturnAddressStreetAddress) {
        this.pickPointReturnAddressStreetAddress = pickPointReturnAddressStreetAddress;
    }

    public String getPickPointReturnAddressStreetAddress() {
        return pickPointReturnAddressStreetAddress;
    }

    public void setPickPointReturnAddressContactPerson(String pickPointReturnAddressContactPerson) {
        this.pickPointReturnAddressContactPerson = pickPointReturnAddressContactPerson;
    }

    public String getPickPointReturnAddressContactPerson() {
        return pickPointReturnAddressContactPerson;
    }

    public void setPickPointReturnAddressZip(String pickPointReturnAddressZip) {
        this.pickPointReturnAddressZip = pickPointReturnAddressZip;
    }

    public String getPickPointReturnAddressZip() {
        return pickPointReturnAddressZip;
    }

    public void setPickPointReturnAddressOrganisation(String pickPointReturnAddressOrganisation) {
        this.pickPointReturnAddressOrganisation = pickPointReturnAddressOrganisation;
    }

    public String getPickPointReturnAddressOrganisation() {
        return pickPointReturnAddressOrganisation;
    }

    public void setPickPointReturnAddressPhone(String pickPointReturnAddressPhone) {
        this.pickPointReturnAddressPhone = pickPointReturnAddressPhone;
    }

    public String getPickPointReturnAddressPhone() {
        return pickPointReturnAddressPhone;
    }

    public void setPickPointReturnAddressComment(String pickPointReturnAddressComment) {
        this.pickPointReturnAddressComment = pickPointReturnAddressComment;
    }

    public String getPickPointReturnAddressComment() {
        return pickPointReturnAddressComment;
    }

    public String getSdekUrl() {
        return sdekUrl;
    }

    public void setSdekUrl(String sdekUrl) {
        this.sdekUrl = sdekUrl;
    }

    public String getSdekAccount() {
        return sdekAccount;
    }

    public void setSdekAccount(String sdekAccount) {
        this.sdekAccount = sdekAccount;
    }

    public String getSdekSecurePassword() {
        return sdekSecurePassword;
    }

    public void setSdekSecurePassword(String sdekSecurePassword) {
        this.sdekSecurePassword = sdekSecurePassword;
    }

    public int getSdekSendDelayInSec() {
        return sdekSendDelayInSec;
    }

    public void setSdekSendDelayInSec(int sdekSendDelayInSec) {
        this.sdekSendDelayInSec = sdekSendDelayInSec;
    }

    public int getSdekSendCityCode() {
        return sdekSendCityCode;
    }

    public void setSdekSendCityCode(int sdekSendCityCode) {
        this.sdekSendCityCode = sdekSendCityCode;
    }

    public String getSdekSellerName() {
        return sdekSellerName;
    }

    public void setSdekSellerName(String sdekSellerName) {
        this.sdekSellerName = sdekSellerName;
    }

    public String getSdekSellerPhone() {
        return sdekSellerPhone;
    }

    public void setSdekSellerPhone(String sdekSellerPhone) {
        this.sdekSellerPhone = sdekSellerPhone;
    }
}
