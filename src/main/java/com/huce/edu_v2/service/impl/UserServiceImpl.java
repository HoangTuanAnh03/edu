package com.huce.edu_v2.service.impl;

import com.huce.edu_v2.advice.AppException;
import com.huce.edu_v2.advice.ErrorCode;
import com.huce.edu_v2.advice.exception.DuplicateRecordException;
import com.huce.edu_v2.advice.exception.ResourceNotFoundException;
import com.huce.edu_v2.dto.mapper.UserMapper;
import com.huce.edu_v2.dto.request.CreateUserRequest;
import com.huce.edu_v2.dto.request.PasswordCreationRequest;
import com.huce.edu_v2.dto.request.UpdateUserRequest;
import com.huce.edu_v2.dto.response.ResultPaginationDTO;
import com.huce.edu_v2.dto.response.UserResponse;
import com.huce.edu_v2.entity.Role;
import com.huce.edu_v2.entity.User;
import com.huce.edu_v2.entity.VerificationCode;
import com.huce.edu_v2.repository.RoleRepository;
import com.huce.edu_v2.repository.UserRepository;
import com.huce.edu_v2.service.UserService;
import com.huce.edu_v2.service.VerifyCodeService;
import com.huce.edu_v2.util.SecurityUtil;
import com.huce.edu_v2.util.constant.VerifyTypeEnum;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    VerifyCodeService verifyCodeService;
    PasswordEncoder passwordEncoder;
    UserMapper userMapper;
    SecurityUtil securityUtil;

    /**
     * @param email - Input email
     * @return boolean indicating if the email already exited or not
     */
    @Override
    public boolean isEmailExistAndActive(String email) {
        return this.userRepository.existsByEmailAndActive(email, true);
    }

    /**
     * @param id - Input UserId
     * @return User Object on a given userId
     */
    @Override
    public User fetchUserById(String id) {
        return this.userRepository.findById(id).orElse(null);
    }

    /**
     * @return UserResponse Object - Info currentUser
     */
    @Override
    public UserResponse fetchMyInfo() {
        String email = securityUtil.getCurrentUserLogin().orElse(null);
        return this.userMapper.toUserResponse(Objects.requireNonNull(userRepository.findByEmail(email).orElse(null)));
    }

    /**
     * @param request - password
     */
    @Override
    public void createPassword(PasswordCreationRequest request) {
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (StringUtils.hasText(user.getPassword()))
            throw new AppException(ErrorCode.PASSWORD_EXISTED);

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
    }

    /**
     * @param email - Input email
     * @return User Object on a given email
     */
    @Override
    public User fetchUserByEmail(String email) {
        return this.userRepository.findByEmail(email).orElse(null);
    }

    /**
     * @param id - Input UserId
     * @return User Details based on a given data updated to database
     */
    @Override
    public UserResponse fetchResUserDtoById(String id) {
        User user = this.userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User", "userId", id)
        );

        return this.userMapper.toUserResponse(user);
    }

    /**
     * @param spec     - filter
     * @param pageable - page, size, sort(field,asc(desc))
     * @return ResultPaginationDTO based on a given spec and pageable
     */
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public ResultPaginationDTO fetchAllUser(Specification<User> spec, Pageable pageable) {
        Page<User> pageUser = this.userRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageUser.getTotalPages());
        mt.setTotal(pageUser.getTotalElements());

        rs.setMeta(mt);

        // remove sensitive data
        List<UserResponse> listUser = pageUser.getContent()
                .stream().map(this.userMapper::toUserResponse)
                .collect(Collectors.toList());

        rs.setResult(listUser);

        return rs;
    }

    /**
     * @param createUserRequest - Input CreateUserRequest Object
     * @return User Details based on a given data saved to database
     */
    @Override
    public UserResponse handleCreateUser(CreateUserRequest createUserRequest) {
        User user = userRepository.findByEmail(createUserRequest.getEmail()).orElse(null);
        if (user != null) {
            if (user.getActive()) throw new DuplicateRecordException("USER ", "Email", createUserRequest.getEmail());
            else userRepository.delete(user);
        }

        Role role = roleRepository.findByName("USER").orElseThrow(
                () -> new ResourceNotFoundException("Role", "roleName", "USER")
        );

        User newUser = User.builder()
                .id("").name(createUserRequest.getName())
//                .gender(newUser.getGender())
//                .address(newUser.getAddress())
//                .dob(newUser.getDob())
                .email(createUserRequest.getEmail())
                .role(role)
                .password(passwordEncoder.encode(createUserRequest.getPassword()))
                .active(false)
                .build();

        VerificationCode verificationCode = verifyCodeService.findByEmail(createUserRequest.getEmail());

        if (verificationCode != null) {
            verifyCodeService.delete(verificationCode);
        }

        String code = RandomStringUtils.randomAlphanumeric(64);

        verifyCodeService.save(VerificationCode.builder()
                .code(code)
                .type(VerifyTypeEnum.REGISTER)
                .email(createUserRequest.getEmail())
                .exp(LocalDateTime.now()).build());

        userRepository.save(newUser);
        return this.userMapper.toUserResponse(newUser);
    }

    /**
     * @param id                - Input UserId
     * @param updateUserRequest - Input UpdateUserRequest Object
     * @return User Details based on a given data updated to database
     */
    @Override
    public UserResponse handleUpdateUser(String id, UpdateUserRequest updateUserRequest) {
        User currentUser = this.userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User", "userId", id)
        );

        currentUser.setName(updateUserRequest.getName());
        currentUser.setAddress(updateUserRequest.getAddress());
        currentUser.setGender(updateUserRequest.getGender());
        currentUser.setDob(updateUserRequest.getDob());
        currentUser.setMobileNumber(updateUserRequest.getMobileNumber());
        //  set company

        // check company
//            if (reqUser.getCompany() != null) {
//                Optional<Company> companyOptional = this.companyService.findById(reqUser.getCompany().getId());
//                currentUser.setCompany(companyOptional.isPresent() ? companyOptional.get() : null);
//            }

        currentUser = this.userRepository.save(currentUser);
        return this.userMapper.toUserResponse(currentUser);
    }

    /**
     * @param id - Input UserId
     * @return boolean indicating if the delete of User details is successful or not
     */
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public boolean handleDeleteUser(String id) {
        User user = this.userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User", "userId", id)
        );
        this.userRepository.deleteById(user.getId());
        return true;
    }

    /**
     * @param email
     * @return
     */
    @Override
    public Boolean forgotPassword(String email) {
        User user = userRepository.findFirstByEmailAndActive(email, true).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED)
        );

        if (!StringUtils.hasText(user.getPassword()))
            throw new AppException(ErrorCode.LOGIN_WITH_GOOGLE);

        VerificationCode verificationCode = verifyCodeService.findByEmail(email);

        if (verificationCode != null) {
            verifyCodeService.delete(verificationCode);
        }

        String code = RandomStringUtils.randomAlphanumeric(64);

        verifyCodeService.save(VerificationCode.builder()
                .code(code)
                .type(VerifyTypeEnum.FORGOT_PASSWORD)
                .email(email)
                .exp(LocalDateTime.now()).build());
        return true;
    }
}
