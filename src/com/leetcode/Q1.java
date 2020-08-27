package com.leetcode;

import java.util.HashMap;
import java.util.Map;
/**
 * 两数之和
 */
public class Q1 {
	/*
	 给定一个整数数组 nums 和一个目标值 target，请你在该数组中找出和为目标值的那 两个 整数，并返回他们的数组下标。
	你可以假设每种输入只会对应一个答案。但是，数组中同一个元素不能使用两遍。
	示例:
		给定 nums = [2, 7, 11, 15], target = 9
		因为 nums[0] + nums[1] = 2 + 7 = 9
		所以返回 [0, 1]
	 */
	
	public void doTest() {
		int size = 30*10000;
		
		int[] nums = new int[size]; 
		for (int i = 0; i < nums.length; i++) {
			nums[i] = i + 3;//随便,反正不要和下标一样
		}
		
		int index1 = nums.length - 2;
		int index2 = nums.length - 1;
		int target = nums[index1] + nums[index2];
		
		int[] twoSum = twoSum2(nums,target);
		
		System.out.println("期望:" + index1 + "," + index2);
		System.out.println("结果:" + twoSum[0] + "," + twoSum[1]);
	}
	
	/**
	 * 暴力法,耗时高
	 * @param nums
	 * @param target
	 * @return
	 */
	public int[] twoSum1(int[] nums, int target) {
        for(int i = 0;i < nums.length;i++){
           for(int j = i + 1;j < nums.length;j++){
                if(nums[i] + nums[j] == target){
                    return new int[]{i,j};
                }
            }
        }

        return new int[]{-1,-1};
    }
	
	/**
	 * 巧用hash,耗时低,但需要内存支撑
	 * @param nums
	 * @param target
	 * @return
	 */
	public int[] twoSum2(int[] nums, int target) {
		Map<Integer,Integer> map = new HashMap<>();
		
        for(int i = 0;i < nums.length;i++){
        	Integer index = map.get(target - nums[i]);
        	if(index != null) {
        		return new int[]{index,i};
        	}else {
        		map.put(nums[i], i);
        	}
        }

        return new int[]{-1,-1};
    }
}
