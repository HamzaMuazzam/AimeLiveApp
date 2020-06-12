package com.example.aimeliveapp.network_calls.getchekuser;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetUserDetail {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("profile_image")
    @Expose
    private String profileImage;
    @SerializedName("likes")
    @Expose
    private String likes;
    @SerializedName("following")
    @Expose
    private String following;
    @SerializedName("total_shares")
    @Expose
    private String totalShares;
    @SerializedName("follower")
    @Expose
    private String follower;
    @SerializedName("total_comments")
    @Expose
    private String totalComments;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("hearts")
    @Expose
    private String hearts;
    @SerializedName("contributors")
    @Expose
    private String contributors;
    @SerializedName("myincome")
    @Expose
    private String myincome;
    @SerializedName("wallets")
    @Expose
    private String wallets;
    @SerializedName("wishes")
    @Expose
    private String wishes;
    @SerializedName("voucher")
    @Expose
    private String voucher;
    @SerializedName("top_fans")
    @Expose
    private String topFans;
    @SerializedName("uuid")
    @Expose
    private String uuid;
    @SerializedName("Broadcaster")
    @Expose
    private String broadcaster;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getFollowing() {
        return following;
    }

    public void setFollowing(String following) {
        this.following = following;
    }

    public String getTotalShares() {
        return totalShares;
    }

    public void setTotalShares(String totalShares) {
        this.totalShares = totalShares;
    }

    public String getFollower() {
        return follower;
    }

    public void setFollower(String follower) {
        this.follower = follower;
    }

    public String getTotalComments() {
        return totalComments;
    }

    public void setTotalComments(String totalComments) {
        this.totalComments = totalComments;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getHearts() {
        return hearts;
    }

    public void setHearts(String hearts) {
        this.hearts = hearts;
    }

    public String getContributors() {
        return contributors;
    }

    public void setContributors(String contributors) {
        this.contributors = contributors;
    }

    public String getMyincome() {
        return myincome;
    }

    public void setMyincome(String myincome) {
        this.myincome = myincome;
    }

    public String getWallets() {
        return wallets;
    }

    public void setWallets(String wallets) {
        this.wallets = wallets;
    }

    public String getWishes() {
        return wishes;
    }

    public void setWishes(String wishes) {
        this.wishes = wishes;
    }

    public String getVoucher() {
        return voucher;
    }

    public void setVoucher(String voucher) {
        this.voucher = voucher;
    }

    public String getTopFans() {
        return topFans;
    }

    public void setTopFans(String topFans) {
        this.topFans = topFans;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getBroadcaster() {
        return broadcaster;
    }

    public void setBroadcaster(String broadcaster) {
        this.broadcaster = broadcaster;
    }

}