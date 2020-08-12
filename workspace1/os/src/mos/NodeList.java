package mos;

import java.util.ArrayList;

import mos.PCB;

public class NodeList {
	public PCB head;
	public int size = 0;
	int leftID = 0;
	int type; // 0,1,2,3--run，ready，wait，finish 表示运行队列、就绪队列、阻塞队列和完成队列,
				// 4 表示其他队列,5创建队列。

	public NodeList(int type) {
		this.type = type;
		head = new PCB("head", 0, 0, 0, 0, 0);
	}

	public void init() {
		leftID = 0;
		size = 0;
		head.next = null;
	}

	public PCB CreatePCB(String name, int arrive_time, int need_time, int memoryNeed, int printerNeed) {
		PCB p = new PCB(name, leftID, arrive_time, need_time, memoryNeed, printerNeed);
		leftID++;
		p.SetState(1);
		return p;
	}

	public void AddToTail(PCB p) {
		if (p == null)
			return;
		p.state = type;
		size++;
		if (head.next == null) {
			head.next = p;
		} else {
			PCB tp = head.next;
			while (tp.next != null)
				tp = tp.next;
			tp.next = p;
		}
	}

	// 返回完成的进程，null空表示没有完成
	public PCB PopComple() {
		if (head.next == null)
			return null;
		else {
			PCB p = head;
			while (p.next != null) {
				if (p.next.state == 3) {
					PCB cp = p.next;
					p.next = cp.next;
					cp.next = null;
					size--;
					return cp;
				}
			}
			return null;
		}
	}

	public PCB PopHead() {
		if (head.next == null)
			return null;
		else {
			PCB p = head.next;
			head.next = p.next;
			p.next = null;
			size--;
			return p;
		}
	}

	// 取出服务时间最短的进程
	public PCB PobShortest() {
		return PobShortest(0);
	}

	// 根据规则取出服务时间最短的进程 0取最短服务时间，其他取最长服务时间
	public PCB PobShortest(int rule) {
		if (head.next == null)
			return null;
		PCB minHP = head;// minHP为最小节点或大的头一节点
		PCB p = head.next;
		while (p.next != null) {
			if (compare(p.next.need_time, minHP.next.need_time, rule)) {
				minHP = p;
			}
			p = p.next;
		}
		p = minHP.next;
		minHP.next = p.next;
		p.next = null;
		size--;
		return p;
	}

	private boolean compare(int x, int y, int rule) {
		if (rule == 0)
			return x < y;
		return x > y;
	}

	// 返回值表示完成了几个
	public int run() {
		int count = 0;
		PCB p = head.next;
		while (p != null) {
			if (p.run())
				count++;
			p = p.next;
		}
		return count;
	}

	public int minTime() {
		PCB minHP = head.next;
		PCB p = head.next;
		if (p == null)
			return 999999;
		while (p.next != null) {
			if (compare(p.next.need_time, minHP.need_time, 0)) {
				minHP = p;
			}
			p = p.next;
		}
		return minHP.need_time;
	}

	public String[][] getTable() {
		// (isChange == false && type != 0) ||

		PCB p = head.next;
		ArrayList<String[]> table = new ArrayList<String[]>();
		while (p != null) {
			// 完成队列特殊处理
			if (type == 3)
				table.add(p.getComplete());
			else if (type == 0)
				table.add(p.getRun());
			else
				table.add(p.getMessage());
			p = p.next;
		}
		return table.toArray(new String[0][]);
	}

	public void show() {
		PCB p = head.next;
		System.out.println("队列：");
		while (p != null) {
			System.out.println(p.name + " " + p.arrive_time + " " + p.need_time + " " + p.more_time);
			p = p.next;
		}
	}
}
