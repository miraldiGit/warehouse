package com.miraldi.warehouse.services;

import com.miraldi.warehouse.dto.converter.UserConverter;
import com.miraldi.warehouse.dto.userDto.ChangeUserPasswordDto;
import com.miraldi.warehouse.dto.userDto.CreateUserDto;
import com.miraldi.warehouse.dto.userDto.UpdateUserDto;
import com.miraldi.warehouse.dto.userDto.UserDetailsDto;
import com.miraldi.warehouse.entities.User;
import com.miraldi.warehouse.repositories.RepositoryUser;
import com.miraldi.warehouse.security.CustomUserDetails;
import com.miraldi.warehouse.security.SecurityUtils;
import com.miraldi.warehouse.utils.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.miraldi.warehouse.repositories.specifications.SpecificationUser.hasCityLike;
import static com.miraldi.warehouse.repositories.specifications.SpecificationUser.hasCountryLike;
import static com.miraldi.warehouse.repositories.specifications.SpecificationUser.hasEmailLike;
import static com.miraldi.warehouse.repositories.specifications.SpecificationUser.hasFirstNameLike;
import static com.miraldi.warehouse.repositories.specifications.SpecificationUser.hasLastNameLike;
import static com.miraldi.warehouse.repositories.specifications.SpecificationUser.hasPostalCode;
import static com.miraldi.warehouse.repositories.specifications.SpecificationUser.hasRole;
import static com.miraldi.warehouse.repositories.specifications.SpecificationUser.hasUsernameLike;

@Service
@RequiredArgsConstructor
@Slf4j
public class ServiceUser implements UserDetailsService {

    private final UserConverter userConverter;
    private final RepositoryUser repositoryUser;
    private final BCryptPasswordEncoder passwordEncoder;



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = repositoryUser.findByUsernameOrEmail(username, username);

        if(user.isEmpty()){
            log.error("User was not found");
            throw new UsernameNotFoundException("User was not found");
        }
        log.info("User {} was found", username);
        return new CustomUserDetails(user.get());

    }

    public UserDetailsDto createUser(CreateUserDto createUserDto) {
        if (repositoryUser.findByUsernameOrEmail(createUserDto.getEmail(),
                createUserDto.getEmail()).isEmpty()
                && repositoryUser.findByUsernameOrEmail(createUserDto.getUsername(),
                createUserDto.getUsername()).isEmpty()) {

            createUserDto.setPassword(passwordEncoder
                    .encode(createUserDto.getPassword()));
            User user = repositoryUser.save(userConverter.convertCreateUserDtoToUser(createUserDto));
            return userConverter.convertUserToUserDetailsDto(user);
        }
        else {
            if (repositoryUser.findByUsernameOrEmail(createUserDto.getEmail(),
                    createUserDto.getEmail()).isPresent()) {
                throw new IncorrectDataException(List.of(
                        "The provided email already exists"));
            }
            if (repositoryUser.findByUsernameOrEmail(createUserDto.getUsername(),
                    createUserDto.getUsername()).isPresent()) {
                throw new IncorrectDataException(List.of(
                        "The provided username already exists"));
            }
            return null;
        }
    }

    public void updateUser(Long id, UpdateUserDto updateUserDto) {
        var user = repositoryUser.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if(updateUserDto.getLastName() != null &&
           updateUserDto.getFirstName() != null &&
           updateUserDto.getCountry() != null &&
           updateUserDto.getPostalCode()>=1000 && updateUserDto.getPostalCode()<=999999){
           userConverter.convertUpdateUserDtoToUser(updateUserDto, user);
            repositoryUser.save(user);
        }
        else{
            throw new IncorrectDataException("All of the fields must not be null and the postal code must be" +
                    "between 1000 and 999999.");
        }
    }

    public void changeUserPassword(ChangeUserPasswordDto changeUserPasswordDto) {
        var user = repositoryUser.findByUsernameOrEmail(
                changeUserPasswordDto.getUsernameOrEmail(),
                changeUserPasswordDto.getUsernameOrEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (SecurityUtils.loggedUser().getUsername().equals(changeUserPasswordDto.getUsernameOrEmail())
                || SecurityUtils.loggedUser().getEmail().equals(changeUserPasswordDto.getUsernameOrEmail())) {

            String newPassword = changeUserPasswordDto.getNewPassword();
            String oldPassword = changeUserPasswordDto.getOldPassword();
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

            if (!bCryptPasswordEncoder.matches(oldPassword, user.getPassword())){
                throw new IncorrectDataException(
                        "Old password does not match with current password");
            }
            if(!bCryptPasswordEncoder.matches(newPassword, user.getPassword())) {
                String encryptedNewPassword = passwordEncoder
                        .encode(changeUserPasswordDto.getNewPassword());
                user.setPassword(encryptedNewPassword);
                repositoryUser.save(user);
            }
            else throw new IncorrectDataException(
                    "New Password does not fulfill historical conditions"
            );
        }
        else throw new AccessDeniedException();
    }

    public Page<UserDetailsDto> searchUsers(UserRequestFilter requestFilter, Pageable pageable) {
        Page<User> pageUser;

        Specification<User> userSpecification =
                Specification.where(hasRole(requestFilter.getRole()))
                        .and(hasUsernameLike(requestFilter.getUsername()))
                        .and(hasLastNameLike(requestFilter.getLastName()))
                        .and(hasFirstNameLike(requestFilter.getFirstName()))
                        .and(hasEmailLike(requestFilter.getEmail()))
                        .and(hasCityLike(requestFilter.getCity()))
                        .and(hasCountryLike(requestFilter.getCountry()))
                        .and(hasPostalCode(requestFilter.getPostalCode()));

        pageUser = repositoryUser.findAll(userSpecification, pageable);

        return pageUser.map(userConverter::convertUserToUserDetailsDto);
    }

    public void deleteUser(Long id) {
        var user = repositoryUser.findById(id)
                .orElseThrow(ResourceNotFoundException::new);
        if(!user.getUsername().equals("root")){
            user.setDeleted(true);
            repositoryUser.save(user);
        }
        else throw new IllegalArgumentException();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserRequestFilter {

        private Role role;
        private String username;
        private String lastName;
        private String firstName;
        private String email;
        private String city;
        private Integer postalCode;
        private String country;
    }
}