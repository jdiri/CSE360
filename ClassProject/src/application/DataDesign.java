package application;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DataDesign {
	
    private static final String USERS_FILE = "users.txt";
    private static final String BOOKS_FILE = "books.txt";
    private static final String TRANSACTIONS_FILE = "transactions.txt";

  
    public static class Book {
    	
        private String title;
        private double preMarkupPrice;
        private double finalPrice;
        private String category;
        private String condition;
        private int quantity;
        private Seller seller;

        public Book(String title, double preMarkupPrice, String category, String condition, int quantity, Seller seller) {
            this.title = title;
            this.preMarkupPrice = preMarkupPrice;
            this.category = category;
            this.condition = condition;
            this.quantity = quantity;
            this.seller = seller;
            calculateFinalPrice();
        }

        public double calculateFinalPrice() {
     
            double markup = switch (condition.toLowerCase()) {
                case "used like new" -> 0.25;   
                case "moderately used" -> 0.15; 
                case "heavily used" -> 0.05;     
                default -> 0.15;
            };
            
            finalPrice = preMarkupPrice * (1 + markup);
            return finalPrice;
        }

        public void updateQuantity() {quantity--;}

        public Map<String, Object> getBookDetails() {
            Map<String, Object> details = new HashMap<>();
            details.put("title", title);
            details.put("preMarkupPrice", preMarkupPrice);
            details.put("finalPrice", finalPrice);
            details.put("category", category);
            details.put("condition", condition);
            details.put("quantity", quantity);
            details.put("seller", seller.getASUID());
            return details;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {return true;}
            if (obj == null || getClass() != obj.getClass()) {return false;}
            Book other = (Book) obj;
            return title.equals(other.title) && 
                   category.equals(other.category) && 
                   condition.equals(other.condition) &&
                   seller.equals(other.seller);
        }

        public Seller getSeller() {return seller;}

        public String getTitle() { return title; }
        public double getPreMarkupPrice() { return preMarkupPrice; }
        public double getFinalPrice() { return finalPrice; }
        public String getCategory() { return category; }
        public String getCondition() { return condition; }
        public int getQuantity() { return quantity; }
    }


    public static abstract class User {
        protected String asuID;
        protected String password;
        protected String role;
        protected boolean isApproved;

        public User(String asuID, String password, String role) {
            this.asuID = asuID;
            this.password = password;
            this.role = role;
            this.isApproved = role.equals("buyer");
        }

        public void register(String asuID, String password, String role) {
            this.asuID = asuID;
            this.password = password;
            this.role = role;
        }

        public boolean login() {return isApproved;}

        public void logout() {
  
        }

 
        public String getRole() { return role; }
        public String getPassword() { return password; }
        public String getASUID() { return asuID; }
        public void setRole(String role) { this.role = role; }
        public void setPassword(String password) { this.password = password; }
        public void setASUID(String asuID) { this.asuID = asuID; }
        public boolean getApprovalStatus() { return isApproved; }
        public void setApprovalStatus(boolean status) { this.isApproved = status; }
    }


    public static class Buyer extends User {
    	
        private List<Book> cart;

        public Buyer(String asuID, String password) {
            super(asuID, password, "buyer");
            cart = new ArrayList<>();
        }

        public void addToCart(Book book) {cart.add(book);}
        public void addToCart(Book book, int quantity) {
            if (book.getQuantity() < quantity) {
                System.out.println("Not enough stock available!");
                return;
            }
            for (int i = 0; i < quantity; i++) {
                cart.add(book);
                book.updateQuantity();
            }
            System.out.println(quantity + " copies of " + book.getTitle() + " added to cart.");
        }

        public void removeFromCart(Book book) {cart.remove(book);}
        
        

        public void clearCart() {cart.clear();}

        public Transaction checkout() {
            if (cart.isEmpty()) {return null;}

            Transaction transaction = new Transaction(cart.get(0), this);
            clearCart();
            return transaction;
        }

        public List<Book> viewBooks() {return Bookstore.getInstance().getBooksByFilter(null, null);}

        public List<Book> getCart() {return new ArrayList<>(cart);}
    }


    public static class Seller extends User {
        private List<Book> listedBooks;

        public Seller(String asuID, String password) {
            super(asuID, password, "seller");
            listedBooks = new ArrayList<>();
        }

        public void listBook(String title, double preMarkupPrice, String category, String condition, int quantity) {
            Book book = new Book(title, preMarkupPrice, category, condition, quantity, this);
            listedBooks.add(book);
            Bookstore.getInstance().updateInventory(book);
        }

        public List<Book> getListedBooks() {
            return new ArrayList<>(listedBooks);
        }

        public Map<String, Object> getSellerStats() {
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalBooks", listedBooks.size());
            stats.put("totalSold", calculateTotalSold());
            return stats;
        }

        private int calculateTotalSold() {
   
            return Bookstore.getInstance().getTransactions().stream()
                   .filter(t -> t.getBook().getSeller().equals(this))
                   .toList()
                   .size();
        }

        public void updateBookQuantity(Book book) {
            if (listedBooks.contains(book)) {
                book.updateQuantity();
            }
        }
    }


    public static class Admin extends User {
        public static final String ADMIN_ID = "admin";
        public static final String ADMIN_PASSWORD = "password";

        public Admin() {
            super(ADMIN_ID, ADMIN_PASSWORD, "admin");
            this.isApproved = true;
        }

        public void manageUser(User user) {
            if (user == null) return;
            
            Bookstore store = Bookstore.getInstance();
            List<User> users = store.getUsers();
            
            // if user exists in system
            boolean userExists = users.stream()
                                    .anyMatch(u -> u.getASUID().equals(user.getASUID()));
                                    
            if (!userExists) return;
            
            // if user is a seller awaiting approval -> they will be in pendingSellers list
            if (user.getRole().equals("seller") && !user.getApprovalStatus()) { return;}
            
 
            for (User existingUser : users) {
                if (existingUser.getASUID().equals(user.getASUID())) {
                    // update details
                    existingUser.setPassword(user.getPassword());
                    existingUser.setRole(user.getRole());
                    existingUser.setApprovalStatus(user.getApprovalStatus());
                    break;
                }
            }
            
            // save
            store.saveData();
        }

        public List<Transaction> viewTransactionHistory() {
            return Bookstore.getInstance().getTransactions();
        }

        public Map<String, Object> getSystemStats() {
            Map<String, Object> stats = new HashMap<>();
            Bookstore store = Bookstore.getInstance();
            
            stats.put("totalUsers", store.getUsers().size());
            stats.put("totalBooks", store.getInventory().size());
            stats.put("totalTransactions", store.getTransactions().size());
            
            return stats;
        }
    }

 
    public static class Transaction {
        private String timestamp;
        private Book book;
        private User buyer;

        public Transaction(Book book, User buyer) {
            this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            this.book = book;
            this.buyer = buyer;
        }

        public Map<String, Object> getTransactionDetails() {
            Map<String, Object> details = new HashMap<>();
            details.put("timestamp", timestamp);
            details.put("book", book.getBookDetails());
            details.put("buyer", buyer.getASUID());
            return details;
        }


        public String getTimestamp() { return timestamp; }
        public Book getBook() { return book; }
        public User getBuyer() { return buyer; }
    }


    public static class Bookstore {
        private static Bookstore instance;
        private List<User> users;
        private List<User> pendingSellers;
        private List<Book> inventory;
        private List<Transaction> transactions;

        public Bookstore() {
            users = new ArrayList<>();
            pendingSellers = new ArrayList<>();
            inventory = new ArrayList<>();
            transactions = new ArrayList<>();
            loadData(); 
        }

        public static Bookstore getInstance() {
            if (instance == null) {
                instance = new Bookstore();
            }
            return instance;
        }
        
        private void saveUsers() {
            try {
                List<String> lines = new ArrayList<>();
                
              
                for (User user : users) {
                    lines.add(String.format("%s,%s,%s,%b",
                        user.getASUID(),
                        user.getPassword(),
                        user.getRole(),
                        user.getApprovalStatus()
                    ));
                }
                
               
                for (User user : pendingSellers) {
                    lines.add(String.format("%s,%s,%s,%b",
                        user.getASUID(),
                        user.getPassword(),
                        user.getRole(),
                        user.getApprovalStatus()
                    ));
                }

                Files.write(Paths.get(USERS_FILE), lines);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        private void loadData() {
            loadUsers();
            loadBooks();
            loadTransactions();
        }
        
        private void saveData() {
            saveUsers();
            saveBooks();
            saveTransactions();
        }
        
        private void loadUsers() {
            try {
                if (!Files.exists(Paths.get(USERS_FILE))) {
                    Files.createFile(Paths.get(USERS_FILE));
                    return;
                }

                List<String> lines = Files.readAllLines(Paths.get(USERS_FILE));
                for (String line : lines) {
                    String[] parts = line.split(",");
                    if (parts.length == 4) {
                        String id = parts[0];
                        String password = parts[1];
                        String role = parts[2];
                        boolean isApproved = Boolean.parseBoolean(parts[3]);

                        User user = switch (role) {
                            case "buyer" -> new Buyer(id, password);
                            case "seller" -> new Seller(id, password);
                            default -> null;
                        };

                        if (user != null) {
                            user.setApprovalStatus(isApproved);
                            if (isApproved) {
                                users.add(user);
                            } else {
                                pendingSellers.add(user);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void registerUser(String asuID, String password, String role) {
            User newUser = switch (role.toLowerCase()) {
                case "buyer" -> new Buyer(asuID, password);
                case "seller" -> new Seller(asuID, password);
                default -> throw new IllegalArgumentException("Invalid role");
            };

            if (role.equals("seller")) {
                pendingSellers.add(newUser);
            } else {
                users.add(newUser);
            }
            saveUsers(); 
        }

        public User authenticateUser(String asuID, String password) {
 
            if (asuID.equals(Admin.ADMIN_ID) && password.equals(Admin.ADMIN_PASSWORD)) {
                return new Admin();
            }

           
            return users.stream()
                   .filter(u -> u.getASUID().equals(asuID) && u.getPassword().equals(password))
                   .findFirst()
                   .orElse(null);
        }

        public List<Book> getBooksByFilter(String category, String condition) {
            return inventory.stream()
                   .filter(b -> (category == null || b.getCategory().equals(category)) &&
                               (condition == null || b.getCondition().equals(condition)))
                   .toList();
        }
        
        private void loadBooks() {
            try {
                if (!Files.exists(Paths.get(BOOKS_FILE))) {
                    Files.createFile(Paths.get(BOOKS_FILE));
                    return;
                }

                List<String> lines = Files.readAllLines(Paths.get(BOOKS_FILE));
                for (String line : lines) {
                    String[] parts = line.split(",");
                    if (parts.length == 6) {
                        String title = parts[0];
                        double preMarkupPrice = Double.parseDouble(parts[1]);
                        String category = parts[2];
                        String condition = parts[3];
                        int quantity = Integer.parseInt(parts[4]);
                        String sellerID = parts[5];

                        Seller seller = (Seller) users.stream()
                            .filter(u -> u.getRole().equals("seller") && u.getASUID().equals(sellerID))
                            .findFirst()
                            .orElse(null);

                        if (seller != null) {
                            Book book = new Book(title, preMarkupPrice, category, condition, quantity, seller);
                            inventory.add(book);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        private void loadTransactions() {
            try {
                if (!Files.exists(Paths.get(TRANSACTIONS_FILE))) {
                    Files.createFile(Paths.get(TRANSACTIONS_FILE));
                    return;
                }

                List<String> lines = Files.readAllLines(Paths.get(TRANSACTIONS_FILE));
                for (String line : lines) {
                    String[] parts = line.split(",");
                    if (parts.length == 5) {
                        String timestamp = parts[0];
                        String bookTitle = parts[1];
                        String buyerID = parts[2];
                        String sellerID = parts[3];
                        double price = Double.parseDouble(parts[4]);

             
                        User buyer = users.stream()
                            .filter(u -> u.getASUID().equals(buyerID))
                            .findFirst()
                            .orElse(null);

                        Book book = inventory.stream()
                            .filter(b -> b.getTitle().equals(bookTitle) && 
                                       b.getSeller().getASUID().equals(sellerID))
                            .findFirst()
                            .orElse(null);

                        if (buyer != null && book != null) {
                            Transaction transaction = new Transaction(book, buyer);
                            transactions.add(transaction);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        private void saveTransactions() {
            try {
                List<String> lines = new ArrayList<>();
                for (Transaction transaction : transactions) {
                    lines.add(String.format("%s,%s,%s,%s,%.2f",
                        transaction.getTimestamp(),
                        transaction.getBook().getTitle(),
                        transaction.getBuyer().getASUID(),
                        transaction.getBook().getSeller().getASUID(),
                        transaction.getBook().getFinalPrice()
                    ));
                }
                Files.write(Paths.get(TRANSACTIONS_FILE), lines);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        private void saveBooks() {
            try {
                List<String> lines = new ArrayList<>();
                for (Book book : inventory) {
                    lines.add(String.format("%s,%.2f,%s,%s,%d,%s",
                        book.getTitle(),
                        book.getPreMarkupPrice(),
                        book.getCategory(),
                        book.getCondition(),
                        book.getQuantity(),
                        book.getSeller().getASUID()
                    ));
                }
                Files.write(Paths.get(BOOKS_FILE), lines);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void processTransaction(Transaction transaction) {
            transactions.add(transaction);
            transaction.getBook().updateQuantity();
            saveBooks(); 
            saveTransactions();
        }

        public void updateInventory(Book book) {
            inventory.add(book);
            saveBooks(); 
        }

        public Map<String, Object> getStats() {
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalUsers", users.size());
            stats.put("totalBooks", inventory.size());
            stats.put("totalTransactions", transactions.size());
            return stats;
        }

        public List<Book> findIdenticalBooks(Book book) {
            return inventory.stream()
                   .filter(b -> b.equals(book))
                   .toList();
        }

        public List<User> getPendingSellers() {
            return new ArrayList<>(pendingSellers);
        }

        public void approveSeller(User seller) {
            if (pendingSellers.remove(seller)) {
                seller.setApprovalStatus(true);
                users.add(seller);
                saveUsers(); 
            }
        }

        public void denySeller(User seller) {
            pendingSellers.remove(seller);
            saveUsers(); 
        }

       
        public List<User> getUsers() { return new ArrayList<>(users); }
        public List<Book> getInventory() { return new ArrayList<>(inventory); }
        public List<Transaction> getTransactions() { return new ArrayList<>(transactions); }
    }
}
