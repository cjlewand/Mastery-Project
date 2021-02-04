package learn.house;

import learn.house.ui.Controller;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    public static void main(String[] args) {

        ApplicationContext context = new ClassPathXmlApplicationContext("dependency-configuration.xml");

        Controller controller = context.getBean(Controller.class);
        controller.run();
    }

    /*
    Host emails to use:
    normal: irosenkranc8w@reverbnation.com
    no reservations: ckornas99@hibu.com

    Guest Emails to use:
    normal: tstiggers6l@prlog.org
    no reservations: cspurden7k@forbes.com

    States to use:
    normal: FL
    no reservations: TX

     */
}
