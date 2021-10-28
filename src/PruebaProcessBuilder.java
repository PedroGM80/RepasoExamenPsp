
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.TreeMap;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Pedro Gallego Morales
 */
public class PruebaProcessBuilder {

    static ArrayList<String> commandLine;
    static TreeMap<String, String> environmentParameters;
    static ProcessBuilder aProcessBuilder;
    static Process unProceso; //'Process' is an abstract class, but we can assign an instance of a subclass of 'Process'
    //(that it's just what the method 'aProcessBuilder.start()' from ProcessBuilder class returns)
    static ProcessBuilder.Redirect.Type redirectType;
    static ProcessBuilder.Redirect aRedirect;
    static OutputStream outputStreamToSubprocessInput;
    static InputStream inputStreamFromSubprocessOutput;
    static InputStream inputStreamFromSubprocessError;

    static Scanner scanner = new Scanner(System.in);

    private static class Menu {

        final static int MAXOPCION = 26; //It should be the ordinal of the last enum's value
        //Warning: The sequence of the values in the enum 'opcionesMenu' must be THE SAME as the integer values showed in the Menu

        enum opcionesMenu {
            newCommandLine, changeCommandLine, environmentParameters, parentProcessWorkingDirectory, workingDirectory, processBuilder, start, destroy, exitValue, waitFor,
            redirectType, redirectInput, redirectOutput, redirectError, isErrorStreamRedirected, setRedirectErrorStream, inheritIO, seeRedirectInput, seeRedirectOutput, seeRedirectError,
            getOutputStream, getInputStream, getInputStreamFromSubprocessError, escribirEnOutputStream,
            gc, end
        }
        static opcionesMenu[] arrayOpcionesMenu = opcionesMenu.values();

        private static opcionesMenu menu() {

            //Scanner cadena= new Scanner(System.in); 
            int opcion;

            do {
                System.out.println("________________________________________________________________________________________________________________________");
                System.out.println();
                System.out.println("                                     Let's interact with 'ProcessBuilder' objects");
                System.out.println();
                System.out.println("Write the number of the desired option and then press intro: \n");
                System.out.println();
                System.out.println("--------------------Define the characteristic of the system-dependent process that will be created---------------------");
                System.out.println("01-Write the command Line: [route\\]program file and the arguments that must be passed to the program");
                System.out.println("02-Don't create but simply change the command line of the actual 'ProcessBuilder' object");
                System.out.println("03-Set up enviroments parameters");
                System.out.println("04-Show the 'PruebaProcessBuilder' (the parent process)'s working directory");
                System.out.println("05-Set up the working directory");
                System.out.println("-------------------Create a instance of class 'Process' and interact with it--------------------------------");
                System.out.println("06-Create a new 'ProcessBuilder' object with the command line set in Menu option 01");
                System.out.println("07-start: create an instance of the class 'Process' and a new system-dependent process as setted by the 'ProcessBuilder' object");
                System.out.println("08-destroy: kill the system-dependent process; the object instance of class 'Process' is NOT destroyed");
                System.out.println("09-exitValue: get the exit code of the just finished system-dependent process");
                System.out.println("10-waitFor: Block this application until the previously created system-dependent process finish");
                System.out.println("----------------------------------Streams to/from the subprocess--------------------------------");
                System.out.println("11-Sets the type of the Redirect object");
                System.out.println("12-Sets the current process builder's standard input source (the Redirect object). Subprocesses subsequently started by the current object's start() method obtain their standard input from this source.");
                System.out.println("13-Sets the current process builder's standard output destination (the Redirect object). Subprocesses subsequently started by the current object's start() method send their standard output to this destination.");
                System.out.println("14-Sets the current process builder's standard error destination (the Redirect object). Subprocesses subsequently started by the current object's start() method send their standard error from this source.");
                System.out.println("15-isErrorStreamRedirected");
                System.out.println("16-redirectErrorStream");
                System.out.println("17-inheritIO");
                System.out.println("18-See the current Process Builder's Redirect Input");
                System.out.println("19-See the current Process Builder's Redirect Output");
                System.out.println("20-See the current Process Builder's Redirect Error");
                System.out.println("21-Get a 'outPutStream' connected to the process's input");
                System.out.println("22-Get a 'inPutStream' connected to the process's output");
                System.out.println("23-Get a 'inPutStream' connected to the process's error output");
                System.out.println("24-Escribir en el 'outPutStream' (se envian bytes al subproceso y luego se reciben bytes del subproceso')");
                System.out.println("-----------------------Ask to the virtual machine--------------------------------------------------");
                System.out.println("25-gc (garbage collector)");
                System.out.println("26-Quit");
                System.out.println("___________________________________________________________________________________________________________________________");
                System.out.println("");

                opcion = scanner.nextInt();

            } while (opcion < 1 || opcion > MAXOPCION);

            scanner.nextLine();//We consume all the characters left in the actual line (almost, it will be a 'carriage return' because we have read something yet)

            return arrayOpcionesMenu[opcion - 1]; //Warning: The sequence of the values of the enum 'opcionesMenu' must be THE SAME as the integer values showed in the Menu
        }

    }

