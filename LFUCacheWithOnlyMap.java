package com.itbulls.learnit.javacore.dilshod;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class LFUCacheWithOnlyMap {
    class Item {
        int count;
        long time;
        int key;
        int value;

        public Item(int key, int value) {
            this.key = key;
            this.value = value;
            count = 1;
            this.time = System.nanoTime();
        }

        public int getCount() {
            return count;
        }

        public long getTime() {
            return time;
        }

        @Override
        public String toString() {
            return ""+value + "(count="+count+")" + "(last time used in millis="+time+")";
        }
    }

    private int capacity;
    private Map<Integer, Item> map;

    LFUCacheWithOnlyMap(int capacity) {
        this.capacity = capacity;
        map = new HashMap<>();
    }

    int get(int key) {
        if (!map.containsKey(key)) {
            return -1;
        }
        Item item = map.get(key);
        item.count++;
        item.time = System.nanoTime();
        return item.value;
    }

    void put(int key, int value) {
        if (capacity <= 0) {
            return;
        }

        // If key already exists, update its value and access information
        if (map.containsKey(key)) {
            Item item = map.get(key);
            item.value = value;
            item.count++;
            item.time = System.nanoTime();
        } else {
            // If cache is full, remove most frequently used item and kep least frequently used items
            if (map.size() >= capacity) {
                int keyFrequentUsed = map.values().stream()
                        .max(Comparator.comparing(Item::getCount)
                                .thenComparing(Item::getTime)).get().key;
                map.remove(keyFrequentUsed);
            }

            // Add new item
            Item item = new Item(key, value);
            map.put(key, item);
        }
    }

    public static void main(String[] args) {
        // Create a LFU cache with capacity 2
        LFUCacheWithOnlyMap cache = new LFUCacheWithOnlyMap(2);

        // Insert key-value pairs
        cache.put(1, 1);
        System.out.println("Put key 1 value=" + 1); // put: 1
        System.out.println(cache.map);
        cache.put(2, 2);
        System.out.println("Put key 2 value=" + 2); // put: 1
        System.out.println(cache.map);
        // Retrieve values
        System.out.println("Getting key 1 value=" + cache.get(1)); // Output: 1
        System.out.println(cache.map);
        // System.out.println("Getting key 2 value=" + cache.get(2)); // Output: 2
        // System.out.println(cache.queue);

        // Insert a new key-value pair, evicts key 2 since it is least frequently used
        System.out.println("Put key 3 value=" + 3); // put: 3
        System.out.println("Insert a new key-value pair, evicts key 2 since it is least frequently used");
        cache.put(3, 3);
        System.out.println(cache.map);

        // Retrieve values, key 1 should be evicted
        System.out.println("Retrieve values, key 1 should be evicted, return " + cache.get(1)); // Output: -1

    }
}
