package com.codebyscott.awstest;

import com.amazonaws.services.sns.model.*;
import com.codebyscott.awstest.wrappers.AmazonSNSClientWrap;
import org.apache.log4j.Logger;

/**
 * Created by bradleyschwab on 4/5/17.
 */
public class SNSClient {

    public static Logger logger = Logger.getLogger(SNSClient.class);

    private AmazonSNSClientWrap snsWrap;

    SNSClient(AmazonSNSClientWrap snsWrap) {
        this.snsWrap = snsWrap;
    }

    public void sendMessage(String testTopic, int waitForSubsciptionInSeconds) {
        CreateTopicResult createTopicResult = snsWrap.createTopic(testTopic);
        String topicArn = createTopicResult.getTopicArn();
        logger.debug("topic " + testTopic + " " + topicArn);
        SubscribeResult subscribeResult = snsWrap.subscribe(topicArn,"email","scott.schwab@gmail.com");
        logger.debug("sns subscription result: " + subscribeResult);
        PublishRequest publishRequest = new PublishRequest()
                .withTargetArn(topicArn)
                .withMessage("hello")
                .withSubject("test message");

        try {
            Thread.sleep(waitForSubsciptionInSeconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        PublishResult publishResult = snsWrap.publish(publishRequest);
        logger.debug("public results " + publishResult);

    }


}
