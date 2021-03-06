package aris.kots.adminclientapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;

public class DBAdapter {

    private static final String TAG = "DBAdapter";
    
    private static final String DATABASE_NAME = "MyDatabase";
    /****************************************************************/
    private static final String TABLE_REMEMBER_LOGIN = "remember_login";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_ISADMIN = "isAdmin";
    /****************************************************************/
    private static final String TABLE_IP = "table_ip";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_INTERFACE_NAME = "interface_name";
    private static final String KEY_INTERFACE_IP = "interface_ip";
    private static final String KEY_MALICIOUS_IP = "malicious_ip";
    private static final String KEY_FREQUENCY = "frequency";

    /****************************************************************/
    private static final String TABLE_PATTERN = "table_pattern";
    private static final String KEY_MALICIOUS_PATTERN = "malicious_pattern";
    /****************************************************************/
    private static final String TABLE_USERS_DEVICES = "table_users_devices";
    /****************************************************************/
    private static final String TABLE_ADMIN_DELETE = "table_admin_delete";
    /****************************************************************/
    private static final String TABLE_ADMIN_INSERT_PATTERN = "table_admin_insert_pattern";
    /****************************************************************/
    private static final String TABLE_ADMIN_INSERT_IP = "table_admin_insert_ip";
    
    
    private static final int DATABASE_VERSION = 2;

    private static final String DATABASE_CREATE1 =
        "create table if not exists " + TABLE_REMEMBER_LOGIN + " (username VARCHAR primary key, password VARCHAR, isAdmin VARCHAR);";
    
    private static final String DATABASE_CREATE2 =
            "create table if not exists " + TABLE_IP + 
            " (id integer primary key autoincrement, user_id VARCHAR, interface_name VARCHAR, interface_ip VARCHAR,"+
            " malicious_ip VARCHAR, frequency integer);";  

    private static final String DATABASE_CREATE3 =
            "create table if not exists " + TABLE_PATTERN + 
            " (id integer primary key autoincrement, user_id VARCHAR, interface_name VARCHAR, interface_ip VARCHAR,"+
            " malicious_pattern VARCHAR, frequency integer);";  
    
    private static final String DATABASE_CREATE4 =
            "create table if not exists " + TABLE_USERS_DEVICES + 
            " (id integer primary key autoincrement,username VARCHAR,user_id VARCHAR);";   
    
    private static final String DATABASE_CREATE5 =
            "create table if not exists " + TABLE_ADMIN_DELETE + 
            " (id integer primary key autoincrement,device_id VARCHAR);";  
    
    private static final String DATABASE_CREATE6 =
            "create table if not exists " + TABLE_ADMIN_INSERT_IP + 
            " (id integer primary key autoincrement,malicious_ip VARCHAR);";
    
    private static final String DATABASE_CREATE7 =
            "create table if not exists " + TABLE_ADMIN_INSERT_PATTERN + 
            " (id integer primary key autoincrement,malicious_pattern VARCHAR);"; 
    
