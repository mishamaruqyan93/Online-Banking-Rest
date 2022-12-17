package online.banking.rest.onlinebankingrest.endpoint;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.banking.rest.onlinebankingrest.dto.userAuthRegDto.UserAuthDto;
import online.banking.rest.onlinebankingrest.dto.userAuthRegDto.UserAuthResponseDto;
import online.banking.rest.onlinebankingrest.entity.User;
import online.banking.rest.onlinebankingrest.mapper.UserMapper;
import online.banking.rest.onlinebankingrest.service.impl.CustomerServiceImpl;
import online.banking.rest.onlinebankingrest.util.JwtTokenUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserAuthentication {

    private final CustomerServiceImpl customerServiceImpl;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserMapper userMapper;


    @PostMapping("/user-auth")
    public ResponseEntity<?> auth(@RequestBody UserAuthDto userAuthDto) {
        Optional<User> customerByEmail = customerServiceImpl.findCustomerByEmail(userAuthDto.getEmail());
        if (customerByEmail.isPresent()) {
            User customer = customerByEmail.get();
            if (passwordEncoder.matches(userAuthDto.getPassword(), customer.getPassword())) {
                log.info("User with username {} get out token", customer.getEmail());
                return ResponseEntity.ok(UserAuthResponseDto.builder()
                        .token(jwtTokenUtil.generateToken(customer.getEmail()))
                        .userDto(userMapper.map(customer))
                        .build()
                );
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
