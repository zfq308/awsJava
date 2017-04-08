package com.codebyscott.awstest;

import com.amazonaws.services.ec2.model.*;
import com.codebyscott.awstest.wrappers.AmazonEC2ClientWrap;
import org.apache.log4j.Logger;


import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;


/**
 * Created by bradleyschwab on 4/1/17.
 */
public class Ec2Client {

    public static Logger logger = Logger.getLogger(Ec2Client.class);

    private AmazonEC2ClientWrap wrap;

    public Ec2Client(AmazonEC2ClientWrap wrap) {
        this.wrap = wrap;
    }

    public void createEC2(String securityGroupName, String keyPairName,
                          String vpcCidr, String subnetCidr)
            throws AmazonEC2Exception, IOException {



        String keyName = createKey(keyPairName);
        String vpcId = createVpc(vpcCidr);
        String subnetId  = createSubnet(vpcId, subnetCidr);


        String securityGroupId = createSecurityGroup(vpcId, securityGroupName, "from my test aws code");
        createAuthorization(securityGroupId);



        RunInstancesRequest runInstancesRequest = new RunInstancesRequest();
        runInstancesRequest
                .withImageId("ami-0b33d91d")
                .withInstanceType("t2.micro")
                .withMinCount(1)
                .withMaxCount(1)
                .withKeyName(keyName)
                .withSecurityGroupIds(securityGroupId)
                .withSubnetId(subnetId);

        RunInstancesResult result = wrap.runInstances(runInstancesRequest);


        List<String> itemsToTag = Arrays.asList(vpcId,subnetId,securityGroupId);
        createTags(itemsToTag,"group","schwabAPITest");
        logger.info(result);
    }


    /**
     * Create a key, if needed
     *
     * @param keyName
     * @return
     * @throws AmazonEC2Exception
     * @throws IOException
     */
    public String createKey(String keyName) throws AmazonEC2Exception, IOException {
        CreateKeyPairResult createKeyPairResult = null;
        CreateKeyPairRequest createKeyPairRequest = new CreateKeyPairRequest(keyName);
        try {
            createKeyPairResult = wrap.createKeyPair(createKeyPairRequest);
        } catch (AmazonEC2Exception ae) {
            if (ae.getErrorCode().contains("InvalidKeyPair.Duplicate")) {
                logger.info("key already exist");
                return keyName;
            } else {
                throw ae;
            }
        }
        try {
            Writer writer = new FileWriter(keyName + ".pem");
            writer.write(createKeyPairResult.getKeyPair().getKeyMaterial());
            writer.flush();
            writer.close();
        } catch (IOException ioe) {
            logger.warn("key could not be written " + keyName + ".pem", ioe);
            throw ioe;
        }
        return createKeyPairResult.getKeyPair().getKeyName();
    }


    String createSubnet(String vpcId, String cidr) {
        CreateSubnetRequest createSubnetRequest = new CreateSubnetRequest();
        createSubnetRequest.setCidrBlock(cidr);
        createSubnetRequest.setVpcId(vpcId);
        createSubnetRequest.setAvailabilityZone("us-east-1a");

        CreateSubnetResult createSubnetResult = wrap.createSubnet(createSubnetRequest);

        Subnet subnet = createSubnetResult.getSubnet();
        subnet.setMapPublicIpOnLaunch(true);
        logger.info("subnet: " + createSubnetRequest);
        return createSubnetResult.getSubnet().getSubnetId();

    }

    //    public InstanceNetworkInterfaceSpecification createAA(String subnetId) {
//        InstanceNetworkInterfaceSpecification instanceNetworkInterfaceSpecification = new InstanceNetworkInterfaceSpecification();
//        instanceNetworkInterfaceSpecification.setAssociatePublicIpAddress(true);
//        instanceNetworkInterfaceSpecification.setSubnetId(subnetId);
//        return instanceNetworkInterfaceSpecification;
//    }
//    String createInternetGatewaySubnet(String vpcId) {
//        CreateSubnetRequest createSubnetRequest = new CreateSubnetRequest();
//        createSubnetRequest.setCidrBlock("0.0.0.0/0");
//        createSubnetRequest.setVpcId(vpcId);
//        createSubnetRequest.setAvailabilityZone("us-east-1a");
//
//        CreateSubnetResult createSubnetResult = wrap.createSubnet(createSubnetRequest);
//        logger.info("subnet: " + createSubnetRequest);
//        return createSubnetResult.getSubnet().getSubnetId();
//
//    }
//
//    private String createInternetGateway() {
//        CreateInternetGatewayRequest createInternetGatewayRequest = new CreateInternetGatewayRequest();
//        CreateInternetGatewayResult createInternetGatewayResult = wrap.createInternetGateway(createInternetGatewayRequest);
//        return createInternetGatewayResult.getInternetGateway().getInternetGatewayId();
//    }

//    private NatGateway createNatGateway(String subnetId) {
//        CreateNatGatewayRequest createNatGatewayRequest = new CreateNatGatewayRequest().withSubnetId(subnetId);
//        CreateNatGatewayResult createNatGatewayResult = wrap.createInternetGateway(createInternetGatewayRequest);
//        return createNatGatewayResult.getNatGateway().
//    }


