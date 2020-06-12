package com.pma.server.mappers;

import com.pma.server.Dto.PictureDto;
import com.pma.server.Dto.ReportItemDto;
import com.pma.server.model.ReportItem;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ReportItemMapper {

    public static ReportItemDto toReportItemDto(ReportItem reportItem){

        ReportItemDto reportItemDto = new ReportItemDto();
        reportItemDto.setId(reportItem.getId());
        reportItemDto.setFaultName(reportItem.getFaultName());
        reportItemDto.setDetails(reportItem.getDetails());

        reportItemDto.setPicture(reportItem.getPicture());
//        PictureDto pictureDto = new PictureDto();
//
//        if(reportItem.getPicture() != null) {
//            pictureDto.setPictureName(reportItem.getPicture());
//
//        }
//        reportItemDto.setPicture(pictureDto);

        return reportItemDto;

    }

    public static ReportItemDto toReportItemDtoWithPictures(ReportItem reportItem) {

        ReportItemDto reportItemDto = new ReportItemDto();
        reportItemDto.setId(reportItem.getId());
        reportItemDto.setFaultName(reportItem.getFaultName());
        reportItemDto.setDetails(reportItem.getDetails());
        PictureDto pictureDto = new PictureDto();

        if(reportItem.getPicture() != null){
            pictureDto.setPictureName(reportItem.getPicture());

            String filePath = new File("").getAbsolutePath();
            filePath = filePath.concat("/pictures/");
            File in = new File(filePath + pictureDto.getPictureName());
//            try {
//                pictureDto.setPicture(IOUtils.toByteArray(new FileInputStream(in)));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
    //        reportItemDto.setPicture(pictureDto);

            reportItemDto.setPicture(reportItem.getPicture());
        }
        return reportItemDto;
    }

}
