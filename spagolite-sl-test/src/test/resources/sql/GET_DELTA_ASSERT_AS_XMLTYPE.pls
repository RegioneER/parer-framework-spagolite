create or replace FUNCTION GET_DELTA_ASSERT_AS_XMLTYPE (DELTAXML IN XMLTYPE) 
  RETURN XMLTYPE AS risultato XMLTYPE;
BEGIN
  /*
    Questa funzione accetta in input un XML delta nel formato completo (primo processamento delle differenza)
    e ne restituisce un altro in formato compatto adatto alla visualizzazione e alla trasformazione in una VIEW
  */
  SELECT    XMLQUERY( '
    declare function local:processaRecordFigliCancellati($element as element(), $livello as xs:integer)
                                                as element()* {
      let $liv := $livello + 1
      for $k in $element/recordFigliCancellati
        return  local:processaRecordFigliCancellati($k, $liv)
      ,
      let $liv := $livello + 1
      for $a in $element/recordCancellato
        let $id := /risultato/recordMaster/idRecord
        let $dsChiaveTipoRecord :=  string-join($a/keyRecord/datoKey/valoreKey,"~~")
        let $path := $a/../path
        return  <rec>
                    <idRecord>{$id/text()}</idRecord>
                    <tipoAsserzione>recordCancellato</tipoAsserzione>
                    <valoreNew></valoreNew>
                    <valoreOld></valoreOld>
                    <label></label>
                    <dsChiaveTipoRecord>{$dsChiaveTipoRecord}</dsChiaveTipoRecord>
                    <nmTipoRecord>{$a/../tipoRecord/text()}</nmTipoRecord>
                    <livello>{$liv}</livello>
                    <path>{$path/text()}</path>
                    <idRecordFiglio>{$a/idRecord/text()}</idRecordFiglio>
                    <idRecordPadre>{$a/../pathId/text()}</idRecordPadre>
                </rec>
      ,
      let $liv := $livello + 1
      for $a in $element/datiCancellati/datoCancellato
        let $id := /risultato/recordMaster/idRecord
        let $dsChiaveTipoRecord :=  string-join($a/../keyRecord/datoKey/valoreKey,"~~")
        let $path := $a/../../path
        return  <rec>
                    <idRecord>{$id/text()}</idRecord>
                    <tipoAsserzione>datoRecordCancellato</tipoAsserzione>
                    <valoreNew></valoreNew>
                    <valoreOld>{$a/valoreDato/text()}</valoreOld>
                    <label>{$a/labelDato/text()}</label>
                    <dsChiaveTipoRecord>{$dsChiaveTipoRecord}</dsChiaveTipoRecord>
                    <nmTipoRecord>{$a/../../tipoRecord/text()}</nmTipoRecord>
                    <livello>{$liv}</livello>
                    <path>{$path/text()}</path>
                    <idRecordFiglio>{$a/../idRecord/text()}</idRecordFiglio>
                    <idRecordPadre>{$a/../../pathId/text()}</idRecordPadre>
                </rec>
    };

    declare function local:processaRecordFigliModificati($element as element(), $livello as xs:integer)
                        as element()* {
      let $liv := $livello + 1
      for $k in $element/recordFigliModificati
        return  local:processaRecordFigliModificati($k, $liv)
      ,
      (: INSERIMENTO NUOVO RECORD  :)
      let $liv := $livello + 1
      for $a in $element/nuovoRecord
        let $id := /risultato/recordMaster/idRecord
        let $dsChiaveTipoRecord :=  string-join($a/keyRecord/datoKey/valoreKey,"~~")
        let $path := $a/../path
        return  (<rec>
                    <idRecord>{$id/text()}</idRecord>
                    <tipoAsserzione>recordInserito</tipoAsserzione>
                    <valoreNew></valoreNew>
                    <valoreOld></valoreOld>
                    <label></label>
                    <dsChiaveTipoRecord>{$dsChiaveTipoRecord}</dsChiaveTipoRecord>
                    <nmTipoRecord>{$a/../tipoRecord/text()}</nmTipoRecord>
                    <livello>{$liv}</livello>
                    <path>{$path/text()}</path>
                    <idRecordFiglio>{$a/idRecord/text()}</idRecordFiglio>
                    <idRecordPadre>{$a/../pathId/text()}</idRecordPadre>
                </rec>,
                      (: NUOVO DATO IN UN NUOVO RECORD FIGLIO :)
                      for $nuovoDato in $a/nuovoDato
                        return  <rec>
                                    <idRecord>{$id/text()}</idRecord>
                                    <tipoAsserzione>datoRecordInserito</tipoAsserzione>
                                    <valoreNew>{$nuovoDato/valoreDato/text()}</valoreNew>
                                    <valoreOld></valoreOld>
                                    <label>{$nuovoDato/labelDato/text()}</label>
                                    <dsChiaveTipoRecord>{$dsChiaveTipoRecord}</dsChiaveTipoRecord>
                                    <nmTipoRecord>{$nuovoDato/../../tipoRecord/text()}</nmTipoRecord>
                                    <livello>{$liv}</livello>
                                    <path>{$path/text()}</path>
                                    <idRecordFiglio>{$nuovoDato/../idRecord/text()}</idRecordFiglio>
                                    <idRecordPadre>{$nuovoDato/../../pathId/text()}</idRecordPadre>
                                </rec>
                      ,
                let $liv2 := $livello + 1
                for $k2 in $a/recordFigliModificati
                  return  local:processaRecordFigliModificati($k2, $liv2)
                      
                )
                
      ,
      (: DATO RECORD MODIFICATO :)
      let $liv := $livello + 1
      for $a in $element/datiModificati/datoModificato
        let $id := /risultato/recordMaster/idRecord
        let $dsChiaveTipoRecord :=  string-join($a/../keyRecord/datoKey/valoreKey,"~~")
        let $path := $a/../../path
        return  <rec>
                    <idRecord>{$id/text()}</idRecord>
                    <tipoAsserzione>datoRecordModificato</tipoAsserzione>
                    <valoreNew>{$a/nuovoDato/valoreDato/text()}</valoreNew>
                    <valoreOld>{$a/vecchioDato/valoreDato/text()}</valoreOld>
                    <label>{$a/nuovoDato/labelDato/text()}</label>
                    <dsChiaveTipoRecord>{$dsChiaveTipoRecord}</dsChiaveTipoRecord>
                    <nmTipoRecord>{$a/../../tipoRecord/text()}</nmTipoRecord>
                    <livello>{$liv}</livello>
                    <path>{$path/text()}</path>
                    <idRecordFiglio>{$a/../idRecord/text()}</idRecordFiglio>
                    <idRecordPadre>{$a/../../pathId/text()}</idRecordPadre>
                </rec>
      ,
      (: NUOVO DATO IN UN RECORD FIGLIO GIA ESISTENTE :)
      let $liv := $livello + 1
      for $a in $element/datiModificati/nuovoDato
        let $id := /risultato/recordMaster/idRecord
        let $dsChiaveTipoRecord :=  string-join($a/../keyRecord/datoKey/valoreKey,"~~")
        let $path := $a/../../path
        return  <rec>
                    <idRecord>{$id/text()}</idRecord>
                    <tipoAsserzione>datoRecordInserito</tipoAsserzione>
                    <valoreNew>{$a/valoreDato/text()}</valoreNew>
                    <valoreOld></valoreOld>
                    <label>{$a/labelDato/text()}</label>
                    <dsChiaveTipoRecord>{$dsChiaveTipoRecord}</dsChiaveTipoRecord>
                    <nmTipoRecord>{$a/../../tipoRecord/text()}</nmTipoRecord>
                    <livello>{$liv}</livello>
                    <path>{$path/text()}</path>
                    <idRecordFiglio>{$a/../idRecord/text()}</idRecordFiglio>
                    <idRecordPadre>{$a/../../pathId/text()}</idRecordPadre>
                </rec>
      ,
      (: CHIAVE MODIFICATA IN UN RECORD FIGLIO GIA ESISTENTE :)
      let $liv := $livello + 1
      for $a in $element/datiModificati/chiaveModificata
        let $id := /risultato/recordMaster/idRecord
        let $dsChiaveTipoRecord :=  string-join($a/../keyRecord/datoKey/valoreKey,"~~")
        let $path := $a/../../path
        return  <rec>
                    <idRecord>{$id/text()}</idRecord>
                    <tipoAsserzione>chiaveRecordModificata</tipoAsserzione>
                    <valoreNew>{$a/../keyRecord/datoKey[colonnaKey=$a/colonnaKey]/valoreKey/text()}</valoreNew>
                    <valoreOld>{$a/vecchioValoreChiave/text()}</valoreOld>
                    <label></label>
                    <dsChiaveTipoRecord>{$dsChiaveTipoRecord}</dsChiaveTipoRecord>
                    <nmTipoRecord>{$a/../../tipoRecord/text()}</nmTipoRecord>
                    <livello>{$liv}</livello>
                    <path>{$path/text()}</path>
                    <idRecordFiglio>{$a/../idRecord/text()}</idRecordFiglio>
                    <idRecordPadre>{$a/../../pathId/text()}</idRecordPadre>
                </rec>
    };
  
    let $recs := (
      for $a in /risultato
      let $id := $a/recordMaster/idRecord
      let $campiEliminati :=  for $b in $a/recordMaster/campiEliminati/campoEliminato
                                return  <rec>
                                          <idRecord>{$id/text()}</idRecord>
                                          <tipoAsserzione>campoEliminato</tipoAsserzione>
                                          <valoreOld>{$b/valoreDato/text()}</valoreOld>
                                          <label>{$b/labelDato/text()}</label>
                                          <livello>1</livello>
                                        </rec>
      let $campiImpostati :=  for $b in $a/recordMaster/campiImpostati/campoImpostato
                                return  <rec>
                                            <idRecord>{$id/text()}</idRecord>
                                            <tipoAsserzione>campoImpostato</tipoAsserzione>
                                            <valoreNew>{$b/valoreDato/text()}</valoreNew>
                                            <label>{$b/labelDato/text()}</label>
                                            <livello>1</livello>
                                        </rec>
      let $campiModificati :=  for $b in $a/recordMaster/campiModificati/campoModificato
                                return  <rec>
                                            <idRecord>{$id/text()}</idRecord>
                                            <tipoAsserzione>campoModificato</tipoAsserzione>
                                            <valoreNew>{$b/nuovoDatoRecord/valoreDato/text()}</valoreNew>
                                            <valoreOld>{$b/vecchioDatoRecord/valoreDato/text()}</valoreOld>
                                            <label>{$b/nuovoDatoRecord/labelDato/text()}</label>
                                            <livello>1</livello>
                                        </rec>
      let $chiaviModificate :=  for $b in $a/recordMaster/chiaviModificate/chiaveModificata
                                return  <rec>
                                            <idRecord>{$id/text()}</idRecord>
                                            <tipoAsserzione>chiaveModificata</tipoAsserzione>
                                            <valoreNew>{$b/nuovoKeyRecord/datoKey/valoreKey/text()}</valoreNew>
                                            <valoreOld>{$b/vecchioKeyRecord/datoKey/valoreKey/text()}</valoreOld>
                                            <label></label>
                                            <livello>1</livello>
                                        </rec>
      let $recordFigliCancellati := for $b in $a/datiRecordCancellati/recordFigliCancellati
                                        return  local:processaRecordFigliCancellati($b, 1)
      let $recordFigliModificati := for $b in $a/datiRecordModificati/recordFigliModificati
                                        return  local:processaRecordFigliModificati($b, 1)
                                  
      return  ($campiEliminati, $campiImpostati, $campiModificati, $chiaviModificate, $recordFigliCancellati, $recordFigliModificati)
    )
    let $recordMaster := (
      for $a in /risultato/recordMaster
        return  <recordMaster>
                  {$a/tipoRecord}
                  {$a/idRecord}
                  {$a/keyRecord}
                </recordMaster>
    )
    return  <asserzioni>
              {$recordMaster}
              {$recs}
            </asserzioni>

  ' PASSING DELTAXML RETURNING CONTENT) AS RISULTATO_XML INTO risultato
FROM  DUAL;

  RETURN risultato;
END GET_DELTA_ASSERT_AS_XMLTYPE;