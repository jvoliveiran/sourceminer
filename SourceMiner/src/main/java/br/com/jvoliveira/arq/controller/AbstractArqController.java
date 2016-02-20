/**
 * 
 */
package br.com.jvoliveira.arq.controller;

import java.lang.reflect.ParameterizedType;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.jvoliveira.arq.domain.ObjectDB;
import br.com.jvoliveira.arq.service.AbstractArqService;
import br.com.jvoliveira.security.domain.User;


/**
 * @author Joao Victor
 *
 */
public abstract class AbstractArqController<T extends ObjectDB> {

	protected T obj;
	
	protected String title;
	
	protected String path;
	
	protected AbstractArqService<T> service;
	
	protected Class<T> typeClass;
	
	@SuppressWarnings("unchecked")
	public AbstractArqController(){
		this.typeClass = (Class<T>) (
	               (ParameterizedType) getClass().getGenericSuperclass())
	              .getActualTypeArguments()[0];
		
		try {
			obj = typeClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	@ModelAttribute("userLogged")
	public User getUsuarioLogado(){
		return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	@RequestMapping(value = {"","/index"})
	public String getIndex(Model model){
		model.addAttribute("list",service.getAll());
		return forward("index");
	}
	
	@RequestMapping("/view/{id}")
	public String view(@PathVariable Long id, Model model){
		model.addAttribute("obj", service.getOneById(id));
		return forward("view");
	}
	
	@RequestMapping("/create")
	public String create(Model model) throws InstantiationException, IllegalAccessException{
		this.obj = typeClass.newInstance();
		model.addAttribute("obj", this.obj);
		return forward("form");
	}
	
	@RequestMapping(value="/edit", method=RequestMethod.POST)
	public String edit(@RequestParam("idObj") Long id, Model model){
		this.obj = this.service.getOneById(id);
		model.addAttribute("obj", this.obj);
		return forward("form");
	}
	
	@RequestMapping(value="/create", method=RequestMethod.POST)
	public String save(@Validated T obj, Errors errors, Model model){
		
		if(errors.hasErrors()){
			model.addAttribute("obj", obj);
			return forward("form");
		}
		
		service.persist(obj);
		
		return redirect("index");
	}
	
	@RequestMapping(value="/delete", method=RequestMethod.POST)
	public String delete(@RequestParam("idObj") Long id, Model model){
		service.remove(id);
		
		return redirect("index");
	}
	
	@RequestMapping("/cancel")
	public String cancelOperation(){
		try {
			this.obj = typeClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {			
			e.printStackTrace();
		}
		return redirect("index");
	}
	
	protected String forward(String page){
		return path+"/"+page;
	}
	
	protected String redirect(String action){
		return "redirect:/" + path + "/" + action;
	}
	
	@ModelAttribute("controllerTitle")
	public String getTitle(){
		return this.title;
	}
}
