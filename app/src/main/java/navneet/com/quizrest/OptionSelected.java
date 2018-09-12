package navneet.com.quizrest;

/**
 * Created by user on 07-Dec-2017.
 */

public interface OptionSelected {
    void onOptionCorrect();
    void onOptionWrong();
    void onSectionComplete();
}
