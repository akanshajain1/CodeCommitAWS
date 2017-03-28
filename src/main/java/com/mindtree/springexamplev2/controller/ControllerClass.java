package com.mindtree.springexamplev2.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.*;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.amazonaws.services.cloudwatch.model.Datapoint;
import com.amazonaws.services.dynamodbv2.model.TableDescription;
import com.amazonaws.services.ec2.model.Instance;
import com.mindtree.springexamplev2.dto.DTO;
import com.mindtree.springexamplev2.entity.EntityClassForDynamo;
import com.mindtree.springexamplev2.exception.ServiceException;
import com.mindtree.springexamplev2.service.Service;
import com.mindtree.springexamplev2.util.AWSServices;
import com.mindtree.springexamplev2.util.ApplicationLogs;
import com.mindtree.springexamplev2.util.CloudWatch;
import com.mindtree.springexamplev2.util.Mail;
import com.mindtree.springexamplev2.util.S3Upload;
import com.mindtree.springexamplev2.util.SNSClass;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

@Controller
public class ControllerClass {

	private static final int MEMORY_THRESHOLD = 1024 * 1024 * 3; // 3MB
	private static final int MAX_FILE_SIZE = 1024 * 1024 * 40; // 40MB
	private static final int MAX_REQUEST_SIZE = 1024 * 1024 * 50; // 50MB
	Service service = new Service();
	S3Upload s3Upload = new S3Upload();
	CloudWatch cloudWatch = new CloudWatch();
	Mail mail = new Mail();
	SNSClass snsClass = new SNSClass();
	AWSServices awsServices = new AWSServices();
	String metricName = null;
	ApplicationLogs applicationLogs = new ApplicationLogs();
	private static final Logger LOGGER = Logger.getLogger(ControllerClass.class);

	@RequestMapping(value = "register.do")
	public ModelAndView showForm() {
		System.out.println("I am here*********************");
		return new ModelAndView("register", "adddata", new DTO());

	}

