package com.akm.http.util;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Provides test cases for the CollectionUtil class.
 *
 * @since 0.4
 * @author Amir
 */
public class CollectionUtilTest {
    private List<String> list = null;
    private Map<String, String> map = null;

    @BeforeEach
    public void setUp() {
        list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");

        map = new HashMap<>();
        map.put("a", "1");
        map.put("b", "2");
        map.put("c", "3");
        map.put("d", "4");
    }

    @AfterEach
    public void tearDown() {
        list = null;
        map = null;
    }

    @Test
    public final void testFilterCollection() {
        list.add(" e ");
        list.add("");
        list.add("  ");
        list = (List<String>) CollectionUtil.filterCollection(list,
                e -> !e.trim().isEmpty());
        assertAll("filter collection",
            () -> assertNotNull(list, "list is null"),
            () -> assertFalse(list.isEmpty(), "list is empty"),
            () -> assertEquals(5, list.size(), "list size is invalid"),
            () -> assertEquals(" e ", list.get(list.size() - 1), "list element is invalid"));
    }

    @Test
    public final void testFilterMap() {
        map.put("e", " 5 ");
        map.put("f", "");
        map.put("g", "  ");
        map = CollectionUtil.filterMap(map,
                e -> !e.getValue().trim().isEmpty());
        assertAll("filter map",
            () -> assertNotNull(map, "map is null"),
            () -> assertFalse(map.isEmpty(), "map is empty"),
            () -> assertEquals(5, map.size(), "map size is invalid"),
            () -> assertEquals(" 5 ", map.get("e"), "map element is invalid"));
    }
}
