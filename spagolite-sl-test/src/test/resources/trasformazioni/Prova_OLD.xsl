<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="html" encoding="UTF-8" indent="yes"/>
        <HTML>
            <BODY>
<!--                
                <xsl:template match="/risultato/recordMaster">
                    Modifica <b>
                        <xsl:value-of select="tipoRecord"/>
                    </b>
                    identificato da <xsl:for-each select="keyRecord/datoKey">
                        <xsl:value-of select="valoreKey" /> - 
                    </xsl:for-each>
                    <hr></hr>
                    <ul>

                        <xsl:for-each select="chiaviModificate/chiaveModificata">
                            <li>
                                Modificato campo chiave <b>
                                    <xsl:value-of select="vecchioKeyRecord/datoKey/colonnaKey" />
                                </b>
                                da <b>
                                    <xsl:value-of select="vecchioKeyRecord/datoKey/valoreKey" />
                                </b>
                                a <b>
                                    <xsl:value-of select="nuovoKeyRecord/datoKey/valoreKey" />
                                </b>
                            </li>
                        </xsl:for-each>			

                    </ul>
                    <hr></hr>
                    <ul>
                        <xsl:for-each select="campiEliminati/campoEliminato">
                            <li>
                                Eliminato campo 
                                <b><xsl:value-of select="labelDato" /></b> con valore 
                                <b><xsl:value-of select="valoreDato" /></b>
                            </li>
                        </xsl:for-each>

                        <xsl:for-each select="campiImpostati/campoImpostato">
                            <li>
                                Impostato campo
                                <b><xsl:value-of select="labelDato" /></b> con valore 
                                <b><xsl:value-of select="valoreDato" /></b>
                            </li>
                        </xsl:for-each>				
                        <xsl:for-each select="campiModificati/campoModificato">
                            <li>
                                Modificato campo <b>
                                    <xsl:value-of select="vecchioDatoRecord/labelDato" />
                                </b>
                                da <b>
                                    <xsl:value-of select="vecchioDatoRecord/valoreDato" />
                                </b>
                                a <b>
                                    <xsl:value-of select="nuovoDatoRecord/valoreDato" />
                                </b>
                            </li>
                        </xsl:for-each>	
                    </ul>				
                    <hr></hr>
                </xsl:template>
				
 -->
 
                <xsl:template match="/risultato">
                    <xsl:apply-templates select="datiRecordAggiunti/recordFigli" />
                </xsl:template>
            </BODY>
        </HTML>

    <xsl:template match="datiRecordAggiunti">
        <xsl:apply-templates select="recordFigli" />
    </xsl:template>	

    <xsl:template match="recordFigli">
        <ul>
            Record figlio <b><xsl:value-of select="tipoRecord" /></b><BR/>
<!--            <xsl:apply-templates select="nuoviDati" />
            <xsl:apply-templates select="nuovoRecord" /> -->
            <xsl:apply-templates select="recordFigli" /> 
        </ul>
    </xsl:template>	
<!--
    <xsl:template match="nuovoRecord">
        <li>
            Inserito nuovo record identificato dalla chiave
                <b>
                    <xsl:for-each select="keyRecord/datoKey">
                        <xsl:value-of select="valoreKey" /> - 
                    </xsl:for-each>
                </b>
        </li>
        <xsl:apply-templates select="recordFigli" />
    </xsl:template>	

    <xsl:template match="nuoviDati">
        <xsl:for-each select="nuovoDato">
            <li>Inserito nuovo dato <b><xsl:value-of select="labelDato" /></b> con valore <b><xsl:value-of select="valoreDato" /></b>
            </li>
        </xsl:for-each>
    </xsl:template>	
-->
    <!--
    <xsl:template match="recordCancellati/recordCancellato">
        <hr></hr>
        <li>
            Cancellato <b>
                <xsl:value-of select="../../tipoRecord" />
            </b>
            identificato da
            <b>
                <xsl:for-each select="keyRecord/datoKey">
                    <xsl:value-of select="valoreKey" /> - 
                </xsl:for-each>
            </b>
        </li>
    </xsl:template>
	
    <xsl:template match="recordModificati/recordModificato">
        <hr></hr>
        <li>
            Modificato <b>
                <xsl:value-of select="../../tipoRecord" />
            </b>
            identificato da
            <b>
                <xsl:for-each select="keyRecord/datoKey">
                    <xsl:value-of select="valoreKey" /> - 
                </xsl:for-each>
            </b>
            <ul>
                <xsl:for-each select="datiRecord">
                    <li>
                        Campo <xsl:value-of select="nuovoDatoRecord/datoRecord/labelDato" />
                        da <b>'<xsl:value-of select="vecchioDatoRecord/datoRecord/valoreDato" />'</b>
                        a <b>'<xsl:value-of select="nuovoDatoRecord/datoRecord/valoreDato" />'</b>
                    </li>
                </xsl:for-each>
            </ul>
        </li>
    </xsl:template>

        <xsl:template match="datiRecordInseriti/datoRecordInserito">
            <hr></hr>
            <li>
                Inserito <b>
                    <xsl:value-of select="../../tipoRecord" />
                </b>
                identificato da
                <b>
                    <xsl:for-each select="keyRecord/datoKey">
                        <xsl:value-of select="valoreKey" /> - 
                    </xsl:for-each>
                </b>
                <ul>
                    <xsl:for-each select="datoRecord">
                        <li>
                            Campo <xsl:value-of select="labelDato" />
                            impostato a <b>'<xsl:value-of select="valoreDato" />'</b>
                        </li>
                    </xsl:for-each>
                </ul>
            </li>
        </xsl:template>	
    -->
    
</xsl:stylesheet>