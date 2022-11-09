<%--The following links are the sources of this sample--%>
<%--https://www3.ntu.edu.sg/home/ehchua/programming/java/JSPByExample.html--%>
<%--https://www.baeldung.com/jstl--%>
<%--https://www.youtube.com/watch?v=Ka9lKLtlcZs--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE>
<html lang="en">
<head><title>First JSP</title></head>
<body>
<%
    double num = Math.random();
    //noinspection IfStatementWithIdenticalBranches
    if (num > 0.90) {
%>
<h2>You'll have a luck day!</h2>
<p>(<%= num %>)</p>
<%
} else {
%>
<h2>Well, life goes on ... </h2>
<p>(<%= num %>)</p>
<%
    }
%>
<a href="<%= request.getRequestURI() %>"><h3>Try Again</h3></a>
<a href="../index.html">Go back</a>
</body>
</html>
