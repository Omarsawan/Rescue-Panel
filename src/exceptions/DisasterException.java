package exceptions;

import model.disasters.Disaster;

abstract public class DisasterException extends SimulationException{

	Disaster disaster;
	
	public DisasterException(Disaster disaster){
		super();
	}
	
	public DisasterException(Disaster disaster,String message){
		super(message);
	}
}
