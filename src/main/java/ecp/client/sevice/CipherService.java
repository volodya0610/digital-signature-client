package ecp.client.sevice;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class CipherService {
    public final KeyPair keyPair;
    public final Signature signature;

    public String verifyFile(byte[] file, String clientSignature) throws SignatureException, InvalidKeyException, IOException {
        try {
            byte[] digitalSignature = Base64.getDecoder().decode(clientSignature);
            signature.initSign(keyPair.getPrivate());
            // Проверка подписи
            signature.initVerify(keyPair.getPublic());
            signature.update(file);
            boolean verified = signature.verify(digitalSignature);
            return "Signature Verified: " + verified;
        }
        catch (Exception e){
            return "Signature Verified: " + false;
        }
    }

}
