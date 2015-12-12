package com.shopchat.consumer.models.entities;

/**
 * Created by sourin on 12/12/15.
 */
public class MessageUnitEntity {

    private String speaker;
    private String message;
    private String writtenAt;
    private boolean isQuestion;
    private boolean isAnswer;
    private String referenceId;

    public MessageUnitEntity() {
    }

    public MessageUnitEntity(String speaker, String message, String writtenAt, boolean isQuestion, boolean isAnswer, String referenceId) {
        this.speaker = speaker;
        this.message = message;
        this.writtenAt = writtenAt;
        this.isQuestion = isQuestion;
        this.isAnswer = isAnswer;
        this.referenceId = referenceId;
    }

    public String getSpeaker() {
        return speaker;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getWrittenAt() {
        return writtenAt;
    }

    public void setWrittenAt(String writtenAt) {
        this.writtenAt = writtenAt;
    }

    public boolean isQuestion() {
        return isQuestion;
    }

    public void setIsQuestion(boolean isQuestion) {
        this.isQuestion = isQuestion;
    }

    public boolean isAnswer() {
        return isAnswer;
    }

    public void setIsAnswer(boolean isAnswer) {
        this.isAnswer = isAnswer;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }
}
