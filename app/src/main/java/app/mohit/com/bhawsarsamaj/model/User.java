package app.mohit.com.bhawsarsamaj.model;


import android.graphics.Bitmap;

import java.util.Date;
import java.util.Date;
import java.util.HashMap;

import app.mohit.com.bhawsarsamaj.util.ServerUtil;

public class User {

    private String userid;
    private String username;
    private String password;
    private Bitmap bitmap;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public User(){

    }

    public User(String profileid){
        {
            HashMap map = new HashMap();
            map.put("method","downloadImage");
            map.put("imageid",profileid);
            Long start = System.currentTimeMillis();
            bitmap =  ServerUtil.getImageFromServer(map);
        }
    }

    public User(String userid, String username, String password, String address, String city, String contactno, long profileid, String gender, Date dob) {
        this.userid = userid;
        this.username = username;
        this.password = password;
        this.address = address;
        this.city = city;
        this.contactno = contactno;
        this.profileid = profileid;
        this.gender = gender;
        this.dob = dob;
        this.bitmap=bitmap;
    }

    private String address;
    private String city;
    private String contactno;
    private long profileid;
    private String gender;
    private Date dob;






    @Override
    public String toString() {
        return "User [userid=" + userid + ", username=" + username + ", password=" + password + ", address=" + address
                + ", city=" + city + ", contactno=" + contactno + ", profileid=" + profileid + ", gender=" + gender
                + ", dob=" + dob + "]";
    }



    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getId() {
        return userid;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getContactno() {
        return contactno;
    }
    public void setContactno(String contactno) {
        this.contactno = contactno;
    }
    public long getProfileid() {
        return profileid;
    }
    public void setProfileid(long profileid) {
        this.profileid = profileid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }




}
