package com.pma.server.Dto;

import com.pma.server.model.Address;
import lombok.Data;

@Data
public class BuildingDto {

    private Long id;

    private Address address;

    public BuildingDto() {}

    public BuildingDto(Long id, Address address) {
        this.id = id;
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
