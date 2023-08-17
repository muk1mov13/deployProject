package com.example.backend.service.fileService;

import org.springframework.http.HttpEntity;

import java.io.Serializable;
import java.util.List;

public interface FileService {
    <T> HttpEntity<? extends Serializable> createExcelFile(List<T> filteredObjects, List<String> columnNames, Class<?> clazz, boolean b);
}
