package mma;

import models.BaseModel;
import models.User;
import org.mongodb.morphia.annotations.Reference;
import play.data.validation.Constraints;

import java.util.Date;

/**
 * Created by harshitjain on 11/11/17.
 */
public class AccApplication extends BaseModel {
    private String fullname;
    @Constraints.Pattern("^[+0-9]+$")
    private String countryCode;
    @Constraints.Pattern("^[0-9]+$")
    private String mobile;
    @Reference
    private User user;
    @Reference
    private AccommodationType accommodationType;
    private String idProofUrl;
    private int requestedRoomCount;
    private int approvedRoomCount;
    private AccApplicationStatus status = AccApplicationStatus.APPLIED;
    private Date checkInDate;
    private Date checkOutDate;
    private String email;
    private String addressLine1;
    private String addressLine2;
    private String addressLine3;
    private String addressLine4;
    private String statusChangeComment;
    private int pincode;
    private int amountToPay;

    public void setAccommodationType(AccommodationType accommodationType) {
        this.accommodationType = accommodationType;
    }

    public int getAmountToPay() {
        return amountToPay;
    }

    public void setAmountToPay(int amountToPay) {
        this.amountToPay = amountToPay;
    }

    public int getPincode() {
        return pincode;
    }

    public void setPincode(int pincode) {
        this.pincode = pincode;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getAddressLine3() {
        return addressLine3;
    }

    public void setAddressLine3(String addressLine3) {
        this.addressLine3 = addressLine3;
    }

    public String getAddressLine4() {
        return addressLine4;
    }

    public void setAddressLine4(String addressLine4) {
        this.addressLine4 = addressLine4;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public AccommodationType getAccommodationType() {
        return accommodationType;
    }

    public void setAccommodationTypeId(String id) {
        this.accommodationType = AccommodationTypeDao.getInstance().accommodationType(id);
    }

    public String getIdProofUrl() {
        return idProofUrl;
    }

    public void setIdProofUrl(String idProofUrl) {
        this.idProofUrl = idProofUrl;
    }

    public int getRequestedRoomCount() {
        return requestedRoomCount;
    }

    public void setRequestedRoomCount(int requestedRoomCount) {
        this.requestedRoomCount = requestedRoomCount;
    }

    public int getApprovedRoomCount() {
        return approvedRoomCount;
    }

    public void setApprovedRoomCount(int approvedRoomCount) {
        this.approvedRoomCount = approvedRoomCount;
    }

    public AccApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(AccApplicationStatus status) {
        this.status = status;
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(Date checkInDate) {
        this.checkInDate = checkInDate;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(Date checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public String getStatusChangeComment() {
        return statusChangeComment;
    }

    public void setStatusChangeComment(String statusChangeComment) {
        this.statusChangeComment = statusChangeComment;
    }
}
