package ecp.client.util;

import lombok.experimental.UtilityClass;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

@UtilityClass
public class ConnectionFactory {
    public static Session getConnectionSession(String username, String password) {
        // Настройки подключения к почтовому серверу
        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "imaps");
        props.put("mail.imap.host", "imap.yandex.ru");
        props.put("mail.imap.port", "993");
        props.put("mail.imap.ssl.enable", "true");
        props.put("mail.imap.ssl.protocols", "TLSv1.2 TLSv1.3");

        props.setProperty("mail.imap.ssl.checkserveridentity", "true");
        props.setProperty("mail.imap.ssl.trust", "imap.yandex.ru");
        props.setProperty("mail.imap.ssl.enable", "true");

        // Создание сессии
        return Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }
}
