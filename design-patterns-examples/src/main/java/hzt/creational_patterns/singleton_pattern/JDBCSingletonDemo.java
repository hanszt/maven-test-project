package hzt.creational_patterns.singleton_pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"squid:S106", "squid:S2221"})
class JDBCSingletonDemo {

    private static final Logger LOGGER = LoggerFactory.getLogger(JDBCSingletonDemo.class);

    private static final List<Integer> readValueList = new ArrayList<>();

    static int count = 1;
    static int choice;

    public static void main(String[] args) {

        JDBCSingleton jdbc = JDBCSingleton.getInstance();

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
        do {
            LOGGER.info("DATABASE OPERATIONS");
            LOGGER.info(" --------------------- ");
            LOGGER.info(" 1. Insertion ");
            LOGGER.info(" 2. View      ");
            LOGGER.info(" 3. Delete    ");
            LOGGER.info(" 4. Update    ");
            LOGGER.info(" 5. Exit      ");

            System.out.print("\n");
            System.out.print("Please enter the choice what you want to perform in the database: ");


                choice = Integer.parseInt(br.readLine());
            switch (choice) {

                case 1:
                    executeInsertion(jdbc, br);
                break;
                case 2:
                    executeView(jdbc, br);
                break;
                case 3:
                    executeDelete(jdbc, br);
                    break;
                case 4:
                    executeUpdate(jdbc, br);
                    break;
                default:
                    return;
            }

            LOGGER.info("Press Enter key to continue...");
            readValueList.add(System.in.read());
        } while (choice != 4);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        LOGGER.info("ReadValues: {}", readValueList);
    }

    private static void executeUpdate(JDBCSingleton jdbc, BufferedReader br) throws IOException {
        System.out.print("Enter the username,  you want to update: ");
        String username = br.readLine();
        System.out.print("Enter the new password ");
        String password = br.readLine();

        try {
            int i = jdbc.update(username, password);
            if (i > 0) {
                LOGGER.info("{} Data has been updated successfully", count++);
            }

        } catch (RuntimeException e) {
           LOGGER.error("Could not execute update", e);
        }
    }

    private static void executeDelete(JDBCSingleton jdbc, BufferedReader br) throws IOException {
        System.out.print("Enter the userid,  you want to delete: ");
        int userid = Integer.parseInt(br.readLine());

        try {
            int i = jdbc.delete(userid);
            if (i > 0) {
                LOGGER.info("{} Data has been deleted successfully", count++);
            } else {
                LOGGER.info("Data has not been deleted");
            }

        } catch (Exception e) {
            LOGGER.error("Could not execute delete", e);
        }
    }

    private static void executeView(JDBCSingleton jdbc, BufferedReader br) throws IOException {
        System.out.print("Enter the username : ");
        String username = br.readLine();

        try {
            jdbc.view(username);
        } catch (Exception e) {
            LOGGER.error("Could not execute view", e);
        }
    }

    private static void executeInsertion(JDBCSingleton jdbc, BufferedReader br) throws IOException {
        System.out.print("Enter the username you want to insert data into the database: ");
        String username = br.readLine();
        System.out.print("Enter the password you want to insert data into the database: ");
        String password = br.readLine();

        try {
            int i = jdbc.insert(username, password);
            if (i > 0) {
                LOGGER.info("{} Data has been inserted successfully", (count++));
            } else {
                LOGGER.info("Data has not been inserted ");
            }

        } catch (Exception e) {
            LOGGER.error("Could not execute insertion", e);
        }
    }
}
