import java.util.*;

/**
 * Design and implement a least-frequently used cache.
 * The cache should have a configurable hard limit of maximum number of elements.
 */

    /*
        capacity : 2

        put(1, 100)
        { 1 : 100}

        put(2, 200)
        { 2 : 200}

        get(1) -> 100

        put(3, 300)

        // expected
        {2 : 200, 3:300} < - actually asking for

        get(1) -> throw NoSuchElementException();

    */
public class LFUCache {
    class Item implements Comparable<Item> {
        int count;
        long time;
        int key;
        int value;

        public Item(int key, int value) {
            this.key = key;
            this.value = value;
            count = 1;
            this.time = new Date().getTime();
        }

        @Override
        public int compareTo(Item o) {
            if(count > o.count) return 1;
            else if (count < o.count) return -1;
            else {
                return time == o.count ? 0 : time > o.count ? 1 : -1;
            }
        }

        @Override
        public String toString() {
            return ""+value + "(count="+count+")" + "(last time used in millis="+time+")";
        }
    }

    private int capacity;
    private PriorityQueue<Item> queue;
    private Map<Integer, Item> map;

    LFUCache(int capacity) {
        this.capacity = capacity;
        queue = new PriorityQueue<>(capacity);
        map = new HashMap<>();
    }

    int get(int key) {
        if(!map.containsKey(key)) {
            // throw new NoSuchElementException();
            return -1;
        }
        Item item = map.get(key);
        item.count++;
        item.time = new Date().getTime();
        return item.value;
    }

    void put(int key, int value) {
        if(map.containsKey(key)) {
            Item item = map.get(key);
            item.count++;
            item.time = new Date().getTime();
            item.value = value;
        } else {
            if(map.size() == capacity) {
                Item item = queue.poll();
                map.remove(item.key);
            }
            Item item = new Item(key, value);
            map.put(key, item);
            queue.add(item);
        }
    }

    public static void main(String[] args) {
        // Create a LFU cache with capacity 2
        LFUCache cache = new LFUCache(2);

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

        // Insert a new key-value pair, evicts key 2 since it is least frequently used
        System.out.println("Put key 3 value=" + 3); // put: 3
        System.out.println("Insert a new key-value pair, evicts key 2 since it is least frequently used");
        cache.put(3, 3);

        System.out.println(cache.map);

        // Retrieve values, key 1 should be evicted
        System.out.println("Retrieve values, key 1 should be evicted, return " + cache.get(1)); // Output: -1

    }
}
