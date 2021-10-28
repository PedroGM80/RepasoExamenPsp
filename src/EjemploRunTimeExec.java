
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Pedro Gallego Morales
 */
public class EjemploRunTimeExec {
    public static void main(String[] args) throws IOException {
       


//The next example, launch CMD.EXE, grab stdin/stdout and push to stdin command to be interpreted by the shell. 

    String line;
    OutputStream stdin = null;
    InputStream stderr = null;
    InputStream stdout = null;
       Process process = Runtime.getRuntime().exec("C:\\Windows\\System32\\notepad.exe");
      // launch EXE and grab stdin/stdout and stderr
     // Process process = Runtime.getRuntime ().exec ("/folder/exec.exe");
      stdin = process.getOutputStream ();
      stderr = process.getErrorStream ();
      stdout = process.getInputStream ();

      // "write" the parms into stdin
      line = "param1" + "\n";
      stdin.write(line.getBytes() );
      stdin.flush();

      line = "param2" + "\n";
      stdin.write(line.getBytes() );
      stdin.flush();

      line = "param3" + "\n";
      stdin.write(line.getBytes() );
      stdin.flush();

      stdin.close();

      // clean up if any output in stdout
      BufferedReader brCleanUp =
        new BufferedReader (new InputStreamReader (stdout));
      while ((line = brCleanUp.readLine ()) != null) {
        System.out.println ("[Stdout] " + line);
      }
      brCleanUp.close();

      // clean up if any output in stderr
      brCleanUp =
        new BufferedReader (new InputStreamReader (stderr));
      while ((line = brCleanUp.readLine ()) != null) {
        System.out.println ("[Stderr] " + line);
      }
      brCleanUp.close();


    }
}
