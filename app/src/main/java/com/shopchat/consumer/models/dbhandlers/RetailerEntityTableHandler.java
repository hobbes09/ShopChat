//package com.shopchat.consumer.models.dbhandlers;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//import android.util.Log;
//
//import com.shopchat.consumer.models.entities.RetailerEntity;
//
//import java.util.ArrayList;
//
//
///**
// * Created by sourin on 02/12/15.
// */
//public class RetailerEntityTableHandler extends SQLiteOpenHelper {
//
//    // All Static variables
//    // Database Version
//    private static final int DATABASE_VERSION = 2;
//
//    // Database Name
//    private static final String DATABASE_NAME = "LokalChatDB";
//
//    // Retailer table name
//    private static final String TABLE_RETAILER = "retailers";
//
//    // Retailer Table Columns names
//    private static final String KEY_RETAILER_ID = "retailer_id";
//    private static final String KEY_RETAILER_SHOP_NAME = "shop_name";
//
//    public RetailerEntityTableHandler(Context context) {
//        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase sqLiteDatabase) {
//        Log.v("Create>>>", "creating retailer");
//        String CREATE_RETAILER_TABLE = "CREATE TABLE " + TABLE_RETAILER + " (" +
//                KEY_RETAILER_ID + " VARCHAR(255) PRIMARY KEY, " +
//                KEY_RETAILER_SHOP_NAME + " VARCHAR(255))";
//        Log.v("Debug@@@>>>>>> ", CREATE_RETAILER_TABLE);
//        sqLiteDatabase.execSQL(CREATE_RETAILER_TABLE);
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
//        // Drop older table if existed
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_RETAILER);
//
//        // Create tables again
//        onCreate(sqLiteDatabase);
//    }
//
//    // Adding new retailerEntity
//    public void addRetailerEntity(RetailerEntity retailerEntity) {
//        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(KEY_RETAILER_ID, retailerEntity.getRetailerId());
//        values.put(KEY_RETAILER_SHOP_NAME, retailerEntity.getShopName());
//
//        // Inserting Row
//        sqLiteDatabase.insert(TABLE_RETAILER, null, values);
//        sqLiteDatabase.close(); // Closing database connection
//    }
//
//    // Adding new retailerEntities
//    public void addRetailerEntities(ArrayList<RetailerEntity> retailerEntities) {
//        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
//
//        for(int index = 0; index < retailerEntities.size(); index++){
//            RetailerEntity retailerEntity = retailerEntities.get(index);
//
//            ContentValues values = new ContentValues();
//            values.put(KEY_RETAILER_ID, retailerEntity.getRetailerId());
//            values.put(KEY_RETAILER_SHOP_NAME, retailerEntity.getShopName());
//
//            // Inserting Row
//            sqLiteDatabase.insert(TABLE_RETAILER, null, values);
//        }
//
//        sqLiteDatabase.close(); // Closing database connection
//    }
//
//    // Getting single RetailerEntity
//    public RetailerEntity getRetailerEntityFromRetailerId(String retailerId) {
//        Log.v("Debug@@@>>>>>> ", "here");
//        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
//
//        Cursor cursor = sqLiteDatabase.query(TABLE_RETAILER, new String[] { KEY_RETAILER_ID, KEY_RETAILER_SHOP_NAME}, KEY_RETAILER_ID + "=?",
//                new String[] { retailerId }, null, null, null, null);
//        Log.v("Debug@@@>>>>>>", Integer.toString(cursor.getCount()));
//        if (cursor.getCount() > 0){
//            cursor.moveToFirst();
//            RetailerEntity retailerEntity = new RetailerEntity(cursor.getString(0), cursor.getString(1));
//            // return retailerEntity
//            return retailerEntity;
//        }else{
//            return null;
//        }
//    }
//
//    // Getting single RetailerEntity
//    public RetailerEntity getRetailerEntityFromShopName(String shopName) {
//        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
//
//        Cursor cursor = sqLiteDatabase.query(TABLE_RETAILER, new String[] { KEY_RETAILER_ID, KEY_RETAILER_SHOP_NAME}, KEY_RETAILER_SHOP_NAME + "=?",
//                new String[] { String.valueOf(shopName) }, null, null, null, null);
//        if (cursor.getCount() > 0){
//            cursor.moveToFirst();
//            RetailerEntity retailerEntity = new RetailerEntity(cursor.getString(0), cursor.getString(1));
//            // return retailerEntity
//            return retailerEntity;
//        }else{
//            return null;
//        }
//    }
//
//    // Getting All RetailerEntity
//    public ArrayList<RetailerEntity> getAllRetailerEntities() {
//        ArrayList<RetailerEntity> retailerEntities = new ArrayList<RetailerEntity>();
//        // Select All Query
//        String selectQuery = "SELECT * FROM " + TABLE_RETAILER + ";";
//
//        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
//        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
//
//        // looping through all rows and adding to list
//        if (cursor.moveToFirst()) {
//            do {
//                RetailerEntity retailerEntity = new RetailerEntity(cursor.getString(0), cursor.getString(1));
//                // Adding retailer to list
//                retailerEntities.add(retailerEntity);
//            } while (cursor.moveToNext());
//        }
//
//        // return retailer list
//        return retailerEntities;
//    }
//
//    // Getting  retailer count
//    public int getRetailerCount() {
//        int count = 0;
//        String countQuery = "SELECT  * FROM " + TABLE_RETAILER + ";";
//        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
//        Cursor cursor = sqLiteDatabase.rawQuery(countQuery, null);
//        count = cursor.getCount();
//        cursor.close();
//
//        // return count
//        return count;
//    }
//
//    // Updating single retailer
//    public int updateRetailerEntity(RetailerEntity retailerEntity) {
//        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(KEY_RETAILER_ID, retailerEntity.getRetailerId());
//        values.put(KEY_RETAILER_SHOP_NAME, retailerEntity.getShopName());
//
//        // updating row
//        int update = sqLiteDatabase.update(TABLE_RETAILER, values, KEY_RETAILER_ID + " = ?",
//                new String[] { String.valueOf(retailerEntity.getRetailerId()) });
//
//        sqLiteDatabase.close();
//
//        return update;
//    }
//
//    // Deleting single retailer
//    public void deleteRetailer(RetailerEntity retailerEntity) {
//        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
//        sqLiteDatabase.delete(TABLE_RETAILER, KEY_RETAILER_ID + " = ?",
//                new String[] { String.valueOf(retailerEntity.getRetailerId()) });
//        sqLiteDatabase.close();
//    }
//
//    // Drop table
//    public void dropTable(){
//        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_RETAILER);
//    }
//
//
//}
