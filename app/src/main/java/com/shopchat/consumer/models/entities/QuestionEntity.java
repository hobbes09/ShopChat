package com.shopchat.consumer.models.entities;

/**
 * Created by sourin on 02/12/15.
 */
public class QuestionEntity {

    private String questionId;
    private String productId;
    private String questionText;
    private int seen;
    private long updatedAt;

    public QuestionEntity(String questionId, String productId, String questionText, int seen, long updatedAt) {
        this.questionId = questionId;
        this.productId = productId;
        this.questionText = questionText;
        this.seen = seen;
        this.updatedAt = updatedAt;
    }

    public QuestionEntity(){
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public int getSeen() {
        return seen;
    }

    public void setSeen(int seen) {
        this.seen = seen;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }
}
