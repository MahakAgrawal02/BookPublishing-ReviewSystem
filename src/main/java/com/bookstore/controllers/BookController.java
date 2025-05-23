package com.bookstore.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.bookstore.entity.Book;
import com.bookstore.payload.request.BookRequest;
import com.bookstore.payload.response.BookResponse;
import com.bookstore.payload.response.ReviewResponse;
import com.bookstore.services.BookService;
import com.bookstore.services.UserDetailsImpl;

import jakarta.validation.Valid;

/**
 * REST controller for managing book-related operations.
 */
@RestController
@RequestMapping("/book")
public class BookController {

	@Autowired
	private BookService bookService;

	/**
	 * Saves a new book authored by the currently authenticated user.
	 * Validates required fields: bookId, title, and content.
	 *
	 * @param bookRequest the book details to save
	 * @return success message or error response
	 */
	@PostMapping("/save")
	public ResponseEntity<String> saveBook(@RequestBody BookRequest bookRequest) {
		// Retrieve the authenticated user's details
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

		// Assign the current user as the book's author
		bookRequest.setAuthorUsername(userDetails.getUsername());

		// Validate required fields
		if (bookRequest.getBookId() == null) {
			return new ResponseEntity<>("Book ID is mandatory", HttpStatus.BAD_REQUEST);
		}
		if (bookRequest.getTitle() == null || bookRequest.getTitle().trim().isEmpty()) {
			return new ResponseEntity<>("Title is mandatory", HttpStatus.BAD_REQUEST);
		}
		if (bookRequest.getContent() == null || bookRequest.getContent().trim().isEmpty()) {
			return new ResponseEntity<>("Content is mandatory", HttpStatus.BAD_REQUEST);
		}

		bookService.saveBook(bookRequest);
		String response = "Book with id=" + bookRequest.getBookId() + " is saved.";
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	/**
	 * Retrieves all books with associated reviews and author info.
	 *
	 * @return list of BookResponse objects
	 */
	@GetMapping("/all-books")
	public ResponseEntity<List<BookResponse>> getAllBooks() {
		List<Book> books = bookService.getAllBooks();
		List<BookResponse> bookResponses = books.stream()
			.map(book -> new BookResponse(
				book.getBookId(),
				book.getTitle(),
				book.getContent(),
				book.getAuthor().getUsername(),
				book.getReviews().stream()
					.map(review -> new ReviewResponse(
						review.getReviewer().getUsername(),
						review.getRating(),
						review.getComment(),
						review.getTimestamp()
					))
					.collect(Collectors.toList())
			))
			.collect(Collectors.toList());

		return new ResponseEntity<>(bookResponses, HttpStatus.OK);
	}

	/**
	 * Searches for books by title containing the given keyword.
	 *
	 * @param keyword the keyword to search in titles
	 * @return list of matching BookResponse objects
	 */
	@GetMapping("/search")
	public ResponseEntity<List<BookResponse>> searchBooks(@RequestParam String keyword) {
		List<Book> books = bookService.searchBooksByTitle(keyword);
		List<BookResponse> bookResponses = books.stream()
			.map(book -> new BookResponse(
				book.getBookId(),
				book.getTitle(),
				book.getContent(),
				book.getAuthor().getUsername(),
				book.getReviews().stream()
					.map(review -> new ReviewResponse(
						review.getReviewer().getUsername(),
						review.getRating(),
						review.getComment(),
						review.getTimestamp()
					))
					.collect(Collectors.toList())
			))
			.collect(Collectors.toList());

		return new ResponseEntity<>(bookResponses, HttpStatus.OK);
	}

	/**
	 * Returns a list of top-rated books, limited by the given count.
	 *
	 * @param limit the number of top books to return (default is 10)
	 * @return list of top BookResponse objects
	 */
	@GetMapping("/top")
	public ResponseEntity<List<BookResponse>> getTopBooks(@RequestParam(defaultValue = "10") int limit) {
		List<Book> books = bookService.getTopBooksByRating(limit);
		List<BookResponse> bookResponses = books.stream()
			.map(book -> new BookResponse(
				book.getBookId(),
				book.getTitle(),
				book.getContent(),
				book.getAuthor().getUsername(),
				book.getReviews().stream()
					.map(review -> new ReviewResponse(
						review.getReviewer().getUsername(),
						review.getRating(),
						review.getComment(),
						review.getTimestamp()
					))
					.collect(Collectors.toList())
			))
			.collect(Collectors.toList());

		return new ResponseEntity<>(bookResponses, HttpStatus.OK);
	}
}
