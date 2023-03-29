package com.jasper.study.service;


import com.jasper.study.dto.in.Field;
import com.jasper.study.dto.in.RequestDTO;
import com.jasper.study.dto.out.ResponseDTO;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.util.*;

@Service
public class GenerateDocumentsService {


    public ResponseDTO generete(RequestDTO requestDTO) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            File file = ResourceUtils.getFile("classpath:templates/poc.jrxml");
            Map<String, Object> mapVariables = new HashMap<>();
            requestDTO.getFields().forEach(field -> buildMapVariables(field, mapVariables));
            JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
            JasperPrint print = JasperFillManager.fillReport(jasperReport, mapVariables,
                    new JRBeanCollectionDataSource(Collections.singleton(mapVariables)));
            responseDTO.setFile(Base64Utils.encodeToString(JasperExportManager.exportReportToPdf(print)));
            return responseDTO;
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
