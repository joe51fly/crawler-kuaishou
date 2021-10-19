package com.joe.kuaishou.bean;

import java.io.Serializable;

public class MyInfo implements Serializable {
    private Integer id;
    private String myName;
    private String myId;
    private String myAvatar;
    private String myEid;
    private Long myUserId;
    /*
    我的粉丝
     */
    private Integer myFans;
    /*
    我的关注
     */
    private Integer myFollow;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMyName() {
        return myName;
    }

    public void setMyName(String myName) {
        this.myName = myName;
    }

    public String getMyId() {
        return myId;
    }

    public void setMyId(String myId) {
        this.myId = myId;
    }

    public String getMyAvatar() {
        return myAvatar;
    }

    public void setMyAvatar(String myAvatar) {
        this.myAvatar = myAvatar;
    }

    public String getMyEid() {
        return myEid;
    }

    public void setMyEid(String myEid) {
        this.myEid = myEid;
    }

    public Long getMyUserId() {
        return myUserId;
    }

    public void setMyUserId(Long myUserId) {
        this.myUserId = myUserId;
    }

    public Integer getMyFans() {
        return myFans;
    }

    public void setMyFans(Integer myFans) {
        this.myFans = myFans;
    }

    public Integer getMyFollow() {
        return myFollow;
    }

    public void setMyFollow(Integer myFollow) {
        this.myFollow = myFollow;
    }

    @Override
    public String toString() {
        return "MyInfo{" +
                "id=" + id +
                ", myName='" + myName + '\'' +
                ", myId='" + myId + '\'' +
                ", myAvatar='" + myAvatar + '\'' +
                ", myEid='" + myEid + '\'' +
                ", myUserId=" + myUserId +
                ", myFans=" + myFans +
                ", myFollow=" + myFollow +
                '}';
    }
}
