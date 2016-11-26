package cz.muni.fi.pa165.yellowlibrary.service.facade;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.inject.Inject;

import cz.muni.fi.pa165.yellowlibrary.api.dto.BookInstanceCreateDTO;
import cz.muni.fi.pa165.yellowlibrary.api.dto.BookInstanceDTO;
import cz.muni.fi.pa165.yellowlibrary.api.enums.BookInstanceAvailability;
import cz.muni.fi.pa165.yellowlibrary.api.facade.BookInstanceFacade;
import cz.muni.fi.pa165.yellowlibrary.backend.entity.Book;
import cz.muni.fi.pa165.yellowlibrary.backend.entity.BookInstance;
import cz.muni.fi.pa165.yellowlibrary.backend.entity.Department;
import cz.muni.fi.pa165.yellowlibrary.backend.enums.BookAvailability;
import cz.muni.fi.pa165.yellowlibrary.service.BeanMappingService;
import cz.muni.fi.pa165.yellowlibrary.service.BookInstanceService;
import cz.muni.fi.pa165.yellowlibrary.service.BookService;
import cz.muni.fi.pa165.yellowlibrary.service.configuration.ServiceConfiguration;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Matej Gallo
 */

@ContextConfiguration(classes = ServiceConfiguration.class)
public class BookInstanceFacadeTest extends AbstractTestNGSpringContextTests {

  @Mock
  private BookInstanceService bookInstanceService;

  @Mock
  private BookService bookService;

  @Spy
  @Inject
  private BeanMappingService beanMappingService;

  @InjectMocks
  private BookInstanceFacade bookInstanceFacade = new BookInstanceFacadeImpl();

  private BookInstance bookInstanceOne;
  private BookInstance newBookInstance;
  private Book book;
  private Department dep;

  @BeforeMethod
  public void setupBookInstance() {
    MockitoAnnotations.initMocks(this);
    bookInstanceOne = new BookInstance();
    book = new Book();
    dep = new Department();
    dep.setName("Test Department");
    dep.setShortName("TD");
    book.setIsbn("123-123-123");
    book.setPages(222);
    book.setDescription("The Life of a Tester");
    book.setAuthor("Harry");
    book.setName("Domain");
    book.setDepartment(dep);
    book.setId(13L);

    bookInstanceOne.setBookState("NEW");
    bookInstanceOne.setBookAvailability(BookAvailability.AVAILABLE);
    bookInstanceOne.setBook(book);
    bookInstanceOne.setId(42L);

    newBookInstance = new BookInstance();
    newBookInstance.setBookAvailability(bookInstanceOne.getBookAvailability());
    newBookInstance.setBook(bookInstanceOne.getBook());
    newBookInstance.setId(bookInstanceOne.getId());
    newBookInstance.setVersion(bookInstanceOne.getVersion());
    newBookInstance.setBookState(bookInstanceOne.getBookState());
  }

  @Test
  public void testFindById() {
    when(bookInstanceService.getBookInstanceById(bookInstanceOne.getId()))
        .thenReturn(bookInstanceOne);
    BookInstanceDTO bookInstanceDTO = bookInstanceFacade.findById(bookInstanceOne.getId());
    verify(bookInstanceService).getBookInstanceById(bookInstanceOne.getId());
    BookInstance bookInstance = beanMappingService.mapTo(bookInstanceDTO, BookInstance.class);
    Assert.assertEquals(bookInstance, bookInstanceOne);
  }

  /*@Test
  public void testAddBookInstance() {
    BookInstanceDTO bookInstanceDTO = beanMappingService.mapTo(bookInstanceOne, BookInstanceDTO);
    bookInstanceFacade.createBookInstance(bookInstanceDTO);
    verify(bookInstanceService).addBookInstance(any(BookInstance.class));
  }*/

  @Test
  public void testCreateBookInstance() {
    BookInstanceCreateDTO bookInstanceDTO = new BookInstanceCreateDTO();
    bookInstanceDTO.setBookInstanceAvailability(BookInstanceAvailability.AVAILABLE);
    bookInstanceDTO.setBookId(13L);
    bookInstanceDTO.setBookState("NEW");

    when(bookService.getBook(13L)).thenReturn(book);
    when(bookInstanceService.addBookInstance(any(BookInstance.class))).thenReturn(bookInstanceOne);

    Long id = bookInstanceFacade.createBookInstance(bookInstanceDTO);
    Assert.assertEquals(id, bookInstanceOne.getId());
  }

  @Test
  public void testChangeBookState() {
    String newState = "NEW STATE";
    bookInstanceFacade.changeBookState(bookInstanceOne.getId(), newState);
    verify(bookInstanceService).changeState(bookInstanceOne, newState);
  }

  @Test
  public void testChangeBookAvailability() {
    when(bookInstanceService.getBookInstanceById(bookInstanceOne.getId()))
        .thenReturn(bookInstanceOne);
    bookInstanceFacade.changeBookAvailability(bookInstanceOne.getId(), BookInstanceAvailability.REMOVED);

    BookInstanceDTO bookInstanceDTO = bookInstanceFacade.findById(bookInstanceOne.getId());
    BookInstance bookInstance = beanMappingService.mapTo(bookInstanceDTO, BookInstance.class);

    verify(bookInstanceService).changeAvailability(bookInstance, BookAvailability.REMOVED);
  }

  @Test
  public void testSetBook() {
    bookInstanceFacade.setBook(bookInstanceOne.getId(), 15L);
    Book newBook = new Book();
    newBook.setDepartment(dep);
    newBook.setName("NEW BOOK");
    newBook.setPages(123);
    newBook.setId(15L);
    when(bookService.getBook(15L)).thenReturn(newBook);
    verify(bookInstanceService).setBook(bookInstanceOne, newBook);
  }

}
