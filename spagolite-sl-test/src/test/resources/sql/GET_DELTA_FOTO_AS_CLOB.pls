create or replace FUNCTION GET_DELTA_FOTO_AS_CLOB (XMLNEW IN CLOB, XMLOLD IN CLOB) 
  RETURN CLOB AS risultato CLOB;
BEGIN
  /*
    Questa funzione riceve in input la foto di un oggetto Attuale ed una precedente (IN FORMATO CLOB) e torna
    l'XML del DELTA tra le due (IN FORMATO DELTA).
  */
  SELECT  GET_DELTA_FOTO_AS_XMLTYPE(XMLTYPE(XMLNEW), XMLTYPE(XMLOLD)).getClobVal() 
  INTO    risultato
  FROM    DUAL;
  
  RETURN risultato;
  
END GET_DELTA_FOTO_AS_CLOB;