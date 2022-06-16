package bsep.pkiapp.service;

import bsep.pkiapp.model.ConfirmationToken;
import bsep.pkiapp.model.ConfirmationTokenType;
import bsep.pkiapp.repository.ConfirmationTokenRepository;
import bsep.pkiapp.security.util.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ConfirmationTokenService {

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ConfirmationToken saveToken(ConfirmationToken confirmationToken) {
        return confirmationTokenRepository.save(confirmationToken);
    }

    public ConfirmationToken findByToken(String token) {
        for (ConfirmationToken confToken : confirmationTokenRepository.findAll()) {
            if (passwordEncoder.matches(token.substring(token.length() - 10), confToken.getToken()))
                return confToken;
        }
        return null;
    }

    public void deleteToken(ConfirmationToken confirmationToken) {
        confirmationTokenRepository.delete(confirmationToken);
    }

    public ConfirmationToken generateConfirmationToken(String email, ConfirmationTokenType tokenType) {
        log.debug("GCT with TP: [{}] for U: [{}]", tokenType.toString(), email);
        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.setEmail(email);
        confirmationToken.setToken(tokenUtils.generateToken(email, "ROLE_USER", null));
        confirmationToken.setTokenType(tokenType);
        saveToken(confirmationToken);
        return confirmationToken;
    }

    public void encodeToken(ConfirmationToken token) {
        log.debug("ET: {}", token);
        token.setToken(passwordEncoder.encode(token.getToken().substring(token.getToken().length() - 10)));
        saveToken(token);
    }
}
