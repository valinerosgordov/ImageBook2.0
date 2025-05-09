package ru.saasengine.client.model.workspace;

import ru.minogin.core.client.rpc.Transportable;

public class WorkspaceInfo implements Transportable {
	private static final long serialVersionUID = -2296853891488011580L;

	private String workspaceName;

	WorkspaceInfo() {}

	public WorkspaceInfo(String workspaceName) {
		this.workspaceName = workspaceName;
	}

	public String getWorkspaceName() {
		return workspaceName;
	}
}
