import java.util.ArrayList;
import java.util.Scanner;
 
class Bin {
    int number;
    int fullness;
    String wasteType;
    String priority;
 
    Bin(int number, int fullness, String wasteType, String priority) {
        this.number = number;
        this.fullness = fullness;
        this.wasteType = wasteType;
        this.priority = priority;
    }
}
 
public class MunicipalWasteCollectionOptimizer {
     static String getWasteType(int choice) {
        switch (choice) {
            case 1:  return "Organic";
            case 2:  return "Plastic";
            default: return "Metal";
        }
    }
 
        static String getPriority(int fullness) {
        if (fullness < 50) {
            return "LOW";
        } else if (fullness <= 80) {
            return "MEDIUM";
        } else {
            return "URGENT";
        }
    }
 
    public static void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);
 
        ArrayList<Bin> bins = new ArrayList<>();
 
        System.out.print("Number of bins: ");
        int n = sc.nextInt();
 
        
        for (int i = 1; i <= n; i++) {
            System.out.println("\nBin " + i);
 
            System.out.print("Fullness (0-100): ");
            int fullness = sc.nextInt();
            while (fullness < 0 || fullness > 100) {      // validation
                System.out.print("Invalid! Enter 0-100: ");
                fullness = sc.nextInt();
            }
 
            System.out.print("Waste type (1=Organic, 2=Plastic, 3=Metal): ");
            int choice = sc.nextInt();
            while (choice < 1 || choice > 3) {            // validation
                System.out.print("Invalid! Enter 1-3: ");
                choice = sc.nextInt();
            }
 
            String type = getWasteType(choice);
            String priority = getPriority(fullness);
            bins.add(new Bin(i, fullness, type, priority));
        }
         System.out.println("\n--- RESULT ---");
        for (Bin b : bins) {
            System.out.println("Bin " + b.number + ": " + b.fullness + "% | " + b.wasteType);
            System.out.println("Priority: " + b.priority + " | Truck: " + b.wasteType + "\n");
        }
    }
}