    public static void main(String[] args) {

        Menu.opcionesMenu opcion;

        while ((opcion = Menu.menu()) != Menu.opcionesMenu.end) {

            switch (opcion) {

                case newCommandLine:
                case changeCommandLine:
                    String line;
                    // do{
                    System.out.println("Write the program file's route that contains the code to built a new process, plus the needed command line arguments");
                    line = scanner.nextLine();
                    // }while (line.equals("")); //We ensure that 'commandLine' is not empty, that is, not any reference to empty String

                    StringTokenizer st = new StringTokenizer(line);  //'line' is never null

                    if (opcion == Menu.opcionesMenu.newCommandLine) //CREATE a new ArrayList
                    {
                        commandLine = new ArrayList<String>(st.countTokens()); //enabling the do-while:'commandLine' has at least 1 String (the route of the program's file)
                    } else {
                        assert opcion == Menu.opcionesMenu.changeCommandLine;
                        try {
                            commandLine = (ArrayList<String>) aProcessBuilder.command(); //We DO NOT CREATE a new ArrayList, we just get the one of the existing 'aProcessBuilder' object
                            commandLine.clear(); //Changing this ArrayList (cleaning up, in this case), we change the state of the actual 'aProcessBuilder' object
                        } catch (NullPointerException npe) {
                            System.err.println("No 'ProcessBuilder' object has been created yet");
                            break;
                        }
                    }

                    while (st.hasMoreTokens()) {
                        commandLine.add(st.nextToken());
                    }

                    break;

                case environmentParameters:

                    //System.out.printf("The system properties are: %s\n", System.getProperties().toString());
                    //System.out.printf("The current process environment is: %s\n", System.getenv());
                    //System.out.printf("The environment variables are: %s\n", aProcessBuilder.environment());
                    System.out.println("Write the name of the environment parameter that you want change or the name of a new one");
                    System.out.println("Note: When passing information to a Java subprocess, 'system properties' (see 'System' class API) are generally preferred over environment variables.");
                    String envParamName = scanner.next();

                    // System.out.printf("The current value of the environment variable %s is %s\n", envParamName, aProcessBuilder.environment().get(envParamName));               
                    System.out.println("Write the value of the former environment parameter");
                    String envParamValue = scanner.next();

                    //We 'interpret' the String %Var% (Windows) or $(Var) (Linux) by its 'supposed' value
                    String oldEnvParamValue;

                    if (aProcessBuilder.environment().containsKey(envParamName)) {
                        oldEnvParamValue = aProcessBuilder.environment().get(envParamName).toString();
                    } else {
                        oldEnvParamValue = "";
                    }

                    if (System.getProperty("os.name").contains("Windows")) //In case of OS Windows
                    {
                        envParamValue = envParamValue.replace("%" + envParamName + "%", oldEnvParamValue);
                    } else if (System.getProperty("os.name").contains("Linux")) //In case of OS Linux
                    {
                        envParamValue = envParamValue.replace("$(" + envParamName + ")", oldEnvParamValue);
                    }
                    /*else (other OS: no change is made in the 'envParamValue' String*/

                    //We change the value of the environment variable
                    try {
                        aProcessBuilder.environment().put(envParamName, envParamValue);
                    } catch (NullPointerException npe) {
                        if (environmentParameters == null) {
                            System.err.println("There isn't settled a 'ProcessBuilder' object");
                        } else {
                            System.err.println("Attempting to insert or query the presence of a null key or value (in a Map)");
                        }
                    } catch (ClassCastException cce) {
                        System.err.println("Attempting to query the presence of a key or value which is not of type String");
                    } catch (UnsupportedOperationException uoe) {
                        System.err.println("The modification is not permitted by the operating system");
                    } catch (IllegalArgumentException iae) {
                        System.err.println("The modification is not permitted by the operating system");
                    }

                    System.out.printf("%s=%s\n", envParamName, aProcessBuilder.environment().get(envParamName));

                    break;

                case parentProcessWorkingDirectory: 
						                       try {
                    if (aProcessBuilder == null) {
                        throw new NullPointerException();
                    }
                } catch (NullPointerException npe) {
                    System.err.println("There isn't settled a 'ProcessBuilder' object");
                }

                try {
                    System.out.printf("The working directory of 'PruebaProcessBuilder' (the parent directory) is: %s\n",
                            aProcessBuilder.directory().getAbsolutePath());
                } catch (NullPointerException npe) {
                    System.out.printf("The working directory of 'PruebaProcessBuilder' (the parent directory) is"
                            + " the initial working directory: %s\n", System.getProperty("user.dir"));
                }

                break;

                case workingDirectory:
                    String directory;
                    System.out.println("Write the path to the working directory ('.' means the current directory)");
                    directory = scanner.nextLine();
                    try {
                        aProcessBuilder.directory(new File(directory)); //if the object 'File' were null, it will settled the working directory of the current java process (PruebaProcessBuilder)
                    } catch (NullPointerException npe) {
                        System.err.println("There isn't settled a 'ProcessBuilder' object");
                    }
                    break;

                case processBuilder:       try {
                    aProcessBuilder = new ProcessBuilder(commandLine);
                    System.out.printf("Created the 'ProcessBuilder' object of identity %s, ready to launch the system dependent command %s\n", aProcessBuilder.toString(), commandLine.get(0));
                } catch (NullPointerException npe) {
                    System.err.println("There hasn't been settled a command line string");
                }
                break;

                case start:               /*Modifying a process builder's attributes will affect processes subsequently started by that object's start() method, 
					                            but will never affect previously started processes or the Java process itself.*/
						                      try { //The new process will invoke the command and arguments given by command(),
                    //                            in a working directory as given by directory(),
                    //                            with a process environment as given by environment().                                     
                    unProceso = aProcessBuilder.start();
                    //'start()' returns an instance of a subclass of 'Process' (Process is an abstract class) that
                    //allows to control the process an obtain information about it

                    System.out.printf("Created a 'Process' object through the 'ProcessBuilder' object %s and launching the system-dependent command %s\n", aProcessBuilder.toString(), commandLine.get(0));

                } catch (IOException ioe) {
                    System.err.println(ioe.getMessage());
                } //The file of 'commandLine' or the directory of 'workingDirectory doesn't exists
                catch (NullPointerException npe) {
                    System.err.println("It hasn't been created a 'ProcessBuilder' object");
                } //'aProcessBuilder' is null 
                catch (IndexOutOfBoundsException ioobe) {
                    System.err.println("It has been set up an empty command line");
                } //'commandLine' is an empty ArrayList (has length 0} 
                break;

                case destroy:
                    unProceso.destroy();
                    System.out.println("The system-dependent process has just been destroyed (not the object instance of class 'Process')");
                    break;

                case exitValue:
                    System.out.printf("The exit value of the ended process is %d\n", unProceso.exitValue());
                    break;

                case waitFor:
                    System.out.println("The process of 'pruebaProcess' application is blocked waiting for the termination of de system-dependent process");
                    try {
                        System.out.printf("Uuuaaaammmmm!. The process of the 'pruebaProcess' application has just woke up, because the"
                                + " system-dependent process has terminated returning the exit value %d\n",
                                unProceso.waitFor());
                    } catch (InterruptedException ie) {
                        ie.getMessage();
                        System.err.println("The 'pruebaProcess' thread has been interrupted by some other thread ");
                    }
                    break;

                case redirectType:
                    boolean repetir;

                    do {
                        System.out.println("Choose the type of the Redirect object: (P)IPE, (I)NHERIT, (R)ead from file, (W)rite to file, (A)ppend to file");
                        line = scanner.nextLine();

                        repetir = false;
                        char op;

                        op = line.charAt(0);

                        File f;

                        switch (op) {

                            case 'P':
                            case 'p':
                                aRedirect = ProcessBuilder.Redirect.PIPE;
                                assert aRedirect.file() == null && aRedirect.type() == ProcessBuilder.Redirect.Type.PIPE;
                                assert Redirect.PIPE.file() == null && Redirect.PIPE.type() == Redirect.Type.PIPE;
                                break;

                            case 'I':
                            case 'i':
                                aRedirect = ProcessBuilder.Redirect.INHERIT;
                                assert aRedirect.file() == null && aRedirect.type() == ProcessBuilder.Redirect.Type.PIPE;
                                assert Redirect.INHERIT.file() == null && Redirect.INHERIT.type() == Redirect.Type.INHERIT;
                                break;

                            case 'R':
                            case 'r':
                                System.out.println("Write the name of the file from which to read");
                                line = scanner.nextLine();
                                f = new File(line);
                                try {
                                    aRedirect = Redirect.from(f);
                                    assert aRedirect.type() == ProcessBuilder.Redirect.Type.READ && aRedirect.file() == f;
                                } catch (NullPointerException npe) {
                                    System.err.println(npe.getMessage());
                                }

                                break;

                            case 'W':
                            case 'w':
                                System.out.println("Write the name of the file to write in");
                                line = scanner.nextLine();
                                f = new File(line);
                                try {
                                    aRedirect = Redirect.to(f);
                                    assert aRedirect.type() == ProcessBuilder.Redirect.Type.WRITE && aRedirect.file() == f;
                                } catch (NullPointerException npe) {
                                    System.err.println(npe.getMessage());
                                }

                                break;

                            case 'A':
                            case 'a':
                                System.out.println("Write the name of the file to append in");
                                line = scanner.nextLine();
                                f = new File(line);
                                try {
                                    aRedirect = Redirect.appendTo(f);
                                    assert aRedirect.type() == ProcessBuilder.Redirect.Type.APPEND && aRedirect.file() == f;
                                } catch (NullPointerException npe) {
                                    System.err.println(npe.getMessage());
                                }

                                break;

                            default:
                                repetir = true;
                                break;
                        }

                    } while (repetir);

                    break;

                case redirectInput:        //Set a new standard input source (of a subprocess)
						
						                       // redirectInput(aRedirect) <---> aRedirect.Type.READ, aRedirect.Type.PIPE, aRedirect.Type.INHERIT
						                       // redirectInput(aRedirect),aRedirect.Type.WRITE,aRedirect.Type.APPEND----->IllegalArgumentException
						                       
						                       //1º) aProcessBuilder.redirectInput(aRedirect), aRedirect.Type!= Redirect.Type.PIPE 
						                       //2º) aProcess=aProcessBuilder.start()
						                       //3º) ----->outputStreamToSubprocessInput=aProcess.getInputStream() == null inputStream  
						                       //4º) ----->outputStreamToSubprocessInput.write(salida)----->IOException
						
						                       /*If the source is Redirect.PIPE (the initial value), then the standard input of a subprocess can be written to using
					                             the output stream returned by Process.getOutputStream(). 
					                             If the source is set to any other value, then Process.getOutputStream() will return a null output stream.*/
						
						                       try {                            //source
                    aProcessBuilder.redirectInput(aRedirect); //returns 'this'
                    //aProcessBuilder.redirectInput(Redirect.from(new File(nombreFichero));
                    //aProcessBuilder.redirectInput(new File(nombreFichero));
                } catch (IllegalArgumentException iae) {
                    System.err.println("The redirect does not correspond to a valid source of data, that is, has type WRITE or APPEND");
                }

                break;

                case redirectOutput:       //Set a new standard output destination (of a subprocess)
						
						                       // redirectOutput <---> Redirect.Type.WRITE, Redirect.Type.APPEND, Redirect.Type.PIPE, Redirect.Type.INHERIT 
	                                           // redirectInput(aRedirect),aRedirect.Type.READ----->IllegalArgumentException
	                       
	                                           //1º) aProcessBuilder.redirectOutput(aRedirect), aRedirect.Type!= Redirect.Type.PIPE 
	                                           //2º) aProcess=aProcessBuilder.start()
	                                           //3º) ----->inputStreamToSubprocessOutput=aProcess.getInputStream() == null inputStream  
	                                           //4º) ----->inputStreamToSubprocessOutput.read(entrada)----->returns -1
						                       //                                       .available()  -----> returns 0
						
						
						                       /*If the destination is Redirect.PIPE (the initial value), then the standard output of a subprocess can be read using 
					                             the input stream returned by Process.getInputStream().
					                             If the destination is set to any other value, then Process.getInputStream() will return a null input stream.*/
						                       try {                              //destination
                    aProcessBuilder.redirectOutput(aRedirect); //returns 'this'
                    //aProcessBuilder.redirectOutput(Redirect.to(new File(nombreFichero)));
                    //aProcessBuilder.redirectOutput(new File(nombreFichero));
                } catch (IllegalArgumentException iae) {
                    System.err.println("The redirect does not correspond to a valid destination of data, that is, has type READ");
                }

                break;

                case redirectError:        //Set a new standard error destination (of a subprocess)
						
	                                           // redirectError <---> Redirect.Type.WRITE, Redirect.Type.APPEND, Redirect.Type.PIPE, Redirect.Type.INHERIT 
                                               // redirectError(aRedirect),aRedirect.Type.READ----->IllegalArgumentException
    
                                               //1º) aProcessBuilder.redirectError(aRedirect), aRedirect.Type!= Redirect.Type.PIPE 
                                               //2º) aProcess=aProcessBuilder.start()
                                               //3º) ----->inputStreamToSubprocessOutput=aProcess.getInputStream() == null inputStream  
                                               //4º) ----->inputStreamToSubprocessOutput.read(entrada)----->returns -1
	                                           //                                       .available()  -----> returns 0
						
						                       /*If the destination is Redirect.PIPE (the initial value), then the error output of a subprocess can be read using 
					                             the input stream returned by Process.getErrorStream(). If the destination is set to any other value, 
					                             then Process.getErrorStream() will return a null input stream. 
                                                 If the redirectErrorStream attribute has been set true, then the redirection set by this method has no effect.
                                                */
						                        try {                               //destination
                    aProcessBuilder.redirectError(aRedirect); //returns 'this' 
                    //aProcessBuilder.redirectError(Redirect.to(new File(nombreFichero)));
                    //aProcessBuilder.redirectError(new File(nombreFichero));      
                } catch (IllegalArgumentException iae) {
                    System.err.println("The redirect does not correspond to a valid destination of data, that is, has type READ");
                }

                break;

                case isErrorStreamRedirected: /*Tells whether this process builder merges standard error and standard output. 
                                                    If this property is true, then any error output generated by subprocesses subsequently started by this object's start() method will be merged 
                                                    with the standard output, so that both can be read using the Process.getInputStream() method. This makes it easier to correlate error messages
                                                    with the corresponding output. The initial value is false.
                                                   */
						                           try {
                    System.out.printf("The current process builder's standard error is redirected?: %s\n", aProcessBuilder.redirectErrorStream());
                } catch (NullPointerException npe) {
                    System.err.println("There isn't settled a 'ProcessBuilder' object");
                }

                break;

                case setRedirectErrorStream: /* Initially, this property is false, meaning that the standard output and error output of a subprocess are sent to two separate streams, which can be accessed using the Process.getInputStream() and Process.getErrorStream() methods. 
					                             If the value is set to true, then: 
						                            •standard error is merged with the standard output and always sent to the same destination (this makes it easier to correlate error messages with the corresponding output) 
						                            •the common destination of standard error and standard output can be redirected using redirectOutput
						                            •any redirection set by the redirectError method is ignored when creating a subprocess 
						                            •the stream returned from Process.getErrorStream() will always be a null input stream
						                       */
						
						                        try {
                    System.out.printf("The current process builder's standard error redirected has changed to: %s\n", !aProcessBuilder.redirectErrorStream());
                    aProcessBuilder.redirectErrorStream(!aProcessBuilder.redirectErrorStream());
                } catch (NullPointerException npe) {
                    System.err.println("There isn't settled a 'ProcessBuilder' object");
                }

                break;

                case inheritIO:            /*Sets the source and destination for subprocess standard I/O to be the same as those of the current Java process. 
					                             This gives behavior equivalent to most operating system command interpreters, or the standard C library function system().
					                           */
						                       try {
                    System.out.println("Sets the source and destination for subprocess standard I/O to be the same as those of the current Java process");
                    aProcessBuilder.inheritIO(); //return 'this'
                } catch (NullPointerException npe) {
                    System.err.println("There isn't settled a 'ProcessBuilder' object");
                }

                /*behaves in exactly the same way as the invocation  pb.redirectInput(Redirect.INHERIT)
                                                                                                      .redirectOutput(Redirect.INHERIT)
                                                                                                      .redirectError(Redirect.INHERIT)
                 */
                break;

                case seeRedirectInput:     /*Returns this process builder's standard input source. Subprocesses subsequently started by this object's start() method 
					                             obtain their standard input from this source. The initial value is Redirect.PIPE.*/
						                       try {
                    System.out.printf("The current process builder's standard input source is: %s\n", aProcessBuilder.redirectInput().toString());
                } catch (NullPointerException npe) {
                    System.err.println("There isn't settled a 'ProcessBuilder' object");
                }

                break;

                case seeRedirectOutput:    /*Returns this process builder's standard output destination. Subprocesses subsequently started by this object's start() method 
                                                 redirect their standard output to this destination. The initial value is Redirect.PIPE.*/
                                               try {
                    System.out.printf("The current process builder's standard output destination is: %s\n", aProcessBuilder.redirectOutput().toString());
                } catch (NullPointerException npe) {
                    System.err.println("There isn't settled a 'ProcessBuilder' object");
                }

                break;

                case seeRedirectError:     /*Returns this process builder's standard error destination. Subprocesses subsequently started by this object's start() method 
                                                 redirect their standard error to this destination. The initial value is Redirect.PIPE.*/
                                               try {
                    System.out.printf("The current process builder's standard error destination is: %s\n", aProcessBuilder.redirectError().toString());
                } catch (NullPointerException npe) {
                    System.err.println("There isn't settled a 'ProcessBuilder' object");
                }

                break;

                case getOutputStream:      try {
                    outputStreamToSubprocessInput = unProceso.getOutputStream();  //if 'redirectInput' has redirect the standard input of the subprocess
                } catch (NullPointerException npe) {
                    System.err.println("There isn't settled a 'Process' object");
                } //to another source different from 'PIPE' (a pipe to parent process)
                //then 'getOutputStream()' returns a null output stream for which
                //'write()' always throw IOException and 'close()' do nothing
                System.out.println("We get an outputStream conected to the standard subprocess's input");
                break;

                case getInputStream:       try {
                    inputStreamFromSubprocessOutput = unProceso.getInputStream(); //if 'redirectInput' has redirect the standard input of the subprocess
                } catch (NullPointerException npe) {
                    System.err.println("There isn't settled a 'Process' object");
                } //to another source different from 'PIPE' (a pipe to parent process)
                //then 'getInputStream()' returns a null input stream for which
                //'read()' always return -1, 'available()' always returns 0 and
                //'close()' do nothing
                System.out.println("We get an inputStream conected to the standard subprocess's output");
                break;

                case getInputStreamFromSubprocessError:  try {
                    inputStreamFromSubprocessError = unProceso.getErrorStream(); //if 'redirectError' has redirect the standard input of the subprocess
                } catch (NullPointerException npe) {
                    System.err.println("There isn't settled a 'Process' object");
                } //to another source different from 'PIPE' (a pipe to parent process)
                //then 'getInputStream()' returns a null input stream for which
                //'read()' always return -1, 'available()' always returns 0 and
                //'close()' do nothing
                System.out.println("We get an errorStream conected to the standard subprocess's error console");
                break;

                case escribirEnOutputStream:

                    System.out.println("Write the characters that will be redirect to the subprocess (as a group of bytes)");

                    try {
                        byte[] salida = new byte[100];
                        System.in.read(salida);                      //get the bytes from standard 'in'
                        try {
                            outputStreamToSubprocessInput.write(salida); //send the bytes to subprocess 
                        } catch (IOException ioe) {
                            System.err.println("The standard input of the subprocess is different from PIPE");
                            break;
                        }

                        outputStreamToSubprocessInput.flush();       //Send now the bytes buffered in this outputStream  
                        System.out.println("I'm 'PruebaProcess': I've just send the bytes");

                        byte[] entrada = new byte[100];
                        if (inputStreamFromSubprocessOutput.read(entrada) == -1) //get the bytes from subprocess
                        {
                            System.err.println("It has been reached the end of the input or "
                                    + "The standard input of the subprocess is different from PIPE");
                        }
                        System.out.write(entrada);                    //write the bytes to the standard 'out'
                        System.out.println("I'm 'PruebaProcess': I've just received the bytes from subprocess's output");

                        byte[] entradaError = new byte[100];
                        if (inputStreamFromSubprocessError.read(entradaError) == -1) //get the bytes from subprocess
                        {
                            System.err.println("\nIt has been reached the end of the error or "
                                    + "The standard error of the subprocess is different from PIPE");
                        };
                        System.out.println("\nI'm 'PruebaProcess': I've just received the bytes from subprocess's error ");
                        System.out.write(entradaError);

                        //By default, the error console is the input console,
                        //so 'outputStreamToSubprocessInput' is the same as 'inputStreamToSubprocessError'
                        //     if(inputStreamToSubprocessError.available()!=0){  //if there are bytes to read
                        //  inputStreamToSubprocessError.read(entrada);  //get the bytes from the subprocess's error console
                        //System.out.println("I'm 'PruebaProcess': I've just received the bytes from subprocess's error console");
                        //     System.err.write(entrada);
                        //     }				                         
                    } catch (IOException ioe) {
                        ioe.getMessage();
                    } catch (NullPointerException npe) {
                        System.err.println("outputStream or inputStream to subprocess hasn't been created yet");
                    }

                    break;

                case gc:
                    System.gc();
                    System.out.println("A request to invoque the garbage collector has been made");
                    break;

            }
        }

        System.out.println("The program will end normally");
    }

}


