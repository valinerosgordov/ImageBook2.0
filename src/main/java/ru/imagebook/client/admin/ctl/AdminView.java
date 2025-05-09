package ru.imagebook.client.admin.ctl;

public interface AdminView {
	void permissionDenied();

	void infoBackupComplete();

	void infoUpdateComplete();

	void infoCleanComplete();
}
