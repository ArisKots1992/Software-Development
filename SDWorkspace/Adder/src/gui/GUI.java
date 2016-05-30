package gui;

import java.awt.Dimension;
import java.awt.FontFormatException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.*;

import database.DBfunctions;
import memory.ActiveUsers;
import memory.IPMemory;
import memory.PatternMemory;

//@SuppressWarnings("serial")
public class GUI extends JFrame{

	DBfunctions dbfunctions = new DBfunctions();

	private Thread t;
	private Thread t1;
	private Thread t2;
	private Thread t3;
	private Thread t4;
	//private Font tabfont;
	private Listener actionlistener=new Listener(); // actionlistener that handles input*/
	//main screen contains a JTabbedpane with 6 tabs
	public JLabel tablabel=new JLabel();
	private JTabbedPane tabbedpane=new JTabbedPane();	//tabbedpanel
	// first tab contains 2 JTextfields, 2 JLabels  2 JButtons and a JTextArea
	public JTextField IpField=new JTextField("");
	public JTextField PatternField=new JTextField("");
	public JLabel Iplabel=new JLabel("Enter Malicious IP");
	public JLabel Patternlabel=new JLabel("Enter Malicious Pattern");
	public JButton InsertIp=new JButton("Insert IP");	
	public JButton InsertPat=new JButton("Insert Pattern");
	private StatsArea message=new StatsArea();
	//second tab contains 1 JButton, 1 JLabel  and 1 scrollable JTextArea
	public JButton Activeusers=new JButton("Active Users");
	public JLabel Userslabel=new JLabel("Press button to show Active Users");
	private StatsArea userstats=new StatsArea();            //scrollable JtextAreas
	//third tab contains 1 JButton, 1 JLabel  and 1 scrollable JTextArea
	public JButton MalIps=new JButton("Malicious IPs");
	public JLabel Maliplabel=new JLabel("Press button to show Malicious IPs");
	private StatsArea malipstats=new StatsArea();            //scrollable JtextAreas
	//fourth tab contains 1 JButton, 1 JLabel  and 1 scrollable JTextArea
	public JButton Malpats=new JButton("Malicious Patterns");
	public JLabel Malpatlabel=new JLabel("Press button to show Malicious Patterns");
	private StatsArea malpatstats=new StatsArea();            //scrollable JtextAreas
	//fifth tab contains 1 JButton, 1 JLabel  and 1 scrollable JSCrollPane
	public JButton Malipstatistics=new JButton("Malicious IP Statistics");
	public JLabel Malipstatslabel=new JLabel("Press button to show Malicious IP Statistics");
	public JScrollPane malipstatsstats=new JScrollPane();
	//fifth tab contains 1 JButton, 1 JLabel  and 1 scrollable JScrollPane
	public JButton Malpatstatistics=new JButton("Malicious Pattern Statistics");
	public JLabel Malpatstatslabel=new JLabel("Press button to show Malicious Pattern Statistics");
	public JScrollPane malpatstatsstats=new JScrollPane(); 
	
	public class Listener implements ActionListener{//GUI ActionListener
		
		public Listener(){
			
		}
		public void actionPerformed(ActionEvent ae){
			if(ae.getSource()==InsertIp){
				IPMemory.getInstance().add(IpField.getText());
				message.text.append("You just inserted Malicious IP :" + IpField.getText()+"\n");
				IpField.setText("");
			}
			else if(ae.getSource()==InsertPat){
				PatternMemory.getInstance().add(PatternField.getText());
				message.text.append("You just inserted Malicious Pattern :" + PatternField.getText()+"\n");
				PatternField.setText("");
			}
			else if(ae.getSource()==MalIps){
//				String acc = "";
//				for (String s : IPMemory.getInstance().get_ips() )
//					acc += s + "\n";
//				malipstats.text.setText(acc);
				Runnable r= new AutoRefreshThirdThread();
		        t2= new Thread(r);
		        t2.start();
			}
			else if(ae.getSource()==Malpats){
				Runnable r= new AutoRefreshFourthThread();
		        t3= new Thread(r);
		        t3.start();
			}
			else if(ae.getSource()==Activeusers){
				Runnable r= new AutoRefreshFifthThread();
		        t4= new Thread(r);
		        t4.start();
		        
			}
			else if(ae.getSource()==Malipstatistics){
				Runnable r= new AutoRefreshThread();
		        t= new Thread(r);
		        t.start();
			}
			else if(ae.getSource()==Malpatstatistics){
				Runnable r= new AutoRefreshSecondThread();
		        t1= new Thread(r);
		        t1.start();
			}
			
		}
	}
	class AutoRefreshFifthThread implements Runnable{
		
