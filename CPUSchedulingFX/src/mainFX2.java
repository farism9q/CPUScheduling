import java.awt.Color;
import java.awt.TextField;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;

import com.sun.prism.Image;

import PQ.LinkedPQ;
import PQ.PQElement;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class mainFX2 extends Application{
	int burst_time[], waiting_time[],tat[];
	float wait_avg,TAT_avg;
	String process[];
	int nbPro = 0;
	int totalBT=0;
	int TimeQuantum = 0; //Quantum number
	int outputGantt = 0;
	Parent root;
	Scene scene;

	boolean RRisSelected;
	String processRR[];
	int s = 0;
	@FXML
	RadioButton SRTF, RR, Priority;
	@FXML
	Button selectFileBtn;
	@FXML
	Label quantumLbl;
	@FXML
	javafx.scene.control.TextField quantumTxt;
	@FXML
	BorderPane pane = new BorderPane();
	@FXML
	Label avg_TATlbl, avg_waitinglbl;
	@FXML
	javafx.scene.control.TextField avg_TAT, avg_waiting;

    /**
     * @wbp.parser.entryPoint
     */
    @Override
    public void start(Stage stage) throws Exception {

        stage.setTitle("CPU Scheduling");
        root = FXMLLoader.load(getClass().getResource("/main.fxml"));
        scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
        

      
       
        
    }

    /**
     * @wbp.parser.entryPoint
     */
    public static void main(String[] args) {
        launch(args);
    }
    public void SRTFComp(ActionEvent eve) {
		if(SRTF.isSelected()) {
			quantumLbl.setVisible(false);
			quantumTxt.setVisible(false);
		}
	}
    
    public void RRComp(ActionEvent eve) {
		if(RR.isSelected()) {
			quantumLbl.setVisible(true);
			quantumTxt.setVisible(true);
		}
	}
    
    public void PriorityComp(ActionEvent eve) {
		if(Priority.isSelected()) {
			quantumLbl.setVisible(false);
			quantumTxt.setVisible(false);
		}
	}
    
    public void selectFileBtnComp(ActionEvent eve) {
    	JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File("C:\\Users\\HP\\Documents\\csc227 files"));
		int respone = fileChooser.showOpenDialog(null); //returns 0 or 1, (0 == has chosen a file, 1 == cancelled or pressed EXIT button
		if(respone == 0) { 
		if(SRTF.isSelected()) {
    		try {
				SRTF(fileChooser.getSelectedFile().getAbsolutePath());
//				System.out.println("here avgs: " + wait_avg + TAT_avg);
				gantt();
				

			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
    	}
    	else if(RR.isSelected()) {
    		try {
    			
				RR(fileChooser.getSelectedFile().getAbsolutePath(), Integer.parseInt(quantumTxt.getText()));
				gantt();
			} catch (IOException e2) {
				e2.printStackTrace();
			}
    	}
    		else if(Priority.isSelected()) {
    		try {
    			Priority(fileChooser.getSelectedFile().getAbsolutePath());
				gantt();
				 
			} catch (IOException e3) {
					e3.printStackTrace();
				}              
    		}
		
    }
    }
    
    public void SRTF(String f) throws FileNotFoundException {
    	try{
			File file = new File(f);
		Scanner reader = new Scanner(file);
		int completion_time[],k = 0,i,j,n,total=0,total_comp=0,pos,temp;
		String tempString;
		n = 30; //assume maximum number of processes is 30
		process = new String[n];
		burst_time = new int[n];
		waiting_time = new int[n];
		tat = new int[n];
		completion_time=new int[n];
		while(reader.hasNextLine()) {
				String processName = reader.nextLine(); // reading the first row, which is the process's name
				String burstTimeAndPriority[] = new String[2]; // an array for process burst time and priority
				burstTimeAndPriority = reader.nextLine().split(","); // Splitting the second row, the first one is gonna be burst time, second one is priority if it exists 
				int burstTime = Integer.parseInt(burstTimeAndPriority[0]); // converting burst time to an integer, because reading from file always as string 
				totalBT+=burstTime; // we need the total burst time to help us design the gantt chart
				process[k] = processName; 
				burst_time[k++] = burstTime;
				nbPro++;
			}
		// Selection sort
		for(int x=0;x<nbPro;x++) {
			pos=x;
		for(j=x+1;j<nbPro;j++) {
		if(burst_time[j]<burst_time[pos])// searching for the lowest burst time
			pos=j;
		}// sorting based on lowest burst time (lowest burst time is going to be the first in the array)
		temp=burst_time[x];
		burst_time[x]=burst_time[pos];
		burst_time[pos]=temp;
		// sorting the process name 
		tempString=process[x];
		process[x]=process[pos];
		process[pos]=tempString;
		}

		for(i=1;i<nbPro;i++)
		{
		completion_time[i]=0; 
		for(j=0;j<i;j++)
		completion_time[i]+=burst_time[j]; // you can see completion time when a process stops(same as tat when all processes arrived same time)
		 total_comp+=completion_time[i]; // here we increment total completion time 
		}
		
		 
		//First process has 0 waiting time
		waiting_time[0]=0;
		//calculate waiting time
		for(i=1;i<nbPro;i++)
		{
		waiting_time[i]=0;
		for(j=0;j<i;j++)
		waiting_time[i]+=burst_time[j]; // counting waiting time for each process by collecting the previous processes burst time
		total+=waiting_time[i]; // counting the total waiting time
		}
		
		//Calculating Average waiting time
		wait_avg=(float)total/nbPro; 
		total=0; // after calculating avg waiting time, we want to calculate the avg of turnaround time 
		 
		System.out.println("\nPro_number\t Burst Time \tcompletion_time\tWaiting Time\tTurnaround Time");
		for(i=0;i<nbPro;i++)
		{
		tat[i]=burst_time[i]+waiting_time[i]; // tat is turnaround time and it is the total of burst time + waiting time of a process
		 //Calculating Turnaround Time
		total+=tat[i];
		System.out.println("\n"+process[i]+"\t\t "+burst_time[i]+"\t\t "+completion_time[i]+"\t\t"+waiting_time[i]+"\t\t "+tat[i]);
		}
		 
		//Calculation of Average Turnaround Time
		TAT_avg=(float)total/nbPro;
		System.out.println("\n\nAWT: "+wait_avg);
		System.out.println("\nATAT: "+TAT_avg);
		}catch (Exception e) {
			System.out.println("an error occured");
			e.printStackTrace();
		}
	}
    public void RR(String f, int quantNum)throws IOException {
    	try{
			File file = new File(f);
			Scanner reader = new Scanner(file);			
			int n = 30;
			process=new String[n];
			burst_time=new int[n];
			int burst_time_demo[] = new int[n]; // we will use it to help us do the algorithm
			waiting_time=new int[n];
			tat=new int[n];
			nbPro = 0;
			int sum=0, q=0;
			wait_avg=0;
       		TAT_avg=0;
			TimeQuantum = quantNum;
			processRR = new String[90]; // this array needed when designing gantt chart for RR
			

			
			while(reader.hasNextLine()) {
				String processName = reader.nextLine(); 
				String burstTimeAndPriority[] = new String[2];
				burstTimeAndPriority = reader.nextLine().split(",");
				int burstTime = Integer.parseInt(burstTimeAndPriority[0]); //
				totalBT+=burstTime;
				process[q] = processName;
				burst_time[q] = burstTime;
				burst_time_demo[q++] = burstTime;

				nbPro++;
			}
			System.out.println("\nPro_number\t Burst Time\tWaiting Time\tTurnaround Time");
							
			do{
		    	for(int i=0;i<nbPro;i++)
		    	{
		     		if(burst_time_demo[i]>TimeQuantum) // if the burst time greater than quantum number 
		     		{
			     		processRR[s++] = process[i];
			     		outputGantt++;
		     			burst_time_demo[i]-=TimeQuantum; //decreasing process's burst time
		      		  	for(int j=0;j<nbPro;j++)
		      			{
		       				if((j!=i)&&(burst_time_demo[j]!=0)) { //increasing "the waiting time" for the advanced processes BY QUANTUM NUMBER
		        			waiting_time[j]+=TimeQuantum; // increasing the waiting time by "quantum number"

		       				}
		         		}
		          	}
		           	else
		           	{ 		    
		           		for(int j=0;j<nbPro;j++)
		           		{
		            			if((j!=i)&&(burst_time_demo[j]!=0)) { // increasing "the waiting time"for the other processes BY BURST TIME
		             			waiting_time[j]+=burst_time_demo[i]; // increasing the waiting time by "quantum number"

		            			}
		              		} 
		           		if(burst_time_demo[i] != 0) {
		           			processRR[s++] = process[i];
		           			outputGantt++;
		           		}

		           		burst_time_demo[i]=0; // making the burst time equal to zero, because the burst time is smaller than the quantum number 
	   
							
		                } 

		       }
		               sum=0; 
		               for(int k=0;k<nbPro;k++) 
	            		sum=sum+burst_time_demo[k]; // sum is the total burst time for every process (we need it to check if we finished the total burst time)
		    } while(sum!=0); // we will not enter this while loop until we finish the total burst time for every process 
		                
		             for(int i=0;i<nbPro;i++) {
		                	 tat[i]=waiting_time[i]+burst_time[i]; //the array a is the burst time of each process
		                	 wait_avg+=waiting_time[i];
		                      TAT_avg+=tat[i]; 
		 					System.out.println("\n"+process[i]+"\t\t "+burst_time[i]+"\t "+"\t"+waiting_time[i]+"\t\t "+tat[i]);

		             }
		               
		               TAT_avg = TAT_avg / nbPro;
		               wait_avg = wait_avg / nbPro;
		           	System.out.println("\n\nAWT: "+wait_avg);
		    		System.out.println("\nATAT: "+TAT_avg);
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("something went wrong ");
		}
		
	}
    public void Priority(String f) throws IOException {
    	try{	
    		File file = new File(f);
    		Scanner reader = new Scanner(file);	
    		nbPro = 0;
            int nbP; 
            nbPro = 0;
            wait_avg = 0;
            TAT_avg = 0;
            
            
        	LinkedPQ<Integer> pq = new LinkedPQ<Integer>();
        	PQElement<Integer> pqElement = new PQElement<Integer>(0,0,""); // needed for saving node elements
            

            nbP = 30; // here we are assuming max processes are 30
            waiting_time = new int[nbP];
            tat = new int[nbP];
            burst_time = new int [nbP];
            process = new String[nbP];
            int totalBurstTime = 0;
            while(reader.hasNextLine()) {
    			String processName = reader.nextLine();
    			process[nbPro] = processName;
    			String burstTimeAndPriority[] = new String[2];
    			burstTimeAndPriority = reader.nextLine().replaceAll(" ", "").split(",");
    			int bt = Integer.parseInt(burstTimeAndPriority[0]);
            	int pt = Integer.parseInt(burstTimeAndPriority[1]);
            	totalBT+=bt;
            	pq.enqueue(bt, pt, processName);
            	nbPro++;
    		}
    		System.out.println("\nPro Number\t Burst Time\tWaiting Time\tTurnaround Time");

            for(int i = 0; i<nbPro; i++) {
            	pqElement = pq.serve();
            	process[i] = pqElement.name;
            	totalBurstTime += pqElement.data; // totalBurstTime means TAT
            	burst_time[i] = pqElement.data;
            	tat[i] = totalBurstTime;
            	waiting_time[i] = totalBurstTime - pqElement.data;
            	TAT_avg += tat[i];
            	wait_avg += waiting_time[i];
    			System.out.println("\n"+process[i]+"\t\t "+burst_time[i]+"\t "+"\t"+waiting_time[i]+"\t\t "+tat[i]);

            }
            TAT_avg = TAT_avg/ nbPro;
            wait_avg = wait_avg / nbPro;
        	System.out.println("\n\nAWT: "+wait_avg);
    		System.out.println("\nATAT: "+TAT_avg);
           
        }catch (Exception e) {
        	e.printStackTrace();
        	System.out.println("something went wrong ");
        }
    	
    }

	

public void gantt() {
	ArrayList<String> color = new ArrayList<String>();
	  HBox hbox = new HBox();
      HBox time = new HBox();
	  hbox.setMinWidth(700);
      hbox.setSpacing(1);
      time.setMinWidth(700);
      Text text = new Text("Gantt chart");
      text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 16));
      int trackBurstTime =0;
      
      TableView<DataTableView> tableView = new TableView<DataTableView>();
      
            
      
      TableColumn<DataTableView, String> processName   = 
              new TableColumn<>("process Name");
          TableColumn<DataTableView, String> burstTime = 
              new TableColumn<>("Burst Time");
          TableColumn<DataTableView, String> waitingTime       = 
              new TableColumn<>("Waiting Time");
          TableColumn<DataTableView, String> turnaroundTime  = 
              new TableColumn<>("Turnaround Time");
          
         
         
          
          processName.setCellValueFactory(new PropertyValueFactory<DataTableView, String>("proName"));
          burstTime.setCellValueFactory(new PropertyValueFactory<DataTableView, String>("bt"));
          waitingTime.setCellValueFactory(new PropertyValueFactory<DataTableView, String>("wt"));
          turnaroundTime.setCellValueFactory(new PropertyValueFactory<DataTableView, String>("tat"));
          

      tableView.setColumnResizePolicy(tableView.CONSTRAINED_RESIZE_POLICY); 
      
      tableView.getColumns().add(processName);
      tableView.getColumns().add(burstTime);
      tableView.getColumns().add(waitingTime);
      tableView.getColumns().add(turnaroundTime);
      
      tableView.setMaxSize(700, 300);

      float width = (float)700/totalBT;


    rand_col(color, nbPro);
	hbox.setMinWidth(700);
    hbox.setSpacing(1);
    time.setMinWidth(700);

    Text txt = new Text("0");
    txt.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 10));
    time.getChildren().add(txt);
    if(RR.isSelected()) {
    	int btRR[] = new int[nbPro];
    	int preTracBT = 0;
    	for(int j = 0; j<btRR.length; j++) {
    		btRR[j] = burst_time[j];
    	}
    	
//    	for(int j = 0; j<processRR.length; j++) {
//    		System.out.println("here proRR: "+processRR[j]);
//    	}
    	 for(int i = 0; i<outputGantt; i++) {
    	    	
    		 int cou = strToInt(processRR[i])-1; // cou is the index of process 
    		 if(burst_time[cou] <= TimeQuantum) {
//    			 System.out.println("burst time: "+burst_time[cou]);
      	    	trackBurstTime+=burst_time[cou]; 
    		 }
     		 else {
     	    	trackBurstTime+=TimeQuantum;
      	    	burst_time[cou] -= TimeQuantum;

     		 }
    		 
//  	    	System.out.println("TrackBurstTime: "+trackBurstTime);

     		     	    	Text time_txt = new Text(Integer.toString(trackBurstTime)); //burstTime as text
    	        time_txt.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 10));
    	        time.getChildren().add(time_txt);
    	        
    	    	Button btn = new Button(processRR[i]);


    	        if( i == 0) {
    	            btn.setMinWidth(trackBurstTime*width);
    	            HBox.setMargin(time_txt, new Insets(0, 0, 0,
    	            		trackBurstTime*width-12));
    	        }
    	        else {
    	            btn.setMinWidth((trackBurstTime - preTracBT)*width);
//    	            System.out.println("(trackBurstTime - (trackBurstTime - TimeQuantum))" + (trackBurstTime - (trackBurstTime - TimeQuantum)) );
    	            HBox.setMargin(time_txt, new Insets(0, 0, 0,
    	            		(trackBurstTime - preTracBT)*width - 5));
    	          
    	        }

    	        int num = strToInt(processRR[i]);
    	        btn.setStyle(color.get(num-1)); // num-1 because idnex is zero based
    	    	hbox.getChildren().add(btn);
    	    	preTracBT = trackBurstTime;
    	 }
    	
    	
