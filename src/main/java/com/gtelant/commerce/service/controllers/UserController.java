package com.gtelant.commerce.service.controllers;
import com.gtelant.commerce.service.dtos.UserRequest;
import com.gtelant.commerce.service.dtos.UserSegmentResponse;
import com.gtelant.commerce.service.mappers.UserMapper;
import com.gtelant.commerce.service.models.User;
import com.gtelant.commerce.service.dtos.UserResponse;
import com.gtelant.commerce.service.models.UserSegment;
import com.gtelant.commerce.service.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin("*")
@Tag(name = "User", description = "User management APIs")
public class UserController {

    //注入service和mapper
    private final UserService userService;
    private final UserMapper userMapper;

    @Autowired
    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @Operation(summary = "取得所有的使用者", description = "回傳list of all users")
    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers().stream()
                .map(userMapper::toUserResponse)
                .toList();
    }

    @Operation(summary = "使用者分頁", description = "從第1頁開始，每頁10筆")
    @GetMapping("/page")
    //設定預設值page=1, size=10
    public List<UserResponse> getAllUsersPage(@RequestParam(defaultValue = "1") int page, //第幾頁
                                              @RequestParam(defaultValue = "10") int size,// 每頁幾筆
                                              @RequestParam(defaultValue = "")String query,//搜尋關鍵字,預設值空字串
                                              @RequestParam(required = false)Boolean hasSubscribe,//是否訂閱,大寫Boolean預設值null
                                              @RequestParam(required = false)Integer segmentId)//標籤ID,預設值null
    {
        //Spring Data JPA 頁碼是從0開始，所以要-1
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return userService.getAllUsersPage(query, hasSubscribe, segmentId, pageRequest).stream()
                .map(userMapper::toUserResponse)
                .toList();
    }

    @Operation(summary = "用ID去找使用者", description = "用ID去找使用者")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable int id) {
        User user = userService.getUserById(id);
        //找不到使用者
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        UserResponse dto = userMapper.toUserResponse(user);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "新增使用者", description = "新增使用者")
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest userRequest) {
        User user = userMapper.toUser(userRequest);
        User createdUser = userService.createUser(user);
        UserResponse dto = userMapper.toUserResponse(createdUser);
        return ResponseEntity.ok(dto);
    }


    @Operation(summary = "刪除用者", description = "刪除使用者")
    @DeleteMapping
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        boolean deleted = userService.deleteUser(id);
        //刪除失敗
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "新增使用者標籤", description = "用id，新增使用者標籤")
    @PostMapping(" /{id}/segments/{segmentId} ")
   public ResponseEntity<UserSegmentResponse> addUserToSegment(@PathVariable int id, @PathVariable int segmentId) {
        UserSegment userSegment = userService.addUserToSegment(id, segmentId);
        if (userSegment == null) {
            return ResponseEntity.notFound().build();
        }
        UserSegmentResponse dto = userMapper.toUserSegmentResponse(userSegment);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "用 userId 查詢使用者的 segment", description = "回傳該使用者所有的 segment 標籤")
    @GetMapping("/{userId}/segments")
    public ResponseEntity<List<UserSegmentResponse>> getUserSegmentsByUserId(@PathVariable int userId) {
        List<UserSegment> segments = userService.getUserSegmentsByUserId(userId);
        if (segments == null || segments.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<UserSegmentResponse> dtoList = segments.stream()
                .map(userMapper::toUserSegmentResponse)
                .toList();
        return ResponseEntity.ok(dtoList);
    }
}

