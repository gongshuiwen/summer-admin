package io.summernova.admin.core.security.authorization;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * @author gongshuiwen
 */
class TestSimpleGrantedAuthority {

    @Test
    void testOf() {
        String test1 = "test";
        assertSame(SimpleGrantedAuthority.of(test1), SimpleGrantedAuthority.of(test1));
        assertSame(SimpleGrantedAuthority.of(test1).getAuthority(), SimpleGrantedAuthority.of(test1).getAuthority());
        assertSame(test1, SimpleGrantedAuthority.of(test1).getAuthority());

        @SuppressWarnings("all")
        String test2 = new String(test1);
        assertNotSame(test1, test2);
        assertSame(SimpleGrantedAuthority.of(test1), SimpleGrantedAuthority.of(test2));
        assertSame(SimpleGrantedAuthority.of(test1).getAuthority(), SimpleGrantedAuthority.of(test2).getAuthority());
        assertSame(test1, SimpleGrantedAuthority.of(test2).getAuthority());
    }
}
