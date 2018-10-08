<%@ page import="static ru.javawebinar.topjava.util.TimeUtil.formatLocalDateTime" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Meals</title>
</head>
<body>
<h2>Meals</h2>
<table border="1" cellpadding="8" cellspacing="0">
    <tr>
        <th>Дата/время</th>
        <th>Описание</th>
        <th>Калории</th>
        <th></th>
        <th></th>
    </tr>
    <c:forEach items="${meals}" var="meal">
        <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.MealWithExceed"/>
        <tr>
            <td style="color:${meal.exceed ? 'red' : 'green'}">${meal.description}</td>
            <td style="color:${meal.exceed ? 'red' : 'green'}"><%=formatLocalDateTime(meal.getDateTime(), "yyyy-MM-dd HH:mm:ss")%>
            </td>
            <td style="color:${meal.exceed ? 'red' : 'green'}">${meal.calories}</td>
            <td>Редактировать</td>
            <td>Удалить</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>