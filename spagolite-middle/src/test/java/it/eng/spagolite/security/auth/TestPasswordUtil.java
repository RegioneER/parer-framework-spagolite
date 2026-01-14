/*
 * Engineering Ingegneria Informatica S.p.A.
 *
 * Copyright (C) 2023 Regione Emilia-Romagna <p/> This program is free software: you can
 * redistribute it and/or modify it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the License, or (at your option)
 * any later version. <p/> This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Affero General Public License for more details. <p/> You should
 * have received a copy of the GNU Affero General Public License along with this program. If not,
 * see <https://www.gnu.org/licenses/>.
 */

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
