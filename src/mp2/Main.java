package mp2;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class Main {
    public static void main(String[] args) throws Exception {
        String separator = "================================================";
        User u1 = new User("maciek123");
        User u2 = new User("andrzej456");
        User u3 = new User("maja789");

        Server s1 = new Server("server1", u1);
        Server s2 = new Server("server2", u2);

        UserOnServer u1s2 = new UserOnServer(u1, s1);
        UserOnServer u2s2 = new UserOnServer(u3, s2);

        System.out.println(separator);
        u1.showLinks("is member of", System.out);
        u1s2.showLinks("user", System.out);
        System.out.println(separator);
        s1.showLinks("has a member", System.out);
        u1s2.showLinks("server", System.out);
        Role r1 = new Role("moderator", u1);
        Role r2 = new Role("admin", u2);

        s1.addLink("has a role", "of user", u2, r1);

        System.out.println(separator);

        System.out.printf("%s's users with role %s: %s%n",
                s1.getName(), r1.getName(),
                s1.getLinkedObject("has a role", r1));


        Message.createMessage(s1, u2, "hej hej");
        s1.showLinks("contains", System.out);

        System.out.println(separator);

//shows 1 message already added
        System.out.println(ObjectPlus.getExtent(Message.class));

        ObjectPlus.removeFromExtent(s1);

//        empty, since server got removed
//        System.out.println(ObjectPlus.getExtent(Message.class));


        String file = "out/extents.bin";
        try (ObjectOutputStream out =
                     new ObjectOutputStream(new FileOutputStream(file))) {
            ObjectPlus.writeExtents(out);
            System.out.printf("Extent saved to file %s.%n", file);
        } catch (IOException e) {
            System.out.printf("Error writing extents to file: %s%n", file);
        }
    }
}