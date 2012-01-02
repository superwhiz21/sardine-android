package cn.lzu.edu.webdav;

import org.simpleframework.xml.Element;

public class Response {
	@Element
	private String href;

	public String toString() {
		return href;
	}

}
