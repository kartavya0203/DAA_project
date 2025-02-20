import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class DataCleaningProject {

    // Method to handle missing values (replace with mean)
    public static double[] handleMissingValues(double[] arr) {
        double sum = 0;
        int count = 0;

        for (double value : arr) {
            if (!Double.isNaN(value)) {
                sum += value;
                count++;
            }
        }

        double mean = sum / count;

        for (int i = 0; i < arr.length; i++) {
            if (Double.isNaN(arr[i])) {
                arr[i] = mean;
            }
        }

        return arr;
    }

    // Method to remove outliers (replace with NaN)
    public static double[] removeOutliers(double[] arr) {
        double[] result = new double[arr.length];
        double mean = calculateMean(arr);
        double stdDev = calculateStandardDeviation(arr, mean);

        for (int i = 0; i < arr.length; i++) {
            if (Math.abs(arr[i] - mean) > 2 * stdDev) {
                result[i] = Double.NaN;
            } else {
                result[i] = arr[i];
            }
        }

        return result;
    }

    // Method to remove duplicates
    public static double[] removeDuplicates(double[] arr) {
        Set<Double> set = new HashSet<>();
        List<Double> uniqueList = new ArrayList<>();

        for (double value : arr) {
            if (set.add(value)) {
                uniqueList.add(value);
            }
        }

        double[] uniqueArray = new double[uniqueList.size()];
        for (int i = 0; i < uniqueList.size(); i++) {
            uniqueArray[i] = uniqueList.get(i);
        }

        return uniqueArray;
    }

    // Method to calculate the mean
    public static double calculateMean(double[] arr) {
        double sum = 0;
        int count = 0;

        for (double value : arr) {
            sum += value;
            count++;
        }

        return sum / count;
    }

    // Method to calculate the standard deviation
    public static double calculateStandardDeviation(double[] arr, double mean) {
        double sum = 0;

        for (double value : arr) {
            sum += Math.pow(value - mean, 2);
        }

        return Math.sqrt(sum / arr.length);
    }

    // Method to print cleaned data
    public static void printCleanedData(double[] dataArray) {
        System.out.println("\nCleaned data:");
        for (double value : dataArray) {
            if (Double.isNaN(value)) {
                System.out.print("NaN ");
            } else {
                System.out.print(value + " ");
            }
        }
    }

    // Method to print mean and standard deviation
    public static void printMeanAndStandardDeviation(double[] arr) {
        double mean = calculateMean(arr);
        double stdDev = calculateStandardDeviation(arr, mean);

        System.out.println("\nMean: " + mean);
        System.out.println("Standard Deviation: " + stdDev);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to Data Cleaning Project");

        // Input dataset from the user
        System.out.println("Enter data separated by spaces, press enter to finish:");
        String inputLine = scanner.nextLine();
        if (inputLine.isEmpty()) {
            System.out.println("No data entered. Exiting...");
            scanner.close();
            return;
        }

        // Convert input to double array
        double[] dataArray = stringToDoubleArray(inputLine);

        boolean continueCleaning = true;
        while (continueCleaning) {
            // Choose data cleaning operation
            System.out.println("\nChoose a data cleaning operation:");
            System.out.println("1. Handle missing values");
            System.out.println("2. Remove outliers");
            System.out.println("3. Remove duplicates");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid choice. Please enter a valid number.");
                continue;
            }

            // Perform selected data cleaning operation
            switch (choice) {
                case 1:
                    dataArray = handleMissingValues(dataArray);
                    System.out.println("Missing values handled successfully.");
                    break;
                case 2:
                    dataArray = removeOutliers(dataArray);
                    System.out.println("Outliers removed successfully.");
                    break;
                case 3:
                    dataArray = removeDuplicates(dataArray);
                    System.out.println("Duplicates removed successfully.");
                    break;
                default:
                    System.out.println("Invalid choice.");
                    continue;
            }

            // Print cleaned data
            printCleanedData(dataArray);

            // Print mean and standard deviation
            printMeanAndStandardDeviation(dataArray);

            // Ask user if they want to continue cleaning the same dataset
            System.out.println("\nDo you want to perform another data cleaning operation on the same dataset? (yes/no)");
            String input = scanner.nextLine().trim().toLowerCase();
            continueCleaning = input.equals("yes");
        }

        System.out.println("Exiting Data Cleaning Project.");
        scanner.close();
    }

    // Method to convert string input to double array
    public static double[] stringToDoubleArray(String inputLine) {
        String[] values = inputLine.replaceAll(",", "").split("\\s+");
        double[] dataArray = new double[values.length];

        for (int i = 0; i < values.length; i++) {
            dataArray[i] = Double.parseDouble(values[i]);
        }

        return dataArray;
    }
}
