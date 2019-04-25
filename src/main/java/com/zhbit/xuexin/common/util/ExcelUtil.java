package com.zhbit.xuexin.common.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;

@Component
public class ExcelUtil {

    public static Workbook getWorkbookFromFile(MultipartFile file) {
        Workbook wb = null;
        InputStream in = null;
        String fileName = file.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        try {
            in = file.getInputStream();
            if (".xlsx".equalsIgnoreCase(suffix)) { // 2007+
                wb = WorkbookFactory.create(in);
            } else {
                wb = new HSSFWorkbook(in); // below the 2007
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return wb;
    }

//    public static boolean validateData(Row row, int begin, int end) {
//        int count = 0;
//        for (int i = begin; i <= end; i++) {
//            String cellVal = getStringCellValue(row.getCell(i));
//            if (StringUtils.isEmpty(cellVal)) {
//                count++;
//            }
//        }
//        return (count == 0 || count == end - begin + 1);
//    }

    public static boolean isFull(Row trRow, int firstIndex, int lastIndex) {
        int i = 0;
        for (i = firstIndex; i <= lastIndex; i++) {
            String cellVal = getStringCellValue(trRow.getCell(i));
            if (StringUtils.isEmpty(cellVal))
                break;
        }
        return (i == lastIndex + 1);
//        if (i != lastIndex + 1)
//            return false;
//        else
//            return true;
    }

    public static String getStringCellValue(Cell cell) {
        if (cell != null) {
            String cellTypeName = cell.getCellTypeEnum().name();
            if ("STRING".equals(cellTypeName)) {
                return String.valueOf(cell.getStringCellValue()).trim();
            } else if ("NUMERIC".equals(cellTypeName)) {
                // numeric trim '.'
                String cellStr = String.valueOf(cell.getNumericCellValue());
                return cellStr.substring(0, cellStr.lastIndexOf("."));
            } else if ("BOOLEAN".equals(cellTypeName)) {
                return String.valueOf(cell.getBooleanCellValue());
            } else
                return "";
        }
        return "";
    }

}
