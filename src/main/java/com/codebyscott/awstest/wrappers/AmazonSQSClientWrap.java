package com.codebyscott.awstest.wrappers;

import com.amazonaws.services.sqs.model.*;

/**
 * Created by bradleyschwab on 4/6/17.
 */
public interface AmazonSQSClientWrap {

    CreateQueueResult createQueue(String queueName);
    Void sendMessage(SendMessageRequest sendMessageRequest);
    ReceiveMessageResult receiveMessage(ReceiveMessageRequest receiveMessageRequest);
    Void deleteMessage(DeleteMessageRequest deleteMessageRequest);
    Void deleteQueue(String queueName);
}
