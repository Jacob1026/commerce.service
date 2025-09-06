package com.gtelant.commerce.service.controllers;
import com.gtelant.commerce.service.dtos.UserRequest;
import com.gtelant.commerce.service.mappers.UserMapper;
import com.gtelant.commerce.service.models.User;
import com.gtelant.commerce.service.dtos.UserResponse;
import com.gtelant.commerce.service.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
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

    @Operation(summary = "使用者分頁", description = "限制一次查詢設定筆數的使用者資料")
    @GetMapping("/page")
    public List<UserResponse> getAllUsersPage(@RequestParam(defaultValue = "1") int page, //第幾頁
                                              @RequestParam(defaultValue = "10") int size)//每頁幾筆
    {
        PageRequest pageRequest = PageRequest.of(page, size);
        return userService.getAllUsersPage(pageRequest).stream()
                .map(userMapper::toUserResponse)
                .toList();
    }

    @Operation(summary = "用ID去找使用者", description = "回傳list of all users")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse>getUserById(@PathVariable int id){
        User user = userService.getUserById(id);
        //找不到使用者
        if(user == null){
            return ResponseEntity.notFound().build();
        }
        UserResponse dto = userMapper.toUserResponse(user);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "新增使用者", description = "新增使用者")
    @PostMapping
    public ResponseEntity<UserResponse>createUser(@RequestBody UserRequest userRequest){
        User user = userMapper.toUser(userRequest);
        User createdUser = userService.createUser(user);
        UserResponse dto = userMapper.toUserResponse(createdUser);
        return ResponseEntity.ok(dto);
    }


    @Operation(summary = "刪除用者", description = "刪除使用者")
    @DeleteMapping
    public ResponseEntity<Void>deleteUser(@PathVariable int id){
        boolean deleted = userService.deleteUser(id);
        //刪除失敗
        if(!deleted){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

}
