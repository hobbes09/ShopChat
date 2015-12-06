//package com.shopchat.consumer.models.dbhandlers;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//import android.util.Log;
//
//import com.shopchat.consumer.models.entities.QuestionEntity;
//
//import java.util.ArrayList;
//
///**
// * Created by sourin on 03/12/15.
// */
//public class QuestionEntityTableHandler extends SQLiteOpenHelper {
//
//    // All Static variables
//    // Database Version
//    private static final int DATABASE_VERSION = 2;
//
//    // Database Name
//    private static final String DATABASE_NAME = "LokalChatDB";
//
//    // Question table name
//    private static final String TABLE_QUESTION = "questions";
//
//    // Question Table Columns names
//    private static final String KEY_QUESTION_ID = "question_id";
//    private static final String KEY_QUESTION_PRODUCT_ID = "product_id";
//    private static final String KEY_QUESTION_TEXT = "question_text";
//    private static final String KEY_QUESTION_UPDATED_AT = "updated_at";
//
//
//    public QuestionEntityTableHandler(Context context) {
//        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase sqLiteDatabase) {
//        Log.v("Create>>>", "creating question");
//        String CREATE_QUESTION_TABLE = "CREATE TABLE " + TABLE_QUESTION + " (" +
//                KEY_QUESTION_ID + " VARCHAR(255) PRIMARY KEY, " +
//                KEY_QUESTION_PRODUCT_ID + " VARCHAR(255), " +
//                KEY_QUESTION_TEXT + " VARCHAR(255), " +
//                KEY_QUESTION_UPDATED_AT +" BIGINT UNSIGNED NOT NULL )";
//
//        sqLiteDatabase.execSQL(CREATE_QUESTION_TABLE);
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
//        // Drop older table if existed
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTION);
//
//        // Create tables again
//        onCreate(sqLiteDatabase);
//    }
//
//    // Adding new questionEntity
//    public void addQuestionEntity(QuestionEntity questionEntity) {
//        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(KEY_QUESTION_ID, questionEntity.getQuestionId());
//        values.put(KEY_QUESTION_PRODUCT_ID, questionEntity.getProductId());
//        values.put(KEY_QUESTION_TEXT, questionEntity.getQuestionText());
//        values.put(KEY_QUESTION_UPDATED_AT, questionEntity.getUpdatedAt());
//
//        // Inserting Row
//        sqLiteDatabase.insert(TABLE_QUESTION, null, values);
//        sqLiteDatabase.close(); // Closing database connection
//    }
//
//    // Getting single QuestionEntity
//    public QuestionEntity getQuestionEntityFromQuestionId(String questionId) {
//        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
//
//        Cursor cursor = sqLiteDatabase.query(TABLE_QUESTION, new String[] { KEY_QUESTION_ID, KEY_QUESTION_PRODUCT_ID, KEY_QUESTION_TEXT, KEY_QUESTION_UPDATED_AT}
//                , KEY_QUESTION_ID + "=?", new String[] { String.valueOf(questionId) }, null, null, null, null);
//        if (cursor.getCount() > 0){
//            cursor.moveToFirst();
//            QuestionEntity questionEntity = new QuestionEntity(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3));
//            // return questionEntity
//            return questionEntity;
//        }else{
//            return null;
//        }
//    }
//
//    // Getting All questionEntities
//    public ArrayList<QuestionEntity> getAllQuestionEntities() {
//        ArrayList<QuestionEntity> questionEntities = new ArrayList<QuestionEntity>();
//        // Select All Query
//        String selectQuery = "SELECT * FROM " + TABLE_QUESTION + ";";
//
//        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
//        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
//
//        // looping through all rows and adding to list
//        if (cursor.moveToFirst()) {
//            do {
//                QuestionEntity questionEntity = new QuestionEntity(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3));
//                // Adding product to list
//                questionEntities.add(questionEntity);
//            } while (cursor.moveToNext());
//        }
//
//        // return question list
//        return questionEntities;
//    }
//
//    // Getting  question count
//    public int getQuestionCount() {
//        int count = 0;
//        String countQuery = "SELECT  * FROM " + TABLE_QUESTION  + ";";
//        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
//        Cursor cursor = sqLiteDatabase.rawQuery(countQuery, null);
//        count = cursor.getCount();
//        cursor.close();
//
//        // return count
//        return count;
//    }
//
//    // Updating single question
//    public int updateQuestionEntity(QuestionEntity questionEntity) {
//        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(KEY_QUESTION_ID, questionEntity.getQuestionId());
//        values.put(KEY_QUESTION_PRODUCT_ID, questionEntity.getProductId());
//        values.put(KEY_QUESTION_TEXT, questionEntity.getQuestionText());
//        values.put(KEY_QUESTION_UPDATED_AT, questionEntity.getUpdatedAt());
//
//        // updating row
//        int update = sqLiteDatabase.update(TABLE_QUESTION, values, KEY_QUESTION_ID + " = ?",
//                new String[] { String.valueOf(questionEntity.getQuestionId()) });
//
//        sqLiteDatabase.close();
//
//        return update;
//    }
//
//    // Deleting single question
//    public void deleteQuestion(QuestionEntity questionEntity) {
//        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
//        sqLiteDatabase.delete(TABLE_QUESTION, KEY_QUESTION_ID + " = ?",
//                new String[] { String.valueOf(questionEntity.getQuestionId()) });
//        sqLiteDatabase.close();
//    }
//
//    // Drop table
//    public void dropTable(){
//        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTION);
//    }
//
//
//
//}
