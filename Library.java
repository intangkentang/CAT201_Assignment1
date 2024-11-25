import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;

public class Library {
    private List<Book> books;
    private final String filePath = "src/books.csv";

    public Library() {
        books = new ArrayList<>();
        loadBooksFromFile();
    } //creating new array list for book class

    public List<Book> getBooks() {
        return books;
    } //getter to get book details

    public boolean borrowABook(Book book, String borrowerName) {
        if(book !=null && book.getStatus()) {
            book.borrowBook(borrowerName);
            return true;
        }
        return false;
    } //method to check status before borrow book

    public boolean returnABook(int isbn) {
        for (Book book : books) {
            if (book.getISBN()==isbn && !book.getStatus()) {
                book.returnBook();
                return true;
            }
        }
        return false;
    } //method to check status before return book

    public void addBook(Book book) {
        if(book !=null && !books.contains(book)) {
            books.add(book); //using fx of imported package
            saveBookToFile(book);
        }
    } //method to add book and load into file

    public Book searchBookByTitle(String title) {
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(title)) {
                return book;
            }
        }
        return null;
    } //method to search book by title

    public Book searchBookByAuthor(String author) {
        for (Book book : books) {
            if (book.getAuthor().equalsIgnoreCase(author)) {
                return book;
            }
        }
        return null;
    } //method to search book by author

    public Book searchBookByISBN(int isbn) {
        for (Book book : books) {
            if (book.getISBN()==isbn) {
                return book;
            }
        }
        return null;
    } //method to search book by isbn

    private void saveBookToFile(Book book) {
        try (FileWriter fw = new FileWriter(filePath, true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            String bookData = String.format("%s,%s,%d,%b,%s", book.getTitle(), book.getAuthor(), book.getISBN(), book.getStatus(), book.getBorrowerName());
            bw.newLine();
            bw.write(bookData);
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    } //method to save added book into csv file

    private void loadBooksFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            //read line data
            while ((line = br.readLine()) != null) {
                String[] details = line.split(",");
                String title = details[0];
                String author = details[1];
                int ISBN = Integer.parseInt(details[2]);
                boolean status = Boolean.parseBoolean(details[3]);
                String borrower = (details.length > 4) ? details[4] : "";
                books.add(new Book(title, author, ISBN, status, borrower));
            }
            System.out.println("Books loaded successfully.");
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    } //method to load all books from csv file

    public void saveBookList( ObservableList<Book> bookList) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Write each book to the CSV file
            for (Book book : bookList) {
                writer.write(book.toCSV());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error saving book list to file: " + e.getMessage(), e);
        }
    } //method to save updated data into file
}
