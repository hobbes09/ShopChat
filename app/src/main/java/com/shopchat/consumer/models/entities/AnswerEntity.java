package com.shopchat.consumer.models.entities;

/**
 * Created by sourin on 02/12/15.
 */
public class AnswerEntity {

    private String answerId;
    private String answerText;
    private String retailerId;
    private String questionId;
    private long updatedAt;

    public AnswerEntity(String answerId, String answerText, String retailerId, String questionId, long updatedAt) {
        this.answerId = answerId;
        this.answerText = answerText;
        this.retailerId = retailerId;
        this.questionId = questionId;
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

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }
}