/*-----------------------------------------Crear con objetos 'ProcessBuilder' subprocesos que tienen sus propios Flujos de E/S
 * 
 * -Intentar crear un subproceso antes de crear el objeto 'aProcessBuilder'. Mensaje de error
 * -Intentar crear el objeto 'aProcessBuilder' antes de crear una línea de comandos. Mensaje de error
 * 
 * -Crear una línea de comandos para lanzar un editor sencillo tipo block de notas
 * -Crear un objeto 'ProcessBuilder'. Fíjese en su identidad
 * -Crear un subproceso para el block de notas mediante el objeto 'ProcessBuilder' actual. Fíjese en la identidad del objeto 'ProcessBuilder' que lo ha creado
 * -No mate ni finalice el subproceso
 * 
 * -Cambiar (NO CREAR) una nueva línea de comandos para lanzar la calculadora
 * -Crear un subproceso para la calculadora mediante el objeto 'ProcessBuilder' actual (es decir, NO crear un nuevo objeto 'ProcessBuilder'.  Fíjese en la identidad del objeto 'ProcessBuilder' que lo ha creado
 * -No mate ni finalice el subproceso
 * 
 * -Cree (NO CAMBIAR) una nueva línea de comandos para lanzar otro programa
 * -Cree un subproceso. ¿Que subproceso se ha lanzado? ¿Porqué?
 * -Cierre este último subproceso
 * 
 * -Crear un objeto 'ProcessBuilder'. Fíjese en su identidad
 * -Crear un subproceso para la calculadora mediante el objeto 'ProcessBuilder' actual. Fíjese en la identidad del objeto 'ProcessBuilder' que lo ha creado
 *
 * -Matar uno de los subprocesos 
 * -Obtener el valor devuelto por el subproceso
 * 
 * -Bloquear la aplicación 'pruebaProcessBuilder' hasta que finalice el subproceso
 * -Cierre el proceso
 * -Obtener valor devuelto por el subproceso
 * 
 * -----------------------------------------Crear con objetos 'ProcessBuilder' subprocesos que comparten los flujos del proceso padre
 * 
 * -Lance un comando para arrancar una shell de su sistema operativo (en Windows: cmd; en Linux: bshrc ¿?)
 * -Compruebe si se ha creado el subproceso empleando utilidades del sistema operativo (en Windows ctr-alt-spr para abrir el administrador de tareas o 
 *                                                                                      clic derecho en barra de tareas, pestaña 'Procesos';
 *                                                                                      en Linux comando 'ps -a' ¿?)
 * -¿Que ocurre si trata de obtener el valor devuelto por el proceso?
 * 
 * 
 * 
 * 
 * ---------------------------------------------------------Establecer directorio de trabajo
 * 
 * -Lanzar el comando 'java'. ¿Que mensaje se muestra?. ¿Se habrá ejecutado ese subproceso?. Consulte los procesos activivos en su sistema y averigue
 *  si hay activo un proceso denominado 'javaw'
 * 
 * 
 * -Lanzar el comando 'java Mayusculas'. ¿Se habrá ejecutado ese subproceso?.
 * -Obtener valor devuelto por el subproceso. ¿Como interpreta este valor?
 * 
 * 
 * -Establecer como directorio del proceso padre ('pruebaProcessBuilder) a '..\mayusculas\bin\' (suponiendo que se tiene el paquete 'mayusculas')
 * -Se supone que la línea de comandos actual es 'java Mayusculas'
 * -lanzar el subproceso 'java Mayusculas'. ¿Que mensaje se muestra?. ¿Se habrá ejecutado ese subproceso?.  Consulte los procesos activivos en su sistema y averigue
 *  si hay activo un proceso denominado 'javaw Mayusculas'
 *  -¿Que ocurrirá si tratamos de obtener en este momento el valor devuelto por el programa 'java Mayusculas'?
 * 
 * -----------------------------------------------------------Cambiar las variables de entorno
 * 
 * -Lanzar el comando 'java Mayusculas'. ¿Se encontró el archivo del programa 'Mayúsculas'?
 * -Establezca el valor de la variable de entorno 'Path' a: (Windows:%path%;..\mayusculas\bin\;) (Linux $(PATH):..\mayusculas\bin:   ¿?)
 *                                                'CLASSPATH' a: (Windows: %CLASSPATH%;..\mayusculas\bin\;  )   (Linux: $(CLASSPATH);..\mayusculas\bin\;  ¿? )
 * -Lanzar el comando 'java Mayusculas'. ¿Se encontró el archivo del programa 'Mayúsculas'?
 * -Matar el subproceso
 * 
 * --------------------------------------------------------Comunicar proceso padre con subprocesos: Redirect
 * 
 * -Configurar la línea de comandos para ejecutar el programa 'Mayusculas.class'
 * -Crear un nuevo objeto 'ProcessBuilder'
 * -Ajustar las variables de entorno 'Path' y 'CLASSPATH'
 * -Si le resulta más cómodo, cambie el directorio de trabajo
 * 
 * ----------redirectInput: PIPE y redirectOutput: PIPE
 * 
 * -¿Cual es el tipo de 'Redirect' input, output y errror, del objeto 'ProcessBuilder' actual? ¿Que significa eso?
 * -Lance un subproceso del programa 'Mayusculas'
 * -Obtener los inputStream y outputStream que comunican el proceso padre con el subproceso a través de su entrada estandar, salida estándar y salida de error
 * -Probar esa conexión enviando algunos bytes desde el proceso padre al subproceso hijo y obtener los bytes enviados del supbroceso hijo al proceso padre
 * 
 * ----------redirectInput: PIPE y redirectOutput: INHERIT
 * -Defina un objeto 'Redirect' de tipo INHERIT
 * -Establezca la salida de suproceso con el objeto 'Redirect' que acaba de crear
 * -Si vuelve a probar la conexión Padre-subproceso hijo, enviando algunos bytes, ¿cambia algo? ¿Porqué?
 * -Mate al subproceso actual
 * -Examine el tipo de 'Redirect' que tiene el 'ProcessBuilder' actual para la salida y entrada de los subprocesos
 * -Lance un nuevo subproceso del programa Mayusculas
 * -Tratar de obtener los inputStream y outputStream que comunican el proceso padre con el subproceso a través de su entrada estandar, salida estándar y salida de error
 * -Pruebe la conexión enviando algunos bytes. ¿Que ocurre? ¿Porqué?
 * 
 * 
 * ----------redirectInput: INHERIT y redirectOutput: PIPE
 * -Mate al proceso actual
 * -Establezca con objetos 'Redirect' la entrada al Subproceso con un 'Redirect.Type.INHERIT' y la salida del subproceso con un 'Redirect.Type.PIPE'
 * -Lance un nuevo subproceso del programa Mayusculas
 * -Tratar de obtener los inputStream y outputStream que comunican el proceso padre con el subproceso a través de su entrada estandar, salida estándar y salida de error
 * -Pruebe la conexión enviando algunos bytes. ¿Que ocurre? ¿Porqué?
 * 
 * 
 * ----------redirectInput: INHERIT y redirectOutput: INHERIT
 * -Mate al proceso actual
 * -Establezca con objetos 'Redirect' la entrada al Subproceso con un 'Redirect.Type.INHERIT' y la salida del subproceso con un 'Redirect.Type.INHERIT' y error del subproceso a 'Redirect.Type.INHERIT'
 *  (Hay dos formas de hacer esto, ¿cuales son esas dos maneras?)
 * -Lance un nuevo subproceso del programa Mayusculas
 * -Tratar de obtener los inputStream y outputStream que comunican el proceso padre con el subproceso a través de su entrada estandar, salida estándar y salida de error
 * -Pruebe la conexión enviando algunos bytes. ¿Que ocurre? ¿Porqué?
 * 
 * 
 * -¿Está la salida de error redireccionada a la salida estandar?
 * -Redireccionar la salida de error para que coincida con la salida estándar
 * -Lance un subproceso del programa Mayusculas
 * -Obtener los inputStream y outputStream que comunican el proceso padre con el subproceso a través de su entrada estandar, salida estándar y salida de error
 * -Pruebe la conexión enviando algunos bytes. ¿Que ocurre? ¿Porqué?
 * 
 * */
