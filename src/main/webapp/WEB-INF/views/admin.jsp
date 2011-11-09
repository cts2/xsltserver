<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
		<title>Admin</title>
		<link rel="stylesheet" href="resources/style.css" type="text/css"/>
	</head>
  <body>
    <h2>Loaded XSLT Files</h2>
    <table width="50%">
        <tr>
            <th width="4%">No</th>
            <th width="30%">Filename</th>
            <th width="20%">&nbsp;</th>
        </tr>
        <c:choose>
            <c:when test="${files != null}">
                <c:forEach var="file" items="${files}" varStatus="counter">
                    <tr>
                        <td>${counter.index + 1}</td>
                        <td>${file}</td>
                        <td><div align="left">
                        	<form:form action="admin/file/${file}" method="delete">
                        		<input type="submit" value="Delete"/>
                        		<a href="admin/file/${file}">Download</a>
                            </form:form>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
            </c:when>
        </c:choose>
    </table>

    <h2>Add New File</h2>
    <form action="admin/files" method="post" enctype="multipart/form-data">
        <table width="50%" border="1" cellspacing="0">
            <tr>
                <td width="35%"><strong>File to upload</strong></td>
                <td width="65%"><input type="file" name="file" /></td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td><input type="submit" name="submit" value="Add"/></td>
            </tr>
        </table>
    </form>
  </body>
</html>
