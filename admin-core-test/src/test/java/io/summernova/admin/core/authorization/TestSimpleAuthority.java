package io.summernova.admin.core.authorization;

import io.summernova.admin.core.security.authorization.SimpleAuthority;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * @author gongshuiwen
 */
class TestSimpleAuthority {

    @Test
    void testOf() {
        String test1 = "test";
        assertSame(SimpleAuthority.of(test1), SimpleAuthority.of(test1));
        assertSame(SimpleAuthority.of(test1).getAuthority(), SimpleAuthority.of(test1).getAuthority());
        assertSame(test1, SimpleAuthority.of(test1).getAuthority());

        @SuppressWarnings("all")
        String test2 = new String(test1);
        assertNotSame(test1, test2);
        assertSame(SimpleAuthority.of(test1), SimpleAuthority.of(test2));
        assertSame(SimpleAuthority.of(test1).getAuthority(), SimpleAuthority.of(test2).getAuthority());
        assertSame(test1, SimpleAuthority.of(test2).getAuthority());
    }
}
