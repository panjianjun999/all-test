package com.leetcode;

import java.util.HashSet;
import java.util.Set;

/**
 * 无重复字符的最长子串
 */
public class Q3 {
	/*
	给定一个字符串，请你找出其中不含有重复字符的 最长子串 的长度。
	示例 1:
	输入: "abcabcbb"
	输出: 3 
	解释: 因为无重复字符的最长子串是 "abc"，所以其长度为 3。
	*/
	
	public void doTest() {
		String str = "dvdf";
		System.out.println("str=" + str);
		System.out.println("rs=" + lengthOfLongestSubstring(str));
	}
	
	public int lengthOfLongestSubstring(String s) {
		int rs = 0;
		Set<Character> set = new HashSet<>();
		for (int i = 0; i < s.length(); i++) {
			for (int j = i; j < s.length(); j++) {
				char c = s.charAt(j);
				if(set.contains(c)) {//重复
					rs = Math.max(rs, set.size());
					set.clear();
					break;
				}else {
					set.add(c);
					rs = Math.max(rs, set.size());
					
					if(j == s.length() - 1){//本轮到尾了,肯定是最长的了
						return rs;
					}
				}
			}
		}
		
		return rs;
    }
}
