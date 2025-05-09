package ru.minogin.core.server.system;

public class MySQLDump extends ExternalTool {
	private String mySQLDumpPath;

	public MySQLDump(String mySQLDumpPath) {
		this.mySQLDumpPath = mySQLDumpPath;
	}

	public void dump(String user, String password, String databaseName, String path) {
		createExec().execute(
				mySQLDumpPath + " -u" + user + " -p" + password + " -r" + path + " " + databaseName);
	}
}
