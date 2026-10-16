import java.io.*;
import java.util.*;
import java.util.stream.*;

class BookException extends Exception {
    BookException(String message) { super(message); }
}

class Book {
    String title;
    boolean available = true;

    Book(String title) { this.title = title; }

    void borrow() throws BookException {
        if (!available) throw new BookException(title + " is already borrowed!");
        available = false;
        System.out.println("Borrowed: " + title);
    }

    void returnBook() throws BookException {
        if (available) throw new BookException(title + " was not borrowed!");
        available = true;
        System.out.println("Returned: " + title);
    }

    String status() { return available ? "Available" : "Borrowed"; }
}

class ReferenceBook extends Book {
    ReferenceBook(String title) { super(title); }

    @Override
    void borrow() throws BookException {
        throw new BookException(title + " is a reference book and cannot be borrowed.");
    }

    @Override
    String status() { return "Reference only"; }
}

public class CommunityLibrarySystem {
    static Book[] books = {
        new Book("Java Basics"),
        new Book("Data Structures"),
        new Book("Algorithms"),
        new Book("The Great Gatsby"),
        new Book("1984"),
        new ReferenceBook("Oxford Dictionary")
    };

    static List<String> borrowedTitles = new ArrayList<>();
    static List<String> transactions   = new ArrayList<>();
    static final String FILE_NAME = "library.txt";

    static void showBooks() {
        System.out.println("\n--- Library Books ---");
        for (int i = 0; i < books.length; i++) {
            System.out.println(i + ". " + books[i].title + " | " + books[i].status());
        }
    }

    static int countAvailable(int index) {
        if (index == books.length) return 0;
        return (books[index].available && !(books[index] instanceof ReferenceBook) ? 1 : 0)
                + countAvailable(index + 1);
    }

    static boolean validIndex(int i) { return i >= 0 && i < books.length; }

    static void borrowBook(Scanner sc) {
        showBooks();
        sc.nextLine(); // consume leftover newline
        System.out.print("Enter your name: ");
        String member = sc.nextLine();  
        System.out.print("Enter book number to borrow: ");
        int choice = sc.nextInt();
        sc.nextLine(); // consume newline

        if (!validIndex(choice)) {
            System.out.println("Invalid choice.");
            return;
        }
        try {
            books[choice].borrow();
            borrowedTitles.add(books[choice].title);
            transactions.add(member + " borrowed " + books[choice].title);
        } catch (BookException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void returnBook(Scanner sc) {
        showBooks();
        System.out.print("Enter book number to return: ");
        int choice = sc.nextInt();
        sc.nextLine(); 

        if (!validIndex(choice)) {
            System.out.println("Invalid choice.");
            return;
        }
        try {
            books[choice].returnBook();
            transactions.add("Returned " + books[choice].title); // log return
        } catch (BookException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void saveReport() {
        if (transactions.isEmpty()) {
            System.out.println("No transactions to save yet.");
            return;
        }
        try (FileWriter fw = new FileWriter(FILE_NAME)) {
            fw.write(String.join("\n", transactions));
            System.out.println("Report saved to " + FILE_NAME);
        } catch (IOException e) {
            System.out.println("Error writing file.");
        }
    }

    static void readReport() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            System.out.println("\n--- Saved Report ---");
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line.toUpperCase());
            }
        } catch (IOException e) {
            System.out.println("No report found. Save one first.");
        }
    }

    static void statistics() {
        if (borrowedTitles.isEmpty()) {
            System.out.println("No borrowing data yet.");
            return;
        }
        System.out.println("\n--- Library Statistics ---");
        System.out.println("Total borrowed: " + borrowedTitles.size());

        String mostBorrowed = borrowedTitles.stream()
                .collect(Collectors.groupingBy(b -> b, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .get().getKey();
        System.out.println("Most borrowed: " + mostBorrowed);

        System.out.println("Unique books: " + borrowedTitles.stream().distinct().toList());
        System.out.println("Books currently available: " + countAvailable(0));
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("=== COMMUNITY MANAGEMENT LIBRARY SYSTEM ===");

        while (true) {
            System.out.println("\n1. Show Books");
            System.out.println("2. Borrow Book");
            System.out.println("3. Return Book");
            System.out.println("4. Save Report to File");
            System.out.println("5. Read Report from File");
            System.out.println("6. Statistics");
            System.out.println("7. Exit");
            System.out.print("Choose: ");

            int choice;
            try {
                choice = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Please enter a number.");
                sc.nextLine(); 
                continue;
            }

            if (choice == 1)      showBooks();
            else if (choice == 2) borrowBook(sc);
            else if (choice == 3) returnBook(sc);
            else if (choice == 4) saveReport();
            else if (choice == 5) readReport();
            else if (choice == 6) statistics();
            else if (choice == 7) { System.out.println("Goodbye!"); break; }
            else                  System.out.println("Invalid option.");
        }
        sc.close();
    }
}
