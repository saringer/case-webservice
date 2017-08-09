package de.agdb.views.contacts.synchronize_contacts;

import com.github.scribejava.apis.GoogleApi20;
import com.github.scribejava.apis.LiveApi;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.Token;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.*;
import de.agdb.test.OAuthListener;
import de.agdb.test.OAuthPopupButton;
import de.agdb.test.OAuthPopupConfig;

import static de.agdb.Constants.*;

public class ContactServiceLayout extends HorizontalLayout {

    public ContactServiceLayout(String flag) {

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
                        } else {
                            ((OAuth1AccessToken) token).getToken();
                            ((OAuth1AccessToken) token).getTokenSecret();
                        }
                    }

                    @Override
                    public void authDenied(String reason) {
                        Notification.show("Failed to authenticate!", Notification.Type.ERROR_MESSAGE);
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
                OAuthPopupButton google = new OAuthPopupButton(LiveApi.instance(), config);
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
                        } else {
                            ((OAuth1AccessToken) token).getToken();
                            ((OAuth1AccessToken) token).getTokenSecret();
                        }
                    }

                    @Override
                    public void authDenied(String reason) {
                        Notification.show("Failed to authenticate!" + reason, Notification.Type.ERROR_MESSAGE);
                    }
                });

                horizontalWrapper.addComponent(label);
                horizontalWrapper.addComponent(google);
                horizontalWrapper.setComponentAlignment(label, Alignment.MIDDLE_LEFT);
                horizontalWrapper.setComponentAlignment(google, Alignment.MIDDLE_CENTER);

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
                OAuthPopupButton google = new OAuthPopupButton(YahooApi.instance(), config);
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
                            System.out.print(((OAuth2AccessToken) token).getAccessToken());
                        } else {
                            ((OAuth1AccessToken) token).getToken();
                            ((OAuth1AccessToken) token).getTokenSecret();
                        }
                    }

                    @Override
                    public void authDenied(String reason) {
                        System.out.println("failed");
                        Notification.show("Failed to authenticate!" + reason, Notification.Type.ERROR_MESSAGE);
                    }
                });

                horizontalWrapper.addComponent(label);
                horizontalWrapper.addComponent(google);
                horizontalWrapper.setComponentAlignment(label, Alignment.MIDDLE_LEFT);
                horizontalWrapper.setComponentAlignment(google, Alignment.MIDDLE_CENTER);

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


