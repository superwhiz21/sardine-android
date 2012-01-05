package cn.lzu.edu.webdav;

import org.simpleframework.xml.Element;

public class Propstat {
	
	@Element
	private String status;
	@Element
	private Prop prop;

	public Prop getProp() {
		return prop;
	}

	public String getStatus() {
		return status;
	}

}
