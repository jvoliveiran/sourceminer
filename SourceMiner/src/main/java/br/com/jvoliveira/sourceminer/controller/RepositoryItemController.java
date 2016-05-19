/**
 * 
 */
package br.com.jvoliveira.sourceminer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.jvoliveira.arq.controller.AbstractArqController;
import br.com.jvoliveira.sourceminer.component.javaparser.ChangeLogGroupModel;
import br.com.jvoliveira.sourceminer.domain.RepositoryItem;
import br.com.jvoliveira.sourceminer.domain.enums.AssetType;
import br.com.jvoliveira.sourceminer.domain.enums.ChangeFileType;
import br.com.jvoliveira.sourceminer.domain.pojo.ItemAssetGroupType;
import br.com.jvoliveira.sourceminer.search.filter.ItemChangeLogFilter;
import br.com.jvoliveira.sourceminer.service.RepositoryItemService;

/**
 * @author Joao Victor
 *
 */
@Controller
@RequestMapping("/dashboard/repository_item")
public class RepositoryItemController extends AbstractArqController<RepositoryItem>{

	@Autowired
	public RepositoryItemController(RepositoryItemService repositoryItem) {
		this.service = repositoryItem;
		this.path = "dashboard";
		this.title = "Dashboard";
	}
	
	@RequestMapping(value = "/item_details", method = RequestMethod.POST)
	public String itemDetails(@RequestParam Long idItem, Model model){
		
		loadDefaultItemDetails(idItem, model);
		
		return forward("item_details");
	}
	
	@RequestMapping(value = "/search_item_changelog", method = RequestMethod.POST)
	public String searchItemChangelog(@Validated ItemChangeLogFilter filter, @RequestParam Long idItem, Model model){
		RepositoryItem item = ((RepositoryItemService)service).getItemById(idItem);
		
		((RepositoryItemService)service).searchItemChangeLog(item, filter);
		model.addAttribute("itemChangeLogFilter", filter);
		
		loadDefaultItemDetails(idItem, model);
		
		return forward("item_details");
	}
	
	private void loadDefaultItemDetails(Long idItem, Model model){

		((RepositoryItemService)service).setItemChangeLogFilter(new ItemChangeLogFilter());
		
		RepositoryItem item = ((RepositoryItemService)service).getItemById(idItem);
		ItemAssetGroupType assets = ((RepositoryItemService)service).getItemAssetGroupType(item);
		String fileContent = ((RepositoryItemService)service).getFileContentInRevision(item.getPath(), new Long(-1));
		List<ChangeLogGroupModel> historyChangeLog = ((RepositoryItemService)service).getChangeLogInRepositoryItem(item);
		
		model.addAttribute("item", item);
		model.addAttribute("project", item.getProject());
		model.addAttribute("fileContent", fileContent);
		model.addAttribute("revisionsNumber", item.getAllRevisionsNumber());
		model.addAttribute("historyChangeLog", historyChangeLog);
		model.addAttribute("assets", assets);
		
		model.addAttribute("itemChangeLogFilter", ((RepositoryItemService)service).getItemChangeLogFilter());
	}
	
	@ResponseBody
	@RequestMapping(value = "/file_content_revision", method = RequestMethod.POST)
	public String getFileContentInRevision(@RequestParam Long revisionNumber, @RequestParam String path){
		return ((RepositoryItemService)service).getFileContentInRevision(path, revisionNumber);
	}
	
	@ModelAttribute("assetTypeList")
	public List<AssetType> getAssetTypeList(){
		return AssetType.getValues();
	}
	
	@ModelAttribute("changeFileTypeList")
	public List<ChangeFileType> getChangeFileTypeList(){
		return ChangeFileType.getValues();
	}
}
