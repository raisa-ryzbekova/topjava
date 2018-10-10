<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-type" content="text/html; charset=UTF-8">
    <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.Meal" scope="request"/>
    <title>Meal</title>
</head>
<body>
<form method="post" action="meals">
    <input type="hidden" name="id" value="${meal.id}">
    <dl>
        <dt>Описание</dt>
        <dd><input type="text" name="description" size=50 value="${meal.description!=null?meal.description:""}"></dd>
    </dl>
    <dl>
        <dt>Дата/время</dt>
        <dd><input type="datetime-local" name="dateTime" size=50 value="${meal.dateTime!=null?meal.dateTime:""}"></dd>
    </dl>
    <dl>
        <dt>Калории</dt>
        <dd><input type="text" name="calories" size=50 value="${meal.calories!="0"?meal.calories:""}"></dd>
    </dl>
    <button type="submit">Сохранить</button>
    <button onclick="window.history.back()">Отменить</button>
</form>
</body>
</html>
