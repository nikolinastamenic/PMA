package com.pma.server.service;

import com.pma.server.Dto.ReportDto;
import com.pma.server.model.Report;
import org.springframework.stereotype.Service;

@Service
public interface ReportService {

    ReportDto findReportDtoById(Long id);
    Report findReportById(Long id);
    Report save(Report report);


}
