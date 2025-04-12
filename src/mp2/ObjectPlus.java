package mp2;

import java.io.*;
import java.util.*;

public abstract class ObjectPlus implements Serializable {

    private static Map<Class<? extends ObjectPlus>, List<ObjectPlus>> extents = new HashMap<>();
    private Map<String, Map<Object, ObjectPlus>> links = new Hashtable<>();
    private static Set<ObjectPlus> allParts = new HashSet<>();

    public ObjectPlus() {
        List<ObjectPlus> extent;

        Class<? extends ObjectPlus> type = this.getClass();
        if (extents.containsKey(type)) {
            extent = extents.get(type);
        } else {
            extent = new ArrayList<>();
            extents.put(type, extent);
        }
        extent.add(this);
    }

    public static void writeExtents(ObjectOutputStream stream) {
        try {
            stream.writeObject(extents);
            System.out.print("Extent saved");
        } catch (IOException e) {
            System.out.println("Error writing extents");
        }
    }

    public static void readExtents(ObjectInputStream stream) {
        try {
            extents = (HashMap) stream.readObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<Class<? extends ObjectPlus>, List<ObjectPlus>> getExtents() {
        return Collections.unmodifiableMap(extents);
    }

    public static <T> Iterable<T> getExtent(Class<T> type) throws ClassNotFoundException {
        if (extents.containsKey(type)) {
            return (Iterable<T>) extents.get(type);
        }

        throw new ClassNotFoundException(
                String.format("%s. Stored extents: %s",
                        type.toString(),
                        extents.keySet()));
    }

    public static void showExtent(Class<? extends ObjectPlus> type) throws Exception {
        List<ObjectPlus> extent = null;

        if (extents.containsKey(type)) {
            extent = extents.get(type);
        } else {
            throw new ClassNotFoundException("Unknown class: " + type);
        }

        System.out.printf("Extent of class \"%s\": %n", type.getSimpleName());

        for (Object obj : extent) {
            System.out.println(obj);
        }
    }

    public static void removeFromExtent(ObjectPlus obj) {
        if (extents.containsKey(obj.getClass())) {
            extents.get(obj.getClass()).remove(obj);
        }

        List<ObjectPlus> partsToRemove = new ArrayList<>();

        for (Map<Object, ObjectPlus> roleLinks : obj.links.values()) {
            for (ObjectPlus linkedObj : roleLinks.values()) {
                if (allParts.contains(linkedObj)) {
                    partsToRemove.add(linkedObj);
                }
            }
        }

        for (ObjectPlus part : partsToRemove) {
            if (extents.containsKey(part.getClass())) {
                extents.get(part.getClass()).remove(part);
            }
            allParts.remove(part);
        }
    }


    private void addLink(String role, String reverseRole,
                         ObjectPlus target, Object qualifier,
                         int counter) {

        Map<Object, ObjectPlus> objectLinks;

        if (counter < 1) {
            return;
        }

        if (links.containsKey(role)) {
            objectLinks = links.get(role);
        } else {
            objectLinks = new HashMap<>();
            links.put(role, objectLinks);
        }

        if (!objectLinks.containsKey(qualifier)) {
            objectLinks.put(qualifier, target);

            target.addLink(reverseRole, role, this, this, counter - 1);
        }
    }

    public void addLink(String role, String reverseRole,
                        ObjectPlus target,
                        Object qualifier) {
        addLink(role, reverseRole, target, qualifier, 2);
    }

    public void addLink(String role, String reverseRole, ObjectPlus target) {
        addLink(role, reverseRole, target, target, 0);
    }

    public void addPart(String role, String reverseRole, ObjectPlus part) throws Exception {
        if (allParts.contains(part)) {
            throw new Exception("Part is already connected.");
        }

        addLink(role, reverseRole, part);

        allParts.add(part);
    }

    public ObjectPlus[] getLinks(String role) throws Exception {
        Map<Object, ObjectPlus> objectLinks;

        if (!links.containsKey(role)) {
            throw new Exception("No links for role: " + role);
        }

        objectLinks = links.get(role);
        return (ObjectPlus[]) objectLinks.values().toArray(new ObjectPlus[0]);
    }

    public void showLinks(String role, PrintStream stream) throws Exception {
        Map<Object, ObjectPlus> objectLinks;

        if (!links.containsKey(role)) {
            throw new Exception("No links for role: " + role);
        }

        objectLinks = links.get(role);
        Collection<ObjectPlus> col = objectLinks.values();
        stream.println(this.getClass().getSimpleName() + " links, role '" + role + "':");
        for (Object obj : col) {
            stream.println("\t" + obj);
        }
    }

    public ObjectPlus getLinkedObject(String role, Object qualifier) throws Exception {
        Map<Object, ObjectPlus> objectLinks;

        if (!links.containsKey(role)) {
            throw new Exception("No links for role: " + role);
        }

        objectLinks = links.get(role);
        if (!objectLinks.containsKey(qualifier)) {
            throw new Exception("No links for qualifier: " + qualifier);
        }

        return objectLinks.get(qualifier);
    }

}
