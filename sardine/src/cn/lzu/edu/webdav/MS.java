package cn.lzu.edu.webdav;

import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name = "multistatus")
public class MS {

	@ElementList(inline = true)
	private List<Response> responses;

	public List<Response> getResponse() {
		return responses;
	}
}
