package com.pma.server.service;

import com.pma.server.model.ReportItem;
import org.springframework.stereotype.Service;

@Service
public interface ReportItemService {

    ReportItem findReportItemById(Long id);


}