<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="html" encoding="UTF-8" indent="yes"/> 

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
                <xsl:apply-templates select="rec[tipoAsserzione='chiaveModificata']" />
                <xsl:apply-templates select="rec[tipoAsserzione='campoEliminato']" />
                <xsl:apply-templates select="rec[tipoAsserzione='campoImpostato']" />
                <xsl:apply-templates select="rec[tipoAsserzione='campoModificato']" />
                <xsl:apply-templates select="rec[nmTipoRecord]" >
                    <xsl:sort select="path" />
                </xsl:apply-templates>
            </BODY>
        </HTML>        
    </xsl:template>


    <xsl:template match="rec[tipoAsserzione='chiaveModificata']">
        <TABLE style="margin-left:15px" border="0" width="98%" >
            <TR>
                <TD>
                    <TABLE style="margin-left:15px" border="0" width="98%" >
                        <xsl:for-each select=".">
                            <TR bgcolor="#fffae6">
                                <TD>
                                    Campo chiave modificato da <b>
                                        <xsl:value-of select="valoreOld" />
                                    </b>
                                    a <b>
                                        <xsl:value-of select="valoreNew" />
                                    </b>
                                </TD>
                            </TR>
                        </xsl:for-each>				
                    </TABLE>
                </TD>
            </TR>
        </TABLE>			
    </xsl:template>

    <xsl:template match="rec[tipoAsserzione='campoEliminato']">
        <TABLE style="margin-left:15px" border="0" width="98%" >
            <TR>
                <TD>
                    <TABLE style="margin-left:15px" border="0" width="98%" >
                        <xsl:for-each select=".">
                            <TR bgcolor="#fffae6">
                                <TD>
                                    Eliminato campo <b>
                                        <xsl:value-of select="label" />
                                    </b>
                                </TD>
                            </TR>
                        </xsl:for-each>				
                    </TABLE>
                </TD>
            </TR>
        </TABLE>			
    </xsl:template>
                  
    <xsl:template match="rec[tipoAsserzione='campoImpostato']">
        <TABLE style="margin-left:15px" border="0" width="98%" >
            <TR>
                <TD>
                    <TABLE style="margin-left:15px" border="0" width="98%" >
                        <xsl:for-each select=".">
                            <TR bgcolor="#fffae6">
                                <TD>
                                    Impostato campo <b>
                                        <xsl:value-of select="label" />
                                    </b> con valore 
                                    <b>
                                        <xsl:value-of select="valoreNew" />
                                    </b>
                                </TD>
                            </TR>
                        </xsl:for-each>				
                    </TABLE>
                </TD>
            </TR>
        </TABLE>			
    </xsl:template>

    <xsl:template match="rec[tipoAsserzione='campoModificato']">
        <TABLE style="margin-left:15px" border="0" width="98%" >
            <TR>
                <TD>
                    <TABLE style="margin-left:15px" border="0" width="98%" >
                        <xsl:for-each select=".">
                            <TR bgcolor="#fffae6">
                                <TD>
                                    Modificato campo <b>
                                        <xsl:value-of select="label" />
                                    </b>
                                    da <b>
                                        <xsl:value-of select="valoreOld" />
                                    </b>
                                    a <b>
                                        <xsl:value-of select="valoreNew" />
                                    </b>
                                </TD>
                            </TR>
                        </xsl:for-each>				
                    </TABLE>
                </TD>
            </TR>
        </TABLE>			
    </xsl:template>

    <xsl:template match="rec[nmTipoRecord]">
      	<xsl:variable name="precedente">
            <xsl:value-of select="preceding-sibling::*[1]/path" />
        </xsl:variable>

        <TABLE style="margin-left:15px" border="0" width="98%" >
            <xsl:choose>
                <xsl:when test="not(path=$precedente)">
                        <TR>
                            <TD>
                                <TABLE style="margin-left:15px" border="0" width="98%" >
                                    <TR bgcolor="#fffac0">
                                        <TD>
                                            <xsl:value-of select="path" />
                                        </TD>
                                    </TR>
                                    <TR bgcolor="#fffac0">
                                        <TD>
                                            <TABLE style="margin-left:15px" border="0" width="98%" >
                                                <TR bgcolor="#fffac0">
                                                    <TD>
                                                         <xsl:value-of select="tipoAsserzione" />PATH DIVERSO
                                                    </TD>
                                                </TR>
                                            </TABLE>
                                        </TD>
                                    </TR>
                                </TABLE>
                            </TD>
                        </TR>
                </xsl:when>
                <xsl:otherwise>
                    <TR>
                        <TD>
                            <TABLE style="margin-left:15px" border="0" width="98%" >
                                <TR bgcolor="#fffac0">
                                    <TD>
                                        <TABLE style="margin-left:15px" border="0" width="98%" >
                                            <TR bgcolor="#fffac0">
                                                <TD>
                                                     <xsl:value-of select="tipoAsserzione" />
                                                </TD>
                                            </TR>
                                        </TABLE>
                                    </TD>
                                </TR>
                            </TABLE>
                        </TD>
                    </TR>
                </xsl:otherwise>
            </xsl:choose>        
        </TABLE>			

    </xsl:template>
                 
                                                   
<!--                                    
    <xsl:template match="rec[nmTipoRecord]">
	<xsl:variable name="precedente">
            <xsl:value-of select="preceding-sibling::*[1]/path" />
        </xsl:variable>

        <TABLE style="margin-left:15px" border="0" width="98%" >
            <xsl:choose>
                <xsl:when test="not(path=$precedente)">
                        <TR>
                            <TD>
                                <TABLE style="margin-left:15px" border="0" width="98%" >
                                    <TR bgcolor="#fffac0">
                                        <TD>
                                            <xsl:value-of select="path" />
                                        </TD>
                                    </TR>
                                    <TR bgcolor="#fffac0">
                                        <TD>
                                            <TABLE style="margin-left:15px" border="0" width="98%" >
                                                <TR bgcolor="#fffac0">
                                                    <TD>
                                                         <xsl:value-of select="tipoAsserzione" />PATH DIVERSO
                                                    </TD>
                                                </TR>
                                            </TABLE>
                                        </TD>
                                    </TR>
                                </TABLE>
                            </TD>
                        </TR>
                </xsl:when>
                <xsl:otherwise>
                    <TR>
                        <TD>
                            <TABLE style="margin-left:15px" border="0" width="98%" >
                                <TR bgcolor="#fffac0">
                                    <TD>
                                        <TABLE style="margin-left:15px" border="0" width="98%" >
                                            <TR bgcolor="#fffac0">
                                                <TD>
                                                     <xsl:value-of select="tipoAsserzione" />
                                                </TD>
                                            </TR>
                                        </TABLE>
                                    </TD>
                                </TR>
                            </TABLE>
                        </TD>
                    </TR>
                </xsl:otherwise>
            </xsl:choose>        
        </TABLE>			

    </xsl:template>
-->
<!--
    <xsl:template match="rec[tipoAsserzione='recordCancellato']">
        <TABLE style="margin-left:15px" border="0" width="98%" >
            <TR>
                <TD>
                    <TABLE style="margin-left:15px" border="0" width="98%" >
                        <xsl:for-each select=".">
                            <TR bgcolor="#fff5cc">
                                <TD>
                                    Cancellato record identificato da 
                                        <b><xsl:value-of select="dsChiaveTipoRecord" /></b>
                                </TD>
                            </TR>
                        </xsl:for-each>				
                        
                        </TABLE>
                    </TD>
                </TR>
            </TABLE>			
        </xsl:template>
    -->                                                                                                                                                                     
</xsl:stylesheet>