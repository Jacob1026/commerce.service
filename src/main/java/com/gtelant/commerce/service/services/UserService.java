package com.gtelant.commerce.service.services;

import com.gtelant.commerce.service.models.Segment;
import com.gtelant.commerce.service.models.User;
import com.gtelant.commerce.service.models.UserSegment;
import com.gtelant.commerce.service.repositories.SegmentRepository;
import com.gtelant.commerce.service.repositories.UserRepository;
import com.gtelant.commerce.service.repositories.UserSegmentRepository;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
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
    // 分頁查詢，支援多條件
    public Page<User> getAllUsersPage(String query, Boolean hasSubscribe, Integer segmentId, PageRequest pageRequest) {
        Specification<User> spec = userSpecification(query, hasSubscribe, segmentId);
        return userRepository.findAll(spec, pageRequest);
    }

    private Specification<User> userSpecification(String queryName, Boolean hasNewsletter, Integer segmentId) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            // if predicates.size() = 3 how many "AND"? => 2
            //if predicates.size() = 8  how many "AND"? => 7

            if(queryName != null && !queryName.isEmpty()) {
                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), "%"+ queryName.toLowerCase()+"%"),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), "%"+ queryName.toLowerCase()+"%")
                ));
            }
            if(hasNewsletter != null) {
                predicates.add(criteriaBuilder.equal(root.get("hasNewsletter"), hasNewsletter));
            }

            if(segmentId != null) {
                Join<User , UserSegment> userUserSegmentJoin = root.join("userSegments");
                predicates.add(criteriaBuilder.equal(userUserSegmentJoin.get("segment").get("id"), segmentId));

                //如果 userSegment有 屬性segmentId 則可以直接使用
                //predicates.add(criteriaBuilder.equal(userUserSegmentJoin.get("segmentId"), segmentId));

                //如果欲查詢Segment參數為字串（name）=> segmentName
                //predicates.add(criteriaBuilder.equal(userUserSegmentJoin.get("segment").get("name"), segmentName)
            }

            Predicate[] predicateArray = predicates.toArray(new Predicate[0]);
            return criteriaBuilder.and(predicateArray);
        });
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

    public List<UserSegment> getUserSegmentsByUserId(int userId) {
        return userSegmentRepository.findByUserId(userId);
    }


}
