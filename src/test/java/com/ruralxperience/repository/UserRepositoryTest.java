package com.ruralxperience.repository;

import com.ruralxperience.entity.User;
import com.ruralxperience.enums.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldFindUserByEmail() {
        User admin = userRepository.findByEmail("admin@ruralxperience.com").orElse(null);
        assertThat(admin).isNotNull();
    }

    @Test
    void shouldCheckIfUserExistsByEmail() {
        Boolean notExists = userRepository.existsByEmail("host.test@test.com");
        assertThat(notExists).isFalse();
    }

    @Test
    void shouldFindUserByRole() {
        // Get explorers (2 explorers by page)
        Pageable pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<User> users = userRepository.findByRole(Role.EXPLORER, pageable);

        assertThat(users).isNotNull().hasSize(2);
    }

    @Test
    void shouldSearchUsers() {
        // Super is the first user. So, he will be on the first page.
        Pageable pageable = PageRequest.of(1, 2, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<User> users = userRepository.searchUsers("Sup", pageable);
        assertThat(users).isEmpty();
    }

    @Test
    void shouldCountByRole() {
        Long nbOfHosts = userRepository.countByRole(Role.HOST);
        assertThat(nbOfHosts).isEqualTo(5);
    }
}