package org.frogpeak.horn;

import java.awt.*;
import java.io.File;
import java.io.FilenameFilter;

/**
 * Convenient File dialogs.
 * @author Phil Burk (C) 2003
 */

class FileChooser {

	/** If browse button hit, open a LoadFile dialog then put file name into text field.
	 */
	public static File browseLoad(
		Frame frame,
		String message,
		File defaultFile,
		String pSuffix) {

		final String suffix = pSuffix;
		FileDialog fileDlg = new FileDialog(frame, message, FileDialog.LOAD);

		/* Filter out any files that do not end with the given suffix. */
		if (suffix != null) {
			fileDlg.setFilenameFilter(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.endsWith(suffix);
				}
			});
		}

		if (defaultFile != null) {
			fileDlg.setFile(defaultFile.getAbsolutePath());
		}
		fileDlg.show();
		String dirName = fileDlg.getDirectory();
		String fileName = fileDlg.getFile();
		if ((fileName != null) && (dirName != null)) {
			fileName = dirName + fileName;
			return new File(fileName);
		}
		else return null;
	}

	public static File browseSave(
		Frame frame,
		String message,
		File defaultFile) {
		FileDialog fileDlg = new FileDialog(frame, message, FileDialog.SAVE);
		if (defaultFile != null) {
			fileDlg.setFile(defaultFile.getAbsolutePath());
		}
		fileDlg.show();
		String dirName = fileDlg.getDirectory();
		String fileName = fileDlg.getFile();
		if ((fileName != null) && (dirName != null)) {
			fileName = dirName + fileName;
		}
		return new File(fileName);
	}

}
