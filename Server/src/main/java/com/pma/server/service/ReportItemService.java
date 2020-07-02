package com.pma.server.service;

import com.pma.server.Dto.NewReportItemDto;
import com.pma.server.Dto.ReportMysqlIdsDto;
import com.pma.server.model.ReportItem;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReportItemService {

    ReportItem findReportItemById(Long id);
    ReportMysqlIdsDto newReportItem(NewReportItemDto newReportItemDtos);


}
