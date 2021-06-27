<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="th" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <title>Login page</title>
</head>
<body>
<h2>Registration</h2>

<form th:th:action="@{/login}" method="post">
    <div><label> User Name : <input type="text" name="username"/> </label></div>
    <br>
    <div><label> Password: <input type="password" name="password"/> </label></div>
    <br>
    <div><input type="submit" value="Sign In"/></div>
    <br><br>
    <a href="/registration">Registration page</a>
</form>

</body>
</html>