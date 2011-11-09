<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    exclude-result-prefixes="xs"
    version="2.0">
    <xsl:param name="param1"/>
    <xsl:param name="param2"/>
    <xsl:template match="/">
Parm1: <xsl:value-of select="$param1"/>
Parm2: <xsl:value-of select="$param2"/>
    </xsl:template>
</xsl:stylesheet>