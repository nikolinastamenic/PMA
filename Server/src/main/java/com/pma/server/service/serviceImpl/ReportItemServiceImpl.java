package com.pma.server.service.serviceImpl;

import com.pma.server.Dto.NewReportItemDto;
import com.pma.server.Dto.NewReportItemItemDto;
import com.pma.server.Dto.ReportMysqlIdsDto;
import com.pma.server.Dto.ReportMysqlIdsItemDto;
import com.pma.server.model.Report;
import com.pma.server.model.ReportItem;
import com.pma.server.model.Task;
import com.pma.server.repository.ReportItemRepository;
import com.pma.server.service.ReportItemService;
import com.pma.server.service.ReportService;
import com.pma.server.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.event.ListDataEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ReportItemServiceImpl implements ReportItemService {

    @Autowired
    private ReportItemRepository reportItemRepository;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ReportService reportService;

    @Override
    public ReportItem findReportItemById(Long id) {
        return this.reportItemRepository.findReportItemById(id);
    }


    @Override
    public ReportMysqlIdsDto newOrUpdateReportItems(NewReportItemDto newReportItemDtos) {

        ReportMysqlIdsDto reportMysqlIdsDto = new ReportMysqlIdsDto();
        List<ReportMysqlIdsItemDto> list = new ArrayList<>();
        for (NewReportItemItemDto newReportItemItemDto : newReportItemDtos.getNewReportItemItemDtoList()) {
            if (newReportItemItemDto.getMySqlReportItemId() == 0) {
                ReportMysqlIdsItemDto reportItemItemDto = newReportItems(newReportItemItemDto);
                list.add(reportItemItemDto);
            } else {
                ReportMysqlIdsItemDto reportItemItemDto = updateReportItems(newReportItemItemDto);
                list.add(reportItemItemDto);
            }
        }
        reportMysqlIdsDto.setReportMysqlIdsItemDtos(list);

        return reportMysqlIdsDto;
    }

    public ReportMysqlIdsItemDto updateReportItems(NewReportItemItemDto updateReportItemItemDto) {

        ReportItem reportItem = this.reportItemRepository.findReportItemById((long) updateReportItemItemDto.getMySqlReportItemId());

        reportItem.setFaultName(updateReportItemItemDto.getFaultName());
        reportItem.setDetails(updateReportItemItemDto.getDetails());
        reportItem.setPicture(updateReportItemItemDto.getPicture().getPictureName());

        this.reportItemRepository.save(reportItem);

        if(updateReportItemItemDto.getPicture() != null) {
            try (FileOutputStream stream = new FileOutputStream(new File("").getAbsolutePath() + "/pictures/" + updateReportItemItemDto.getPicture().getPictureName())) {
                stream.write(updateReportItemItemDto.getPicture().getPicture());

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        ReportMysqlIdsItemDto reportMysqlIdsItemDto = new ReportMysqlIdsItemDto();
        reportMysqlIdsItemDto.setReportItemMysqlId((long) updateReportItemItemDto.getMySqlReportItemId());
        reportMysqlIdsItemDto.setReportId(updateReportItemItemDto.getReportId());
        reportMysqlIdsItemDto.setReportItemId(updateReportItemItemDto.getReportItemId());
        reportMysqlIdsItemDto.setReportMysqlId((long) updateReportItemItemDto.getMySqlReportId());

        return reportMysqlIdsItemDto;

    }

    public ReportMysqlIdsItemDto newReportItems(NewReportItemItemDto newReportItemItemDto) {

        ReportMysqlIdsDto reportMysqlIdsDto = new ReportMysqlIdsDto();
        List<ReportMysqlIdsItemDto> list = new ArrayList<>();


        Task task = taskService.getTaskById(new Long(newReportItemItemDto.getMySqlTaskId()));
        ReportMysqlIdsItemDto reportMysqlIdsItemDto = new ReportMysqlIdsItemDto();

        ReportItem reportItem = new ReportItem(newReportItemItemDto.getFaultName(), newReportItemItemDto.getDetails(), newReportItemItemDto.getPicture().getPictureName());
        this.reportItemRepository.save(reportItem);

        if (newReportItemItemDto.getPicture().getPicture() != null) {
            try (FileOutputStream stream = new FileOutputStream(new File("").getAbsolutePath() + "/pictures/" + newReportItemItemDto.getPicture().getPictureName())) {
                stream.write(newReportItemItemDto.getPicture().getPicture());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Long reportId;
        if (task.getReport() == null) {

            Report report = new Report();
            DateFormat dateFormat = new SimpleDateFormat(
                    "EEE MMM dd HH:mm:ss zzz yyyy");
            try {
                Date date = dateFormat.parse(newReportItemItemDto.getReportCreatedDate());
                report.setDate(date);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            System.out.println(dateFormat.format(new Date()));


            List<ReportItem> reportItems = new ArrayList<>();
            reportItems.add(reportItem);
            report.setItemList(reportItems);

            reportId = this.reportService.save(report).getId();
            reportMysqlIdsItemDto.setReportMysqlId(reportId);
            task.setReport(report);
            this.taskService.save(task);
        } else {
            Report report = this.reportService.findReportById(task.getReport().getId());
            report.getItemList().add(reportItem);
            reportId = this.reportService.save(report).getId();
            reportMysqlIdsItemDto.setReportMysqlId(reportId);

        }

        reportMysqlIdsItemDto.setReportItemMysqlId(reportItem.getId());
        reportMysqlIdsItemDto.setReportId(newReportItemItemDto.getReportId());
        reportMysqlIdsItemDto.setReportItemId(newReportItemItemDto.getReportItemId());

        return reportMysqlIdsItemDto;
    }


}