		public void run(){
			while(true){
				 DisplayActiveUsers();
				try{
					Thread.sleep(5000);
				}
				catch(InterruptedException ie){
				}
			}
		}		
	}
	class AutoRefreshFourthThread implements Runnable{
		public void run(){
			while(true){
				 DisplayPatTabStats();
				try{
					Thread.sleep(5000);
				}
				catch(InterruptedException ie){
				}
			}
		}		
	}
	class AutoRefreshThirdThread implements Runnable{
		public void run(){
			while(true){
				 DisplayIpTabStats();
				try{
					Thread.sleep(5000);
				}
				catch(InterruptedException ie){
				}
			}
		}		
	}
	class AutoRefreshThread implements Runnable{
		public void run(){
			while(true){
				
				DisplayStats();
				try{
					Thread.sleep(5000);
				}
				catch(InterruptedException ie){
				}
			}
		}		
	}
	class AutoRefreshSecondThread implements Runnable{
	      public void run(){
	        while(true){
	          DisplaypatStats();
	          try{
	            Thread.sleep(5000);
	          }
	          catch(InterruptedException ie){
	          }
	        }
	      }   
	    }
	public class StatsArea{
		public JTextArea text;
		public JScrollPane scroll;
		
		public StatsArea(){
			text=new JTextArea();
			text.setEditable(false);
			//text.setVisible(true);
			scroll=new JScrollPane(text);			
		}
	}
		
