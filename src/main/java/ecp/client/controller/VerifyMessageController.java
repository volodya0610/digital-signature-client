package ecp.client.controller;

import ecp.client.sevice.EmailReceiver;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class VerifyMessageController {
    private final EmailReceiver emailService;

    @PostMapping(path = "/verify", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> verifyMessage(@RequestParam String username, @RequestParam String password) throws Exception {
        var result = emailService.receiveMessage(username, password);
        return ResponseEntity.ok(result);
    }
}
