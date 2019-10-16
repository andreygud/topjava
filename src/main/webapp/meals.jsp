<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<%--<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>--%>
<html>
<head>
    <title>Meal list</title>
    <style>
        .normal {
            color: green;
        }

        .excess {
            color: red;
        }
    </style>
</head>
<body>
<section>
    <h3><a href="index.html">Home</a></h3>
    <hr/>
    <h2>Meals</h2>
    <form method="get" action="meals" id="filterForm">
        <input type="text" value="filtered" name="action" hidden>
        <table>
            <tr>
                <th></th>
                <th>Start</th>
                <th>End</th>
            </tr>
            <tr>
                <th>Date</th>
                <td><input type="date" value="${param.dateStart}" name="dateStart" id="1"></td>
                <td><input type="date" value="${param.dateEnd}" name="dateEnd" id="2"></td>
            </tr>
            <tr>
                <th>Time</th>
                <td><input type="time" value="${param.timeStart}" name="timeStart" id="3"></td>
                <td><input type="time" value="${param.timeEnd}" name="timeEnd" id="4"></td>
            </tr>
        </table>
        <br/>
        <button type="submit">Filter</button>
        <button type="submit"
                onclick="document.getElementById('1').value='';document.getElementById('2').value='';document.getElementById('3').value='';document.getElementById('4').value='';">
            Reset
        </button>
    </form>
    <a href="meals?action=create">Add Meal</a>
    <br/>
    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <c:forEach items="${meals}" var="meal">
            <jsp:useBean id="meal" type="ru.javawebinar.topjava.to.MealTo"/>
            <tr class="${meal.excess ? 'excess' : 'normal'}">
                <td>
                        <%--${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}--%>
                        <%--<%=TimeUtil.toString(meal.getDateTime())%>--%>
                        <%--${fn:replace(meal.dateTime, 'T', ' ')}--%>
                        ${fn:formatDateTime(meal.dateTime)}
                </td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="meals?action=update&id=${meal.id}">Update</a></td>
                <td><a href="meals?action=delete&id=${meal.id}">Delete</a></td>
            </tr>
        </c:forEach>
    </table>
</section>
</body>
</html>