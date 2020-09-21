package com.lambdaschool.bookstore.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lambdaschool.bookstore.models.*;
import com.lambdaschool.bookstore.services.BookService;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BookService bookService;

    private List<Book> bookList = new ArrayList<>();

    @Before
    public void setUp() throws Exception {

        Author a1 = new Author("John", "Mitchell");
        Author a2 = new Author("Dan", "Brown");
        Author a3 = new Author("Jerry", "Poe");
        Author a4 = new Author("Wells", "Teague");
        Author a5 = new Author("George", "Gallinger");
        Author a6 = new Author("Ian", "Stewart");

        a1.setAuthorid(1);
        a2.setAuthorid(2);
        a3.setAuthorid(3);
        a4.setAuthorid(4);
        a5.setAuthorid(5);
        a6.setAuthorid(6);


        Section s1 = new Section("Fiction");
        Section s2 = new Section("Technology");
        Section s3 = new Section("Travel");
        Section s4 = new Section("Business");
        Section s5 = new Section("Religion");

        s1.setSectionid(10);
        s2.setSectionid(11);
        s3.setSectionid(12);
        s4.setSectionid(13);
        s5.setSectionid(14);


        String b1Name = "Flatterland";
        String b2Name = "Digital Fortress";
        String b3Name = "The Da Vinci Code";
        String b4Name = "Essentials of Finance";
        String b5Name = "Calling Texas Home";

        Book b1 = new Book(b1Name, "9780738206752", 2001, s1);
        b1.setBookid(20);
        b1.getWrotes()
                .add(new Wrote(a6, new Book()));



        Book b2 = new Book(b2Name, "9788489367012", 2007, s1);
        b2.setBookid(30);
        b2.getWrotes()
                .add(new Wrote(a2, new Book()));


        Book b3 = new Book(b3Name, "9780307474278", 2009, s1);
        b3.setBookid(40);
        b3.getWrotes()
                .add(new Wrote(a2, new Book()));


        Book b4 = new Book(b4Name, "1314241651234", 0, s4);
        b4.setBookid(50);
        b4.getWrotes()
                .add(new Wrote(a3, new Book()));
        b4.getWrotes()
                .add(new Wrote(a5, new Book()));


        Book b5 = new Book(b5Name, "1885171382134", 2000, s3);
        b4.setBookid(50);
        b5.getWrotes()
                .add(new Wrote(a4, new Book()));

        bookList.add(b1);
        bookList.add(b2);
        bookList.add(b3);
        bookList.add(b4);
        bookList.add(b5);



    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void listAllBooks() throws Exception {
        String apiUrl = "/books/books";
        Mockito.when(bookService.findAll()).thenReturn(bookList);

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON);
        MvcResult r = mockMvc.perform(rb).andReturn();
        String tr = r.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(bookList);

        System.out.println(er);
        assertEquals(er, tr);
    }

    @Test
    public void getBookById() throws Exception {
        String apiUrl = "/books/book/20";
        Mockito.when(bookService.findBookById(20))
                .thenReturn(bookList.get(0));

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl)
                .accept(MediaType.APPLICATION_JSON);
        MvcResult r = mockMvc.perform(rb)
                .andReturn();
        String tr = r.getResponse()
                .getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(bookList.get(0));

        System.out.println(tr);
        assertEquals(er,
                tr);
    }
    @Test
    public void getBookByIdNotFound() throws Exception {
        String apiUrl = "/books/book/2000";
        Mockito.when(bookService.findBookById(2000))
                .thenReturn(null);

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl)
                .accept(MediaType.APPLICATION_JSON);
        MvcResult r = mockMvc.perform(rb)
                .andReturn();
        String tr = r.getResponse()
                .getContentAsString();
        String er ="";

        assertEquals(er,
                tr);
    }

    @Test
    public void w_addNewBook() throws Exception {
        String apiUrl = "/books/book";
        String b10Name = "ThisISforThEtEST";
        Author a6 = new Author("Ian", "Stewart");
        a6.setAuthorid(6);

        Section s1 = new Section("Fiction");
        s1.setSectionid(10);

        Book b10 = new Book(b10Name, "9780738206752", 2001, s1);
        b10.setBookid(200);
        b10.getWrotes()
                .add(new Wrote(a6, new Book()));

        ObjectMapper mapper = new ObjectMapper();
        String bookString = mapper.writeValueAsString(b10);

        Mockito.when(bookService.save(any(Book.class))).thenReturn(b10);

        RequestBuilder rb = MockMvcRequestBuilders.post(apiUrl).accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookString);

        mockMvc.perform(rb).andExpect(status().isCreated()).andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void x_updateFullBook() throws Exception{
        String apiUrl = "/books/book/200";
        String b10Name = "Dogdogdogdoggo";
        Author a6 = new Author("Ian", "Stewart");
        a6.setAuthorid(6);

        Section s1 = new Section("Fiction");
        s1.setSectionid(10);

        Book b10 = new Book(b10Name, "9780738206752", 2001, s1);
        b10.setBookid(200);
        b10.getWrotes()
                .add(new Wrote(a6, new Book()));


        Mockito.when(bookService.update(b10, 200))
                .thenReturn(b10);
        ObjectMapper mapper = new ObjectMapper();
        String restaurantString = mapper.writeValueAsString(b10);

        RequestBuilder rb = MockMvcRequestBuilders.put(apiUrl, 10L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(restaurantString);

        mockMvc.perform(rb)
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    public void y_deleteBookById()  throws Exception{
        String apiUrl = "/books/book/{bookid}";

        RequestBuilder rb = MockMvcRequestBuilders.delete(apiUrl, "200")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(rb)
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}