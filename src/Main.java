import java.io.*;
import java.util.*;
import java.time.*;
import java.time.temporal.ChronoUnit;

class Trivia {

    public static class Main {
        public static void main(String[] args) {
            String file = "TriviaSample.txt";
            LocalDate registrationDate = LocalDate.of(2023, 12, 1);
            displayTrivia(file, registrationDate);
        }

        public static void displayTrivia(String file, LocalDate registrationDate) {
            try (FileReader reader = new FileReader(file);
                 BufferedReader br = new BufferedReader(reader)) {

                List<TriviaQuestion> questions = new ArrayList<>();
                int dayCount = calculateDay(registrationDate, LocalDate.now());

                // Read and store questions from the file
                String line;
                while ((line = br.readLine()) != null) {
                    String option;
                    String answer;
                    String question = line;

                    // In case the questions are in multiple lines
                    String nextLine = br.readLine();
                    if (nextLine.contains("?")) {
                        question = line + "\n" + nextLine;
                        option = br.readLine();
                    } else {
                        option = nextLine;
                    }

                    // In case options are in multiple lines
                    String lineAfterQuestion = br.readLine();
                    if (lineAfterQuestion.contains(",") || lineAfterQuestion.contains("character")) {
                        option = option + lineAfterQuestion;
                        answer = br.readLine();
                    } else {
                        answer = lineAfterQuestion;
                    }

                    questions.add(new TriviaQuestion(question, displayOptions(option), answer));
                    String blank = br.readLine();
                }

                // Display and review questions
                for (int i = 0; i < questions.size(); i++) {
                    TriviaQuestion currentQuestion = questions.get(i);

                    // Check if the question has been attempted
                    if (i < dayCount - 1) {
                        reviewQuestion(currentQuestion, true);
                    } else {
                        // Offer to attempt the question
                        System.out.println("Day " + (i + 1) + " - Question not attempted.");
                        System.out.print("Do you want to attempt this question now? (yes/no): ");
                        Scanner scanner = new Scanner(System.in);
                        String userChoice = scanner.next().toLowerCase();

                        if (userChoice.equals("yes")) {
                            int points = displayQuestion(i + 1, currentQuestion.getQuestion(),
                                    currentQuestion.getChoices(), currentQuestion.getAnswer(), "");
                            System.out.println("You have been awarded " + points + " point(s).");
                        } else {
                            System.out.println("Skipping question.\n");
                        }
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public static int calculateDay(LocalDate registrationDate, LocalDate currentDate) {
            long days = ChronoUnit.DAYS.between(registrationDate, currentDate) + 1;
            return (int) days;
        }

        public static void reviewQuestion(TriviaQuestion question, boolean showAnswer) {
            System.out.println("Reviewing Question:");
            System.out.println("======================================================================");
            System.out.println(question.getQuestion());

            // Display the answer if requested
            if (showAnswer) {
                System.out.println("Correct Answer: " + question.getAnswer());
            }

            System.out.println("======================================================================\n");
        }

        public static int displayQuestion(int dayCount, String question, List<String> choices, String answer, String blank) {
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
                    System.out.printf("Incorrect. The correct answer is: %s \n", answer);
                    return 0; // 0 points for incorrect answer
                }
            }
        }

        // Split the options in the txt file
        public static String[] displayOptions(String optionLine) {
            return optionLine.split(",");
        }
    }

// Class to represent a trivia question
public static class TriviaQuestion {
    private String question;
    private List<String> choices;
    private String answer;

    public TriviaQuestion(String question, String[] choices, String answer) {
        this.question = question;
        this.choices = Arrays.asList(choices);
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getChoices() {
        return choices;
    }

    public String getAnswer() {
        return answer;
    }
}
}
