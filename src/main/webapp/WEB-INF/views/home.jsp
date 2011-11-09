<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
	<title>XSLT Transform Server</title>
	<link rel="stylesheet" href="resources/style.css" type="text/css"/>
</head>
<html>
<head>
	<title>XSLT Transform Server</title>
</head>
<body>
	<p>The XSLT Transform Server is a web based utility to apply XSLT transformations to XML files.
	 The XSLT files may be referenced by URI or loaded into the service.</p>

	<h3>
	<a href="admin">Admin Console</a> (login required)
	</h3>
	
	<h3>Example</h3>
	<a href="transform?xmlurl=http://www.nws.noaa.gov/view/xml_view2.php?tname=XF0%26prodver=1&xslturl=http://www.nws.noaa.gov/xml/tpex/samples/NaturalViewFoX.xsl&encoding=text">
	Example Transform URL
	</a>
	
	<h3>Syntax</h3>

	<h4>Usage:</h4>
	<table>
	    <tr><td>URL</td><td colspan="2">/transform?[{xsltname} OR {xslturl}]&{xmlurl}</td></tr>
	    <tr><td>Method</td><td colspan="2">GET</td></tr>
	</table>
	<h4>Parameters:</h4>
	<table>
		<tr>
		<th>Parameter</th>
		<th>Description</th>
		</tr>
		<tr>
	    <tr><td>xsltname</td><td>The file name of a loaded XSLT (xsltname=myXslt.xslt)</td></tr>
	    <tr><td>xslturl</td><td>The url of an XSLT (xslturl=http://some/server.org/myXslt.xslt)</td></tr>
	    <tr><td>xmlurl</td><td>The url of an XML file to transform (xmlurl=http://some/server.org/myXml.xml)</td></tr>
	    <tr><td>param</td><td>XSLT Parameter in the form {http://xyz.foo.com/yada/baz.html}foo=value (param={http://my.ns.org}myParam=10)</td></tr>
	    <tr><td>encoding</td><td>The content type to return as. Default: (encoding=application/xml)</td></tr>
	   
	</table>
	 <p>'param' is repeatable to allow for multiple parameters</p>
	 <h4><b>NOTE:</b> 'xsltname' OR 'xslturl' may be specified, but not both</h4>
 
</body>
</html>
