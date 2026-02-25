package org.example.controller;

import org.example.entity.User;
import org.example.service.UserService;
import org.example.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private RedisUtil redisUtil;
    
    // 获取所有用户
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAll();
        return ResponseEntity.ok(users);
    }
    
    // 根据ID获取用户
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        // 先从缓存中查找
        String cacheKey = "user:" + id;
        User cachedUser = (User) redisUtil.get(cacheKey);
        
        if (cachedUser != null) {
            System.out.println("从缓存中获取用户数据");
            return ResponseEntity.ok(cachedUser);
        }
        
        // 缓存中没有则从数据库查询
        return userService.findById(id)
                .map(user -> {
                    // 将查询结果存入缓存
                    redisUtil.set(cacheKey, user, 30, TimeUnit.MINUTES);
                    System.out.println("从数据库获取用户数据并存入缓存");
                    return ResponseEntity.ok(user);
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    // 创建用户
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User savedUser = userService.save(user);
        // 清除相关缓存
        redisUtil.delete("user:" + savedUser.getId());
        return ResponseEntity.ok(savedUser);
    }
    
    // 删除用户
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        // 清除缓存
        redisUtil.delete("user:" + id);
        return ResponseEntity.noContent().build();
    }
    
    // 测试Redis功能
    @GetMapping("/redis/test")
    public ResponseEntity<String> testRedis() {
        String key = "test:key";
        String value = "Hello Redis!";
        
        redisUtil.set(key, value, 5, TimeUnit.MINUTES);
        Object result = redisUtil.get(key);
        
        return ResponseEntity.ok("Redis测试成功: " + result);
    }
}
