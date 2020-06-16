package com.pma.server.controller;

import com.pma.server.Dto.ReportDto;
import com.pma.server.Dto.UserDto;
import com.pma.server.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/report", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReportController {

    @Autowired
    private ReportService reportService;


    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDto> getReportById(@PathVariable("id") Long id) {

        ReportDto reportDto = this.reportService.findReportById(id);
        return new ResponseEntity(reportDto, HttpStatus.OK);
    }

}
