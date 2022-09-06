package tech.outspace.papershare.model.dto;

import org.mapstruct.*;
import tech.outspace.papershare.model.entity.objs.User;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface UserMapper {

    User toUser(UserDto userDto);

    UserDto toDto(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromUserDto(UserDto userDto, @MappingTarget User user);
}
