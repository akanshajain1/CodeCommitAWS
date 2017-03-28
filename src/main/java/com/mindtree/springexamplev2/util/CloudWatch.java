package com.mindtree.springexamplev2.util;

import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import com.amazonaws.AmazonClientException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClient;
import com.amazonaws.services.cloudwatch.model.Datapoint;
import com.amazonaws.services.cloudwatch.model.Dimension;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsRequest;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsResult;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClient;
import com.amazonaws.services.securitytoken.model.AssumeRoleRequest;
import com.amazonaws.services.securitytoken.model.AssumeRoleResult;
import com.amazonaws.services.securitytoken.model.Credentials;

public class CloudWatch {

	public  List<Datapoint> showCharts(String metric , String dimension , String metricname , String metricvalue){
		List<Datapoint> dataPoints = null;
		final  String instanceId ="i-572b00d9";
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
BasicAWSCredentials aa = new BasicAWSCredentials("", secretKey)
			sessionCredentials = new BasicSessionCredentials(stsCredentials.getAccessKeyId(),
					stsCredentials.getSecretAccessKey(),stsCredentials.getSessionToken());
			// s3 = new AmazonS3Client(sessionCredentials);
			System.out.println("access key" + stsCredentials.getAccessKeyId());
			// credentials = new
			// ProfileCredentialsProvider("default").getCredentials();
		} catch (Exception e) {
			throw new AmazonClientException(e.getMessage(), e);
		}
		 final AmazonCloudWatchClient client = new AmazonCloudWatchClient(sessionCredentials);
		 client.setEndpoint("http://monitoring.ap-southeast-1.amazonaws.com");
		 System.out.println("safsdjfdgv");
		 final GetMetricStatisticsRequest request = request(metric,dimension,metricname,metricvalue);
		 System.out.println("sddjfr");
	        final GetMetricStatisticsResult result = result(client, request);
	        return toStdOut(result, instanceId);   
	}
	private  GetMetricStatisticsRequest request(String metric , String dimension, String metricname, String metricvalue) {
        final long twentyFourHrs = 1000 * 60 * 60 * 24;
        final int oneHour = 60 * 60;
        System.out.println("in cloudwatch"); 
        
        return new GetMetricStatisticsRequest()
            .withStartTime(new Date(new Date().getTime()- twentyFourHrs))
            .withNamespace(metric)
            .withPeriod(oneHour)
            .withDimensions(new Dimension().withName(metricname).withValue(metricvalue))
            .withMetricName(dimension)
            .withStatistics("Average", "Maximum")
            .withEndTime(new Date());
    }

    private  GetMetricStatisticsResult result(
            final AmazonCloudWatchClient client, final GetMetricStatisticsRequest request) {
    	System.out.println("dfjkdshgkhnk");
         return client.getMetricStatistics(request);
    }

    private  List<Datapoint> toStdOut(final GetMetricStatisticsResult result, final String instanceId) {
        System.out.println(result); // outputs empty result: {Label: CPUUtilization,Datapoints: []}
        for (final Datapoint dataPoint : result.getDatapoints()) {
        	System.out.println();
            System.out.printf("%s instance's average CPU utilization : %s%n", instanceId, dataPoint.getAverage());   
            System.out.println(dataPoint.getTimestamp().getHours());
            System.out.printf("%s instance's max CPU utilization : %s%n", instanceId, dataPoint.getMaximum());
        }
		return result.getDatapoints();
    }
}
