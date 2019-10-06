<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<html>
<head>
    <title>Meals</title>
    <link rel="stylesheet" type="text/css" href="css/style.css"/>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals:</h2>
<p><a href="meals?action=add" class="button">Add New Record</a></p>
<table>
    <tr>
        <th>Description</th>
        <th>DateTime</th>
        <th>Calories</th>
        <th>Action</th>
    </tr>
    <%--@elvariable id="mealtolist" type="java.util.List"--%>
    <c:forEach var="mealto" items="${mealtolist}">
        <%--@elvariable id="mealto" type="ru.javawebinar.topjava.model.MealTo"--%>
        <tr class="${mealto.excess? 'red':'green'}">
            <td>${mealto.description}</td>
            <td>${mealto.dateTime.format( DateTimeFormatter.ofPattern("dd.MM.yy hh:mm"))}</td>
            <td>${mealto.calories}</td>
            <td>
                <a href="meals?action=edit&id=${mealto.id}">Edit</a>
                <a href="meals?action=delete&id=${mealto.id}">Delete</a>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>