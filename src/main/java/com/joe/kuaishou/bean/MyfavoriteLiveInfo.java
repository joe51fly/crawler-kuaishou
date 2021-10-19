package com.joe.kuaishou.bean;

import java.io.Serializable;
import java.util.Date;

public class MyfavoriteLiveInfo implements Serializable {
    private Long id;
    private String userName;
    /**
     * 对应myfavorite表里的eid
     */
    private String userEid;
    /**
     * 个人简介
     */
    private String userText;
    /**
     * 是一串数字不知道是干啥用的
     */
    private String userId;
    /**
     * 直播时的id
     */
    private String userLiveStreamId;
    /**
     * 头像
     */
    private String userHeadUrl;
    /**
     * 好像也是头像
     */
    private String coverUrl;
    /**
     * 直播封面
     */
    private String rtCoverUrl;
    /**
     * m3u8直播视频链接
     */
    private String hlsPlayUrl;
    /**
     * flv直播视频链接
     */
    private String playUrls;

    private String userKwaiId;
    private String userPrincipalId;
    /**
     * 是否正在直播：0:没有直播，1:正在直播
     */
    private boolean isLive;
    /**
     * 直播开始时间
     */
    private Date liveStartTime;
    /**
     * 上次更新的时间
     */
    private Date updateTime;
    /**
     * 是否是我的特别关注：0：不是，1：是
     */
    private boolean isMyfavorite;
    /**
     * 备注
     */
    private String remarks;

    /*
        是否是置顶 0否 1是
     */
    private int isTop;

    @Override
    public String toString() {
        return "MyfavoriteLiveInfo{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", userEid='" + userEid + '\'' +
                ", userText='" + userText + '\'' +
                ", userId='" + userId + '\'' +
                ", userLiveStreamId='" + userLiveStreamId + '\'' +
                ", userHeadUrl='" + userHeadUrl + '\'' +
                ", coverUrl='" + coverUrl + '\'' +
                ", rtCoverUrl='" + rtCoverUrl + '\'' +
                ", hlsPlayUrl='" + hlsPlayUrl + '\'' +
                ", playUrls='" + playUrls + '\'' +
                ", userKwaiId='" + userKwaiId + '\'' +
                ", userPrincipalId='" + userPrincipalId + '\'' +
                ", isLive=" + isLive +
                ", liveStartTime=" + liveStartTime +
                ", updateTime=" + updateTime +
                ", isMyfavorite=" + isMyfavorite +
                ", remarks='" + remarks + '\'' +
                ", isTop=" + isTop +
                '}';
    }

    public int getIsTop() {
        return isTop;
    }

    public void setIsTop(int isTop) {
        this.isTop = isTop;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEid() {
        return userEid;
    }

    public void setUserEid(String userEid) {
        this.userEid = userEid;
    }

    public String getUserText() {
        return userText;
    }

    public void setUserText(String userText) {
        this.userText = userText;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserLiveStreamId() {
        return userLiveStreamId;
    }

    public void setUserLiveStreamId(String userLiveStreamId) {
        this.userLiveStreamId = userLiveStreamId;
    }

    public String getUserHeadUrl() {
        return userHeadUrl;
    }

    public void setUserHeadUrl(String userHeadUrl) {
        this.userHeadUrl = userHeadUrl;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getRtCoverUrl() {
        return rtCoverUrl;
    }

    public void setRtCoverUrl(String rtCoverUrl) {
        this.rtCoverUrl = rtCoverUrl;
    }

    public String getHlsPlayUrl() {
        return hlsPlayUrl;
    }

    public void setHlsPlayUrl(String hlsPlayUrl) {
        this.hlsPlayUrl = hlsPlayUrl;
    }

    public String getPlayUrls() {
        return playUrls;
    }

    public void setPlayUrls(String playUrls) {
        this.playUrls = playUrls;
    }

    public String getUserKwaiId() {
        return userKwaiId;
    }

    public void setUserKwaiId(String userKwaiId) {
        this.userKwaiId = userKwaiId;
    }

    public String getUserPrincipalId() {
        return userPrincipalId;
    }

    public void setUserPrincipalId(String userPrincipalId) {
        this.userPrincipalId = userPrincipalId;
    }

    public boolean isLive() {
        return isLive;
    }

    public void setLive(boolean live) {
        isLive = live;
    }

    public Date getLiveStartTime() {
        return liveStartTime;
    }

    public void setLiveStartTime(Date liveStartTime) {
        this.liveStartTime = liveStartTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public boolean isMyfavorite() {
        return isMyfavorite;
    }

    public void setMyfavorite(boolean myfavorite) {
        isMyfavorite = myfavorite;
    }
}
