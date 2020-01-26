package ru.mdimension.stand_bot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.mdimension.stand_bot.dto.User;
import ru.mdimension.stand_bot.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

import static ru.mdimension.stand_bot.Util.createUserDTO;

@Service
public class UserService {

    private final UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    private void save(User user) {
        repository.save(user);
    }

    public List<User> getAll() {
        return repository.findAll();
    }

    public List<User> getAllWithoutAuthor(long chatId) {
        return repository.findAll().stream()
                .filter(user -> user.getUserId() != chatId)
                .collect(Collectors.toList());
    }

    public void saveUserIfNotExist(Update update) {
        User one = repository.findByUserId(update.getMessage().getChatId());
        if (one == null) {
            one = createUserDTO(update);
            save(one);
        }
    }

    public void saveUserIfNotExist(CallbackQuery callbackQuery) {
        User one = repository.findByUserId(callbackQuery.getMessage().getChatId());
        if (one == null) {
            one = createUserDTO(callbackQuery);
            save(one);
        }
    }

    public String getRole(User user) {
        return repository.findByUserId(user.getUserId()).getRole();
    }

    public void setAdmin(String userId) {
        User user = repository.findByUserId(Long.valueOf(userId));
        user.setRole("ADMIN");
        repository.save(user);
    }

    public List<User> getAdmins() {
        return repository.getByRole("ADMIN");
    }

    public List<User> getUsers() {
        return repository.getByRole("USER");
    }

    public void removeAdmin(String userId) {
        User one = repository.findByUserId(Long.valueOf(userId));
        one.setRole("USER");
        repository.save(one);
    }

}
