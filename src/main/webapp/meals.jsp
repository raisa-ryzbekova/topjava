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
        <th>Описание</th>
        <th>Дата/время</th>
        <th>Калории</th>
        <th></th>
        <th></th>
    </tr>
    <c:forEach items="${meals}" var="meal">
        <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.MealWithExceed"/>
        <tr style="color:${meal.exceed ? 'red' : 'green'}">
            <td><a href="meals?id=${meal.id}&action=view">${meal.description}</a></td>
            <td><%=formatLocalDateTime(meal.getDateTime())%></td>
            <td>${meal.calories}</td>
            <td><a href="meals?id=${meal.id}&action=edit">Редактировать</a></td>
            <td><a href="meals?id=${meal.id}&action=delete">Удалить</a></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>