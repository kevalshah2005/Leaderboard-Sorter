import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class App {
    static class ScoreComparator implements Comparator<Tank> {
        public int compare(Tank a, Tank b) {
            return (b.score - a.score) > 0 ? 1 : -1;
        }
    }

    static ArrayList<Tank> tanks;

    public static void main(String[] args) throws Exception {
        tanks = parseLeaderboard("Leaderboard.txt");
        Collections.sort(tanks, new ScoreComparator());
        writeToLeaderboard(tanks);
    }

    public static ArrayList<Tank> parseLeaderboard(String filepath) {
        ArrayList<Tank> returnList = new ArrayList<>();
        
        try {
            File leaderboard = new File(filepath);
            Scanner sc = new Scanner(leaderboard);
            while (sc.hasNextLine()) {
                String data = sc.nextLine();
                String delims = "[\\-(]";
                String[] tokens = data.split(delims);

                String name = tokens[1].replaceAll(" ", "");
                double score = -1;
                try {
                    score = Double.parseDouble(tokens[2].replaceAll("[]) ]", ""));
                } catch (NumberFormatException e) {
                    System.out.println("Error parsing score");
                    System.exit(-1);
                }
                
                returnList.add(new Tank(name, score));
            }

            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return returnList;
    }

    public static void writeToLeaderboard(ArrayList<Tank> tanks) {
        try {
            File sortedLeaderboard = new File("SortedLeaderboard.txt");
            if (sortedLeaderboard.createNewFile()) {
                System.out.println("File created: " + sortedLeaderboard.getName());
            } else {
                System.out.println("File already exists.");
            }
            
            FileWriter wr = new FileWriter(sortedLeaderboard.getAbsoluteFile());
            for (Tank tank : tanks) {
                wr.write("-" + tank.name + " (" + tank.score + ")\n");
            }
            wr.close();

            System.out.println("Successfully sorted leaderboard. Please check SortedLeaderboard.txt for the sorted leaderboard.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
