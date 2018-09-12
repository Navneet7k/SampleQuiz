package navneet.com.quizrest;

import com.google.firebase.database.IgnoreExtraProperties;

// [START blog_user_class]
@IgnoreExtraProperties
public class User {

    public String username;
    public String email;
    public String highScore;
    public String totalCorrect;
    public String descOrder;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email,String highScore,String totalCorrect,String descOrder) {
        this.username = username;
        this.email = email;
        this.highScore = highScore;
        this.totalCorrect = totalCorrect;
        this.descOrder=descOrder;
    }

    public String getDescOrder() {
        return descOrder;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getHighScore() {
        return highScore;
    }

    public String getTotalCorrect() {
        return totalCorrect;
    }
}
// [END blog_user_class]
