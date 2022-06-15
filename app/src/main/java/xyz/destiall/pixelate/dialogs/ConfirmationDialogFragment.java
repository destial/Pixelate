package xyz.destiall.pixelate.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import androidx.fragment.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import xyz.destiall.pixelate.Pixelate;
import xyz.destiall.pixelate.events.controls.EventDialogueAction;
import xyz.destiall.pixelate.utils.StringUtils;

public class ConfirmationDialogFragment extends DialogFragment  {
    public static boolean IsShown = false;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        IsShown = true;
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(StringUtils.CONFIRM_ACTION)
                .setPositiveButton(StringUtils.YES, (dialog, id) -> {
                    // User triggered pause
                    EventDialogueAction action = new EventDialogueAction(EventDialogueAction.Dialog_Action.YES);
                    Pixelate.HANDLER.call(action);
                    IsShown = false;
                })
                .setNegativeButton(StringUtils.NO, (dialog, id) -> {
                    // User cancelled the pause
                    EventDialogueAction action = new EventDialogueAction(EventDialogueAction.Dialog_Action.NO);
                    Pixelate.HANDLER.call(action);
                    IsShown = false;
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
