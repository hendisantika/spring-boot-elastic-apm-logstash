package id.my.hendisantika.elasticapmlogstash.service;

import id.my.hendisantika.elasticapmlogstash.entity.User;
import id.my.hendisantika.elasticapmlogstash.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
}
