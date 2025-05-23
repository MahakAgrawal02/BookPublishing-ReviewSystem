package com.bookstore.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.entity.Book;
import com.bookstore.entity.UserEntity;
import com.bookstore.exceptions.DuplicateBookException;
import com.bookstore.payload.request.BookRequest;
import com.bookstore.repository.BookRepository;
import com.bookstore.repository.UserRepository;

@Service
public class BookService {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    UserRepository userRepository;

    /**
     * Save a new book to the repository after validating author and duplication.
     * 
     * @param bookRequest DTO containing book details.
     * @throws DuplicateBookException if a book with the same title already exists by the same author.
     * @throws IllegalArgumentException if author username is not found.
     */
    public void saveBook(BookRequest bookRequest) {
        // Find the author by username
        UserEntity author = userRepository.findByUsername(bookRequest.getAuthorUsername())
            .orElseThrow(() -> new IllegalArgumentException("Author not found with username: " + bookRequest.getAuthorUsername()));

        // Check for duplicate book title by the same author
        if (bookRepository.existsByTitleAndAuthor(bookRequest.getTitle(), author)) {
            throw new DuplicateBookException("You have already published a book with this title: " + bookRequest.getTitle());
        }

        // Create and save the book entity
        Book book = new Book();
        book.setBookId(bookRequest.getBookId());
        book.setTitle(bookRequest.getTitle());
        book.setContent(bookRequest.getContent());
        book.setAuthor(author);

        bookRepository.save(book);
    }

    /**
     * Retrieve a book by its ID.
     * 
     * @param id Book ID.
     * @return Book entity if found, else an empty Book instance.
     */
    public Book getBookByID(int id) {
        Optional<Book> book = bookRepository.findById(id);
        return book.isPresent() ? book.get() : new Book();
    }

    /**
     * Delete a book by its ID.
     * 
     * @param id Book ID.
     */
    public void deleteBookByID(int id) {
        bookRepository.deleteById(id);
    }

    /**
     * Retrieve all books in the repository.
     * 
     * @return List of all Book entities.
     */
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    /**
     * Search books by title containing the given keyword (case-insensitive).
     * 
     * @param keyword Search keyword.
     * @return List of books matching the title keyword.
     */
    public List<Book> searchBooksByTitle(String keyword) {
        return bookRepository.findByTitleContainingIgnoreCase(keyword);
    }

    /**
     * Get top books ordered by their average rating.
     * 
     * @param limit Number of top books to retrieve.
     * @return List of top-rated books.
     */
    public List<Book> getTopBooksByRating(int limit) {
        return bookRepository.findTopBooksByAverageRating(limit);
    }
}
