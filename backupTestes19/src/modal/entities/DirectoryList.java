package modal.entities;

import java.io.Serializable;

public class DirectoryList implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;

	public DirectoryList() {
	}

	public DirectoryList(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Directory [ name=" + name + "]";
	}
}
