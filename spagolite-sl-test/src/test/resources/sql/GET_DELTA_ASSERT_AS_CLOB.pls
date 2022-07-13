create or replace FUNCTION GET_DELTA_ASSERT_AS_CLOB (DELTAXML IN CLOB) 
  RETURN CLOB AS risultato CLOB;
BEGIN
  /*
    Questa funzione accetta in input un XML delta (IN FORMATO CLOB) nel formato completo (primo processamento delle differenza)
    e ne restituisce un altro in formato (CLOB) compatto adatto alla visualizzazione e alla trasformazione in una VIEW
  */

  SELECT  GET_DELTA_ASSERT_AS_XMLTYPE(XMLTYPE(DELTAXML)).getClobVal()
  INTO    risultato
  FROM    DUAL;
  
  RETURN risultato;

END GET_DELTA_ASSERT_AS_CLOB;