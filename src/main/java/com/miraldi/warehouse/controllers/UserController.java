package com.miraldi.warehouse.controllers;

import com.miraldi.warehouse.utils.PageableUtil;
import com.miraldi.warehouse.utils.Role;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
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

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final ServiceUser serviceUser;
    private final PageableUtil pageableUtil;

    public UserController(ServiceUser serviceUser,
                          PageableUtil pageableUtil){
        this.serviceUser = serviceUser;
        this.pageableUtil = pageableUtil;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<UserDto> searchUsers(@RequestParam(value = "role", required = false)
                                      Role role,
                                     @RequestParam(value = "username", required = false)
                                      String username,
                                     @RequestParam(value = "last-name", required = false)
                                      String lastName,
                                     @RequestParam(value = "first-name", required = false)
                                      String firstName,
                                     @RequestParam(value = "email", required = false)
                                      String email,
                                     @RequestParam(value = "city", required = false)
                                      String city,
                                     @RequestParam(value = "postal-code", required = false)
                                      Integer postalCode,
                                     @RequestParam(value = "country", required = false)
                                      String country,
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

        Pageable pageRequest = pageableUtil.getPageable(pageable, Sort.unsorted());
        return serviceUser.searchUsers(requestFilter, pageRequest);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@Valid @RequestBody CreateUserDto createUserDto){
        return serviceUser.createUser(createUserDto);
    }

    @PutMapping("/{user-id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateUser(@PathVariable("user-id") Long userId,
                           @Valid @RequestBody UpdateUserDto updateUserDto){
        serviceUser.updateUser(userId, updateUserDto);
    }

    @DeleteMapping("/{user-id}")
    public void deleteUser(@PathVariable("user-id") Long userId){
        serviceUser.deleteUser(userId);
    }
}
