package com.shopchat.consumer.models.entities;

/**
 * Created by sourin on 12/12/15.
 */
public class InboxMessageEntity {

    private String messageHeader;
    private String messageDetail;
    private String questionId;

    public InboxMessageEntity(){
    }

    public InboxMessageEntity(String messageHeader, String messageDetail, String questionId) {
        this.messageHeader = messageHeader;
        this.messageDetail = messageDetail;
        this.questionId = questionId;
    }

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

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }
}
