package com.shopchat.consumer.models.entities;

/**
 * Created by sourin on 02/12/15.
 */
public class AnswerEntity {

    private String answerId;
    private String retailerId;
    private String questionId;
    private String answerText;
    private long updatedAt;

    public AnswerEntity(String answerId, String retailerId, String questionId, String answerText, long updatedAt) {
        this.answerId = answerId;
        this.retailerId = retailerId;
        this.questionId = questionId;
        this.answerText = answerText;
        this.updatedAt = updatedAt;
    }

    public AnswerEntity(){

    }

    public String getAnswerId() {
        return answerId;
    }

    public void setAnswerId(String answerId) {
        this.answerId = answerId;
    }

    public String getRetailerId() {
        return retailerId;
    }

    public void setRetailerId(String retailerId) {
        this.retailerId = retailerId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }
}
