package com.example.aimeliveapp.matchmaker;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetMatchResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("GetMatch")
    @Expose
    private List<GetMatch> getMatch = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<GetMatch> getGetMatch() {
        return getMatch;
    }

    public void setGetMatch(List<GetMatch> getMatch) {
        this.getMatch = getMatch;
    }

}