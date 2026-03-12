package com.orderflow.mapper;

import com.orderflow.dto.UserDto;
import com.orderflow.entity.user.User;
import com.orderflow.entity.warehouse.Warehouse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "userWarehouse.warehouseId", target = "warehouseId")
    UserDto userToUserDto(User user);

    @Mapping(source = "warehouseId", target = "userWarehouse.warehouseId")
    User dtoToUser(UserDto userDto);

    List<UserDto> usersToUserDtos(List<User> users);
}
