//package org.example.ver2;
//
//import org.example.ver2.Transaction.Transaction;
//import org.example.ver2.Transaction.TransactionDao;
//import org.example.ver2.Transaction.TransactionDaoImplementation;
//import org.example.ver2.User.User;
//import org.example.ver2.User.UserDao;
//import org.example.ver2.User.UserDaoImplementation;
//import javafx.application.Application;
//import javafx.beans.property.SimpleStringProperty;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.geometry.Insets;
//import javafx.geometry.Pos;
//import javafx.scene.Scene;
//import javafx.scene.control.*;
//import javafx.scene.control.cell.PropertyValueFactory;
//import javafx.scene.image.Image;
//import javafx.scene.layout.*;
//import javafx.stage.Stage;
//
//import java.sql.Timestamp;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.List;
//
//
//public class Main extends Application {
//    private final MemberService memberService = new MemberService();
//    private final ObservableList<Member> memberList = FXCollections.observableArrayList();
//    private final TableView<Member> memberTable = new TableView<>();
//    private final ComboBox<String> ViewSelector = new ComboBox<>();
//
//
//    @Override
//    public void start(Stage stage) {
//
////        Image bgImage = new Image("images/background.png");
////        BackgroundImage backgroundImage = new BackgroundImage(
////                bgImage,
////                BackgroundRepeat.NO_REPEAT,
////                BackgroundRepeat.NO_REPEAT,
////                BackgroundPosition.CENTER,
////                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
////        );
//
//        StackPane rootLayout = new StackPane();
////        rootLayout.setBackground(new Background(backgroundImage));
//
////        //Login
////        TextField usernameField = new TextField();
////        PasswordField passwordField = new PasswordField();
////        Button loginButton = new Button("Login");
////
////        VBox loginLayout = new VBox(10, new Label("Username"), usernameField,
////                new Label("Password"), passwordField, loginButton);
////
////
////        loginLayout.setPadding(new Insets(20));
////        Scene loginScene = new Scene(loginLayout, 300, 200);
////
////        UserDao userDao = new UserDaoImplementation();
////
////        loginButton.setOnAction(e -> {
////            String username = usernameField.getText();
////            String password = passwordField.getText();
////
////             User logIn = userDao.logIn(username, password);
////
////             if(logIn != null) {
////                 Alert alert = new Alert(Alert.AlertType.INFORMATION, "Login successful!");
////                 alert.showAndWait();
////
////                 showDashboard(stage);
////             } else {
////                 Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid username or password");
////                 alert.showAndWait();
////             }
////
////
////        });
////
////        stage.setTitle("Login");
////        stage.setScene(loginScene);
////        stage.show();
//
//
//        ViewSelector.getItems().addAll("Insert Member", "View/Delete Member", "Update Member", "Transaction History");
//        ViewSelector.setValue("Insert Member");
//
//        Label menuLabel = new Label("Select Action:");
//        menuLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
//
//
//        VBox menuBox = new VBox(5, menuLabel, ViewSelector);
//        menuBox.setPadding(new Insets(10));
//        menuBox.setSpacing(5);
//        menuBox.setMaxWidth(350);
//
//
//        //Input fields
//        TextField insertAccountNumberField = new TextField();
//        TextField insertFirstNameField = new TextField();
//        TextField insertMiddleNameField = new TextField();
//        TextField insertLastNameField = new TextField();
//        TextField insertAddressField = new TextField();
//        DatePicker insertBirthdatePicker = new DatePicker();
//
//        Label ageLabel = new Label("Age");
//        TextField ageField = new TextField();
//        ageField.setPrefWidth(250);
//        ageField.setEditable(false);
//
//        insertBirthdatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
//            if(newValue != null) {
//                int age = calculateAge(newValue, LocalDate.now());
//                ageField.setText(String.valueOf(age));
//                ageLabel.setText("Age");
//            } else {
//                ageField.clear();
//                ageLabel.setText("");
//            }
//        });
//
//
//        //Insert Button
//        Button insertBtn = new Button("Add");
//
//        GridPane insertLayout = new GridPane();
//        insertLayout.setPadding(new Insets(50));
//        insertLayout.setHgap(10);
//        insertLayout.setVgap(10);
//
//
//        ColumnConstraints col1 = new ColumnConstraints();
//        col1.setPrefWidth(120);
//        ColumnConstraints col2 = new ColumnConstraints();
//        col2.setPrefWidth(350);
//        insertLayout.getColumnConstraints().addAll(col1, col2);
//
//
//        insertAccountNumberField.setPrefWidth(350);
//        insertFirstNameField.setPrefWidth(350);
//        insertMiddleNameField.setPrefWidth(350);
//        insertLastNameField.setPrefWidth(350);
//        insertAddressField.setPrefWidth(350);
//        insertBirthdatePicker.setPrefWidth(250);
//
//
//        insertLayout.add(new Label("Account Number"), 0, 0);
//        insertLayout.add(insertAccountNumberField, 1, 0);
//        insertLayout.add(new Label("First Name"), 0, 1);
//        insertLayout.add(insertFirstNameField, 1, 1);
//        insertLayout.add(new Label("Middle Name"), 0, 2);
//        insertLayout.add(insertMiddleNameField, 1, 2);
//        insertLayout.add(new Label("Last Name"), 0, 3);
//        insertLayout.add(insertLastNameField, 1, 3);
//        insertLayout.add(new Label("Address"), 0, 4);
//        insertLayout.add(insertAddressField, 1, 4);
//        insertLayout.add(new Label("Birthdate"), 0, 5);
//        insertLayout.add(insertBirthdatePicker, 1, 5);
//        insertLayout.add(ageLabel, 0, 6);
//        insertLayout.add(ageField, 1, 6);
//        insertLayout.add(insertBtn, 1, 7);
//
//
//
//        insertBtn.setOnAction(e -> {
//            String accountNumber = insertAccountNumberField.getText().trim();
//            String firstName = insertFirstNameField.getText().trim();
//            String middleName = insertMiddleNameField.getText().trim();
//            String lastName = insertLastNameField.getText().trim();
//            String address = insertAddressField.getText().trim();
//            LocalDate birthdate = insertBirthdatePicker.getValue();
//
//
//            if(accountNumber.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || address.isEmpty() || birthdate == null) {
//                showAlert("Missing fields", "Please fill in all required fields.");
//                return;
//            }
//
//            if(middleName.isEmpty() || middleName.equalsIgnoreCase("N/A")) {
//                middleName = "N/A";
//            }
//
//            Member member = Member.builder()
//                    .accountNumber(accountNumber)
//                    .firstName(firstName)
//                    .middleName(middleName)
//                    .lastName(lastName)
//                    .address(address)
//                    .birthdate(birthdate)
//                    .age(6)
//                    .build();
//
//
//            if(memberService.isDuplicateMember(member)) {
//                showAlert("Double Entry", "This member already exists.");
//            } else {
//                memberService.insertMember(member);
//                showAlert("Success", "Member inserted successfully!");
//                refreshTableData();
//            }
//            insertAccountNumberField.clear();
//            insertFirstNameField.clear();
//            insertMiddleNameField.clear();
//            insertLastNameField.clear();
//            insertAddressField.clear();
//            insertBirthdatePicker.setValue(null);
//        });
//
//        //Update fields
//        TextField idField = new TextField();
//        TextField accountNumberField = new TextField();
//        TextField firstNameField = new TextField();
//        TextField middleNameField = new TextField();
//        TextField lastNameField = new TextField();
//        TextField addressField = new TextField();
//        DatePicker birthdatePicker = new DatePicker();
//
//        //Update Button
//        Button updateBtn = new Button("Update");
//        GridPane updateLayout = new GridPane();
//        updateLayout.setPadding(new Insets(20));
//        updateLayout.setHgap(10);
//        updateLayout.setVgap(10);
//
//        ColumnConstraints col3 = new ColumnConstraints();
//        col3.setPrefWidth(120);
//        ColumnConstraints col4 = new ColumnConstraints();
//        col4.setPrefWidth(350);
//        updateLayout.getColumnConstraints().addAll(col3, col4);
//
//        idField.setPrefWidth(350);
//        accountNumberField.setPrefWidth(350);
//        firstNameField.setPrefWidth(350);
//        middleNameField.setPrefWidth(350);
//        lastNameField.setPrefWidth(350);
//        addressField.setPrefWidth(350);
//        birthdatePicker.setPrefWidth(250);
//
//        updateLayout.add(new Label("Member ID"), 0, 0);
//        updateLayout.add(idField, 1, 0);
//        updateLayout.add(new Label("Account Number"), 0, 1);
//        updateLayout.add(accountNumberField, 1, 1);
//        updateLayout.add(new Label("First Name"), 0, 2);
//        updateLayout.add(firstNameField, 1, 2);
//        updateLayout.add(new Label("Middle Name"), 0, 3);
//        updateLayout.add(middleNameField, 1, 3);
//        updateLayout.add(new Label("Last Name"), 0, 4);
//        updateLayout.add(lastNameField, 1, 4);
//        updateLayout.add(new Label("Address"), 0, 5);
//        updateLayout.add(addressField, 1, 5);
//        updateLayout.add(new Label("Birthdate"), 0, 6);
//        updateLayout.add(birthdatePicker, 1, 6);
//        updateLayout.add(updateBtn, 1, 7);
//
//
//        updateBtn.setOnAction(e -> {
//            String idText = idField.getText().trim();
//
//            if(idText.isEmpty()) {
//                showAlert("Missing ID", "Please enter the member ID to update.");
//                return;
//            }
//            int id;
//            try{
//                id = Integer.parseInt(idText);
//            } catch(NumberFormatException ex) {
//                showAlert("Invalid ID", "Member ID must be a number.");
//                return;
//            }
//
//            String accountNumber = accountNumberField.getText().trim();
//            String firstName = firstNameField.getText().trim();
//            String middleName = middleNameField.getText().trim();
//            String lastName = lastNameField.getText().trim();
//            String address = addressField.getText().trim();
//            LocalDate birthdate = birthdatePicker.getValue();
//
//            if(accountNumber.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || address.isEmpty() || birthdate == null) {
//                showAlert("Missing fields", "Please fill in all required fields.");
//                return;
//            }
//
//            if(middleName.isEmpty() || middleName.equalsIgnoreCase("N/A")) {
//                middleName = "N/A";
//            }
//
//            Member member = Member.builder()
//                    .id(id)
//                    .accountNumber(accountNumber)
//                    .firstName(firstName)
//                    .middleName(middleName)
//                    .lastName(lastName)
//                    .address(address)
//                    .birthdate(birthdate)
//                    .build();
//
//            boolean success = memberService.updateMember(member);
//
//            if(success) {
//                showAlert("Success", "Member updated successfully!");
//                refreshTableData();
//            } else {
//                showAlert("Failed", "Failed to update the member.");
//            }
//
//            idField.clear();
//            accountNumberField.clear();
//            firstNameField.clear();
//            middleNameField.clear();
//            lastNameField.clear();
//            addressField.clear();
//            birthdatePicker.setValue(null);
//        });
//
//
//        TableColumn<Member, Integer> idCol = new TableColumn<>("ID");
//        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
//
//        TableColumn<Member, String> accountCol = new TableColumn<>("Account Number");
//        accountCol.setCellValueFactory(new PropertyValueFactory<>("accountNumber"));
//
//        TableColumn<Member, String> firstNameCol = new TableColumn<>("First Name");
//        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
//
//        TableColumn<Member, String> middleNameCol = new TableColumn<>("Middle Name");
//        middleNameCol.setCellValueFactory(new PropertyValueFactory<>("middleName"));
//
//        TableColumn<Member, String> lastNameCol = new TableColumn<>("Last Name");
//        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
//
//        TableColumn<Member, String> addressCol = new TableColumn<>("Address");
//        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
//
//        TableColumn<Member, String> birthdateCol = new TableColumn<>("Birthdate");
//        birthdateCol.setCellValueFactory(new PropertyValueFactory<>("birthdate"));
//
//
//        memberTable.getColumns().addAll(idCol, accountCol, firstNameCol, middleNameCol, lastNameCol, addressCol, birthdateCol);
//        memberTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
//
//
//        Button deleteBtn = new Button("Delete Record");
//        deleteBtn.setOnAction(ex -> {
//            Member selected = memberTable.getSelectionModel().getSelectedItem();
//            if(selected == null) {
//                showAlert("No Selection", "Please select a member to delete.");
//                return;
//            }
//
//            boolean deleted = memberService.deleteMember(selected.getId());
//            if(deleted) {
//                showAlert("Success", "Member deleted successfully!");
//                refreshTableData();
//            } else {
//                showAlert("Failed", "Failed to delete the selected member.");
//            }
//        });
//
//
//        VBox viewLayout = new VBox(10, memberTable, deleteBtn);
//        viewLayout.setPadding(new Insets(20));
//        VBox.setVgrow(memberTable, Priority.ALWAYS);
//
//        TableView<Transaction> transactionTable = new TableView<>();
//
//        TableColumn<Transaction, Double> amountCol = new TableColumn<>("Amount");
//        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
//
//        TableColumn<Transaction, String> typeCol = new TableColumn<>("Type");
//        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
//
//        TableColumn<Transaction, Timestamp> dateCol = new TableColumn<>("Date");
//        dateCol.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
//
//        transactionTable.getColumns().addAll(typeCol, amountCol, dateCol);
//
//        TransactionDao transactionDao = new TransactionDaoImplementation();
//
//        TextField memberAccountNumberField = new TextField();
//        memberAccountNumberField.setPromptText("Enter Account Number");
//
//        Button viewTransactionBtn = new Button("View Transactions");
//
//        viewTransactionBtn.setOnAction(e -> {
//            String accountNumber = memberAccountNumberField.getText().trim();
//            if(accountNumber.isEmpty()) {
//                showAlert("Input Error","Please enter an Account Number.");
//                return;
//            }
//
//            List<Transaction> transactions = transactionDao.getTransactionsForMember(accountNumber);
//            transactionTable.setItems(FXCollections.observableArrayList(transactions));
//        });
//
//        VBox transactionLayout = new VBox(10, memberAccountNumberField, viewTransactionBtn, transactionTable);
//        transactionLayout.setPadding(new Insets(10));
//
//        TextField accountNumField = new TextField();
//        accountNumField.setPromptText("Account Number (for new transaction)");
//
//        TextField amountField = new TextField();
//        amountField.setPromptText("Amount");
//
//        ComboBox<String> typeComboBox = new ComboBox<>();
//        typeComboBox.getItems().addAll("Deposit", "Withdrawal");
//        typeComboBox.setPromptText("Select Transaction Type");
//
//        Label balanceLabel = new Label("Current Balance: ₱0.00");
//
//        Button recordTransactionBtn = new Button("Record Transaction");
//
//        recordTransactionBtn.setOnAction(e -> {
//            String acc = accountNumField.getText().trim();
//            String amtText = amountField.getText().trim();
//            String type = typeComboBox.getValue();
//
//            if(acc.isEmpty() || amtText.isEmpty() || type == null) {
//                showAlert("Input Error", "Please fill all fields to record a transaction");
//                return;
//            }
//            try {
//                double amt = Double.parseDouble(amtText);
//
//                Transaction t = new Transaction();
//                t.setAccountNumber(acc);
//                t.setAmount(amt);
//                t.setType(type);
//                t.setTimestamp(LocalDateTime.now());
//
//
//                if(transactionDao.recordTransaction(t)) {
//                    double balance = transactionDao.getTotalBalance(acc);
//                    balanceLabel.setText("Current Balance: ₱" + balance);
//                    showAlert("Success", "Transaction recorded!");
//
//                    if(memberAccountNumberField.getText().trim().equals(acc)) {
//                        List<Transaction> transactions = transactionDao.getTransactionsForMember(acc);
//                        transactionTable.setItems(FXCollections.observableArrayList(transactions));
//                    }
//
//                    accountNumField.clear();
//                    amountField.clear();
//                    typeComboBox.setValue(null);
//                } else {
//                    showAlert("Error", "Failed to record transaction.");
//                }
//            } catch(NumberFormatException ex) {
//                showAlert("Input Error", "Amount must be a valid number.");
//            }
//        });
//
//        VBox transactionsLayout = new VBox(10,
//                memberAccountNumberField,
//                viewTransactionBtn,
//                transactionTable,
//                new Separator(),
//                accountNumField,
//                amountField,
//                typeComboBox,
//                recordTransactionBtn,
//                balanceLabel);
//
//        transactionsLayout.setPadding(new Insets(10));
//
//        StackPane mainContent = new StackPane();
//        mainContent.getChildren().add(insertLayout);
//
//
//        ViewSelector.setOnAction(e -> {
//            mainContent.getChildren().clear();
//            switch(ViewSelector.getValue()) {
//                case "Insert Member" -> mainContent.getChildren().add(insertLayout);
//                case "Update Member" -> mainContent.getChildren().add(updateLayout);
//                case "View Member/Delete Member" -> {
//                    refreshTableData();
//                    mainContent.getChildren().add(viewLayout);
//                }
//                case "Transaction History" -> {
//                    mainContent.getChildren().add(transactionsLayout);
//                }
//            }
//
//        });
//
//
//
//        HBox topBar = new HBox(20, menuBox);
//        topBar.setPadding(new Insets(20));
//        topBar.setAlignment(Pos.CENTER_LEFT);
//
//        VBox mainLayout = new VBox(20, topBar, mainContent);
//        mainLayout.setPadding(new Insets(40));
//        mainLayout.setAlignment(Pos.TOP_CENTER);
//        mainLayout.setStyle("-fx-background-color: rgba(255,255,255,0); -fx-background-radius: 12;");
//
//        HBox.setHgrow(mainContent, Priority.ALWAYS);
//        VBox.setVgrow(memberTable, Priority.ALWAYS);
//
//
//        TextArea notepad = new TextArea();
//        notepad.setPromptText("Write your notes here...");
//        notepad.setPrefWidth(250);
//        notepad.setWrapText(true);
//
//        VBox notepadBox = new VBox(new Label("Reminder"), notepad);
//        notepadBox.setPadding(new Insets(10));
//        notepadBox.setSpacing(5);
//        notepadBox.setPrefWidth(270);
//
//        HBox mainWithNotepad = new HBox(notepadBox, mainLayout);
//        mainWithNotepad.setSpacing(15);
//        mainWithNotepad.setPadding(new Insets(20));
//        HBox.setHgrow(mainLayout, Priority.ALWAYS);
//
//        rootLayout.getChildren().add(mainWithNotepad);
//
//        Scene scene = new Scene(rootLayout);
//
////        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
//
//
//        stage.setTitle("Cooperative Management System");
//        stage.setScene(scene);
//        stage.setMaximized(false);
//        stage.show();
//
//    }
//
//    private void showAlert(String title, String message) {
//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setTitle(title);
//        alert.setHeaderText(null);
//        alert.setContentText(message);
//        alert.showAndWait();
//    }
//
//    private void refreshTableData() {
//        List<Member> allMembers = memberService.getAllMembers();
//        //ObservableList<Member> observableMembers = FXCollections.observableArrayList(allMembers);
//        memberTable.setItems(FXCollections.observableArrayList(allMembers));
//    }
//
//    private int calculateAge(LocalDate birthDate, LocalDate currentDate) {
//        if((birthDate != null) && (currentDate != null)) {
//            return currentDate.getYear() - birthDate.getYear() - ((currentDate.getDayOfYear() < birthDate.getDayOfYear()) ? 1 : 0);
//        } else {
//            return 0;
//        }
//    }
//
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//
//
//}
//
////import java.sql.Connection;
////import java.sql.DriverManager;
////import java.sql.PreparedStatement;
////import java.sql.ResultSet;
////import java.sql.Statement;
////
////public class Main {
////    private static final String URL = "jdbc:mysql://localhost:3306/cooperative_system";
////
////    private static final String USERNAME = "root";
////
////    private static final String PASSWORD = "";
////
////    public static void main(String[] args) {
////
////
////
////        Member member = Member.builder()
////                .id(7)
////                .accountNumber("AAA-01")
////                .firstName("Debbie Jane")
////                .middleName("Lee")
////                .lastName("Abrea")
////                .address("Hagonoy, Bulacan")
////                .birthdate("1998-09-20")
////                .build();
////
////        updateMember(member);
////
////    }
////
////    public static void deleteMember(int id) {
////        String query = "DELETE FROM member WHERE id = ?";
////
////
////        try(Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
////            PreparedStatement preparedStatement = connection.prepareStatement(query);) {
////
////
////            preparedStatement.setInt(1, id);
////
////            int numberOfRowsDeleted = preparedStatement.executeUpdate();
////
////            if(numberOfRowsDeleted > 0) {
////                System.out.println("Successfully deleted!");
////            } else {
////                System.out.println("Deleting failed!");
////            }
////
////        } catch(Exception e) {
////            e.printStackTrace();
////        }
////    }
////
////    public static void updateMember(Member member) {
////        String query = "UPDATE member SET account_number = ?, first_name = ?, middle_name = ?, last_name = ?, address = ?, birthdate = ? WHERE id = ?";
////
////
////        try(Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
////            PreparedStatement preparedStatement = connection.prepareStatement(query);) {
////
////            preparedStatement.setString(1, member.getAccountNumber());
////            preparedStatement.setString(2, member.getFirstName());
////            preparedStatement.setString(3, member.getMiddleName());
////            preparedStatement.setString(4, member.getLastName());
////            preparedStatement.setString(5, member.getAddress());
////            preparedStatement.setDate(6, java.sql.Date.valueOf(member.getBirthdate()));
////            preparedStatement.setInt(7, member.getId());
////
////            int numberOfRowsUpdated = preparedStatement.executeUpdate();
////
////            if(numberOfRowsUpdated > 0) {
////                System.out.println("Successfully updated!");
////            } else {
////                System.out.println("Updating failed!");
////            }
////
////        } catch(Exception e) {
////            e.printStackTrace();
////        }
////    }
////
////    public static void insertMember(Member member) {
////        String query = "INSERT INTO member(account_number, first_name, middle_name, last_name, address, birthdate) VALUES(?, ?, ?, ?, ?)";
////
////
////        try(Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
////            PreparedStatement preparedStatement = connection.prepareStatement(query);) {
////
////            preparedStatement.setString(1, member.getAccountNumber());
////            preparedStatement.setString(2, member.getFirstName());
////            preparedStatement.setString(3, member.getMiddleName());
////            preparedStatement.setString(4, member.getLastName());
////            preparedStatement.setString(5, member.getAddress());
////            preparedStatement.setDate(6, java.sql.Date.valueOf(member.getBirthdate()));
////
////            int numberOfRowsInserted = preparedStatement.executeUpdate();
////
////            if(numberOfRowsInserted > 0) {
////                System.out.println("Successfully inserted!");
////            } else {
////                System.out.println("Inserting failed!");
////            }
////
////        } catch(Exception e) {
////            e.printStackTrace();
////        }
////    }
////
////    public static void fetchSpecificMember(int id) {
////        String query = "SELECT * FROM member WHERE id = ?";
////
////
////        try(Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
////            PreparedStatement preparedStatement = connection.prepareStatement(query);) {
////
////            preparedStatement.setInt(1, id);
////
////            ResultSet resultSet = preparedStatement.executeQuery();
////
////            while(resultSet.next()) {
////                System.out.println("ID: " + resultSet.getInt("id"));
////                System.out.println("Account Number: " + resultSet.getString("account_number"));
////                System.out.println("First name: " + resultSet.getString("first_name"));
////                System.out.println("Middle name: " + resultSet.getString("middle_name"));
////                System.out.println("Last name: " + resultSet.getString("last_name"));
////                System.out.println("Address: " + resultSet.getString("address"));
////                System.out.println("Birthdate: " + resultSet.getDate("birthdate").toString());
////                System.out.println("*******************");
////            }
////
////        } catch(Exception e) {
////            e.printStackTrace();
////        }
////    }
////
////
////    public static void fetchMembers() {
////        String query = "SELECT * FROM member";
////
////
////        try(Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
////            Statement statement = connection.createStatement();) {
////
////
////
////            ResultSet resultSet = statement.executeQuery(query);
////
////            while(resultSet.next()) {
////                System.out.println("ID: " + resultSet.getInt("id"));
////                System.out.println("Account Number: " + resultSet.getString("account_number"));
////                System.out.println("First name: " + resultSet.getString("first_name"));
////                System.out.println("Middle name: " + resultSet.getString("middle_name"));
////                System.out.println("Last name: " + resultSet.getString("last_name"));
////                System.out.println("Address: " + resultSet.getString("address"));
////                System.out.println("Birthdate: " + resultSet.getDate("birthdate").toString());
////                System.out.println("*******************");
////            }
////
////        } catch(Exception e) {
////            e.printStackTrace();
////        }
////    }
////}
