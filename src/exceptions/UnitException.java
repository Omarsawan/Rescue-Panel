package exceptions;

import simulation.Rescuable;
import model.units.Unit;

abstract public class UnitException extends SimulationException {

	Unit unit;
	Rescuable target;
	
	public UnitException(Unit unit, Rescuable target){		
		super();
		this.unit=unit;
		this.target=target;
	}
	
	public UnitException(Unit unit, Rescuable target, String message){
		super(message);
	} 
}
