package com.miraldi.warehouse.controllers;

import com.miraldi.warehouse.dto.userDto.ChangeUserPasswordDto;
import com.miraldi.warehouse.dto.userDto.CreateUserDto;
import com.miraldi.warehouse.dto.userDto.UpdateUserDto;
import com.miraldi.warehouse.dto.userDto.UserDetailsDto;
import com.miraldi.warehouse.security.SecurityUtils;
import com.miraldi.warehouse.services.AccessDeniedException;
import com.miraldi.warehouse.services.ServiceTokenGenerator;
import com.miraldi.warehouse.services.ServiceUser;
import com.miraldi.warehouse.services.ServiceUser.UserRequestFilter;
import com.miraldi.warehouse.utils.PageableUtil;
import com.miraldi.warehouse.utils.Role;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static com.miraldi.warehouse.utils.PathsAndStrings.BASE_USER_PATH;
import static com.miraldi.warehouse.utils.PathsAndStrings.CHANGE_PASSWORD_PATH;
import static com.miraldi.warehouse.utils.PathsAndStrings.REFRESH_TOKEN_PATH;
import static com.miraldi.warehouse.utils.PathsAndStrings.USER_PATH_VARIABLE;
import static com.miraldi.warehouse.utils.Role.SYSTEM_ADMIN;

@Tag(name = "User Controller", description = "Operations related to user management")
@RestController
@RequestMapping(BASE_USER_PATH)
public class UserController {

    private final ServiceUser serviceUser;
    private final PageableUtil pageableUtil;

    public UserController(ServiceUser serviceUser,
                          PageableUtil pageableUtil){
        this.serviceUser = serviceUser;
        this.pageableUtil = pageableUtil;
    }

    @GetMapping
    @Operation(summary = "Search for users", description = "Filter and sort users based on different fields.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved users")
    @ResponseStatus(HttpStatus.OK)
    public Page<UserDetailsDto> searchUsers(@RequestParam(value = "role", required = false)
                                            Role role,
                                            @RequestParam(value = "username", required = false)
                                            String username,
                                            @RequestParam(value = "lastName", required = false)
                                            String lastName,
                                            @RequestParam(value = "firstName", required = false)
                                            String firstName,
                                            @RequestParam(value = "email", required = false)
                                            String email,
                                            @RequestParam(value = "city", required = false)
                                            String city,
                                            @RequestParam(value = "postalCode", required = false)
                                            Integer postalCode,
                                            @RequestParam(value = "country", required = false)
                                            String country,
                                            @RequestParam(value = "sortBy", required = false)
                                            String sortBy,
                                            @RequestParam(value="sortDirection", defaultValue = "ASC")
                                            Sort.Direction sortDirection,
                                            Pageable pageable){

        var requestFilter = new UserRequestFilter();
        requestFilter.setRole(role);
        requestFilter.setUsername(username);
        requestFilter.setLastName(lastName);
        requestFilter.setFirstName(firstName);
        requestFilter.setEmail(email);
        requestFilter.setCity(city);
        requestFilter.setPostalCode(postalCode);
        requestFilter.setCountry(country);

        Sort sort;
        if (sortBy != null) {
            sort = Sort.by(new Sort.Order(sortDirection, sortBy));
        } else {
            sort = Sort.unsorted();
        }
        Pageable pageRequest = pageableUtil.getPageable(pageable, sort);

        if (!SecurityUtils.loggedUser().getRole().equals(SYSTEM_ADMIN)){
            throw new AccessDeniedException();
        }
        return serviceUser.searchUsers(requestFilter, pageRequest);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create user", description = "Create users with different roles.")
    @ApiResponse(responseCode = "201", description = "Successfully created user")
    public UserDetailsDto addUser(@Valid @RequestBody CreateUserDto createUserDto){
        return serviceUser.createUser(createUserDto);
    }

    @PutMapping(USER_PATH_VARIABLE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update user", description = "Update a user.")
    @ApiResponse(responseCode = "200", description = "Successfully updated user")
    public void updateUser(@PathVariable("user-id") Long userId,
                           @Valid @RequestBody UpdateUserDto updateUserDto){
        serviceUser.updateUser(userId, updateUserDto);
    }

    @PutMapping(CHANGE_PASSWORD_PATH)
    @Operation(summary = "Change the user password", description = "The user can change his password.")
    @ApiResponse(responseCode = "200", description = "Successfully changed password")
    public ResponseEntity<String> changePassword(@Valid @RequestBody ChangeUserPasswordDto changeUserPasswordDto){
        if (!(SecurityUtils.loggedUser().getUsername().equals(changeUserPasswordDto.getUsernameOrEmail())
                || SecurityUtils.loggedUser().getEmail().equals(changeUserPasswordDto.getUsernameOrEmail()))){
            throw new AccessDeniedException();
        }
        serviceUser.changeUserPassword(changeUserPasswordDto);
        return new ResponseEntity<>("Password Changed Successfully!",
                new HttpHeaders(), HttpStatus.OK);
    }

    @DeleteMapping(USER_PATH_VARIABLE)
    @Operation(summary = "Deletes a user", description = "Deletes a user.")
    public void deleteUser(@PathVariable("user-id") Long userId){
        serviceUser.deleteUser(userId);
    }

    @GetMapping(REFRESH_TOKEN_PATH)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Refresh the token", description = "Refresh the access token.")
    @ApiResponse(responseCode = "200", description = "Successfully refreshed token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServiceTokenGenerator.refreshTokenGenerator(request, response);
    }
}
