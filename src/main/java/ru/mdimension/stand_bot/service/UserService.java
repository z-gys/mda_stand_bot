package ru.mdimension.stand_bot.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.mdimension.stand_bot.dto.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static ru.mdimension.stand_bot.Util.createUserDTO;

@Service
public class UserService {

    private final Map<Long, User> repository = new ConcurrentHashMap<>();

    private void save(User user) {
        repository.put(user.getUserId(), user);
    }

    public List<User> getAll() {
        return new ArrayList<>(repository.values());
    }

    public List<User> getAllWithoutAuthor(long chatId) {
        return repository.values().stream()
                .filter(user -> user.getUserId() != chatId)
                .collect(Collectors.toList());
    }

    public void saveUserIfNotExist(Update update) {
        User one = repository.get(update.getMessage().getChatId());
        if (one == null) {
            one = createUserDTO(update);
            save(one);
        }
    }

    public void saveUserIfNotExist(CallbackQuery callbackQuery) {
        User one = repository.get(callbackQuery.getMessage().getChatId());
        if (one == null) {
            one = createUserDTO(callbackQuery);
            save(one);
        }
    }

    public String getRole(User user) {
        return repository.get(user.getUserId()).getRole();
    }

    public void setAdmin(String userId) {
        User user = repository.get(Long.valueOf(userId));
        user.setRole("ADMIN");
        repository.put(user.getUserId(), user);
    }
}
