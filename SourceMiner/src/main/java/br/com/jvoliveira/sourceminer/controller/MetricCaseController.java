/**
 * 
 */
package br.com.jvoliveira.sourceminer.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.jvoliveira.arq.controller.AbstractArqController;
import br.com.jvoliveira.sourceminer.domain.CaseMetricItem;
import br.com.jvoliveira.sourceminer.domain.Metric;
import br.com.jvoliveira.sourceminer.domain.MetricCase;
import br.com.jvoliveira.sourceminer.service.MetricCaseService;

/**
 * @author Joao Victor
 *
 */
@Controller
@RequestMapping("/metric_case")
public class MetricCaseController extends AbstractArqController<MetricCase>{

	@Autowired
	public MetricCaseController(MetricCaseService service){
		this.service = service;
		this.path = "metric_case";
		this.title = "Case de Métricas";
	}
	
	@RequestMapping("/create")
	public String create(Model model) throws InstantiationException, IllegalAccessException{
		this.obj = new MetricCase();
		this.obj.setCaseMetricItems(new ArrayList<CaseMetricItem>());
		model.addAttribute("obj", this.obj);
		return forward("form");
	}
	
	@Override
	@RequestMapping(value="/create", method=RequestMethod.POST)
	public String save(@Validated MetricCase obj, Errors errors, Model model){
		
		if(errors.hasErrors()){
			model.addAttribute("obj", obj);
			return forward("form");
		}
		obj.setCaseMetricItems(this.obj.getCaseMetricItems());
		service.persist(obj);
		
		return redirect("index");
	}
	
	@ModelAttribute("metrics")
	public List<Metric> getAllMetrics(){
		return ((MetricCaseService)this.service).getAllMetrics();
	}
	
	@ResponseBody
	@RequestMapping(value = "/add_metric_case_item", method = RequestMethod.POST)
	public List<CaseMetricItem> addMetricCaseItem(@RequestParam String idMetric, @RequestParam String valueMetric){
		
		CaseMetricItem caseMetric = ((MetricCaseService)this.service).parseItemMetric(idMetric, valueMetric);
		
		this.obj.getCaseMetricItems().add(caseMetric);
		
		return Arrays.asList(caseMetric);
	}
	
	@ResponseBody
	@RequestMapping(value = "/remove_metric_case_item", method = RequestMethod.POST)
	public List<CaseMetricItem> removeMetricCaseItem(@RequestParam String idMetricCase){
		Integer idCase = Integer.parseInt(idMetricCase);
		Iterator<CaseMetricItem> iterator = this.obj.getCaseMetricItems().iterator();
		
		while(iterator.hasNext()){
			CaseMetricItem item = iterator.next();
			if(item.getId().equals(idCase)){
				iterator.remove();
				break;
			}	
		}
		
		return this.obj.getCaseMetricItems();
	}
}
