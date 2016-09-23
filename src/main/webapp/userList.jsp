<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>User list</title>
</head>
<body>
<h2><a href="index.html">Home</a></h2>
<h2>User list</h2>
<table border="1" cellpadding="8" cellspacing="0">
    <thead>
    <tr>
        <th>Name</th>
        <th>Email</th>
        <th>Date of registration</th>
    </tr>
    </thead>
    <c:forEach items="${userList}" var="user">
        <tr>
            <td> <a href="users?id=${user.id}">${user.name}</a></td>
            <td>${user.email}</td>
            <td>${user.registered}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
