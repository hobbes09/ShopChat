package com.shopchat.consumer.managers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.shopchat.consumer.models.entities.AnswerEntity;
import com.shopchat.consumer.models.entities.ProductEntity;
import com.shopchat.consumer.models.entities.QuestionEntity;
import com.shopchat.consumer.models.entities.RetailerEntity;

import java.util.ArrayList;

/**
 * Created by sourin on 06/12/15.
 */
public class DataBaseManager extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 3;

    // Database Name
    private static final String DATABASE_NAME = "LokalChatDB";

    // table names
    private static final String TABLE_QUESTION = "questions";
    private static final String TABLE_ANSWER = "answers";
    private static final String TABLE_PRODUCT = "products";
    private static final String TABLE_RETAILER = "retailers";

    // Question Table Columns names
    private static final String KEY_QUESTION_ID = "question_id";
    private static final String KEY_QUESTION_PRODUCT_ID = "product_id";
    private static final String KEY_QUESTION_TEXT = "question_text";
    private static final String KEY_QUESTION_UPDATED_AT = "updated_at";

    // Answer Table Columns names
    private static final String KEY_ANSWER_ID = "answer_id";
    private static final String KEY_ANSWER_RETAILER_ID = "retailer_id";
    private static final String KEY_ANSWER_QUESTION_ID = "question_id";
    private static final String KEY_ANSWER_TEXT = "answer_text";
    private static final String KEY_ANSWER_UPDATED_AT = "updated_at";

    // Product Table Columns names
    private static final String KEY_PRODUCT_ID = "product_id";
    private static final String KEY_PRODUCT_CATEGORY = "category";
    private static final String KEY_PRODUCT_CATEGORY_DESCRIPTION = "category_description";
    private static final String KEY_PRODUCT_NAME = "product_name";
    private static final String KEY_PRODUCT_DESCRIPTION = "product_description";
    private static final String KEY_PRODUCT_IMAGE_URL = "image_url";

    // Retailer Table Columns names
    private static final String KEY_RETAILER_ID = "retailer_id";
    private static final String KEY_RETAILER_SHOP_NAME = "shop_name";


    public DataBaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String CREATE_QUESTION_TABLE = "CREATE TABLE " + TABLE_QUESTION + " (" +
                KEY_QUESTION_ID + " VARCHAR(255) PRIMARY KEY, " +
                KEY_QUESTION_PRODUCT_ID + " VARCHAR(255), " +
                KEY_QUESTION_TEXT + " VARCHAR(255), " +
                KEY_QUESTION_UPDATED_AT +" BIGINT UNSIGNED NOT NULL )";

        String CREATE_ANSWER_TABLE = "CREATE TABLE " + TABLE_ANSWER + " (" +
                KEY_ANSWER_ID + " VARCHAR(255) PRIMARY KEY, " +
                KEY_ANSWER_RETAILER_ID + " VARCHAR(255), " +
                KEY_ANSWER_QUESTION_ID + " VARCHAR(255), " +
                KEY_ANSWER_TEXT + " VARCHAR(255), " +
                KEY_ANSWER_UPDATED_AT +" BIGINT UNSIGNED NOT NULL )";

        String CREATE_PRODUCT_TABLE = "CREATE TABLE " + TABLE_PRODUCT + " (" +
                KEY_PRODUCT_ID + " VARCHAR(255) PRIMARY KEY, " +
                KEY_PRODUCT_CATEGORY + " VARCHAR(255), " +
                KEY_PRODUCT_CATEGORY_DESCRIPTION + " VARCHAR(255), " +
                KEY_PRODUCT_NAME + " VARCHAR(255), " +
                KEY_PRODUCT_DESCRIPTION + " VARCHAR(255), " +
                KEY_PRODUCT_IMAGE_URL + " VARCHAR(255) )";

        String CREATE_RETAILER_TABLE = "CREATE TABLE " + TABLE_RETAILER + " (" +
                KEY_RETAILER_ID + " VARCHAR(255) PRIMARY KEY, " +
                KEY_RETAILER_SHOP_NAME + " VARCHAR(255))";

        Log.v("LokalChat", "Creating tables ...");

        sqLiteDatabase.execSQL(CREATE_QUESTION_TABLE);
        sqLiteDatabase.execSQL(CREATE_ANSWER_TABLE);
        sqLiteDatabase.execSQL(CREATE_PRODUCT_TABLE);
        sqLiteDatabase.execSQL(CREATE_RETAILER_TABLE);

        Log.v("LokalChat", "Tables created successfully ...");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // Drop older table if existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTION);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_ANSWER);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_RETAILER);

        // Create tables again
        onCreate(sqLiteDatabase);
    }

    /*
    Methods for Question Entity >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
     */

    // Adding new questionEntity
    public void addQuestionEntity(QuestionEntity questionEntity) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_QUESTION_ID, questionEntity.getQuestionId());
        values.put(KEY_QUESTION_PRODUCT_ID, questionEntity.getProductId());
        values.put(KEY_QUESTION_TEXT, questionEntity.getQuestionText());
        values.put(KEY_QUESTION_UPDATED_AT, questionEntity.getUpdatedAt());

        // Inserting Row
        sqLiteDatabase.insert(TABLE_QUESTION, null, values);
        sqLiteDatabase.close(); // Closing database connection
    }

    // Getting single QuestionEntity
    public QuestionEntity getQuestionEntityFromQuestionId(String questionId) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query(TABLE_QUESTION, new String[] { KEY_QUESTION_ID, KEY_QUESTION_PRODUCT_ID, KEY_QUESTION_TEXT, KEY_QUESTION_UPDATED_AT}
                , KEY_QUESTION_ID + "=?", new String[] { String.valueOf(questionId) }, null, null, null, null);
        if (cursor.getCount() > 0){
            cursor.moveToFirst();
            QuestionEntity questionEntity = new QuestionEntity(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3));
            // return questionEntity
            return questionEntity;
        }else{
            return null;
        }
    }

    // Getting All questionEntities
    public ArrayList<QuestionEntity> getAllQuestionEntities() {
        ArrayList<QuestionEntity> questionEntities = new ArrayList<QuestionEntity>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_QUESTION + " ORDER BY " + KEY_QUESTION_UPDATED_AT + " DESC";

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                QuestionEntity questionEntity = new QuestionEntity(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3));
                // Adding product to list
                questionEntities.add(questionEntity);
            } while (cursor.moveToNext());
        }

        // return question list
        return questionEntities;
    }

    // Getting  question count
    public int getQuestionCount() {
        int count = 0;
        String countQuery = "SELECT  * FROM " + TABLE_QUESTION + " ORDER BY " + KEY_ANSWER_UPDATED_AT + " ASC";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(countQuery, null);
        count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    // Updating single question
    public int updateQuestionEntity(QuestionEntity questionEntity) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_QUESTION_ID, questionEntity.getQuestionId());
        values.put(KEY_QUESTION_PRODUCT_ID, questionEntity.getProductId());
        values.put(KEY_QUESTION_TEXT, questionEntity.getQuestionText());
        values.put(KEY_QUESTION_UPDATED_AT, questionEntity.getUpdatedAt());

        // updating row
        int update = sqLiteDatabase.update(TABLE_QUESTION, values, KEY_QUESTION_ID + " = ?",
                new String[] { String.valueOf(questionEntity.getQuestionId()) });

        sqLiteDatabase.close();

        return update;
    }

    // Deleting single question
    public void deleteQuestion(QuestionEntity questionEntity) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_QUESTION, KEY_QUESTION_ID + " = ?",
                new String[] { String.valueOf(questionEntity.getQuestionId()) });
        sqLiteDatabase.close();
    }

    // Drop table
    public void dropQuestionTable(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTION);
    }


    /*
    Methods for Answer Entity >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
     */

    // Adding new answerEntity
    public void addAnswerEntity(AnswerEntity answerEntity) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ANSWER_ID, answerEntity.getAnswerId());
        values.put(KEY_ANSWER_RETAILER_ID, answerEntity.getRetailerId());
        values.put(KEY_ANSWER_QUESTION_ID, answerEntity.getQuestionId());
        values.put(KEY_ANSWER_TEXT, answerEntity.getAnswerText());
        values.put(KEY_ANSWER_UPDATED_AT, answerEntity.getUpdatedAt());

        // Inserting Row
        sqLiteDatabase.insert(TABLE_ANSWER, null, values);
        sqLiteDatabase.close(); // Closing database connection
    }

    // Adding new answerEntities
    public void addAnswerEntities(ArrayList<AnswerEntity> answerEntities) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        for(int index = 0; index < answerEntities.size(); index++){
            AnswerEntity answerEntity = answerEntities.get(index);

            ContentValues values = new ContentValues();
            values.put(KEY_ANSWER_ID, answerEntity.getAnswerId());
            values.put(KEY_ANSWER_RETAILER_ID, answerEntity.getRetailerId());
            values.put(KEY_ANSWER_QUESTION_ID, answerEntity.getQuestionId());
            values.put(KEY_ANSWER_TEXT, answerEntity.getAnswerText());
            values.put(KEY_ANSWER_UPDATED_AT, answerEntity.getUpdatedAt());

            // Inserting Row
            sqLiteDatabase.insert(TABLE_ANSWER, null, values);
        }

        sqLiteDatabase.close(); // Closing database connection
    }

    // Getting single AnswerEntity
    public AnswerEntity getAnswerEntityFromAnswerId(String answerId) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query(TABLE_ANSWER, new String[] { KEY_ANSWER_ID, KEY_ANSWER_RETAILER_ID, KEY_ANSWER_QUESTION_ID, KEY_ANSWER_TEXT, KEY_ANSWER_UPDATED_AT}
                , KEY_ANSWER_ID + "=?", new String[] { String.valueOf(answerId) }, null, null, null, null);
        if (cursor.getCount() > 0){
            cursor.moveToFirst();
            AnswerEntity answerEntity = new AnswerEntity(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4));
            // return retailerEntity
            return answerEntity;
        }else{
            return null;
        }
    }

    // Getting answerEntities from questionId
    public ArrayList<AnswerEntity> getAnswerEntitiesFromQuestionId(String questionId) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ArrayList<AnswerEntity> answerEntities = new ArrayList<AnswerEntity>();

        Cursor cursor = sqLiteDatabase.query(TABLE_ANSWER, new String[] { KEY_ANSWER_ID, KEY_ANSWER_RETAILER_ID, KEY_ANSWER_QUESTION_ID, KEY_ANSWER_TEXT, KEY_ANSWER_UPDATED_AT}
                , KEY_ANSWER_QUESTION_ID + "=?", new String[] { String.valueOf(questionId) }, null, null, null, null);
        if (cursor.getCount() > 0){
            while (cursor.moveToNext()){
                AnswerEntity answerEntity = new AnswerEntity(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4));
                answerEntities.add(answerEntity);
            }
            // return answerEntities
            return answerEntities;
        }else{
            return null;
        }
    }

    // Updating single answer
    public int updateAnswerEntity(AnswerEntity answerEntity) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ANSWER_ID, answerEntity.getAnswerId());
        values.put(KEY_ANSWER_RETAILER_ID, answerEntity.getRetailerId());
        values.put(KEY_ANSWER_QUESTION_ID, answerEntity.getQuestionId());
        values.put(KEY_ANSWER_TEXT, answerEntity.getAnswerText());
        values.put(KEY_ANSWER_UPDATED_AT, answerEntity.getUpdatedAt());

        // updating row
        int update = sqLiteDatabase.update(TABLE_ANSWER, values, KEY_ANSWER_ID + " = ?",
                new String[] { String.valueOf(answerEntity.getAnswerId()) });

        sqLiteDatabase.close();

        return update;
    }

    // Deleting single answer
    public void deleteAnswer(AnswerEntity answerEntity) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_ANSWER, KEY_ANSWER_ID + " = ?",
                new String[] { String.valueOf(answerEntity.getAnswerId()) });
        sqLiteDatabase.close();
    }

    // Drop table
    public void dropAnswerTable(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_ANSWER);
    }


    /*
    Methods for Product Entity >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
     */

    // Adding new productEntity
    public void addProductEntity(ProductEntity productEntity) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PRODUCT_ID, productEntity.getProductId());
        values.put(KEY_PRODUCT_CATEGORY, productEntity.getCategory());
        values.put(KEY_PRODUCT_CATEGORY_DESCRIPTION, productEntity.getCategoryDescription());
        values.put(KEY_PRODUCT_NAME, productEntity.getProductName());
        values.put(KEY_PRODUCT_DESCRIPTION, productEntity.getProductDescription());
        values.put(KEY_PRODUCT_IMAGE_URL, productEntity.getImageurl());

        // Inserting Row
        sqLiteDatabase.insert(TABLE_PRODUCT, null, values);
        sqLiteDatabase.close(); // Closing database connection
    }

    // Adding new ProductEntities
    public void addProductEntities(ArrayList<ProductEntity> productEntities) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        for(int index = 0; index < productEntities.size(); index++){
            ProductEntity productEntity = productEntities.get(index);

            ContentValues values = new ContentValues();
            values.put(KEY_PRODUCT_ID, productEntity.getProductId());
            values.put(KEY_PRODUCT_CATEGORY, productEntity.getCategory());
            values.put(KEY_PRODUCT_CATEGORY_DESCRIPTION, productEntity.getCategoryDescription());
            values.put(KEY_PRODUCT_NAME, productEntity.getProductName());
            values.put(KEY_PRODUCT_DESCRIPTION, productEntity.getProductDescription());
            values.put(KEY_PRODUCT_IMAGE_URL, productEntity.getImageurl());

            // Inserting Row
            sqLiteDatabase.insert(TABLE_PRODUCT, null, values);
        }

        sqLiteDatabase.close(); // Closing database connection
    }

    // Getting single ProductEntity
    public ProductEntity getProductEntityFromProductId(String productId) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query(TABLE_PRODUCT, new String[] { KEY_PRODUCT_ID, KEY_PRODUCT_CATEGORY, KEY_PRODUCT_CATEGORY_DESCRIPTION,
                        KEY_PRODUCT_NAME, KEY_PRODUCT_DESCRIPTION, KEY_PRODUCT_IMAGE_URL}, KEY_PRODUCT_ID + "=?",
                new String[] { String.valueOf(productId) }, null, null, null, null);
        if (cursor.getCount() > 0){
            cursor.moveToFirst();
            ProductEntity productEntity = new ProductEntity(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4), cursor.getString(5));
            // return productEntity
            return productEntity;
        }else{
            return null;
        }
    }

    // Getting single ProductEntity
    public ProductEntity getProductEntityFromProductName(String productName) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query(TABLE_PRODUCT, new String[] { KEY_PRODUCT_ID, KEY_PRODUCT_CATEGORY, KEY_PRODUCT_CATEGORY_DESCRIPTION,
                        KEY_PRODUCT_NAME, KEY_PRODUCT_DESCRIPTION, KEY_PRODUCT_IMAGE_URL}, KEY_PRODUCT_NAME + "=?",
                new String[] { String.valueOf(productName) }, null, null, null, null);
        if (cursor.getCount() > 0){
            cursor.moveToFirst();
            ProductEntity productEntity = new ProductEntity(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4), cursor.getString(5));
            // return productEntity
            return productEntity;
        }else{
            return null;
        }
    }

    // Getting All ProductEntities
    public ArrayList<ProductEntity> getAllProductEntities() {
        ArrayList<ProductEntity> productEntities = new ArrayList<ProductEntity>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_PRODUCT + ";";

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ProductEntity productEntity = new ProductEntity(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4), cursor.getString(5));
                // Adding product to list
                productEntities.add(productEntity);
            } while (cursor.moveToNext());
        }

        // return product list
        return productEntities;
    }

    // Getting  product count
    public int getProductCount() {
        int count = 0;
        String countQuery = "SELECT  * FROM " + TABLE_PRODUCT  + ";";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(countQuery, null);
        count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    // Updating single product
    public int updateProductEntity(ProductEntity productEntity) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PRODUCT_ID, productEntity.getProductId());
        values.put(KEY_PRODUCT_CATEGORY, productEntity.getCategory());
        values.put(KEY_PRODUCT_CATEGORY_DESCRIPTION, productEntity.getCategoryDescription());
        values.put(KEY_PRODUCT_NAME, productEntity.getProductName());
        values.put(KEY_PRODUCT_DESCRIPTION, productEntity.getProductDescription());
        values.put(KEY_PRODUCT_IMAGE_URL, productEntity.getImageurl());

        // updating row
        int update = sqLiteDatabase.update(TABLE_PRODUCT, values, KEY_PRODUCT_ID + " = ?",
                new String[] { String.valueOf(productEntity.getProductId()) });

        sqLiteDatabase.close();

        return update;
    }

    // Deleting single product
    public void deleteProduct(ProductEntity productEntity) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_PRODUCT, KEY_PRODUCT_ID + " = ?",
                new String[] { String.valueOf(productEntity.getProductId()) });
        sqLiteDatabase.close();
    }

    // Drop table
    public void dropProductTable(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT);
    }

    /*
    Methods for Retailer Entity >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
     */

    // Adding new retailerEntity
    public void addRetailerEntity(RetailerEntity retailerEntity) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_RETAILER_ID, retailerEntity.getRetailerId());
        values.put(KEY_RETAILER_SHOP_NAME, retailerEntity.getShopName());

        // Inserting Row
        sqLiteDatabase.insert(TABLE_RETAILER, null, values);
        sqLiteDatabase.close(); // Closing database connection
    }

    // Adding new retailerEntities
    public void addRetailerEntities(ArrayList<RetailerEntity> retailerEntities) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        for(int index = 0; index < retailerEntities.size(); index++){
            RetailerEntity retailerEntity = retailerEntities.get(index);

            ContentValues values = new ContentValues();
            values.put(KEY_RETAILER_ID, retailerEntity.getRetailerId());
            values.put(KEY_RETAILER_SHOP_NAME, retailerEntity.getShopName());

            // Inserting Row
            sqLiteDatabase.insert(TABLE_RETAILER, null, values);
        }

        sqLiteDatabase.close(); // Closing database connection
    }

    // Getting single RetailerEntity
    public RetailerEntity getRetailerEntityFromRetailerId(String retailerId) {
        Log.v("Debug@@@>>>>>> ", "here");
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query(TABLE_RETAILER, new String[] { KEY_RETAILER_ID, KEY_RETAILER_SHOP_NAME}, KEY_RETAILER_ID + "=?",
                new String[] { retailerId }, null, null, null, null);
        Log.v("Debug@@@>>>>>>", Integer.toString(cursor.getCount()));
        if (cursor.getCount() > 0){
            cursor.moveToFirst();
            RetailerEntity retailerEntity = new RetailerEntity(cursor.getString(0), cursor.getString(1));
            // return retailerEntity
            return retailerEntity;
        }else{
            return null;
        }
    }

    // Getting single RetailerEntity
    public RetailerEntity getRetailerEntityFromShopName(String shopName) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query(TABLE_RETAILER, new String[] { KEY_RETAILER_ID, KEY_RETAILER_SHOP_NAME}, KEY_RETAILER_SHOP_NAME + "=?",
                new String[] { String.valueOf(shopName) }, null, null, null, null);
        if (cursor.getCount() > 0){
            cursor.moveToFirst();
            RetailerEntity retailerEntity = new RetailerEntity(cursor.getString(0), cursor.getString(1));
            // return retailerEntity
            return retailerEntity;
        }else{
            return null;
        }
    }

    // Getting All RetailerEntity
    public ArrayList<RetailerEntity> getAllRetailerEntities() {
        ArrayList<RetailerEntity> retailerEntities = new ArrayList<RetailerEntity>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_RETAILER + ";";

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                RetailerEntity retailerEntity = new RetailerEntity(cursor.getString(0), cursor.getString(1));
                // Adding retailer to list
                retailerEntities.add(retailerEntity);
            } while (cursor.moveToNext());
        }

        // return retailer list
        return retailerEntities;
    }

    // Getting  retailer count
    public int getRetailerCount() {
        int count = 0;
        String countQuery = "SELECT  * FROM " + TABLE_RETAILER + ";";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(countQuery, null);
        count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    // Updating single retailer
    public int updateRetailerEntity(RetailerEntity retailerEntity) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_RETAILER_ID, retailerEntity.getRetailerId());
        values.put(KEY_RETAILER_SHOP_NAME, retailerEntity.getShopName());

        // updating row
        int update = sqLiteDatabase.update(TABLE_RETAILER, values, KEY_RETAILER_ID + " = ?",
                new String[] { String.valueOf(retailerEntity.getRetailerId()) });

        sqLiteDatabase.close();

        return update;
    }

    // Deleting single retailer
    public void deleteRetailer(RetailerEntity retailerEntity) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_RETAILER, KEY_RETAILER_ID + " = ?",
                new String[] { String.valueOf(retailerEntity.getRetailerId()) });
        sqLiteDatabase.close();
    }

    // Drop table
    public void dropRetailerTable(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_RETAILER);
    }


}
