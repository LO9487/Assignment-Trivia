import java.io.*;
import java.util.*;
import java.time.*;
import java.time.temporal.ChronoUnit;



    class Trivia {



        public static class Main {
            public static void main(String[] args) {
                String file = "TriviaSample.txt";
                LocalDate userRegistrationDate = LocalDate.of(2023, 11, 27);
                displayTrivia(file, userRegistrationDate);
            }

            public static void displayTrivia(String file, LocalDate userRegistrationDate) {
                try (FileReader reader = new FileReader(file);
                     BufferedReader br = new BufferedReader(reader)) {

                    String line;
                    int totalPoints = 0;
                    int dayCount = calculateDayCount(userRegistrationDate, LocalDate.now());

                    // Display trivia questions based on the current day
                    for (int i = dayCount; (line = br.readLine()) != null; i++) {
                        String question = line;
                        List<String> choices = parseChoices(br.readLine());
                        String answer = br.readLine();
                        String blank = br.readLine();  // Skip the blank line

                        // Check if there is another line for the question
                        String nextLine;
                        while ((nextLine = br.readLine()) != null && !nextLine.isEmpty()) {
                            question += "\n" + nextLine;  // Append additional lines to the question
                        }

                        int points = presentQuestion(i + 1, question, choices, answer, blank);
                        totalPoints += points;

                        System.out.println("You have been awarded " + points + " point(s), you now have " + totalPoints + " points.\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            public static int calculateDayCount(LocalDate userRegistrationDate, LocalDate currentDate) {
                long days = ChronoUnit.DAYS.between(userRegistrationDate, currentDate);
                return (int) days ;
            }

            public static int presentQuestion(int dayCount, String question, List<String> choices, String answer, String blank) {
                Scanner scanner = new Scanner(System.in);

                System.out.println("Day " + dayCount + " Trivia (Attempt #1)");
                System.out.println("======================================================================");
                System.out.println(question);

                // Shuffle options
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

                    // Shuffle options for the second attempt
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

            public static List<String> parseChoices(String choicesLine) {
                return Arrays.asList(choicesLine.split(","));
            }
        }}





