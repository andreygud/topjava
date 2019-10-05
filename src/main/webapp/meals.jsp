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

<table>
    <tr>
        <th>Description</th>
        <th>Date</th>
        <th>Time</th>
        <th>Calories</th>
    </tr>
    <%--@elvariable id="mealtolist" type="java.util.List"--%>
    <c:forEach var="mealto" items="${mealtolist}">
        <%--@elvariable id="mealto" type="ru.javawebinar.topjava.model.MealTo"--%>
        <tr class="${mealto.excess eq true?"red":"default"}">
            <td>${mealto.description}</td>
            <td>${mealto.dateTime.format( DateTimeFormatter.ofPattern("dd.MM.yy"))}</td>
            <td>${mealto.dateTime.format( DateTimeFormatter.ofPattern("hh:mm"))}</td>
            <td>${mealto.calories}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>