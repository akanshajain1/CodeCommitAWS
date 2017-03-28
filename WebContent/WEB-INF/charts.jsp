<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
 <html>
  <head>
 
  </head>
  <body style="background-color:lightblue">
 
		<img src="mindtree.png" alt="mindtree logo" />

    <h3>${metric}</h3>
    <h1>${dimensions}</h1>
   <table border="1">
		<tr>
		
			<td>Date , Time </td>
			<td>Average Value</td>
			<td>Maximum Value</td>
			
			
		</tr>
	
			<c:forEach items="${datapintlist}" var="list">
			<tr>
			
				<td>${list.getTimestamp()}</td>
				<td>${list.getAverage()}</td>
				<td>${list.getMaximum()}</td>
			
					</tr>
			</c:forEach>

	
		
	</table>
	<a href="index.jsp">Back To Home</a>
  </body>
</html>