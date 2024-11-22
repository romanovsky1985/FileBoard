package my.fileboard.mapper;

import my.fileboard.dto.FileDTO;
import my.fileboard.entity.FileEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class FileMapper {
    public abstract FileDTO map(FileEntity entity);
}