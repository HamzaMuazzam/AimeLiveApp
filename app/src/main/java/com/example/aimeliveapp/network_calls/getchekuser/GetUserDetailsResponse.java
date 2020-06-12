package com.example.aimeliveapp.network_calls.getchekuser;


import java.util.List;

import com.example.aimeliveapp.network_calls.getchekuser.GetUserDetail;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetUserDetailsResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("GetUserDetails")
    @Expose
    private List<GetUserDetail> getUserDetails = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<GetUserDetail> getGetUserDetails() {
        return getUserDetails;
    }

    public void setGetUserDetails(List<GetUserDetail> getUserDetails) {
        this.getUserDetails = getUserDetails;
    }

}