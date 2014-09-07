package org.magnum.dataup;

public class MaxVideoId {
    private static volatile MaxVideoId instance = new MaxVideoId();
    private static long maxId = 0L;
    
    public static long getMaxId() {
    	return maxId += 1L;
    }
 
    // private constructor
    private MaxVideoId() {
    }
 
    public static MaxVideoId getInstance() {
        return instance;
    }
}
