package za.co.TurntApp.www.DataStructures;

/**
 * Created by jackson on 2016/03/26.
 */
public class FollowData {
   public String name,username,image,id,surname;
    public int state;

    public FollowData(String name,String surname, String username, String image, int state,String id) {
        this.name = name;
        this.username = username;
        this.image = image;
        this.state = state;
        this.id=id;
        this.surname=surname;
    }
}
