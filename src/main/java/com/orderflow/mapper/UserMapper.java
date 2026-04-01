package com.orderflow.mapper;

import com.orderflow.dto.UserDto;
import com.orderflow.entity.user.User;
import com.orderflow.entity.warehouse.Warehouse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "userWarehouse.warehouseId", target = "warehouseId")
    UserDto userToUserDto(User user);

    @Mapping(source = "warehouseId", target = "userWarehouse", qualifiedByName = "uuidToWarehouse")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User dtoToUser(UserDto userDto);

    List<UserDto> usersToUserDtos(List<User> users);

    @Named("uuidToWarehouse")
    default Warehouse uuidToWarehouse(UUID warehouseId) {
        if (warehouseId == null) {return null;}
        Warehouse warehouse = new Warehouse();
        warehouse.setWarehouseId(warehouseId);
        return warehouse;
    }
}
