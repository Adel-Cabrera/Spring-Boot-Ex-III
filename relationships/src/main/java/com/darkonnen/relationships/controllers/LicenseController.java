package com.darkonnen.relationships.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.darkonnen.relationships.models.License;
import com.darkonnen.relationships.models.Person;
import com.darkonnen.relationships.services.LicenseService;
import com.darkonnen.relationships.services.PersonService;


@Controller 
@RequestMapping("licenses")
public class LicenseController {
	private final LicenseService licenseService;
	private final PersonService personService;
	
	public LicenseController(LicenseService lService, PersonService pService) {
		this.licenseService = lService;
		this.personService = pService;
	}
	
	@RequestMapping(value="new", method=RequestMethod.GET)
	public String newLicense(@ModelAttribute("license") License license, Model model) {
		List<Person> persons = personService.allPersons();
		model.addAttribute("persons", persons);
		
		return "licenses/new.jsp";
	}
	
	@RequestMapping(value="new", method=RequestMethod.POST)
	public String addNewLicense(@Valid @ModelAttribute("license") License license, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "licenses/new.jsp"; 
		} else {
			license.setNumber(String.format("%06d", licenseService.allLicenses().size()+1));
			licenseService.createLicense(license);
			Long path = license.getPerson().getId();
			return "redirect:/persons/" + path;
		}
	}
}