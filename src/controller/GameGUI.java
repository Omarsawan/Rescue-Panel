package controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import controller.CommandCenter;
import controller.GameGUI;
import exceptions.CannotTreatException;
import exceptions.IncompatibleTargetException;
import model.disasters.Collapse;
import model.disasters.Disaster;
import model.disasters.Fire;
import model.disasters.GasLeak;
import model.disasters.Infection;
import model.disasters.Injury;
import model.events.WorldListener;
//import jdk.nashorn.internal.ir.SetSplitState;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.people.CitizenState;
import model.units.Ambulance;
import model.units.DiseaseControlUnit;
import model.units.Evacuator;
import model.units.FireTruck;
import model.units.GasControlUnit;
import model.units.Unit;
import simulation.Address;
import simulation.Rescuable;
import simulation.Simulatable;
import view.GameView;

public class GameGUI implements ActionListener,WorldListener{
	
	private CommandCenter commandcenter;
	private GameView gameview;
	private ArrayList<JButton>units;
	private ArrayList<JButton>grid;
	private ArrayList<Unit>availableunits;
	static ArrayList<Simulatable>sims;
	static private Citizen cit;
	static private ResidentialBuilding bul;
	static private Unit o;
	static ArrayList<JLabel>simslabels;
	static GameView tmp;
	static boolean action;
	static JTextArea msg;
	static Unit curUnit;
	static Address curLocation;
	static JPanel buildings;
	static JTextArea info;
	static JTextArea infoUnits;
	static JTextArea disaster;
	static JTextArea c;
	String textLog="";
	
