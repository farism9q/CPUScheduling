public class DataTableView{


	
	int n = 0;

	int nbProcesses = 0;
	String process[];
	int burst_time[];
	int waitingTime[];
	int TAT[];
	int completionTime[];
	float wait_avg; // average waiting time
	float tat_avg; // average turnaround time
	
	String proName = "", empty, empty2, bt;
	int wt, tat;
	float tatAv;

	float waitAvg;

	
// this constuctor for SRTF
	public DataTableView(String[] process ,int[] burstTime,int[] waitTime,
		 int[] turnAT) {
		burst_time = burstTime;
		this.process = process;
		waitingTime = waitTime;
		TAT = turnAT;	
	}
	public DataTableView(int nbP,String[] process ,int[] burstTime,int[] waitTime,
			 int[] turnAT, float awt, float atat) {
			burst_time = burstTime;
			nbProcesses = nbP;
			this.process = process;
			waitingTime = waitTime;
			TAT = turnAT;
			wait_avg = awt;
			tat_avg = atat;

				}
	public DataTableView(String proName, String bt, int wt, int tat) {
		this.proName = proName;
		this.bt = bt;
		this.wt = wt;
		this.tat = tat;
		
	}
	
	public DataTableView(String proName, String bt) {
		this.proName = proName;
		this.bt = bt;
	}
	
	public DataTableView(String proName, String bt, String empty, String empty2) {
		this.proName = proName;
		this.bt = bt;
		this.empty = empty;
		this.empty2 = empty2;
		
		
	}
	public String getProName() {
		return proName;
	}
	public String getEmpty() {
		return empty;
	}
	public String getEmpty2() {
		return empty2;
	}
	public int getWt() {
		return wt;
	}
	public String getBt() {
		return bt;
	}
	public int getTat() {
		return tat;
	}
	public float getTat_avg() {
		return tat_avg;
	}
	public float getWait_avg() {
		return wait_avg;
	}
	}
