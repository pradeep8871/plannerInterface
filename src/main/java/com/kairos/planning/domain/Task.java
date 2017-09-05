package com.kairos.planning.domain;

import java.util.Arrays;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.AnchorShadowVariable;
import org.optaplanner.core.api.domain.variable.PlanningVariable;
import org.optaplanner.core.api.domain.variable.PlanningVariableGraphType;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XStreamAlias("Task")
//@PlanningEntity(difficultyComparatorClass = TaskDifficultyComparator.class)
public class Task  extends TaskOrEmployee {
	Logger log= LoggerFactory.getLogger(this.getClass());
	private TaskType taskType;
	// Planning variables: changes during planning, between score calculations.
    @PlanningVariable(valueRangeProviderRefs = {"taskRange","employeeRange"},
            graphType = PlanningVariableGraphType.CHAINED)
    private TaskOrEmployee previousTaskOrEmployee;
    //@AnchorShadowVariable(sourceVariableName = "previousTaskOrEmployee")
    //@CustomShadowVariable(variableListenerClass = LocationVariableUpdaterListener.class,
     //      sources = {@PlanningVariableReference(variableName = "previousTaskOrVehicle")})
   // private Vehicle vehicle;
    private String taskName;

	public Long[] getBrokenHardConstraints() {
		return brokenHardConstraints;
	}
	public Long getBrokenHardConstraintsSum() {
		Long sum=0l;
		if(brokenHardConstraints!=null){
			sum=Arrays.stream(brokenHardConstraints).mapToLong(Long::longValue).sum();
		}
		return sum;
	}

	public void setBrokenHardConstraints(Long[] brokenHardConstraints) {
		this.brokenHardConstraints = brokenHardConstraints;
	}

	private Long[] brokenHardConstraints;

    
    
    
    
	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	//@PlanningVariable(valueRangeProviderRefs = {"employeeRange"})
	@AnchorShadowVariable(sourceVariableName = "previousTaskOrEmployee")
	private Employee employee;
    private String routeId;

    public Task() {
    }

