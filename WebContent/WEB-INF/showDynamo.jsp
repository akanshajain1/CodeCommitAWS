<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body style="background-color:lightblue">
<marquee>
		<img src="mindtree.png" alt="mindtree logo" />
	</marquee>
	<h1>${message }</h1>
	<table border="1">
		<tr>
		
			<td>Table Name</td>
			<td>CreationDate</td>
			<td>State</td>
			<!-- <td>Type</td> -->
			
		</tr>
	
			<c:forEach items="${dynamolist }" var="list">
			<tr>
			
				<td>${list.getTableName()}</td>
				<td>${list.getCreationDateTime()}</td>
				<td>${list.getTableStatus()}</td>
			  <%--  <td>${list.getInstanceType()}</td>  --%> 
				<td><button>
						<a href="delete.do?email=${ list.getTableName()}&name=${list.name}">ConditionalCheckFailedRequests</a><br>
						<a href="delete.do?email=${ list.getTableName()}&name=${list.name}">NetworkPacketsIn</a><br>
						<a href="delete.do?email=${ list.getTableName()}&name=${list.name}">ConsumedReadCapacityUnits</a><br>
						<a href="delete.do?email=${ list.getTableName()}&name=${list.name}">ConsumedReadCapacityUnits</a><br>
					</button></td>
					<br>
					</tr>
			</c:forEach>

	
		
	</table>
</body>
</html>