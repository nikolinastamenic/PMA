package com.pma.server.service.serviceImpl;

import com.pma.server.Dto.ReportDto;
import com.pma.server.mappers.ReportMapper;
import com.pma.server.model.Report;
import com.pma.server.repository.ReportRepository;
import com.pma.server.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Override
    public ReportDto findReportById(Long id) {

        Report report = this.reportRepository.findReportById(id);
        return ReportMapper.toReportDtoWithItemsWithPictures(report);

    }
}