	private WindowAdapter wadapter=new WindowAdapter(){//when GUI closed 
		public void windowClosing(WindowEvent w){
			//server close
			//threads terminate
			Runtime.getRuntime().halt(0);
		}
	};
            	
	
	public GUI() throws FontFormatException, IOException{
		super("Server GUI (Auto Refresh Enabled)");
		this.setSize(1200,660);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(wadapter);
		this.setVisible(true);
		Create();
		
	}
	public void InsertTab_Create() throws FontFormatException, IOException{	//creates first tab
		JPanel panel1 =new JPanel();
		JPanel panel2=new JPanel();
		JPanel panel3=new JPanel();
		panel1.setLayout(new BoxLayout(panel1,BoxLayout.Y_AXIS));
		panel2.setLayout(new BoxLayout(panel2,BoxLayout.X_AXIS));
		panel3.setLayout(new BoxLayout(panel3,BoxLayout.X_AXIS));
		//size of contents of tab1
		IpField.setSize(180,30);	//set sizes of JTextFields
		IpField.setColumns(15);
		IpField.setMaximumSize(new Dimension(150,30));
		IpField.setMinimumSize(new Dimension(150,30));
		PatternField.setSize(180,30);
		PatternField.setColumns(100);
		PatternField.setMaximumSize(new Dimension(150,30));
		PatternField.setMinimumSize(new Dimension(150,30));
		panel2.add(Box.createRigidArea(new Dimension(10,0)));//horizontal space
		panel2.add(Iplabel);	//insert label
		panel2.add(Box.createRigidArea(new Dimension(5,0)));//horizontal space
		panel2.add(IpField);	//insert textfield
		panel2.add(Box.createRigidArea(new Dimension(5,0)));
		panel2.add(InsertIp);	//insert button
		panel3.add(Box.createRigidArea(new Dimension(10,0)));
		panel3.add(Patternlabel);	//label
		panel3.add(Box.createRigidArea(new Dimension(5,0)));
		panel3.add(PatternField);	//textfield
		panel3.add(Box.createRigidArea(new Dimension(5,0)));
		panel3.add(InsertPat);		//button
		panel1.add(panel2);		//panel 1 inserts other panels one under the other
		panel1.add(panel3);
		panel1.add(message.scroll);
//		tabfont=Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/home/andreas/Desktop/1.ttf"));
//		panel1.setFont(tabfont);
		tabbedpane.addTab("<html><body leftmargin=15 topmargin=8 marginwidth=15 marginheight=5>Insert</body></html>",null,panel1); //add panel1 to tabbedpanel
				
	}
	public void ActiveNodesTab_Create(){	//create  Tab (similar to above function)
		JPanel panel1 =new JPanel();
		JPanel panel2=new JPanel();
		panel1.setLayout(new BoxLayout(panel1,BoxLayout.Y_AXIS));
		panel2.setLayout(new BoxLayout(panel2,BoxLayout.X_AXIS));
		panel2.add(Box.createRigidArea(new Dimension(10,0)));
		panel2.add(Userslabel);
		panel2.add(Box.createRigidArea(new Dimension(5,0)));
		panel2.add(Activeusers);
		panel1.add(panel2);
		panel1.add(userstats.scroll);
		tabbedpane.addTab("<html><body leftmargin=15 topmargin=8 marginwidth=15 marginheight=5>Active Users</body></html>",null,panel1);
				
	}
	public void MalIPsTab_Create(){	//create  Tab (similar to above function)
		JPanel panel1 =new JPanel();
		JPanel panel2=new JPanel();
		panel1.setLayout(new BoxLayout(panel1,BoxLayout.Y_AXIS));
		panel2.setLayout(new BoxLayout(panel2,BoxLayout.X_AXIS));
		panel2.add(Box.createRigidArea(new Dimension(10,0)));
		panel2.add(Maliplabel);
		panel2.add(Box.createRigidArea(new Dimension(5,0)));
		panel2.add(MalIps);
		panel1.add(panel2);
		panel1.add(malipstats.scroll);
		tabbedpane.addTab("<html><body leftmargin=15 topmargin=8 marginwidth=15 marginheight=5>Malicious IPs</body></html>",null,panel1);
				
	}
	public void MalPatsTab_Create(){	//create  Tab (similar to above function)
		JPanel panel1 =new JPanel();
		JPanel panel2=new JPanel();
		panel1.setLayout(new BoxLayout(panel1,BoxLayout.Y_AXIS));
		panel2.setLayout(new BoxLayout(panel2,BoxLayout.X_AXIS));
		panel2.add(Box.createRigidArea(new Dimension(10,0)));
		panel2.add(Malpatlabel);
		panel2.add(Box.createRigidArea(new Dimension(5,0)));
		panel2.add(Malpats);
		panel1.add(panel2);
		panel1.add(malpatstats.scroll);
		tabbedpane.addTab("<html><body leftmargin=15 topmargin=8 marginwidth=15 marginheight=5>Malicious Patterns</body></html>",null,panel1);
				
	}
	public void MalIpStatsTab_Create(){	//create  Tab (similar to above function)
		JPanel panel1=new JPanel();
		JPanel panel2=new JPanel();
		panel1.setLayout(new BoxLayout(panel1,BoxLayout.Y_AXIS));
		panel2.setLayout(new BoxLayout(panel2,BoxLayout.X_AXIS));
		panel2.add(Box.createRigidArea(new Dimension(10,0)));
		panel2.add(Malipstatslabel);
		panel2.add(Box.createRigidArea(new Dimension(5,0)));
		panel2.add(Malipstatistics);
		panel1.add(panel2);
		panel1.add(malipstatsstats);
		tabbedpane.addTab("<html><body leftmargin=15 topmargin=8 marginwidth=15 marginheight=5>IP Statistics</body></html>",null,panel1);
				
	}
	public void MalPatStatsTab_Create(){	//create  Tab (similar to above function)
		JPanel panel1 =new JPanel();
		JPanel panel2=new JPanel();
		panel1.setLayout(new BoxLayout(panel1,BoxLayout.Y_AXIS));
		panel2.setLayout(new BoxLayout(panel2,BoxLayout.X_AXIS));
		panel2.add(Box.createRigidArea(new Dimension(10,0)));
		panel2.add(Malpatstatslabel);
		panel2.add(Box.createRigidArea(new Dimension(5,0)));
		panel2.add(Malpatstatistics);
		panel1.add(panel2);
		panel1.add(malpatstatsstats);
		tabbedpane.addTab("<html><body leftmargin=15 topmargin=8 marginwidth=15 marginheight=5>Pattern Statistics</body></html>",null,panel1);
				
	}
	
