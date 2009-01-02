<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page isELEnabled="true" %>
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
	<form action="SendMail" method="POST">
		<div style="position: absolute; width: 570px; height: 470px; background-color: transparent; border: 0px solid #000; top: 15px; left: 15px;">
		To:<br/>
		<input type="text" name="mailTo" value="${mailTo}" style="border: 1px solid #0F0F0F; height: 16px; width: 570px;"><br/>
		Subject:<br/>
		<input type="text" name="mailSubject" value="${mailSubject}" style="border: 1px solid #0F0F0F; height: 16px; width: 570px;"><br/>
		<br/>
		<textarea  name="mailText" style="border: 1px solid #0F0F0F; height: 325px; width: 570px;">${mailText}</textarea>
		${mailResult}
		<input type="submit" value="Send" style="position: absolute; right: -2px; bottom: 5px; border: 1px solid #0F0F0F;"/>
		</div>
	</form>
</div>
</body>
</html>