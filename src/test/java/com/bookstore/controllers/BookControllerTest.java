package com.bookstore.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.bookstore.payload.request.BookRequest;
import com.bookstore.payload.request.UserRequest;
import com.bookstore.services.BookService;
import com.bookstore.configs.Constants;
import com.bookstore.helpers.Converter;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)  // Use same test instance for @BeforeAll non-static method
public class BookControllerTest {
    
    @Autowired
    private MockMvc mockMvc;  // For mocking HTTP requests
    
    @Autowired
    private BookService bookService;  // For managing book test data
    
    private String jwtAdmin;       // JWT token for admin user
    private String jwtSimpleUser;  // JWT token for simple (non-admin) user
    
    @BeforeAll
    public void setUp() throws Exception {
        // Ensure no book with ID 20 exists before tests run
        bookService.deleteBookByID(20);
        
        // Obtain JWT tokens for admin and simple users to use in authenticated requests
        jwtAdmin = getJWTafterMock(Constants.ADMIN_USER, Constants.LOGIN_URL);
        jwtSimpleUser = getJWTafterMock(Constants.SIMPLE_USER, Constants.LOGIN_URL);
    }
    
    @Test
    public void saveBook_shouldSaveBook_whenBookInfoIsValid() throws Exception {
        // Prepare a valid book request with book ID 20 and admin author
        BookRequest bookRequest = new BookRequest();
        bookRequest.setBookId(20);
        bookRequest.setAuthorUsername("Admin");  // Admin user assumed to exist
        bookRequest.setTitle("Prisoner of Azkaban");
        bookRequest.setContent("Book content here");
            
        // Perform POST request to save book with admin JWT token
        MvcResult mvcResult = GetMVCResultFromMockMvcPerform(bookRequest, Constants.SAVE_BOOK_URL, jwtAdmin);
        
        // Assert that response status is HTTP 201 Created
        int actualStatus = mvcResult.getResponse().getStatus();
        int expectedStatus = HttpStatus.CREATED.value();
        
        assertEquals(expectedStatus, actualStatus);
    }
    
    @Test
    public void saveBook_shouldReturnUnauthorized_whenUserIsNotAdmin() throws Exception {
        // Prepare a book request with book ID 21 and admin author (but user is not admin)
        BookRequest bookRequest = new BookRequest();
        bookRequest.setBookId(21);
        bookRequest.setAuthorUsername("Admin");
        bookRequest.setTitle("Prisoner of Azkaban");
        bookRequest.setContent("Book content here");

        // Perform POST request to save book with simple user JWT token (no admin rights)
        MvcResult mvcResult = GetMVCResultFromMockMvcPerform(bookRequest, Constants.SAVE_BOOK_URL, jwtSimpleUser);
        
        // Assert that response status is HTTP 403 Forbidden
        int actualStatus = mvcResult.getResponse().getStatus();
        int expectedStatus = HttpStatus.FORBIDDEN.value();
        
        assertEquals(expectedStatus, actualStatus);
    }
    
