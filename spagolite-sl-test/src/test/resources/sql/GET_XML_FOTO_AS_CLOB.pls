create or replace FUNCTION GET_XML_FOTO_AS_CLOB (QUERY_STRING IN CLOB, ID_OGGETTO IN NUMBER) RETURN CLOB 
IS
  foto XMLTYPE;
BEGIN
  EXECUTE IMMEDIATE QUERY_STRING INTO foto USING ID_OGGETTO;
  
--  dbms_output.put_line('Risultato 2..:'||foto.getClobval());
  
  RETURN foto.getClobval();
END GET_XML_FOTO_AS_CLOB;