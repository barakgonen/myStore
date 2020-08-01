package org.barak.myStore.common;

public enum MethodToExecute {
	ADD(Constants.ADD),
	DELETE(Constants.DELETE),
	QUERY(Constants.QUERY),
	STOP_CONNECTION(Constants.STOP_CONNECTION),
	UKNOWN(Constants.UKNOWN_COMMAND);

	public final String label;
	private MethodToExecute(String string) {
		this.label = string;
	}
}
