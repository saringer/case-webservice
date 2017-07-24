package de.agdb.views.contacts.manage_contacts;

import de.agdb.backend.entities.Contacts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ContactService {

    private static ContactService instance;
    private static final Logger LOGGER = Logger.getLogger(ContactService.class.getName());

    private final HashMap<Long, Contacts> contacts = new HashMap<>();
    private long nextId = 0;

    @Autowired
    JdbcTemplate jdbcTemplate;


    public ContactService() {

    }

    /**
     * @return a reference to an example facade for Contacts objects.
     */
    public static ContactService getInstance() {
        if (instance == null) {
            instance = new ContactService();
            instance.ensureTestData();
        }
        return instance;
    }

    /**
     * @return all available Contacts objects.
     */
    public synchronized List<Contacts> findAll() {
        return findAll(null);
    }

    /**
     * Finds all Contacts's that match given filter.
     *
     * @param stringFilter
     *            filter that returned objects should match or null/empty string
     *            if all objects should be returned.
     * @return list a Contacts objects
     */
    public synchronized List<Contacts> findAll(String stringFilter) {
        ArrayList<Contacts> arrayList = new ArrayList<>();
        for (Contacts contact : contacts.values()) {
            try {
                boolean passesFilter = (stringFilter == null || stringFilter.isEmpty())
                        || contact.toString().toLowerCase().contains(stringFilter.toLowerCase());
                if (passesFilter) {
                    arrayList.add(contact.clone());
                }
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(ContactService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        Collections.sort(arrayList, new Comparator<Contacts>() {

            @Override
            public int compare(Contacts o1, Contacts o2) {
                return (int) (o2.getId() - o1.getId());
            }
        });
        return arrayList;
    }

    /**
     * Finds all Contacts's that match given filter and limits the resultset.
     *
     * @param stringFilter
     *            filter that returned objects should match or null/empty string
     *            if all objects should be returned.
     * @param start
     *            the index of first result
     * @param maxresults
     *            maximum result count
     * @return list a Contacts objects
     */
    public synchronized List<Contacts> findAll(String stringFilter, int start, int maxresults) {
        ArrayList<Contacts> arrayList = new ArrayList<>();
        for (Contacts contact : contacts.values()) {
            try {
                boolean passesFilter = (stringFilter == null || stringFilter.isEmpty())
                        || contact.toString().toLowerCase().contains(stringFilter.toLowerCase());
                if (passesFilter) {
                    arrayList.add(contact.clone());
                }
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(ContactService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        Collections.sort(arrayList, new Comparator<Contacts>() {

            @Override
            public int compare(Contacts o1, Contacts o2) {
                return (int) (o2.getId() - o1.getId());
            }
        });
        int end = start + maxresults;
        if (end > arrayList.size()) {
            end = arrayList.size();
        }
        return arrayList.subList(start, end);
    }

    /**
     * @return the amount of all customers in the system
     */
    public synchronized long count() {
        return contacts.size();
    }

    /**
     * Deletes a customer from a system
     *
     * @param value
     *            the Contacts to be deleted
     */
    public synchronized void delete(Contacts value) {
        contacts.remove(value.getId());
    }

    /**
     * Persists or updates customer in the system. Also assigns an identifier
     * for new Contacts instances.
     *
     * @param entry
     */
    public synchronized void save(Contacts entry) {
        if (entry == null) {
            LOGGER.log(Level.SEVERE,
                    "Contact is empty/null");
            return;
        }
        if (entry.getId() == null) {
            entry.setId(nextId++);
        }
        try {
            entry = (Contacts) entry.clone();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        contacts.put(entry.getId(), entry);
    }

    /**
     * Sample Data
     */
    public void ensureTestData() {
       if (findAll().isEmpty()) {
            final String[] names = new String[] { "Gabrielle Patel", "Brian Robinson", "Eduardo Haugen",
                    "Koen Johansen", "Alejandro Macdonald", "Angel Karlsson", "Yahir Gustavsson", "Haiden Svensson",
                    "Emily Stewart", "Corinne Davis", "Ryann Davis", "Yurem Jackson", "Kelly Gustavsson",
                    "Eileen Walker", "Katelyn Martin", "Israel Carlsson", "Quinn Hansson", "Makena Smith",
                    "Danielle Watson", "Leland Harris", "Gunner Karlsen", "Jamar Olsson", "Lara Martin",
                    "Ann Andersson", "Remington Andersson", "Rene Carlsson", "Elvis Olsen", "Solomon Olsen",
                    "Jaydan Jackson", "Bernard Nilsen" };
            Random r = new Random(0);
            for (String name : names) {
                String[] split = name.split(" ");
                Contacts c = new Contacts();
                c.setFirstName(split[0]);
                c.setLastName(split[1]);
                c.setEmail(split[0].toLowerCase() + "@" + split[1].toLowerCase() + ".com");
                //c.setStatus(ContactStatus.values()[r.nextInt(ContactStatus.values().length)]);
                c.setStatus("No Status");
                int daysOld = 0 - r.nextInt(365 * 15 + 365 * 60);
                c.setBirthDate(LocalDate.now().plusDays(daysOld));
                save(c);
            }
        }

        //List<Categories> categories = jdbcTemp.queryForList(sql, Categories.class);


    }

}
