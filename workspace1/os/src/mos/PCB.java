package mos;

public class PCB {
	public String name; // ������
	public int ID; // ���̱�ʶ����ID=0��1��2������
	public int arrive_time; // ����ʱ��
	public int need_time; // ���Ʒ���ʱ��
	public int prio; // ����������������Խ�����ȼ�Խ��
	public int wait_time; // �ȴ�ʱ��
	public int more_time; // ʣ��ִ�е�ʱ��
	public int state; // ����״̬ ִ��--0������--1������--2�����--3������--4
	public PCB next; // ��������ָ��
	public int memoryNeed;
	public int printerNeed;
	int ctime = 0; // ���ʱ��
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

	// ���в�����Ƿ���� true��ʾ��ɣ�false��ʾû���
	public boolean run() {
		return run(1);
	}

	public String[] getMessage() {
		String a[] = { name, String.valueOf(arrive_time), String.valueOf(need_time), String.valueOf(memoryNeed),
				String.valueOf(printerNeed), String.valueOf(wait_time) };
		return a;
	}

	// {"��������", "����ʱ��", "����ʱ��","���ʱ��", "��תʱ��","��Ȩ��תʱ��"}
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