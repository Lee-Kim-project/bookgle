package bookgle.bookgle.user.service;

import bookgle.bookgle.dto.UserRegisterDto;
import bookgle.bookgle.exception.ExceptionStatus;
import bookgle.bookgle.exception.ServiceException;
import bookgle.bookgle.user.domain.User;
import bookgle.bookgle.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor한
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void registerUser(UserRegisterDto userRegisterDto) throws ServiceException {
        String username = userRegisterDto.getUsername();
        String password = userRegisterDto.getPassword();
        String displayName = userRegisterDto.getDisplayName();

        // 이미 존재하는 유저인지 확인
        if (userRepository.existsByUsername(username)) {
            throw new ServiceException(ExceptionStatus.ALREADY_REGISTERED_USER);
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setDisplayName(displayName);
        // 각 권한에 맞게 설정 (일단은 모두 일반 유저)
        user.setRole("ROLE_USER");

        userRepository.save(user);
    }
}
