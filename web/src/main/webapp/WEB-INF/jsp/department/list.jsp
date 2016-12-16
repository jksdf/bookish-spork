<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" trimDirectiveWhitespaces="false" session="false" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="my" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<my:mainpage>
<jsp:attribute name="body">

    <a href="${pageContext.request.contextPath}/department/new" class="btn btn-primary">
        <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
        New department
    </a>

    <table class="table">
        <thead>
        <tr>
            <th>short name</th>
            <th>name</th>
            <th>number of books</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${departments}" var="department">
            <tr>
                <td>${department.shortName}</td>
                <td><c:out value="${department.name}"/></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>


</jsp:attribute>
</my:mainpage>