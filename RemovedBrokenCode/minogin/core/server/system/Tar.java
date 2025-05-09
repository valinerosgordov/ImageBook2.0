package ru.minogin.core.server.system;

public class Tar extends ExternalTool {
	private final String tarPath;

	public Tar(String tarPath) {
		this.tarPath = tarPath;
	}

	public void tarFolder(String folderPath, String archivePath) {
		String command = tarPath + " -czpf " + archivePath + " " + folderPath;
		createExec().execute(command);
	}
}