    private final Context context;    

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBAdapter(Context ctx) 
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }
        
    private static class DatabaseHelper extends SQLiteOpenHelper 
    {
        DatabaseHelper(Context context) 
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) 
        {
        	try {
        		System.out.println("CREATION");
        		db.execSQL(DATABASE_CREATE1);
        		db.execSQL(DATABASE_CREATE2);
        		db.execSQL(DATABASE_CREATE3);
        		db.execSQL(DATABASE_CREATE4);
//        		db.execSQL(DATABASE_CREATE5);
//        		db.execSQL(DATABASE_CREATE6);
//        		db.execSQL(DATABASE_CREATE7);
        	} catch (SQLException e) {
        		e.printStackTrace();
        	}
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
        {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS contacts");
            onCreate(db);
        }
    }    

    //---opens the database---
    public DBAdapter open() throws SQLException 
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //---closes the database---    
    public void close() 
    {
        DBHelper.close();
    }
    
    //---insert a record into the database---
    public long RememberLoginUser(String username,String password,String isAdmin) 
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_USERNAME, username);
        initialValues.put(KEY_PASSWORD, password);
        initialValues.put(KEY_ISADMIN, isAdmin);
        return db.insert(TABLE_REMEMBER_LOGIN, null, initialValues);
        
    }
    public long insertTableIP(String user_id, String interface_name,
			String interface_ip, String mal_ip, int frequency) 
    {        
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_USER_ID, user_id);
        initialValues.put(KEY_INTERFACE_NAME, interface_name);
        initialValues.put(KEY_INTERFACE_IP, interface_ip);
        initialValues.put(KEY_MALICIOUS_IP, mal_ip);
        initialValues.put(KEY_FREQUENCY, frequency);
        
        return db.insert(TABLE_IP, null, initialValues);
    }
    public long insertTablePATTERNS(String user_id, String interface_name,
			String interface_ip, String mal_pattern, int frequency) 
    {        
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_USER_ID, user_id);
        initialValues.put(KEY_INTERFACE_NAME, interface_name);
        initialValues.put(KEY_INTERFACE_IP, interface_ip);
        initialValues.put(KEY_MALICIOUS_PATTERN, mal_pattern);
        initialValues.put(KEY_FREQUENCY, frequency);
        
        return db.insert(TABLE_PATTERN, null, initialValues);
    }
    public long insertTableUSERSDEVICES(String username,String user_id) 
    {        
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_USERNAME, username);
        initialValues.put(KEY_USER_ID, user_id);
        
        return db.insert(TABLE_USERS_DEVICES, null, initialValues);
    }
    public long insertTableADMIN_DELETE(String user_id) 
    {        
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_USER_ID, user_id);
        
        return db.insert(TABLE_ADMIN_DELETE, null, initialValues);
    }
    public long insertTableADMIN_INSERT_IP(String malicious_ip) 
    {        
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_MALICIOUS_IP, malicious_ip);
        
        return db.insert(TABLE_ADMIN_INSERT_IP, null, initialValues);
    }
    public long insertTableADMIN_INSERT_PATTERN(String mal_pattern) 
    {        
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_MALICIOUS_PATTERN, mal_pattern);
        
        return db.insert(TABLE_ADMIN_INSERT_PATTERN, null, initialValues);
    }
    //---deletes a particular record---

    ///////////////////////
    //GET LOGINUSER
    public Cursor getLogInUser() 
    {
        return db.query(TABLE_REMEMBER_LOGIN, null, null, null,null,null,null );
    }
  public boolean deleteLogInUser() {
  
	  db.delete(TABLE_REMEMBER_LOGIN, null, null);
      return true;
}
    ///////////////////////
    public Cursor getDevicesByUsername(String username) 
    {
        return db.query(TABLE_USERS_DEVICES, new String[] {KEY_USER_ID}, "username=?", new String[] {username},null,null,null );
    }
    public Cursor getAllDevices() 
    {
        return db.query(true,TABLE_USERS_DEVICES, new String[] {KEY_USER_ID}, null, null,null,null,null,null,null);//last 2 null and distinct first
    }
    public Cursor getAllInterfacesByUser(String user_id) 
    {
        return db.query(true,TABLE_IP, new String[] {KEY_INTERFACE_NAME}, "user_id=?",  new String[] {user_id},null,null,null,null,null);//last 2 null and distinct first
    }
    public Cursor getIPStatistics(String user_id,String interface_name) 
    {
        return db.query(TABLE_IP, null, "user_id=? and interface_name=?", new String[] {user_id,interface_name},null,null,null );
    }
    public Cursor getPatternStatistics(String user_id,String interface_name) 
    {
        return db.query(TABLE_PATTERN, null, "user_id=? and interface_name=?", new String[] {user_id,interface_name},null,null,null );
    }

    //--- Number of 
//    public int NumberofNotes(String exhibition_name) {
//        
//    	 Cursor c = db.query(TABLE_NOTES, new String[] {KEY_NOTE_ID}, "exhibition_name=?", new String[]{exhibition_name},null,null,null );
//        int x = c.getCount();
//        if(x>=0)	
//        	return x;
//        else
//        	return 0;
//    }
    
    public boolean ExistRememberLogin(String name) {
        
        String Query = "Select * from " + TABLE_REMEMBER_LOGIN;
        
        Cursor cursor = db.rawQuery(Query, null);
                if(cursor.getCount()<=0){
                	return false;
                }
            return true;
    }
//    public boolean deleteALL() {
//        
//        db.delete(TABLE_EXHIBITIONS, null, null);
//            return true;
//    }
}
