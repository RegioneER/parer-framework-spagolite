package it.eng.spagolite.security.auth;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.eng.spagoLite.security.auth.PwdUtil;

/**
 * Test sulla classe che genera le password.
 *
 * @author Snidero_L
 */
public class TestPasswordUtil {

    public TestPasswordUtil() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testPasswordWitoutSalt() {
        String actualPassword = PwdUtil.encodePassword("password");
        final String expectedPassword = "W6ph5Mm5Pz8GgiULbPgzG37mj9g=";
        Assertions.assertEquals(expectedPassword, actualPassword);
    }

    @Test
    public void testPasswordWithSalt() {
        byte[] salt = PwdUtil.decodeUFT8Base64String("7TtONHgKep1bl7wmDGQ5jA==");
        String actualPassword = PwdUtil.encodePBKDF2Password(salt, "password");
        final String expectedPassword = "BQ9fmLrh2aNFQGRmtD+jjGiv6UaDKBcKvSzKaVWQ1NcHj8ZELAH1ZZXphSFzHlNJXlMjSIYl0Fd597B1kntbRw==";
        Assertions.assertEquals(expectedPassword, actualPassword);

    }

}
