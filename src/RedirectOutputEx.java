/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Pedro Gallego Morales
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class RedirectOutputEx {

    public static void main(String[] args) throws IOException {

        var homeDir = System.getProperty("user.home");

        var processBuilder = new ProcessBuilder();

        processBuilder.command("cmd.exe", "/c", "date /t");//comando de fecha de windows

        var fileName = new File(String.format("%s" + File.separator + "Documents" + File.separator + "tmp" + File.separator + "output.txt", homeDir));

        processBuilder.redirectOutput(fileName);//The program redirects the builder's output to a file. It runs the Windows date command.

        var process = processBuilder.start();
//We redirect the process builders standard output to a file.
        try (var reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()))) {

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }
    }
}
