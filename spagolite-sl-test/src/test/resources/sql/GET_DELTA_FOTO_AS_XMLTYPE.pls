create or replace FUNCTION GET_DELTA_FOTO_AS_XMLTYPE (XMLNEW IN XMLTYPE, XMLOLD IN XMLTYPE) 
  RETURN XMLTYPE AS risultato XMLTYPE;
BEGIN
  /*
    Questa funzione riceve in input la foto di un oggetto Attuale ed una precedente e torna
    l'XML del DELTA tra le due.
  */
  SELECT    XMLQUERY(  '
                declare function local:elaboraChildInseritiModificati() 
                    as element()* {

                                (: RECORDS INSERITI NEI recordChild COMPRESI NUOVI RAMI recordChild :)
                                let $childModificati := ( let $blocco := local:processaRecordChildModificati(/diff/foto2/fotoOggetto/recordChild)
                                    return  if ( not(empty($blocco)) )
                                            then  (
                                                    <datiRecordModificati>
                                                      {$blocco}
                                                    </datiRecordModificati>
                                                  )
                                            else  ()
                                )

                                (: RECORDS CANCELLATI NEI recordChild COMPRESI RAMI COMPLETI recordChild :)
                                let $childCancellati := ( let $blocco2 := local:processaRecordChildCancellati(/diff/foto1/fotoOggetto/recordChild)
                                    return  if ( not(empty($blocco2)) )
                                            then  (
                                                    <datiRecordCancellati>
                                                      {$blocco2}
                                                    </datiRecordCancellati>
                                                  )
                                            else  ()
                                )
                                return  <tutto>
                                          {$childModificati}
                                          {$childCancellati}
                                        </tutto>
                };

                declare function local:processaRecordChildModificati($element as element()* )
                    as element()* {
                          for $child in $element
                            let $esisteRecordChild := /diff/foto1//recordChild[tipoRecord=$child/tipoRecord]
                                    (: RAMO IN CUI SI GESTISCE IL CASO IN CUI NELLA PRIMA FOTO MANCA IL recordChild A QUALSIASI LIVELLO :)
                            return  if ( not(exists($esisteRecordChild)) )
                                    then  (
                                            <recordFigliModificati>
                                              {$child/tipoRecord}
                                              <path>{$child/string-join(ancestor-or-self::recordChild/tipoRecord/text(),''/'')}</path>
                                              <pathId>{$child/string-join(ancestor-or-self::child/idRecord/text(),''/'')}</pathId>
                                              {
                                                for $c in $child/child
                                                  return  <nuovoRecord>
                                                            { $c/idRecord, 
                                                              $c/keyRecord,
                                                              for $dato in $c/datoRecord
                                                                return  <nuovoDato>
                                                                          {$dato/*}
                                                                        </nuovoDato>
                                                            }
                                                            {local:processaRecordChildModificati($c/recordChild)}
                                                          </nuovoRecord>
                                              }
                                            </recordFigliModificati>
                                          )
                                          (:  RAMO IN CUI SI GESTISCE IL CASO IN CUI NELLA PRIMA FOTO VIENE TROVATO LO STESSO recordChild A QUALSIASI LIVELLO
                                              MA POTREBBERO ESSERE STATI AGGIUNTI DEI RECORD IN PIU :)
                                    else  (
                                            let $blocco :=  (
                                                for $figlio2 in $child/child
                                                  let $figlio1 := /diff/foto1/fotoOggetto//recordChild[tipoRecord=$figlio2/../tipoRecord]/child[idRecord=$figlio2/idRecord]
                                                    return  if ( empty($figlio1) ) 
                                                            then  (
                                                                    <nuovoRecord>
                                                                      { $figlio2/idRecord, 
                                                                        $figlio2/keyRecord,
                                                                        for $dato in $figlio2/datoRecord
                                                                          return  <nuovoDato>
                                                                                    {$dato/*}
                                                                                  </nuovoDato>
                                                                      }
                                                                      {local:processaRecordChildModificati($figlio2/recordChild)}
                                                                    </nuovoRecord>
                                                                  )
                                                            else  (   (: GESTIONE DEI DATI DEL RECORD AGGIUNTI NEL CASO IN CUI UN RECORD ESISTESSE GIA :)
                                                                      let $datiRecordModificati := (
                                                                        for $datoRecord2 in $figlio2/datoRecord
                                                                        let $datoRecord1 := $figlio1/datoRecord[colonnaDato=$datoRecord2/colonnaDato]
                                                                          return  if ( empty($datoRecord1) )
                                                                                  then  (
                                                                                          <nuovoDato>
                                                                                            {$datoRecord2/*}
                                                                                          </nuovoDato>
                                                                                        )
                                                                                  else  (   (: GESTIONE DEL DATO MODIFICATO :)
                                                                                          if ( not($datoRecord2/labelDato = $datoRecord1/labelDato) or not($datoRecord2/valoreDato = $datoRecord1/valoreDato))
                                                                                          then  (
                                                                                                  <datoModificato>
                                                                                                    <vecchioDato>
                                                                                                      {$datoRecord1/*}
                                                                                                    </vecchioDato>
                                                                                                    <nuovoDato>
                                                                                                      {$datoRecord2/*}
                                                                                                    </nuovoDato>
                                                                                                  </datoModificato>
                                                                                                )
                                                                                          else  ()
                                                                                        )
                                                                      )
                                                                      let $chiaviModificate := (
                                                                        (: MODIFICA DELLA CHIAVE DI UN RECORD :)
                                                                        for $datoKey2 in $figlio2/keyRecord/datoKey
                                                                        let $datoKey1 := $figlio1/keyRecord/datoKey[colonnaKey=$datoKey2/colonnaKey]
                                                                          return  if ( not(empty($datoKey1)) )
                                                                                  then  (
                                                                                          if ( not($datoKey1/valoreKey = $datoKey2/valoreKey) )
                                                                                          then  (
                                                                                                  <chiaveModificata>
                                                                                                    {$datoKey1/colonnaKey}
                                                                                                    <vecchioValoreChiave>
                                                                                                      {$datoKey1/valoreKey/text()}
                                                                                                    </vecchioValoreChiave>
                                                                                                  </chiaveModificata>
                                                                                                )
                                                                                          else  ()
                                                                                        )
                                                                                  else  ()
                                                                      
                                                                      )
                                                                      return  if ( not(empty($datiRecordModificati)) or not(empty($chiaviModificate)) )
                                                                              then  (
                                                                                      <datiModificati>
                                                                                      {
                                                                                        $figlio2/idRecord, 
                                                                                        $figlio2/keyRecord,
                                                                                        $datiRecordModificati,
                                                                                        $chiaviModificate
                                                                                      }
                                                                                      </datiModificati>
                                                                                    )
                                                                              else  ()
                                                                      ,
                                                                      local:processaRecordChildModificati($figlio2/recordChild)
                                                                  )
                                                            )
                                                return  if ( not(empty($blocco)) )
                                                        then  (
                                                                 <recordFigliModificati>
                                                                    {$child/tipoRecord}
                                                                    <path>{$child/string-join(ancestor-or-self::recordChild/tipoRecord/text(),''/'')}</path>
                                                                    <pathId>{$child/string-join(ancestor-or-self::child/idRecord/text(),''/'')}</pathId>
                                                                    {$blocco}
                                                                 </recordFigliModificati>
                                                              )
                                                        else  ()
                                          )
                };
                
                declare function local:processaRecordChildCancellati($element as element()* )
                    as element()* {
                          for $child in $element
                            let $esisteRecordChild := /diff/foto2//recordChild[tipoRecord=$child/tipoRecord]
                                    (: RAMO IN CUI SI GESTISCE IL CASO IN CUI NELLA SECONDA FOTO MANCA IL recordChild A QUALSIASI LIVELLO :)
                            return  if ( not(exists($esisteRecordChild)) )
                                    then  (
                                            <recordFigliCancellati>
                                              {$child/tipoRecord}
                                              <path>{$child/string-join(ancestor-or-self::recordChild/tipoRecord/text(),''/'')}</path>
                                              <pathId>{$child/string-join(ancestor-or-self::child/idRecord/text(),''/'')}</pathId>
                                              {
                                                for $c in $child/child
                                                  return  <recordCancellato>
                                                            { $c/idRecord, 
                                                              $c/keyRecord
                                                            }
                                                            {local:processaRecordChildCancellati($c/recordChild)}
                                                          </recordCancellato>
                                              }
                                            </recordFigliCancellati>
                                          )
                                          (:  RAMO IN CUI SI GESTISCE IL CASO IN CUI NELLA PRIMA FOTO VIENE TROVATO LO STESSO recordChild A QUALSIASI LIVELLO
                                              MA POTREBBERO ESSERE STATI CANCELLATI DEI RECORD :)
                                    else  (
                                            let $blocco :=  (
                                                              for $figlio1 in $child/child
                                                                let $figlio2 := /diff/foto2/fotoOggetto//recordChild[tipoRecord=$figlio1/../tipoRecord]/child[idRecord=$figlio1/idRecord]
                                                                  return  if ( empty($figlio2) ) 
                                                                          then  (
                                                                                  <recordCancellato>
                                                                                    { $figlio1/idRecord, 
                                                                                      $figlio1/keyRecord
                                                                                    }
                                                                                    {local:processaRecordChildCancellati($figlio1/recordChild)}
                                                                                  </recordCancellato>
                                                                                )
                                                                          else  (   (: IL RECORD ESISTE MA POTREBBERO ESSERE STATI CANCELLATI DEI DATI :)
                                                                                    let $datiCancellati := (
                                                                                      for $datoRecord1 in $figlio1/datoRecord
                                                                                      let $datoRecord2 := $figlio2/datoRecord[colonnaDato=$datoRecord1/colonnaDato]
                                                                                        return  if ( empty($datoRecord2) )
                                                                                                then  (
                                                                                                        <datoCancellato>
                                                                                                          {$datoRecord1/*}
                                                                                                        </datoCancellato>
                                                                                                      )
                                                                                                else  ()
                                                                                    )
                                                                                    return  if ( not(empty($datiCancellati)) )
                                                                                            then  (
                                                                                                    <datiCancellati>
                                                                                                    {
                                                                                                      $figlio1/idRecord, 
                                                                                                      $figlio1/keyRecord,
                                                                                                      $datiCancellati
                                                                                                    }
                                                                                                    </datiCancellati>
                                                                                                  )
                                                                                            else  ()
                                                                                    ,                                                                          
                                                                                    local:processaRecordChildCancellati($figlio1/recordChild)
                                                                                )
                                                            )
                                                return  if ( not(empty($blocco)) )
                                                        then  (
                                                                 <recordFigliCancellati>
                                                                    {$child/tipoRecord}
                                                                    <path>{$child/string-join(ancestor-or-self::recordChild/tipoRecord/text(),''/'')}</path>
                                                                    <pathId>{$child/string-join(ancestor-or-self::child/idRecord/text(),''/'')}</pathId>
                                                                    {$blocco}
                                                                 </recordFigliCancellati>
                                                              )
                                                        else  ()
                                          )
                };
                
                for $diff in /diff
                  return  <risultato>
                            <recordMaster>
                              {$diff/foto2/fotoOggetto/recordMaster/tipoRecord}
                              {$diff/foto2/fotoOggetto/recordMaster/idRecord}
                              {$diff/foto2/fotoOggetto/recordMaster/keyRecord}
                              {
                                (: CAMPI CHIAVE MODIFICATI SUL RECORD MASTER :)
                                (
                                  let $blocco :=  (
                                                    for $datoKey in $diff/foto1/fotoOggetto/recordMaster/keyRecord/datoKey
                                                      let $valoreKey1 := $datoKey/valoreKey
                                                      let $valoreKey2 := $diff/foto2/fotoOggetto/recordMaster/keyRecord/datoKey[colonnaKey=$datoKey/colonnaKey]/valoreKey
                                                      return  if ( $valoreKey1 != $valoreKey2 )
                                                              then  <chiaveModificata>
                                                                      <vecchioKeyRecord>
                                                                      {$valoreKey1/..}
                                                                      </vecchioKeyRecord>
                                                                      <nuovoKeyRecord>
                                                                      {$valoreKey2/..}
                                                                      </nuovoKeyRecord>
                                                                    </chiaveModificata>
                                                              else ()
                                                  )
                                    return  if( not(empty($blocco)) )
                                            then  (
                                                    <chiaviModificate>
                                                      {$blocco}
                                                    </chiaviModificate>
                                                  )
                                            else  ()
                                )
                              }
                              {
                                (: CAMPI ELIMINATI SUL RECORD MASTER :)
                                (
                                  let $blocco :=  (
                                                    for $colonnaDato1 in $diff/foto1/fotoOggetto/recordMaster/datoRecord/colonnaDato
                                                      return  if ( not(exists($diff/foto2/fotoOggetto/recordMaster/datoRecord[colonnaDato=$colonnaDato1])) )
                                                              then  <campoEliminato>
                                                                        {$colonnaDato1}
                                                                        {$colonnaDato1/../labelDato}
                                                                        {$colonnaDato1/../valoreDato}
                                                                    </campoEliminato>
                                                              else ()
                                                  )
                                    return  if( not(empty($blocco)) )
                                            then  (
                                                    <campiEliminati>
                                                      {$blocco}
                                                    </campiEliminati>
                                                  )
                                            else  ()
                                )
                              
                              }
                              {
                                (: CAMPI IMPOSTATI SUL RECORD MASTER :)
                                (
                                  let $blocco :=  (
                                                    for $colonnaDato2 in $diff/foto2/fotoOggetto/recordMaster/datoRecord/colonnaDato
                                                      return  if ( not(exists($diff/foto1/fotoOggetto/recordMaster/datoRecord[colonnaDato=$colonnaDato2])) )
                                                              then  
                                                                    <campoImpostato>
                                                                        {$colonnaDato2}
                                                                        {$colonnaDato2/../labelDato}
                                                                        {$colonnaDato2/../valoreDato}
                                                                    </campoImpostato>
                                                              else ()
                                                  )
                                    return  if ( not(empty($blocco)) )
                                            then  (
                                                    <campiImpostati>
                                                      {$blocco}
                                                    </campiImpostati>
                                                  )
                                            else  ()
                                )
                              }
                              {
                                (: CAMPI IMPOSTATI SUL RECORD MASTER :)
                                (
                                  let $blocco :=  (
                                                    for $colonnaDato1 in $diff/foto1/fotoOggetto/recordMaster/datoRecord/colonnaDato
                                                      let $colonnaDato2:=$diff/foto2/fotoOggetto/recordMaster/datoRecord[colonnaDato=$colonnaDato1]/colonnaDato
                                                      let $labelDato:=$colonnaDato1/../labelDato
                                                      let $valoreDato:=$colonnaDato1/../valoreDato
                                                      return  if ( exists($colonnaDato2) )
                                                              then  if ( $colonnaDato2/..[labelDato=$labelDato] and $colonnaDato2/..[valoreDato=$valoreDato] )
                                                                    then ()
                                                                    else 
                                                                          <campoModificato>
                                                                              <vecchioDatoRecord>
                                                                                {$colonnaDato1/../*}
                                                                              </vecchioDatoRecord>
                                                                              <nuovoDatoRecord>
                                                                                {$colonnaDato2/../*}
                                                                              </nuovoDatoRecord>
                                                                          </campoModificato>
                                                              else ()
                                  
                                                  )
                                    return  if( not(empty($blocco)) )
                                            then  (
                                                    <campiModificati>
                                                      {$blocco}
                                                    </campiModificati>
                                                  )
                                            else  ()
                                                  
                                )
                              }
                              </recordMaster>
                              { local:elaboraChildInseritiModificati()/* }
                          </risultato>
              ' PASSING FOTO_XML RETURNING CONTENT) AS RISULTATO_XML INTO risultato
FROM
    (SELECT   XMLELEMENT("diff",
                    XMLELEMENT("foto1",XMLNEW),
                    XMLELEMENT("foto2",XMLOLD)
              ) AS FOTO_XML
    FROM DUAL);


  RETURN risultato;
END GET_DELTA_FOTO_AS_XMLTYPE;