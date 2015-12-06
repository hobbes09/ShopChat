//package com.shopchat.consumer.models.dbhandlers;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//import android.util.Log;
//
//import com.shopchat.consumer.models.entities.AnswerEntity;
//
//import java.util.ArrayList;
//
///**
// * Created by sourin on 02/12/15.
// */
//public class AnswerEntityTableHandler extends SQLiteOpenHelper {
//
//    // All Static variables
//    // Database Version
//    private static final int DATABASE_VERSION = 2;
//
//    // Database Name
//    private static final String DATABASE_NAME = "LokalChatDB";
//
//    // Answer table name
//    private static final String TABLE_ANSWER = "answers";
//
//    // Answer Table Columns names
//    private static final String KEY_ANSWER_ID = "answer_id";
//    private static final String KEY_ANSWER_RETAILER_ID = "retailer_id";
//    private static final String KEY_ANSWER_QUESTION_ID = "question_id";
//    private static final String KEY_ANSWER_TEXT = "answer_text";
//    private static final String KEY_ANSWER_UPDATED_AT = "updated_at";
//
//
//    public AnswerEntityTableHandler(Context context) {
//        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase sqLiteDatabase) {
//        Log.v("Create>>>", "creating answer");
//        String CREATE_ANSWER_TABLE = "CREATE TABLE " + TABLE_ANSWER + " (" +
//                KEY_ANSWER_ID + " VARCHAR(255) PRIMARY KEY, " +
//                KEY_ANSWER_RETAILER_ID + " VARCHAR(255), " +
//                KEY_ANSWER_QUESTION_ID + " VARCHAR(255), " +
//                KEY_ANSWER_TEXT + " VARCHAR(255), " +
//                KEY_ANSWER_UPDATED_AT +" BIGINT UNSIGNED NOT NULL )";
//
//
//        sqLiteDatabase.execSQL(CREATE_ANSWER_TABLE);
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
//        // Drop older table if existed
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_ANSWER);
//
//        // Create tables again
//        onCreate(sqLiteDatabase);
//    }
//
//    // Adding new answerEntity
//    public void addAnswerEntity(AnswerEntity answerEntity) {
//        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(KEY_ANSWER_ID, answerEntity.getAnswerId());
//        values.put(KEY_ANSWER_RETAILER_ID, answerEntity.getRetailerId());
//        values.put(KEY_ANSWER_QUESTION_ID, answerEntity.getQuestionId());
//        values.put(KEY_ANSWER_TEXT, answerEntity.getAnswerText());
//        values.put(KEY_ANSWER_UPDATED_AT, answerEntity.getUpdatedAt());
//
//        // Inserting Row
//        sqLiteDatabase.insert(TABLE_ANSWER, null, values);
//        sqLiteDatabase.close(); // Closing database connection
//    }
//
//    // Adding new answerEntities
//    public void addAnswerEntities(ArrayList<AnswerEntity> answerEntities) {
//        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
//
//        for(int index = 0; index < answerEntities.size(); index++){
//            AnswerEntity answerEntity = answerEntities.get(index);
//
//            ContentValues values = new ContentValues();
//            values.put(KEY_ANSWER_ID, answerEntity.getAnswerId());
//            values.put(KEY_ANSWER_RETAILER_ID, answerEntity.getRetailerId());
//            values.put(KEY_ANSWER_QUESTION_ID, answerEntity.getQuestionId());
//            values.put(KEY_ANSWER_TEXT, answerEntity.getAnswerText());
//            values.put(KEY_ANSWER_UPDATED_AT, answerEntity.getUpdatedAt());
//
//            // Inserting Row
//            sqLiteDatabase.insert(TABLE_ANSWER, null, values);
//        }
//
//        sqLiteDatabase.close(); // Closing database connection
//    }
//
//    // Getting single AnswerEntity
//    public AnswerEntity getAnswerEntityFromAnswerId(String answerId) {
//        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
//
//        Cursor cursor = sqLiteDatabase.query(TABLE_ANSWER, new String[] { KEY_ANSWER_ID, KEY_ANSWER_RETAILER_ID, KEY_ANSWER_QUESTION_ID, KEY_ANSWER_TEXT, KEY_ANSWER_UPDATED_AT}
//                , KEY_ANSWER_ID + "=?", new String[] { String.valueOf(answerId) }, null, null, null, null);
//        if (cursor.getCount() > 0){
//            cursor.moveToFirst();
//            AnswerEntity answerEntity = new AnswerEntity(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4));
//            // return retailerEntity
//            return answerEntity;
//        }else{
//            return null;
//        }
//    }
//
//    // Getting answerEntities from questionId
//    public ArrayList<AnswerEntity> getAnswerEntitiesFromQuestionId(String questionId) {
//        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
//        ArrayList<AnswerEntity> answerEntities = new ArrayList<AnswerEntity>();
//
//        Cursor cursor = sqLiteDatabase.query(TABLE_ANSWER, new String[] { KEY_ANSWER_ID, KEY_ANSWER_RETAILER_ID, KEY_ANSWER_QUESTION_ID, KEY_ANSWER_TEXT, KEY_ANSWER_UPDATED_AT}
//                , KEY_ANSWER_QUESTION_ID + "=?", new String[] { String.valueOf(questionId) }, null, null, null, null);
//        if (cursor.getCount() > 0){
//            while (cursor.moveToNext()){
//                AnswerEntity answerEntity = new AnswerEntity(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4));
//                answerEntities.add(answerEntity);
//            }
//            // return answerEntities
//            return answerEntities;
//        }else{
//            return null;
//        }
//    }
//
//    // Updating single answer
//    public int updateAnswerEntity(AnswerEntity answerEntity) {
//        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(KEY_ANSWER_ID, answerEntity.getAnswerId());
//        values.put(KEY_ANSWER_RETAILER_ID, answerEntity.getRetailerId());
//        values.put(KEY_ANSWER_QUESTION_ID, answerEntity.getQuestionId());
//        values.put(KEY_ANSWER_TEXT, answerEntity.getAnswerText());
//        values.put(KEY_ANSWER_UPDATED_AT, answerEntity.getUpdatedAt());
//
//        // updating row
//        int update = sqLiteDatabase.update(TABLE_ANSWER, values, KEY_ANSWER_ID + " = ?",
//                new String[] { String.valueOf(answerEntity.getAnswerId()) });
//
//        sqLiteDatabase.close();
//
//        return update;
//    }
//
//    // Deleting single answer
//    public void deleteAnswer(AnswerEntity answerEntity) {
//        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
//        sqLiteDatabase.delete(TABLE_ANSWER, KEY_ANSWER_ID + " = ?",
//                new String[] { String.valueOf(answerEntity.getAnswerId()) });
//        sqLiteDatabase.close();
//    }
//
//    // Drop table
//    public void dropTable(){
//        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_ANSWER);
//    }
//
//
//}