	public void Create() throws FontFormatException, IOException{ //creates main screen
		InsertTab_Create();
		MalIPsTab_Create();
		MalPatsTab_Create();
		ActiveNodesTab_Create();
		MalIpStatsTab_Create();
		MalPatStatsTab_Create();
		//add ActionListener
		InsertIp.addActionListener(actionlistener);
		InsertPat.addActionListener(actionlistener);
		Activeusers.addActionListener(actionlistener);
		MalIps.addActionListener(actionlistener);
		Malpats.addActionListener(actionlistener);
		Malipstatistics.addActionListener(actionlistener);
		Malpatstatistics.addActionListener(actionlistener);
		this.setContentPane(tabbedpane);
	}
	public void DisplayStats(){// Displays stats
		String[] columns={"Node ID","Interface Name","Interface IP","Malicious IP","Frequency"};
		String[][] stat_array = {};
		Set<String> active_users = ActiveUsers.getInstance().get_active_users();

		int total_number_of_records = 0;
		List<String[]> total_records = new ArrayList<String[]>();
		for (String node_id : active_users) {
			ArrayList<String[]> stat_list = null;
			try {
				stat_list = dbfunctions.getIPStatistics(node_id);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if (stat_list != null) {			// add all records for this user to the total records
				for (String[] s : stat_list)
					total_records.add(s);
			}
			total_number_of_records += stat_list.size();	// add number of records for this node
		}

		if (total_number_of_records != 0) {
			stat_array = new String[total_number_of_records][];
			int i = 0;
			for (String[] s : total_records)
				stat_array[i++] = s;
		}
		JTable ipstats=new JTable(stat_array,columns);
		ipstats.setFillsViewportHeight(true);
		malipstatsstats.getViewport ().add (ipstats);	
	}
	public void DisplaypatStats(){
		String[] columns={"Node ID","Interface Name","Interface IP","Malicious Pattern","Frequency"};
		String[][] stat_array = {};
		Set<String> active_users = ActiveUsers.getInstance().get_active_users();

		int total_number_of_records = 0;
		List<String[]> total_records = new ArrayList<String[]>();
		for (String node_id : active_users) {
			ArrayList<String[]> stat_list = null;
			try {
				stat_list = dbfunctions.getPatternsStatistics(node_id);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if (stat_list != null) {			// add all records for this user to the total records
				for (String[] s : stat_list)
					total_records.add(s);
			}
			total_number_of_records += stat_list.size();	// add number of records for this node
		}

		if (total_number_of_records != 0) {
			stat_array = new String[total_number_of_records][];
			int i = 0;
			for (String[] s : total_records)
				stat_array[i++] = s;
		}
		JTable patstats=new JTable(stat_array,columns);
		patstats.setFillsViewportHeight(true);
		malpatstatsstats.getViewport ().add (patstats);
		}
		public void DisplayIpTabStats(){
			String acc = "";
			for (String s : IPMemory.getInstance().get_ips() )
				acc += s + "\n";
			malipstats.text.setText(acc);
		}
		public void DisplayPatTabStats(){
			String acc = "";
			for (String s : PatternMemory.getInstance().get_patterns() )
				acc +=s + "\n";
			malpatstats.text.setText(acc);
		}
		public void DisplayActiveUsers(){
			String acc = "";
			for (String s : ActiveUsers.getInstance().get_active_users())
				acc +=s + "\n";
			userstats.text.setText(acc);
		}
		
	}