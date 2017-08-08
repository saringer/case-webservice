package de.agdb;

public class Constants {

    protected Constants() {
        // don't allow the class to be instantiated
    }

    /*
        Location where the user gets redirected after successful OAuth2 authentication in the synchronize contacts
        view
     */
    public static final String redirectUrl = "http://localhost:8080/";

    /*
        Application registered on the "Google API Console" via "jim.saringer@gmail.com"
     */
    public static final String googleClientId = "833038328979-iglf2mm0jap840b0vaq4jvo1miqvtkb4.apps.googleusercontent.com";
    public static final String googleClientSecret = "Q_Pb1hvhGs1hH1CIsqYv1S1M";
    // Scope for accessing Google Contacts API
    public static final String googleScope = "https://www.googleapis.com/auth/contacts.readonly";



    /*
        Registered on the "Microsoft Registration Application Portal"  via "jim.saringer@outlook.de"
     */
    public static final String microsoftClientId = "79ca92bd-c42e-4d1e-b591-99df76f6832f";
    public static final String microsoftClientSecret = "qCz61Eb3S1tpPqdGpDxv000";
    // Scope for accessing Microsoft Contacts API
    public static final String microsoftScope = "wl.basic";
}
