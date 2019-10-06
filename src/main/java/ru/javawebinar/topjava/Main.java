package ru.javawebinar.topjava;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @see <a href="http://topjava.herokuapp.com">Demo</a>
 * @see <a href="https://github.com/JavaOPs/topjava">Initial project</a>
 */
public class Main {
    public static void main(String[] args) {

        Map<Long, Object> map = new HashMap<>();

        System.out.println(Collections.max(map.keySet()));

        System.out.format("Hello Topjava Enterprise!");
    }
}
