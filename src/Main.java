import java.io.*;
import java.util.*;
import java.time.*;
import java.time.temporal.ChronoUnit;



class Trivia {



    public static class Main {
        public static void main(String[] args) {
            String file = "TriviaSample.txt";
            // edit
            LocalDate registrationDate = LocalDate.of(2023, 11, 28);
            displayTrivia(file, registrationDate);
        }

        public static void displayTrivia(String file, LocalDate registrationDate) {
            try (FileReader reader = new FileReader(file);
                 BufferedReader br = new BufferedReader(reader)) {
                String line;
                int totalPoints = 0;
                int dayCount = calculateDay(registrationDate, LocalDate.now());
                int questionCount = 0;

                while ((line = br.readLine()) != null) {
                    String option;
                    String answer;
                    String question = line;

//                    In case the questions are in multiple lines
                    String nextLine = br.readLine();
                    if(nextLine.contains("?")){
                        question= line +"\n"+ nextLine;
                     option = br.readLine();}
                    else{ option = nextLine;}

//                    In case options in multiple lines
                    String lineAfterQuestion = br.readLine();
                    if(lineAfterQuestion.contains(",")||lineAfterQuestion.contains("character")){
                        option = option + lineAfterQuestion;
                        answer = br.readLine();
                    }
                    else{answer = lineAfterQuestion;}
                    String[] choice = displayOptions(option);
                    List<String> choices= Arrays.asList(choice);
                    String blank = br.readLine();

                    // Increment the question counter
                    questionCount++;


                    // Display the question based on dayCount
                    if (questionCount == dayCount) {
                        int points = displayQuestion(dayCount, question, choices, answer, blank);
                        totalPoints += points;
                        System.out.println("You have been awarded " + points + " point(s), you now have " + totalPoints + " points.\n");
                        dayCount++;
                        break;
                    }
                }
//                catch and identify the errors when the code runs
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public static int calculateDay(LocalDate RegistrationDate, LocalDate currentDate) {
            long days = ChronoUnit.DAYS.between(RegistrationDate, currentDate)+1;
            return (int) days ;
        }

        public static <choices> int displayQuestion(int dayCount, String question,  List<String> choices, String answer, String blank) {
            Scanner scanner = new Scanner(System.in);

            System.out.println("Day " + dayCount + " Trivia (Attempt #1)");
            System.out.println("======================================================================");
            System.out.println(question);

            // Change the arrangement of options
            Collections.shuffle(choices);

            char option = 'A';
            for (String choice : choices) {
                System.out.println("[" + option + "] " + choice);
                option++;
            }
            System.out.println("======================================================================");

            // User input
            System.out.print("Enter your answer (A/B/C/D): ");
            char userChoice = scanner.next().charAt(0);

            // Validate user input
            int choiceIndex = userChoice - 'A';
            if (choiceIndex < 0 || choiceIndex >= 4) {
                System.out.println("Invalid choice. Try again.");
                return 0;
            }

            // Check if the user's choice is correct
            if (choices.get(choiceIndex).equals(answer)) {
                System.out.println("Congratulations! You answered it correctly.\n");
                return 2; // 2 points for the first attempt
            } else {
                System.out.println("Whoops, that doesn’t look right, try again!\n");

                // Second attempt
                System.out.println("Day " + dayCount + " Trivia (Attempt #2)");
                System.out.println("======================================================================");
                System.out.println(question);

                // Rearrange options for the second attempt
                Collections.shuffle(choices);

                option = 'A';
                for (String choice : choices) {
                    System.out.println("[" + option + "] " + choice);
                    option++;
                }
                System.out.println("======================================================================");

                // User input for the second attempt
                System.out.print("Enter your answer (A/B/C/D): ");
                userChoice = scanner.next().charAt(0);

                // Validate user input
                choiceIndex = userChoice - 'A';
                if (choices.get(choiceIndex).equals(answer)) {
                    System.out.println("Congratulations! You answered it correctly.\n");
                    return 1; // 1 point for the second attempt
                } else {
                    System.out.printf("Incorrect. The correct answer is: %s \n" , answer);
                    return 0; // 0 points for incorrect answer
                }
            }
        }

        // Split the options in the txt file
        public static String[] displayOptions(String optionLine){
            return optionLine.split(",");}
    }}
