package com.codebyscott.awstest.wrappers;

import com.amazonaws.services.ec2.model.*;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.BucketVersioningConfiguration;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.SetBucketVersioningConfigurationRequest;

import java.util.List;

/**
 * Created by bradleyschwab on 4/2/17.
 */
public interface AmazonEC2ClientWrap {

    public CreateKeyPairResult createKeyPair(CreateKeyPairRequest createKeyPairRequest);
    public CreateSubnetResult createSubnet(CreateSubnetRequest createSubnetRequest);
    public CreateVpcResult createVpc(CreateVpcRequest createVpcRequest);
    public DescribeSecurityGroupsResult describeSecurityGroups(DescribeSecurityGroupsRequest describeSecurityGroupsRequest);
    public CreateSecurityGroupResult createSecurityGroup(CreateSecurityGroupRequest createSecurityGroupRequest);
    public AuthorizeSecurityGroupIngressResult authorizeSecurityGroupIngress(AuthorizeSecurityGroupIngressRequest authorizeSecurityGroupIngressRequest);
    public CreateTagsResult createTags(CreateTagsRequest createTagsRequest);
    public RunInstancesResult runInstances(RunInstancesRequest runInstancesRequest);

    public CreateInternetGatewayResult createInternetGateway(CreateInternetGatewayRequest  createInternetGatewayRequest);
    public CreateRouteResult createRoute(String internetGatewayId, String instanceId);

}
