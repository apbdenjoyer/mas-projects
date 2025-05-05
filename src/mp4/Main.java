package mp4;


import mp3.parcel.Parcel;

import java.io.*;
import java.util.Scanner;

public class Main {
    private static final String separator = "=======================";
    private static final Scanner intScanner = new Scanner(System.in);
    private static final Scanner stringScanner = new Scanner(System.in);
    private static final String EXTENTS_DIR = "src/extents/";

    public static void main(String[] args) throws Exception {
        // Ensure extents directory exists in the project root
        File dir = new File(EXTENTS_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        while (true) {
            System.out.println("\n====== mp3.Main Menu ======");
            System.out.println("\n -- Options: --");
            System.out.println("1. Save Current Extent to File");
            System.out.println("2. Read Extent from File");
            System.out.println("3. Fill with Test Data");
            System.out.println("0. Exit");

            int choice = getIntInput("Choose an option: ");

            switch (choice) {
                case 1: {
                    String filename = getStringInput("Please provide a file name (with '.bin'): ");
                    try (ObjectOutputStream oos = new ObjectOutputStream(
                            new FileOutputStream(EXTENTS_DIR + filename))) {
                        ObjectPlus4.writeExtents(oos);
                        System.out.println("Extents saved to " + EXTENTS_DIR + filename);
                    } catch (IOException e) {
                        System.out.println("Error saving extents: " + e.getMessage());
                    }
                    break;
                }
                case 2: {
                    String filename = getStringInput("Please provide a file name (with '.bin'): ");
                    try (ObjectInputStream ois = new ObjectInputStream(
                            new FileInputStream(EXTENTS_DIR + filename))) {
                        ObjectPlus4.readExtents(ois);
                        System.out.println("Extents loaded from " + EXTENTS_DIR + filename);
                    } catch (IOException e) {
                        System.out.println("Error reading extents: " + e.getMessage());
                    }
                    break;
                }
                case 3:
                    fillInTestData();
                    break;

                case 0:
                    System.out.println("Exiting application...");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void fillInTestData() throws Exception {

        System.out.println(separator+"Static atribute constraint"+separator);


        User u1 = new User("basia", "basia@gmail.com", "koteczek123");   //ok

        System.out.println(u1);
        try {
            User u2 = new User("kasia", "kasia@gmail.com", "kot");   //err. - hasło < 8 znaków
        } catch (IllegalArgumentException e) {
            System.out.println(e);
        }

        System.out.println(separator+"Dynamic atribute constraint"+separator);

        u1.setLogin("basia2");  //ok
        try {
            u1.setLogin("basia2");  //err. - ta sama nazwa
        } catch (IllegalArgumentException e) {
            System.out.println(e);
        }

        System.out.println(separator+"Unique constraint"+separator);

        try {
            User u3 = new User("basia3", "basia@gmail.com", "koteczek321");  //err. - ten sam email co u1
        } catch (IllegalArgumentException e) {
            System.out.println(e);
        }

        User u4 = new User("adam", "adam@gmail.com", "adamMalysz123");
        Server s1 = new Server("server1");

        u1.addToServer(s1);

        System.out.println(separator+"Ordered constraint"+separator);


        Channel c1 = new Channel("channel1");
        c1.addToServer(s1);

        /*Sleeping for custom constraint*/
        Thread.sleep(5000);

        Message m1 = new Message(u1, "hejka");
        Thread.sleep(100);
        Message m2 = new Message(u1, "cześć");
        Thread.sleep(100);
        Message m3 = new Message(u1, "dzień dobry");
        Message m4 = new Message(u1, "siema");

        c1.addMessage(m1);
        c1.addMessage(m2);
        c1.addMessage(m3);
        c1.addMessagePinned(m3);    //ok

        System.out.println(separator+"Subset constraint"+separator);

        try {
            c1.addMessagePinned(m4);    //err. - nie ma na kanale normalnie
        } catch (Exception e) {
            System.out.println(e);
        }

        for (Message m : c1.getMessages()) {
            System.out.println(m);
        }

        System.out.println(separator+"Bag constraint"+separator);


        u1.addToServer(s1); //ok
        System.out.println(u1.getUserOnServers());

        System.out.println(separator+"XOR constraint"+separator);


        TextSupport ts = new TextSupport("UTF-8");
        c1.addTextSupport(ts);
        VoiceSupport vs = new VoiceSupport(328);
        try {
            c1.addVoiceSupport(vs);     //err. - istnieje już text support
        } catch (Mp4Exception e) {
            System.out.println(e);
        }

        System.out.println(separator+"Custom constraint"+separator);


        /*Before making a message, the user has to be at least 5 seconds old.*/
        User u5 = new User("kuba", "kuba@gmail.com", "1224356yhgt");

        try {
            Message m5 = new Message(u5, "hej");    //err. - konto za młode
        } catch (Mp4Exception e) {
            System.out.println(e);
        }

        Thread.sleep(5000);
        Message m5 = new Message(u5, "hej");
        c1.addMessage(m5);
    }

    private static int getIntInput(String prompt) {
        System.out.print(prompt);
        while (!intScanner.hasNextInt()) {
            System.out.println("Invalid input. Enter a valid number.");
            intScanner.next();
        }
        return intScanner.nextInt();
    }

    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return stringScanner.nextLine().trim();
    }

}