//	    	here, we're adding data into table 
     	for(int j = 0; j<nbPro; j++) {
	    	tableView.getItems().add(new DataTableView(process[j], Integer.toString(btRR[j]), waiting_time[j], tat[j]));
    	    }
     	

    	
    	
    }
    else {
//	System.out.println(nbPro);
    for(int i = 0; i<nbPro; i++) {
    	trackBurstTime+=burst_time[i];
    	Text time_txt = new Text(Integer.toString(trackBurstTime)); //burstTime as text
        time_txt.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 10));
        time.getChildren().add(time_txt);
        
    	Button btn = new Button(process[i]);

//    	here, we're adding data into table 
    	tableView.getItems().add(new DataTableView(process[i], Integer.toString(burst_time[i]), waiting_time[i], tat[i]));


        if( i == 0) {
            btn.setMinWidth(burst_time[i]*width);
            HBox.setMargin(time_txt, new Insets(0, 0, 0,
            		trackBurstTime*width-12));
        }
        else {
            btn.setMinWidth((trackBurstTime - (trackBurstTime-burst_time[i]))*width-12);
        	 HBox.setMargin(time_txt, new Insets(0, 0, 0,
             		(trackBurstTime - (trackBurstTime-burst_time[i]))*width-12));
        }
        
    	btn.setStyle(color.get(i));
    	hbox.getChildren().add(btn);
    }	
    }


    
