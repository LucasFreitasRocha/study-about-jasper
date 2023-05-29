package com.jasper.study.service;


import com.jasper.study.dto.in.Field;
import com.jasper.study.dto.in.RequestDTO;
import com.jasper.study.dto.out.CustomMultipartFile;
import com.jasper.study.dto.out.ResponseDTO;
import jakarta.servlet.http.HttpServletResponse;
import jdk.jfr.ContentType;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class GenerateDocumentsService {


    public ResponseDTO genereteBase64(RequestDTO requestDTO) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            responseDTO.setFileBase64(Base64.getEncoder().encodeToString((generete(requestDTO))));
           // responseDTO.setFile(new CustomMultipartFile(arquivoPdf, "teste", "pdf"));
            return responseDTO;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void prepereDonwload(HttpServletResponse response, RequestDTO requestDTO){
        CustomMultipartFile customMultipartFile = new CustomMultipartFile(generete(requestDTO), "teste", "pdf");
        try {
            response.setHeader("Content-disposition", "attachment; filename="
                    + customMultipartFile.getOriginalFilename());
            response.setHeader("Cache-Control", "max-age=86400, public");
            response.setContentType("application/pdf");
            IOUtils.copy(customMultipartFile.getInputStream(), response.getOutputStream() );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] generete(RequestDTO requestDTO){
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            File file = ResourceUtils.getFile("classpath:templates/poc.jrxml");
            Map<String, Object> mapVariables = new HashMap<>();
            requestDTO.getFields().forEach(field -> buildMapVariables(field, mapVariables));
            JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
            JasperPrint print = JasperFillManager.fillReport(jasperReport, mapVariables,
                    new JRBeanCollectionDataSource(Collections.singleton(mapVariables)));
            return JasperExportManager.exportReportToPdf(print);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void buildMapVariables(Field field, Map<String, Object> mapVariables) {
        if (field.getKey().endsWith("_list")) {
            buildListMapVariables(field, mapVariables);
        } else {
            mapVariables.put(field.getKey(), field.getValue());
        }
    }

    private void buildListMapVariables(Field field, Map<String, Object> mapVariables) {
        List<String> newList;
        if (mapVariables.containsKey(field.getKey())) {
            newList = new ArrayList<>();
            List<Object> oldList = Arrays.asList(mapVariables.get(field.getKey()));
            oldList.forEach(oldValue -> newList.add(buildNewName(String.valueOf(oldValue))));
            newList.add(field.getValue());
            mapVariables.put(field.getKey(), newList);
        } else {
            newList = new ArrayList<>();
            newList.add(field.getValue());
            mapVariables.put(field.getKey(), newList);
        }
    }

    private String buildNewName(String oldValue) {
        String newValue = oldValue.replace("[", "");
        return newValue.replace("]", "");
    }
}
