package com.pma.server.mappers;

import com.pma.server.Dto.ReportDto;
import com.pma.server.Dto.ReportItemDto;
import com.pma.server.model.Report;
import com.pma.server.model.ReportItem;

import java.util.ArrayList;
import java.util.List;

public class ReportMapper {

    public static ReportDto toReportDto(Report report){
        ReportDto reportDto = new ReportDto();

        reportDto.setDate(report.getDate());
        reportDto.setId(report.getId());

        List<ReportItemDto> reportItemDtos = new ArrayList<>();

        for(ReportItem reportItem: report.getItemList()){
            ReportItemDto reportItemDto = ReportItemMapper.toReportItemDto(reportItem);
            reportItemDtos.add(reportItemDto);
        }
        reportDto.setItemList(reportItemDtos);

        return reportDto;
    }


    public static ReportDto toReportDtoWithItemsWithPictures(Report report){
        ReportDto reportDto = new ReportDto();

        reportDto.setDate(report.getDate());
        reportDto.setId(report.getId());

        List<ReportItemDto> reportItemDtos = new ArrayList<>();

        for(ReportItem reportItem: report.getItemList()){
            ReportItemDto reportItemDto = ReportItemMapper.toReportItemDtoWithPictures(reportItem);
            reportItemDtos.add(reportItemDto);
        }
        reportDto.setItemList(reportItemDtos);

        return reportDto;
    }

}