    public Task(Long id, TaskOrEmployee previousTaskOrEmployee, Vehicle vehicle, int duration, Location location, Integer priority, TaskType taskType) {
        this.id=id;
        this.previousTaskOrEmployee = previousTaskOrEmployee;
       // this.vehicle = vehicle;
        //this.employee = employee;
        this.duration = duration;
        this.location = location;
        this.priority=priority;
        this.taskType=taskType;

    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

   // @CustomShadowVariable(variableListenerClass = TaskChangeListener.class,
	 //      sources = {@PlanningVariableReference(variableName = "previousTaskOrVehicle")})
    private Route route;
  //  @PlanningVariable(valueRangeProviderRefs = {"employeeRange"} )
   // private Employee employee;
	@XStreamAlias("startTime")
	private Date startTime;
	@XStreamAlias("endTime")
    private Date endTime;

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Integer getEarlyStartMinutes() {
		return earlyStartMinutes;
	}

	public void setEarlyStartMinutes(Integer earlyStartMinutes) {
		this.earlyStartMinutes = earlyStartMinutes;
	}

	private Integer earlyStartMinutes;
    private boolean locked;
    private int indexInTaskType;
    private int readyTime;
	public int getReadyTime() {
		return readyTime;
	}
	public void setReadyTime(int readyTime) {
		this.readyTime = readyTime;
	}
	public int getIndexInTaskType() {
		return indexInTaskType;
	}
	public void setIndexInTaskType(int indexInTaskType) {
		this.indexInTaskType = indexInTaskType;
	}
	public boolean isLocked() {
		return locked;
	}
	public void setLocked(boolean locked) {
		this.locked = locked;
	}
	/*public Employee getEmployee() {
		return employee;
	}
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}*/
	/*@Override
	public Vehicle getVehicle() {
		return vehicle;
	}*/
	public TaskType getTaskType() {
		return taskType;
	}
	public void setTaskType(TaskType taskType) {
		this.taskType = taskType;
	}
	public Citizen getCitizen() {
		return citizen;
	}
	public void setCitizen(Citizen citizen) {
		this.citizen = citizen;
	}
	public TaskOrEmployee getPreviousTaskOrEmployee() {
		return previousTaskOrEmployee;
	}
	public void setPreviousTaskOrEmployee(TaskOrEmployee previousTaskOrEmployee) {
		this.previousTaskOrEmployee = previousTaskOrEmployee;
	}
	/*public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}*/
	private Citizen citizen;
	private Integer priority;
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	private Long id;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
    public String getLabel(){
    	return id+"_"+priority+"_"+new DateTime(getStartTime()).toString("HH:mm")+"_"+new DateTime(getEndTime()).toString("HH:mm");
    }
    private long duration;
    public long getDuration() {

		//return  taskType==null?0:taskType.getBaseDuration().intValue();
		//return (int)ChronoUnit.MINUTES.between(LocalDateTime.ofInstant(startTime.toInstant(), ZoneId.systemDefault()),
		//		LocalDateTime.ofInstant(endTime.toInstant(), ZoneId.systemDefault()));
		return getInterval().toDuration().getStandardMinutes();
	}
	public Long getDurationIncludingArrivalTime(){
		//return getDuration()+getReachingTime();
		return getIntervalIncludingArrival().toDuration().getStandardMinutes();
	}
	public Interval getInterval(){
    	return new Interval(new DateTime(startTime),new DateTime(endTime));
	}
	public Interval waitingInterval() {
		DateTime start= null, end=null;
		if (previousTaskOrEmployee instanceof Task) {
			start=new DateTime(((Task) previousTaskOrEmployee).getEndTime());
			end=new DateTime(this.getStartTime()).minusMinutes(getReachingTime());
			//return new Interval(new DateTime(((Task) previousTaskOrEmployee).getEndTime()), new DateTime(this.getStartTime()).minusMinutes(getReachingTime()));
		} else {
			if (((Employee) previousTaskOrEmployee).getEarliestStartTime() == null) {
				start=new DateTime(getStartTime());
				end=new DateTime(getEndTime());
				//return new Interval(new DateTime(getStartTime()), new DateTime(getEndTime()));
			} else {
				start=new DateTime(((Employee) previousTaskOrEmployee).getEarliestStartTime());
				end=new DateTime(this.getStartTime()).minusMinutes(getReachingTime());
				//return new Interval(new DateTime(((Employee) previousTaskOrEmployee).getEarliestStartTime()), new DateTime(this.getStartTime()).minusMinutes(getReachingTime()));
			}
		}
		if(start!=null && start.isBefore(end)){
			return new Interval(start,end);
		}else{
			return null;
		}
	}
		public Long waitingMinutes(){
			return waitingInterval()==null?0l:(long)(waitingInterval().toDuration().toStandardMinutes().getMinutes());
	}
	public Interval getIntervalIncludingArrival(){
		DateTime taskStartTimeIncludingDriving= previousTaskOrEmployee instanceof Task?new DateTime(((Task) previousTaskOrEmployee).getEndTime()):null;
		DateTime taskEndTime=new DateTime(endTime);
		if(taskStartTimeIncludingDriving==null || !taskStartTimeIncludingDriving.isBefore(taskEndTime)){
			taskStartTimeIncludingDriving=new DateTime(startTime).minusMinutes(getReachingTime());
		}
		//DateTime taskStartTimeIncludingDriving=previousTaskOrVehicle instanceof Vehicle?new DateTime(startTime).minusMinutes(getReachingTime()):
		//		new DateTime(((Task)previousTaskOrVehicle).getEndTime());
		return new Interval(taskStartTimeIncludingDriving,taskEndTime);
	}
	private Location location;

	public void setLocation(Location location) {
		this.location = location;
	}

	@Override
	public Vehicle getVehicle() {
		return employee==null?null:employee.getVehicle() ;
	}

	@Override
	public Location getLocation() {
		return location;
	}
	
	public long getDistanceFromPreviousTaskOrEmployee() {
        if (previousTaskOrEmployee == null) {
            throw new IllegalStateException("This method must not be called when the previousTaskOrVehicle ("
                    + previousTaskOrEmployee + ") is not initialized yet.");
        }
        return getDistanceFrom(previousTaskOrEmployee);
    }
    public boolean isAfter(Task prevTask){
		return !this.getStartTime().before(prevTask.getEndTime());
	}

	public boolean isBefore(Task prevTask){
		return this.getStartTime().before(prevTask.getStartTime());
	}

	/*
	In minutes
	 */
	public int getReachingTime() {
		if (previousTaskOrEmployee == null) {
			throw new IllegalStateException("This method must not be called when the previousTaskOrVehicle ("
					+ previousTaskOrEmployee + ") is not initialized yet.");
		}
		if (employee.getVehicle() == null) {
			throw new IllegalStateException("This method must not be called when the vehicle ("
					+ employee + ") is not initialized yet.");
		}
		int reachingTime = (int) (getDistanceFrom(previousTaskOrEmployee) / employee.getVehicle() .getSpeedKmpm());
		return reachingTime;
	}
	public long getDistanceFrom(TaskOrEmployee previousTaskOrEmployee) {
		return previousTaskOrEmployee.getLocation().getDistanceFrom(this.getLocation());
	}
	public String toString(){
		return "T:"+id+"-"+priority+"-"+getDuration()+"-";//+"-"+taskType.getRequiredSkillList();
	}
	public int getMissingSkillCount() {
        if (employee==null) {
            return 0;
        }
        int count = 0;
        for (Skill skill : taskType.getRequiredSkillList()) {
            if (!employee.getSkillSet().contains(skill)) {
                count++;
            }
        }
        return count;
    }
	public int getMissingSkillCountForEmployee(Employee employee) {
		if (employee == null) {
			return 0;
		}
		int count = 0;
		for (Skill skill : taskType.getRequiredSkillList()) {
			if (!employee.getSkillSet().contains(skill)) {
				count++;
			}
		}
		return count;
	}
	/*public int getMissingSkillCountForVehicle() {
		if (employee == null) {
			return 0;
		}
		if (vehicle== null) {
			return 0;
		}
		int count = 0;
		for (Skill skill : vehicle.getRequiredSkillList()) {
			if (!employee.getSkillSet().contains(skill)) {
				count++;
			}
		}
		return count;
	}*/
	/*public Long getEmployeeId(){
		return employee.getId();
	}*/
	public void setNextTask(Task nextTask){
	    this.nextTask=nextTask;
    }

    public boolean canAssignedEmployeeWork(){
		return employee!=null && employee.canWorkThisInterval(this.getIntervalIncludingArrival());
	}
	public long getMinutesExceedingAvailability(){
    	return employee==null?0l: employee.getExceedingMinutesForTaskInterval(this.getIntervalIncludingArrival());
	}
	public long getMinutesExceedingAvailabilityForReachingUnit(){
		return employee==null?0l: employee.getExceedingMinutesForTaskInterval(getIntervalToReachBack());
	}
	public boolean canAssignedEmployeeReachBack(){
		return (!isLastTaskOfRoute() || employee.canWorkThisInterval(getIntervalToReachBack()));
	}
	public boolean canEmployeeWork(Employee employee){
		return employee!=null && employee.canWorkThisInterval(this.getIntervalIncludingArrival()) ;
	}
	public boolean canEmployeeReachBack(Employee employee){
		return (!isLastTaskOfRoute() || employee.canWorkThisInterval(getIntervalToReachBack()));
	}

	public boolean isLastTaskOfRoute(){
    	return nextTask==null;
	}
	public Interval getIntervalToReachBack(){
		Interval interval = new Interval(new DateTime(endTime),new DateTime(endTime).plusMinutes(getTimeToReachBackUnit().intValue()));
		return interval;
	}
	/*
	Return minutes
	 */
	public Long getTimeToReachBackUnit(){
		return (long) Math.ceil(getDistanceFrom(employee)/employee.getVehicle().getSpeedKmpm());
	}

	public String getRouteId() {
		return routeId;
	}

	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}
}
