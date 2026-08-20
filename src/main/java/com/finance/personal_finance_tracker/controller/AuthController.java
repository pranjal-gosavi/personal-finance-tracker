package com.finance.personal_finance_tracker.controller;

import com.finance.personal_finance_tracker.model.User;
import com.finance.personal_finance_tracker.payload.*;
import com.finance.personal_finance_tracker.repository.UserRepository;
import com.finance.personal_finance_tracker.security.JwtUtils;
import com.finance.personal_finance_tracker.security.CustomUserDetailsService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final CustomUserDetailsService userDetailsService;

    public AuthController(AuthenticationManager authenticationManager,
                          UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          JwtUtils jwtUtils,
                          CustomUserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest().body("Username is already taken");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles("ROLE_USER");
        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
            String token = jwtUtils.generateToken(userDetails);

            return ResponseEntity.ok(new AuthResponse(token));

        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(401).body("Invalid username or password");
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("An error occurred: " + ex.getMessage());
        }
    }
}
























//package com.finance.personal_finance_tracker.controller;
//
//import com.finance.personal_finance_tracker.model.User;
//import com.finance.personal_finance_tracker.payload.*;
//import com.finance.personal_finance_tracker.repository.UserRepository;
//import com.finance.personal_finance_tracker.security.JwtUtils;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.*;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.Authentication;
//import org.springframework.beans.factory.annotation.Autowired;
//
//@RestController
//@RequestMapping("/api/auth")
//public class AuthController {
//
//    @Autowired private AuthenticationManager authenticationManager;
//    @Autowired private UserRepository userRepository;
//    @Autowired private PasswordEncoder passwordEncoder;
//    @Autowired private JwtUtils jwtUtils;
//    @Autowired private com.finance.personal_finance_tracker.security.CustomUserDetailsService userDetailsService;
//
//    @PostMapping("/register")
//    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
//        if (userRepository.existsByUsername(request.getUsername())) {
//            return ResponseEntity.badRequest().body("Username is already taken");
//        }
//        User user = new User();
//        user.setUsername(request.getUsername());
//        user.setPassword(passwordEncoder.encode(request.getPassword()));
//        user.setRoles("ROLE_USER");
//        userRepository.save(user);
//        return ResponseEntity.ok("User registered successfully");
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
//        );
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
//        String token = jwtUtils.generateToken(userDetails);
//        return ResponseEntity.ok(new AuthResponse(token));
//    }
//}
