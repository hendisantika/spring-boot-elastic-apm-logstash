package id.my.hendisantika.elasticapmlogstash.service;

import id.my.hendisantika.elasticapmlogstash.entity.User;
import id.my.hendisantika.elasticapmlogstash.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-elastic-apm-logstash
 * User: hendisantika
 * Link: s.id/hendisantika
 * Email: hendisantika@yahoo.co.id
 * Telegram : @hendisantika34
 * Date: 27/09/25
 * Time: 06.47
 * To change this template use File | Settings | File Templates.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @CaptureTransaction("user-service")
    public List<User> getAllUsers() {
        log.info("Fetching all users");

        try {
            List<User> users = userRepository.findAll();
            log.info("Retrieved {} users", users.size());
            return users;
        } catch (Exception e) {
            log.error("Error fetching all users", e);
            throw e;
        }
    }

    @CaptureSpan("get-user-by-id")
    public Optional<User> getUserById(Long id) {
        MDC.put("userId", String.valueOf(id));

        try {
            log.info("Fetching user by id: {}", id);
            Optional<User> user = userRepository.findById(id);

            if (user.isPresent()) {
                log.info("User found: {}", user.get().getEmail());
            } else {
                log.warn("User not found with id: {}", id);
            }

            return user;
        } catch (Exception e) {
            log.error("Error fetching user by id: {}", id, e);
            throw e;
        } finally {
            MDC.remove("userId");
        }
    }

    @CaptureSpan("create-user")
    public User createUser(User user) {
        MDC.put("userEmail", user.getEmail());

        try {
            log.info("Creating new user: {}", user.getEmail());

            if (userRepository.existsByEmail(user.getEmail())) {
                log.error("User already exists with email: {}", user.getEmail());
                throw new RuntimeException("User already exists with email: " + user.getEmail());
            }

            User savedUser = userRepository.save(user);
            log.info("User created successfully with id: {}", savedUser.getId());

            return savedUser;
        } catch (Exception e) {
            log.error("Error creating user", e);
            throw e;
        } finally {
            MDC.remove("userEmail");
        }
    }
}
