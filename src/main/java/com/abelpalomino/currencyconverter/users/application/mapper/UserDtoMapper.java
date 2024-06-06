package com.abelpalomino.currencyconverter.users.application.mapper;

import com.abelpalomino.currencyconverter.users.application.dto.user.UserBodyDto;
import com.abelpalomino.currencyconverter.users.application.dto.user.UserDto;
import com.abelpalomino.currencyconverter.users.domain.model.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserDtoMapper {
    UserDto toDto(UserModel user);

    UserModel toModel(UserBodyDto userBody);
}
