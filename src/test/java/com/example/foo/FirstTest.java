package com.example.foo;

import java.beans.Transient;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

class FirstTest {
    @Test
    void test() {
        System.out.println("FirstTest");
    }

    @Test
    void foo() {
        Foo foo = new Foo();
        Assertions.assertEquals("foo", foo.foo());
    }
}