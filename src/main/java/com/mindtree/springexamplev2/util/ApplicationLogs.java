package com.mindtree.springexamplev2.util;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClient;
import com.amazonaws.services.securitytoken.model.AssumeRoleRequest;
import com.amazonaws.services.securitytoken.model.AssumeRoleResult;
import com.amazonaws.services.securitytoken.model.Credentials;

public class ApplicationLogs {
	static AmazonS3 s3 ;
	String bucketName = "bucket-created-for-spring-application";
	private  void init(){
		/*ClientConfiguration clientConfig = new ClientConfiguration();

		clientConfig.setProxyHost("172.22.218.218");
		clientConfig.setProxyPort(8085);
		clientConfig.withProtocol(Protocol.HTTP);*/
		AWSCredentials credentials = null;
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

		s3 = new AmazonS3Client(sessionCredentials);
	}
	
	
	public String   uploadFile(){
		init();
		String etag=null;

		
	
		String key = "ApplicationLogs";
		//String fileName = "D:/eclipse-jee-mars-R-win32-x86_64/eclipse/MCEJavaNew.docx";
		System.out.println("Uploading object in a bucket through program......................");
		System.out.println("Bucket name is :" + bucketName);
		System.out.println("Key of bucket is :" + key);
		File file =  new File("/var/log/tomcat8/catalina.out");
		//System.out.println("file name is :" + fileName);
		System.out.println("value of file :"+file.getAbsolutePath()); 
		
		try {
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentLength(0);
	      
			PutObjectResult result = s3.putObject(new PutObjectRequest(bucketName,key,file));
			
			System.out.println(result.getETag());
			etag=result.getETag();
		} catch (AmazonServiceException ex) {
			System.out.println("Error Message:    " + ex.getMessage());
			System.out.println("Error Message:    " + ex.getMessage());
			System.out.println("HTTP Status Code: " + ex.getStatusCode());
			System.out.println("AWS Error Code:   " + ex.getErrorCode());
			System.out.println("Error Type:       " + ex.getErrorType());
			System.out.println("Request ID:       " + ex.getRequestId());
		}
	
		return etag ;
		
		
		
}
}
