package pl.sinnotech.page.generator.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TemplateFile {

	private String name;
	private String content;

	public TemplateFile(String name, String content) {
		super();
		this.name = name;
		this.content = content;
	}

	public TemplateFile(File file) {
		try {
			File root = new File("").getAbsoluteFile();
			String rootDir = root + File.separator;

			this.content = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
			this.name = file.getAbsolutePath().substring(rootDir.length());
			this.name = getFileNameWithStandarizedPathSeparator(name);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getName() {
		return name;
	}

	public String getContent() {
		return content;
	}

	public void updateContent(String newContent) {
		this.content = newContent;
	}

	private String getFileNameWithStandarizedPathSeparator(String path) {
		String separator = "/";
		String wrongSeparator = "\\";
		path = path.replace(wrongSeparator, separator);
		return path;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TemplateFile other = (TemplateFile) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TemplateFile [name=" + name + "]: " + content;
	}

	public String getHTMLName(String rootDir) {
		return name.split("\\.")[0] + ".html";
	}

	public TemplateFile getCopy() {
		return new TemplateFile(name, new String(content));
	}

}
