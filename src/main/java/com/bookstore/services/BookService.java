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

	public void saveBook(BookRequest bookRequest) {
		// Find the author by username
		UserEntity author = userRepository.findByUsername(bookRequest.getAuthorUsername())
			.orElseThrow(() -> new IllegalArgumentException("Author not found with username: " + bookRequest.getAuthorUsername()));
		
		// Check for duplicate book title by the same author
		if (bookRepository.existsByTitleAndAuthor(bookRequest.getTitle(), author)) {
			throw new DuplicateBookException("You have already published a book with this title: " + bookRequest.getTitle());
		}
		
		// Create and save the book
		Book book = new Book();
		book.setBookId(bookRequest.getBookId());
		book.setTitle(bookRequest.getTitle());
		book.setContent(bookRequest.getContent());
		book.setAuthor(author);
		
		bookRepository.save(book);
	}

	public Book getBookByID(int id) {
		Optional<Book> book = bookRepository.findById(id);
		return book.isPresent() ? book.get() : new Book();
	}
	
	public void deleteBookByID(int id) {
		bookRepository.deleteById(id);
	}

	public List<Book> getAllBooks() {
		return bookRepository.findAll();
	}
	
	public List<Book> searchBooksByTitle(String keyword) {
		return bookRepository.findByTitleContainingIgnoreCase(keyword);
	}
	
	public List<Book> getTopBooksByRating(int limit) {
		return bookRepository.findTopBooksByAverageRating(limit);
	}
}
