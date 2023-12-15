import java.io.*;
import java.util.*;
import java.time.*;
import java.time.temporal.ChronoUnit;



class Trivia {



    public static class Main {
        public static void main(String[] args) {
            String file = "TriviaSample.txt";
            int totalPoints = 0;
            LocalDate userRegistrationDate = LocalDate.of(2023, 12, 10);
            int dayCount = calculateDayCount(userRegistrationDate, LocalDate.now());
            readFile(file, userRegistrationDate);
            redoUnattemptedQuestion(file,totalPoints,dayCount);
        }

        public static void readFile(String file, LocalDate userRegistrationDate) {
            try (FileReader reader = new FileReader(file);
                 BufferedReader br = new BufferedReader(reader)) {
                String line;
                int totalPoints = 0;
                int dayCount = calculateDayCount(userRegistrationDate, LocalDate.now());
                int questionCount = 0;

                while ((line = br.readLine()) != null) {
                    String option;
                    String answer;
                    List<String> choices;
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
                    choices = seperateChoices(option);
                    String blank = br.readLine();
                   
                    questionCount++;
                    
                 if (questionCount == dayCount){
                        System.out.println("Day " + (dayCount) + " - Question not attempted.");
                        System.out.print("Do you want to attempt this question now? (yes/no): \n");
                        Scanner scanner = new Scanner(System.in);
                        String userChoice = scanner.nextLine();

                        if (userChoice.equals("yes")) {
                            int points = presentQuestion(dayCount, question, choices, answer, blank, totalPoints);
                        totalPoints += points;
                        
                        } else {
                            System.out.println("Skipping question.\n");
                        }
                    
             


//                     Display the question based on dayCount
//                    if (questionCounter == dayCount) {
//                        int points = presentQuestion(dayCount, question, choices, answer, blank);
//                        totalPoints += points;
//                        System.out.println("You have been awarded " + points + " point(s), you now have " + totalPoints + " points.\n");
//                       
//                        break;
                    }
                }
            }catch (IOException e) {
                e.printStackTrace();}
        }
    }
    
    
    
        public static void reviewAttemptedQuestion(String question,String answer,boolean attempted,int dayNum) {

            System.out.println("Question "+dayNum+ " :");
            System.out.println("=".repeat(75));
            System.out.println(question);

            // Display the answer if the user already answered the question
            if (attempted) {
                System.out.println("Correct Answer: " + answer);
            }

            System.out.println("=".repeat(75));
            System.out.println();
        }

        public static int calculateDayCount(LocalDate userRegistrationDate, LocalDate currentDate) {
            long days = ChronoUnit.DAYS.between(userRegistrationDate, currentDate)+1;
            return (int) days ;
        }

        public static int presentQuestion(int dayCount, String question, List<String> choices, String answer, String blank, int totalPoints) {
            Scanner scanner = new Scanner(System.in);   
            // Shuffle options
               Collections.shuffle(choices);

    int points = 0;
    char userChoice;

  
    for (int attempt = 1; attempt <= 2; attempt++) {
        // Shuffle options for each attempt
        Collections.shuffle(choices);

        System.out.println("Day " + dayCount + " Trivia (Attempt #" + attempt + ")");
        System.out.println("======================================================================");
        System.out.println(question);

        char option = 'A';
        for (String choice : choices) {
            System.out.println("[" + option + "] " + choice);
            option++;
        }
        System.out.println("======================================================================");

        System.out.print("Enter your answer (A/B/C/D): ");
        userChoice = scanner.next().charAt(0);

        int choiceIndex = userChoice - 'A';

        if (userChoice >= 'A' && userChoice <= 'D' && choiceIndex < choices.size()) {
            
            if (choices.get(choiceIndex).equals(answer)) {
                System.out.println("Congratulations! You answered it correctly.");
                points = (attempt == 1) ? 2 : 1;
                System.out.println("You have been awarded " + points + " point(s), you now have " + points + " points.\n");
                break;
            } else { if(attempt ==1){
                System.out.println("Whoops, that doesn’t look right, try again!\n");}
                   else if(attempt == 2){System.out.println("Your answer is still incorrect, the correct answer is:[" +(char) ('A' + choiceIndex-1) +"]"+ answer);}    
            }
        } else {
            System.out.println("Invalid choice. Try again.");
            attempt--; // To repeat the current attempt
        }
    }
            storeUserAnswer(answer);
    return points;
    
    
}
        
        public static void storeUserAnswer(String answer){
        try {
          FileWriter fileWriter = new FileWriter("userAnswer.txt", true);
          BufferedWriter bw = new BufferedWriter(fileWriter);
          bw.write(answer+"\n");
          bw.close();
                    }catch(Exception e){System.out.println("Error in storeUserAnswer ");}
       
       
       }
        
        public static void redoUnattemptedQuestion(String file, int totalPoints, int dayCount){
                try (FileReader reader = new FileReader(file);
                 BufferedReader br = new BufferedReader(reader)) {
                String line;
               
                
                int questionCount = 0;

                while ((line = br.readLine()) != null) {
                    String option;
                    String answers;
                    List<String> choices;
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
                        answers = br.readLine();
                    }
                    else{answers = lineAfterQuestion;}
                    choices = seperateChoices(option);
                    String blank = br.readLine();
                   
                    questionCount++;
                    
                if(questionCount< dayCount)
                {if(checkAttemptedQuestion(answers)){reviewAttemptedQuestion(question, answers, true, questionCount);}
                    else { System.out.println("Day " + questionCount + " - Question not attempted.");
                        System.out.print("Do you want to attempt this question now? (yes/no): \n");
                        Scanner scanner = new Scanner(System.in);
                        String userChoice = scanner.nextLine();

                        if (userChoice.equals("yes")) {
                            int points = presentQuestion(questionCount, question, choices, answers, blank, totalPoints);
                        totalPoints += points;
                        
                        } else {
                            System.out.println("Skipping question.\n");}}}    
                    
                }}catch(Exception e){System.out.println("Error occurs in redoUnattemptedQuestion");}
        
        
        }
        
        
        public static boolean checkAttemptedQuestion(String answer){
        try {FileReader reader = new FileReader("userAnswer.txt");
                 BufferedReader br = new BufferedReader(reader);
                 String lines="";
                  while ((lines = br.readLine()) != null){
                          if(lines.equals(answer)){return true;}
                          if(br.readLine().equals(answer)){return true;}
                  }
                         }catch(Exception e){};
        return false;
        }
        
        public static List<String> seperateChoices(String choicesLine) {
            return Arrays.asList(choicesLine.split(","));
        }
    }
