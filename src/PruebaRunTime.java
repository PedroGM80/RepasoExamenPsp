
import java.lang.Runtime;
import java.util.Scanner;
import java.io.IOException;

/**
 *********************************************************
 * pruebaRunTime*************************************** A program 'to play' with
 * the java.lang.Runtime Java class
 *
 * @author Miguel Angel Bolivar Perez (mabolivarperez@yahoo.es), IES Fernando
 * Aguilar Quignon - Cádiz - Spain
 * @version 1.0
 *
 */
public class PruebaRunTime {

    private static class Menu {

        final static int MAXOPCION = 13; //It should be the ordinal of the last enum's value
        //Warning: The sequence of the values in the enum 'opcionesMenu' must be THE SAME as the integer values showed in the Menu

        enum opcionesMenu {
            availableProcessors, freeMemory, totalMemory, maxMemory, gc, exec, addShutdownHook, try_re_register_a_ShutdownHook,
            shutting_down_and_try_re_register_a_shutdownHook, removeShutdownHook, exit, halt, end
        }
        static opcionesMenu[] arrayOpcionesMenu = opcionesMenu.values();

        private static opcionesMenu menu() {

            Scanner entrada = new Scanner(System.in); //<-----Oh! bad, bad boy

            int opcion;

            do {
                System.out.println("___________________________________________________________________________________");
                System.out.println();
                System.out.println("Let's interact with the (single) object 'Runtime' of the Java application that allows the application to interface with the environment in which the application is running ");
                System.out.println();
                System.out.println("Write the number of the desired option and then press intro: \n");
                System.out.println("-----------------------------------Processes's Resources: Processors and Memory---------------------");
                System.out.println("01-availableProcessors");
                System.out.println("02-freeMemory");
                System.out.println("03-totalMemory");
                System.out.println("04-maxMemory");
                System.out.println("05-gc (garbage collector)");
                System.out.println("------------------------------------------Playing with Processes----------------------------------");
                System.out.println("06-exec");
                System.out.println("---------------------------------------------Shutdown Hooks---------------------------------------");
                System.out.println("07-addShutdownHook. Register a new virtual-machine shutdown hook");
                System.out.println("08-try re-registering the last registered shutdownHook (an 'IllegalArgumentException' will be thrown)");
                System.out.println("(*under construction)09-shutting down de virtual machine (exit) and try registering a new shutdownHook (an 'IllegaStateException' will be thrown)");
                System.out.println("10-removeShutdownHook. De-register the last registered shutdown hook, if any");
                System.out.println("-----------------------------------------Finishing: exit, halt, end-------------------------------");
                System.out.println("11-exit. The program is just going to initiate its shutdown sequence, then it will return 0 status code");
                System.out.println("\tNote: typing ^C, user logoff o system shutdown will cause shutdown of the application's Java virtual machine and therefore the exits of the program");
                System.out.println("12-halt. The program ends abnormally, returning 1 status code");
                System.out.println("13-end.  Let the program ends normally \n");
                System.out.println("___________________________________________________________________________________");
                System.out.println("");

                opcion = entrada.nextInt();

            } while (opcion < 1 || opcion > MAXOPCION);

            return arrayOpcionesMenu[opcion - 1]; //Warning: The sequence of the values of the enum 'opcionesMenu' must be THE SAME as the integer values showed in the Menu
        }

    }

    //***************************************
    static String programa;
    static String parametrosEntorno;
    static String directorioTrabajo;

    private static class PruebaShutdownHook extends Thread {

        public static int id = 0;
        public static PruebaShutdownHook ultimoShutdownHookRegistrado;

        public int idShutdownHookRegistrado;
        private String message;

        public PruebaShutdownHook() {
            idShutdownHookRegistrado = ++id;
            message = "The shutdownHook # " + idShutdownHookRegistrado + " makes a thread-safe short-running computation,avoiding deadlocks with no user interaction and carefully relaying in services";
            ultimoShutdownHookRegistrado = this;
        }

        public void run() {
            System.out.println(message);
        }

    }

