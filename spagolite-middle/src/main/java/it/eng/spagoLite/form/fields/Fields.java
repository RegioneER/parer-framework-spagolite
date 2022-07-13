package it.eng.spagoLite.form.fields;

import it.eng.spagoCore.error.EMFError;
import it.eng.spagoLite.db.base.BaseRowInterface;
import it.eng.spagoLite.form.Component;
import it.eng.spagoLite.form.base.BaseElements;
import it.eng.spagoLite.form.fields.impl.CheckBox;
import it.eng.spagoLite.form.fields.impl.Input;
import it.eng.spagoLite.message.Message;
import it.eng.spagoLite.message.Message.MessageLevel;
import it.eng.spagoLite.message.MessageBox;
import java.io.File;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class Fields<T extends Field> extends BaseElements<T> {

    private static final long serialVersionUID = 1L;

    private static final int SIZE_THRESHOLD = 1000000;

    private Status status;

    public Fields(Component parent, String name, String description) {
        super(parent, name, description);
    }

    public void post(HttpServletRequest servletRequest) {
        if (ServletFileUpload.isMultipartContent(servletRequest)) {
            throw new IllegalArgumentException(
                    "La request è di tipo multipart/form-data utilizzare il metodo postMultipart(HttpServletRequest servletRequest)");
        }
        for (Field field : this) {
            field.post(servletRequest);
        }
    }

    /**
     *
     * Effettua una post multipart utilizzando come folder temporanea la cartella java.io.tmpdir
     *
     * @param servletRequest
     *            Http request
     * @param maxFileSize
     *            Sets the maximum allowed size of a single uploaded file
     * 
     * @return risultato elaborazione
     * 
     * @throws FileUploadException
     *             se la dimensione massima del file è stata raggiunta o si è verificato un errore di I/O.
     *
     */
    public String[] postMultipart(HttpServletRequest servletRequest, int maxFileSize) throws FileUploadException {
        return this.postMultipart(servletRequest, new File(System.getProperty("java.io.tmpdir")), maxFileSize);
    }

    /**
     *
     * @param servletRequest
     *            Http request
     * @param tempDirRepository
     *            Folder temporanea su cui avviene il deposito dei file più grandi di 1 MB
     * @param maxFileSize
     *            Sets the maximum allowed size of a single uploaded file
     * 
     * @return risultato elaborazione
     * 
     * @throws FileUploadException
     *             se la dimensione massima del file è stata raggiunta o si è verificato un errore di I/O.
     *
     */
    public String[] postMultipart(HttpServletRequest servletRequest, File tempDirRepository, int maxFileSize)
            throws FileUploadException {
        if (!ServletFileUpload.isMultipartContent(servletRequest)) {
            throw new IllegalArgumentException(
                    "La request non è di tipo multipart/form-data utilizzare il metodo post(HttpServletRequest servletRequest)");
        }
        String[] paramReturn = null;
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, FileItem> fileMap = new HashMap<String, FileItem>();
        // Create a factory for disk-based file items
        DiskFileItemFactory factory = new DiskFileItemFactory(SIZE_THRESHOLD, tempDirRepository);
        // Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload(factory);
        // maximum size before a FileUploadException will be thrown
        upload.setFileSizeMax(maxFileSize);

        for (FileItem item : upload.parseRequest(servletRequest)) {
            if (item.isFormField()) {
                String value = new String(item.get(), Charset.forName("UTF-8"));
                paramMap.put(item.getFieldName(), value);
                if (item.getFieldName().toLowerCase().startsWith("operation")) {
                    String[] tmp = StringUtils.split(item.getFieldName(), "_");
                    paramReturn = Arrays.copyOfRange(tmp, 1, tmp.length);
                }
            } else {
                String filename = item.getName();
                if (!StringUtils.isEmpty(filename)) {
                    paramMap.put(item.getFieldName(), filename);
                    fileMap.put(item.getFieldName(), item);
                }
            }
        }

        for (Field field : this) {
            if (field instanceof SingleValueField) {
                FileItem fi;
                SingleValueField svf = (SingleValueField) field;
                if (!svf.isReadonly() && svf.isEditMode()) {
                    if (svf instanceof CheckBox) {
                        svf.clear();
                        if (StringUtils.isNotBlank(paramMap.get(field.getName()))) {
                            ((CheckBox) svf).setChecked(true);
                        }
                    } else {
                        svf.setValue(paramMap.get(field.getName()));
                    }
                    if ((fi = fileMap.get(field.getName())) != null) {
                        Input svf2 = (Input) field;
                        svf2.setFileItem(fi);
                    }
                }
            }
        }
        return paramReturn;
    }

    /**
     * Compila il form con il contenuto del rowbean
     *
     * @param row
     *            oggetto bean
     * 
     * @throws EMFError
     */
    public void copyFromBean(BaseRowInterface row) throws EMFError {
        for (Field field : this) {
            if (field instanceof SingleValueField) {
                SingleValueField<?> singleValueField = (SingleValueField<?>) field;
                singleValueField.format(row.getObject(singleValueField.getAlias()));
            }
        }
    }

    /**
     * Compila il row bean con il contenuto del form
     *
     * @param row
     *            oggetto bean
     * 
     * @throws EMFError
     */
    public void copyToBean(BaseRowInterface row) throws EMFError {
        for (Field field : this) {
            if (field instanceof SingleValueField) {
                SingleValueField<?> singleValueField = (SingleValueField<?>) field;
                row.setObject(singleValueField.getAlias(), singleValueField.parse());
            }
        }
    }

    /**
     * Imposta l'edit mode per tutti i fields
     *
     */
    public void setEditMode() {
        for (Field field : this) {
            field.setEditMode();
        }
    }

    /**
     * Imposta il view mode per tutti i fields
     *
     */
    public void setViewMode() {
        for (Field field : this) {
            field.setViewMode();
        }
    }

    public boolean check() {
        boolean check = true;

        for (Field field : this) {
            check = check && field.check();
        }

        return check;
    }

    public boolean validate(MessageBox messageBox) {
        boolean check = true;

        for (Field field : this) {
            Message message = field.validate();

            if (message != null) {
                messageBox.addMessage(message);
                check = false;
            }
        }

        return check;
    }

    public boolean postAndValidate(HttpServletRequest servletRequest, MessageBox messageBox) {
        post(servletRequest);
        return validate(messageBox);
    }

    /* OVERLOAD DEI METODI asJSON */

    /**
     * Metodo che ritorna un JSONObject popolato con i dati del form e l'aggiunta di un eventuale Message visualizzato
     * in un popup JavaScript lato client
     * 
     * @param msg
     *            messaggio
     * 
     * @return json object
     * 
     * @throws EMFError
     */
    public JSONObject asJSON(Message msg) throws EMFError {
        JSONObject json = asJSON();
        if (msg != null) {
            try {
                MessageLevel level = msg.getMessageLevel();
                switch (level) {
                case ERR:
                    json.put("jsonErrorMessage", msg.getText());
                    break;
                case WAR:
                    json.put("jsonWarningMessage", msg.getText());
                    break;
                case INF:
                    json.put("jsonInfoMessage", msg.getText());
                    break;
                default:
                    break;
                }
            } catch (JSONException ex) {
                throw new EMFError(EMFError.ERROR, "Errore durante il settaggio dell'errore json", ex);
            }
        }
        return json;
    }

    /**
     * Metodo che ritorna un JSONObject popolato con i dati del form e l'aggiunta del nome di una funzione JavaScript da
     * eseguire lato client
     * 
     * @param funcName
     *            nome funzione
     * 
     * @return Json object
     * 
     * @throws EMFError
     */
    public JSONObject asJSON(String funcName) throws EMFError {
        JSONObject json = asJSON();
        try {
            json.put("jsonFunctionName", funcName);
        } catch (JSONException ex) {
            throw new EMFError(EMFError.ERROR, "Errore durante il settaggio dell'errore json", ex);
        }
        return json;
    }

    /**
     * Metodo che ritorna un JSONObject popolato con i dati del form e l'aggiunta di un eventuale Message visualizzato
     * in un popup JavaScript lato client e del nome di una funzione JavaScript da eseguire lato client dopo la
     * pressione del tasto OK sul messaggio
     * 
     * @param msg
     *            messaggio
     * @param funcName
     *            nome funzione
     * 
     * @return Json object
     * 
     * @throws EMFError
     */
    public JSONObject asJSON(Message msg, String funcName) throws EMFError {
        JSONObject json = asJSON(msg);
        try {
            json.put("jsonFunctionName", funcName);
        } catch (JSONException ex) {
            throw new EMFError(EMFError.ERROR, "Errore durante il settaggio dell'errore json", ex);
        }
        return json;
    }

    @Override
    public JSONObject asJSON() throws EMFError {
        JSONObject result = new JSONObject();
        try {
            result.put("name", getName());
            result.put("description", getDescription());
            JSONArray sons = new JSONArray();
            for (Component component : getComponentList()) {
                sons.put(component.asJSON());
            }
            result.put("map", sons);
            result.put("type", "Fields");
        } catch (JSONException e) {
            throw new EMFError(EMFError.ERROR, "Eccezione nella crezione dell'oggetto JSON", e);
        }
        return result;
    }

    public void reset() {
        for (Field field : this) {
            field.reset();
        }
    }

    public void clear() {
        for (Field field : this) {
            field.clear();
        }
    }

    public int compare(BaseRowInterface row, Field[] excludeList) throws EMFError {
        Set<Field> excludeSet = new HashSet<Field>();
        for (Field field : excludeList) {
            excludeSet.add(field);
        }

        int i = 0;
        for (Component component : this) {
            if (excludeSet.contains(component)) {
                continue;
            }

            if (component instanceof SingleValueField<?>) {
                SingleValueField<?> singleValueField = (SingleValueField<?>) component;

                if (!Objects.equals(singleValueField.parse(), row.getObject(singleValueField.getAlias()))) {
                    i++;
                }
            }
        }

        return i;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status mode) {
        this.status = mode;
    }
}
