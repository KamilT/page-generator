package pl.sinnotech.page.generator.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import pl.sinnotech.page.generator.model.TemplateFile;

public class WriterService {

    public void write(List<TemplateFile> templateFileList) {
        String rootDir = new File("").getAbsolutePath() + File.separator;
        String outputPath = rootDir + "output";

        File directory = new File(outputPath);
        if (!directory.exists()) {
            directory.mkdir();
        } else {
            purgeDirectoryButKeepSubDirectories(directory);
        }

        templateFileList.stream().forEach(item -> {
            File file = new File(outputPath + File.separator + item.getHTMLName(rootDir));
            try {
                FileWriter fw = new FileWriter(file.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(item.getContent());
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(-1);
            }
        });
    }

    void purgeDirectoryButKeepSubDirectories(File dir) {
        for (File file : dir.listFiles()) {
            if (!file.isDirectory()) {
                file.delete();
            }
        }
    }
}
