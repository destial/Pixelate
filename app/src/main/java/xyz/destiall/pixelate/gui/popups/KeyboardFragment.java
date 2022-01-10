package xyz.destiall.pixelate.gui.popups;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import xyz.destiall.pixelate.Pixelate;
import xyz.destiall.pixelate.events.controls.EventChat;

public class KeyboardFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Send", (dialog, which) -> Pixelate.HANDLER.call(new EventChat(input.getText().toString())));
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        return builder.create();
    }
}
