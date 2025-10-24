package co.onclass.api.utils;

import co.onclass.api.dto.capacidad.CapacidadConTecnologiaResponseDto;
import co.onclass.api.dto.capacidad.CapacidadRequestDto;
import co.onclass.api.dto.capacidad.CapacidadResponseDto;
import co.onclass.model.capacidad.Capacidad;
import co.onclass.model.capacidad.CapacidadDetallada;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CapacidadMapper {

    Capacidad toCapacidad(CapacidadRequestDto capacidadRequestDto);

    CapacidadResponseDto toCapacidadResponseDto(Capacidad capacidad);

    @Mapping(source = "tecnologias", target = "tecnologias")
    CapacidadConTecnologiaResponseDto toCapacidadConTecnologias(CapacidadDetallada capacidadDetallada);
}
