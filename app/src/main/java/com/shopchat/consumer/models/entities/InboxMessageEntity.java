package com.shopchat.consumer.models.entities;

/**
 * Created by sourin on 12/12/15.
 */
public class InboxMessageEntity {

    private String messageHeader;
    private String messageDetail;
    private int seen;
    private String questionId;

    public InboxMessageEntity(String messageHeader, String messageDetail, int seen, String questionId) {
        this.messageHeader = messageHeader;
        this.messageDetail = messageDetail;
        this.seen = seen;
        this.questionId = questionId;
    }

    public InboxMessageEntity(){}

    public String getMessageHeader() {
        return messageHeader;
    }

    public void setMessageHeader(String messageHeader) {
        this.messageHeader = messageHeader;
    }

    public String getMessageDetail() {
        return messageDetail;
    }

    public void setMessageDetail(String messageDetail) {
        this.messageDetail = messageDetail;
    }

    public int getSeen() {
        return seen;
    }

    public void setSeen(int seen) {
        this.seen = seen;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }
}
