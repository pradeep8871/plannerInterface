package com.kairos.planning.solution;

import java.util.List;


//import org.springframework.data.annotation.Id;

import com.kairos.planning.domain.AvailabilityRequest;
import com.kairos.planning.domain.Citizen;
import com.kairos.planning.domain.Employee;
import com.kairos.planning.domain.Location;
import com.kairos.planning.domain.Skill;
import com.kairos.planning.domain.Task;
import com.kairos.planning.domain.TaskType;
import com.kairos.planning.domain.UnavailabilityRequest;
import com.kairos.planning.domain.Vehicle;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;


@XStreamAlias("TaskPlanningSolution")
public class TaskPlanningSolution {

	//@Id
	private String id;

	private List<Vehicle> vehicleList;
	private List<Citizen> citizenList;
	private List<Task> taskList;
	private List<TaskType> taskTypeList;
	private List<Employee> employeeList;
	private List<Skill> skillList;
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

	private List<Location> locationList;
	public List<Vehicle> getVehicleList() {
		return vehicleList;
	}

	public void setVehicleList(List<Vehicle> vehicleList) {
		this.vehicleList = vehicleList;
	}

	public List<Citizen> getCitizenList() {
		return citizenList;
	}

	public void setCitizenList(List<Citizen> citizenList) {
		this.citizenList = citizenList;
	}

	public List<Task> getTaskList() {
		return taskList;
	}

	public void setTaskList(List<Task> taskList) {
		this.taskList = taskList;
	}

	public List<TaskType> getTaskTypeList() {
		return taskTypeList;
	}

	public void setTaskTypeList(List<TaskType> taskTypeList) {
		this.taskTypeList = taskTypeList;
	}

	public List<Employee> getEmployeeList() {
		return employeeList;
	}

	public void setEmployeeList(List<Employee> employeeList) {
		this.employeeList = employeeList;
	}

	public List<Skill> getSkillList() {
		return skillList;
	}

	public void setSkillList(List<Skill> skillList) {
		this.skillList = skillList;
	}

	
	public List<Location> getLocationList() {
		return locationList;
	}

	public void setLocationList(List<Location> locationList) {
		this.locationList = locationList;
	}

	
}
