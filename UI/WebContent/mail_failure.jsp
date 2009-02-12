<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>DecidR Prototype</title>
</head>
<body style="background-color: #EAEAEA">
<div style="position: absolute; width: 600px; height: 50px; background-color: transparent; border: 0px solid #000; top: 25px; left: 50%; margin-left:-300px; text-align: center; font-size: 25px;">
DecidR Prototype
</div>
<div style="position: absolute; width: 600px; height: 500px; background-color: #FAFAFA; border: 1px solid #000; top: 75px; left: 50%; margin-left:-300px;">
	<div style="position: absolute; width: 570px; height: 250px; background-color: transparent; border: 0px solid #000; top: 125px; left: 15px; text-align: center;">
	Oops!<br/>
	an error occured while sending your mail:<br/>${mailResult}<br/><br>
	<a href="index.jsp">try again!</a>
	</div>
</div>
</body>
</html>