<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="html" encoding="UTF-8" indent="yes"/>

    <xsl:template match="/risultato">
        <HTML>
            <BODY>
                <xsl:apply-templates select="recordMaster" />
                <xsl:apply-templates select="datiRecordAggiunti" />
                <xsl:apply-templates select="datiRecordCancellati" />
            </BODY>
        </HTML>
    </xsl:template>

    <xsl:template match="recordMaster">
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
                    <b>
                        <xsl:value-of select="labelDato" />
                    </b> con valore 
                    <b>
                        <xsl:value-of select="valoreDato" />
                    </b>
                </li>
            </xsl:for-each>

            <xsl:for-each select="campiImpostati/campoImpostato">
                <li>
                    Impostato campo
                    <b>
                        <xsl:value-of select="labelDato" />
                    </b> con valore 
                    <b>
                        <xsl:value-of select="valoreDato" />
                    </b>
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

    <xsl:template match="datiRecordAggiunti">
        <xsl:apply-templates select="recordFigli" />
    </xsl:template>	

    <xsl:template match="recordFigli">
        <ul>
            Record figlio <b>
                <xsl:value-of select="tipoRecord" />
            </b>
            <BR/>
            <xsl:apply-templates select="nuoviDati" /> 
            
            <xsl:variable name="tipoRecordOrigine" select="tipoRecord" />
            <xsl:variable name="idRecordOrigine" select="idRecord" />
            <xsl:for-each select="/risultato/datiRecordCancellati//recordFigliCancellati[tipoRecord=$tipoRecordOrigine]/datiCancellati[idRecord=$idRecordOrigine]/datoCancellato">
                <li>
                    Cancellato dato <xsl:value-of select="labelDato" /> con valore <xsl:value-of select="valoreDato" />
                </li>
            </xsl:for-each>
            
            <xsl:apply-templates select="nuovoRecord" /> 
            <xsl:apply-templates select="recordFigli" /> 
        </ul>
    </xsl:template>	

    <xsl:template match="nuovoRecord">
        <li>
            Inserito nuovo record identificato dalla chiave
            <b>
                <xsl:for-each select="keyRecord/datoKey">
                    <xsl:value-of select="valoreKey" /> - 
                </xsl:for-each>
            </b>
            <ul>
                <xsl:for-each select="nuovoDato">
                    <li>Campo <i>
                            <xsl:value-of select="labelDato" />
                        </i> impostato a <b>
                            <xsl:value-of select="valoreDato" />
                        </b>
                    </li>
                </xsl:for-each>
            </ul>
        </li>
        <xsl:apply-templates select="recordFigli" />
    </xsl:template>

    <xsl:template match="nuoviDati">
        <xsl:for-each select="nuovoDato">
            <li>Impostato nuovo dato <b>
                    <xsl:value-of select="labelDato" />
                </b> con valore <b>
                    <xsl:value-of select="valoreDato" />
                </b>
            </li>
        </xsl:for-each>
    </xsl:template>	
        
    <!-- ZONA RECORD CANCELLATI -->
        
    <xsl:template match="datiRecordCancellati">
        <xsl:apply-templates select="recordFigli2" />
    </xsl:template>	
    
    <xsl:template match="recordFigli2">
        <xsl:variable name="tipoRecordOrigine" select="tipoRecord" />
        <xsl:if test="not(/risultato/datiRecordAggiunti//recordFigli[tipoRecord=$tipoRecordOrigine])">
            <ul>
                Record figlio <b>
                    <xsl:value-of select="tipoRecord" />
                </b>
                <BR/>
                <xsl:apply-templates select="recordCancellato" /> 
                <xsl:apply-templates select="recordFigli2" /> 
            </ul>
        </xsl:if>
    </xsl:template>	
    
    <xsl:template match="recordCancellato">
        <li>Cancellato record identificato da 
            <xsl:for-each select="keyRecord/datoKey">
                <xsl:value-of select="valoreKey" /> - 
            </xsl:for-each>
        </li>
    </xsl:template>	
        
</xsl:stylesheet>