//package com.shopchat.consumer.models.dbhandlers;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//import android.util.Log;
//
//import com.shopchat.consumer.models.entities.ProductEntity;
//
//import java.util.ArrayList;
//
///**
// * Created by sourin on 02/12/15.
// */
//public class ProductEntityTableHandler extends SQLiteOpenHelper {
//
//    // All Static variables
//    // Database Version
//    private static final int DATABASE_VERSION = 2;
//
//    // Database Name
//    private static final String DATABASE_NAME = "LokalChatDB";
//
//    // Product table name
//    private static final String TABLE_PRODUCT = "products";
//
//    // Product Table Columns names
//    private static final String KEY_PRODUCT_ID = "product_id";
//    private static final String KEY_PRODUCT_CATEGORY = "category";
//    private static final String KEY_PRODUCT_CATEGORY_DESCRIPTION = "category_description";
//    private static final String KEY_PRODUCT_NAME = "product_name";
//    private static final String KEY_PRODUCT_DESCRIPTION = "product_description";
//    private static final String KEY_PRODUCT_IMAGE_URL = "image_url";
//
//    public ProductEntityTableHandler(Context context) {
//        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase sqLiteDatabase) {
//        Log.v("Create>>>", "creating product");
//        String CREATE_PRODUCT_TABLE = "CREATE TABLE " + TABLE_PRODUCT + " (" +
//                                        KEY_PRODUCT_ID + " VARCHAR(255) PRIMARY KEY, " +
//                KEY_PRODUCT_CATEGORY + " VARCHAR(255), " +
//                KEY_PRODUCT_CATEGORY_DESCRIPTION + " VARCHAR(255), " +
//                                        KEY_PRODUCT_NAME + " VARCHAR(255), " +
//                                        KEY_PRODUCT_DESCRIPTION + " VARCHAR(255), " +
//                KEY_PRODUCT_IMAGE_URL + " VARCHAR(255) )";
//        sqLiteDatabase.execSQL(CREATE_PRODUCT_TABLE);
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
//        // Drop older table if existed
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT);
//
//        // Create tables again
//        onCreate(sqLiteDatabase);
//    }
//
//    // Adding new productEntity
//    public void addProductEntity(ProductEntity productEntity) {
//        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(KEY_PRODUCT_ID, productEntity.getProductId());
//        values.put(KEY_PRODUCT_CATEGORY, productEntity.getCategory());
//        values.put(KEY_PRODUCT_CATEGORY_DESCRIPTION, productEntity.getCategoryDescription());
//        values.put(KEY_PRODUCT_NAME, productEntity.getProductName());
//        values.put(KEY_PRODUCT_DESCRIPTION, productEntity.getProductDescription());
//        values.put(KEY_PRODUCT_IMAGE_URL, productEntity.getImageurl());
//
//        // Inserting Row
//        sqLiteDatabase.insert(TABLE_PRODUCT, null, values);
//        sqLiteDatabase.close(); // Closing database connection
//    }
//
//    // Adding new ProductEntities
//    public void addProductEntities(ArrayList<ProductEntity> productEntities) {
//        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
//
//        for(int index = 0; index < productEntities.size(); index++){
//            ProductEntity productEntity = productEntities.get(index);
//
//            ContentValues values = new ContentValues();
//            values.put(KEY_PRODUCT_ID, productEntity.getProductId());
//            values.put(KEY_PRODUCT_CATEGORY, productEntity.getCategory());
//            values.put(KEY_PRODUCT_CATEGORY_DESCRIPTION, productEntity.getCategoryDescription());
//            values.put(KEY_PRODUCT_NAME, productEntity.getProductName());
//            values.put(KEY_PRODUCT_DESCRIPTION, productEntity.getProductDescription());
//            values.put(KEY_PRODUCT_IMAGE_URL, productEntity.getImageurl());
//
//            // Inserting Row
//            sqLiteDatabase.insert(TABLE_PRODUCT, null, values);
//        }
//
//        sqLiteDatabase.close(); // Closing database connection
//    }
//
//    // Getting single ProductEntity
//    public ProductEntity getProductEntityFromProductId(String productId) {
//        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
//
//        Cursor cursor = sqLiteDatabase.query(TABLE_PRODUCT, new String[] { KEY_PRODUCT_ID, KEY_PRODUCT_CATEGORY, KEY_PRODUCT_CATEGORY_DESCRIPTION,
//                                KEY_PRODUCT_NAME, KEY_PRODUCT_DESCRIPTION, KEY_PRODUCT_IMAGE_URL}, KEY_PRODUCT_ID + "=?",
//                                new String[] { String.valueOf(productId) }, null, null, null, null);
//        if (cursor.getCount() > 0){
//            cursor.moveToFirst();
//            ProductEntity productEntity = new ProductEntity(cursor.getString(0), cursor.getString(1), cursor.getString(2),
//                                                            cursor.getString(3), cursor.getString(4), cursor.getString(5));
//            // return productEntity
//            return productEntity;
//        }else{
//            return null;
//        }
//    }
//
//    // Getting single ProductEntity
//    public ProductEntity getProductEntityFromProductName(String productName) {
//        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
//
//        Cursor cursor = sqLiteDatabase.query(TABLE_PRODUCT, new String[] { KEY_PRODUCT_ID, KEY_PRODUCT_CATEGORY, KEY_PRODUCT_CATEGORY_DESCRIPTION,
//                        KEY_PRODUCT_NAME, KEY_PRODUCT_DESCRIPTION, KEY_PRODUCT_IMAGE_URL}, KEY_PRODUCT_NAME + "=?",
//                new String[] { String.valueOf(productName) }, null, null, null, null);
//        if (cursor.getCount() > 0){
//            cursor.moveToFirst();
//            ProductEntity productEntity = new ProductEntity(cursor.getString(0), cursor.getString(1), cursor.getString(2),
//                    cursor.getString(3), cursor.getString(4), cursor.getString(5));
//            // return productEntity
//            return productEntity;
//        }else{
//            return null;
//        }
//    }
//
//    // Getting All ProductEntities
//    public ArrayList<ProductEntity> getAllProductEntities() {
//        ArrayList<ProductEntity> productEntities = new ArrayList<ProductEntity>();
//        // Select All Query
//        String selectQuery = "SELECT * FROM " + TABLE_PRODUCT + ";";
//
//        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
//        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
//
//        // looping through all rows and adding to list
//        if (cursor.moveToFirst()) {
//            do {
//                ProductEntity productEntity = new ProductEntity(cursor.getString(0), cursor.getString(1), cursor.getString(2),
//                        cursor.getString(3), cursor.getString(4), cursor.getString(5));
//                // Adding product to list
//                productEntities.add(productEntity);
//            } while (cursor.moveToNext());
//        }
//
//        // return product list
//        return productEntities;
//    }
//
//    // Getting  product count
//    public int getProductCount() {
//        int count = 0;
//        String countQuery = "SELECT  * FROM " + TABLE_PRODUCT  + ";";
//        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
//        Cursor cursor = sqLiteDatabase.rawQuery(countQuery, null);
//        count = cursor.getCount();
//        cursor.close();
//
//        // return count
//        return count;
//    }
//
//    // Updating single product
//    public int updateProductEntity(ProductEntity productEntity) {
//        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(KEY_PRODUCT_ID, productEntity.getProductId());
//        values.put(KEY_PRODUCT_CATEGORY, productEntity.getCategory());
//        values.put(KEY_PRODUCT_CATEGORY_DESCRIPTION, productEntity.getCategoryDescription());
//        values.put(KEY_PRODUCT_NAME, productEntity.getProductName());
//        values.put(KEY_PRODUCT_DESCRIPTION, productEntity.getProductDescription());
//        values.put(KEY_PRODUCT_IMAGE_URL, productEntity.getImageurl());
//
//        // updating row
//        int update = sqLiteDatabase.update(TABLE_PRODUCT, values, KEY_PRODUCT_ID + " = ?",
//                new String[] { String.valueOf(productEntity.getProductId()) });
//
//        sqLiteDatabase.close();
//
//        return update;
//    }
//
//    // Deleting single product
//    public void deleteProduct(ProductEntity productEntity) {
//        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
//        sqLiteDatabase.delete(TABLE_PRODUCT, KEY_PRODUCT_ID + " = ?",
//                new String[] { String.valueOf(productEntity.getProductId()) });
//        sqLiteDatabase.close();
//    }
//
//    // Drop table
//    public void dropTable(){
//        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT);
//    }
//
//}
