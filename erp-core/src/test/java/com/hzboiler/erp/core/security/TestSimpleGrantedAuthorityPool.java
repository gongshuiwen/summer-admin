package com.hzboiler.erp.core.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * @author gongshuiwen
 */
class TestSimpleGrantedAuthorityPool {

    @Test
    void testGetAuthority() {
        String test1 = "test";
        assertSame(SimpleGrantedAuthorityPool.getAuthority(test1), SimpleGrantedAuthorityPool.getAuthority(test1));
        assertSame(SimpleGrantedAuthorityPool.getAuthority(test1).getAuthority(), SimpleGrantedAuthorityPool.getAuthority(test1).getAuthority());
        assertSame(test1, SimpleGrantedAuthorityPool.getAuthority(test1).getAuthority());

        String test2 = new String(test1);
        assertNotSame(test1, test2);
        assertSame(SimpleGrantedAuthorityPool.getAuthority(test1), SimpleGrantedAuthorityPool.getAuthority(test2));
        assertSame(SimpleGrantedAuthorityPool.getAuthority(test1).getAuthority(), SimpleGrantedAuthorityPool.getAuthority(test2).getAuthority());
        assertSame(test1, SimpleGrantedAuthorityPool.getAuthority(test2).getAuthority());
    }
}
