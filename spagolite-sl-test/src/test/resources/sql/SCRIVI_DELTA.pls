create or replace PROCEDURE SCRIVI_DELTA (IDOGGETTOEVENTO IN NUMBER, ID_DELTA OUT NUMBER) AS 
  seqVal number;
  id_oggetto_evento_attuale number;
  id_oggetto_evento_prec number;
  xml_delta XMLTYPE;
BEGIN
  /*
     Questa procedura scrive il delta in formato "ASSERZIONI" nella tabella LOG_DELTA_FOTO
     solo se esiste una foto immediatamente precedente a quella relativa all'ID_OGGETTO_EVENTO
     passato come parametro.
  */
  seqVal := -1;
  SELECT  SLOG_DELTA_FOTO.NEXTVAL ID_DELTA_FOTO,
          oggetto_evento_attuale.id_oggetto_evento ID_OGGETTO_EVENTO_ATTUALE,
          oggetto_evento_prec.ID_OGGETTO_EVENTO ID_OGGETTO_EVENTO_PREC,
          GET_DELTA_ASSERT_AS_XMLTYPE(GET_DELTA_FOTO_AS_XMLTYPE(foto_prec.BL_FOTO_OGGETTO, foto_attuale.BL_FOTO_OGGETTO)) DELTA
  INTO    seqVal,
          id_oggetto_evento_attuale,
          id_oggetto_evento_prec,
          xml_delta
  FROM    LOG_OGGETTO_EVENTO oggetto_evento_attuale
  JOIN    LOG_EVENTO evento_attuale on (
            evento_attuale.id_evento = oggetto_evento_attuale.ID_EVENTO)
  JOIN    LOG_CHIAVE_ACCESSO_EVENTO chiave_accesso_attuale on (
            chiave_accesso_attuale.ID_APPLIC = evento_attuale.ID_APPLIC and
            chiave_accesso_attuale.ID_TIPO_OGGETTO = oggetto_evento_attuale.ID_TIPO_OGGETTO and
            chiave_accesso_attuale.ID_OGGETTO = oggetto_evento_attuale.ID_OGGETTO and
            chiave_accesso_attuale.ID_EVENTO = evento_attuale.ID_EVENTO
          )
  JOIN    LOG_OGGETTO_EVENTO oggetto_evento_prec on (
            oggetto_evento_prec.ID_TIPO_OGGETTO = oggetto_evento_attuale.ID_TIPO_OGGETTO and
            oggetto_evento_prec.TI_RUOLO_OGGETTO_EVENTO = 'outcome' and
            oggetto_evento_prec.ID_OGGETTO = oggetto_evento_attuale.ID_OGGETTO
          )
  JOIN    LOG_EVENTO evento_prec on (
            evento_prec.ID_EVENTO = oggetto_evento_prec.ID_EVENTO
          )
  JOIN    LOG_CHIAVE_ACCESSO_EVENTO chiave_accesso_prec on (
            chiave_accesso_prec.ID_APPLIC = evento_prec.ID_APPLIC and
            chiave_accesso_prec.ID_TIPO_OGGETTO = oggetto_evento_prec.ID_TIPO_OGGETTO and
            chiave_accesso_prec.ID_OGGETTO = oggetto_evento_prec.ID_OGGETTO and
            chiave_accesso_prec.ID_EVENTO = evento_prec.ID_EVENTO 
            and
            chiave_accesso_prec.DT_REG_EVENTO = (
                SELECT  max(max_chiave.DT_REG_EVENTO)
                FROM    LOG_CHIAVE_ACCESSO_EVENTO max_chiave
                WHERE   max_chiave.ID_APPLIC = chiave_accesso_prec.ID_APPLIC
                AND     max_chiave.ID_TIPO_OGGETTO = chiave_accesso_prec.ID_TIPO_OGGETTO
                AND     max_chiave.ID_OGGETTO = oggetto_evento_prec.ID_OGGETTO
                AND     max_chiave.DT_REG_EVENTO < chiave_accesso_attuale.DT_REG_EVENTO
            )
          )
  JOIN    LOG_FOTO_OGGETTO_EVENTO foto_attuale on (
            foto_attuale.ID_OGGETTO_EVENTO = oggetto_evento_attuale.ID_OGGETTO_EVENTO
          )
  JOIN    LOG_FOTO_OGGETTO_EVENTO foto_prec on (
            foto_prec.ID_OGGETTO_EVENTO = oggetto_evento_prec.ID_OGGETTO_EVENTO
          )
  WHERE   oggetto_evento_attuale.id_oggetto_evento = IDOGGETTOEVENTO;

  INSERT INTO LOG_DELTA_FOTO (ID_DELTA_FOTO,ID_OGGETTO_EVENTO,ID_OGGETTO_EVENTO_PREC, BL_DELTA_FOTO)
  VALUES (seqVal,id_oggetto_evento_attuale,id_oggetto_evento_prec,xml_delta);
  ID_DELTA := seqVal;

  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      ID_DELTA := -1;
  
END SCRIVI_DELTA;