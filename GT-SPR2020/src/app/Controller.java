//=============================================================================
//PROGRAMMER1: Anthony Rufin
// PANTHER ID1: 6227314
// CLASS: CAP5507
//
//PROGRAMMER2: Your name
// PANTHER ID2: Your panther ID
// CLASS: Your class: example CAP5706
//
// SEMESTER: Spring 2026
// CLASSTIME: T 4:30PM, Online
//
// Project: This project allows the user to either input payoffs for a game, or allow the program to generate the payoffs for a game. It then takes this data and prints the normal form, nash equilibria, strategies and payoffs, best responses, and indifference mix probabilities.
// DUE: Friday, February 20, 2026
//
// CERTIFICATION: I certify that this work is my own and that
// none of it is the work of any other person.
//=============================================================================
package app;
import java.util.*;

public class Controller {
    static Scanner sc = new Scanner(System.in); //Scanner for user input
    static Random rand = new Random(); //Value to be used to generate random values.
    static boolean hasPure;
    static int rows, cols; //The rows and columns of the table
    static int[][] player1;//Holds the payoffs for player 1 
    static int[][] player2;//Holds the payoffs for player 2
    
    public static void main(String[] args) {
        System.out.printf("Enter (R)andom or (M)anual payoffs enteries:\n");
        String mode = sc.next().trim().toUpperCase();
        
        System.out.print("Enter the number of rows: ");
        rows = sc.nextInt();
        
        System.out.print("Enter the number of cols: ");
        cols = sc.nextInt();
        if (rows < 1 || rows > 9 || cols < 1 || cols > 9) {//Checks to make sure the dimensions of the game are within 1-9.
            System.out.println("Error: Dimensions must be between 1 and 9");
            return;
        }
        
        player1 = new int[rows][cols];
        player2 = new int[rows][cols];
        
        if(mode.equals("R")){
            /*
            Contains: random generation of values, calculation of Strategy spaces and payoffs, 
            the normal form for the game, the nash equlibria and the Best Responses & Expected Payoffs, along with a function
            for calculating the indifference probabilities if the table is 2x2.
            */
            randomMode();
            displayStrategySpaces();
            printNormalForm();
            nashEquilibrium();
            printBeliefs();
            if(rows == 2 && cols == 2){
                if(!hasPure){
                    indifference();
                }
            }
        }else{
            /*
            Contains: Manual user input for payoffs, the normal form for the game, the nash equlibria, and the indifference probabilities
            if the table is 2x2            
            */
            manualMode();
            printNormalForm();
            nashEquilibrium();
            if(rows == 2 && cols == 2){
                if(!hasPure){
                    indifference();
                }
            }
        }
        System.out.println();

        
    }
    /*
        The randomMode method handles the generation of payoffs for the game, with the number of values determined by the rows and cols set in the main
        method. The Random class is used here to generate a value between 199 and 99 (shown here as 199-99)
    */
    public static void randomMode(){
        for (int i = 0; i < rows; i++){
            for (int j = 0; j < cols; j++) {
                player1[i][j] = rand.nextInt(199) - 99;
                player2[i][j] = rand.nextInt(199) - 99;
            }
        }
    }
    /*
        The manualMode method allows the user to manually insert the payoffs for the game. 
    */
    public static void manualMode(){
        System.out.println("\nManual Entries");
        
        sc.nextLine();
        for (int i = 0; i < rows; i++){
            for (int j = 0; j < cols; j++) {
                    try{
                        System.out.printf("Enter payoff for (A%d, B%d)", i+1, j+1);
                        String input = sc.nextLine().trim(); 
                        String[] payoffs = input.split(","); //This parses and puts the inputs into an array

                        player1[i][j] = Integer.parseInt(payoffs[0].trim());
                        player2[i][j] = Integer.parseInt(payoffs[1].trim());
                    }catch(Exception E){ //A try catch is used here for error handling, since it is possible for the user to input an invalid character.
                        System.out.println("Error, Format must be: x,y");
                        return;
                    }
                
            }
            System.out.println("---------------------------");
        }
    }

    
    /*
        The displayStrategySpaces method generates the Strategies and the payoffs for random mode generation.  
    */
    public static void displayStrategySpaces(){
        System.out.println("----------------------------------------------");
        System.out.println("Player 1's Strategies");
        System.out.println("----------------------------------------------");
        /*
            This for loop is to print the list of strategies that Player 1 has. 
        */
        System.out.print("{");
        for (int i = 0; i < rows; i++){
            System.out.printf("A%d", i+1);
            if (i < rows - 1) {
                System.out.print(", ");
            }
        }
        System.out.print("}\n");
        System.out.println();
        System.out.println("----------------------------------------------");
        System.out.println("Player 1's Payoffs");
        System.out.println("----------------------------------------------");
        /*
            This for loop will print the payoffs based on the generated values from player1.  
        */
        for(int i = 0; i < rows; i++){
            for (int j = 0; j < cols; j++){
                System.out.printf("%4d", player1[i][j]);
                    if (j < cols - 1) System.out.print(", ");
            }
            System.out.println();
        }
        System.out.println();
        System.out.println("----------------------------------------------");
        System.out.println("Player 2's Strategies");
        System.out.println("----------------------------------------------");
        
        /*
            Like before, the next two for loops do the same as the for loops above, only this time, it is for player2's strategies and payoffs.
        */
        System.out.print("{");
        for (int i = 0; i < cols; i++){
            System.out.printf("B%d", i+1);
            if (i < cols - 1) {
                System.out.print(", ");
            }
            
        }
        System.out.print("}\n");
        System.out.println();
        System.out.println("----------------------------------------------");
        System.out.println("Player 2's Payoffs");
        System.out.println("----------------------------------------------");
         for(int i = 0; i < rows; i++){
            for (int j = 0; j < cols; j++){
                System.out.printf("%4d", player2[i][j]);
                    if (j < cols - 1) System.out.print(", ");
            }
            System.out.println();
            }
        System.out.println();
    }
    /*
        The printNormalForm method displays the normal form of the game. After having generated or recieved inputted values, the printNormalForm
        method will print each value out in a table. 
    */
    public static void printNormalForm(){
        System.out.println("=======================================");
        System.out.println("Display Normal Form");
        System.out.println("=======================================");
        System.out.print("\t");
        for (int j = 1; j <= cols; j++){
            System.out.printf("B%-8d", j);
        }
        System.out.println();
        for (int i = 0; i < rows; i++){
            System.out.printf("A%d |", i+1);
            for (int j = 0; j < cols; j++){
                System.out.printf("(%3d,%3d)",player1[i][j], player2[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }
    /*
        The calculateBestPlayer1 method returns a boolean array containing the rows and columns where there is a maximum payoff for player 1. 
    */
    public static boolean[][] calculateBestResponsePlayer1(){
        boolean[][] bestRespPlayer1 = new boolean[rows][cols];
        
        for(int j = 0; j < cols; j++){
            int maxPayoff = Integer.MIN_VALUE; //maxPayoff is set to min here, and is repeatedly compared to each value in player1 to find the maximum payoff of each column. 
            for (int i = 0; i < rows; i++){
                maxPayoff = Math.max(maxPayoff, player1[i][j]);
            }
            
            for (int i = 0; i < rows; i++){
                if(player1[i][j] == maxPayoff){
                    bestRespPlayer1[i][j] = true;
                }
            }
        }
        return bestRespPlayer1;
    }
    /*
        The calculateBestPlayer2 method returns a boolean array containing the rows and columns where there is a maximum payoff for player 2. 
    */
    public static boolean[][] calculateBestResponsePlayer2(){
        boolean[][] bestRespPlayer2 = new boolean[rows][cols];
        
        for(int i = 0; i < rows; i++){
            int maxPayoff = Integer.MIN_VALUE;//maxPayoff is set to min here, and is repeatedly compared to each value in player2 to find the maximum payoff of each row.
            for (int j = 0; j < cols; j++){
                maxPayoff = Math.max(maxPayoff, player2[i][j]);
            }
            
             for (int j = 0; j < cols; j++){
                if(player2[i][j] == maxPayoff){
                    bestRespPlayer2[i][j] = true;
                }
            }
        }
        return bestRespPlayer2;
    }
    /*
        The nashEquilibrium method prints the Nash Equlibria locations within the game. This uses the calculateBestResponsePlayer1
        and calculateBestResponsePlayer2 methods to find the locations where the best responses were found.
    */
    public static void nashEquilibrium(){
        boolean[][] bestRespPlayer1 = calculateBestResponsePlayer1();
        boolean[][] bestRespPlayer2 = calculateBestResponsePlayer2(); 
        List<String> nashEqui = new ArrayList<>();
        
        System.out.println("=======================================");
        System.out.println("Nash Equilibrium Locations");
        System.out.println("=======================================");
        System.out.print("\t");
        for (int j = 1; j <= cols; j++){
            System.out.printf("B%-8d", j);
        }
        System.out.println();
        /*
            Here, the loop will print each value in both arrays, using a conditional operator to check whether to print the value as normal, or to 
            print an "H", denoting the value is a Best Response from that player. 
        */
        for (int i = 0; i < rows; i++){
            System.out.printf("A%d |", i+1);
            for (int j = 0; j < cols; j++){
                String player1Best = bestRespPlayer1[i][j] ? "H": String.valueOf(player1[i][j]);
                String player2Best = bestRespPlayer2[i][j] ? "H": String.valueOf(player2[i][j]);
                
                if (bestRespPlayer1[i][j] && bestRespPlayer2[i][j]){
                    nashEqui.add("(A"+ (i+1) +", B" + (j+1) + ")");
                    
                }
                System.out.printf("(%3s,%3s)",player1Best, player2Best);
            }
            System.out.println();
        }
        System.out.println();
        if (nashEqui.isEmpty()){
            /*
                If no nash equilibrium was found, it will print a statement denoting that none was found. Otherwise, the program
                will print out each value to the console. 
            */
            System.out.println("No Pure Nash Equilibrium found.");
            hasPure = false;
        }else{
            System.out.printf("Nash Pure Equilibrium(s): ");
            nashEqui.forEach(value -> System.out.print(value + " "));
            hasPure = true;
        }
        System.out.println();
    }
    /*
        The printBeliefs method prints the expected payoffs and best responses when mixing. This uses the generateBeliefs method to create 
        random beliefs for each player, along with the formatBeliefs method to print out a table of expected payoffs and beliefs. 
    */
    public static void printBeliefs(){
        double[] beliefsPlayer2 = generateBeliefs(cols);
        double[] beliefsPlayer1 = generateBeliefs(rows);
        double[] expectedPlayoffsPlayer1 = new double[rows];
        double[] expectedPlayoffsPlayer2 = new double[cols];
        double sum = 0;
        System.out.println("\n----------------------------------------------");
        System.out.println("Player 1 Expected Payoffs with Player 2 Mixing");
        System.out.println("----------------------------------------------");
        //Multipies player 1's payoffs at row i by the belief at j to find the expected payoff 
        for (int i = 0; i < rows; i++){
            sum = 0;
            for (int j = 0; j < cols; j++){
                sum += player1[i][j] * beliefsPlayer2[j];
            }
            expectedPlayoffsPlayer1[i] = sum;
            System.out.printf("U(A%d,%s) = ", i+1, formatBeliefs(beliefsPlayer2));
            System.out.printf("%.2f\n",sum );

        }
        System.out.println("----------------------------------------------");
        System.out.println("Player 1 Best Response with Player 2 Mixing");
        System.out.println("----------------------------------------------");
        
        printBestResponse(expectedPlayoffsPlayer1,beliefsPlayer2, "A");
        System.out.println("\n----------------------------------------------");
        System.out.println("Player 2 Expected Payoffs with Player 1 Mixing");
        System.out.println("----------------------------------------------");
        //Multipies player 2's payoffs at column j by the belief at i to find the expected payoff 
        for (int j = 0; j < cols; j++){
            sum = 0;
            for (int i = 0; i < rows; i++){
                sum += player2[i][j] * beliefsPlayer1[i];
            }
            expectedPlayoffsPlayer2[j] = sum;
            System.out.printf("U(B%d,%s) = ", j+1, formatBeliefs(beliefsPlayer1));
            System.out.printf("%.2f\n",sum );
        }
        System.out.println("----------------------------------------------");
        System.out.println("Player 2 Best Response with Player 1 Mixing");
        System.out.println("----------------------------------------------");
        printBestResponse(expectedPlayoffsPlayer2,beliefsPlayer1, "B");
        
        //Expected Payoffs with both player's beliefs are mixed
        double u1 = 0, u2 = 0;
        for (int i = 0; i < rows; i++){
            for (int j = 0; j < cols; j++){
                u1 += beliefsPlayer1[i] * beliefsPlayer2[j] * player1[i][j];
                u2 += beliefsPlayer1[i] * beliefsPlayer2[j] * player2[i][j];
            }
        }
        System.out.println("\n----------------------------------------------");
        System.out.println("Player 1 & 2 Expected Payoffs with both Players Mixing");
        System.out.println("----------------------------------------------");
        //Prints the output as a formatted string to show the expected payoffs when both player's beliefs are mixed.
        System.out.printf("Player 1 -> U(%s),%s)) = %.2f\n", formatBeliefs(beliefsPlayer1), formatBeliefs(beliefsPlayer2), u1);
        System.out.printf("Player 2 -> U(%s),%s)) = %.2f\n", formatBeliefs(beliefsPlayer1), formatBeliefs(beliefsPlayer2), u2);
        
    }
    /*
        The formatBeliefs is used to format the generated beliefs with two decimal places of precision. It is a helper method for printBeliefs.
    */
    public static String formatBeliefs(double[] beliefs){ 
        StringBuilder sb = new StringBuilder("("); //A StringBuilder is used to create the formatted strings. 
        
        for(int i = 0; i < beliefs.length; i++){//The for loop will go through each value in the beliefs array and create the formatted string.
            sb.append(String.format("%.2f", beliefs[i]));

            if (i < beliefs.length - 1)
                sb.append(", ");
        }
        return sb.toString();
    }
    /*
        The generateBeliefs is used to generate beliefs for each player. It is a helper method for printBeliefs. This is done via a for loop that 
        generates random doubles and adds them to a sum variable. The values are then normalized by dividing by the sum.
    */
    public static double[] generateBeliefs(int size){
        double[] beliefs = new double[size];
        double sum = 0;
        double best = Double.MIN_VALUE;
        
        for (int i = 0; i < size; i++){
            beliefs[i] = rand.nextDouble();
            sum += beliefs[i];            
        }
        
        for (int i = 0; i < size; i++){
            beliefs[i] /= sum;
        }
        
        return beliefs;
    }
    /*
        The printBestResponse method is used print the best response strategies by finding the strategies that achieve the maximum payoff.
        It is a helper method for printBeliefs. 
    */
    public static void printBestResponse(double[] expectedPayoffs, double[] beliefs, String label){
        //Find the maximum payoffs
        double max = Arrays.stream(expectedPayoffs).max().getAsDouble();
        System.out.print("BR( ");
        String formatted = formatBeliefs(beliefs);//Format the beliefs as a string to be printed out to the console.
        System.out.print(formatted);
        System.out.print(") = {");
        for (int i = 0; i < expectedPayoffs.length; i++){//Used to output the strategies that give the maximum payoff.
            if(Math.abs(expectedPayoffs[i] - max) < 1e-6){
                System.out.print(label + (i+1) + " ");
            }
        }
        System.out.print("}"); 
        
    }
    /*
        The indifference method is used find the indifference mix probabilities if the game is 2x2
    */
    public static void indifference(){
        double qValue, pValue;
        //qValue and pValue represent the probability of player 1 and 2's strategies.
        double denominator = player1[0][0] - player1[0][1] - player1[1][0] + player1[1][1];
        qValue = (player1[1][1] - player1[0][1])/denominator;
        
        denominator = player2[0][0] - player2[1][0] - player2[0][1] + player2[1][1];
        pValue = (player2[1][1] - player2[1][0]) / denominator;
        System.out.printf("Player 1 probability of strategies (A1) = %.2f\n", pValue);
        System.out.printf("Player 1 probability of strategies (A2) = %.2f\n", 1 - pValue);
        System.out.printf("Player 2 probability of strategies (B1) = %.2f\n", qValue);
        System.out.printf("Player 2 probability of strategies (B2) = %.2f\n", 1 - qValue);

    }
}
