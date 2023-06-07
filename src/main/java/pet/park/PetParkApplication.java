// Copyright (c) 2023 by Promineo Tech.

package pet.park;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * This class is the entry point to a Java application. The purpose of this
 * class is to start Spring Boot. This class is positioned at the top-level
 * package. All other packages in the application are in subpackages. This is in
 * keeping with the needs of the component scan in which Spring loads all
 * classes in this package and all subpackages.
 * 
 * In the component scan, Spring examines all classes in this package and all
 * subpackages. This is the way that Spring identifies classes for which it will
 * manage the class lifecycle. Classes that Spring manages are known as managed
 * Beans. Managed Beans are eligible for Dependency Injection (DI) in which a
 * class requests an object using the @Autowired annotation and Spring
 * automatically creates and injects the object into an instance variable.
 * 
 * The component scan is enabled through the @SpringBootApplication annotation.
 * Also enabled through this annotation is Spring Boot Auto-Configuration. In
 * Auto-Configuration, Spring Boot examines the Java classpath to see what is
 * there. It then configures the application accordingly. For example, if Tomcat
 * (or another Web application container) is found on the classpath, Spring Boot
 * sets up a Dispatcher Servlet, which is responsible for routing HTTP requests
 * to controller methods that you specify.
 * 
 * @author Promineo
 *
 */
@SpringBootApplication
public class PetParkApplication {
  /**
   * This method starts Spring Boot. Since this is a Web application it will
   * run, waiting for HTTP requests until you stop it.
   * 
   * @param args Unused
   */
  public static void main(String[] args) {
    SpringApplication.run(PetParkApplication.class, args);
  }
}