    /**
     * @param args
     */
    public static void main(String[] args) {

        Runtime theRuntimeObject = Runtime.getRuntime(); //We get the only instance of Runtime class for the current Java application
        Menu.opcionesMenu opcion;

        while ((opcion = Menu.menu()) != Menu.opcionesMenu.end) {

            switch (opcion) {

                case availableProcessors:
                    System.out.printf("There are available %d processors\n", theRuntimeObject.availableProcessors());
                    break;

                case freeMemory:
                    System.out.printf("The amount of free memory in the Java Virtual Machine available for FUTURE allocated objects are %d bytes \n",
                            theRuntimeObject.freeMemory());
                    break;

                case totalMemory:
                    System.out.printf("The total amount of memory currently available in the Java Virtual Machine available for CURRENT and FUTURE objects are %d bytes\n",
                            theRuntimeObject.totalMemory());
                    break;

                case maxMemory:
                    System.out.printf("The max amount of memory that the Java virtual machine will attempt to use are %d bytes\n",
                            theRuntimeObject.totalMemory());
                    break;

                case gc:
                    System.out.println("We request (it's not a subrrutine call) that the garbage collector does its work");
                    theRuntimeObject.gc();  //It's equivalent to System.gc();
                    break;

                case exec: {
                    Scanner cadena = new Scanner(System.in);
                    System.out.println("Introduce (la ruta del fichero de) el programa a ejecutar, con los argumentos de línea de comandos necesarios");
                    programa = cadena.next();
                    // System.out.println("Introduce una secuencia de 'variablesEntorno=Valor'");
                    // parametrosEntorno=cadena.next();
                    // System.out.println("Introduce el nombre del directorio de trabajo");
                    // directorioTrabajo=cadena.next();
                    try {
                        Process p = theRuntimeObject.exec(programa, null, null);
                    } catch (IOException ioe) {
                        System.out.println("No pudo ser ejecutado el programa por error de E/S");
                    }
                }
                break;

                case addShutdownHook:
                    theRuntimeObject.addShutdownHook(new PruebaShutdownHook());
                    System.out.println("Registered shutdown Hook #" + PruebaShutdownHook.ultimoShutdownHookRegistrado.id);
                    break;

                case try_re_register_a_ShutdownHook:
                    theRuntimeObject.addShutdownHook(PruebaShutdownHook.ultimoShutdownHookRegistrado);
                    System.out.println("Registered shutdown Hook #" + PruebaShutdownHook.ultimoShutdownHookRegistrado.id);
                    break;

                case shutting_down_and_try_re_register_a_shutdownHook:
                    System.out.println("Falta por implementar: lanzar un thread que cree y registre nuevos shutdownHook");
                    break;

                case removeShutdownHook:
                    theRuntimeObject.removeShutdownHook(PruebaShutdownHook.ultimoShutdownHookRegistrado);
                    System.out.println("De-registered shutdown Hook #" + PruebaShutdownHook.ultimoShutdownHookRegistrado.id);
                    break;

                case exit:
                    System.out.println("The program initiates its shutdown sequence and then halts, so we return a 0 status code");
                    theRuntimeObject.exit(0);//the same as the more convenient System.exit()
                    //System.out.println("This message is never prompted, because we'll never return from the call to exit()");
                    break;

                case halt:
                    System.out.println("The program ends abnormally, without initiating its shutdown sequence, so we return a !=0 status code");
                    theRuntimeObject.halt(1);
                    //System.out.println("This message is never prompted, because we'll never return from the call to halt()");
                    break;
            }
        }

        System.out.println("The program will end normally");
    }

}

/*PRACTICE with 'pruebaRunTime':
 * 
 * -----------------------------------Process's Resources: Processors and Memory---------------------
 * a) Find out the number of AVAILABLE PROCESSORS in your computer; 
 * b) Secondly, have a look on the TOTAL and MAXIMUM amount of memory (what's the difference between these two concepts?) of the Java virtual machine
 *    on which your program is being executed
 * c) Now, let's think on the FREE memory: What's the meaning of this concept?. It's greater or smaller than the Total amount of memory? Why?
 *   c.1) Well, find out the current amount of FREE memory and keep this figure in your mind
 *   c.2) Repeat again the former step (1 o several times); that is, get again the amount of FREE memory. Look at the figure you obtain each time,
 *        it's smaller and smaller. Why?. Which are the 'guilty' objects that are consuming memory?.
 * d) Call the 'garbage collector'. Do you think it will attend your request instantly?; to discover it, find out again the FREE memory.  
 * 
 * -----------------------------------------Finishing: exit, halt, end-------------------------------
 * 
 * e) Execute the application three times and finish it each time in a different way: 
 *    -by exit(), returning 0 error-code
 *    -by halt(), returning 1 (that it's,not 0) error-code 
 *    -and let the program ends just reaching its last sentence (the usual finalization, returning 0).
 *    
 *    note: you can get the error code returned by the last executed program just looking at the accurate Environment Variable in your operating system
 *    
 *---------------------------------------------Shutdown Hooks---------------------------------------    
 * 
 * f) Register one (a single) new virtual-machine shutdown hook. Then choose finish the program by exit or ending normally (but no by halt!). 
 *    What messages (and in which sequence) appear on the screen?. Why?
 * g) Execute the application again and register one more time a new virtual-machine shutdown hook. Then choose finish the program by halt; does appear any message
 *    on the screen this time? Why?
 * h) Execute the application and register several shutdown hooks; watch out that they are given id numbers in increasing order (#1,#2,#3,...). Thereafter, 
 *    finish the program by exit or ending normally (careful: Not by halt!) and look at the sequence in which, the previously registered shutdown hooks, are
 *    now invoked; is the same sequence as when they were created? Why is this so?
 * i) Register one (o more, if you want) shutdown hooks and then try re-registering the last registered shutdownHook. What happened?. Why? (yes, I know, that's
 *    a silly question)
 * j) (*under construction) shutting down the virtual machine (exit) and try registering a new shutdownHook
 * k) Execute the application and:
 *   k.1)Firstly, choose the menu option "De-register the last registered shutdown hook, if any" (despite of the fact that we are not registered any shutdown hook yet).
 *       What happened?.
 *   k.2)Register one (o more, if you want) shutdown hooks and then choose the menu option "De-register the last registered shutdown hook, if any". Then, finish the
 *       application by exit or ending normally (but not by halt!) and read the messages that appear on the screen. Are they consistent with what we previously did?
 *       
 * */
