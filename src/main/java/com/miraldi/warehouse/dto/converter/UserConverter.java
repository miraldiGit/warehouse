package com.miraldi.warehouse.dto.converter;

import com.miraldi.warehouse.dto.userDto.CreateUserDto;
import com.miraldi.warehouse.dto.userDto.UpdateUserDto;
import com.miraldi.warehouse.dto.userDto.UserDetailsDto;
import com.miraldi.warehouse.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper
public interface UserConverter {


    UserDetailsDto convertUserToUserDetailsDto(User user);

    User convertCreateUserDtoToUser(CreateUserDto createUserDto);

    void convertUpdateUserDtoToUser(UpdateUserDto updateUserDto, @MappingTarget User user);

}