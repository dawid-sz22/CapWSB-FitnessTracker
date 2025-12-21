package pl.wsb.fitnesstracker.user.internal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.wsb.fitnesstracker.training.api.Training;
import pl.wsb.fitnesstracker.training.api.TrainingRepository;
import pl.wsb.fitnesstracker.user.api.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
class UserServiceImpl implements UserService, UserProvider {

    private final UserRepository userRepository;
    private final TrainingRepository trainingRepository;

    public void deleteUser(Long id){
        if (userRepository.existsById(id)) {
            List<Training> trainings = trainingRepository.findByUserId(id);
            trainingRepository.deleteAll(trainings);
            userRepository.deleteById(id);
        }else {
            throw new IllegalArgumentException("User with id " + id + " does not exist");
        }
    }

    @Override
    public User createUser(final User user) {
        log.info("Creating User {}", user);
        if (user.getId() != null) {
            throw new IllegalArgumentException("User has already DB ID, update is not permitted!");
        }
        return userRepository.save(user);
    }

    @Override
    public Optional<User> getUser(final Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public Optional<User> getUserByEmail(final String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> getUserByAge(final Integer age) {
        return userRepository.findByAge(age);
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User updateUser(Long id, String firstName) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFirstName(firstName);

        return userRepository.save(user);
    }

}