    public String createVpc(String cidr) {
        CreateVpcRequest createVpcRequest = new CreateVpcRequest();
        createVpcRequest.setCidrBlock(cidr);
        createVpcRequest.setInstanceTenancy("default");
        CreateVpcResult createVpcResult = wrap.createVpc(createVpcRequest);
        Vpc vpc = createVpcResult.getVpc();

        logger.info("vpc: " + createVpcRequest);
        return createVpcResult.getVpc().getVpcId();

    }

//    private RouteTable createRouteTable() {
//        CreateRouteRequest createRouteRequest = new CreateRouteRequest();
//        Route internet = new Route();
//        internet.setDestinationCidrBlock("0.0.0.0/0");
//        internet.setDestinationCidrBlock(createInternetGateway().getInternetGatewayId());
//        Route local = new Route();
//        local.setDestinationCidrBlock("10.0.0.0/16");
//
//    }
    private List<IpPermission> createIpPermissons() {

        String ipAdder = "0.0.0.0/0";  // should be restricted to my providers IP range
        IpRange ipRange = new IpRange();
        ipRange.setCidrIp(ipAdder);

        IpPermission sshIpPermission = new IpPermission();
        sshIpPermission.setIpProtocol("tcp");
        sshIpPermission.setFromPort(22);
        sshIpPermission.setToPort(22);
        sshIpPermission.setIpv4Ranges(Arrays.asList(ipRange));

        IpPermission httpIpPermission = new IpPermission();
        httpIpPermission.setIpProtocol("tcp");
        httpIpPermission.setFromPort(80);
        httpIpPermission.setToPort(80);
        httpIpPermission.setIpv4Ranges(Arrays.asList(ipRange));

        IpPermission httpsIpPermission = new IpPermission();
        httpsIpPermission.setIpProtocol("tcp");
        httpsIpPermission.setFromPort(443);
        httpsIpPermission.setToPort(443);
        httpsIpPermission.setIpv4Ranges(Arrays.asList(ipRange));

        IpPermission httpAltIpPermission = new IpPermission();
        httpAltIpPermission.setIpProtocol("tcp");
        httpAltIpPermission.setFromPort(8080);
        httpAltIpPermission.setToPort(8080);
        httpAltIpPermission.setIpv4Ranges(Arrays.asList(ipRange));

        return Arrays.asList(sshIpPermission, httpIpPermission, httpsIpPermission, httpAltIpPermission);
    }

    /**
     * Create a security group if needed, enable ssh connections, and tag
     *
     * @param groupName
     * @param groupComment
     * @return
     * @throws AmazonEC2Exception
     */
    public String createSecurityGroup(String vpcId, String groupName, String groupComment) throws AmazonEC2Exception {

        try {
            // see if group already exists
            DescribeSecurityGroupsResult describeSecurityGroupsResult =
                    wrap.describeSecurityGroups(new DescribeSecurityGroupsRequest().withGroupNames(groupName));
            return describeSecurityGroupsResult.getSecurityGroups().get(0).getGroupId();
        } catch (AmazonEC2Exception ae) {
            if (!ae.getErrorCode().contains("InvalidGroup.NotFound")) {
                throw ae;
            }
        }
        CreateSecurityGroupResult createSecurityGroupResult =
                wrap.createSecurityGroup(new CreateSecurityGroupRequest().withGroupName(groupName)
                        .withDescription(groupComment)
                        .withVpcId(vpcId));
        return createSecurityGroupResult.getGroupId();
    }

    public void createAuthorization(String securityGroupId) throws AmazonEC2Exception {
        try {
            logger.debug("security group = " + securityGroupId);
            AuthorizeSecurityGroupIngressRequest authorizeSecurityGroupIngressRequest =
                new AuthorizeSecurityGroupIngressRequest().withGroupId(securityGroupId).withIpPermissions(createIpPermissons());
            wrap.authorizeSecurityGroupIngress(authorizeSecurityGroupIngressRequest);
        } catch(AmazonEC2Exception ae2) {
            if (ae2.getErrorCode().contains("InvalidPermission.Duplicate")) {
                logger.info("security setting already exist");
            } else {
                throw ae2;
            }
        }
    }

    public CreateTagsResult createTags(List<String> itemsToTag, String key, String value) {
        Tag tag = new Tag(key,value);
        CreateTagsRequest tagsRequest = new CreateTagsRequest(itemsToTag,Arrays.asList(tag));
        return(wrap.createTags(tagsRequest));
    }
}





