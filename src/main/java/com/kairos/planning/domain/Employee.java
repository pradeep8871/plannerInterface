package com.kairos.planning.domain;

import java.util.*;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.joda.time.DateTime;
import org.joda.time.Interval;

@XStreamAlias("Employee")
public class Employee extends TaskOrEmployee{
	public Employee() {
	}
	public Employee(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	private Long id;
	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	private Location location;
	private String name;
	private Vehicle vehicle;
	private Set<Skill> skillSet;
	public Set<Skill> getSkillSet() {
		return skillSet;
	}
	public void setSkillSet(Set<Skill> skillSet) {
		this.skillSet = skillSet;
	}
	//private Map<Citizen, Affinity> affinityMap= new LinkedHashMap<Citizen, Affinity>();
	private List<AvailabilityRequest> availabilityList;
	
	private List<UnavailabilityRequest> unavailabilityRequests;

	public List<UnavailabilityRequest> getUnavailabilityRequests() {
		return unavailabilityRequests;
	}

	public void setUnavailabilityRequests(List<UnavailabilityRequest> unavailabilityRequests) {
		this.unavailabilityRequests = unavailabilityRequests;
	}
	public List<AvailabilityRequest> getAvailabilityList() {
		return availabilityList;
	}
	public void setAvailabilityList(List<AvailabilityRequest> availabilityList) {
		this.availabilityList = availabilityList;
	}
	/*public Map<Citizen, Affinity> getAffinityMap() {
		return affinityMap;
	}
	public void setAffinityMap(Map<Citizen, Affinity> affinityMap) {
		this.affinityMap = affinityMap;
	}*/

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getAvailableMinutes(){
		long mins=0;
		for(AvailabilityRequest availabilityRequest:availabilityList){
			mins+=availabilityRequest.getMinutes();
		}
		return mins;
	}
	public String getAvailableMinutesAsString(){
		final StringBuilder availability= new StringBuilder("[");
		availabilityList.forEach(availabilityRequest->{
			availability.append(availabilityRequest.getIntervalAsString());
		});
		return availability.append("]").toString();
	}
	
	public String toString(){
		return "E:"+id+"-"+name+"-"+getAvailableMinutes();//+skillSet+"-
	}
	/*public Affinity getAffinity(Citizen citizen) {
        Affinity affinity = affinityMap.get(citizen);
        if (affinity == null) {
            affinity = Affinity.NONE;
        }
        return affinity;
    }*/
    
    public boolean canWorkThisInterval(Interval taskTime){
		boolean canWork=false;
		for (AvailabilityRequest availabilityRequest : availabilityList) {
			if(availabilityRequest.getInterval().contains(taskTime)){
				canWork=true;
				break;
			}
		}
		return canWork;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public Interval getWorkInterval(){
        Interval workInterval=null;
        if(nextTask!=null){
            Task currentTask=nextTask;
            DateTime start = currentTask.getIntervalIncludingArrival().getStart();
            while(currentTask!=null){
                if(currentTask.nextTask==null) break;
                currentTask=currentTask.nextTask;

            }
            DateTime end = currentTask.getIntervalToReachBack().getEnd();
            if(start.isBefore(end)){
                workInterval= new Interval(start,end);
            }
        }
        return workInterval;
    }
    public String getWorkIntervalAsString(){
	    return getWorkInterval()==null?"null":getWorkInterval().getStart().toString("HH:mm")+"-"+getWorkInterval().getEnd().toString("HH:mm");
    }
    public boolean workIntervalOverlapsWithSameVehicle(Employee otherEmployee){
    	boolean overlaps=false;
        Optional<Interval> optional= Optional.ofNullable(this.getWorkInterval());
    	if((this.getVehicle() != null && this.getVehicle().equals(otherEmployee.getVehicle()) &&
                optional.isPresent()) && optional.get().overlaps(otherEmployee.getWorkInterval())){
			overlaps=true;
		}
		return overlaps;
	}
}
