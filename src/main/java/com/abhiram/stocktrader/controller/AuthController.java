package com.abhiram.stocktrader.controller;

import com.abhiram.stocktrader.dto.AuthResponse;
import com.abhiram.stocktrader.dto.LoginRequest;
import com.abhiram.stocktrader.dto.RegisterRequest;
import com.abhiram.stocktrader.entity.User;
import com.abhiram.stocktrader.service.JwtService;
import com.abhiram.stocktrader.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;

    public AuthController(
            UserService userService,
            JwtService jwtService) {

        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public AuthResponse register(
            @RequestBody RegisterRequest request) {

        User user = userService.register(request);

        return new AuthResponse(
                "User created: " + user.getUsername());
    }

    @PostMapping("/login")
    public AuthResponse login(
            @RequestBody LoginRequest request) {

        User user = userService.login(
                request.getEmail(),
                request.getPassword());

        String token = jwtService.generateToken(
                user.getEmail());

        return new AuthResponse(token);
    }
}