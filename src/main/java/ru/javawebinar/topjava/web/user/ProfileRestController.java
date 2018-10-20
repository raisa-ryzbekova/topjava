package ru.javawebinar.topjava.web.user;

import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.User;

import static ru.javawebinar.topjava.web.SecurityUtil.userId;

@Controller
public class ProfileRestController extends AbstractUserController {

    public User get() {
        return super.get(userId);
    }

    public void delete() {
        super.delete(userId);
    }

    public void update(User user) {
        super.update(user, userId);
    }
}