package com.aeternity.aecan.models.dynamic;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

public interface DynamicDialog {

    DialogFragment getFragment(Fragment parent);

}
