package com.example.admin.rest;

import com.example.admin.bindings.LogInForm;
import com.example.admin.service.UserService;
import com.example.admin.utils.JwtUtil;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
	private UserService  userService;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LogInForm request) {
        Map<String, String> response = new HashMap<>();

        try {
            // Step 1: Authenticate using Spring Security
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
                )
            );

            // Step 2: Custom business logic (isActive, etc.)
            String loginAccount = userService.loginAccount(request);

            if (!"Success".equals(loginAccount)) {
                response.put("message", loginAccount); // e.g., "Account inactive"
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            // Step 3: Generate JWT token
            String token = jwtUtil.generateToken(request.getEmail());
//            response.put("token", token);
//            response.put("message", "redirect:/dashboard?email=" + request.getEmail());
            response.put("token", token);
            response.put("email", request.getEmail()); 
            response.put("message", "Login successful");
            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            response.put("message", "Invalid credentials. Please check email and password.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

}
