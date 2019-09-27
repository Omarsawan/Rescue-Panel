package model.units;

import exceptions.CannotTreatException;
import exceptions.IncompatibleTargetException;
import model.disasters.Disaster;
import model.events.SOSResponder;
import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.people.CitizenState;
import simulation.Address;
import simulation.Rescuable;
import simulation.Simulatable;

public abstract class Unit implements Simulatable, SOSResponder {
	private String unitID;
	private UnitState state;
	private Address location;
	private Rescuable target;
	private int distanceToTarget;
	private int stepsPerCycle;
	private WorldListener worldListener;

	public Unit(String unitID, Address location, int stepsPerCycle,
			WorldListener worldListener) {
		this.unitID = unitID;
		this.location = location;
		this.stepsPerCycle = stepsPerCycle;
		this.state = UnitState.IDLE;
		this.worldListener = worldListener;
	}

	public void setWorldListener(WorldListener listener) {
		this.worldListener = listener;
	}

	public WorldListener getWorldListener() {
		return worldListener;
	}

	public UnitState getState() {
		return state;
	}

	public void setState(UnitState state) {
		this.state = state;
	}

	public Address getLocation() {
		return location;
	}

	public void setLocation(Address location) {
		this.location = location;
	}

	public String getUnitID() {
		return unitID;
	}

	public Rescuable getTarget() {
		return target;
	}

	public int getStepsPerCycle() {
		return stepsPerCycle;
	}

	public void setDistanceToTarget(int distanceToTarget) {
		this.distanceToTarget = distanceToTarget;
	}

	@Override
	public void respond(Rescuable r)throws CannotTreatException,IncompatibleTargetException {

		if (target != null && state == UnitState.TREATING)
			reactivateDisaster();
		finishRespond(r);
	}

	public void reactivateDisaster() {
		Disaster curr = target.getDisaster();
		curr.setActive(true);
	}

	public void finishRespond(Rescuable r)throws CannotTreatException,IncompatibleTargetException{
		if(this instanceof Ambulance || this instanceof DiseaseControlUnit){
			if((r instanceof ResidentialBuilding))
				throw new IncompatibleTargetException(this,r,"Incompatible Target");
		}
		else{
			if((r instanceof Citizen))
				throw new IncompatibleTargetException(this,r,"Incompatible Target");
		}
		if(!canTreat(r))
			throw new CannotTreatException(this,r,"Cannot Treat");
		target = r;
		state = UnitState.RESPONDING;
		Address t = r.getLocation();
		distanceToTarget = Math.abs(t.getX() - location.getX())
				+ Math.abs(t.getY() - location.getY());
	}

	public abstract void treat();

	public void cycleStep() {
		if (state == UnitState.IDLE)
			return;
		if (distanceToTarget > 0) {
			distanceToTarget = distanceToTarget - stepsPerCycle;
			if (distanceToTarget <= 0) {
				distanceToTarget = 0;
				Address t = target.getLocation();
				worldListener.assignAddress(this, t.getX(), t.getY());
			}
		} else {
			state = UnitState.TREATING;
			treat();
			}
		}
	

	public void jobsDone() {
		target = null;
		state = UnitState.IDLE;
	}

	public void setStepsPerCycle(int stepsPerCycle) {
		this.stepsPerCycle = stepsPerCycle;
	}
	
	public boolean canTreat(Rescuable r){
		if(r instanceof ResidentialBuilding){
			if(((ResidentialBuilding)r).getFireDamage()==0&&this instanceof FireTruck)
				return false;
			else if (((ResidentialBuilding)r).getGasLevel()==0&& this instanceof GasControlUnit)
				return false;
			else if(((ResidentialBuilding)r).getFoundationDamage()==0&&this instanceof Evacuator)
				return false;
		}
		else if(r instanceof Citizen){
			if(((Citizen)r).getBloodLoss()==0&&this instanceof Ambulance)
				return false;
			else if(((Citizen)r).getToxicity()==0&&this instanceof DiseaseControlUnit)
				return false;
		}
		return true;
	}
}
