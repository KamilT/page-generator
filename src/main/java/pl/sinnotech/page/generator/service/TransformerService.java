package pl.sinnotech.page.generator.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import pl.sinnotech.page.generator.exception.ProcessingException;
import pl.sinnotech.page.generator.model.ProjectContext;
import pl.sinnotech.page.generator.model.TemplateFile;

public class TransformerService {

    private static final String INCLUDE_COMMAND = "##Include:((\\S)*)##";
    private static final String ARG_COMMAND = "##Arg:(\\d*)##";
    private static final String IF_COMMAND = "##If:(\\d*):((\\s|\\w|[\"!@$%^&*()_+=-])*):((\\s|\\w|[\"!@$%^&*()_+=-])*)##";

    public void transform(ProjectContext projectContext) throws ProcessingException {
        // go through template files
        for (TemplateFile templateFile : projectContext.getTemplateFileList()) {
            Collection<TemplateFile> includeFileList = projectContext.getCopyOfIncludeFileList();

            Map<String, TemplateFile> includefilesMap = new HashMap<>();
            includeFileList.stream().forEach(item -> includefilesMap.put(item.getName(), item));

            while (hasMoreCommands(templateFile)) {
                processIncludeCommand(templateFile, includefilesMap);
            }
            System.out.println("Transformed: " + templateFile.getName());
        }
    }

    private void processIncludeCommand(TemplateFile includeFile, Map<String, TemplateFile> includefilesMap)
            throws ProcessingException {
        Pattern pattern = Pattern.compile(INCLUDE_COMMAND);
        Matcher matcher = pattern.matcher(includeFile.getContent());
        if (matcher.find()) {
            String fileToInclude = matcher.group(1);
            String[] includeCommand = fileToInclude.split(",");
            TemplateFile includeThis = includefilesMap.get(includeCommand[0]);
            if (includeThis != null) {
                String newContent = includeFile.getContent().replaceFirst(INCLUDE_COMMAND, includeThis.getContent());
                newContent = processContextualCommands(newContent, includeCommand, includeFile.getName());
                includeFile.updateContent(newContent);
            } else {
                throw new ProcessingException("Cannot find file: " + fileToInclude);
            }
        }
    }

    private String processContextualCommands(String content, String[] includeCommand, String fileName)
            throws ProcessingException {
        String newContent = processArgCommands(includeCommand, content, fileName);
        newContent = processIfCommands(includeCommand, newContent, fileName);
        return newContent;
    }

    private String processIfCommands(String[] includeCommand, String content, String fileName)
            throws ProcessingException {
        Pattern ifPattern = Pattern.compile(IF_COMMAND);

        boolean hasIfCommands = true;
        while (hasIfCommands) {
            hasIfCommands = false;
            Matcher matcher = ifPattern.matcher(content);
            if (matcher.find()) {
                hasIfCommands = true;
                int commandIndex = Integer.parseInt(matcher.group(1));
                if (commandIndex < 1) {
                    commandIndex = 1;
                }
                String value = matcher.group(2);
                String replacer = matcher.group(4);
                if (includeCommand.length > commandIndex) {
                    if (value.equals(includeCommand[commandIndex])) {
                        content = content.replaceFirst(IF_COMMAND, replacer);
                    } else {
                        content = content.replaceFirst(IF_COMMAND, "");
                    }
                } else {
                    content = content.replaceFirst(IF_COMMAND, "");
                }
            }
        }
        return content;
    }

    private String processArgCommands(String[] includeCommand, String content, String fileName) {
        Pattern argPattern = Pattern.compile(ARG_COMMAND);

        boolean hasArgCommands = true;
        while (hasArgCommands) {
            hasArgCommands = false;
            Matcher matcher = argPattern.matcher(content);
            if (matcher.find()) {
                hasArgCommands = true;
                int commandIndex = Integer.parseInt(matcher.group(1));
                if (commandIndex < 1) {
                    commandIndex = 1;
                }
                if (includeCommand.length > commandIndex) {
                    content = content.replaceFirst(ARG_COMMAND, includeCommand[commandIndex]);
                } else {
                    content = content.replaceFirst(ARG_COMMAND, "");
                }
            }
        }
        return content;
    }

    private boolean hasMoreCommands(TemplateFile includeFile) {
        Pattern pattern = Pattern.compile(INCLUDE_COMMAND);
        Matcher matcher = pattern.matcher(includeFile.getContent());
        return matcher.find();
    }
}
