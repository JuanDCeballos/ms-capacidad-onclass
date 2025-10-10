package co.onclass.api.utils;

import co.onclass.api.dto.capacidad.CapacidadRequestDto;
import co.onclass.api.dto.capacidad.CapacidadResponseDto;
import co.onclass.model.capacidad.Capacidad;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CapacidadMapper {

    Capacidad toCapacidad(CapacidadRequestDto capacidadRequestDto);

    CapacidadResponseDto toCapacidadResponseDto(Capacidad capacidad);
}
