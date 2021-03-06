package cz.muni.fi.pa165.yellowlibrary.service;

import com.google.common.base.Strings;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import cz.muni.fi.pa165.yellowlibrary.backend.dao.BookDao;
import cz.muni.fi.pa165.yellowlibrary.backend.entity.Book;

/**
 * @author Norbert Slivka
 */
@Service
public class BookServiceImpl implements BookService {

  @Inject
  private BookDao bookDao;

  @Override
  public void addBook(Book book) {
    bookDao.create(book);
  }

  @Override
  public Book getBook(long id) {
    return bookDao.getBookFromId(id);
  }

  @Override
  public List<Book> getAllBooks() {
    return bookDao.getAllBooks();
  }

  @Override
  public List<Book> searchBooks(String author, String name, String description, String isbn,
                                Set<Long> departments) {
    return bookDao.getAllBooks()
        .stream()
        .filter(book ->
            (!Strings.isNullOrEmpty(author) && book.getAuthor().contains(author)) ||
                (!Strings.isNullOrEmpty(name) && book.getName().contains(name)) ||
                (!Strings.isNullOrEmpty(isbn) && book.getIsbn().contains(isbn)) ||
                (!Strings.isNullOrEmpty(description) &&
                    book.getDescription().contains(description)) ||
                (departments != null && !departments.isEmpty() &&
                    departments.contains(book.getDepartment().getId())))
        .collect(Collectors.toList());
  }

  @Override
  public void editBook(Book book) {
    bookDao.update(book);
  }
}
