package org.vizzoid.utils.algorithm;

public class BinarySearch {

    public static boolean perform(int[] search, int target) {
        return perform(search, search.length / 2, target, search.length - 1);
    }

    /**
     * @return if found
     */
    public static boolean perform(int[] search, int mid, int target, int max) {
        int mid0 = search[mid];
        if (mid0 == target) return true;
        if (mid == 0 || mid == max) return false;

        int minOrMax;
        int newMax;

        if (mid0 > target) {
            minOrMax = 0;
            newMax = mid;
        } else {
            minOrMax = max + 1;
            newMax = max;
        }

        return perform(search, ((mid + minOrMax) / 2), target, newMax);
    }

}