	@RequestMapping(value = "adddata.do", method = RequestMethod.POST)
	public ModelAndView addData(@ModelAttribute("adddata") DTO adddata, @RequestParam("file") MultipartFile file,
			ModelMap model, BindingResult errors, HttpServletRequest request) {

		System.out.println("I am here_____________________________________");
		System.out.println(adddata.getName());
		InputStream file1 = null;
		String etag = null;
		File newF = null;

		// DiskFileItemFactory factory = new DiskFileItemFactory();
		// factory.setSizeThreshold(MEMORY_THRESHOLD);
		//
		// ServletFileUpload upload = new ServletFileUpload(factory);
		// upload.setFileSizeMax(MAX_FILE_SIZE);
		// upload.setSizeMax(MAX_REQUEST_SIZE);
		//
		// String uuidValue = "";
		// FileItem itemFile = null;
		//
		// try {
		// // parses the request's content to extract file data
		// List formItems = upload.parseRequest(request);
		// Iterator iter = formItems.iterator();
		//
		// // iterates over form's fields to get UUID Value
		// while (iter.hasNext()) {
		// FileItem item = (FileItem) iter.next();
		//
		// if (!item.isFormField()) {
		// itemFile = item;
		// }
		// }
		// file1 = itemFile.getInputStream();
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

		/*
		
		*/
		InputStream inputStream = null;
		OutputStream outputStream = null;
		String filename = file.getOriginalFilename();

		File newFile = new File(filename);
		try {
			inputStream = file.getInputStream();

			if (!newFile.exists()) {
				newFile.createNewFile();
			}
			outputStream = new FileOutputStream(newFile);
			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(newFile.getAbsolutePath() + "aaaaaaa");

		// File item1= new File(filename);
		// File inputDir = new File("/NewFolder");
		// String UPLOAD_DIRECTORY = inputDir.getAbsolutePath();
		// File newFile = new File(UPLOAD_DIRECTORY);
		// if (!newFile.exists()) {
		// newFile.mkdirs();
		//
		//
		// }

		/*
		 * try { System.out.println("herer"+filename); inputStream =
		 * file.getInputStream();
		 * 
		 * File newFiledc = new File("C:/Users/files/" + filename);
		 * System.out.println("gvj"+newFiledc.getAbsolutePath()); outputStream =
		 * new FileOutputStream(newFiledc);
		 * System.out.println(outputStream+"egfrr"); int read = 0; byte[] bytes
		 * = new byte[1024];
		 * 
		 * while ((read = inputStream.read(bytes)) != -1) {
		 * outputStream.write(bytes, 0, read); } } catch (IOException e) { //
		 * TODO Auto-generated catch block e.printStackTrace(); }
		 */

		/*
		 * System.out.println("Uploadibng"+newFile.getPath());
		 * 
		 * 
		 * 
		 * try { System.out.println("Going to writ"+newFile.getPath());
		 * //System.out.println(item1.getContentType()); ((FileItem)
		 * file).write(new File(UPLOAD_DIRECTORY + File.separator + filename));
		 * } catch (Exception e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */

		// File fileForS3 = new File(filename);
		/*
		 * FileInputStream fileIn= null; try { fileIn = new
		 * FileInputStream(fileForS3); byte[] fileBytes =file.getBytes();
		 * fileIn.read(fileBytes); InputStream inputStream = new
		 * ByteArrayInputStream(fileBytes); fileIn = (FileInputStream)
		 * inputStream; fileIn.close(); } catch (IOException e1) { // TODO
		 * Auto-generated catch block e1.printStackTrace(); }
		 */
		// Creating//

		// System.out.println(newFile);
		/*
		 * try { item1. item1.write(new File(UPLOAD_DIRECTORY + File.separator +
		 * filename)); System.out.println(item1.getName()+" Written"); } catch
		 * (Exception e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */
		/*
		 * File fileForS3 = new File("D:/NewFolder");
		 * //System.out.println(newFile.getAbsolutePath());
		 * 
		 * System.out.println(fileForS3.exists() + " " +
		 * fileForS3.getAbsolutePath()); System.out.println("file name in c" +
		 * filename);
		 */
		applicationLogs.uploadFile();
		String keyName = adddata.getEmail() + adddata.getName();
		etag = s3Upload.uploadFile(newFile, keyName);
		System.out.println("Etag value is :::" + filename);

		System.out.println("nexct");
		try {
			// System.out.println(service.addData(dto));
			System.out.println("hhhh");
			if (service.addData(adddata, etag)) {
				try {
					snsClass.sendSNS(adddata.getEmail());

				} catch (Exception e) {

					e.printStackTrace();
				}
				request.setAttribute("message", "Successfully Registered");
				request.setAttribute("mail", "Confirmation Mail is send to this Id:" + adddata.getEmail());
			} else {
				request.setAttribute("message", "Not Registered Successfully ");
			}

		} catch (ServiceException e) {

			// System.out.println(e.getMessage());
			e.printStackTrace();
			request.setAttribute("error", e.getMessage());
			LOGGER.error(e.getMessage(), e);
		}
		return new ModelAndView("message");

	}

	@RequestMapping(value = "view.do")
	public ModelAndView viewData(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		try {
			
			List<EntityClassForDynamo> entityClass = service.viewData();
			System.out.println("in view method and " + entityClass.size());
			System.out.println(entityClass.get(1).getUniqueIdentifer());
			request.setAttribute("datalist", entityClass);
			System.out.println("after getting list");
			if (entityClass.size() <= 1) {
				System.out.println("in if part");
				request.setAttribute("message", "Nothing in DataBase for Displaying");
				mav.setViewName("message");
			} else {
				System.out.println("in else part");
				mav.setViewName("view");
			}
			applicationLogs.uploadFile();
			System.out.println("in try ");
		} catch (ServiceException e) {
			System.out.println("in catch block");
			request.setAttribute("error", e.getMessage());
			mav.setViewName("message");
			LOGGER.error(e.getMessage(), e);
		}
		return mav;

	}

	@RequestMapping(value = "delete.do")
	public ModelAndView deleteData(@RequestParam("email") String email, @RequestParam("name") String name,
			@RequestParam("key") String key, HttpServletRequest request) {
		System.out.println("oin delete method");
		try {
			if (service.deleteData(email, name)) {
				System.out.println("after delete");
				if (!key.equals(null)) {
					if (s3Upload.deleteObject(email + name)) {
						System.out.println("heeeloo");
						try {
							mail.sendMail(email);
						} catch (MessagingException e) {
							// TODO Auto-generated catch block
							request.setAttribute("error", "Some Error in Email Service");
							e.printStackTrace();
						}
						request.setAttribute("message", "deleted successfully");
					}
					applicationLogs.uploadFile();
				} else {
					System.out.println("hekllhds");
					try {
						mail.sendMail(email);
					} catch (MessagingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						request.setAttribute("error", "Some Error in Email Service");
					}
					request.setAttribute("message", "deleted successfully");

				}
			} else
				request.setAttribute("message", "not deleted successfully");

		} catch (ServiceException e) {
			request.setAttribute("error", e.getMessage());
			LOGGER.error(e.getMessage(), e);
		}
		return new ModelAndView("message");

	}

	@RequestMapping(value = "showDrop.do")
	public ModelAndView displayList() {
		return new ModelAndView("dropDownlist");

	}

	@RequestMapping(value = "showChart.do")
	public ModelAndView showChart(@RequestParam("metric") String metric, HttpServletRequest request,
			HttpSession session) {
		System.out.println("in heee");
		System.out.println(metric + "metric value");
		session.setAttribute("metric", metric);
		request.setAttribute("metric", metric);
		List<String> dimensionList = new ArrayList<String>();
		List<String> metricList = new ArrayList<String>();
		metricName = metric;
		
		if (metric.equalsIgnoreCase("AWS/EC2")) {

			dimensionList.add("CPUCreditUsage");
			dimensionList.add("CPUUtilization");
			dimensionList.add("NetworkIn");

			metricList.add("InstanceId");
			metricList.add("ImageId");
			request.setAttribute("dimensionList", dimensionList);
			request.setAttribute("metricList", metricList);
		}

		if (metric.equalsIgnoreCase("AWS/DynamoDB")) {

			dimensionList.add("ConditionalCheckFailedRequests");
			dimensionList.add("ConsumedReadCapacityUnits");
			dimensionList.add("ConsumedWriteCapacityUnits");

			metricList.add("TableName");

			request.setAttribute("dimensionList", dimensionList);
			request.setAttribute("metricList", metricList);
		}

		if (metric.equalsIgnoreCase("AWS/S3")) {

			dimensionList.add("BucketSizeBytes");
			dimensionList.add("NumberOfObjects");

			metricList.add("BucketName");
			metricList.add("StorageType");
			request.setAttribute("dimensionList", dimensionList);
			request.setAttribute("metricList", metricList);
		}

		if (metric.equalsIgnoreCase("AWS/ECS")) {

			dimensionList.add("CPUReservation");
			dimensionList.add("MemoryReservation");
			dimensionList.add("CPUUtilization");
			metricList.add("ClusterName");
			metricList.add("ServiceName");
			request.setAttribute("dimensionList", dimensionList);
			request.setAttribute("metricList", metricList);
		}

		if (metric.equalsIgnoreCase("AWS/SNS")) {

			dimensionList.add("PublishSize");
			dimensionList.add("NumberOfNotificationsDelivered");

			metricList.add("TopicName");
			metricList.add("SMSType");
			System.out.println(metricList.get(0));
			request.setAttribute("dimensionList", dimensionList);
			request.setAttribute("metricList", metricList);
		}

		if (metric.equalsIgnoreCase("AWS/SQS")) {
			dimensionList.add("NumberOfMessagesSent");
			dimensionList.add("ApproximateNumberOfMessagesDelayed");

			dimensionList.add("ApproximateNumberOfMessagesVisible");
			metricList.add("QueueName");

			request.setAttribute("dimensionList", dimensionList);
			request.setAttribute("metricList", metricList);
		}
		applicationLogs.uploadFile();
		return new ModelAndView("dropDownlist");

	}

	@RequestMapping("showAllChart.do")
	public ModelAndView showAllChart(@RequestParam("dimensions") String dimensions,
			@RequestParam("metricname") String metricname, @RequestParam("metricvalue") String metricvalue,
			HttpServletRequest request, HttpSession session) {
		System.out.println("heeee +");
		String metric = (String) request.getAttribute("metric");
		ModelAndView mav = new ModelAndView();
		List<Datapoint> dataPoints = cloudWatch.showCharts(metricName, dimensions, metricname, metricvalue);
		System.out.println(dataPoints.size());
		List<String> averageDataPoints = new ArrayList<String>();
		List<String> maximumDataPoints = new ArrayList<String>();

		for (Datapoint dp : dataPoints) {
			String listName;
			List<String> dataString = new ArrayList<String>();
			String graphValues=String.valueOf(dp.getTimestamp());
			dataString.add(0,"'"+graphValues+"'");
			dataString.add(1, dp.getAverage().toString());
			dataString.add(2, dp.getMaximum().toString());
			averageDataPoints.add(dataString.toString());
		}
		List<String> headerElement = new ArrayList<String>();
		headerElement.add("'Time'");
		headerElement.add("'Average'");
		headerElement.add("'Maximum'");
		averageDataPoints.add(0, headerElement.toString());

		if (dataPoints.size() == 0) {
			request.setAttribute("message", "Nothing to display in CloudWatch");

			mav.setViewName("message");
		} else {
			request.setAttribute("metric", metric);
			request.setAttribute("dimensions", dimensions);
			request.setAttribute("metricname", metricname);
			request.setAttribute("metricvalue", metricvalue);
			request.setAttribute("datapintlist", averageDataPoints.toString());
			request.setAttribute("dataPoints", dataPoints);
			System.out.println("vvvvvvvvvvvvvvv" + dataPoints.get(0).getMaximum());
			mav.setViewName("showData");
		}
		applicationLogs.uploadFile();
		System.out.println("size" + dataPoints.size());
		return mav;
	}

}
