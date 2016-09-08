package com.example.dopetheimmortal.turntapp.DataStructures;

/**
 * Created by jackson on 2016/09/08.
 */
public class GeneralUser {
   public String id,name,surname,image_name,following,followers,status,invited;

    public GeneralUser(String id, String name, String surname, String image_name, String following, String followers, String status, String invited) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.image_name = image_name;
        this.following = following;
        this.followers = followers;
        this.status = status;
        this.invited = invited;
    }
}
