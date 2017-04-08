package com.codebyscott.awstest.wrappers.impl;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.*;
import com.codebyscott.awstest.wrappers.AmazonSQSClientWrap;
import org.apache.log4j.Logger;

/**
 * Created by bradleyschwab on 4/6/17.
 */
public class AmazonSQSClientWrapImpl implements AmazonSQSClientWrap {
    Logger logger = Logger.getLogger(AmazonSQSClientWrapImpl.class);

    private AmazonSQS sqsClient;

    public AmazonSQSClientWrapImpl(AmazonSQS amazonSQS) {
        this.sqsClient = amazonSQS;
    }


    @Override
    public CreateQueueResult createQueue(String queueName) {
        CreateQueueResult result = sqsClient.createQueue(queueName);
        logger.debug("create queue result " + result);
        return result;
    }

    @Override
    public Void sendMessage(SendMessageRequest sendMessageRequest) {
        sqsClient.sendMessage(sendMessageRequest);
        logger.debug("send message called");
        return null;
    }

    @Override
    public ReceiveMessageResult receiveMessage(ReceiveMessageRequest receiveMessageRequest) {
        ReceiveMessageResult receiveMessageResult = sqsClient.receiveMessage(receiveMessageRequest);
        logger.debug("receiveMessage return " + receiveMessageResult);
        return receiveMessageResult;
    }

    @Override
    public Void deleteMessage(DeleteMessageRequest deleteMessageRequest) {
        sqsClient.deleteMessage(deleteMessageRequest);
        logger.debug("deleteMessage called");
        return null;
    }

    @Override
    public Void deleteQueue(String queueName) {
        sqsClient.deleteQueue(queueName);
        logger.debug("deleteQueue called");
        return null;
    }
}
