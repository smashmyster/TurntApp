package com.example.dopetheimmortal.turntapp.Useful;

/**
 * Created by jackson on 2016/03/26.
 */
public class Profile_Data {
    public String user,dbid,email,name,surname,bday,phone,regid,gender,pic,status,following,followers,loyalty,state,priv;

    public Profile_Data(String user, String dbid, String email, String name, String surname, String bday, String phone, String regid, String gender, String pic, String status,String following, String followers, String loyalty, String state, String priv) {
        this.user = user;
        this.dbid = dbid;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.bday = bday;
        this.phone = phone;
        this.regid = regid;
        this.gender = gender;
        this.pic = pic;
        this.status = status;
        this.following = following;
        this.followers = followers;
        this.loyalty = loyalty;
        this.state = state;
        this.priv = priv;
    }
}
