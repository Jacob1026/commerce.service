package com.gtelant.commerce.service.services;

import com.gtelant.commerce.service.models.Segment;
import com.gtelant.commerce.service.models.User;
import com.gtelant.commerce.service.models.UserSegment;
import com.gtelant.commerce.service.repositories.SegmentRepository;
import com.gtelant.commerce.service.repositories.UserRepository;
import com.gtelant.commerce.service.repositories.UserSegmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final SegmentRepository segmentRepository;
    private final UserSegmentRepository userSegmentRepository;

    @Autowired
    public UserService(UserRepository userRepository, SegmentRepository segmentRepository, UserSegmentRepository userSegmentRepository) {
        this.userRepository = userRepository;
        this.segmentRepository = segmentRepository;
        this.userSegmentRepository = userSegmentRepository;
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
//分頁，要求型態為Page
    public Page<User> getAllUsersPage(PageRequest pageRequest) {
        return userRepository.findAll(pageRequest);
    }

    public User getUserById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    public User updateUser(Integer id, User updatedUser) {
        if (userRepository.existsById(id)) {
            updatedUser.setId(id);
            return userRepository.save(updatedUser);
        }
        return null;
    }
    public boolean deleteUser(Integer id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    //新增使用者到某個Segment
    public UserSegment addUserToSegment(int id, int segmentId) {
        Optional<User> user = userRepository.findById(id);
        Optional<Segment> segment = segmentRepository.findById(segmentId);

        if (user.isPresent() && segment.isPresent()) {
            UserSegment userSegment = new UserSegment();
            userSegment.setUser(user.get());
            userSegment.setSegment(segment.get());
            userSegment.setCreatedAt(java.time.LocalDateTime.now());
            userSegmentRepository.save(userSegment);
            return null;
        }
        return null;
    }
}
