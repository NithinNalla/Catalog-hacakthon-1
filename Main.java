import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

    private static final String DATA_FILE_PATH = "patient_records.txt";
    private static final String BACKUP_FILE_PATH = "patient_records_backup.txt";

    public static void main(String[] args) {
        Map<String, String> patientRecords = loadPatientRecords();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            displayMenu();
            String userChoice = scanner.nextLine();

            switch (userChoice) {
                case "1":
                    addRecord(scanner, patientRecords);
                    break;
                case "2":
                    viewRecord(scanner, patientRecords);
                    break;
                case "3":
                    viewAllRecords(patientRecords);
                    break;
                case "4":
                    updateRecord(scanner, patientRecords);
                    break;
                case "5":
                    deleteRecord(scanner, patientRecords);
                    break;
                case "6":
                    searchRecords(scanner, patientRecords);
                    break;
                case "7":
                    backupRecords(patientRecords);
                    break;
                case "8":
                    System.out.println("Exiting...");
                    System.out.println("Successfully exited.");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option. Please choose a valid option from the menu.");
            }
        }
    }

    private static void displayMenu() {
        System.out.println("\n===== Health Monitoring System =====");
        System.out.println("1. Add Record");
        System.out.println("2. View Record by Patient ID");
        System.out.println("3. View All Records");
        System.out.println("4. Update Record");
        System.out.println("5. Delete Record");
        System.out.println("6. Search Records by Keyword");
        System.out.println("7. Backup Records");
        System.out.println("8. Exit");
        System.out.print("Choose an option: ");
    }

    private static void addRecord(Scanner scanner, Map<String, String> patientRecords) {
        System.out.print("Enter patient ID: ");
        String patientId = scanner.nextLine();
        System.out.print("Enter health record: ");
        String healthRecord = scanner.nextLine();

        if (patientId.isEmpty() || healthRecord.isEmpty()) {
            System.out.println("Patient ID and health record cannot be empty.");
            return;
        }

        addPatientRecord(patientRecords, patientId, healthRecord);
    }

    private static void viewRecord(Scanner scanner, Map<String, String> patientRecords) {
        System.out.print("Enter patient ID: ");
        String patientId = scanner.nextLine();
        String record = getPatientRecord(patientRecords, patientId);
        System.out.println("Health Record: " + record);
    }

    private static void viewAllRecords(Map<String, String> patientRecords) {
        if (patientRecords.isEmpty()) {
            System.out.println("No records found.");
        } else {
            System.out.println("\n===== All Patient Records =====");
            for (Map.Entry<String, String> entry : patientRecords.entrySet()) {
                System.out.println("Patient ID: " + entry.getKey() + ", Health Record: " + entry.getValue());
            }
        }
    }

    private static void updateRecord(Scanner scanner, Map<String, String> patientRecords) {
        System.out.print("Enter patient ID to update: ");
        String patientId = scanner.nextLine();

        if (patientRecords.containsKey(patientId)) {
            System.out.print("Enter new health record: ");
            String newHealthRecord = scanner.nextLine();
            patientRecords.put(patientId, newHealthRecord);
            savePatientRecords(patientRecords);
            System.out.println("Record updated successfully.");
        } else {
            System.out.println("No record found for this ID.");
        }
    }

    private static void deleteRecord(Scanner scanner, Map<String, String> patientRecords) {
        System.out.print("Enter patient ID to delete: ");
        String patientId = scanner.nextLine();

        if (patientRecords.containsKey(patientId)) {
            patientRecords.remove(patientId);
            savePatientRecords(patientRecords);
            System.out.println("Record deleted successfully.");
        } else {
            System.out.println("No record found for this ID.");
        }
    }

    private static void searchRecords(Scanner scanner, Map<String, String> patientRecords) {
        System.out.print("Enter keyword to search: ");
        String keyword = scanner.nextLine().toLowerCase();

        boolean found = false;
        System.out.println("\n===== Search Results =====");
        for (Map.Entry<String, String> entry : patientRecords.entrySet()) {
            if (entry.getValue().toLowerCase().contains(keyword)) {
                System.out.println("Patient ID: " + entry.getKey() + ", Health Record: " + entry.getValue());
                found = true;
            }
        }
        if (!found) {
            System.out.println("No records found containing the keyword: " + keyword);
        }
    }

    private static void backupRecords(Map<String, String> patientRecords) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BACKUP_FILE_PATH))) {
            for (Map.Entry<String, String> entry : patientRecords.entrySet()) {
                writer.write(entry.getKey() + ":" + entry.getValue());
                writer.newLine();
            }
            System.out.println("Backup created successfully.");
        } catch (IOException e) {
            System.out.println("Error creating backup: " + e.getMessage());
        }
    }

    private static Map<String, String> loadPatientRecords() {
        Map<String, String> patientRecords = new HashMap<>();
        File file = new File(DATA_FILE_PATH);

        if (!file.exists()) {
            return patientRecords;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":", 2);
                if (parts.length == 2) {
                    String id = parts[0];
                    String record = parts[1];
                    patientRecords.put(id, record);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

        return patientRecords;
    }

    private static void savePatientRecords(Map<String, String> patientRecords) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_FILE_PATH))) {
            for (Map.Entry<String, String> entry : patientRecords.entrySet()) {
                writer.write(entry.getKey() + ":" + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    private static void addPatientRecord(Map<String, String> patientRecords, String patientId, String healthRecord) {
        patientRecords.put(patientId, healthRecord);
        savePatientRecords(patientRecords);
        System.out.println("Record added successfully.");
    }

    private static String getPatientRecord(Map<String, String> patientRecords, String patientId) {
        return patientRecords.getOrDefault(patientId, "No record found for this ID.");
    }
}
