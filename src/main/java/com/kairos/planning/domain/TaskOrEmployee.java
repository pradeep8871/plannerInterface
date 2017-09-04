package com.kairos.planning.domain;

public abstract class TaskOrEmployee {

    protected Task nextTask;
    
  	public Task getNextTask(){
  		return nextTask;
  	}

    
    public abstract Vehicle getVehicle();
    
    public abstract Location getLocation();
}
