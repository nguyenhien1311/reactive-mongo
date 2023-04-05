package com.example.mongo.mappers;

import com.example.mongo.domain.Beer;
import com.example.mongo.dto.BeerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface BeerMapper {
    Beer toDomain(BeerDTO dto);

    BeerDTO toDto(Beer beer);
}
