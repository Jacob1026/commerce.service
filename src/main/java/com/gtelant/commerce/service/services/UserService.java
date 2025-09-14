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
//新增使用者
    public User createUser(User user) {
        return userRepository.save(user);
    }
//取得所有使用者
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    // 分頁查詢，支援多條件
    public Page<User> getAllUsersPage(String query, Boolean hasSubscribe, Integer segmentId, PageRequest pageRequest) {
        Specification<User> spec = userSpecification(query, hasSubscribe, segmentId);
        return userRepository.findAll(spec, pageRequest);
    }
//多條件查詢
    private Specification<User> userSpecification(String queryName, Boolean hasSubscribe, Integer segmentId) {
        //Specification<User> 是一個函數式介面，可以用lambda表示法來實作
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
            if(hasSubscribe != null) {
                predicates.add(criteriaBuilder.equal(root.get("hasSubscribe"), hasSubscribe));
            }

            if(segmentId != null) {
                //透過 Join 來連接 User 和 UserSegment資料表
                Join<User , UserSegment> userUserSegmentJoin = root.join("userSegments");// userSegments
                predicates.add(criteriaBuilder.equal(userUserSegmentJoin.get("segment").get("id"), segmentId));

                //如果 userSegment有 屬性segmentId 則可以直接使用
                //predicates.add(criteriaBuilder.equal(userUserSegmentJoin.get("segmentId"), segmentId));

                //如果欲查詢Segment參數為字串（name）=> segmentName
                //predicates.add(criteriaBuilder.equal(userUserSegmentJoin.get("segment").get("name"), segmentName)

                //
            }
            Predicate[] predicateArray = predicates.toArray(new Predicate[0]);
            return criteriaBuilder.and(predicateArray);
        });
    }

//用ID去找使用者
    public User getUserById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }
//更新使用者
    public User updateUser(Integer id, User updatedUser) {
        if (userRepository.existsById(id)) {
            updatedUser.setId(id);
            return userRepository.save(updatedUser);
        }
        return null;
    }
//刪除使用者
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
//用 userId 查詢使用者的 segment
    public List<UserSegment> getUserSegmentsByUserId(int userId) {
        return userSegmentRepository.findByUserId(userId);
    }


}