//    showing averages of TAT and waiting time
	avg_TATlbl.setVisible(true); avg_waitinglbl.setVisible(true);
	avg_TAT.setText(Float.toString(TAT_avg)); avg_waiting.setText(Float.toString(wait_avg));
	avg_TAT.setVisible(true); avg_waiting.setVisible(true);
 
	
     
    VBox vbox_bottom = new VBox(10, text, hbox, time);
    vbox_bottom.setPadding(new Insets(10));
    pane.setTop(vbox_bottom);
    pane.setCenter(tableView);
    
    totalBT = trackBurstTime = nbPro = 0;
    

    
 

  }
public void rand_col(ArrayList<String> color, int n) {
    for( int i=0;i<n;i++ ) {
        Random rand = new Random();
        float r = rand.nextFloat() / 2F + 0.5F;
        float g = rand.nextFloat() / 2F + 0.5F;
        float b = rand.nextFloat() / 2F + 0.5F;
        Color randcol = new Color(r, g, b);
        String hex = String.format("#%02x%02x%02x", randcol.getRed(), randcol.getGreen(), randcol.getBlue());
        String col = "-fx-background-color:" + hex + ";";
        color.add(col);
    }
}
// helping us to choose random colors
private int strToInt(String s) {
	int num = 0;
	for(int i = 0; i<s.length(); i++) {
		if(Character.isDigit(s.charAt(i))) {
			num = Character.getNumericValue(s.charAt(i));
		}
			
	}

	return (int) num;
}
}
