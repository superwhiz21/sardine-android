package cn.lzu.edu.webdav;

import java.util.List;

import org.simpleframework.xml.Element;

public class Response {
	@Element
	private List<String> href;
	@Element Propstat propstat;

	public Propstat getPropstat() {
		return propstat;
	}

	public List<String> getHref() {
		return href;
	}

}