    @Test
    public void saveBook_shouldReturnUnauthorized_whenNoTokenProvided() throws Exception {
        // Prepare a book request without providing any JWT token
        BookRequest bookRequest = new BookRequest();
        bookRequest.setBookId(22);
        bookRequest.setAuthorUsername("Admin");
        bookRequest.setTitle("Prisoner of Azkaban");
        bookRequest.setContent("Book content here");
        
        // Perform POST request to save book without authentication header
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post(Constants.SAVE_BOOK_URL)
                .content(Converter.convertToString(bookRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        
        // Assert that response status is HTTP 401 Unauthorized
        int actualStatus = mvcResult.getResponse().getStatus();
        int expectedStatus = HttpStatus.UNAUTHORIZED.value();
        
        assertEquals(expectedStatus, actualStatus);
    }
    
    @Test
    public void getAllBooks_ShouldReturnAllBooks_WhenJWTisAdmin() throws Exception {
        // Perform GET request to fetch all books with admin JWT token
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get(Constants.GET_ALL_BOOKS_URL)
                .header("Authorization", "Bearer " + jwtAdmin)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        
        String bodyString = mvcResult.getResponse().getContentAsString();
        
        // Assert response status is HTTP 200 OK
        int actualStatus = mvcResult.getResponse().getStatus();
        int expectedStatus = HttpStatus.OK.value();
        
        System.err.println("Body: " + bodyString);
        
        // Assert response contains key book fields expected in JSON output
        assertEquals(expectedStatus, actualStatus);
        assertTrue(bodyString.contains("bookId")
                && bodyString.contains("author")
                && bodyString.contains("title")
                && bodyString.contains("content"));
    }
    
    @Test
    public void getAllBooks_ShouldReturnAllBooks_WhenJWTisSimpleUser() throws Exception {
        // Perform GET request to fetch all books with simple user JWT token
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get(Constants.GET_ALL_BOOKS_URL)
                .header("Authorization", "Bearer " + jwtSimpleUser)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        
        // Assert response status is HTTP 200 OK
        int actualStatus = mvcResult.getResponse().getStatus();
        int expectedStatus = HttpStatus.OK.value();
        
        assertEquals(expectedStatus, actualStatus);
    }
    
    @Test
    public void getAllBooks_ShouldReturnUnauthorized_WhenNoJwtProvided() throws Exception {
        // Perform GET request to fetch all books with no JWT token provided
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get(Constants.GET_ALL_BOOKS_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        
        String bodyString = mvcResult.getResponse().getContentAsString();
        
        // Assert response status is HTTP 401 Unauthorized
        int actualStatus = mvcResult.getResponse().getStatus();
        int expectedStatus = HttpStatus.UNAUTHORIZED.value();
        
        assertEquals(expectedStatus, actualStatus);
        // Assert that response contains an error message
        assertTrue(bodyString.contains("error"));
    }
    
    /**
     * Extracts JWT token string from JSON response string.
     * Assumes token is at a fixed substring position.
     * 
     * @param responseAsString response body string containing token JSON
     * @return extracted JWT token string
     */
    private String getTokenFromResponseAsString(String responseAsString) {
        return responseAsString.substring(10, responseAsString.length() - 2);
    }
    
    /**
     * Performs login mock request to get JWT token for given user credentials.
     * 
     * @param user UserRequest containing username and password
     * @param url login URL
     * @return JWT token string
     * @throws Exception in case of request failure
     */
    private String getJWTafterMock(UserRequest user, String url) throws Exception {
        MvcResult mvcResult = GetMVCResultFromMockMvcPerform(user, url);
        String bodyString = mvcResult.getResponse().getContentAsString();
        return getTokenFromResponseAsString(bodyString);
    }
    
    /**
     * Helper to perform POST request with UserRequest JSON payload.
     * 
     * @param user UserRequest object
     * @param url endpoint URL
     * @return MvcResult of the performed request
     * @throws Exception in case of request failure
     */
    private MvcResult GetMVCResultFromMockMvcPerform(UserRequest user, String url) throws Exception {
        return this.mockMvc.perform(MockMvcRequestBuilders.post(url)
                .content(Converter.convertToString(user))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
    }
    
    /**
     * Helper to perform POST request with BookRequest JSON payload and Authorization header.
     * 
     * @param bookRequest BookRequest object
     * @param url endpoint URL
     * @param jwtToken JWT token for Authorization header
     * @return MvcResult of the performed request
     * @throws Exception in case of request failure
     */
    private MvcResult GetMVCResultFromMockMvcPerform(BookRequest bookRequest, String url, String jwtToken) throws Exception {
        return this.mockMvc.perform(MockMvcRequestBuilders.post(url)
                .content(Converter.convertToString(bookRequest))
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
    }
}
