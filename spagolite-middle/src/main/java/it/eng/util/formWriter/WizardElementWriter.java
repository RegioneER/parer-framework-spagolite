package it.eng.util.formWriter;

import java.io.IOException;
import java.io.Writer;

public interface WizardElementWriter {
    public void writeAdd(Writer writer) throws IOException;

    public void writeGet(Writer writer) throws IOException;

    public void writeConstantFull(Writer writer) throws IOException;

    public void writeConstant(Writer writer) throws IOException;
}
