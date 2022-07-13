<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="html" encoding="UTF-8" indent="yes"/>
    
    <xsl:template match="/risultato">
        <html>
            <body>
                 <xsl:call-template name="figli" />
            </body>
        </html>
    </xsl:template>
    
    <xsl:template name="figli">
        <ul>
            <xsl:for-each select="datiRecordAggiunti/recordFigli ">
                <ul>
                    TIPO_RECORD = <xsl:value-of select="tipoRecord" />
                    <li>
                        Dato inserito = <xsl:value-of select="nuovoDato" /> 
                    </li>
                        <xsl:variable name="tipoRecordOrigine" as="xs:element" select="tipoRecord"/>
                        <xsl:variable name="recordFiglioCancellato" as="xs:element" select="/risultato/datiRecordCancellati//recordFigli[tipoRecord=$tipoRecordOrigine]" />
                        <xsl:if test="$recordFiglioCancellato">
                            <li>
                               Dato Cancellato = <xsl:value-of select="$recordFiglioCancellato/datoCancellato/text()" />
                            </li>
                        </xsl:if>
                </ul>
            </xsl:for-each>
        </ul>
    </xsl:template>    
    
</xsl:stylesheet>