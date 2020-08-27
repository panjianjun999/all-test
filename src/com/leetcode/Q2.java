package com.leetcode;

import java.util.Arrays;
import java.util.List;

/**
 * 两数相加
 * @author Pan
 * @version 创建时间：2020年8月14日 上午11:04:48
 */
public class Q2 {
	public static class ListNode {
		int val;
		ListNode next;
		ListNode(int x) { val = x; }
		
		public String toString() {
			StringBuffer sb = new StringBuffer();
			
			sb.append(val);
			if(next != null) {
				sb.append(next.toString());
			}
			
			return sb.toString();
		}
	}
	
	/*
	给出两个 非空 的链表用来表示两个非负的整数。其中，它们各自的位数是按照 逆序 的方式存储的，并且它们的每个节点只能存储 一位 数字。
	如果，我们将这两个数相加起来，则会返回一个新的链表来表示它们的和。
	您可以假设除了数字 0 之外，这两个数都不会以 0 开头。
	示例：
	输入：(2 -> 4 -> 3) + (5 -> 6 -> 4)
	输出：7 -> 0 -> 8
	原因：342 + 465 = 807
	*/
	
	public void doTest() {
		List<Integer> i1 = Arrays.asList(9);
		ListNode l1 = intToNode(i1);
		System.out.println(i1 + "-->i1=" + l1.toString());
		
		List<Integer> i2 = Arrays.asList(9,9,9,9,9,9,9,9,9,1);
		ListNode l2 = intToNode(i2);
		System.out.println(i2 + "-->i2=" + l2.toString());
		
		ListNode l12 = addTwoNumbers(l1,l2);
		System.out.println("iall=" + l12.toString());
	}
	
	public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
		ListNode rs = null;
		ListNode rs_last = null;
		
		boolean isAddH = false;//高位是否要进1
		ListNode l1_last = l1;
		ListNode l2_last = l2;
		while (l1_last != null || l2_last != null || isAddH) {
			int sum = 0;//当前位总数,2个1位数相加,范围:0~18
			if(l1_last != null) {
				sum += l1_last.val;
				
				l1_last = l1_last.next;
			}
			
			if(l2_last != null) {
				sum += l2_last.val;
				
				l2_last = l2_last.next;
			}
			
			if(isAddH) {//高位进1
				sum += 1;	
			}
			
			ListNode next = new ListNode(sum%10);//余数就是当前位
			
			isAddH = (sum >= 10);//下一个高位是否要进1
			
			if(rs == null) {
				rs = next;
			}else {
				rs_last.next = next;
			}
			
			rs_last = next;
		}
		
		return rs;
    }
	
	/**
	 * 
	 * @param iis 代表一个数字,如{3,4,2}=342
	 * @return
	 */
	public ListNode intToNode(List<Integer> iis) {
		ListNode rs = null;
		ListNode last = null;
		for (int i = 0; i < iis.size(); i++) {
			int val = iis.get(iis.size() - 1 - i);
			ListNode next = new ListNode(val);
			
			if(rs == null) {
				rs = next;
			}else {
				last.next = next;
			}
			
			last = next;
		}
		
		return rs;
	}
}
