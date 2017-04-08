package com.codebyscott.awstest.wrappers.impl;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.CreateTopicResult;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.amazonaws.services.sns.model.SubscribeResult;
import com.codebyscott.awstest.wrappers.AmazonSNSClientWrap;
import org.apache.log4j.Logger;

/**
 * Created by bradleyschwab on 4/5/17.
 */
public class AmazonSNSClientWrapImpl implements AmazonSNSClientWrap {
    Logger logger = Logger.getLogger(AmazonSNSClientWrapImpl.class);

    private AmazonSNS snsClient;

    public AmazonSNSClientWrapImpl(AmazonSNS amazonSNS) {
        this.snsClient = amazonSNS;
    }

    @Override
    public CreateTopicResult createTopic(String testTopic) {
        CreateTopicResult result = snsClient.createTopic(testTopic);
        logger.debug("create topic  return " + result);
        return result;
    }

    @Override
    public SubscribeResult subscribe(String topic, String method, String endpoint) {
        SubscribeResult result = snsClient.subscribe(topic,method,endpoint);
        logger.debug("subscibe return " + result);
        return result;
    }

    @Override
    public PublishResult publish(PublishRequest publishRequest) {
        PublishResult result = snsClient.publish(publishRequest);
        logger.debug("public result " + result);
        return result;
    }
}
