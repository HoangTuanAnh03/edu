package com.huce.edu_v2.controller;

import com.huce.edu_v2.advice.exception.StorageException;
import com.huce.edu_v2.dto.ApiResponse;
import com.huce.edu_v2.dto.request.auth.ForgotPasswordRequest;
import com.huce.edu_v2.dto.request.auth.PasswordCreationRequest;
import com.huce.edu_v2.dto.request.user.UpdateUserRequest;
import com.huce.edu_v2.dto.response.auth.AuthenticationResponse;
import com.huce.edu_v2.dto.response.ResultPaginationDTO;
import com.huce.edu_v2.dto.response.upload.UploadFileResponse;
import com.huce.edu_v2.dto.response.user.UserResponse;
import com.huce.edu_v2.entity.User;
import com.huce.edu_v2.service.UploadService;
import com.huce.edu_v2.service.UserService;
import com.huce.edu_v2.util.SecurityUtil;
import com.huce.edu_v2.util.constant.AppConstants;
import com.huce.edu_v2.util.constant.NameFolders;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;
    UploadService uploadService;
    SecurityUtil securityUtil;

    @PostMapping("/create-password")
    public ApiResponse<?> createPassword(@RequestBody @Valid PasswordCreationRequest request) {
        userService.createPassword(request);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.CREATED.value())
                .message("Password has been created, you could use it to log-in")
                .build();
    }

    @GetMapping("/fetchUserById/{id}")
    public ApiResponse<UserResponse> getUserById(@PathVariable("id") String id) {
        return ApiResponse.<UserResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Fetch user by id")
                .data(this.userService.fetchResUserDtoById(id))
                .build();
    }

    @GetMapping("/my-info")
    public ApiResponse<UserResponse> getMyInfo() {
        return ApiResponse.<UserResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Fetch my info")
                .data(this.userService.fetchMyInfo())
                .build();
    }

//    @GetMapping("")
//    public ApiResponse<ResultPaginationDTO> getAllUser(
//            @Filter Specification<User> spec,
//            Pageable pageable) {
//
//        return ApiResponse.<ResultPaginationDTO>builder()
//                .code(HttpStatus.OK.value())
//                .message("Fetch all users")
//                .data(this.userService.fetchAllUser(spec, pageable))
//                .build();
//    }


    @PutMapping("/{id}")
    public ApiResponse<UserResponse> updateUser(@PathVariable("id") String id, @Valid @RequestBody UpdateUserRequest updateUserRequest) {
        return ApiResponse.<UserResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Update a user")
                .data(this.userService.handleUpdateUser(id, updateUserRequest))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<?> deleteUser(@PathVariable("id") String id) {
        boolean isDeleted = userService.handleDeleteUser(id);
        if (isDeleted) {
            return ApiResponse.<String>builder()
                    .code(HttpStatus.CREATED.value())
                    .message(AppConstants.MESSAGE_200)
                    .data(null)
                    .build();
        } else {
            return ApiResponse.<String>builder()
                    .code(HttpStatus.EXPECTATION_FAILED.value())
                    .message(AppConstants.MESSAGE_417_DELETE)
                    .data(null)
                    .build();
        }
    }

    @PostMapping("/forgotPassword")
    ApiResponse<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        userService.forgotPassword(forgotPasswordRequest.getEmail());

        return ApiResponse.<AuthenticationResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Verify email register success")
                .data(null)
                .build();
    }

    @PostMapping("/upload/avatar")
    public ApiResponse<UploadFileResponse> uploadAvatar(
            @RequestParam(name = "image", required = false) MultipartFile file
    ) throws URISyntaxException, IOException, StorageException {
        // valid type file
        uploadService.validTypeImage(file);
        // store file
        List<String> uploadFile = Collections.singletonList(this.uploadService.store(file, NameFolders.AVATAR));

        UploadFileResponse res = new UploadFileResponse(uploadFile, Instant.now());

        User user = userService.fetchUserByEmail(securityUtil.getCurrentUserLogin().orElse(null));

        userService.setAvatar(user.getId() , uploadFile.get(0));

        return ApiResponse.<UploadFileResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Upload image avatar")
                .data(res)
                .build();
    }
}