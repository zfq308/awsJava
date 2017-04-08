package com.codebyscott.awstest;

import com.amazonaws.services.sqs.model.*;
import com.codebyscott.awstest.wrappers.AmazonSQSClientWrap;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by bradleyschwab on 4/5/17.
 */
public class SQSClient {

    public static Logger logger = Logger.getLogger(SQSClient.class);

    private AmazonSQSClientWrap wrap;

    public SQSClient(AmazonSQSClientWrap wrap) {
        this.wrap = wrap;
    }

    public void createMessageQueue() {
        String queueName = "testQueue";
        CreateQueueResult createQueueResult = wrap.createQueue(queueName);
        String queueUrl = createQueueResult.getQueueUrl();

        Map<String,MessageAttributeValue> m = new HashMap<>();

        sendMessage(queueUrl, "this is test message 1");
        sendMessage(queueUrl, "this is test message 2");

        List<String> msg1 = receiveMessage(queueUrl);
        List<String> msg2 = receiveMessage(queueUrl);

        logger.debug(msg1);
        logger.debug(msg2);

        wrap.deleteQueue(queueName);
    }

    public void sendMessage(String queueUrl, String message) {
        SendMessageRequest sendMessageRequest = new SendMessageRequest()
                .withQueueUrl(queueUrl)
                .withMessageBody(message);
        wrap.sendMessage(sendMessageRequest);
    }

    public List<String> receiveMessage(String queueUrl) {
        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest()
                .withQueueUrl(queueUrl)
                .withWaitTimeSeconds(5)
                .withMaxNumberOfMessages(5);
        ReceiveMessageResult receiveMessageResult = wrap.receiveMessage(receiveMessageRequest);
        List<Message> messageList = receiveMessageResult.getMessages();
        List<String> messageOut = messageList.stream().map(x -> x.getBody()).collect(Collectors.toList());
        messageList.stream().map(x -> wrap.deleteMessage(
                new DeleteMessageRequest()
                        .withQueueUrl(queueUrl)
                        .withReceiptHandle(x.getReceiptHandle())));
        return messageOut;
    }
}
