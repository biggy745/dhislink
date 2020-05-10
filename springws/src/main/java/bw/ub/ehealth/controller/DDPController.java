package bw.ub.ehealth.controller;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import bw.ub.ehealth.dhislink.patient.service.PatientService;
import bw.ub.ehealth.dhislink.redacap.data.service.RedcapDataService;
import bw.ub.ehealth.dhislink.redacap.data.vo.RedcapDataSearchCriteria;
import bw.ub.ehealth.dhislink.redacap.data.vo.RedcapDataVO;
import bw.ub.ehealth.dhislink.specimen.service.SpecimenService;
import bw.ub.ehealth.dhislink.specimen.vo.SpecimenVO;
import bw.ub.ehealth.dhislink.vo.CurrentUser;
import bw.ub.ehealth.dhislink.vo.DDPPostObject;
import bw.ub.ehealth.dhislink.vo.Event;
import bw.ub.ehealth.dhislink.vo.Program;
import bw.ub.ehealth.dhislink.vo.TrackedEntityInstance;

@RestController
@RequestMapping("/ddpcontroller")
public class DDPController {

    private Logger logger = LoggerFactory.getLogger(DDPController.class);

    @Value("${dhis2.api.url}")
    private String dhis2Url;

    @Value("${dhis2.api.program}")
    private String program;

    @Value("${dhis2.api.program.stage}")
    private String programStage;
    
    @Value("${lab.report.pid}")
    private Long labReportPID;

    @Value("${lab.reception.pid}")
    private Long labReceptionPID;

    @Value("${lab.extraction.pid}")
    private Long labExtractionPID;

    @Value("${lab.resulting.pid}")
    private Long labResultingPID;
    
    @Autowired
    private RedcapDataService redcapDataService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private SpecimenService specimenService;
    
    @Autowired
    private DhisLink dhisLink;

    @GetMapping(value = "/getdhisuser")
    @ResponseBody
    public CurrentUser getDhisUser() {
        return dhisLink.getCurrentUser();
    }

    @GetMapping(value = "/events", produces = "application/json")
    public List<Event> getEvents() {

    	Map<String, String> params = new HashMap<>();
        params.put("program", program);
        params.put("programStage", programStage);
        SpecimenVO last = specimenService.findLatestSpecimen();
        String date = "2020-01-01";
        
        if(last != null) {
        	
        	Calendar cal = Calendar.getInstance();
        	cal.setTime(last.getCreated());
        	date = cal.get(Calendar.YEAR) + "-" + cal.get(Calendar.MONTH) + "-" + cal.get(Calendar.DAY_OF_MONTH);
        	params.put("startDate", last.getCreated().toString().replace(' ', 'T'));
        } else {
        	params.put("startDate", date);
        }
        params.put("order", "eventDate:asc");
        params.put("pageSize", "50");

        int page = 1;
        params.put("page", "" + page);
        List<Event> allEvents = new ArrayList<>();
        List<Event> tmp = dhisLink.getEvents(params);

        while(tmp != null && tmp.size() > 0) {
            allEvents.addAll(tmp);
            page++;
            tmp = dhisLink.getEvents(params);
        }

        return allEvents;
    }

    /**
     * 
     * 
     * @param postObject
     * @return
     */
    @PostMapping(value = "/ddp", produces = "application/json")
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String pullSpecimen(@RequestBody DDPPostObject postObject) {
    	
    	SpecimenVO specimen = dhisLink.getOneSpecimen(postObject.getId());
    	
        return dhisLink.getSpecimenFieldList(specimen);
    }
    
    /**
     * Retrieve one specimen based on the barcode parameter
     * 
     * @param barcode
     * @return
     */
    @GetMapping(value = "/onespecimen/{barcode}")
    @ResponseBody
    public String getOneSpecimen(@PathVariable(name = "barcode") String barcode) {
    	
    	SpecimenVO specimen = dhisLink.getOneSpecimen(barcode);    	
    	return dhisLink.getSpecimenFieldList(specimen);
    }

    /**
     * Pull new specimen information from dhis2 
     * 
     * @return
     */
    @GetMapping(value = "/newspecimen", produces = "application/json")
    @ResponseBody
    public List<SpecimenVO> getSpecimen() {
        Map<String, String> params = new HashMap<>();
        params.put("program", program);
        params.put("programStage", programStage);
        params.put("status", "COMPLETED");
        //params.put("trackedEntityInstance", "Sg78qJZCXAm");
        SpecimenVO last = specimenService.findLatestSpecimen();
        
        if(last != null) {
        	params.put("startDate", last.getCreated().toString().replace(' ', 'T'));
        } else {
        	params.put("startDate", "2020-01-01");
        }
        
        params.put("order", "eventDate:asc");
        params.put("pageSize", "20");
        
        BigInteger numPulled = new BigInteger("0");
		int page = 1;
		params.put("page", "" + page);
		List<SpecimenVO> specimen = new ArrayList<>();
		List<SpecimenVO> tmp = dhisLink.getSpecimen(params);

		while (tmp != null && tmp.size() > 0) {
			logger.info("Page " + page + " has " + tmp.size() + " events.");
			specimen.addAll(tmp);
			break;
			//numPulled.add(new BigInteger(tmp.size() + ""));
			//page++;
            //tmp = dhisLink.getSpecimen(params);
		}
				
		return specimen;
    }

    @GetMapping(value = "/program", produces = "application/json")
    @ResponseBody
    public Program getProgram() {

        return dhisLink.getProgram(program);
    }

    @GetMapping(value = "/trackedentityinstance")
    @ResponseBody
    public TrackedEntityInstance getTrackedEntityInstance() {
        TrackedEntityInstance instance = dhisLink.getTrackedEntityInstance("Arv8sb0gLDR", "hGCed18kx7t");
        return instance;
    }

    @GetMapping(value = "/getredcapdata", produces = "application/json")
    public List<RedcapDataVO> getRedcapData() {
    	
    	RedcapDataSearchCriteria criteria = new RedcapDataSearchCriteria();
    	criteria.setFieldName("specimen_barcode");
    	criteria.setValue("215523");
    	
    	return (List<RedcapDataVO>) redcapDataService.searchByCriteria(criteria);
    	
    }
    
    /**
     * 
     * 
     * @return
     */
    @GetMapping(value = "/sendresults", produces = "application/json") 
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public String sendDhisResults() {
    	
    	return dhisLink.getDhisPayload(specimenService.findUnsynchedSpecimen());
    }
}
