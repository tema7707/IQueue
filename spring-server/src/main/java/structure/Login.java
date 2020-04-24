package structure;

public class Login {
    public static boolean isCorrect(String login, String password){
        return User.users.get(login).getPassword().equals(password);
    }
}
