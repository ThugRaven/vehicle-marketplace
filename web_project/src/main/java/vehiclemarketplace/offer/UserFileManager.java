package vehiclemarketplace.offer;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.SessionScoped;

@SessionScoped
public class UserFileManager implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Initialization of list.
	 */
	@PostConstruct
	public void init() {
		unconfirmedUploadedFiles = new ArrayList<>();
	}

	/**
	 * Adding the unconfirmed file to the list.
	 * 
	 * @param unconfirmedUploadedFile unconfirmed file.
	 */
	public void addUnconfirmedUploadedFile(File unconfirmedUploadedFile) {
		unconfirmedUploadedFiles.add(unconfirmedUploadedFile);
	}

	/**
	 * Deleting the confirmed file from the list.
	 * 
	 * @param confirmedUploadedFile confirmed file.
	 */
	public void confirmUploadedFile(File confirmedUploadedFile) {
		unconfirmedUploadedFiles.remove(confirmedUploadedFile);
	}

	/**
	 * Deleting unconfirmed files from the disk if the session is expired.
	 */
	@PreDestroy
	public void destroy() {
		for (File unconfirmedUploadedFile : unconfirmedUploadedFiles) {
			unconfirmedUploadedFile.delete();
		}
	}

	// The list which stores the unconfirmed files.
	private List<File> unconfirmedUploadedFiles;
}