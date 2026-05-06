package demo.security.util;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class AppTest {

    @Test
    public void testGenerateKey() {
        assertNotNull(Utils.generateKey());
    }
}
