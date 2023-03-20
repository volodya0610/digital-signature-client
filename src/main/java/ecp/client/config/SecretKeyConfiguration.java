package ecp.client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@Configuration
public class SecretKeyConfiguration {
    // Чтение ключей
    @Bean
    public KeyPair keyPair() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        // Загрузка содержимого файлов с открытым и закрытым ключами в строки
        String keyFolder = "C:/Users/Volodya/ecp-server-api/src/main/resources/keys/";
        String publicKeyContent = new String(Files.readAllBytes(Paths.get(keyFolder + "public.key")));
        String privateKeyContent = new String(Files.readAllBytes(Paths.get(keyFolder + "private.key")));
        // Преобразование строк в массивы байтов
        byte[] publicKeyBytes = publicKeyContent.getBytes();
        byte[] privateKeyBytes = privateKeyContent.getBytes();

        // Создание экземпляров класса PublicKey и PrivateKey из массивов байтов
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyBytes));
        PrivateKey privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));


        // Создание экземпляра класса KeyPair из созданных экземпляров PublicKey и PrivateKey
        return new KeyPair(publicKey, privateKey);
    }


    // Создание объекта для подписи сообщения
    @Bean
    public Signature signature() throws NoSuchAlgorithmException {
        return Signature.getInstance("SHA256withRSA");
    }

}
