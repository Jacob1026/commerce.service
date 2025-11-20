package com.gtelant.commerce.service.mappers;

import com.gtelant.commerce.service.models.User;
import com.gtelant.commerce.service.models.UserSegment;
import com.gtelant.commerce.service.dtos.UserRequest;
import com.gtelant.commerce.service.dtos.UserResponse;
import com.gtelant.commerce.service.dtos.UserSegmentResponse;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import java.util.stream.Collectors;

//統一管理DTO與Model的轉換
@Component
public class UserMapper {
    public UserResponse toUserResponse(User user) {
        UserResponse dto = new UserResponse();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setBirthday(user.getBirthday());
        dto.setAddress(user.getAddress());
        dto.setCity(user.getCity());
        dto.setZipcode(user.getZipcode());
        dto.setRole(user.getRole());
        dto.setHasSubscribe(user.isHasSubscribe());
        dto.setLastSeenAt(user.getLastSeenAt());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setDeletedAt(user.getDeletedAt());
        if (user.getUserSegments() != null) {
            dto.setUserSegments(user.getUserSegments().stream()
                    .map(this::toUserSegmentResponse)
                    .collect(Collectors.toList()));
        }
        return dto;
    }

    public User toUser(UserRequest dto) {
        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setBirthday(dto.getBirthday());
        user.setAddress(dto.getAddress());
        user.setCity(dto.getCity());
        user.setZipcode(dto.getZipcode());
        user.setPassword(dto.getPassword());
        user.setRole(dto.getRole());
        user.setHasSubscribe(dto.isHasSubscribe());
        return user;
    }

    public UserSegmentResponse toUserSegmentResponse(UserSegment userSegment) {
        UserSegmentResponse dto = new UserSegmentResponse();
        dto.setId(userSegment.getId());
        dto.setUserId(userSegment.getUser().getId());
        dto.setSegmentId(userSegment.getSegment().getId());
        dto.setName(userSegment.getSegment().getName());
        return dto;
    }
}
