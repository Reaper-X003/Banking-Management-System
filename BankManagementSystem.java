package java_project;

import java.util.*;

interface PassWordAuthentication {
    boolean authenticate(String password);
}

class BankAccount {
    String accountHolderName, accountType;
    int accountNumber, accountBalance;

    BankAccount(String name, int number, int balance, String type) {
        accountHolderName = name;
        accountNumber = number;
        accountBalance = balance;
        accountType = type;
    }
}

class AccountManager extends BankAccount implements PassWordAuthentication {
    private final String password;
    private String city;
    private String nid;

    AccountManager(String name, String type, String password, String city, String nid, int initialBalance) {
        super(name, generateAccountNumber(), initialBalance, type);
        this.password = password;
        this.nid = nid;
        this.city = city;
    }

    void displayAccountDetails() {
        System.out.println("Account Holder Name: " + accountHolderName);
        System.out.println("Account Number: " + accountNumber);
        System.out.println("Account Balance: " + accountBalance);
        System.out.println("Account Type: " + accountType);
        System.out.println("City: " + city);
        System.out.println("NID: " + nid);
    }

    void deposit(int amount) {
        accountBalance += amount;
    }

    int withdraw(int amount) {
        if (amount > accountBalance || (accountBalance-amount) < 1000) {
            System.out.println("\t\t\t\t\t\t\"INSUFFICIENT BALANCE. WITHDRAWAL CANCELED\"");
        }
        if (amount <= accountBalance) {
            accountBalance -= amount;
        }
        return accountBalance;
    }

    @Override
    public boolean authenticate(String inputPassword) {
        return this.password.equals(inputPassword);
    }

    // Function used for Unique ID creation
    private static int generateAccountNumber() {
        return (int) ((Math.random() * 9000) + 1000);
    }
}

