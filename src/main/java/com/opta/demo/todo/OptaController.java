package com.opta.demo.todo;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.opta.demo.appConfig.ResponseHandler;

@RestController
@RequestMapping("/api/opta")
public final class OptaController {

	private static final Logger log = LoggerFactory.getLogger(OptaController.class);

	@Autowired
	private TaskPlanningService optaservice;
	@Autowired
	GraphHopperService graphHopperService;
	@Autowired
	KieService optaPlannerService;

	@RequestMapping(value = "/submitXml", method = RequestMethod.POST)
	Map<String, Object> submitXml(@RequestBody Map requestData) {
		/*
		 * if(!requestData.containsKey("id") ||
		 * (String)requestData.get("id")=="" || !requestData.containsKey("xml")
		 * || (String)requestData.get("xml")=="") return
		 * ResponseHandler.generateResponse("planning id doesn't exist",
		 * HttpStatus.NOT_ACCEPTABLE, true, optaservice.getAllPlanning());
		 * optaservice.submitXmltoKie(requestData);
		 */return ResponseHandler.generateResponse("save Data sucessFully", HttpStatus.ACCEPTED, false,
				optaservice.getsolutionbyXml((String) requestData.get("xml")));// optaservice.getAllPlanning());
	}

	@RequestMapping(value = "/getProblemIds", method = RequestMethod.GET)
	Map<String, Object> getProblemIds() {
		return ResponseHandler.generateResponse("save Data sucessFully", HttpStatus.ACCEPTED, false,
				optaservice.getAllPlanning());
	}

	@RequestMapping(value = "/getStatus", method = RequestMethod.GET)
	Map<String, Object> getStatus(@RequestParam String id) {
		return ResponseHandler.generateResponse("save Data sucessFully", HttpStatus.ACCEPTED, false,
				optaservice.getPlanningProblemByid(id));
	}

	@RequestMapping(value = "/getSolution", method = RequestMethod.GET)
	Map<String, Object> getSolution(@RequestParam String id) {
		return ResponseHandler.generateResponse("save Data sucessFully", HttpStatus.ACCEPTED, false,
				optaservice.getSolutionById(id));
	}

	@RequestMapping(value = "/deleteKieContainer", method = RequestMethod.POST)
	Map<String, Object> deleteKieContainer(@RequestParam String id) {
		// optaPlannerService.deleteKieContainer(id);
		return ResponseHandler.generateResponse("save Data sucessFully", HttpStatus.ACCEPTED, false, null);
	}

	@RequestMapping(value = "/makeLocationData", method = RequestMethod.POST)
	Map<String, Object> makeLocationData() {
		graphHopperService.readLocations();
		return ResponseHandler.generateResponse("save Data sucessFully", HttpStatus.ACCEPTED, false, null);
	}

	/*
	 * @RequestMapping(value = "/makeKieContainer", method = RequestMethod.POST)
	 * Map<String ,Object> makeKieContainer() {
	 * optaPlannerService.makeKieContainer(); return
	 * ResponseHandler.generateResponse("save Data sucessFully",
	 * HttpStatus.ACCEPTED, false, null); }
	 */

	@ExceptionHandler
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public void handleTodoNotFound(OptaNotFoundException ex) {
		log.error("Handling error with message: {}", ex.getMessage());
	}
}
