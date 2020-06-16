package com.pma.server.service.serviceImpl;

import com.pma.server.model.ReportItem;
import com.pma.server.repository.ReportItemRepository;
import com.pma.server.service.ReportItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportItemServiceImpl implements ReportItemService {

    @Autowired
    private ReportItemRepository reportItemRepository;

    @Override
    public ReportItem findReportItemById(Long id) {
        return this.reportItemRepository.findReportItemById(id);
    }


}
