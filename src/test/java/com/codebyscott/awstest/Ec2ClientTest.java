package com.codebyscott.awstest;

import com.amazonaws.services.ec2.model.*;

import com.codebyscott.awstest.wrappers.AmazonEC2ClientWrap;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Unit test for simple App.


*/

@RunWith(MockitoJUnitRunner.class)
public class Ec2ClientTest {

    @Mock
    AmazonEC2ClientWrap wrap;


    @Test
    public void testCreateEC2() {
        Ec2Client ec2Client = new Ec2Client(wrap);

        KeyPair kp = new KeyPair().withKeyName("key2").withKeyMaterial("test data");
        CreateKeyPairResult ckpr = new CreateKeyPairResult().withKeyPair(kp);
        when(wrap.createKeyPair(any())).thenReturn(ckpr);

        Vpc vpc = new Vpc().withVpcId("v001");
        CreateVpcResult createVpcResult = new CreateVpcResult().withVpc(vpc);
        when(wrap.createVpc(any())).thenReturn(createVpcResult);

        Subnet s = new Subnet().withVpcId("v001").withSubnetId("s001");
        CreateSubnetResult t = new CreateSubnetResult().withSubnet(s);
        when(wrap.createSubnet(any())).thenReturn(t);

        AmazonEC2Exception ae = new AmazonEC2Exception("one");
        ae.setErrorCode("InvalidGroup.NotFound");
        when(wrap.describeSecurityGroups(any())).thenThrow(ae);

        CreateSecurityGroupResult csgr = new CreateSecurityGroupResult().withGroupId("002");
        when(wrap.createSecurityGroup(any())).thenReturn(csgr);

        when(wrap.authorizeSecurityGroupIngress(any())).thenReturn(new AuthorizeSecurityGroupIngressResult());

        when(wrap.createTags(any())).thenReturn(new CreateTagsResult());
        try {
            ec2Client.createEC2("sg1", "key2","10.0.1.0/16","10.0.1.0/24");
        } catch (IOException ioe) {
            fail("IOException was thrown " + ioe.toString());
        }
    }

    @Test
    public void newKeyTest() {
        Ec2Client ec2Client = new Ec2Client(wrap);
        KeyPair kp = new KeyPair().withKeyName("newK").withKeyMaterial("test data");
        CreateKeyPairResult ckpr = new CreateKeyPairResult().withKeyPair(kp);
        when(wrap.createKeyPair(any())).thenReturn(ckpr);
        try {
            String k = ec2Client.createKey("newK");
            assertEquals("New key test","newK",k);
        } catch (IOException ioe) {
            fail("IOException was thrown " + ioe.toString());
        }
    }


    @Test
    public void duplicateKeyTest() {
        Ec2Client ec2Client = new Ec2Client(wrap);
        AmazonEC2Exception ae = new AmazonEC2Exception("one");
        ae.setErrorCode("InvalidKeyPair.Duplicate");
        when(wrap.createKeyPair(any())).thenThrow(ae);
        try {
            String k = ec2Client.createKey("testK");
            assertEquals("Duplicate key test","testK",k);
        } catch (IOException ioe) {
            fail("IOException was thrown " + ioe.toString());
        }
    }

    @Test(expected = AmazonEC2Exception.class)
    public void failKeyTest() {
        Ec2Client ec2Client = new Ec2Client(wrap);
        AmazonEC2Exception ae = new AmazonEC2Exception("one");
        ae.setErrorCode("not handled");
        when(wrap.createKeyPair(any())).thenThrow(ae);
        try {
            String k = ec2Client.createKey("testK");
        } catch (IOException ioe) {
            fail("IOException was thrown " + ioe.toString());
        }
    }


    @Test
    public void createSubnetTest() {
        Ec2Client ec2Client = new Ec2Client(wrap);

        Subnet s = new Subnet().withVpcId("v001").withSubnetId("s001");
        CreateSubnetResult t = new CreateSubnetResult().withSubnet(s);
        when(wrap.createSubnet(any())).thenReturn(t);
        String out = ec2Client.createSubnet("foo","0.0.0.0/0");
        assertEquals("testing creating subnet","s001",out);
    }