public class BankManagementSystem {
    public static void main(String[] args) {
        List<AccountManager> accounts = new ArrayList<>();

        Scanner scanner = new Scanner(System.in);
        int userChoice;
        boolean quit = false;

        do {
            System.out.println("1. Create Account");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. Check Balance");
            System.out.println("5. Transfer Balance");
            System.out.println("6. Display Account Details");
            System.out.println("0. Quit\n");
            System.out.print("Enter Your Choice: ");
            userChoice = scanner.nextInt();

            switch (userChoice) {
                case 1: { // to take infos from users
                    System.out.print("Enter Account Holder's Name: ");
                    String accountHolderName;

                    // try-catch block for handling non-string input for Account Holder's Name
                    try {
                        accountHolderName = scanner.next();
                        // Check if the entered name contains only letters (no numbers)
                        if (!accountHolderName.matches("[a-zA-Z]+")) {
                            throw new InputMismatchException("Account Holder's Name should be Only String Format.");
                        }

                        System.out.print("Enter Account Type: ");
                        String accountType = scanner.next();

                        System.out.print("Set Password: ");
                        String password = scanner.next();

                        System.out.print("Enter City: ");
                        String city = scanner.next();

                        System.out.print("Enter NID: ");
                        String nid = scanner.next();

                        System.out.print("Set Initial Balance: ");
                        int initialBalance = scanner.nextInt();

                        AccountManager userAccount = new AccountManager(accountHolderName, accountType, password, city, nid, initialBalance);
                        accounts.add(userAccount);
                        System.out.println("\n\tAccount Details\n\tAccount Number: " + userAccount.accountNumber + "\n");
                        userAccount.displayAccountDetails();
                    } catch (InputMismatchException e) {
                        System.out.println("Error: " + e.getMessage());
                        scanner.nextLine();
                    }
                    break;
                }
                case 2: { //deposit part
                    if (accounts.isEmpty()) {
                        System.out.println("No accounts created yet. Please create an account first.");
                        break;
                    }
                    System.out.print("Enter Account Number: ");
                    int accountNumberDeposit = scanner.nextInt();
                    AccountManager depositAccount = findAccount(accounts, accountNumberDeposit);
                    if (depositAccount == null) {
                        System.out.println("Account not found.");
                        break;
                    }
                    System.out.print("Enter Password: ");
                    String inputPassword = scanner.next();
                    if (depositAccount.authenticate(inputPassword)) {
                        System.out.print("Enter Amount To Deposit: ");
                        int depositAmount = scanner.nextInt();
                        depositAccount.deposit(depositAmount);
                        System.out.println("Deposit Successful!!");
                    } else {
                        System.out.println("Wrong Password.");
                    }
                    break;
                }
                case 3: {//withdrawn
                    if (accounts.isEmpty()) {
                        System.out.println("No accounts created yet. Please create an account first.");
                        break;
                    }
                    System.out.print("Enter Account Number: ");
                    int accountNumberWithdraw = scanner.nextInt();
                    AccountManager withdrawAccount = findAccount(accounts, accountNumberWithdraw);
                    if (withdrawAccount == null) {
                        System.out.println("Account not found.");
                        break;
                    }
                    System.out.print("Enter Password: ");
                    String inputPasswordWithdraw = scanner.next();
                    if (withdrawAccount.authenticate(inputPasswordWithdraw)) {
                        System.out.print("Enter Amount Of Money to Withdraw: ");
                        int withdrawAmount = scanner.nextInt();
                        int cb = withdrawAccount.withdraw(withdrawAmount);
                    } else {
                        System.out.println("Wrong Password.");
                    }
                    break;
                }

                case 4: {//check balance
                    if (accounts.isEmpty()) {
                        System.out.println("No accounts created yet. Please create an account first.");
                        break;
                    }
                    System.out.print("Enter Account Number: ");
                    int accountNumberCheck = scanner.nextInt();
                    AccountManager checkAccount = findAccount(accounts, accountNumberCheck);
                    if (checkAccount == null) {
                        System.out.println("Account not found.");
                        break;
                    }
                    System.out.println("Your Current Balance: " + checkAccount.accountBalance);
                    break;
                }
                case 5: {//money transfer
                    System.out.println("Transfer Money");
                    if (accounts.size() < 2) {
                        System.out.println("Not enough accounts to perform a transfer. Please create at least two accounts.");
                        break;
                    }

                    System.out.print("Enter Account Number to Transfer From: ");
                    int fromAccountNumber = scanner.nextInt();
                    AccountManager fromAccount = findAccount(accounts, fromAccountNumber);

                    if (fromAccount == null) {
                        System.out.println("Source Account not found.");
                        break;
                    }

                    System.out.print("Enter Password for Source Account: ");
                    String sourcePassword = scanner.next();

                    if (fromAccount.authenticate(sourcePassword)) {
                        System.out.print("Enter Account Number to Transfer To: ");
                        int toAccountNumber = scanner.nextInt();
                        AccountManager toAccount = findAccount(accounts, toAccountNumber);

                        if (toAccount == null) {
                            System.out.println("Destination Account not found.");
                            break;
                        }

                        System.out.print("Enter Amount To Transfer: ");
                        int transferAmount = scanner.nextInt();

                        if (transferAmount <= fromAccount.accountBalance) {
                            fromAccount.withdraw(transferAmount);
                            toAccount.deposit(transferAmount);
                            System.out.println("Transfer Successful!");
                        } else {
                            System.out.println("Insufficient balance for the transfer.");
                        }
                    } else {
                        System.out.println("Wrong Password for Source Account.");
                    }
                    break;
                }
                case 6: {// Account Details
                    if (accounts.isEmpty()) {
                        System.out.println("No accounts created yet. Please create an account first.");
                        break;
                    }
                    System.out.print("Enter Account Number: ");
                    int accountNumberDisplay = scanner.nextInt();
                    AccountManager displayAccount = findAccount(accounts, accountNumberDisplay);
                    if (displayAccount == null) {
                        System.out.println("Account not found.");
                        break;
                    }
                    displayAccount.displayAccountDetails();
                    break;
                }




                case 0:
                    quit = true;
                default:
                    System.out.println("Invalid Choice.");
                    break;

            }
            System.out.println("\n");
        } while (!quit);
        System.out.println("Thank you for using our Bank!");
    }

    private static AccountManager findAccount(List<AccountManager> accounts, int accountNumber) {
        for (AccountManager account : accounts) {
            if (account.accountNumber == accountNumber) {
                return account;
            }
        }
        return null;
    }
}