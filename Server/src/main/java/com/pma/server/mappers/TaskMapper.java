package com.pma.server.mappers;

import com.pma.server.Dto.AllTaskDto;
import com.pma.server.Dto.ApartmentDto;
import com.pma.server.model.Task;

public class TaskMapper {

    public static AllTaskDto toTaskDto(Task task){

        AllTaskDto taskDto = new AllTaskDto();

        taskDto.setApartmentDto(ApartmentMapper.toApartmentDto(task.getApartment()));
        taskDto.setDeadline(task.getDeadline());
        taskDto.setId(task.getId());
        taskDto.setState(task.getState());
        taskDto.setTypeOfApartment(task.getTypeOfApartment());
        taskDto.setUrgent(task.isUrgent());

        if(task.getReport() != null) {
            taskDto.setReportDto(ReportMapper.toReportDtoWithItemsWithPictures(task.getReport()));
        }
        if(task.getUser() != null) {
            taskDto.setUserDto(UserMapper.toUserDto(task.getUser()));
        }

        return taskDto;
    }

}