	public GameGUI() throws Exception {
	    
		sims=new ArrayList<Simulatable>();
		simslabels=new ArrayList<JLabel>();
		action=true;
		commandcenter=new CommandCenter();
		commandcenter.setGui(this);
		gameview=new GameView();
		availableunits=new ArrayList<Unit>();
		units=new ArrayList<JButton>();
		grid=new ArrayList<JButton>();
		JPanel message=new JPanel();message.setLayout(new BorderLayout());
		gameview.getUnits().add(message,BorderLayout.NORTH);
		
		JPanel cycle=new JPanel();
		cycle.setBackground(Color.LIGHT_GRAY);
		cycle.setLayout(new FlowLayout());
		message.add(cycle,BorderLayout.NORTH);
		
		JButton nc=new JButton("Next Cycle");
		nc.setBackground(Color.white);
		nc.addActionListener(this);nc.setActionCommand("nextCycle");
		cycle.add(nc);
		
		JButton respnd=new JButton("Respond");
		respnd.setBackground(Color.white);
		respnd.addActionListener(this);
		respnd.setActionCommand("respond");
		cycle.add(respnd);
		
		c=new JTextArea();
		c.setBackground(Color.LIGHT_GRAY);
		c.setSize(12,1);
		c.setEditable(false);
		cycle.add(c);
		c.setFont(new java.awt.Font(java.awt.Font.DIALOG,java.awt.Font.CENTER_BASELINE,16));
		c.setText("Current Cycle : "+commandcenter.getEngine().getCurrentCycle()+"\nNumber of cacualities :"+commandcenter.getEngine().calculateCasualties());
		
		msg=new JTextArea("");
		msg.setBackground(Color.GRAY);
		msg.setEditable(false);
		msg.setFont(new java.awt.Font(java.awt.Font.DIALOG,java.awt.Font.CENTER_BASELINE,16));
		msg.setPreferredSize(new Dimension(600,40));
		message.add(msg,BorderLayout.CENTER);
		
		JPanel un=new JPanel();
		un.setBackground(Color.LIGHT_GRAY);
		un.setLayout(new BorderLayout());
		un.setPreferredSize(new Dimension(100,100));
		gameview.getUnits().add(un,BorderLayout.CENTER);
		
		un.add(new JLabel("Available Units"),BorderLayout.NORTH);
		
		JPanel unitsmain=new JPanel();
		unitsmain.setBackground(Color.LIGHT_GRAY);
		unitsmain.setPreferredSize(new Dimension(200,50));
		unitsmain.setLayout(new GridLayout(0,3,1,1));
		un.add(unitsmain,BorderLayout.CENTER);
		
		JPanel inu = new JPanel();
		inu.setBackground(Color.LIGHT_GRAY);
		inu.setLayout(new BorderLayout());
		inu.add(new JLabel(" Unit Information : "), BorderLayout.NORTH);
		inu.setPreferredSize(new Dimension(200,680));
		un.add(inu,BorderLayout.SOUTH);
		infoUnits = new JTextArea();
		infoUnits.setBackground(Color.GRAY);
		JScrollPane scrollu = new JScrollPane(infoUnits);
		scrollu.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		

		inu.add(scrollu,BorderLayout.CENTER);
		
		addDisasters();
		
		JPanel in=new JPanel();
		in.setBackground(Color.LIGHT_GRAY);
		in.setLayout(new BorderLayout());
		in.add(new JLabel(" Info"),BorderLayout.NORTH);
		gameview.getInfo().add(in,BorderLayout.CENTER);
		
		info=new JTextArea();
		info.setEditable(false);
		info.setPreferredSize(new Dimension(gameview.getUnits().getWidth(),500));
		
		
		
		info = new JTextArea();
		JScrollPane scroll = new JScrollPane(info);
		scroll.setPreferredSize(new Dimension(gameview.getUnits().getWidth(),300));
		info.setBackground(Color.gray);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		in.add(scroll,BorderLayout.CENTER);
		
		
		
		for(int i=0;i<10;i++) {
			for(int j=0;j<10;j++) {
				JButton x=new JButton();x.validate();
				x.setActionCommand("Grid");
				x.setBackground(Color.WHITE); //new Color(96,101,138));
				gameview.getRescue().add(x);x.setLayout(new GridLayout(0,3,1,1));
				grid.add(x);
				x.addActionListener(this);
			}
		}
		addunits(unitsmain);
		grid.get(0).add(new JLabel(new ImageIcon()));
		gameview.setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		JButton x=(JButton)e.getSource();
		if(!action) {
			if(x.getActionCommand().equals("yes")) {
				tmp.setVisible(false);
				gameview.setVisible(false);
				try {
					GameGUI o=new GameGUI();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			else {
				if(x.getActionCommand().equals("no")) {
					gameview.exit();
					tmp.exit();
				}
			}
		}
		else {
		if(x.getActionCommand().equals("Grid")) {
			int i=grid.indexOf(x);
			Address o=commandcenter.getEngine().getWorld()[i/10][i%10];
			for(int j=0;j<commandcenter.getVisibleCitizens().size();j++){
				if(((commandcenter.getVisibleCitizens().get(j)).getLocation()).getX()==o.getX()&&
						((commandcenter.getVisibleCitizens().get(j)).getLocation()).getY()==o.getY()){
					cit = commandcenter.getVisibleCitizens().get(j);
					info.setText(viewCitizen(cit));
				}
			}
			for(int j=0;j<commandcenter.getVisibleBuildings().size();j++){
				if(((commandcenter.getVisibleBuildings().get(j)).getLocation()).getX()==o.getX()&&
						((commandcenter.getVisibleBuildings().get(j)).getLocation()).getY()==o.getY()){
					bul = commandcenter.getVisibleBuildings().get(j);
					viewBuilding(bul);
				}
			}
			curLocation=o;
		}
		else {
			if(x.getActionCommand().equals("Unit")) {
				int i=units.indexOf(x);
				o=availableunits.get(i);
				view(o);
				curUnit=o;
			}
			else {
				if(x.getActionCommand().equals("nextCycle")) {
					msg.setText("");
					info.setText("");
					if(commandcenter.getEngine().checkGameOver()) {
						endgame();
					}
					else {
						commandcenter.getEngine().nextCycle();
						addvisible();
						c.setText("Current Cycle : "+commandcenter.getEngine().getCurrentCycle()+"\nNumber of cacualities :"+commandcenter.getEngine().calculateCasualties());
						if(cit!=null)
							viewCitizen(cit);
						if(bul!=null)
							viewBuilding(bul);
						if(o!=null)
							view(o);
						//viewDisasters();
						disaster.setText(textLog);
						addvisible();
					}
				}	
				else {
					handlerespond();
				}
			}
		}
		}	
	}

	public void view(Unit o) {
		
		String s=" Id :"+o.getUnitID()+"\n";
		if(o instanceof Ambulance) {
			s+=" Type : Ambulance\n Location : ("+o.getLocation().getX()+","+o.getLocation().getY()+")\n";
			s+=" Steps Per Cycle :"+o.getStepsPerCycle()+"\n Target :";
			Rescuable a=o.getTarget();
			if(a==null) {
				s+=" No Target Yet\n";
			}
			else {
				if(o.getTarget() instanceof Citizen) {
					s+=" Citizen\n";
				}
				else {
					s+=" Building\n";
				}
				s+=" Target's Location : ("+a.getLocation().getX()+","+a.getLocation().getY()+")\n";
			}
			s+=" State :"+o.getState();
		}
		else {
			if(o instanceof DiseaseControlUnit) {
				s+=" Type : Disease Control Unit\n Location : ("+o.getLocation().getX()+","+o.getLocation().getY()+")\n";
				s+=" Steps Per Cycle :"+o.getStepsPerCycle()+"\n Target :";
				Rescuable a=o.getTarget();
				if(a==null) {
					s+=" No Target Yet\n";
				}
				else {
					if(o.getTarget() instanceof Citizen) {
						s+=" Citizen\n";
					}
					else {
						s+=" Building\n";
					}
					s+=" Target's Location : ("+a.getLocation().getX()+","+a.getLocation().getY()+")\n";
				}
				s+=" State :"+o.getState();
			}
			else {
				if(o instanceof Evacuator) {
					s+=" Type : Evacuator\n Location : ("+o.getLocation().getX()+","+o.getLocation().getY()+")\n";
					s+=" Steps Per Cycle :"+o.getStepsPerCycle()+"\n Target :";
					Rescuable a=o.getTarget();
					if(a==null) {
						s+=" No Target Yet\n";
					}
					else {
						if(o.getTarget() instanceof Citizen) {
							s+=" Citizen\n";
						}
						else {
							s+=" Building\n";
						}
						s+=" Target's Location : ("+a.getLocation().getX()+","+a.getLocation().getY()+")\n";
					}
					s+=" State :"+o.getState();
					Evacuator x=(Evacuator)o;
					s+="\n Number of Passengers on board : "+x.getPassengers().size();
					
					if(((Evacuator) o).getPassengers().size()>0){
						s+="\n \n The Citizens in the Evacuator :\n ";
					for(Citizen i: ((Evacuator) o).getPassengers()){
						s+= "\n-------------------------------\n"+"\n"+viewCitizen(i);
					}
					}
					
				}
				else {
					if(o instanceof FireTruck) {
						s+=" Type : Fire Truck\n Location : ("+o.getLocation().getX()+","+o.getLocation().getY()+")\n";
						s+=" Steps Per Cycle :"+o.getStepsPerCycle()+"\n Target :";
						Rescuable a=o.getTarget();
						if(a==null) {
							s+=" No Target Yet\n";
						}
						else {
							if(o.getTarget() instanceof Citizen) {
								s+=" Citizen\n";
							}
							else {
								s+=" Building\n";
							}
							s+=" Target's Location : ("+a.getLocation().getX()+","+a.getLocation().getY()+")\n";
						}
						s+=" State :"+o.getState();
					}
					else {
						if(o instanceof GasControlUnit){
							s+=" Type : Gas Control Unit\n Location : ("+o.getLocation().getX()+","+o.getLocation().getY()+")\n";
							s+=" Steps Per Cycle :"+o.getStepsPerCycle()+"\n Target :";
							Rescuable a=o.getTarget();
							if(a==null) {
								s+=" No Target Yet\n";
							}
							else {
								if(o.getTarget() instanceof Citizen) {
									s+=" Citizen\n";
								}
								else {
									s+=" Building\n";
								}
								s+=" Target's Location : ("+a.getLocation().getX()+","+a.getLocation().getY()+")\n";
							}
							s+=" State :"+o.getState();
						}
			
					}
				}
			}
		}
		infoUnits.setText(s);
	}
	
	public void addunits(JPanel unitsmain) {
		JButton base=grid.get(0);
		for(Unit i:commandcenter.getEmergencyUnits()) {
			base.validate();
			availableunits.add(i);
			sims.add(i);
			if(i instanceof Ambulance) {
				JButton o=new JButton();o.setActionCommand("Unit");
				o.setIcon(new ImageIcon("amb.png"));
				o.setBackground(Color.GRAY);
				units.add(o);unitsmain.add(o);
				o.addActionListener(this);
				JLabel ol=new JLabel(new ImageIcon("amb.png"));base.add(ol);simslabels.add(ol);

			}
			else {
				if(i instanceof DiseaseControlUnit) {
					JButton o5=new JButton();o5.setActionCommand("Unit");
					o5.setBackground(Color.GRAY);
					o5.setIcon(new ImageIcon("dcu.png"));
					units.add(o5);unitsmain.add(o5);
					o5.addActionListener(this);
					JLabel ol=new JLabel(new ImageIcon("dcu.png"));base.add(ol);simslabels.add(ol);
				}
				else {
					if(i instanceof Evacuator) {
						JButton o2=new JButton();o2.setActionCommand("Unit");
						o2.setIcon(new ImageIcon("bus.png"));
						units.add(o2);unitsmain.add(o2);
						o2.setBackground(Color.GRAY);
						o2.addActionListener(this);
						JLabel ol=new JLabel(new ImageIcon("bus.png"));base.add(ol);simslabels.add(ol);
					}
					else {
						if(i instanceof FireTruck) {
							JButton o3=new JButton();o3.setActionCommand("Unit");
							o3.setIcon(new ImageIcon("FTr.png"));
							units.add(o3);unitsmain.add(o3);
							o3.setBackground(Color.GRAY);
							o3.addActionListener(this);
							JLabel ol=new JLabel(new ImageIcon("FTr.png"));base.add(ol);simslabels.add(ol);
						}
						else {
							if(i instanceof GasControlUnit){
								JButton o4=new JButton();o4.setActionCommand("Unit");
								o4.setIcon(new ImageIcon("gcu.png"));
								o4.setBackground(Color.GRAY);
								units.add(o4);unitsmain.add(o4);
								o4.addActionListener(this);
								JLabel ol=new JLabel(new ImageIcon("gcu.png"));base.add(ol);simslabels.add(ol);
							}
				
						}
					}
				}
			}
		}
	}

	public void addDisasters(){
		
		JPanel log = new JPanel();
		log.setBackground(Color.LIGHT_GRAY);
		log.setLayout(new BorderLayout());
		log.add(new JLabel(" Log : "), BorderLayout.NORTH);
		log.setPreferredSize(new Dimension(100, 250));
		gameview.getInfo().add(log,BorderLayout.NORTH);
		
		disaster = new JTextArea();
		disaster.setBackground(Color.GRAY);
		disaster.setPreferredSize(new Dimension(50,50));
		disaster.setEditable(false);
		disaster.setPreferredSize(new Dimension(gameview.getInfo().getWidth(),150));
		JScrollPane scroll = new JScrollPane(disaster,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		log.add(scroll,BorderLayout.CENTER);
		
		viewDisasters();
		
		
	}
	
	public String viewCitizen(Citizen ci){
		String s = " Citizen Information :\n"
				+ "\n Location : "+"( "+ ci.getLocation().getX()+" , "+ci.getLocation().getY()+" )"+
				"\n Name : "+ci.getName()+
				"\n Age : "+ci.getAge()+
				"\n National ID : "+ci.getNationalID()+
				"\n HP : "+ci.getHp()+
				"\n Blood Loss(%) : "+ci.getBloodLoss()+
				"\n Toxicity : "+ci.getToxicity()+
				"\n Citizen State : "+ci.getState();
		if(ci.getDisaster()!=null && ci.getDisaster().isActive()){
			if(ci.getDisaster() instanceof Injury)
				s+="\n Disaster : "+"Injury";
			else if(ci.getDisaster() instanceof Infection)
				s+="\n Disaster : "+"Infection";
		}
		return s;
	}
	
	public void viewBuilding(ResidentialBuilding bu){
		String s =" Building Informations :\n"+
				"\n Location : "+"( "+ bu.getLocation().getX()+" , "+bu.getLocation().getY()+" )"+
				"\n Structural Integrity : "+ bu.getStructuralIntegrity()+
				"\n Fire Damage : "+ bu.getFireDamage()+
				"\n Gas Level : "+bu.getGasLevel()+
				"\n Foundation Damage : "+bu.getFoundationDamage()+
				"\n Number of Occupants : "+bu.getOccupants().size();
		if(bu.getDisaster()!=null){
			if(bu.getDisaster() instanceof Collapse)
				s+="\n Disaster : "+"Collapse";
			else if (bu.getDisaster() instanceof Fire)
				s+="\n Disaster : "+"Fire";
			else
				s+="\n Disaster : "+"Gas Leak";
		}
		if(bul.getOccupants().size()>0){
			s+="\n \n The Citizens in the building :\n ";
		for(Citizen i: bul.getOccupants()){
			s+= "\n-------------------------------\n"+"\n"+viewCitizen(i);
		}
		}
		
		info.setText(s);
	}
	
	public void addvisible() {
		for(Citizen i:commandcenter.getVisibleCitizens()) {
			JButton o=grid.get(i.getLocation().getX()*10+i.getLocation().getY());
			if(i.getDisaster().isActive()) {
				o.setBackground(Color.red);
			}
			else {
				o.setBackground(Color.LIGHT_GRAY);
			}
			if(i.getState()==CitizenState.DECEASED)
				o.setIcon(new ImageIcon("dead.png"));
			else{
				o.setIcon(new ImageIcon("citizen.png"));
			}
		}
		for(ResidentialBuilding i:commandcenter.getVisibleBuildings()) {
			JButton o=grid.get(i.getLocation().getX()*10+i.getLocation().getY());
			if(i.getDisaster().isActive()) {
				o.setBackground(Color.red);
			}
			else {
				o.setBackground(Color.LIGHT_GRAY);
			}
			
			if(i.getOccupants().size()>0){
				o.setIcon(new ImageIcon("buildingO.jpg"));
			}
			else
			{
				o.setIcon(new ImageIcon("building.png"));
			}
			
			if(i.getStructuralIntegrity()==0){
				o.setIcon(new ImageIcon("destructed.png"));
			}
			else if (i.getDisaster() instanceof Collapse){
				o.setIcon(new ImageIcon("collapsed.png"));
			}
		}
	}
	public void logDisaster(Disaster i) {
		if(textLog.length()>0) {
			textLog+="\n";
		}
		if(i instanceof Collapse){
			textLog+="Collapse in ResidentialBuilding at "+" ( "+((ResidentialBuilding)i.getTarget()).getLocation().getX()+
			" , "+ ((ResidentialBuilding)i.getTarget()).getLocation().getY()+" ) ";
		}
		else if(i instanceof Fire){
			textLog+="Fire in ResidentialBuilding at "+" ( "+((ResidentialBuilding)i.getTarget()).getLocation().getX()+
					" , "+ ((ResidentialBuilding)i.getTarget()).getLocation().getY()+" ) ";
		}
		else if(i instanceof GasLeak){
			textLog+="GasLeak in ResidentialBuilding at "+" ( "+((ResidentialBuilding)i.getTarget()).getLocation().getX()+
			" , "+ ((ResidentialBuilding)i.getTarget()).getLocation().getY()+" ) ";
		}
		else if(i instanceof Infection){
			textLog+="Infection in Citizen : "+((Citizen)i.getTarget()).getName()+" at "+" ( "+((Citizen)i.getTarget()).getLocation().getX()+
			" , "+ ((Citizen)i.getTarget()).getLocation().getY()+" ) ";
		}
		else{
			textLog+="Injury in Citizen : "+((Citizen)i.getTarget()).getName()+" at "+" ( "+((Citizen)i.getTarget()).getLocation().getX()+
					" , "+ ((Citizen)i.getTarget()).getLocation().getY()+" ) ";
		}
	}
	public void logDead(Citizen i) {
		if(textLog.length()>0) {
			textLog+="\n";
		}
		textLog+="Citizen with ID "+i.getNationalID()+" died in Location : ("+i.getLocation().getX()+","+i.getLocation().getY()+") ";
	}
	public void viewDisasters(){
		String s="";
		for(Disaster i:commandcenter.getEngine().getExecutedDisasters()){
		
				if(i instanceof Collapse){
					s+="\n Collapse in ResidentialBuilding at "+" ( "+((ResidentialBuilding)i.getTarget()).getLocation().getX()+
					" , "+ ((ResidentialBuilding)i.getTarget()).getLocation().getY()+" ) ";
				}
				else if(i instanceof Fire){
					s+="\n Fire in ResidentialBuilding at "+" ( "+((ResidentialBuilding)i.getTarget()).getLocation().getX()+
							" , "+ ((ResidentialBuilding)i.getTarget()).getLocation().getY()+" ) ";
				}
				else if(i instanceof GasLeak){
					s+="\n GasLeak in ResidentialBuilding at "+" ( "+((ResidentialBuilding)i.getTarget()).getLocation().getX()+
					" , "+ ((ResidentialBuilding)i.getTarget()).getLocation().getY()+" ) ";
				}
				else if(i instanceof Infection){
					s+="\n Infection in Citizen : "+((Citizen)i.getTarget()).getName()+" at "+" ( "+((Citizen)i.getTarget()).getLocation().getX()+
					" , "+ ((Citizen)i.getTarget()).getLocation().getY()+" ) ";
				}
				else{
					s+="\n Injury in Citizen : "+((Citizen)i.getTarget()).getName()+" at "+" ( "+((Citizen)i.getTarget()).getLocation().getX()+
							" , "+ ((Citizen)i.getTarget()).getLocation().getY()+" ) ";
				}
			
		}
		for(Citizen i:commandcenter.getVisibleCitizens()) {
			if(i.getState()==CitizenState.DECEASED) {
				s+="\n Citizen with ID "+i.getNationalID()+" died in Location : ("+i.getLocation().getX()+","+i.getLocation().getY()+") ";
			}
		}
		for(ResidentialBuilding i:commandcenter.getVisibleBuildings()) {
			if(i.getStructuralIntegrity()==0) {
				s+=i.getOccupants().size()+"\n Citizens died in Building with Location ("+i.getLocation().getX()+","+i.getLocation().getY()+") ";
			}
		}
		disaster.setText(s);
		
	}
	public void endgame() {
		action=false;
		tmp=gameview;
		gameview=new GameView(0);
		JTextArea go=new JTextArea();
		go.setFont(new java.awt.Font(java.awt.Font.DIALOG,java.awt.Font.CENTER_BASELINE,16));
		int score=commandcenter.getEngine().calculateCasualties();
		if(score==0) {
			go.setText("                                                        Congratulations :D\n"
					+ "                                                    You didn't let anyone dies\n\n"
					+ "                                                Would you like to play again?");
		}
		else {
			go.setText("                                                           Game Over\n\n"
				+ "                                                         Your Score is:\n"
				+ "                                                          "+score+" casualities\n"
						+"                                             Would you like to play again?");
		}
		go.setEditable(false);
		gameview.getInfo().add(go,BorderLayout.NORTH);
		
		JPanel option=new JPanel();
		option.setLayout(new FlowLayout());
		
		JButton yes=new JButton("Yes of course");
		yes.setActionCommand("yes");
		yes.addActionListener(this);
		option.add(yes);
		

		JButton no=new JButton("Hell no");
		no.addActionListener(this);no.setActionCommand("no");
		option.add(no);
		
		gameview.getInfo().add(option,BorderLayout.CENTER);
	}
	
	public void handlerespond() {
		if(curUnit==null && curLocation==null) {
			msg.setText("You have to choose a unit and a target first");
		}
		else {
			if(curUnit==null) {
				msg.setText("You have to choose a unit first");
			}
			else {
				if(curLocation==null) {
					msg.setText("You have to choose a target first");
				}
				else {
					Rescuable trgt=null;
					for(ResidentialBuilding i:commandcenter.getVisibleBuildings()) {
						if(i.getLocation()==curLocation) {
							trgt=i;break;
						}
					}
					if(trgt==null) {
						for(Citizen i:commandcenter.getVisibleCitizens()) {
							if(i.getLocation()==curLocation) {
								trgt=i;break;
							}
						}
					}
					if(trgt==null) {
						msg.setText("You have to choose a visible target");
					}
					else {
						try {
							curUnit.respond(trgt);
							msg.setText("Responding.. ");
							view(curUnit);
							curUnit=null;curLocation=null;
						} catch (IncompatibleTargetException e) {
							msg.setText(e.getMessage());
						}
						catch (CannotTreatException  e){
							msg.setText(e.getMessage());
						}
					}
				}
			}
		}
	}
	
	public void assignAddress(Simulatable s, int x, int y) {
		if(s instanceof Unit) {
			Unit unit1=(Unit)s;
			int indx=sims.indexOf(unit1);
			JLabel l=simslabels.get(indx);
			grid.get(unit1.getLocation().getX()*10+unit1.getLocation().getY()).remove(simslabels.get(indx));
			grid.get(x*10+y).revalidate();
			grid.get(x*10+y).add(l);
		}
		else {
			grid.get(x*10+y).revalidate();
			grid.get(x*10+y).add(new JLabel(new ImageIcon("citizen.png")));
		}
		
	}

	public void viewloc(Address a) {
		info.setText("");
		ResidentialBuilding b=null;
		for(ResidentialBuilding i:commandcenter.getVisibleBuildings()) {
			if(i.getLocation()==a) {
				viewBuilding(i);
				b=i;
				break;
			}
		}
		String s="";
		if(b!=null) {
			for(Citizen i:b.getOccupants()) {
				if(i.getLocation()==a) {
					s+=viewCitizen(i);
				}
			}
		}
		else {
			for(Citizen i:commandcenter.getVisibleCitizens()) {
				if(i.getLocation()==a) {
					s+=viewCitizen(i);
				}
			}
		}
		info.setText(s);
	}
	
	public static void main(String[]args) throws Exception {
		GameGUI o=new GameGUI();
	}

}
