package com.dk.ui.widgets;

import java.io.File;

public abstract interface FolderSelectorListener {
	public abstract void onFolderClicked(File folder);
	public abstract void onFolderChecked(File folder, boolean checked);
}
