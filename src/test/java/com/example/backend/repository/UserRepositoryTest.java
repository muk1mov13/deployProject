package com.example.backend.repository;

import com.example.backend.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

    @DataJpaTest
    @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
    public class UserRepositoryTest {

        @Autowired
        private UserRepository userRepository;

        private User testUser;

        @BeforeEach
        public void setUp() {
            testUser = new User(null,"dsvsdv","test_phone","13412",null);
            testUser.setPhone("test_phone");
            testUser = userRepository.save(testUser);
        }

        @AfterEach
        public void tearDown() {
            userRepository.deleteAll();
        }

        @Test
        public void testFindByPhone_Success() {
            Optional<User> foundUser = userRepository.findByPhone("test_phone");
            assertTrue(foundUser.isPresent());
            assertEquals("test_phone", foundUser.get().getPhone());

        }

        @Test
        public void testFindByPhone_UserNotFound() {
            Optional<User> foundUser = userRepository.findByPhone("non_existent_phone");
            assertFalse(foundUser.isPresent());
        }



    }

