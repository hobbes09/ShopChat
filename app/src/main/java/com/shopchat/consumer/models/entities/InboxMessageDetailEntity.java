package com.shopchat.consumer.models.entities;

import java.util.ArrayList;

/**
 * Created by sourin on 12/12/15.
 */
public class InboxMessageDetailEntity {

    private MessageUnitEntity questionMessageUnitEntity;
    private ArrayList<MessageUnitEntity> answersMessageUnitEntity;

    public InboxMessageDetailEntity() {
    }

    public InboxMessageDetailEntity(MessageUnitEntity questionMessageUnitEntity, ArrayList<MessageUnitEntity> answersMessageUnitEntity) {
        this.questionMessageUnitEntity = questionMessageUnitEntity;
        this.answersMessageUnitEntity = answersMessageUnitEntity;
    }

    public MessageUnitEntity getQuestionMessageUnitEntity() {
        return questionMessageUnitEntity;
    }

    public void setQuestionMessageUnitEntity(MessageUnitEntity questionMessageUnitEntity) {
        this.questionMessageUnitEntity = questionMessageUnitEntity;
    }

    public ArrayList<MessageUnitEntity> getAnswersMessageUnitEntity() {
        return answersMessageUnitEntity;
    }

    public void setAnswersMessageUnitEntity(ArrayList<MessageUnitEntity> answersMessageUnitEntity) {
        this.answersMessageUnitEntity = answersMessageUnitEntity;
    }
}
