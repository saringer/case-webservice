package de.agdb.views.contacts.synchronize_contacts;

import com.github.scribejava.apis.GoogleApi20;
import com.github.scribejava.apis.LiveApi;
import com.github.scribejava.core.model.*;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.*;
import de.agdb.backend.data_model.repositories.UsersRepository;
import de.agdb.custom_components.oauth2.OAuthListener;
import de.agdb.custom_components.oauth2.OAuthPopupButton;
import de.agdb.custom_components.oauth2.OAuthPopupConfig;
import org.vaadin.addons.Toastr;
import org.vaadin.addons.builder.ToastBuilder;

import static de.agdb.Constants.*;


public class ContactServiceLayout extends HorizontalLayout {
    private Toastr toastr;



    public ContactServiceLayout(String flag,  UsersRepository usersRepository, Toastr toastr) {
        this.toastr = toastr;

        setWidth("100%");
        setHeight(64, Unit.PIXELS);
        setSpacing(false);
        setMargin(true);


        HorizontalLayout horizontalWrapper = new HorizontalLayout();
        horizontalWrapper.setSpacing(false);
        horizontalWrapper.setHeight(60, Unit.PIXELS);
        horizontalWrapper.setWidth("85%");
        horizontalWrapper.addStyleNames("synccontacts-itembox");
        Label label = new Label();
        label.addStyleNames("blue-label");



        switch (flag) {
            case ("google"): {

                OAuthPopupConfig config = OAuthPopupConfig.getStandardOAuth20Config(googleClientId, googleClientSecret);
                config.setScope(googleScope);
                config.setCallbackUrl(redirectUrl);
                OAuthPopupButton google = new OAuthPopupButton(GoogleApi20.instance(), config);
                google.setHeight(30, Unit.PIXELS);
                google.setWidth(172, Unit.PIXELS);
                google.addStyleName("blue-button");
                google.setPopupWindowFeatures("resizable,width=800,height=600");
                google.addOAuthListener(new OAuthListener() {

                    @Override
                    public void authSuccessful(Token token, boolean isOAuth20) {
                        // Do something useful with the OAuth token, like persist it
                        if (token instanceof OAuth2AccessToken) {
                            ((OAuth2AccessToken) token).getAccessToken();
                            ((OAuth2AccessToken) token).getRefreshToken();
                            ((OAuth2AccessToken) token).getExpiresIn();
                            google.addStyleName("green-button");
                            toastr.toast(ToastBuilder.success("Contacts import successful").build());
                            new GoogleContactsDBParser().storeContactsInDatabase( ((OAuth2AccessToken) token).getAccessToken(), usersRepository);
                        } else {
                            ((OAuth1AccessToken) token).getToken();
                            ((OAuth1AccessToken) token).getTokenSecret();
                        }
                    }

                    @Override
                    public void authDenied(String reason) {
                        google.addStyleName("red-button");
                       toastr.toast(ToastBuilder.error("Contacts import failed").build());
                    }
                });

                horizontalWrapper.addComponent(label);
                horizontalWrapper.addComponent(google);
                horizontalWrapper.setComponentAlignment(label, Alignment.MIDDLE_LEFT);
                horizontalWrapper.setComponentAlignment(google, Alignment.MIDDLE_CENTER);

                label.setValue("Google Contacts");

                ThemeResource resource = new ThemeResource("views/img/mail_provider/social-google-box-icon.png");

                Embedded image = new Embedded(null, resource);
                image.setType(Embedded.TYPE_IMAGE);

                addComponent(image);
                addComponent(horizontalWrapper);
                setComponentAlignment(horizontalWrapper, Alignment.MIDDLE_CENTER);
                setExpandRatio(horizontalWrapper, 1);

                break;
                //content.addComponent(contactServiceLayout);


            }

            case ("microsoft"): {

                OAuthPopupConfig config = OAuthPopupConfig.getStandardOAuth20Config(microsoftClientId, microsoftClientSecret);
                config.setScope(microsoftScope);
                config.setCallbackUrl(redirectUrl);
                OAuthPopupButton microsoft = new OAuthPopupButton(LiveApi.instance(), config);
                microsoft.setHeight(30, Unit.PIXELS);
                microsoft.setWidth(172, Unit.PIXELS);
                microsoft.addStyleName("blue-button");
                microsoft.setPopupWindowFeatures("resizable,width=800,height=600");
                microsoft.addOAuthListener(new OAuthListener() {

                    @Override
                    public void authSuccessful(Token token, boolean isOAuth20) {
                        // Do something useful with the OAuth token, like persist it
                        if (token instanceof OAuth2AccessToken) {
                            ((OAuth2AccessToken) token).getAccessToken();
                            ((OAuth2AccessToken) token).getRefreshToken();
                            ((OAuth2AccessToken) token).getExpiresIn();
                            OAuth2AccessToken accessToken = (OAuth2AccessToken) token;
                            microsoft.addStyleName("green-button");
                            toastr.toast(ToastBuilder.success("Contacts import successful").build());
                            new MicrosoftContactsDBParser().storeContactsInDatabase(accessToken, usersRepository);


                        } else {
                            ((OAuth1AccessToken) token).getToken();
                            ((OAuth1AccessToken) token).getTokenSecret();
                        }
                    }

                    @Override
                    public void authDenied(String reason) {
                        microsoft.addStyleName("red-button");
                        toastr.toast(ToastBuilder.error("Contacts import failed").build());
                    }
                });

                horizontalWrapper.addComponent(label);
                horizontalWrapper.addComponent(microsoft);
                horizontalWrapper.setComponentAlignment(label, Alignment.MIDDLE_LEFT);
                horizontalWrapper.setComponentAlignment(microsoft, Alignment.MIDDLE_CENTER);

                label.setValue("Microsoft Live");

                ThemeResource resource = new ThemeResource("views/img/mail_provider/windows-8-icon.png");

                Embedded image = new Embedded(null, resource);
                image.setType(Embedded.TYPE_IMAGE);

                addComponent(image);
                addComponent(horizontalWrapper);
                setComponentAlignment(horizontalWrapper, Alignment.MIDDLE_CENTER);
                setExpandRatio(horizontalWrapper, 1);
                break;

            }

            case ("yahoo"): {

                OAuthPopupConfig config = OAuthPopupConfig.getStandardOAuth20Config(yahooClientId, yahooClientSecret);
                config.setScope(yahooScope);
                config.setCallbackUrl("http://127.0.0.1");
                OAuthPopupButton yahoo = new OAuthPopupButton(YahooApi.instance(), config);
                yahoo.setHeight(30, Unit.PIXELS);
                yahoo.setWidth(172, Unit.PIXELS);
                yahoo.addStyleName("blue-button");
                yahoo.setPopupWindowFeatures("resizable,width=800,height=600");
                yahoo.addOAuthListener(new OAuthListener() {

                    @Override
                    public void authSuccessful(Token token, boolean isOAuth20) {
                        // Do something useful with the OAuth token, like persist it
                        if (token instanceof OAuth2AccessToken) {
                            ((OAuth2AccessToken) token).getAccessToken();
                            ((OAuth2AccessToken) token).getRefreshToken();
                            ((OAuth2AccessToken) token).getExpiresIn();
                            yahoo.addStyleName("green-button");
                            toastr.toast(ToastBuilder.success("Contacts import successful").build());

                        } else {
                            ((OAuth1AccessToken) token).getToken();
                            ((OAuth1AccessToken) token).getTokenSecret();
                        }
                    }

                    @Override
                    public void authDenied(String reason) {
                        yahoo.addStyleName("red-button");
                        toastr.toast(ToastBuilder.error("Contacts import failed").build());

                    }
                });

                horizontalWrapper.addComponent(label);
                horizontalWrapper.addComponent(yahoo);
                horizontalWrapper.setComponentAlignment(label, Alignment.MIDDLE_LEFT);
                horizontalWrapper.setComponentAlignment(yahoo, Alignment.MIDDLE_CENTER);

                label.setValue("Yahoo");

                ThemeResource resource = new ThemeResource("views/img/mail_provider/yahoo-icon.png");

                Embedded image = new Embedded(null, resource);
                image.setType(Embedded.TYPE_IMAGE);

                addComponent(image);
                addComponent(horizontalWrapper);
                setComponentAlignment(horizontalWrapper, Alignment.MIDDLE_CENTER);
                setExpandRatio(horizontalWrapper, 1);
                break;

            }

            default: {
                break;
            }
        }

    }
}


