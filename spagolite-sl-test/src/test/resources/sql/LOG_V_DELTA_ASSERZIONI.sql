CREATE VIEW LOG_V_DELTA_ASSERZIONI 
              ( ID_ASSERZIONE_DELTA_FOTO,
                ID_OGGETTO_EVENTO,
                ID_OGGETTO,
                TIPO_ASSERZIONE, 
                NM_TIPO_RECORD, 
                LABEL_CAMPO,
                DS_VALORE_NEW_CAMPO,
                DS_VALORE_OLD_CAMPO,
                DS_CHIAVE_TIPO_RECORD,
                PATH,
                LIVELLO,
                ID_RECORD,
                ID_RECORD_PADRE) AS
SELECT  ROWNUM AS ID_ASSERZIONE_DELTA_FOTO,
        delta.ID_OGGETTO_EVENTO AS ID_OGGETTO_EVENTO,
        xml.ID_OGGETTO AS ID_OGGETTO,
        xml."TIPO_ASSERZIONE" AS TIPO_ASSERZIONE, 
        xml."NM_TIPO_RECORD" AS NM_TIPO_RECORD, 
        xml."LABEL_CAMPO" AS LABEL_CAMPO,
        xml."DS_VALORE_NEW_CAMPO" AS DS_VALORE_NEW_CAMPO,
        xml."DS_VALORE_OLD_CAMPO" AS DS_VALORE_OLD_CAMPO,
        xml."DS_CHIAVE_TIPO_RECORD" AS DS_CHIAVE_TIPO_RECORD,
        xml."PATH" AS PATH,
        xml."LIVELLO" AS LIVELLO,
        xml."ID_RECORD" AS ID_RECORD,
        xml."ID_RECORD_PADRE" AS ID_RECORD_PADRE
FROM    LOG_DELTA_FOTO delta,
-- (SELECT XMLTYPE(get_static_xml_delta()) AS DELTA_XML FROM DUAL) delta,
        XMLTABLE('/asserzioni'
          PASSING delta.BL_DELTA_FOTO
          COLUMNS 
             "ID_OGGETTO" integer PATH '/asserzioni/rec/idRecord',
             "TIPO_ASSERZIONE" varchar2(60) PATH '/asserzioni/rec/tipoAsserzione',
             "NM_TIPO_RECORD" varchar2(60) PATH '/asserzioni/rec/nmTipoRecord',
             "DS_VALORE_NEW_CAMPO" varchar2(256) PATH '/asserzioni/rec/valoreNew',
             "DS_VALORE_OLD_CAMPO" varchar2(256) PATH '/asserzioni/rec/valoreOld',
             "LABEL_CAMPO" varchar2(256) PATH '/asserzioni/rec/label',
             "LIVELLO" integer PATH '/asserzioni/rec/livello',
             "DS_CHIAVE_TIPO_RECORD" varchar2(256) PATH '/asserzioni/rec/dsChiaveTipoRecord',
             "PATH" varchar2(256) PATH '/asserzioni/rec/path',
             "ID_RECORD" integer PATH '/asserzioni/rec/idRecordFiglio',
             "ID_RECORD_PADRE" integer PATH '/asserzioni/rec/idRecordPadre'
             ) 
      xml
WHERE   xml.ID_OGGETTO IS NOT NULL
ORDER BY PATH NULLS FIRST, TIPO_ASSERZIONE;