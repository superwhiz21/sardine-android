package cn.lzu.edu.webdav;

import org.simpleframework.xml.Element;

public class Prop {
	
	@Element
	private String creationdate;
	@Element
	private String getlastmodified;
	@Element
	private String getetag;
	@Element (required = false)
	private String getcontenttype;

	public String getCreationdate() {
		return creationdate;
	}

	public String getGetlastmodified() {
		return getlastmodified;
	}

	public String getGetetag() {
		return getetag;
	}

	public String getGetcontenttype() {
		return getcontenttype;
	}

}
