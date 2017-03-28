<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
	 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
	
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<% String dataList =(String) request.getAttribute("datapintlist"); %>
<script type="text/javascript"
	src="https://www.gstatic.com/charts/loader.js"></script>
<script type="text/javascript">
	google.charts.load('current', {
		'packages' : [ 'corechart' ]
	});
	google.charts.setOnLoadCallback(drawChart);
	function drawChart() {
		var dayData = google.visualization.arrayToDataTable(<%=dataList%>); 
		var options = {
			title : 'CloudWatch Metrices',
			hAxis : {
				title : 'Time',
				titleTextStyle : {
					color : '#333'
				}
			},
			vAxis : {
				minValue : 0
			}
		};

		var chart = new google.visualization.LineChart(document
				.getElementById('chart_div'));
		chart.draw(dayData, options);
	}
</script>
</head>
<body style="background-color:lightblue">
<img src="mindtree.png" alt="mindtree logo" />
<a href="index.jsp">Back To Home</a>
<h3>${metric}</h3>
    <h1>${dimensions}</h1>
	<div  id="chart_div" style="width: 900px; height: 500px; float:left"></div>
	<div style=" float:left;margin-left:30px"><table border="1" >
		<tr>
		
			<td>Date , Time </td>
			<td>Average Value</td>
			<td>Maximum Value</td>
			
			
		</tr>
	
			<c:forEach items="${dataPoints}" var="list">
			<tr>
			
				<td>${list.getTimestamp()}</td>
				<td>${list.getAverage()}</td>
				<td>${list.getMaximum()}</td>
			
					</tr>
			</c:forEach>

	
		
	</table></div>
</body>
</html>