package com.lambdaschool.bookstore.services;

import com.lambdaschool.bookstore.BookstoreApplication;
import com.lambdaschool.bookstore.exceptions.ResourceNotFoundException;
import com.lambdaschool.bookstore.models.Author;
import com.lambdaschool.bookstore.models.Book;
import com.lambdaschool.bookstore.models.Section;
import com.lambdaschool.bookstore.models.Wrote;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BookstoreApplication.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BookServiceImplTest {

    @Autowired
    private BookService bookService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        List<Book> myList = bookService.findAll();
        for(Book b : myList){
            System.out.println(b.getBookid() + " " + b.getTitle());
        }
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void A_findAll() {
        assertEquals(5, bookService.findAll().size());
    }

    @Test
    public void AA_findBookById() {
        assertEquals("Flatterland", bookService.findBookById(26).getTitle());
    }

    @Test(expected = EntityNotFoundException.class)
    public void AAB_findBookByIdNotFound(){
        assertEquals("hello",bookService.findBookById(10000).getTitle());
    }

    @Test
    public void z_delete() {
        bookService.delete(26);
        assertEquals(5,bookService.findAll().size());
    }
    @Test(expected = EntityNotFoundException.class)
    public void zz_notFoundDelete()
    {
        bookService.delete(100);
        assertEquals(5, bookService.findAll()
                .size());
    }

    @Test
    public void b_save() {
        Author a7 = new Author("Julliann", "Dulay");
        a7.setAuthorid(20);
        Section s6 = new Section("Fitness");
        s6.setSectionid(21);

        Book b5 = new Book("Drink Water", "021099103910", 2000, s6);
        b5.getWrotes()
                .add(new Wrote(a7, new Book()));
        Book saveB5 = bookService.save(b5);
        assertEquals("Drink Water", saveB5.getTitle());

    }

    @Test(expected = ResourceNotFoundException.class)
    public void bbba_saveputfailed(){
        Author a7 = new Author("Julliann", "Dulay");
        a7.setAuthorid(20);
        Section s6 = new Section("Fitness");
        s6.setSectionid(201);

        Book b5 = new Book("Drink Water", "021099103910", 2000, s6);
        b5.getWrotes()
                .add(new Wrote(a7, new Book()));
        Book saveB5 = bookService.save(b5);
        assertEquals("Drink Water", saveB5.getTitle());
    }
    @Test(expected = EntityNotFoundException.class)
    public void bbbad_saveputfailed(){
        Author a7 = new Author("Julliann", "Dulay");
        a7.setAuthorid(20);
        Section s6 = new Section("Fitness");
        s6.setSectionid(21);

        Book b5 = new Book("Drink Water", "021099103910", 2000, s6);
        b5.getWrotes()
                .add(new Wrote(a7, new Book()));
        b5.setBookid(500);
        Book saveB5 = bookService.save(b5);
        assertEquals("Drink Water", saveB5.getTitle());
    }
    @Test(expected = EntityNotFoundException.class)
    public void bbbad_saveputfailedAuthor(){
        Author a7 = new Author("Julliann", "Dulay");
        Section s6 = new Section("Fitness");
        s6.setSectionid(21);

        Book b5 = new Book("Drink Water", "021099103910", 2000, s6);
        b5.getWrotes()
                .add(new Wrote(a7, new Book()));
        b5.setBookid(500);
        b5.setCopy(300);
        Book saveB5 = bookService.save(b5);
        assertEquals("Drink Water", saveB5.getTitle());
    }

    @Test
    public void  cc_update() {
        Author a6 = new Author("Ian", "Stewart");
        a6.setAuthorid(20);
        Section s1 = new Section("Fiction");
        s1.setSectionid(21);
        Book b1 = new Book("ButterLand", "9780738206752", 2001, s1);
        b1.getWrotes()
                .add(new Wrote(a6, new Book()));
        b1.setCopy(100);
        Book newB1 = bookService.update(b1,26);
        assertEquals("ButterLand", newB1.getTitle());
    }
    @Test(expected = EntityNotFoundException.class)
    public void  ccd_updateFailed() {
        Author a6 = new Author("Ian", "Stewart");
        a6.setAuthorid(20450);
        Section s1 = new Section("Fiction");
        s1.setSectionid(21);
        Book b1 = new Book("ButterLand", "9780738206752", 2001, s1);
        b1.getWrotes()
                .add(new Wrote(a6, new Book()));
        b1.setCopy(100);
        Book newB1 = bookService.update(b1,26);
        assertEquals("ButterLand", newB1.getTitle());
    }

    @Test
    public void zzz_deleteAll() {
        bookService.deleteAll();
        assertEquals(0,bookService.findAll().size());
    }
}