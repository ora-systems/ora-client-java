# Ora Client for Java

This is a simple synchronous client for accessing the ORA rest API.

## To Play

* Clone
* mvn compile
* mvn test

Take a look at the tests to see usage.

## Basics:

Following is a basic hello world app:

    import systems.ora.client.*;
    import java.io.IOException;

    public class App 
    {
      public static void main( String[] args )
      {
        OraClient cli = new OraClient();
        try {
          Halo h = cli.insertHalo(new Halo(1000401, 0.8, 0.7, 0.6, 0.5, 0.4, 0.3));
          System.out.println("Yo. All good.");
        } catch (IOException ex) {
          System.out.println("No such luck.");
        }     
      }
    }

Note that 1000401 is the sandbox app. In a production instance this would be your application id.


## To Include in your maven project:

    <dependency>
      <groupId>systems.ora</groupId>
      <artifactId>ora-client</artifactId>
      <version>0.8</version>
    </dependency>  

  
