package it.eng.parer.sacerlog.exceptions;

import java.text.MessageFormat;

public class SacerLogRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 5497209347748993178L;

    private final SacerLogErrorCategory category;

    public SacerLogRuntimeException(SacerLogExceptionBuilder builder) {
        super(builder.message, builder.cause);
        this.category = builder.category;
    }

    public SacerLogErrorCategory getCategory() {
        return category;
    }

    public static SacerLogExceptionBuilder builder() {
        return new SacerLogExceptionBuilder();
    }

    @Override
    public String getMessage() {
        return "[" + getCategory().toString() + "]" + "  " + super.getMessage();
    }

    public static class SacerLogExceptionBuilder {

        private SacerLogErrorCategory category = SacerLogErrorCategory.INTERNAL_ERROR;
        private String message;
        private Throwable cause;

        public SacerLogRuntimeException build() {
            return new SacerLogRuntimeException(this);
        }

        public SacerLogExceptionBuilder category(SacerLogErrorCategory category) {
            this.setCategory(category);
            return this;
        }

        public SacerLogErrorCategory getCategory() {
            return category;
        }

        public void setCategory(SacerLogErrorCategory category) {
            this.category = category;
        }

        public SacerLogExceptionBuilder message(String messageToFormat, Object... args) {
            this.setMessage(MessageFormat.format(messageToFormat, args));
            return this;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public SacerLogExceptionBuilder cause(Throwable cause) {
            this.setCause(cause);
            return this;
        }

        public Throwable getCause() {
            return cause;
        }

        public void setCause(Throwable cause) {
            this.cause = cause;
        }

    }

    public enum SacerLogErrorCategory {

        INTERNAL_ERROR, LOGGER_ERROR, SQL_ERROR
    }

}
