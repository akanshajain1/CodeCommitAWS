package com.mindtree.springexamplev2.util;

import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClient;
import com.amazonaws.services.securitytoken.model.AssumeRoleRequest;
import com.amazonaws.services.securitytoken.model.AssumeRoleResult;
import com.amazonaws.services.securitytoken.model.Credentials;
import com.amazonaws.services.sns.AmazonSNSClient;

import java.io.InputStream;
import java.util.Properties;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.model.ConfirmSubscriptionRequest;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.CreateTopicResult;
import com.amazonaws.services.sns.model.SubscribeRequest;
import com.mindtree.springexamplev2.exception.ServiceException;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.amazonaws.services.sns.model.DeleteTopicRequest;

public class SNSClass {

	static AmazonSNSClient snsClient;

	/**
	 * The only information needed to create a client are security credentials
	 * consisting of the AWS Access Key ID and Secret Access Key. All other
	 * configuration, such as the service endpoints, are performed
	 * automatically. Client parameters, such as proxies, can be specified in an
	 * optional ClientConfiguration object when constructing a client.
	 *
	 * @see com.amazonaws.auth.BasicAWSCredentials
	 * @see com.amazonaws.auth.ProfilesConfigFile
	 * @see com.amazonaws.ClientConfiguration
	 */
	private void init() throws Exception {
		/*
		 * The ProfileCredentialsProvider will return your [default] credential
		 * profile by reading from the credentials file located at
		 * (~/.aws/credentials).
		 */
		BasicSessionCredentials sessionCredentials;
		try {
			AWSSecurityTokenServiceClient securityTokenService = new AWSSecurityTokenServiceClient();
			System.out.println(securityTokenService);
			//securityTokenService.setEndpoint("sts-ap-southeast-1.amazonaws.com");
			/*GetSessionTokenRequest session_token_request = new GetSessionTokenRequest();
			System.out.println(session_token_request);
			session_token_request.setDurationSeconds(7200);
			GetSessionTokenResult session_token_result =
					securityTokenService.getSessionToken(session_token_request);
			System.out.println(session_token_result);
			Credentials session_creds = session_token_result.getCredentials();
			System.out.println(session_creds.getAccessKeyId());*/
			System.out.println(securityTokenService);
			@SuppressWarnings("null")
			AssumeRoleRequest assumeRoleRequest = new AssumeRoleRequest()
					.withRoleArn("arn:aws:iam::593952467657:role/assumerole").withRoleSessionName("assumerolesession1");
			System.out.println(assumeRoleRequest+"dcs");
			/*credentials =	assumeRoleRequest.getRequestCredentials();
			System.out.println("cregc"+credentials.getAWSAccessKeyId()); */
			
			AssumeRoleResult assumeRoleResult = securityTokenService.assumeRole(assumeRoleRequest);
			System.out.println(assumeRoleResult);
			Credentials stsCredentials = assumeRoleResult.getCredentials();

			sessionCredentials = new BasicSessionCredentials(stsCredentials.getAccessKeyId(),
					stsCredentials.getSecretAccessKey(),stsCredentials.getSessionToken());
			// s3 = new AmazonS3Client(sessionCredentials);
			System.out.println("access key" + stsCredentials.getAccessKeyId());
			// credentials = new
			// ProfileCredentialsProvider("default").getCredentials();
		} catch (Exception e) {
			throw new AmazonClientException(e.getMessage(), e);
		}

		snsClient = new AmazonSNSClient(sessionCredentials);
		snsClient.setRegion(Region.getRegion(Regions.AP_SOUTHEAST_1));
	}

	public void sendSNS(String email) throws ServiceException {
		// create a new SNS client and set endpoint
		try {
			init();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			// create a new SNS topic
			CreateTopicRequest createTopicRequest = new CreateTopicRequest("MyNewTopic");
			CreateTopicResult createTopicResult = snsClient.createTopic(createTopicRequest);
			// print TopicArn
			System.out.println(createTopicResult);
			// get request id for CreateTopicRequest from SNS metadata
			System.out.println("CreateTopicRequest - " + snsClient.getCachedResponseMetadata(createTopicRequest));
			String topicArn = createTopicResult.getTopicArn();
			SubscribeRequest subRequest = new SubscribeRequest(topicArn, "email", email);
			snsClient.subscribe(subRequest);
			// get request id for SubscribeRequest from SNS metadata
			System.out.println("SubscribeRequest - " + snsClient.getCachedResponseMetadata(subRequest));
			System.out.println("Check your email and confirm subscription.");
			/*
			 * ConfirmSubscriptionRequest confirmReq = new
			 * ConfirmSubscriptionRequest().withTopicArn(topicArn).withToken(
			 * "message");
			 * 
			 * snsClient.confirmSubscription(confirmReq);
			 */
			String subject = "Email Regarding Successfully Registration";
			String msg = "This" + email + " is successfully Registrated  ";
			PublishRequest publishRequest = new PublishRequest(topicArn, msg, subject);

			PublishResult publishResult = snsClient.publish(publishRequest);

			// print MessageId of message published to SNS topic
			System.out.println("MessageId - " + publishResult.getMessageId());
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage(), e);

		}
	}
}