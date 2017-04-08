package com.codebyscott.awstest;

import com.amazonaws.services.sqs.model.CreateQueueResult;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.codebyscott.awstest.wrappers.AmazonSQSClientWrap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by bradleyschwab on 4/6/17.
 */

@RunWith(MockitoJUnitRunner.class)
public class SQSClientTest {

    @Mock
    private AmazonSQSClientWrap wrap;

    @Test
    public void sendMessageQueueTest() {
        SQSClient sqsClient = new SQSClient(wrap);


        when(wrap.createQueue(anyString())).thenReturn(new CreateQueueResult().withQueueUrl("foobar"));

        when(wrap.sendMessage(any())).thenAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocationOnMock) throws Throwable {
                return null;
            }
        });

        List<Message> m = Arrays.asList(
                new Message().withReceiptHandle("done1").withBody("one"),
                new Message().withReceiptHandle("done2").withBody("two")
        );

        when(wrap.receiveMessage(any())).thenReturn(new ReceiveMessageResult().withMessages(m));

//        when(wrap.deleteMessage(any())).thenAnswer(new Answer<Void>() {
//            @Override
//            public Void answer(InvocationOnMock invocationOnMock) throws Throwable {
//                return null;
//            }
//        });

        when(wrap.deleteQueue(anyString())).thenAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocationOnMock) throws Throwable {
                return null;
            }
        });


        sqsClient.createMessageQueue();
    }

    @Test
    public void sendMessageTest() {
        SQSClient sqsClient = new SQSClient(wrap);
        when(wrap.sendMessage(any())).thenAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocationOnMock) throws Throwable {
                return null;
            }
        });
        sqsClient.sendMessage("a","b");
    }

    @Test
    public void recieveMessageTest() {
        SQSClient sqsClient = new SQSClient(wrap);
        List<Message> m = Arrays.asList(
                new Message().withReceiptHandle("done1").withBody("one"),
                new Message().withReceiptHandle("done2").withBody("two")
        );
        when(wrap.receiveMessage(any())).thenReturn(new ReceiveMessageResult().withMessages(m));
//        when(wrap.deleteMessage(any())).thenAnswer(new Answer<Void>() {
//            @Override
//            public Void answer(InvocationOnMock invocationOnMock) throws Throwable {
//                return null;
//            }
//        });
        sqsClient.receiveMessage("abc");
    }
}
