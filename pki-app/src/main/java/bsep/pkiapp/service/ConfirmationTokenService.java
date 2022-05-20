package bsep.pkiapp.service;

import bsep.pkiapp.model.ConfirmationToken;
import bsep.pkiapp.model.ConfirmationTokenType;
import bsep.pkiapp.repository.ConfirmationTokenRepository;
import bsep.pkiapp.security.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfirmationTokenService {

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    private TokenUtils tokenUtils;

    public ConfirmationToken saveToken(ConfirmationToken confirmationToken) {
        return confirmationTokenRepository.save(confirmationToken);
    }

    public ConfirmationToken findByToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }

    public void deleteToken(ConfirmationToken confirmationToken) {
        confirmationTokenRepository.delete(confirmationToken);
    }

    public String generateConfirmationToken(String email, ConfirmationTokenType tokenType) {
        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.setEmail(email);
        confirmationToken.setToken(tokenUtils.generateToken(email, "ROLE_USER", null));
        confirmationToken.setTokenType(tokenType);
        saveToken(confirmationToken);
        return confirmationToken.getToken();
    }
}
