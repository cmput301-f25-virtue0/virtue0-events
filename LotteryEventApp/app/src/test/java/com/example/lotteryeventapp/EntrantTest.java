package com.example.lotteryeventapp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EntrantTest {
    private Entrant.Profile mockProfile(String name, String email, String phone) {
        return new Entrant.Profile(name, email, phone);
    }
    public Entrant mockEntrant() {
        return new Entrant("device1", mockProfile("Daniel",
                "dk8@ualberta.ca", "780-123-4567"));
    }

    @Test
    void nullId_throws() {
        assertThrows(NullPointerException.class,
                () -> new Entrant(null, mockProfile("Alice", "a@b.com", "555-1234")));
    }

    @Test
    void updateProfileTest() {
        Entrant entrant = new Entrant("device-123", mockProfile("Alice", "a@b.com", "111"));
        entrant.updateProfile("Bob", "bob@example.com", "222");

        assertEquals("Bob", entrant.getProfile().getName());
        assertEquals("bob@example.com", entrant.getProfile().getEmail());
        assertEquals("222", entrant.getProfile().getPhone());
    }

    @Test
    void optOut_true_to_false() {
        Entrant entrant = mockEntrant();

        assertFalse(entrant.isNotificationOptOut(), "default should be opted-in");

        entrant.setNotificationOptOut(true);
        assertTrue(entrant.isNotificationOptOut());

        entrant.setNotificationOptOut(false);
        assertFalse(entrant.isNotificationOptOut());
    }

}
