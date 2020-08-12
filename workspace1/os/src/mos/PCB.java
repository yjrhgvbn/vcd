package mos;

public class PCB {
	public String name; // 进程名
	public int ID; // 进程标识数，ID=0、1、2、……
	public int arrive_time; // 到达时间
	public int need_time; // 估计服务时间
	public int prio; // 进程优先数，数字越大优先级越高
	public int wait_time; // 等待时间
	public int more_time; // 剩余执行的时间
	public int state; // 进程状态 执行--0、就绪--1、阻塞--2、完成--3、创建--4
	public PCB next; // 队列链接指针
	public int memoryNeed;
	public int printerNeed;
	int ctime = 0; // 完成时间
	int start_time = 0;

	public PCB(String name, int ID, int arrive_time, int need_time, int memoryNeed, int printerNeed) {
		init(name, ID, arrive_time, need_time, memoryNeed, printerNeed);
	}

	private void init(String name, int ID, int arrive_time, int need_time, int memoryNeed, int printerNeed) {
		this.name = name;
		this.ID = ID;
		this.arrive_time = arrive_time;
		this.need_time = need_time;
		this.more_time = need_time;
		this.state = 1;
		this.next = null;
		this.memoryNeed = memoryNeed;
		this.printerNeed = printerNeed;

	}

	public void SetState(int state) {
		this.state = state;
	}

	// 运行并检查是否完成 true表示完成，false表示没完成
	public boolean run() {
		return run(1);
	}

	public String[] getMessage() {
		String a[] = { name, String.valueOf(arrive_time), String.valueOf(need_time), String.valueOf(memoryNeed),
				String.valueOf(printerNeed), String.valueOf(wait_time) };
		return a;
	}

	// {"进程名称", "到达时间", "服务时间","完成时间", "周转时间","带权周转时间"}
	public String[] getComplete() {
		int cyc_time = ctime - arrive_time;
		double avg_cyc = (double) cyc_time / (double) need_time;
		String a[] = { name, String.valueOf(arrive_time), String.valueOf(need_time), String.valueOf(start_time),
				String.valueOf(ctime), String.valueOf(cyc_time), String.valueOf(avg_cyc) };
		return a;
	}

	public String[] getRun() {
		String a[] = { name, String.valueOf(arrive_time), String.valueOf(need_time), String.valueOf(memoryNeed),
				String.valueOf(printerNeed), String.valueOf(more_time) };
		return a;
	}

	public boolean run(int time) {
		if (state == 3 || state == 4)
			return true;
		if (state == 0) {
			if(more_time == need_time)
				start_time = arrive_time + wait_time;
			more_time -= time;
			if (more_time <= 0) {
				this.state = 3;
				ctime = arrive_time + wait_time + need_time;
				return true;
			}
		} else {
			wait_time += time;
		}
		return false;
	}

}