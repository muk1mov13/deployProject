package com.example.backend.service.fileService;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileServiceImpl implements FileService{
    @Override
    public <T> HttpEntity<? extends Serializable> createExcelFile(List<T> filteredObjects, List<String> columnNames, Class<?> clazz,boolean x) {
        try {

            columnNames = adjustConstrainedFields(columnNames);
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet();
            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            cellStyle.getVerticalAlignment();
            generateHeaderRow(filterColumnNames(columnNames,clazz),sheet.createRow(0),cellStyle);
            generateRows(filteredObjects,columnNames,sheet,cellStyle,x);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            workbook.close();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "customer_categories.xlsx");
            return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);
        }
        catch(Exception e){
            return ResponseEntity.ok("The excel file could not be created !");
        }
    }
    public List<String > adjustConstrainedFields(List<String> columnNames){
        List<String> constrainedColumns = List.of("supervisor");
        List<String> result = new ArrayList<>();
        for (String columnName : columnNames) {
            if(constrainedColumns.contains(columnName)){
                result.add(columnName+"Id");
            }else {
                result.add(columnName);
            }
        }
        return result;
    }
    public List<String> filterColumnNames(List<String> columnNames,Class<?> clazz) {
        List<String> fieldNames = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()){
            if(columnNames.contains(field.getName())) {
                fieldNames.add(field.getName());
            }
        }
        return fieldNames;
    }

    private void generateHeaderRow(List<String> columnNames, Row row, CellStyle cellStyle) {
        row.setHeightInPoints((short) 30);
        for (int i = 0; i < columnNames.size(); i++) {
            Cell cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(columnNames.get(i));
        }
    }

    private <T> void generateRows(List<T> filteredObjects, List<String> columnNames, Sheet sheet, CellStyle cellStyle, boolean x) {
        for (int i = 0; i < filteredObjects.size(); i++) {
            generateRowFromData(filteredObjects.get(i),sheet.createRow(i+1),columnNames,cellStyle,x,sheet);
        }
    }

    private <T> void generateRowFromData(T filteredObject, Row row, List<String> columnNames, CellStyle cellStyle,  boolean x,Sheet sheet) {
        row.setHeightInPoints((short) 20);
        for (int i = 0; i < columnNames.size(); i++) {
            Cell cell = row.createCell(i);
            sheet.autoSizeColumn(i);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(invokeMethod(filteredObject,columnNames.get(i),x));
        }
    }


    public <T> String invokeMethod(T filteredObject, String columnName, boolean x){
        try {
            System.out.println(columnName);
            Method method = filteredObject.getClass().getMethod(columnName.equals("active") && !x ?"is": "get" +
                                                                                                         columnName.substring(0,1).toUpperCase()+columnName.substring(1));
            Object[] args = {};
            Object result = method.invoke(filteredObject, args);
           return result.toString().equals("true")?"active":result.toString().equals("false")?"no active":result.toString();
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            return "";
        }
    }
}
