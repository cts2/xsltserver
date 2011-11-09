<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
	<title>Error</title>
	<link rel="stylesheet" href="resources/style.css" type="text/css"/>
</head>
<body>
	<h2>Syntax Error</h2>
	<p>${message}</p>
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
	   
	</table>
	 <p>'param' is repeatable to allow for multiple parameters</p>
	 <h4><b>NOTE:</b> 'xsltname' OR 'xslturl' may be specified, but not both</h4>
 
</body>
</html>