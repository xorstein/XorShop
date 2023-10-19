package com.xorshop.admin.question;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.xorshop.admin.pagin.PagingAndSortingHelper;
import com.xorshop.admin.pagin.PagingAndSortingParam;
import com.xorshop.admin.security.XorshopUserDetails;
import com.xorshop.common.entity.question.Question;
import com.xorshop.common.exception.QuestionNotFoundException;

@Controller
public class QuestionController {

	private static final Logger LOGGER = LoggerFactory.getLogger(QuestionController.class);
	
	private String defaultRedirectURL = "redirect:/questions/page/1?sortField=askTime&sortDir=desc";
	
	@Autowired 
	private QuestionService service;
	
	@GetMapping("/questions")
	public String listFirstPage(Model model) {
		
		LOGGER.info("QuestionController | listFirstPage is called");
		
		return defaultRedirectURL;
	}
	
	@GetMapping("/questions/page/{pageNum}")
	public String listByPage(
			@PagingAndSortingParam(listName = "listQuestions", moduleURL = "/questions") PagingAndSortingHelper helper,
						@PathVariable(name = "pageNum") int pageNum,Model model) {

		LOGGER.info("QuestionController | listByPage is called");
		LOGGER.info("QuestionController | listByPage | pageNum : " + pageNum);
	
		model.addAttribute("activelink", "13");
		model.addAttribute("pageTitle", "Questions");
		service.listByPage(pageNum, helper);

		return "questions/questions";		
	}
	
	@GetMapping("/questions/detail/{id}")
	public String viewQuestion(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
		
		LOGGER.info("QuestionController | viewQuestion is called");
		LOGGER.info("QuestionController | viewQuestion | id : " + id);
		
		try {
			Question question = service.getQuestionById(id);
			
			LOGGER.info("QuestionController | viewQuestion | question : " + question.toString());
			
			model.addAttribute("question", question);
			model.addAttribute("pageTitle", "Questions");
			model.addAttribute("activelink", "13");
			return "questions/question_detail_modal";
			
		} catch (QuestionNotFoundException ex) {
			LOGGER.info("QuestionController | viewQuestion | message : " + ex.getMessage());
			ra.addFlashAttribute("error", ex.getMessage());
			return defaultRedirectURL;	
		}
	}
	
	@GetMapping("/questions/edit/{id}")
	public String editQuestion(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
		
		LOGGER.info("QuestionController | editQuestion is called");
		LOGGER.info("QuestionController | editQuestion | id : " + id);
		
		try {
			Question question = service.getQuestionById(id);
			
			LOGGER.info("QuestionController | editQuestion | question : " + question.toString());
			LOGGER.info("QuestionController | editQuestion | pageTitle : " + "Edit Question (ID: " + id + ")");
			
			model.addAttribute("question", question);
			model.addAttribute("pageTitle", "Modifier la question (Numéro: " + id + ")");
			model.addAttribute("activelink", "13");

			return "questions/question_form";
		} catch (QuestionNotFoundException ex) {
			LOGGER.info("QuestionController | editQuestion | message : " + ex.getMessage());
			ra.addFlashAttribute("error", ex.getMessage());
			return defaultRedirectURL;
		}
	}

	@PostMapping("/questions/save")
	public String saveQuestion(Question question, RedirectAttributes ra,
			@AuthenticationPrincipal XorshopUserDetails userDetails) {
		
		LOGGER.info("QuestionController | saveQuestion is called");
		LOGGER.info("QuestionController | saveQuestion | Question Content : " + question.getQuestionContent());
		
		LOGGER.info("QuestionController | saveQuestion | user Info : " + userDetails.getUser().toString());
		
		try {
			service.saveQuestionByUser(question, userDetails.getUser());
			LOGGER.info("QuestionController | saveQuestion | message : " + "The Question ID " + question.getId() + " has been updated successfully.");
			ra.addFlashAttribute("message", "La question numéro  " + question.getId() + "a été mis à jour avec succès.");
		} catch (QuestionNotFoundException ex) {
			LOGGER.info("QuestionController | saveQuestion | error : " + "Could not find any question with ID " + question.getId());
			ra.addFlashAttribute("error", "Aucune question avec le numéro n'a été trouvée " + question.getId());
		}
		
		return defaultRedirectURL;
		
	}
	
	@GetMapping("/questions/{id}/approve")
	public String approveQuestion(@PathVariable("id") Integer id, RedirectAttributes ra) {
		
		LOGGER.info("QuestionController | approveQuestion is called");
		LOGGER.info("QuestionController | approveQuestion | id : " + id);
		
		service.approve(id);
		
		LOGGER.info("QuestionController | approveQuestion | message : " + "The Question ID " + id + " has been approved.");
		
		ra.addFlashAttribute("message", "La question numéro " + id + " a été approuvé.");
		return defaultRedirectURL;
	}

	@GetMapping("/questions/{id}/disapprove")
	public String disapproveQuestion(@PathVariable("id") Integer id, RedirectAttributes ra) {
		
		LOGGER.info("QuestionController | disapproveQuestion is called");
		LOGGER.info("QuestionController | disapproveQuestion | id : " + id);
		
		service.disapprove(id);
		
		LOGGER.info("QuestionController | approveQuestion | message : " + "The Question ID " + id + " has been disapproved.");
		
		ra.addFlashAttribute("message", "La question numéro  " + id + " a été refusé.");
		return defaultRedirectURL;
	}

	@GetMapping("/questions/delete/{id}")
	public String deleteQuestion(@PathVariable(name = "id") Integer id, RedirectAttributes ra) throws IOException {

		LOGGER.info("QuestionController | deleteQuestion is called");
		LOGGER.info("QuestionController | deleteQuestion | id : " + id);
		
		try {
			service.delete(id);
			
			LOGGER.info("QuestionController | approveQuestion | message : " + String.format("The question ID %d has been deleted successfully.", id));

			ra.addFlashAttribute("message", String.format("La question numéro %d a été supprimé avec succès.", id));
		} catch (QuestionNotFoundException ex) {
			LOGGER.info("QuestionController | saveQuestion | error : " + ex.getMessage());
			ra.addFlashAttribute("error", ex.getMessage());
		}

		return defaultRedirectURL;
	}
}
