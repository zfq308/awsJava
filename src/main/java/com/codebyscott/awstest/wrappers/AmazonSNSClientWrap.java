package com.codebyscott.awstest.wrappers;

import com.amazonaws.services.sns.model.CreateTopicResult;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.amazonaws.services.sns.model.SubscribeResult;
import com.amazonaws.services.sqs.model.*;

/**
 * Created by bradleyschwab on 4/5/17.
 */
public interface AmazonSNSClientWrap {
    public CreateTopicResult createTopic(String testTopic);

    public SubscribeResult subscribe(String topic, String method, String endpoint);

    public PublishResult publish(PublishRequest publishRequest);

}
