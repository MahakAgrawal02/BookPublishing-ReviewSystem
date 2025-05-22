package com.bookstore.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bookstore.entity.Book;
import com.bookstore.payload.request.BookRequest;
import com.bookstore.payload.response.BookResponse;
import com.bookstore.payload.response.ReviewResponse;
import com.bookstore.services.BookService;
import com.bookstore.services.UserDetailsImpl;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/book")
public class BookController {
	@Autowired
	BookService bookService;

	@PostMapping("/save")
	public ResponseEntity<String> saveBook(@RequestBody BookRequest bookRequest) {
		// Get the authenticated user
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		
		// Set the author as the authenticated user
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
		return new ResponseEntity<String>(response, HttpStatus.CREATED);
	}

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
