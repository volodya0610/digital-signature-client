package ecp.client.sevice;

import ecp.client.util.ConnectionFactory;
import ecp.client.util.MessageFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;

@Service
@RequiredArgsConstructor
public class EmailReceiver {
    private final CipherService cipherService;

    public String receiveMessage(String username, String password) throws Exception {
        try {
            Session session = ConnectionFactory.getConnectionSession(username, password);
            Store store = session.getStore("imap");
            store.connect();
            // Получение папки INBOX
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            int messageCount = inbox.getMessageCount();
            //Получение последнего письмо
            Message message = inbox.getMessage(messageCount);

            // Вывод содержимого письма
            System.out.println("Subject: " + message.getSubject());
            System.out.println("From: " + message.getFrom()[0].toString());
            System.out.println("Date: " + message.getSentDate().toString());
            var digitalSignature = MessageFactory.getDigitalSignature(message);
            System.out.println("DigitalSignature: " + digitalSignature);
            var file = MessageFactory.getFile(message.getContent());
            // Закрытие соединения с почтовым сервером
            inbox.close(false);
            store.close();
            //Проверка подписи
            return cipherService.verifyFile(file, digitalSignature);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}

