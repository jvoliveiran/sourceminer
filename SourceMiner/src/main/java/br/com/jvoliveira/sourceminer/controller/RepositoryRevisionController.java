/**
 * 
 */
package br.com.jvoliveira.sourceminer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.jvoliveira.arq.controller.AbstractArqController;
import br.com.jvoliveira.sourceminer.domain.RepositoryRevision;
import br.com.jvoliveira.sourceminer.domain.pojo.ConflictReport;
import br.com.jvoliveira.sourceminer.service.RepositoryRevisionService;

/**
 * @author Joao Victor
 *
 */
@Controller
@RequestMapping("/dashboard/repository_revision")
public class RepositoryRevisionController extends AbstractArqController<RepositoryRevision> {

	@Autowired
	public RepositoryRevisionController(RepositoryRevisionService revisionService) {
		this.service = revisionService;
		this.path = "dashboard";
		this.title = "Dashboard";
	}
	
	@RequestMapping(value = "/revision_details", method = RequestMethod.POST)
	public String revisionDetails(@RequestParam Long idRevision, Model model){
		
		RepositoryRevisionService revisionService = (RepositoryRevisionService) service;
		
		RepositoryRevision revision = revisionService.getRevisionById(idRevision);
		List<ConflictReport> conflictReportResult = revisionService.processIndirectConflictReport(revision);
		
		model.addAttribute("revision", revision);
		model.addAttribute("project", revision.getProject());
		model.addAttribute("conflictReportResult",conflictReportResult);
		
		return forward("revision_details");
	}
}
