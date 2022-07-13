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
                <xsl:apply-templates select="recordMaster" />
                <xsl:apply-templates select="datiRecordModificati" />
            </BODY>
        </HTML>        
    </xsl:template>


    <xsl:template match="recordMaster">
        <TABLE style="margin-left:15px" border="0" width="98%" >
            <TH bgcolor="#ffffcc">
                Modifica <b>
                    <xsl:value-of select="tipoRecord"/>
                </b>
                identificato da <xsl:for-each select="keyRecord/datoKey">
                    <xsl:value-of select="valoreKey" /> - 
                </xsl:for-each>
            </TH>
            <TR>
                <TD>
                    <TABLE style="margin-left:15px" border="0" width="98%" >
                        <xsl:for-each select="chiaviModificate/chiaveModificata">
                            <TR bgcolor="#fffae6">
                                <TD>
                                    Campo chiave <b>
                                        <xsl:value-of select="vecchioKeyRecord/datoKey/colonnaKey" />
                                    </b>
                                    modificato da <b>
                                        <xsl:value-of select="vecchioKeyRecord/datoKey/valoreKey" />
                                    </b>
                                    a <b>
                                        <xsl:value-of select="nuovoKeyRecord/datoKey/valoreKey" />
                                    </b>
                                </TD>
                            </TR>
                        </xsl:for-each>				
                    </TABLE>

                    <TABLE style="margin-left:15px" border="0" width="98%" >
                        <xsl:for-each select="campiEliminati/campoEliminato">
                            <TR bgcolor="#fffae6">
                                <TD>
                                    Eliminato campo <b>
                                        <xsl:value-of select="labelDato" />
                                    </b>
                                </TD>
                            </TR>
                        </xsl:for-each>				
                    </TABLE>

                    <TABLE style="margin-left:15px; margin-right:15px" border="0" width="98%" >
                        <xsl:for-each select="campiImpostati/campoImpostato">
                            <TR bgcolor="#fffae6">
                                <TD>
                                    Impostato campo <b>
                                        <xsl:value-of select="labelDato" />
                                    </b> con valore 
                                    <b>
                                        <xsl:value-of select="valoreDato" />
                                    </b>
                                </TD>
                            </TR>
                        </xsl:for-each>				
                    </TABLE>

                    <TABLE style="margin-left:15px; margin-right:15px" border="0" width="98%" >
                        <xsl:for-each select="campiModificati/campoModificato">
                            <TR bgcolor="#fffae6">
                                <TD>
                                    Modificato campo <b>
                                        <xsl:value-of select="vecchioDatoRecord/labelDato" />
                                    </b>
                                    da <b>
                                        <xsl:value-of select="vecchioDatoRecord/valoreDato" />
                                    </b>
                                    a <b>
                                        <xsl:value-of select="nuovoDatoRecord/valoreDato" />
                                    </b>
                                </TD>
                            </TR>
                        </xsl:for-each>			
                    </TABLE>

                </TD>
            </TR>
        </TABLE>			
    </xsl:template>
	
    <xsl:template match="datiRecordModificati">
        <TABLE style="margin-left:25px" border="0" width="98%" >
            <TR>
                <TD>
                    <xsl:apply-templates select="recordFigliModificati" />
                </TD>
            </TR>
        </TABLE>
    </xsl:template>	

    <xsl:template match="recordFigliModificati">
        <TABLE style="margin-left:25px" border="0" width="98%" >
            <TR bgcolor="#ffad99">
                <TD>
                    Record figlio <b><xsl:value-of select="tipoRecord" /></b>
                </TD>
            </TR>
            <xsl:apply-templates select="datiModificati" /> 
            
            <!-- Gestione dati cancellati -->
            <xsl:variable name="tipoRecordOrigine" select="tipoRecord" />
            <xsl:for-each select="/risultato/datiRecordCancellati//recordFigliCancellati[tipoRecord=$tipoRecordOrigine]/datiCancellati/datoCancellato">
                <TR bgcolor="#fff5cc">
                    <TD>
                        Cancellato dato <xsl:value-of select="labelDato" /> con valore <xsl:value-of select="valoreDato" />
                        identificato da <xsl:for-each select="../keyRecord/datoKey">
                                            <b><xsl:value-of select="valoreKey" /></b> -
                                        </xsl:for-each>
                    </TD>
                </TR>
            </xsl:for-each>
            
            <xsl:apply-templates select="nuovoRecord" /> 
            
            <!-- Gestione RECORD cancellati -->
            <xsl:for-each select="/risultato/datiRecordCancellati//recordFigliCancellati[tipoRecord=$tipoRecordOrigine]/recordCancellato/keyRecord">
                <TR bgcolor="#fff5cc">
                    <TD>
                        Cancellato record identificato da 
                            <xsl:for-each select="datoKey">
                                <b><xsl:value-of select="valoreKey" /></b> -
                            </xsl:for-each>
                    </TD>
                </TR>
            </xsl:for-each>
            
            <TR bgcolor="#fff5cc">
                <TD>
                    <xsl:apply-templates select="recordFigliModificati" />
                </TD>
            </TR>
        </TABLE>
    </xsl:template>	
        
    <xsl:template match="datiModificati">
        <xsl:for-each select="chiaveModificata">
            <TR bgcolor="#fff5cc">
                <TD>
                    Campo chiave <b>
                        <xsl:value-of select="colonnaKey" />
                    </b>
                    con vecchio valore <b>
                        <xsl:value-of select="vecchioValoreChiave" />
                    </b>
                </TD>
            </TR>
        </xsl:for-each>
        <xsl:for-each select="datoModificato">
            <TR bgcolor="#fff5cc">
                <TD>
                    Modificato dato <b><xsl:value-of select="nuovoDato/labelDato" /></b> 
                    da <b><xsl:value-of select="vecchioDato/valoreDato" /></b>
                    a <b><xsl:value-of select="nuovoDato/valoreDato" /></b>
                </TD>
            </TR>
        </xsl:for-each>
        <xsl:for-each select="nuovoDato">
            <TR bgcolor="#fff5cc">
                <TD>
                    Inserito nuovo dato <b><xsl:value-of select="labelDato" /></b> 
                    con valore <b><xsl:value-of select="valoreDato" /></b>
                </TD>
            </TR>
        </xsl:for-each>
        
    </xsl:template>	
        
    <xsl:template match="nuovoRecord">
        <TR bgcolor="#fff5cc">
            <TD>
                Inserito nuovo record identificato dalla chiave
                <b>
                    <xsl:for-each select="keyRecord/datoKey">
                        <xsl:value-of select="valoreKey" /> - 
                    </xsl:for-each>
                </b>
                <TABLE style="margin-left:25px" border="0" width="98%" >
                            <xsl:for-each select="nuovoDato">
                                <TR bgcolor="#fff5cc">
                                    <TD>
                                        Campo <i><xsl:value-of select="labelDato" /></i> 
                                        impostato a <b><xsl:value-of select="valoreDato" /></b>
                                    </TD>
                                </TR>
                            </xsl:for-each>
                </TABLE>
                <xsl:apply-templates select="recordFigliModificati" />
            </TD>
        </TR>
    </xsl:template>
        
</xsl:stylesheet>