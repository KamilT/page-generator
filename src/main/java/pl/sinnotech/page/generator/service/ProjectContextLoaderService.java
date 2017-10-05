package pl.sinnotech.page.generator.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import pl.sinnotech.page.generator.exception.ProcessingException;
import pl.sinnotech.page.generator.model.ProjectContext;
import pl.sinnotech.page.generator.model.TemplateFile;

public class ProjectContextLoaderService {

    public static final String PAGE_FILE_EXTENSION = ".pg";
    public static final String INCLUDE_FILE_EXTENSION = ".inc";

    public ProjectContext loadFromRootFolder(String rootFolder) throws ProcessingException {
        File folder = new File(rootFolder);
        File[] listOfFiles = folder.listFiles();

        List<TemplateFile> includeFileList = new ArrayList<>();
        List<TemplateFile> templateFileList = new ArrayList<>();
        Queue<File> folderQueue = new LinkedBlockingQueue<>();

        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile()) {
                if (listOfFile.getName().endsWith(INCLUDE_FILE_EXTENSION)) {
                    includeFileList.add(new TemplateFile(listOfFile));
                    System.out.println("Included: " + includeFileList.get(includeFileList.size() - 1).getName());
                } else if (listOfFile.getName().endsWith(PAGE_FILE_EXTENSION)) {
                    templateFileList.add(new TemplateFile(listOfFile));
                    System.out.println("Included: " + templateFileList.get(templateFileList.size() - 1).getName());
                }
            } else if (listOfFile.isDirectory()) {
                folderQueue.add(listOfFile);
            }
        }

        while (folderQueue.isEmpty() == false) {
            listOfFiles = folderQueue.poll().listFiles();

            for (File listOfFile : listOfFiles) {
                if (listOfFile.isFile()) {
                    if (listOfFile.getName().endsWith(INCLUDE_FILE_EXTENSION)) {
                        includeFileList.add(new TemplateFile(listOfFile));
                        System.out.println("Included: " + includeFileList.get(includeFileList.size() - 1).getName());
                    } else if (listOfFile.getName().endsWith(PAGE_FILE_EXTENSION)) {
                        // templateFileList.add(new TemplateFile(listOfFiles[i]));
                        throw new ProcessingException("Page files can only be in root folder");
                    }
                } else if (listOfFile.isDirectory()) {
                    folderQueue.add(listOfFile);
                }
            }
        }
        System.out.println("Finished including...");
        return new ProjectContext(includeFileList, templateFileList);
    }
}
