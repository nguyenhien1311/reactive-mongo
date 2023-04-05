package com.example.mongo.mappers;

import com.example.mongo.domain.Customer;
import com.example.mongo.dto.CustomerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {
    CustomerDTO toDto(Customer customer);

    Customer toDomain(CustomerDTO dto);
}
