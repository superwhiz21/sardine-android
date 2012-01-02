package cn.lzu.edu.webdav;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class Example {

	@Element
	private String text;

	@Attribute
	private int index;

	public Example() {
		super();
	}

	public Example(String text, int index) {
		this.text = text;
		this.index = index;
	}

	public String getMessage() {
		return text;
	}

	public int getId() {
		return index;
	}
}