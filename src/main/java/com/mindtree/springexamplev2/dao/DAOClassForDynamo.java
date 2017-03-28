package com.mindtree.springexamplev2.dao;

import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

import org.apache.poi.ss.formula.functions.Count;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.amazonaws.services.dynamodbv2.document.DeleteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.dynamodbv2.model.TableDescription;
import com.amazonaws.services.dynamodbv2.util.Tables;
import com.mindtree.springexamplev2.dto.DTO;
import com.mindtree.springexamplev2.entity.EntityClassForDynamo;
import com.mindtree.springexamplev2.exception.DAOException;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class DAOClassForDynamo {
	static AmazonDynamoDBClient dynamoDB;
	static String tableName = "newtablde1";
	private  void init() throws Exception {
		/*
		 * The ProfileCredentialsProvider will return your [default] credential
		 * profile by reading from the credentials file located at
		 * (~/.aws/credentials).
		 */
		AWSCredentials credentials = null;
		BasicAWSCredentials awsCreds =null;
		try {
			Properties prop = new Properties();
			InputStream input = null;
			//input = new FileInputStream("");
			input =getClass().getClassLoader().getResourceAsStream("credentialsAWS.properties");
			System.out.println("-----------------------"+input.hashCode());
			System.out.println("-----------------------"+input.hashCode());
			try{
			prop.load(input);
			// Create a Properties object to contain connection configuration
			// information.
			}
			catch(Exception exception){
				exception.printStackTrace();
			}
			System.out.println("size of prop"+prop.size());
			
			String SMTP_USERNAME = prop.getProperty("aws_access_key_id"); // Replace
																		// with
			// your SMTP
			// username.
			String SMTP_PASSWORD = prop.getProperty("aws_secret_access_key");
			 awsCreds = new BasicAWSCredentials(SMTP_USERNAME, SMTP_PASSWORD);
			
		} catch (Exception e) {
			throw new AmazonClientException("Cannot load the credentials from the credential profiles file. "
					+ "Please make sure that your credentials file is at the correct "
					+ "location (~/.aws/credentials), and is in valid format.", e);
		}
		dynamoDB = new AmazonDynamoDBClient(awsCreds);
		Region usWest2 = Region.getRegion(Regions.AP_SOUTHEAST_1);
		dynamoDB.setRegion(usWest2);
	}
	/*
	 * public boolean addData(DTO dtoData) throws DAOException { boolean result
	 * = false; try { Class.forName("cdata.jdbc.dynamodb.DynamoDBDriver");
	 * Connection conn = (Connection) DriverManager.getConnection(
	 * "jdbc:dynamodb:Access Key=AKIAIKYJXMNVVLJ5KI3Q;Secret Key=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;Domain=amazonaws.com;Region=SINGAPORE;"
	 * );
	 * 
	 * String query =
	 * "Insert into info(name,lastname,email,password) values(?,?,?,?)";
	 * PreparedStatement prepareStatement = conn.prepareStatement(query);
	 * prepareStatement.setString(1, dtoData.getName())1
	 * prepareStatement.setString(2, dtoData.getLastname());
	 * prepareStatement.setString(3, dtoData.getEmail());
	 * prepareStatement.setString(4, dtoData.getPassword());
	 * prepareStatement.executeQuery(); result = true;
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace(); } return result; }
	 */

	public  void createTable() throws Exception {
		init();

		
		try {
			// Create table if it does not exist yet
			if (Tables.doesTableExist(dynamoDB, tableName)) {

				System.out.println("Table " + tableName + " is already ACTIVE");
			} else {
				ArrayList<KeySchemaElement> keySchema = new ArrayList<KeySchemaElement>();
				keySchema.add(new KeySchemaElement().withAttributeName("email").withKeyType(KeyType.HASH));
				keySchema.add(new KeySchemaElement().withAttributeName("name").withKeyType(KeyType.RANGE));
				ArrayList<AttributeDefinition> attributeDefinitions = new ArrayList<AttributeDefinition>();
				attributeDefinitions.add(
						new AttributeDefinition().withAttributeName("email").withAttributeType(ScalarAttributeType.S));
				attributeDefinitions.add(
						new AttributeDefinition().withAttributeName("name").withAttributeType(ScalarAttributeType.S));
				/*attributeDefinitions.add(new AttributeDefinition().withAttributeName("lastname")
						.withAttributeType(ScalarAttributeType.S));
				attributeDefinitions.add(new AttributeDefinition().withAttributeName("password")
						.withAttributeType(ScalarAttributeType.S));*/
				ProvisionedThroughput provisionedThroughput = new ProvisionedThroughput().withReadCapacityUnits(1L)
						.withWriteCapacityUnits(1L);
				CreateTableRequest tableRequest = new CreateTableRequest(tableName, keySchema)
						.withAttributeDefinitions(attributeDefinitions)
						.withProvisionedThroughput(provisionedThroughput);
				TableDescription table = dynamoDB.createTable(tableRequest).getTableDescription();
				System.out.println("table description::s" + table);

				Tables.awaitTableToBecomeActive(dynamoDB, tableName);

			}
		} catch (Exception e) {

			throw new DAOException(e.getMessage(), e);
		}

	}

	/*public static boolean addData(String name, String lastName, String email, String password) throws DAOException {
		boolean result = false;
		try {
			System.out.println("table is creating.......");
			createTable();
			System.out.println("table is created....");
			EntityClassForDynamo entityClass = new EntityClassForDynamo(name, lastName, email, password);

			System.out.println("Starting the batch save...");
			DynamoDBMapper mapper = new DynamoDBMapper(dynamoDB);
			System.out.println(entityClass.getEmail());
			mapper.save(entityClass);
			 Item item = new Item(); 
			result = true;
			System.out.println("All customers have been saved to the DB");
		} catch (Exception e) {
			// throw new DAOException(e.getMessage(), e);
			e.printStackTrace();
		}

		return result;

	}*/

	public  boolean addItem(DTO dto , String etag) throws DAOException {
		boolean result=false;
		try{
			createTable();
			System.out.println("aaa");
		Table table = new Table(dynamoDB, tableName);
System.out.println("ddddd");
		Item item = new Item().withPrimaryKey("email", dto.getEmail(), "name", dto.getName()).withString("lastname", dto.getLastname())
				.withString("password", dto.getPassword()).withString("UniqueIdentifer", etag);
		table.putItem(item);
		result=true;
		}
		catch(Exception e){
			throw new DAOException(e.getMessage(),e);
		}
		return result;
		

	}

	public  List<EntityClassForDynamo> viewData() throws DAOException {
		try {
			createTable();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		PaginatedScanList<EntityClassForDynamo> entityClassForDynamo = null;
		try {
			System.out.println("********************");
			DynamoDBScanExpression expression = new DynamoDBScanExpression();
			DynamoDBMapper mapper = new DynamoDBMapper(dynamoDB);
			System.out.println(expression);
			entityClassForDynamo = mapper.scan(EntityClassForDynamo.class, expression);
			System.out.println("size::" + entityClassForDynamo.size());
			/*
			 * ScanRequest scanrequest = new ScanRequest("info"); ScanResult
			 * result = dynamoDB.scan(scanrequest);
			 * System.out.println("Result:::"+result);
			 */
			for (EntityClassForDynamo e : entityClassForDynamo) {
				System.out.println(e.getName() + "    email   " + e.getEmail() +  " Id is "+e.getUniqueIdentifer());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOException(e.getMessage(), e);
		}

		return entityClassForDynamo;

	}

	public  int count() throws DAOException {
		try {
			createTable();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DynamoDBMapper mapper = new DynamoDBMapper(dynamoDB);
		DynamoDBScanExpression dynamoDBScanExpression = new DynamoDBScanExpression();
		DynamoDBMapperConfig config = new DynamoDBMapperConfig(
				DynamoDBMapperConfig.PaginationLoadingStrategy.EAGER_LOADING);
		PaginatedScanList<EntityClassForDynamo> paginatedScanList = mapper.scan(EntityClassForDynamo.class,
				dynamoDBScanExpression, config);
		for (EntityClassForDynamo e : paginatedScanList) {
			System.out.println(e.getName() + "email" + e.getEmail());
		}
		System.out.println(paginatedScanList.get(1).getName() + "" + paginatedScanList.get(1).getEmail());
		paginatedScanList.loadAllResults();
		return paginatedScanList.size();
	}

	public  void queryTheObject(String email) {
		Table table = null;
		try {
			createTable();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map<String, Object> expressionAttributeValues = new HashMap<String, Object>();
		expressionAttributeValues.put(":x", "jain");

		table = new DynamoDB(dynamoDB).getTable("newtable");
		/*
		 * QuerySpec spec = new QuerySpec().withHashKey(" email",
		 * "akkujaingolu@gmail.com") .withFilterExpression(
		 * "newtable.lastname = :x").withValueMap(expressionAttributeValues)
		 * .withConsistentRead(true);
		 */
		// String email ="akkujaingolu@gmail.com";

		QuerySpec spec = new QuerySpec().withKeyConditionExpression("email = :v_id")
				.withValueMap(new ValueMap().withString(":v_id", email));
		ItemCollection<QueryOutcome> items = table.query(spec);

		Iterator<Item> iterator = items.iterator();
		while (iterator.hasNext()) {
			System.out.println(iterator.next().toJSONPretty());
		}
	}

	public  boolean deleteItem(String email , String name) throws DAOException {
		boolean result = false;
		try {
			init();
			Table table = new Table(dynamoDB, tableName);
			DeleteItemSpec deleteItem = new DeleteItemSpec().withPrimaryKey("email", email,"name",name)
					.withReturnValues(ReturnValue.ALL_OLD);
			;
			DeleteItemOutcome outcome = table.deleteItem(deleteItem);
			result = true;
			// Check the response.
			System.out.println("Printing item that was deleted...");

			System.out.println(outcome.getItem().toJSONPretty());
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOException(e.getMessage(), e);
		}
		return result;
	}

	public  void main(String ar[]) {

		try {
			Scanner scan = new Scanner(System.in);
			/*System.out.println("enter email id:");
			String email = scan.nextLine();
			System.out.println("enter name:");
			String name = scan.nextLine();
			System.out.println("enter last name:");
			String lastName = scan.nextLine();
			System.out.println("enter password:");
			String password = scan.nextLine();
		System.out.println(	addItem(name, lastName, email, password));*/
			/*queryTheObject(scan.nextLine());*/
			viewData();
			//System.out.println("Number of rows are :" + count());
			/*System.out.println("enter email id");
			
			deleteItem(scan.nextLine(),scan.nextLine());*/

		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
