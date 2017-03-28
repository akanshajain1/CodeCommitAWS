<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<script type="text/javascript">
 function fillList(){
	 
	 var val=document.getElementById("metrices").value;
	 alert(val);
	 document.getElementById("form1").submit();
 }
 function fillmetric(){
	 document.getElementById("metrices").value= document.getElementById("hidder").value;
 }

</script>
<body style="background-color:lightblue" onload="fillmetric()">

		<img src="mindtree.png" alt="mindtree logo" />
	
	<div align="center">
	
<form name="form1" id="form1" action="showChart.do" method="POST" >
 Metrices of Cloud Watch:  <select id="metrices" name="metric" onchange="fillList()">
<option>-- Select Options-- </option>
<option value="AWS/DynamoDB">AWS/DynamoDB</option>
<option value="AWS/EC2">AWS/EC2</option>
<option value="AWS/ECS">AWS/ECS</option>
<option value="AWS/SNS">AWS/SNS</option>
<option value="AWS/SQS">AWS/SQS</option>
<option value="AWS/S3">AWS/S3</option>
</select>
</form>
<br>
<br>
<form action="showAllChart.do" method="POST">
<input type="hidden" name="hidder" id="hidder" value="<%=session.getAttribute("metric")%>"/>
MetricNames of a Metric  <select id="dimensions" name="dimensions">
<c:forEach items="${dimensionList }" var="list">
<option>${list}</option>
</c:forEach>
</select>
<br>
<br>
Dimensions <select id="metricname" name="metricname">
<c:forEach items="${metricList }" var="list">
<option>${list}</option>
</c:forEach>
</select>
<br>
<br>
MetricValue<input type="text" name="metricvalue"/>
<br>
<br>
<input type="submit" name="Submit" id="Submit" value="CloudWatch Data">

</form>
</div>
</body>
</html>