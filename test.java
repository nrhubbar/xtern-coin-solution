import java.io.*;
import java.util.Random;
import java.util.Scanner;
import java.util.UUID;
/* Implementation Notes:
* I implemented this using a static treasury that handles what the current key is, as well as everyone's current balance.
* The way I implemented that balance is making a simple key/value file system data base. Meaning given a user's Id we can -
*   find thier balance file and report thier balance as well as change it. It is not the most secure because any on on the
*   change the value of thier balance File.
* If given more time I would have implemented this using a simple REST architecture with Node.js and MongoDB.
*/
/////////////////////
/* User Class
* Properties :
*   String userId
* Methods :
*   int generateGuess()
*   int getBalance()
*   String generateId();
*   void StartGuessing();
*/
class User {
    public String userId;

    public User(){
        this.userId = this.generateId();
    }

    public int getBalance(){
        return Treasury.GetCoins(this.userId);
    }

    private String generateId(){
        return UUID.randomUUID().toString();
    }

    //if the user already has an id, the can be created then this can be set.
    public void setId(String userId){
        this.userId = userId;
    }
}


/*
* Tresurey Class - handles the random number, incoming guesses, and each users balance.
* Properties
*   private int key
* Methods
*   private void generateKey();
*   public boolean HandleGuess(String userId, int Guess)
*   public int GetCoins(String userId)
*/
class Treasury {
    private static int key;

    /*
    * Attempts to read from, 'bank/<userId>.bal' which holds that users balance.
    *   if that file does not exist, we create the file and save it with a 0.
    */
    public static int GetCoins(String userId){
        String path = userId + ".bal";
        try {
            FileReader userBalance = new FileReader(new File(path));
            Scanner scanner = new Scanner(userBalance);
            return scanner.nextInt();
        }
        catch (FileNotFoundException e){
                System.out.println("Could not find balance with that id: " + userId +". Generating one now.");
                try {
                    new File(path).createNewFile();
                    PrintWriter newUserBalance = new PrintWriter(new FileWriter(path));
                    newUserBalance.println("0");
                }
                catch (IOException e1){
                    System.out.println("There was an Error Creating: " + path);
                    System.out.println(e1.getStackTrace());
                }
            } catch (IOException e2){
                System.out.println("There was an Error with " + path);
                System.out.println(e2.getStackTrace());
            }
            return 0;
        }


    /*
    * Checks to see if key is not null, if key is null we generate a new one
    * Checks to see if guess is equal to key. if yes then:
    *   We Generate new Key; increment User Balance; and return true
    * If their guess is incorrect we return false
    */
    public static boolean HandleGuess(String userId, int guess) {
        if(null == (Integer)Treasury.key){
            Treasury.generateKey();
        }
        String path = userId + ".bal";

        if(guess == Treasury.key){
            System.out.println("Success");
            Treasury.generateKey();
            int currentBalance = Treasury.GetCoins(userId);

            try {
                PrintWriter newUserBalance = new PrintWriter(new FileWriter(path));
                newUserBalance.println(new Integer(currentBalance + 1).toString());
            }
            catch (IOException e){
                System.out.println("There was an Error with " + path);
                System.out.println(e.getStackTrace());
            }

            return true;
        }
        else {
            System.out.println("Not The Key");
            return false;
        }
    }

    private static void generateKey() {
        Random rand = new Random();
        key = rand.nextInt(10);
    }
}


/*
* Main Class - Handles user interaction.
*/
class Main {
    static User currentUser;
    public static void main(String[] args){
        Main.currentUser = new User();
        menu();
    }

    public static void menu(){
        boolean keepGoing = true;
        Random rand = new Random();
        int selection;
        //event loop
        while(keepGoing) {
            System.out.println("1) Returning User? \n 2) New User \n 3) Generate Guess \n 4) View Balance \n 5) Quit");
            Scanner scanner = new Scanner(System.in);
            selection = scanner.nextInt();
            if(selection == 1){
                System.out.println("What is your user id?");
                Main.currentUser.setId(scanner.nextLine());
            } else if (selection == 2){
                System.out.println("Welcome"+ Main.currentUser.userId +"! please Remember your userId");
            } else if(selection == 3){
                Treasury.HandleGuess(Main.currentUser.userId, rand.nextInt(10));
            } else if(selection == 4){
                System.out.println("Your Balance is: " + Main.currentUser.getBalance());
            } else if(selection == 5){
                System.out.println("Thank you for taking part in our economy");
                keepGoing = false;
            } else {
                System.out.println("Invalid Input, please try again.");
            }
        }
    }
}