package ar.edu.utn.frc.tup.lc.iii.security.controllers;

import ar.edu.utn.frc.tup.lc.iii.common.ApiResponse;
import ar.edu.utn.frc.tup.lc.iii.directory.entities.User;
import ar.edu.utn.frc.tup.lc.iii.security.JwtService;
import ar.edu.utn.frc.tup.lc.iii.security.UserDetailsImpl;
import ar.edu.utn.frc.tup.lc.iii.security.dtos.AuthResponse;
import ar.edu.utn.frc.tup.lc.iii.security.dtos.LoginRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String token = jwtService.generateToken(userDetails);
        User user = userDetails.getUser();

        // Construir mapa de tenants con sus roles
        Map<UUID, Set<String>> tenantRoles = new HashMap<>();
        user.getUserRoles().forEach(userRole -> {
            UUID tenantId = userRole.getTenantId();
            tenantRoles.computeIfAbsent(tenantId, k -> new HashSet<>())
                    .add(userRole.getRole().getCode());
        });

        AuthResponse authResponse = AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .userId(user.getId())
                .email(user.getEmail())
                .isSuperAdmin(user.isSuperAdmin())
                .accessibleTenants(user.getAccessibleTenantIds())
                .tenantRoles(tenantRoles)
                .build();

        return ResponseEntity.ok(ApiResponse.success("Login exitoso", authResponse));
    }
}
