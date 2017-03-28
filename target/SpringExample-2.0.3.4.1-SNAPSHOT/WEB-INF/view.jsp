<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body style="background-color:lightblue">

		<img src="mindtree.png" alt="mindtree logo" />
	
	<h1>${error }</h1>
	<h1>${message }</h1>
	<a href="showDrop.do"> Admin Logs By Using Cloud Watch </a>
	<a href="showSplunk.do"></a>
	<table border="1" align="center">
		<tr>
		
			<td>Name</td>
			<td>LastName</td>
			<td>Email</td>
			<td>Id of Your S3 Object</td>
			
		</tr>
	
			<c:forEach items="${datalist }" var="list">
			<tr>
			
				<td>${list.name}</td>
				<td>${list.lastName}</td>
				<td>${list.email}</td>
			   <td>${list.getUniqueIdentifer()}</td>  
				<td><button>
						<a href="delete.do?email=${ list.email}&name=${list.name}&key=${list.getUniqueIdentifer()}">Delete</a>
					</button></td>
					<br>
					</tr>
			</c:forEach>

	
		
	</table>
	<br>
	<br>
	<a href="index.jsp"><input type="button" value="Back to home"> </a>
</body>
</html>