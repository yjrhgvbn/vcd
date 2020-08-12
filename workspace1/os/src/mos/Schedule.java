package mos;

import mos.NodeList;

public class Schedule {

	public enum Rule {
		SJF, SJFP, FCFS
	};// 非抢占短进程,抢占短进程,先来先服务

	Rule rule;// 算法类型，0,1,2 短进程的抢占，非抢占，还有先来先服务
	public NodeList run, ready, wait, finish;
	public NodeList create = new NodeList(5); // 创建队列
	public NodeList enter = new NodeList(4); // 输入队列
	public int memoryLeft = 50, printerLeft = 50;
	public int memoryMax = 50, printerMax = 50;

	public Schedule(Rule rule) {
		this.rule = rule;
		run = new NodeList(0);
		ready = new NodeList(1);
		wait = new NodeList(2);
		finish = new NodeList(3);
	}

	public void setRule(Rule rule) {
		this.rule = rule;
	}

	public void initResource(int memory, int printer) {
		if (memoryLeft - memoryMax + memory < 0) {
			memoryMax = memoryMax - memoryLeft;
			memoryLeft = 0;
		} else {
			memoryLeft = memoryLeft - memoryMax + memory;
			memoryMax = memory;
		}
		if (printerLeft - printerMax + printer < 0) {
			printerMax = printerMax - printerLeft;
			printerLeft = 0;
		} else {
			printerLeft = printerLeft - printerMax + printer;
			printerMax = printer;
		}
	}

	// 加入就绪或创建队列，同时处理抢占事件
	public void add(String name, int arrive_time, int need_time, int memoryNeed, int printerNeed) {
		enter.AddToTail(enter.CreatePCB(name, arrive_time, need_time, memoryNeed, printerNeed));
		if (memoryNeed > memoryLeft || printerNeed > printerLeft) {
			create.AddToTail(ready.CreatePCB(name, arrive_time, need_time, memoryNeed, printerNeed));
			return;
		}
		memoryLeft -= memoryNeed;
		printerLeft -= printerNeed;
		if (rule == Rule.SJFP && run.minTime() > need_time) {
			ready.AddToTail(run.PopHead());
			run.AddToTail(ready.CreatePCB(name, arrive_time, need_time, memoryNeed, printerNeed));
			return;
		}
		ready.AddToTail(ready.CreatePCB(name, arrive_time, need_time, memoryNeed, printerNeed));
	}

	// 运行队列的运行和添加，处理：完成事件，资源释放，创建队列转换，
	public void run() {
		// 运行队列为空时添加
		if (run.size == 0) {
			if (rule == Rule.FCFS) {
				run.AddToTail(ready.PopHead());
			} else {
				run.AddToTail(ready.PobShortest());
			}
		}
		// 更新下等待时间
		ready.run();
		wait.run();
		finish.run();
		create.run();
		// 有完成
		if (run.run() != 0) {
			PCB p = run.PopComple();
			memoryLeft += p.memoryNeed;
			printerLeft += p.printerNeed;
			finish.AddToTail(p);
			// 先处理创建队列转换，
			PCB cp = create.head;
			while (cp != null && cp.next != null) {
				if (cp.next.memoryNeed <= memoryLeft && cp.next.printerNeed <= printerLeft) {
					memoryLeft -= cp.next.memoryNeed;
					printerLeft -= cp.next.printerNeed;
					PCB tp = cp.next;
					cp.next = tp.next;
					tp.next = null;
					ready.AddToTail(tp);
				}
				cp = cp.next;
			}
			if (rule == Rule.FCFS) {
				run.AddToTail(ready.PopHead());
			} else {
				run.AddToTail(ready.PobShortest());
			}
		}

	}

	public void block() {
		wait.AddToTail(run.PopHead());
		if (rule == Rule.FCFS) {
			run.AddToTail(ready.PopHead());
		} else {
			run.AddToTail(ready.PobShortest());
		}
	}

	public void awake() {
		ready.AddToTail(wait.PopHead());
	}

	public void test() {
		ready.PopHead();
	}

	public void rest() {
		run.init();
		ready.init();
		wait.init();
		enter.init();
		create.init();
		finish.init();
		memoryLeft = memoryMax;
		printerLeft = printerMax;
	}

	public static void main(String[] agrs) {
		Schedule s = new Schedule(Rule.FCFS);
		System.out.print("就绪");
		s.ready.show();
		s.run();
		System.out.print("就绪");
		s.ready.show();
		System.out.print("执行");
		s.run.show();
		s.run();
		System.out.print("就绪");
		s.ready.show();
		System.out.print("执行");
		s.run.show();
	}
}
