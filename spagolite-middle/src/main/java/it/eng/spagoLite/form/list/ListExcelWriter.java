/* ====================================================================
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
==================================================================== */
package it.eng.spagoLite.form.list;

import it.eng.spagoCore.error.EMFError;
import it.eng.spagoLite.db.base.BaseRowInterface;
import it.eng.spagoLite.form.fields.Field;
import it.eng.spagoLite.form.fields.Fields;
import it.eng.spagoLite.form.fields.MultiValueField;
import it.eng.spagoLite.form.fields.SingleValueField;
import it.eng.spagoLite.form.fields.impl.ComboBox;
import it.eng.spagoLite.form.fields.impl.Button;
import it.eng.spagoLite.util.Casting.Casting;
import it.eng.spagoLite.xmlbean.form.Field.Type;
import it.eng.spagoLite.form.fields.impl.MultiSelect;
//import it.eng.spagoLite.xmlbean.form.Button;

import java.io.OutputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * @author Enrico Grillini / Lorenzo Snidero
 */
public class ListExcelWriter {

    private List<?> list;
    private Fields<SingleValueField> fields;

    // private Workbook wb;
    // private Sheet sheet;
    private static CellStyle TITLE;
    private static CellStyle HEADER;
    private static CellStyle CELL;
    private static CellStyle CELL_DATE;
    private static CellStyle CELL_DATE_TIME;
    private static CellStyle CELL_DECIMAL_DOTCOMMA;
    private static CellStyle CELL_DECIMAL_DOT;

    public List<?> getList() {
        return list;
    }

    public void setList(List<?> list) {
        this.list = list;
    }

    public Fields<SingleValueField> getFields() {
        return fields;
    }

    public void setFields(Fields<SingleValueField> fields) {
        this.fields = fields;
    }

    public ListExcelWriter(List<?> list) {
        this.list = list;
        this.fields = list.getGenericFields();
    }

    private void addTitle(Sheet sheet) {
        Row titleRow = sheet.createRow(0);
        titleRow.setHeightInPoints(45);
        Cell titleCell = titleRow.createCell(0);
        if (StringUtils.isNotBlank(list.getTitle())) {
            titleCell.setCellValue(list.getTitle());
        } else {
            titleCell.setCellValue(list.getDescription());
        }
        titleCell.setCellStyle(TITLE);
        // sheet.addMergedRegion(CellRangeAddress.valueOf("$A$1:$L$1"));
    }

    private void addHeaders(Sheet sheet) {
        Row headerRow = sheet.createRow(1);

        int i = 0;
        for (Field field : getList()) {
            if (!field.isHidden()) {
                Cell headerCell = headerRow.createCell(i);
                headerCell.setCellValue(field.getDescription());
                headerCell.setCellStyle(HEADER);
                sheet.setColumnWidth(i, 10000);

                i++;
            }
        }
    }

