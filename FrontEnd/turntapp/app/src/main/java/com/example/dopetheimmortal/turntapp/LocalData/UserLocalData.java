package com.example.dopetheimmortal.turntapp.LocalData;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.dopetheimmortal.turntapp.Useful.Profile_Data;

/**
 * Created by jackson on 2016/03/26.
 */
public class UserLocalData {
    public static final String id = "_id";
    public static final String dbid = "dbid";
    public static final String user = "username";
    public static final String email = "email";
    public static final String name = "name";
    public static final String surname = "surname";
    public static final String bday = "bday";
    public static final String phone = "phone";
    public static final String status = "status";
    public static final String pic = "pic";
    public static final String loyalty = "loyalty";
    public static final String state = "state";
    public static final String priv = "private";
    public static final String regid = "regid";
    public static final String gender = "gender";
    public static final String followers = "followers";
    public static final String following = "following";

    public static final String dbname = "user_login";
    public static final String tbname = "user";
    public static final int version = 1;

    private final Context context;
    private Helper helper;
    private SQLiteDatabase database;

    private static class Helper extends SQLiteOpenHelper {

        public Helper(Context context) {
            super(context, dbname, null, version);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("Create table " + tbname + " ( " +
                    id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    dbid+" TEXT NOT NULL, " +
                    user + " TEXT NOT NULL, " +
                    email+" TEXT NOT NULL, " +
                    name+" TEXT NOT NULL, " +
                    surname+" TEXT NOT NULL, " +
                    bday+" TEXT NOT NULL, " +
                    phone+" TEXT NOT NULL, " +
                    status+" TEXT NOT NULL, " +
                    loyalty+" TEXT NOT NULL, " +
                    state+" TEXT NOT NULL, " +
                    priv+" TEXT NOT NULL, " +
                    regid+" TEXT NOT NULL, " +
                    gender+" TEXT NOT NULL, " +
                    followers+" TEXT NOT NULL, " +
                    following+" TEXT NOT NULL, " +
                    pic+ " TEXT NOT NULL);");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
            db.execSQL("DROP TABLE IF EXISTS " + dbname);
            onCreate(db);
        }

    }

    public  UserLocalData(Context c) {
        context = c;

    }

    public UserLocalData open() {
        helper = new Helper(context);
        database = helper.getWritableDatabase();
        return this;

    }

    public void close() {
        helper.close();
    }

    public long createentry(String id,String user,String email,String name,String surname,String bday,String phone,String status,String image,String loyalty,String state,String priv,String regid,String followers,String following,String gender) {
        ContentValues cv = new ContentValues();
        cv.put(this.dbid, id);
        cv.put(this.user,user);
        cv.put(this.email,email);
        cv.put(this.name,name);
        cv.put(this.surname,surname);
        cv.put(this.bday,bday);
        cv.put(this.phone,phone);
        cv.put(this.status,status);
        cv.put(this.pic,image);
        cv.put(this.loyalty,loyalty);
        cv.put(this.state,state);
        cv.put(this.priv,priv);
        cv.put(this.regid,regid);
        cv.put(this.followers,followers);
        cv.put(this.following,following);
        cv.put(this.gender,gender);

        return database.insert(tbname, null, cv);
    }

    public boolean check() {
        String[] colunms = new String[] { id, user };
        Cursor c = database
                .query(tbname, colunms, null, null, null, null, null);
        c.moveToFirst();
        int name = c.getColumnIndex(user);
        try {
            String data = c.getString(name);
            return true;
        } catch (Exception ex) {
            return false;
        }

    }

    public Profile_Data actual() {
        String[] colunms = new String[] { id, user,dbid,email,name,surname,bday,phone,regid,gender,pic,status,following,followers,loyalty,state,priv};
        Cursor c = database
                .query(tbname, colunms, null, null, null, null, null);
        int userid=c.getColumnIndex(dbid);
        int username = c.getColumnIndex(user);
        int useremail = c.getColumnIndex(email);
        int userphone=c.getColumnIndex(phone);
        int ureg=c.getColumnIndex(regid);
        int ugender=c.getColumnIndex(gender);
        int ustatus=c.getColumnIndex(status);
        int upic=c.getColumnIndex(pic);
        int ufollowers=c.getColumnIndex(followers);
        int ufollowing=c.getColumnIndex(following);
        int user_name=c.getColumnIndex(name);
        int usersurname=c.getColumnIndex(surname);
        int userbday=c.getColumnIndex(bday);
        int uloyalty=c.getColumnIndex(loyalty);
        int ustate=c.getColumnIndex(state);
        int upriv=c.getColumnIndex(priv);
        c.moveToFirst();
        try {
        Profile_Data get=new Profile_Data(c.getString(username),c.getString(userid),c.getString(useremail),c.getString(user_name),c.getString(usersurname),c.getString(userbday),c.getString(userphone),c.getString(ureg),c.getString(ugender),c.getString(upic),c.getString(ustatus),c.getString(ufollowing),c.getString(ufollowers),c.getString(uloyalty),c.getString(ustate),c.getString(upriv));
            System.out.println(get.user);
        return  get;
        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(context,ex.toString(),Toast.LENGTH_LONG).show();
            return null;
        }

    }

    public boolean delete(){
        String[] colunms = new String[] { id, user,dbid};
        Cursor c = database
                .query(tbname, colunms, null, null, null, null, null);
        int name = c.getColumnIndex(user);
        c.moveToFirst();
        String whereClause = user + "=?";
        String[] whereArgs = new String[] { c.getString(name) };
        return database.delete(tbname, whereClause, whereArgs)>0;
    }
    public String update_data(String id,String user,String email,String name,String surname,String bday,String phone,String status,String image,String loyalty,String state,String priv,String regid,String followers,String following,String gender){
        ContentValues cv = new ContentValues();
        cv.put(this.dbid, id);
        cv.put(this.user,user);
        cv.put(this.email,email);
        cv.put(this.name,name);
        cv.put(this.surname,surname);
        cv.put(this.bday,bday);
        cv.put(this.phone,phone);
        cv.put(this.status,status);
        cv.put(this.pic,image);
        cv.put(this.loyalty,loyalty);
        cv.put(this.state,state);
        cv.put(this.priv,priv);
        cv.put(this.regid,regid);
        cv.put(this.followers,followers);
        cv.put(this.following,following);
        cv.put(this.gender,gender);
        String where = dbid + "=" + id;
        try{
            database.update(tbname, cv, where, null);
            return "Updated";
        }
        catch (Exception e){
            String error =  e.getMessage().toString();
            return  error;
        }

    }


}