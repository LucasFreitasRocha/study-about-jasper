package com.jasper.study.controller;

import com.jasper.study.dto.in.RequestDTO;
import com.jasper.study.dto.out.ResponseDTO;
import com.jasper.study.service.GenerateDocumentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/generate")
public class GenerateDocumentsController {

    @Autowired
    private GenerateDocumentsService service;

    @PostMapping
    public ResponseEntity<ResponseDTO> generete(@RequestBody RequestDTO requestDTO){
        return ResponseEntity.ok(service.generete(requestDTO));
    }
}
