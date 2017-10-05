package pl.sinnotech.page.generator;

import java.io.File;
import pl.sinnotech.page.generator.exception.ProcessingException;
import pl.sinnotech.page.generator.model.ProjectContext;
import pl.sinnotech.page.generator.service.ProjectContextLoaderService;
import pl.sinnotech.page.generator.service.TransformerService;
import pl.sinnotech.page.generator.service.WriterService;

public class Main {

    public static void main(String[] args) {
        if (args.length == 0) {
            try {
                System.out.println("Started...");
                ProjectContextLoaderService projectContextLoaderService = new ProjectContextLoaderService();
                ProjectContext projectContext = projectContextLoaderService
                        .loadFromRootFolder(new File("").getAbsolutePath());

                System.out.println("Transforming...");

                TransformerService transformerService = new TransformerService();
                transformerService.transform(projectContext);

                System.out.println("Transformed...");

                WriterService writerService = new WriterService();
                writerService.write(projectContext.getTemplateFileList());

                System.out.println("Done");
            } catch (ProcessingException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Template extension: .pg");
            System.out.println("Include extension: .inc");
            System.out.println("");
            System.out.println("Commands:");
            System.out.println("##Include:includes\\example.inc,arg1,arg2##");
            System.out.println("Includes example.inc into processed file, args are optional");
            System.out.println("");
            System.out.println("##Arg:1##");
            System.out.println("Includes content of arg number 1 (could be :2, :3..) ");
            System.out.println("");
            System.out.println("##If:1:Blue:IsBlue##");
            System.out.println("If arg 1 content is 'Blue', incdlue 'IsBlue'");
        }
    }

}
