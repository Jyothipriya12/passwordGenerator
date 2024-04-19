import java.util.Scanner;

public class PasswordProgram {

    public static final Scanner keyboard = new Scanner(System.in);

    public static void main(String[] args) {
        Generator generator = new Generator(keyboard);
        generator.mainLoop();
        keyboard.close();
    }
}

// Class representing the alphabet
class Alphabet {
    // Constants for different character types
    public static final String UPPERCASE_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String LOWERCASE_LETTERS = "abcdefghijklmnopqrstuvwxyz";
    public static final String NUMBERS = "1234567890";
    public static final String SYMBOLS = "!@#$%^&*()-_=+\\/~?";

    // StringBuilder to store the character pool
    private final StringBuilder pool;

    // Constructor to initialize the alphabet based on user preferences
    public Alphabet(boolean uppercaseIncluded, boolean lowercaseIncluded, boolean numbersIncluded, boolean specialCharactersIncluded) {
        pool = new StringBuilder();

        if (uppercaseIncluded) pool.append(UPPERCASE_LETTERS);
        if (lowercaseIncluded) pool.append(LOWERCASE_LETTERS);
        if (numbersIncluded) pool.append(NUMBERS);
        if (specialCharactersIncluded) pool.append(SYMBOLS);
    }

    // Method to get the alphabet as a string
    public String getAlphabet() {
        return pool.toString();
    }
}

// Class representing a password
class Password {
    String value;
    int length;

    // Constructor to initialize the password
    public Password(String s) {
        value = s;
        length = s.length();
    }

    // Method to determine the character type
    public int charType(char c) {
        int val;

        if ((int) c >= 65 && (int) c <= 90)
            val = 1;
        else if ((int) c >= 97 && (int) c <= 122) {
            val = 2;
        } else if ((int) c >= 60 && (int) c <= 71) {
            val = 3;
        } else {
            val = 4;
        }

        return val;
    }

    // Method to calculate the password strength
    public int passwordStrength() {
        String s = this.value;
        boolean usedUpper = false;
        boolean usedLower = false;
        boolean usedNum = false;
        boolean usedSym = false;
        int type;
        int score = 0;

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            type = charType(c);

            if (type == 1) usedUpper = true;
            if (type == 2) usedLower = true;
            if (type == 3) usedNum = true;
            if (type == 4) usedSym = true;
        }

        if (usedUpper) score += 1;
        if (usedLower) score += 1;
        if (usedNum) score += 1;
        if (usedSym) score += 1;

        if (s.length() >= 8) score += 1;
        if (s.length() >= 16) score += 1;

        return score;
    }

    // Method to categorize password strength
    public String calculateScore() {
        int score = this.passwordStrength();

        if (score == 6) {
            return "This is a very good password :D check the Useful Information section to make sure it satisfies the guidelines";
        } else if (score >= 4) {
            return "This is a good password :) but you can still do better";
        } else if (score >= 3) {
            return "This is a medium password :/ try making it better";
        } else {
            return "This is a weak password :( definitely find a new one";
        }
    }

    // Override toString() method
    @Override
    public String toString() {
        return value;
    }
}

// Class representing the password generator
class Generator {
    Alphabet alphabet;
    public static Scanner keyboard;

    // Constructor to initialize the generator with user input
    public Generator(Scanner scanner) {
        keyboard = scanner;
    }

    // Constructor to initialize the generator with alphabet settings
    public Generator(boolean IncludeUpper, boolean IncludeLower, boolean IncludeNum, boolean IncludeSym) {
        alphabet = new Alphabet(IncludeUpper, IncludeLower, IncludeNum, IncludeSym);
    }

    // Method to run the main loop of the generator
    public void mainLoop() {
        System.out.println("Welcome to Ziz Password Services :)");
        printMenu();

        String userOption = "-1";

        while (!userOption.equals("4")) {

            userOption = keyboard.next();

            switch (userOption) {
                case "1" -> {
                    requestPassword();
                    printMenu();
                }
                case "2" -> {
                    checkPassword();
                    printMenu();
                }
                case "3" -> {
                    printUsefulInfo();
                    printMenu();
                }
                case "4" -> printQuitMessage();
                default -> {
                    System.out.println();
                    System.out.println("Kindly select one of the available commands");
                    printMenu();
                }
            }
        }
    }

