package com.shopchat.consumer.models.pojos;

/**
 * Created by sourin on 01/12/15.
 */
public class AnswerPojo {

    private String id;
    private long lastModifiedDate;
    private String question;
    private String questionid;
    private RetailerPojo retailer;
    private String answerText;
    private String retailerId;

    public AnswerPojo(String id, long lastModifiedDate, String question, String questionid, RetailerPojo retailer, String answerText, String retailerId) {
        this.id = id;
        this.lastModifiedDate = lastModifiedDate;
        this.question = question;
        this.questionid = questionid;
        this.retailer = retailer;
        this.answerText = answerText;
        this.retailerId = retailerId;
    }

    public AnswerPojo(){
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(long lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestionid() {
        return questionid;
    }

    public void setQuestionid(String questionid) {
        this.questionid = questionid;
    }

    public RetailerPojo getRetailer() {
        return retailer;
    }

    public void setRetailer(RetailerPojo retailer) {
        this.retailer = retailer;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public String getRetailerId() {
        return retailerId;
    }

    public void setRetailerId(String retailerId) {
        this.retailerId = retailerId;
    }
}
