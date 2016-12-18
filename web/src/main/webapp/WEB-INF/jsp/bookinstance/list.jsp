<%--
  User: Matej Gallo
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" trimDirectiveWhitespaces="false" session="false" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="my" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<my:mainpage title="Book Instances">
    <jsp:attribute name="body">

        <div class="dropdown">
            <button class="btn btn-primary dropdown-toggle" type="button" id="filterMenu" data-toggle="dropdown">Filter
                <span class="caret"></span></button>
            <ul class="dropdown-menu" role="menu" aria-labelledby="filterMenu">
                <li role="presentation"><a role="menuitem" href="<my:replaceParam name="filter" value="all"/>"><fmt:message key="binstance.all"></fmt:message></a></li>
                <li role="presentation" class="divider"></li>
                <li role="presentation"><a role="menuitem" href="<my:replaceParam name="filter" value="available"/>"><fmt:message key="binstance.available.plural"></fmt:message></a></li>
                <li role="presentation"><a role="menuitem" href="<my:replaceParam name="filter" value="borrowed"/>"><fmt:message key="binstance.borrowed.plural"></fmt:message></a></li>
                <li role="presentation"><a role="menuitem" href="<my:replaceParam name="filter" value="removed"/>"><fmt:message key="binstance.removed.plural"></fmt:message></a></li>
            </ul>
        </div>

        <table class="table bookInstance">
            <thead>
            <tr>
                <th class="center nounderline">
                        <c:if test="${isEmployee && not empty bookId}">
                            <a href="${pageContext.request.contextPath}/bookinstance/new/${bookId}"><span
                                    class="glyphicon glyphicon-plus text-success" aria-hidden="true"></span>Add</a>
                        </c:if>
                </th>
                <th>ID</th>
                <th><fmt:message key="binstance.book"/></th>
                <th><fmt:message key="binstance.version"/></th>
                <th><fmt:message key="binstance.state"/></th>
                <th><fmt:message key="binstance.availability"/></th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${bookinstances}" var="bookinstance">
                <tr>
                    <td class="center nounderline">
                            <c:if test="${isEmployee}">
                            <div class="glyphic">
                                <form method="post" action="${pageContext.request.contextPath}/bookinstance/${bookinstance.id}/discard">
                                    <c:if test="${bookinstance.bookAvailability == 'AVAILABLE'}">
                                        <button type="submit" class="btn btn-xs btn-link">
                                            <span class="glyphicon glyphicon-minus text-danger"
                                                  aria-hidden="true"></span>
                                        </button>
                                    </c:if>
                                    <c:if test="${bookinstance.bookAvailability != 'AVAILABLE'}">
                                        <button type="submit" class="btn btn-xs btn-link" disabled>
                                            <span class="glyphicon glyphicon-minus text-muted"
                                                  aria-hidden="true"></span>
                                        </button>
                                    </c:if>
                                </form>
                                <a href="${pageContext.request.contextPath}/bookinstance/${bookinstance.id}/edit/state">
                                    <span class="glyphicon glyphicon-pencil text-warning" aria-hidden="true"></span>
                                </a>
                            </div>
                            </c:if>
                    </td>
                    <td>${bookinstance.id}</td>
                    <td>
                        <a href="${pageContext.request.contextPath}/book/${bookinstance.book.id}">
                            <c:out value="${bookinstance.book.name}"/>
                        </a>
                    </td>
                    <td><c:out value="${bookinstance.version}"/></td>
                    <td><c:out value="${bookinstance.bookState}"/></td>
                    <td>
                        <c:if test="${bookinstance.bookAvailability == 'AVAILABLE'}">
                            <span class="text-success"><fmt:message key="binstance.available"/></span>
                        </c:if>
                        <c:if test="${bookinstance.bookAvailability == 'BORROWED'}">
                            <span class="text-muted"><fmt:message key="binstance.borrowed"/></span>
                        </c:if>
                        <c:if test="${bookinstance.bookAvailability == 'REMOVED'}">
                            <span class="text-danger"><fmt:message key="binstance.removed"/></span>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </jsp:attribute>
</my:mainpage>