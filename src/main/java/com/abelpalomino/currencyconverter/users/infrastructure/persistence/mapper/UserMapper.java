package com.abelpalomino.currencyconverter.users.infrastructure.persistence.mapper;

import com.abelpalomino.currencyconverter.users.domain.model.UserModel;
import com.abelpalomino.currencyconverter.users.infrastructure.persistence.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    UserEntity toEntity(UserModel domain);

    UserModel toModel(UserEntity entity);
}
