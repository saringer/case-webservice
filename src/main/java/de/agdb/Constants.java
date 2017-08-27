package de.agdb;

public class Constants {

    protected Constants() {
        // don't allow the class to be instantiated
    }

    /*
        Location where the user gets redirected after successful OAuth2 authentication in the synchronize contacts
        view
     */
    public static final String redirectUrl = "https://127.0.0.1:80/";
    /*
        Application registered on the "Google API Console" via "jim.saringer@gmail.com"
     */
    public static final String googleClientId = "10783402561-trps0deukc9tbqlub7hv3uvljge1cds9.apps.googleusercontent.com";
    public static final String googleClientSecret = "AEHxKva-Sw9FtvJ3N9ArguA4";
    // Scope for accessing Google Contacts API
    public static final String googleScope = "https://www.googleapis.com/auth/contacts.readonly";

    /*
        Google Maps API Key (Local Host Development)
     */
    public static final String googleMapsApiKey = "AIzaSyDmcnkMKoB-xqSaZ6VdqT-k-G8bnHbO-wQ";


    /*
        Registered on the "Microsoft Registration Application Portal"  via "jim.saringer@outlook.de"
     */
    public static final String microsoftClientId = "79ca92bd-c42e-4d1e-b591-99df76f6832f";
    public static final String microsoftClientSecret = "qCz61Eb3S1tpPqdGpDxv000";
    // Scope for accessing Microsoft Contacts API
    public static final String microsoftScope = "wl.contacts_emails";

    /*

     */
    public static final String yahooClientId = "dj0yJmk9dDlza3dVS3E0eExwJmQ9WVdrOU16Uk9WMmh1TXpRbWNHbzlNQS0tJnM9Y29uc3VtZXJzZWNyZXQmeD0wYQ--";
    public static final String yahooClientSecret = "3a5f149e9c576679338f69d2d92d0eee32104ece";
    // Scope for accessing Yahoo Contacts API
    public static final String yahooScope = "sdct-r";
}
