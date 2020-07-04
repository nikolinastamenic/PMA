package com.pma.server.controller;

import com.pma.server.Dto.NewReportItemDto;
import com.pma.server.Dto.NewReportItemItemDto;
import com.pma.server.Dto.ReportMysqlIdsDto;
import com.pma.server.service.ReportItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/report/item", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReportItemController {

    @Autowired
    private ReportItemService reportItemService;

    @PostMapping(value = "/new")
    public ResponseEntity<ReportMysqlIdsDto> newReportItem(@RequestBody NewReportItemDto newReportItemDto) {

        ReportMysqlIdsDto reportDtos = this.reportItemService.newOrUpdateReportItems(newReportItemDto);

        return new ResponseEntity<>(reportDtos, HttpStatus.OK);
    }


}