    @Test
    public void createVpcTest() {
        Ec2Client ec2Client = new Ec2Client(wrap);
        Vpc vpc = new Vpc().withVpcId("v001");
        CreateVpcResult createVpcResult = new CreateVpcResult().withVpc(vpc);
        when(wrap.createVpc(any())).thenReturn(createVpcResult);
        String out = ec2Client.createVpc("0.0.0.0/0");
        assertEquals("testing creating vpc","v001",out);
    }



    @Test
    public void createSecurityGroupExistingTest() {
        Ec2Client ec2Client = new Ec2Client(wrap);
        SecurityGroup sg = new SecurityGroup().withGroupId("001").withGroupName("foo");
        DescribeSecurityGroupsResult dsg = new DescribeSecurityGroupsResult();
        dsg.setSecurityGroups(Arrays.asList(sg));

        when(wrap.describeSecurityGroups(any())).thenReturn(dsg);

        String id = ec2Client.createSecurityGroup("vpcId", "groupName", "groupComment");
        assertEquals("testing subnet existing path","001",id);
    }

    @Test
    public void createSecurityGroupNewTest() {
        Ec2Client ec2Client = new Ec2Client(wrap);
        AmazonEC2Exception ae = new AmazonEC2Exception("one");
        ae.setErrorCode("InvalidGroup.NotFound");
        when(wrap.describeSecurityGroups(any())).thenThrow(ae);

        CreateSecurityGroupResult csgr = new CreateSecurityGroupResult().withGroupId("002");
        when(wrap.createSecurityGroup(any())).thenReturn(csgr);
        String id = ec2Client.createSecurityGroup("vpcId", "groupName", "groupComment");
        assertEquals("testing subnet new path","002",id);
    }

    @Test(expected = AmazonEC2Exception.class)
    public void createSecurityGroupFail() {
        Ec2Client ec2Client = new Ec2Client(wrap);
        AmazonEC2Exception ae = new AmazonEC2Exception("one");
        ae.setErrorCode("very bad");
        when(wrap.describeSecurityGroups(any())).thenThrow(ae);

        String id = ec2Client.createSecurityGroup("vpcId", "groupName", "groupComment");

    }

    @Test(expected = AmazonEC2Exception.class)
    public void createSecurityGroupFail2() {
        Ec2Client ec2Client = new Ec2Client(wrap);
        AmazonEC2Exception ae = new AmazonEC2Exception("one");
        ae.setErrorCode("InvalidGroup.NotFound");
        when(wrap.describeSecurityGroups(any())).thenThrow(ae);

        AmazonEC2Exception ae2 = new AmazonEC2Exception("two");
        ae2.setErrorCode("very bad again");

        when(wrap.createSecurityGroup(any())).thenThrow(ae2);
        String id = ec2Client.createSecurityGroup("vpcId", "groupName", "groupComment");
    }

    @Test
    public void authorizationTest() {
        Ec2Client ec2Client = new Ec2Client(wrap);
        when(wrap.authorizeSecurityGroupIngress(any())).thenReturn(new AuthorizeSecurityGroupIngressResult());
        ec2Client.createAuthorization("foobar");
    }

    @Test(expected = AmazonEC2Exception.class)
    public void authorizationTestFail() {
        Ec2Client ec2Client = new Ec2Client(wrap);
        AmazonEC2Exception ae = new AmazonEC2Exception("one");
        ae.setErrorCode("two");
        when(wrap.authorizeSecurityGroupIngress(any())).thenThrow(ae);
        ec2Client.createAuthorization("foobar");
    }

    @Test
    public void authorizationTestDuplicate() {
        Ec2Client ec2Client = new Ec2Client(wrap);
        AmazonEC2Exception ae = new AmazonEC2Exception("one");
        ae.setErrorCode("InvalidPermission.Duplicate");
        when(wrap.authorizeSecurityGroupIngress(any())).thenThrow(ae);
        ec2Client.createAuthorization("foobar");
    }

    @Test
    public void tagTest() {
        Ec2Client ec2Client = new Ec2Client(wrap);
        when(wrap.createTags(any())).thenReturn(new CreateTagsResult());
        CreateTagsResult r = ec2Client.createTags(Arrays.asList("foo","bar"),"scott","schwab");
        assertNotNull("empty but not null",r);
    }
}
