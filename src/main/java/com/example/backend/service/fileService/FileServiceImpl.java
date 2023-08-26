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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FileServiceImpl implements FileService{
    @Override
    public <T> HttpEntity<? extends Serializable> createExcelFile(List<T> filteredObjects, List<String> columnNames, Class<?> clazz,boolean x) {
        try {
            columnNames = adjustConstrainedFields(columnNames);
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet();
            Font font = workbook.createFont();
            CellStyle headerCellStyle = workbook.createCellStyle();
            font.setColor(IndexedColors.WHITE.getIndex());
            headerCellStyle.setFont(font);
            headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
            headerCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            List<String> filteredColumnNames = filterColumnNames(columnNames,clazz);
            Map<String,Integer> cellLength = new HashMap<>();
            generateHeaderRow(filteredColumnNames,sheet.createRow(0),headerCellStyle,sheet,cellLength);
            generateRows(filteredObjects,filteredColumnNames,sheet,cellStyle,x,cellLength);
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
    public List<String > adjustConstrainedFields(List<String> columnNames) {
        List<String> constrainedColumns = List.of("supervisor");
        List<String> result = new ArrayList<>();
        for (String columnName : columnNames) {
            if (!constrainedColumns.contains(columnName)) {
                result.add(columnName);
            }
        }
        return result;
    }
    public List<String> filterColumnNames(List<String> columnNames,Class<?> clazz) {
        for (Field field : clazz.getDeclaredFields()){
            if(!columnNames.contains(field.getName())) {
                columnNames.remove(field.getName());
            }
        }
        return columnNames;
    }
    private void generateHeaderRow(List<String> columnNames, Row row, CellStyle cellStyle, Sheet sheet, Map<String, Integer> cellLength) {
        for (int i = 0; i < columnNames.size(); i++) {
            Cell cell = row.createCell(i);
            cellStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(columnNames.get(i).replace('_',' '));
            sheet.setColumnWidth(i,updateCellLength(cellLength,columnNames.get(i),columnNames.get(i).length() + 6) * 256 );
        }
    }
    private <T> void generateRows(List<T> filteredObjects, List<String> columnNames, Sheet sheet, CellStyle cellStyle, boolean x, Map<String, Integer> cellLength) {
        for (int i = 0; i < filteredObjects.size(); i++) {
            generateRowFromData(filteredObjects.get(i),sheet.createRow(i+1),columnNames,cellStyle,x,sheet,cellLength);
        }
    }
    private <T> void generateRowFromData(T filteredObject, Row row, List<String> columnNames, CellStyle cellStyle, boolean x, Sheet sheet, Map<String, Integer> cellLength) {
        Map<String ,String > response;
        for (int i = 0; i < columnNames.size(); i++) {
            Cell cell = row.createCell(i);
            response = invokeMethod(filteredObject,columnNames.get(i),x);
            cell.setCellStyle(cellStyle);
//            Class<?> clazz = Class.forName(response.get("type"));
            cell.setCellValue(response.get("result"));
            sheet.setColumnWidth(i,updateCellLength(cellLength,columnNames.get(i),response.get("result").length()) * 256);
        }
    }
    public int updateCellLength(Map<String, Integer> cellLength, String column, int resultLength){
        if(cellLength.containsKey(column) && resultLength >= cellLength.get(column)) {
            cellLength.put(column,resultLength);
        }else if(!cellLength.containsKey(column)) {
            cellLength.put(column, resultLength);
        }else {
            resultLength = cellLength.get(column);
        }
        return resultLength;
    }
    public <T> Map<String ,String > invokeMethod(T filteredObject, String columnName, boolean x){
        Map<String ,String > response = new HashMap<>();
        try {
            Method method = filteredObject.getClass().getMethod(columnName.equals("active") && !x ?"is": "get" +
                    columnName.substring(0,1).toUpperCase()+columnName.substring(1));
            Object[] args = {};
            Object result = method.invoke(filteredObject, args);
            response.put("type",method.getReturnType().toString());
            response.put("result",result.toString().equals("true")?"active":result.toString().equals("false")?"no active":result.toString());
            return response;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            response.put("type","class.java.lang.String");
            response.put("result","");
            return response;
        }
    }
}
