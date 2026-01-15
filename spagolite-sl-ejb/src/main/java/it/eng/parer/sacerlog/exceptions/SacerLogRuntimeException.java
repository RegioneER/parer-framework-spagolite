/*
 * Engineering Ingegneria Informatica S.p.A.
 *
 * Copyright (C) 2023 Regione Emilia-Romagna <p/> This program is free software: you can
 * redistribute it and/or modify it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the License, or (at your option)
 * any later version. <p/> This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Affero General Public License for more details. <p/> You should
 * have received a copy of the GNU Affero General Public License along with this program. If not,
 * see <https://www.gnu.org/licenses/>.
 */

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
