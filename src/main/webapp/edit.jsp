<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<html>
<head>
    <title>Meals</title>
    <link rel="stylesheet" type="text/css" href="../css/style.css"/>
</head>
<body>

<h3><a href="../index.html">Home</a></h3>
<hr>
<h2>Add/Edit Meals:</h2>

<form method="POST" action='edit' id="mealUpd">

    <table>
        <tr>
            <th>Property</th>
            <th>Value</th>
        </tr>

        <%--@elvariable id="mealRecord" type="ru.javawebinar.topjava.model.Meal"--%>
        <tr>
            <td class="first_column">ID</td>
            <td class="ro_field"><input type="text" readonly name="id" value="${mealRecord.id}"
                                        class="ro_field input_field"/></td>
        </tr>
        <tr>
            <td class="first_column">Description</td>
            <td><input type="text" name="description" value="${mealRecord.description}" class="input_field"/></td>
        </tr>
        <tr>
            <td class="first_column">Date/Time</td>
            <td><input type="datetime-local" name="datetime" value="${mealRecord.dateTime}" class="input_field"/></td>
        </tr>
        <tr>
            <td class="first_column">Calories</td>
            <td><input type="text" name="calories" value="${mealRecord.calories}" class="input_field"/></td>
        </tr>
    </table>
    <br/>
    <input type="submit" value="Submit"/>

</form>

</body>
</html>