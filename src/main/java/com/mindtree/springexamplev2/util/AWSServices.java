package com.mindtree.springexamplev2.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.amazonaws.AmazonClientException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.TableCollection;
import com.amazonaws.services.dynamodbv2.model.ListTablesResult;
import com.amazonaws.services.dynamodbv2.model.TableDescription;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.ec2.model.transform.InstanceNetworkInterfaceAttachmentStaxUnmarshaller;
import com.amazonaws.services.ecs.AmazonECS;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sqs.AmazonSQSClient;

public class AWSServices {
	static AmazonEC2 ec2;
	AmazonS3 s3;
	static AmazonSQSClient client;
	static AmazonDynamoDBClient dynamo;
	AmazonECS ecs;
	static AmazonSNSClient snsClient;
	static AWSCredentials credentials = null;
	static ClientConfiguration clientConfig = null;

	private static void init() {

		clientConfig = new ClientConfiguration();

		clientConfig.setProxyHost("172.22.218.218");
		clientConfig.setProxyPort(8085);
		clientConfig.withProtocol(Protocol.HTTP);

		try {
			credentials = new ProfileCredentialsProvider("default").getCredentials();
		} catch (Exception e) {
			throw new AmazonClientException("Cannot load the credentials from the credential profiles file. "
					+ "Please make sure that your credentials file is at the correct "
					+ "location (C:\\Users\\M1036004\\.aws\\credentials), and is in valid format.", e);
		}

	}

	/*
	 * public void showData(String service) {
	 * 
	 * switch (service) { case "AWS/EC2": { getInstances();
	 * 
	 * break; } case "AWS/SNS": { break; } case "AWS/SQS": { break; } case
	 * "AWS/S3": { break; } case "AWS/DynamoDB": { break; } case "AWS/ECS": {
	 * break; } }
	 * 
	 * }
	 */
	public static List<String> getInstances() {
		init();
		ec2 = new AmazonEC2Client(credentials, clientConfig);
		System.out.println("in aws");
		ec2.setRegion(Region.getRegion(Regions.AP_SOUTHEAST_1));

		/*
		 * System.out.println(credentials); System.out.println(clientConfig);
		 */
		List<String> instanceList = new ArrayList<String>();
		System.out.println("imn awwww");
		DescribeInstancesResult describeInstancesRequest = ec2.describeInstances();
		System.out.println("imn awwwwsadw");
		// DescribeInstancesRequest djfj = ec2.descrin
		System.out.println(describeInstancesRequest);
		List<Reservation> reservations = describeInstancesRequest.getReservations();
		System.out.println("imn awwwwsds");
		List<Instance> instances = new ArrayList<Instance>();
		for (Reservation reservation : reservations) {
			instances.addAll(reservation.getInstances());
			System.out.println(instances.get(0).getInstanceId());
			System.out.println(instances.get(0).getState());
			if (instances.get(0).getState().getName().equals("stopped")) {
				System.out.println("dfj");
			}
		}
		for (Instance insss : instances) {
			if (!insss.getState().getName().equals("stopped") && !insss.getState().getName().equals("terminated") ) {
				instanceList.add(insss.getInstanceId());
				System.out.println(instanceList+"instance list running");
			}
		}

		return instanceList;

	}

	public List<String>  getTables() {
		init();
		System.out.println("dvjkdhsj");
		dynamo = new AmazonDynamoDBClient(credentials, clientConfig);
		dynamo.setRegion(Region.getRegion(Regions.AP_SOUTHEAST_1));
		System.out.println(credentials);
		ListTablesResult tables = dynamo.listTables();
		System.out.println(dynamo + "dsgfdj");

		System.out.println("Listing table names");

		List<String> tableList = tables.getTableNames();
		System.out.println(tableList.size());
		for (String list : tableList) {
			System.out.println(list);
		}
		return tableList;
	}
	
	public static  void getTopic(){
		init();
		snsClient = new AmazonSNSClient(credentials, clientConfig);
		snsClient.setRegion(Region.getRegion(Regions.AP_SOUTHEAST_1));
		snsClient.listTopics();
		System.out.println(snsClient.listTopics().getTopics().get(0).getTopicArn());
		
	}
	
	
	public List<String> getBucket(){
		init();
		List<String> bucketList = new ArrayList<String>();
		s3 = new AmazonS3Client(credentials, clientConfig);
		s3.setRegion(Region.getRegion(Regions.AP_SOUTHEAST_1));
		 for (Bucket bucket : s3.listBuckets()) {
             System.out.println(" - " + bucket.getName());
             bucketList.add( bucket.getName());
         }
		return bucketList;
	}
	
	public static void getQueue(){
		init();
		client = new AmazonSQSClient(credentials);
		client.setRegion(Region.getRegion(Regions.AP_SOUTHEAST_1));
		 for (String queueUrl : client.listQueues().getQueueUrls()) {
             System.out.println("  QueueUrl: " + queueUrl);
         }
	}
	
	/*
	 * List<TableDescription> tableList = new ArrayList<TableDescription>(); for
	 * (String tableName : list) { Table tb = new Table(dynamo, tableName);
	 * TableDescription tableDescription = tb.describe();
	 * tableList.add(tableDescription); } return tableList;
	 * 
	 * }
	 */

	public static void main(String ar[]) {
		getTopic();

	}
}
