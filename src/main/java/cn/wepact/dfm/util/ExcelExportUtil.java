package cn.wepact.dfm.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author wangbin
 */
public class ExcelExportUtil {
    /**
     * 通过excel模版填充数据
     */
    public static void exportExcelByTemplate(List<Object> objects, InputStream template, File targetFile) throws IOException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        if (ObjectUtils.isEmpty(objects)) {
            return;
        }
        ExcelSheet excelSheet = objects.get(0).getClass().getDeclaredAnnotation(ExcelSheet.class);
        Workbook workbook = new XSSFWorkbook(template);
        Sheet sheet;
        if (excelSheet.index() > -1) {
            sheet = workbook.getSheetAt(excelSheet.index());
        } else {
            sheet = workbook.getSheet(excelSheet.name());
        }
        int startRow = Math.max(excelSheet.headRowCount(), 0);

        CellStyle cellStyle = ExcelExportUtil.getStyle(workbook, false, false);
        for (int i = 0; i < objects.size(); i++) {
            Object object = objects.get(i);
            Field[] fields = object.getClass().getDeclaredFields();
            Row row = sheet.createRow(startRow + i);
            for (Field field : fields) {
                ExcelField excelField = field.getDeclaredAnnotation(ExcelField.class);
                if (excelField == null) {
                    continue;
                }
                String fieldName = field.getName();
                String methodName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                Method m = object.getClass().getMethod("get" + methodName);
                Object value = m.invoke(object);
                Cell cell = row.createCell(excelField.column());
                cell.setCellStyle(cellStyle);
                setCellValue(cell, value);
            }
        }
        File file = new File(targetFile.getPath());
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        FileOutputStream fos = new FileOutputStream(targetFile.getPath());
        workbook.write(fos);
        fos.close();
    }

    private static void setCellValue(Cell cell, Object value) {
        if (ObjectUtils.isEmpty(value)) {
            return;
        }
        if (value instanceof String) {
            cell.setCellValue(String.valueOf(value));
        } else if (value instanceof Integer) {
            cell.setCellValue(Double.parseDouble(String.valueOf(value)));
        } else if (value instanceof Long) {
            cell.setCellValue(Double.parseDouble(String.valueOf(value)));
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else if (value instanceof Date) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/M/d");
            String format = sdf.format(value);
            cell.setCellValue(format);
        } else {
            throw new RuntimeException("Excel导出失败，cell类型为:" + cell.getCellTypeEnum().name() + ",值是:" + value);
        }
    }

    public static CellStyle getStyle(Workbook workbook, boolean borderRed, boolean fontRed) {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFillPattern(FillPatternType.NO_FILL);
        cellStyle.setLocked(false);

        if (fontRed) {
            Font font = workbook.createFont();
            font.setColor(Font.COLOR_RED);
            cellStyle.setFont(font);
        }

        if (borderRed) {
            BorderStyle borderStyle = BorderStyle.MEDIUM;
            cellStyle.setBorderTop(borderStyle);
            cellStyle.setBorderBottom(borderStyle);
            cellStyle.setBorderLeft(borderStyle);
            cellStyle.setBorderRight(borderStyle);

            short borderColor = IndexedColors.RED.getIndex();
            cellStyle.setTopBorderColor(borderColor);
            cellStyle.setBottomBorderColor(borderColor);
            cellStyle.setLeftBorderColor(borderColor);
            cellStyle.setRightBorderColor(borderColor);
        }

        return cellStyle;
    }
}
