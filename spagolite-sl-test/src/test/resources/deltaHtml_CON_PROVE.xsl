<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:local="http://local">
    <xsl:output method="html" encoding="UTF-8" indent="yes"/> 

    <xsl:function name="local:prova" >
        <xsl:param name="pos"/>
        <xsl:for-each select="datiRecordModificati//recordFigliModificati" >
            <xsl:sort select="tipoRecord" />
            <xsl:value-of select="tipoRecord"></xsl:value-of>
        </xsl:for-each>
    </xsl:function>
                

    <xsl:template match="/risultato"> 
        <HTML>
            <HEAD>
                <STYLE>
                    table, th, td {
                        border: 0px solid grey;
                        font-family: verdana;
                        font-size: 99%;
                    }
                </STYLE>
            </HEAD>
            <BODY>
                <xsl:value-of select="local:prova(1)"/>
            </BODY>
        </HTML>        
    </xsl:template>

</xsl:stylesheet>