    // Method to generate a password
    private Password GeneratePassword(int length) {
        final StringBuilder pass = new StringBuilder("");

        final int alphabetLength = alphabet.getAlphabet().length();

        int max = alphabetLength - 1;
        int min = 0;
        int range = max - min + 1;

        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * range) + min;
            pass.append(alphabet.getAlphabet().charAt(index));
        }

        return new Password(pass.toString());
    }

    // Method to print useful information about password creation
    private void printUsefulInfo() {
        System.out.println();
        System.out.println("Use a minimum password length of 8 or more characters if permitted");
        System.out.println("Include lowercase and uppercase alphabetic characters, numbers and symbols if permitted");
        System.out.println("Generate passwords randomly where feasible");
        System.out.println("Avoid using the same password twice (e.g., across multiple user accounts and/or software systems)");
        System.out.println("Avoid character repetition, keyboard patterns, dictionary words, letter or number sequences," +
                "\nusernames, relative or pet names, romantic links (current or past) " +
                "and biographical information (e.g., ID numbers, ancestors' names or dates).");
        System.out.println("Avoid using information that the user's colleagues and/or " +
                "acquaintances might know to be associated with the user");
        System.out.println("Do not use passwords which consist wholly of any simple combination of the aforementioned weak components");
    }

    // Method to request password generation parameters from the user
    private void requestPassword() {
        boolean IncludeUpper = false;
        boolean IncludeLower = false;
        boolean IncludeNum = false;
        boolean IncludeSym = false;

        boolean correctParams;

        System.out.println();
        System.out.println("Hello, welcome to the Password Generator :) answer"
                + " the following questions by Yes or No \n");

        do {
            String input;
            correctParams = false;

            do {
                System.out.println("Do you want Lowercase letters \"abcd...\" to be used? ");
                input = keyboard.next();
                PasswordRequestError(input);
            } while (!input.equalsIgnoreCase("yes") && !input.equalsIgnoreCase("no"));

            if (isInclude(input)) IncludeLower = true;

            do {
                System.out.println("Do you want Uppercase letters \"ABCD...\" to be used? ");
                input = keyboard.next();
                PasswordRequestError(input);
            } while (!input.equalsIgnoreCase("yes") && !input.equalsIgnoreCase("no"));

            if (isInclude(input)) IncludeUpper = true;

            do {
                System.out.println("Do you want Numbers \"1234...\" to be used? ");
                input = keyboard.next();
                PasswordRequestError(input);
            } while (!input.equalsIgnoreCase("yes") && !input.equalsIgnoreCase("no"));

            if (isInclude(input)) IncludeNum = true;

            do {
                System.out.println("Do you want Symbols \"!@#$...\" to be used? ");
                input = keyboard.next();
                PasswordRequestError(input);
            } while (!input.equalsIgnoreCase("yes") && !input.equalsIgnoreCase("no"));

            if (isInclude(input)) IncludeSym = true;

            // No Pool Selected
            if (!IncludeUpper && !IncludeLower && !IncludeNum && !IncludeSym) {
                System.out.println("You have selected no characters to generate your " +
                        "password, at least one of your answers should be Yes\n");
                correctParams = true;
            }

        } while (correctParams);

        System.out.println("Great! Now enter the length of the password");
        int length = keyboard.nextInt();

        final Generator generator = new Generator(IncludeUpper, IncludeLower, IncludeNum, IncludeSym);
        final Password password = generator.GeneratePassword(length);

        System.err.println("Your generated password -> " + password);
    }

    // Method to check if input is "Yes"
    private boolean isInclude(String Input) {
        return Input.equalsIgnoreCase("yes");
    }

    // Method to handle errors in password requests
    private void PasswordRequestError(String i) {
        if (!i.equalsIgnoreCase("yes") && !i.equalsIgnoreCase("no")) {
            System.out.println("You have entered something incorrect let's go over it again \n");
        }
    }

    // Method to check password strength
    private void checkPassword() {
        String input;

        System.out.print("\nEnter your password:");
        input = keyboard.next();

        final Password p = new Password(input);

        System.out.println(p.calculateScore());
    }

    // Method to print the menu
    private void printMenu() {
        System.out.println();
        System.out.println("Enter 1 - Password Generator");
        System.out.println("Enter 2 - Password Strength Check");
        System.out.println("Enter 3 - Useful Information");
        System.out.println("Enter 4 - Quit");
        System.out.print("Choice:");
    }

    // Method to print quit message
    private void printQuitMessage() {
        System.out.println("Closing the program bye bye!");
    }
}
