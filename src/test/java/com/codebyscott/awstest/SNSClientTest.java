package com.codebyscott.awstest;

import com.amazonaws.services.sns.model.CreateTopicResult;
import com.amazonaws.services.sns.model.PublishResult;
import com.amazonaws.services.sns.model.SubscribeResult;
import com.codebyscott.awstest.wrappers.AmazonSNSClientWrap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by bradleyschwab on 4/5/17.
 */

@RunWith(MockitoJUnitRunner.class)
public class SNSClientTest {
    @Mock
    private AmazonSNSClientWrap wrap;

    @Test
    public void sendMessageTest() {
        SNSClient snsClient = new SNSClient(wrap);
        when(wrap.createTopic(anyString())).thenReturn(new CreateTopicResult().withTopicArn("foobar"));
        when(wrap.subscribe(anyString(),anyString(),anyString())).thenReturn(new SubscribeResult().withSubscriptionArn("foobar2"));
        when(wrap.publish(any())).thenReturn(new PublishResult().withMessageId("dogcat"));
        snsClient.sendMessage("hello",2);
    }
}
