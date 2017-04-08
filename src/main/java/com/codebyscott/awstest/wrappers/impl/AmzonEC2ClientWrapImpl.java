package com.codebyscott.awstest.wrappers.impl;

import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.model.*;
import com.codebyscott.awstest.wrappers.AmazonEC2ClientWrap;
import org.apache.log4j.Logger;



/**
 * Created by bradleyschwab on 4/2/17.
 */
public class AmzonEC2ClientWrapImpl implements AmazonEC2ClientWrap {
    Logger logger = Logger.getLogger(AmzonEC2ClientWrapImpl.class);

    private AmazonEC2 ec2Client;


    public AmzonEC2ClientWrapImpl(AmazonEC2 ec2Client) {
        logger.debug("Starting client wrapper");
        this.ec2Client = ec2Client;
    }
    @Override
    public CreateKeyPairResult createKeyPair(CreateKeyPairRequest createKeyPairRequest) {
        CreateKeyPairResult result = ec2Client.createKeyPair(createKeyPairRequest);
        logger.debug("wrap createKeyPair request " + createKeyPairRequest + " result " + result);
        return result;
    }
    @Override
    public CreateSubnetResult createSubnet(CreateSubnetRequest createSubnetRequest) {
        CreateSubnetResult result = ec2Client.createSubnet(createSubnetRequest);
        logger.debug("wrap createSubnet request " + createSubnetRequest + " result " + result);
        return result;
    }
    @Override
    public CreateVpcResult createVpc(CreateVpcRequest createVpcRequest) {
        CreateVpcResult result = ec2Client.createVpc(createVpcRequest);
        logger.debug("wrap createVpc request" + createVpcRequest + " result " + result);
        return result;
    }
    @Override
    public DescribeSecurityGroupsResult describeSecurityGroups(DescribeSecurityGroupsRequest describeSecurityGroupsRequest) {
        DescribeSecurityGroupsResult result = ec2Client.describeSecurityGroups(describeSecurityGroupsRequest);
        logger.debug("wrap describeSecurityGroups " + describeSecurityGroupsRequest + " result " + result);
        return result;
    }
    @Override
    public CreateSecurityGroupResult createSecurityGroup(CreateSecurityGroupRequest createSecurityGroupRequest) {
        CreateSecurityGroupResult result = ec2Client.createSecurityGroup(createSecurityGroupRequest);
        logger.debug("wrap createSecurityGroup " + createSecurityGroupRequest + " results " + result);
        return result;
    }
    @Override
    public AuthorizeSecurityGroupIngressResult authorizeSecurityGroupIngress(AuthorizeSecurityGroupIngressRequest authorizeSecurityGroupIngressRequest)  throws AmazonEC2Exception {
        AuthorizeSecurityGroupIngressResult result = ec2Client.authorizeSecurityGroupIngress(authorizeSecurityGroupIngressRequest);
        logger.debug("wrap authorizeSecurityGroupIngress " + authorizeSecurityGroupIngressRequest + " results " + result);
        return result;
    }
    @Override
    public CreateTagsResult createTags(CreateTagsRequest createTagsRequest) {
        CreateTagsResult result = ec2Client.createTags(createTagsRequest);
        logger.debug("wrap createTags " + createTagsRequest + " results " + result);
        return result;
    }
    @Override
    public RunInstancesResult runInstances(RunInstancesRequest runInstancesRequest) {
        RunInstancesResult result = ec2Client.runInstances(runInstancesRequest);
        logger.debug("wrap runInstance " + " result " + result);
        return result;
    }
    @Override
    public CreateInternetGatewayResult createInternetGateway(CreateInternetGatewayRequest  createInternetGatewayRequest) {
        CreateInternetGatewayResult result = ec2Client.createInternetGateway(createInternetGatewayRequest);
        logger.debug("wrap createInternetGatewayRequest " + createInternetGatewayRequest + " result " + result);
        return result;
    }
    @Override
    public CreateRouteResult createRoute(String internetGatewayId, String instanceId) {
        CreateRouteRequest createRouteRequest = new CreateRouteRequest().withDestinationCidrBlock("0.0.0.0/0")
                .withGatewayId(internetGatewayId).withInstanceId(instanceId);
        CreateRouteResult result = ec2Client.createRoute(createRouteRequest);
        logger.debug("Created route " + result);
        return result;
    }

}