    private void addCells(Sheet sheet) throws EMFError {
        int size = 0;

        int j = 2;
        for (BaseRowInterface row : getList().getTable()) {
            Row cellRow = sheet.createRow(j++);

            int i = 0;
            for (SingleValueField<?> field : getList()) {
                if (!field.isHidden()) {
                    size++;
                    field.format(row);

                    Cell cell = cellRow.createCell(i++);

                    if (field instanceof ComboBox) {
                        Casting.setToExcel(cell, field.getDecodedValue(), Type.STRING);
                    } else {
                        Casting.setToExcel(cell, field.getValue(), field.getType());
                    }

                    if (field.getType().equals(Type.DATE)) {
                        cell.setCellStyle(CELL_DATE);
                    } else if (field.getType().equals(Type.DATETIME)) {
                        cell.setCellStyle(CELL_DATE_TIME);
                    } else if (field.getType().equals(Type.DECIMAL)) {
                        if (field.getFormat().equals("#,##0.00")) {
                            cell.setCellStyle(CELL_DECIMAL_DOTCOMMA);
                        } else {
                            cell.setCellStyle(CELL_DECIMAL_DOT);
                        }
                    } else {
                        cell.setCellStyle(CELL);
                    }

                }
            }
        }

        for (int i = 0; i < size; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private void addHeadersForFields(Sheet sheet) {

        int i = 1;
        for (Field field : getFields()) {
            if (!(field instanceof Button)) {
                Row headerRow = sheet.createRow(i);
                if (!field.isHidden()) {
                    Cell headerCell = headerRow.createCell(0);
                    headerCell.setCellValue(field.getDescription());
                    headerCell.setCellStyle(HEADER);
                    sheet.setColumnWidth(i, 10000);

                    i++;
                }
            }
        }
    }

    private void addCellsForFields(Sheet sheet) throws EMFError {
        int i = 1;
        int size = 0;

        for (Field field : getFields()) {
            if (!(field instanceof Button)) {
                Row cellRow = sheet.getRow(i);
                if (!field.isHidden()) {
                    size++;
                    Cell cell = cellRow.createCell(1);

                    if (field instanceof ComboBox) {
                        Casting.setToExcel(cell, ((SingleValueField<?>) field).getDecodedValue(), Type.STRING);
                    } else if (field instanceof MultiSelect) {
                        Casting.setToExcelMultiValue(cell, ((MultiValueField<?>) field).getDecodedValues(),
                                Type.STRING);
                    } else {
                        Casting.setToExcel(cell, ((SingleValueField<?>) field).getValue(), field.getType());
                    }

                    if (field.getType().equals(Type.DATE)) {
                        cell.setCellStyle(CELL_DATE);
                    } else if (field.getType().equals(Type.DATETIME)) {
                        cell.setCellStyle(CELL_DATE_TIME);
                    } else {
                        cell.setCellStyle(CELL);
                    }

                    i++;
                }
            }
        }

        for (int l = 0; l < size; l++) {
            sheet.autoSizeColumn(l);
        }
    }

    private void addTitleForFields(Sheet sheet) {
        Row titleRow = sheet.createRow(0);
        titleRow.setHeightInPoints(45);
        Cell titleCell = titleRow.createCell(0);
        if (StringUtils.isNotBlank(fields.getName())) {
            titleCell.setCellValue(fields.getName());
        } else {
            titleCell.setCellValue(fields.getDescription());
        }
        titleCell.setCellStyle(TITLE);
    }

    public void write(OutputStream outputStream) throws Exception {
        Workbook wb = new HSSFWorkbook();
        createStyles(wb);

        // Creo il foglio
        Sheet sheet = wb.createSheet("Elenco");
        PrintSetup printSetup = sheet.getPrintSetup();
        printSetup.setLandscape(true);
        sheet.setFitToPage(true);
        sheet.setHorizontallyCenter(true);

        addHeaders(sheet);
        addCells(sheet);
        addTitle(sheet);

        Fields fields = list.getGenericFields();
        if (fields != null) {
            // Creo il foglio dei "Fields"
            Sheet sheet2 = wb.createSheet("Filtri");
            PrintSetup printSetup2 = sheet2.getPrintSetup();
            printSetup2.setLandscape(true);
            sheet2.setFitToPage(true);
            sheet2.setHorizontallyCenter(true);

            addHeadersForFields(sheet2);
            addCellsForFields(sheet2);
            addTitleForFields(sheet2);

            sheet2.createFreezePane(0, 1);
        }

        sheet.createFreezePane(0, 2);
        wb.write(outputStream);
    }

    /**
     * Create a library of cell styles
     */
    private static void createStyles(Workbook wb) {
        // Titolo
        Font titleFont = wb.createFont();
        titleFont.setFontHeightInPoints((short) 18);
        titleFont.setBold(true);
        TITLE = wb.createCellStyle();
        TITLE.setAlignment(HorizontalAlignment.LEFT);
        // TITLE.setAlignment(CellStyle.ALIGN_LEFT);
        TITLE.setVerticalAlignment(VerticalAlignment.CENTER);
        // TITLE.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        TITLE.setFont(titleFont);

        // Intestazione
        Font monthFont = wb.createFont();
        monthFont.setFontHeightInPoints((short) 11);
        monthFont.setColor(IndexedColors.WHITE.getIndex());
        HEADER = wb.createCellStyle();
        // HEADER.setAlignment(CellStyle.ALIGN_CENTER);
        // HEADER.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        HEADER.setAlignment(HorizontalAlignment.LEFT);
        HEADER.setVerticalAlignment(VerticalAlignment.CENTER);
        HEADER.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        HEADER.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        // HEADER.setFillPattern(CellStyle.SOLID_FOREGROUND);
        HEADER.setFont(monthFont);
        HEADER.setWrapText(true);

        // Celle
        CELL = wb.createCellStyle();
        // CELL.setAlignment(CellStyle.ALIGN_CENTER);
        CELL.setWrapText(true);
        CELL.setBorderRight(BorderStyle.THIN);
        // CELL.setBorderRight(CellStyle.BORDER_THIN);
        CELL.setRightBorderColor(IndexedColors.BLACK.getIndex());
        CELL.setBorderLeft(BorderStyle.THIN);
        // CELL.setBorderLeft(CellStyle.BORDER_THIN);
        CELL.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        CELL.setBorderTop(BorderStyle.THIN);
        // CELL.setBorderTop(CellStyle.BORDER_THIN);
        CELL.setTopBorderColor(IndexedColors.BLACK.getIndex());
        CELL.setBorderBottom(BorderStyle.THIN);
        // CELL.setBorderBottom(CellStyle.BORDER_THIN);
        CELL.setBottomBorderColor(IndexedColors.BLACK.getIndex());

        DataFormat df = wb.createDataFormat();

        CELL_DATE = wb.createCellStyle();
        // CELL.setAlignment(CellStyle.ALIGN_CENTER);
        CELL_DATE.setWrapText(true);
        CELL_DATE.setDataFormat(df.getFormat("dd/mm/yyyy"));
        CELL_DATE.setBorderRight(BorderStyle.THIN);
        CELL_DATE.setRightBorderColor(IndexedColors.BLACK.getIndex());
        CELL_DATE.setBorderLeft(BorderStyle.THIN);
        CELL_DATE.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        CELL_DATE.setBorderTop(BorderStyle.THIN);
        CELL_DATE.setTopBorderColor(IndexedColors.BLACK.getIndex());
        CELL_DATE.setBorderBottom(BorderStyle.THIN);
        CELL_DATE.setBottomBorderColor(IndexedColors.BLACK.getIndex());

        CELL_DATE_TIME = wb.createCellStyle();
        // CELL.setAlignment(CellStyle.ALIGN_CENTER);
        CELL_DATE_TIME.setWrapText(true);
        CELL_DATE_TIME.setDataFormat(df.getFormat("dd/mm/yyyy HH:mm"));
        CELL_DATE_TIME.setBorderRight(BorderStyle.THIN);
        CELL_DATE_TIME.setRightBorderColor(IndexedColors.BLACK.getIndex());
        CELL_DATE_TIME.setBorderLeft(BorderStyle.THIN);
        CELL_DATE_TIME.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        CELL_DATE_TIME.setBorderTop(BorderStyle.THIN);
        CELL_DATE_TIME.setTopBorderColor(IndexedColors.BLACK.getIndex());
        CELL_DATE_TIME.setBorderBottom(BorderStyle.THIN);
        CELL_DATE_TIME.setBottomBorderColor(IndexedColors.BLACK.getIndex());

        CELL_DECIMAL_DOTCOMMA = wb.createCellStyle();
        // CELL.setAlignment(CellStyle.ALIGN_CENTER);
        CELL_DECIMAL_DOTCOMMA.setWrapText(true);
        CELL_DECIMAL_DOTCOMMA.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));
        CELL_DECIMAL_DOTCOMMA.setBorderRight(BorderStyle.THIN);
        CELL_DECIMAL_DOTCOMMA.setRightBorderColor(IndexedColors.BLACK.getIndex());
        CELL_DECIMAL_DOTCOMMA.setBorderLeft(BorderStyle.THIN);
        CELL_DECIMAL_DOTCOMMA.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        CELL_DECIMAL_DOTCOMMA.setBorderTop(BorderStyle.THIN);
        CELL_DECIMAL_DOTCOMMA.setTopBorderColor(IndexedColors.BLACK.getIndex());
        CELL_DECIMAL_DOTCOMMA.setBorderBottom(BorderStyle.THIN);
        CELL_DECIMAL_DOTCOMMA.setBottomBorderColor(IndexedColors.BLACK.getIndex());

        CELL_DECIMAL_DOT = wb.createCellStyle();
        // CELL.setAlignment(CellStyle.ALIGN_CENTER);
        CELL_DECIMAL_DOT.setWrapText(true);
        CELL_DECIMAL_DOT.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
        CELL_DECIMAL_DOT.setBorderRight(BorderStyle.THIN);
        CELL_DECIMAL_DOT.setRightBorderColor(IndexedColors.BLACK.getIndex());
        CELL_DECIMAL_DOT.setBorderLeft(BorderStyle.THIN);
        CELL_DECIMAL_DOT.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        CELL_DECIMAL_DOT.setBorderTop(BorderStyle.THIN);
        CELL_DECIMAL_DOT.setTopBorderColor(IndexedColors.BLACK.getIndex());
        CELL_DECIMAL_DOT.setBorderBottom(BorderStyle.THIN);
        CELL_DECIMAL_DOT.setBottomBorderColor(IndexedColors.BLACK.getIndex());

    }
}
