package ecp.client.util;

import lombok.experimental.UtilityClass;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@UtilityClass
public class MessageFactory {
    public static String getDigitalSignatureMessage(Message message) throws MessagingException, IOException {
        if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            return getTextFromMimeMultipart(mimeMultipart);
        }
        return "";
    }

    public static String getDigitalSignature(Message message) throws MessagingException, IOException {
        var text = getDigitalSignatureMessage(message);
        var digitalSignature = text.replace("Digital signature: ", "");
        digitalSignature = digitalSignature.trim();
        return digitalSignature;
    }



    public static byte[] getFile(Object content) throws MessagingException, IOException {
        if (content instanceof Multipart) {
            Multipart multipart = (Multipart) content;
            for (int i = 0; i < multipart.getCount(); i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                if (bodyPart instanceof MimeBodyPart) {
                    MimeBodyPart mimeBodyPart = (MimeBodyPart) bodyPart;
                    String fileName = mimeBodyPart.getFileName();
                    if (fileName != null) {
                        // Считывание прикрепленного файла
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        mimeBodyPart.getDataHandler().writeTo(baos);
                        return baos.toByteArray();
                    }
                }
            }
        } else if (content instanceof String) {
            System.out.println("Text: " + content);
            return null;
        }
        return null;
    }

    private static String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws MessagingException, IOException {
        String result = "";
        for (int i = 0; i < mimeMultipart.getCount(); i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                return result + "\n" + bodyPart.getContent(); // without return, same text appears twice in my tests
            }
            result += parseBodyPart(bodyPart);
            if(result.contains("signature")){
                return result;
            }
        }
        return result;
    }

    private static String parseBodyPart(BodyPart bodyPart) throws MessagingException, IOException {
        if (bodyPart.getContent() instanceof MimeMultipart) {
            return getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent());
        }
        return "";
    }
}
