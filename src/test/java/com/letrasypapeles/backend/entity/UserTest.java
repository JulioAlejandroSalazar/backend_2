package com.letrasypapeles.backend.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

class UserTest {

    @Test
    void userShouldHaveRequiredFields() {
        User user = User.builder()
            .username("julio123")
            .lastname("salazar")
            .password("1234")
            .role(Role.ADMIN)
            .build();

        assertNotNull(user.getUsername());
        assertNotNull(user.getLastname());
        assertNotNull(user.getPassword());
        assertEquals("ROLE_ADMIN", user.getAuthorities().iterator().next().getAuthority());
    }

}
