package pl.sinnotech.page.generator.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ProjectContext {
	private final List<TemplateFile> includeFileList;
	private final List<TemplateFile> templateFileList;

	public ProjectContext(List<TemplateFile> includeFileList, List<TemplateFile> templateFileList) {
		super();
		this.includeFileList = includeFileList;
		this.templateFileList = templateFileList;
	}

	public List<TemplateFile> getIncludeFileList() {
		return includeFileList;
	}

	public List<TemplateFile> getTemplateFileList() {
		return templateFileList;
	}

	public Collection<TemplateFile> getCopyOfIncludeFileList() {
		List<TemplateFile> includeFileListCopy = new ArrayList<>();
		for (TemplateFile includeFile : includeFileList) {
			includeFileListCopy.add(includeFile.getCopy());
		}
		return includeFileListCopy;
	}
}
