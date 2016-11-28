package com.akm.http.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Provides test cases for the CollectionUtil class.
 *
 * @since 0.4
 * @author Amir
 */
public class CollectionUtilTest {
    private List<String> list = null;
    private Map<String, String> map = null;

    @Before
    public void setUp() throws Exception {
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

    @After
    public void tearDown() throws Exception {
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
        assertNotNull("list is null", list);
        assertFalse("list is empty", list.isEmpty());
        assertEquals("list size is invalid", 5, list.size());
        assertEquals("list element is invalid", " e ",
                list.get(list.size() - 1));
    }

    @Test
    public final void testFilterMap() {
        map.put("e", " 5 ");
        map.put("f", "");
        map.put("g", "  ");
        map = CollectionUtil.filterMap(map,
                e -> !e.getValue().trim().isEmpty());
        assertNotNull("map is null", map);
        assertFalse("map is empty", map.isEmpty());
        assertEquals("map size is invalid", 5, map.size());
        assertEquals("map element is invalid", " 5 ", map.get("e"));
    }
}
