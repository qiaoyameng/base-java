package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.LoginRequest;
import org.example.dto.LoginResponse;
import org.example.entity.Customer;
import org.example.entity.User;
import org.example.enums.RoleType;
import org.example.exception.BusinessException;
import org.example.repository.CustomerRepository;
import org.example.repository.UserRepository;
import org.example.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Transactional
    public void register(String username, String password, String phone) {
        if (userRepository.existsByUsername(username)) {
            throw new BusinessException("用户名已存在");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(RoleType.CUSTOMER);
        user.setEnabled(true);
        User savedUser = userRepository.save(user);

        Customer customer = new Customer();
        customer.setUser(savedUser);
        customer.setPhone(phone);
        customerRepository.save(customer);

        log.info("用户注册成功: username={}", username);
    }

    public LoginResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtUtil.generateToken(userDetails);

            User user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new BusinessException("用户不存在"));

            Customer customer = customerRepository.findByUserId(user.getId())
                    .orElse(null);

            log.info("用户登录成功: username={}", request.getUsername());

            LoginResponse response = new LoginResponse();
            response.setToken(token);
            response.setUsername(user.getUsername());
            response.setRole(user.getRole());
            response.setUserId(user.getId());
            response.setCustomerId(customer != null ? customer.getId() : null);
            return response;
        } catch (Exception e) {
            throw new BusinessException("用户名或密码错误");
        }
    }
}
