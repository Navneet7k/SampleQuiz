package navneet.com.quizrest;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

/**
 * Created by user on 07-Dec-2017.
 */

public class MyDialogFragment extends DialogFragment
{
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        return new AlertDialog.Builder(getActivity())
                .setTitle("Dialog Title")
                .setPositiveButton("OK", null)
                .create();
    }
}
