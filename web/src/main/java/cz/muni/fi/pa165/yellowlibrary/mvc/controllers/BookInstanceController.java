package cz.muni.fi.pa165.yellowlibrary.mvc.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import javax.inject.Inject;
import javax.validation.Valid;

import cz.muni.fi.pa165.yellowlibrary.api.dto.BookInstanceCreateDTO;
import cz.muni.fi.pa165.yellowlibrary.api.dto.BookInstanceDTO;
import cz.muni.fi.pa165.yellowlibrary.api.dto.BookInstanceNewStateDTO;
import cz.muni.fi.pa165.yellowlibrary.api.enums.BookInstanceAvailability;
import cz.muni.fi.pa165.yellowlibrary.api.facade.BookInstanceFacade;

/**
 * Created by Matej Gallo
 */

@Controller
@RequestMapping("/bookinstance")
public class BookInstanceController extends CommonController {

  final static Logger log = LoggerFactory.getLogger(BookInstanceController.class);

  @Inject
  private BookInstanceFacade bookInstanceFacade;

  @RequestMapping(value = "/list", method = RequestMethod.GET)
  public String list(Model model) {
    model.addAttribute("bookinstances", bookInstanceFacade.getAllBookInstances());
    return "bookinstance/list";
  }

  @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
  public String view(@PathVariable long id, Model model) {
    log.debug("view({})", id);
    model.addAttribute("bookinstance", bookInstanceFacade.findById(id));
    return "bookinstance/view";
  }

  @RequestMapping(value = "/new", method = RequestMethod.GET)
  public String newBookInstance(Model model) {
    // TODO: Retrieve to bookID from URL
    model.addAttribute("bookId", 1);
    log.debug("add()");
    model.addAttribute("bookInstanceCreate", new BookInstanceDTO());
    return "bookinstance/new";
  }

  @RequestMapping(value = "/{id}/newstate", method = RequestMethod.GET)
  public String newBookState(@PathVariable Long id, Model model) {
    model.addAttribute("id", id);
    log.debug("newState({})", id);
    model.addAttribute("bookInstanceNewState", new BookInstanceNewStateDTO());
    return "/bookinstance/newState";
  }

  @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
  public String delete(@PathVariable Long id, Model model,
                       UriComponentsBuilder uriComponentsBuilder,
                       RedirectAttributes redirectAttributes) {
    BookInstanceDTO bookInstanceDTO = bookInstanceFacade.findById(id);
    bookInstanceFacade.deleteBookInstance(id);
    log.debug("delete({})", id);
    redirectAttributes.addFlashAttribute("alert_success", "Book instance of \"" +
        bookInstanceDTO.getBook().getName() + "\" has been successfully deleted.");
    return "redirect:" + uriComponentsBuilder.path("/bookinstance/list").toUriString();
  }

  @RequestMapping(value = "/{id}/changestate", method = RequestMethod.POST)
  public String changeState(@PathVariable Long id, @Valid @ModelAttribute("bookInstanceNewState")BookInstanceNewStateDTO formBean,
                            BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes,
                            UriComponentsBuilder uriComponentsBuilder) {
    log.debug("changeState(bookInstanceNewState={id})", id, formBean);

    if(bindingResult.hasErrors()) {
      for(ObjectError ge : bindingResult.getGlobalErrors()) {
        log.trace("ObjectError: {}", ge);
      }
      for(FieldError fe : bindingResult.getFieldErrors()) {
        model.addAttribute(fe.getField() + "_error", true);
        log.trace("FieldError: {}", fe);
      }
      return uriComponentsBuilder.path("/bookinstance/{id}/newstate").buildAndExpand(id).encode().toUriString();
    }
    bookInstanceFacade.changeBookState(formBean);
    String bookName = bookInstanceFacade.findById(id).getBook().getName();
    redirectAttributes.addFlashAttribute("alert_success", "State of book instance of \"" + bookName +
        "\" has been successfully changed.");
    return "redirect:" + uriComponentsBuilder.path("/bookinstance/list").toUriString();
  }

  @RequestMapping(value = "/create", method = RequestMethod.POST)
  public String create(@Valid @ModelAttribute("bookInstanceCreate") BookInstanceCreateDTO formBean,
                       BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes,
                       UriComponentsBuilder uriComponentsBuilder) {
    log.debug("create(bookInstanceCreate={})", formBean);

    if(bindingResult.hasErrors()) {
      for(ObjectError ge : bindingResult.getGlobalErrors()) {
        log.trace("ObjectError: {}", ge);
      }
      for(FieldError fe : bindingResult.getFieldErrors()) {
        model.addAttribute(fe.getField() + "_error", true);
        log.trace("FieldError: {}", fe);
      }
      return "bookinstance/new";
    }
    Long id = bookInstanceFacade.createBookInstance(formBean);
    String bookName = bookInstanceFacade.findById(id).getBook().getName();
    redirectAttributes.addFlashAttribute("alert_success", "New book instance of \"" + bookName +
        "\" has been successfully created");
    return "redirect:" + uriComponentsBuilder.path("/bookinstance/list").toUriString();
  }

  @ModelAttribute("bookAvailabilities")
  public BookInstanceAvailability[] bookAvailabilities() {
    log.debug("bookAvailabilities()");
    return BookInstanceAvailability.values();
  }

}
