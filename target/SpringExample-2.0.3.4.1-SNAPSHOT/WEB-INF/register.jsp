<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script type="text/javascript">
	function validate() {
		var isvalid = true;
		alert(document.getElementById("name").value);
		var emailId = document.getElementById("email").value;
		alert(emailId);
		atpos = emailId.indexOf("@");
		dotpos = emailId.lastIndexOf(".");
		var firstpass = document.getElementById("password").value;
		var sceondpas = document.getElementById("confirmPassword").value;
		if (document.getElementById("name").value == "") {
			isvalid = false;
			alert("Please Provide your Name");

		}

		if (document.getElementById("lastname").value == "") {
			isvalid = false;
			alert("Please Provide your lastname");

		}

		if (document.getElementById("email").value == "") {
			isvalid = false;
			alert("Please Provide your email");

		}

		if (atpos < 1 || (dotpos - atpos < 2)) {
			isvalid = false;
			alert("Please enter  email ID in correct format");

		}
		if (document.getElementById("password").value == "") {
			isvalid = false;
			alert("Please Provide your password");

		}
		if (document.getElementById("confirmPassword").value == "") {
			isvalid = false;
			alert("Please Provide your confirmPassword ");
		}

		if (firstpass != sceondpas) {
			isvalid = false;
			alert("password must be same!");
			//isvalid = true;

		}
		return isvalid;
	}
	function fillList(){
		 
		 var val=document.getElementById("metrices").value;
		
		 document.getElementById("form1").submit();
	 }
</script>
</head>
<body style="background-color: lightblue">

	<img src="mindtree.png" alt="mindtree logo" />

	<h1>${message }</h1>
	<h2>${error }</h2>
	<div align="center">
		<form:form action="adddata.do" method="POST" commandName="adddata"
			onsubmit="return validate();" enctype="multipart/form-data">
			<form:label path="name"> Name </form:label>
			<form:input path="name" id="name"></form:input>

			<br>
			<br>
			<form:label path="lastname"> LastName </form:label>
			<form:input path="lastname" id="lastname"></form:input>

			<br>
			<br>
			<form:label path="email"> Email </form:label>
			<form:input path="email" id="email"></form:input>

			<br>
			<br>
			<form:label path="password"> Password </form:label>
			<form:input path="password" type="password" id="password"></form:input>

			<br>
			<br>
			<form:label path="confirmPassword"> Confirm Password </form:label>
			<form:input path="confirmPassword" type="password"
				id="confirmPassword"></form:input>

			<br>
			<br>
			
				File <input type="file" name="file" />
			
			<br>
			<br>

			<input type="submit" value="Register" />

			<a href="index.jsp"><input type="button" value="Back to home">
			</a>
			</input>
		</form:form>
	</div>
</body>
</html>