
import java.io.File;
import java.io.IOException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Pedro Gallego Morales
 */
public class ProcessBuilderRedirectIOEx {

    public static void main(String[] args) throws IOException {

        var processBuilder = new ProcessBuilder();

        File fileInput = new File("src/resources", "input.txt");
        fileInput.getParentFile().mkdirs(); // Will create parent directories if not exists
        fileInput.createNewFile();
        
            File fileOutput = new File("src/resources/", "output.txt");
      fileOutput.getParentFile().mkdirs(); // Will create parent directories if not exists
       fileOutput.createNewFile();
        
        

        processBuilder.command("type")
                .redirectInput(fileInput)
                .redirectOutput(fileOutput)
                .start();
    }
}
