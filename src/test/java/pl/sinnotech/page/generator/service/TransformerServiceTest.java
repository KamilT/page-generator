package pl.sinnotech.page.generator.service;

import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import pl.sinnotech.page.generator.exception.ProcessingException;
import pl.sinnotech.page.generator.model.ProjectContext;
import pl.sinnotech.page.generator.model.TemplateFile;

public class TransformerServiceTest {

    @Test
    public void shouldTransformFiles() throws ProcessingException {
        TransformerService service = new TransformerService();

        List<TemplateFile> includeFiles = new ArrayList<>();
        TemplateFile start = new TemplateFile("start.inc", "start ##Include:test/test.inc##");
        TemplateFile test = new TemplateFile("test/test.inc", "hello form test");
        includeFiles.add(test);
        includeFiles.add(start);

        List<TemplateFile> templateFiles = new ArrayList<>();
        TemplateFile template = new TemplateFile("template.pg", "here goes content of inclue: ##Include:start.inc##");
        templateFiles.add(template);

        ProjectContext projectContext = new ProjectContext(includeFiles, templateFiles);
        service.transform(projectContext);

        TemplateFile templateFile = projectContext.getTemplateFileList().get(0);
        Assert.assertEquals("here goes content of inclue: start hello form test", templateFile.getContent());
    }

    @Test
    public void shouldTransformArgCommand() throws ProcessingException {
        TransformerService service = new TransformerService();

        List<TemplateFile> includeFiles = new ArrayList<>();
        TemplateFile start = new TemplateFile("inc.inc", "ARG: ##Arg:1##, ##Arg:2##, ##Arg:3##");
        includeFiles.add(start);

        List<TemplateFile> templateFiles = new ArrayList<>();
        TemplateFile template = new TemplateFile("template.pg", "Included=##Include:inc.inc,x,y##");
        templateFiles.add(template);

        ProjectContext projectContext = new ProjectContext(includeFiles, templateFiles);
        service.transform(projectContext);

        TemplateFile templateFile = projectContext.getTemplateFileList().get(0);
        Assert.assertEquals("Included=ARG: x, y, ", templateFile.getContent());
    }

    @Test
    public void shouldTransformIfCommand() throws ProcessingException {
        TransformerService service = new TransformerService();

        List<TemplateFile> includeFiles = new ArrayList<>();
        TemplateFile start = new TemplateFile("inc.inc", "IF: ##If:1:Blue:IsBlue## ##If:1:Red:\"IsRed\"##");
        includeFiles.add(start);

        List<TemplateFile> templateFiles = new ArrayList<>();
        TemplateFile template = new TemplateFile("template.pg", "Included=##Include:inc.inc,Red##");
        templateFiles.add(template);

        ProjectContext projectContext = new ProjectContext(includeFiles, templateFiles);
        service.transform(projectContext);

        TemplateFile templateFile = projectContext.getTemplateFileList().get(0);
        Assert.assertEquals("Included=IF:  \"IsRed\"", templateFile.getContent());
    }

    @Test
    public void shouldTransform2PgFiles() throws ProcessingException {
        TransformerService service = new TransformerService();

        List<TemplateFile> includeFiles = new ArrayList<>();
        TemplateFile include1 = new TemplateFile("inc.inc", "##Include:inc2.inc,##Arg:1##,##Arg:2####");
        TemplateFile include2 = new TemplateFile("inc2.inc", "IF: ##If:1:Blue:IsBlue## ##If:1:Red:IsRed##");
        includeFiles.add(include1);
        includeFiles.add(include2);

        List<TemplateFile> templateFiles = new ArrayList<>();
        TemplateFile template1 = new TemplateFile("template1.pg", "Included=##Include:inc.inc,Red##");
        TemplateFile template2 = new TemplateFile("template2.pg", "Included=##Include:inc.inc,Blue##");
        templateFiles.add(template1);
        templateFiles.add(template2);

        ProjectContext projectContext = new ProjectContext(includeFiles, templateFiles);
        service.transform(projectContext);

        TemplateFile templateFile1 = projectContext.getTemplateFileList().get(0);
        Assert.assertEquals("Included=IF:  IsRed", templateFile1.getContent());

        TemplateFile templateFile2 = projectContext.getTemplateFileList().get(1);
        Assert.assertEquals("Included=IF: IsBlue ", templateFile2.getContent());
    }
}
