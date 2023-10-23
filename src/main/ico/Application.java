package main.ico;


import main.ico.context.ApplicationContext;
import main.ico.context.ClassPathXmlApplicationContext;
import main.ico.entity.Apple;

public class Application {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext();
        Apple sweetApple = (Apple) context.getBean("sweetApple");
        System.out.println(sweetApple);
    }
}
