package main.ico.context;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassPathXmlApplicationContext implements ApplicationContext{
    private Map iocContainer = new HashMap();
    public ClassPathXmlApplicationContext() {
        try {
            String filePath = this.getClass().getResource("/applicationContext.xml").getPath();
            filePath = new URLDecoder().decode(filePath, "UTF-8");
            SAXReader reader = new SAXReader();
            Document document = reader.read(new File(filePath));
            List<Node> beans = document.getRootElement().selectNodes("bean");
            for (Node node : beans) {
                Element ele = (Element) node;
                String id = ele.attributeValue("id");
                String className = ele.attributeValue("class");
                Class aClass = Class.forName(className);
                Object newInstance = aClass.newInstance();
                List<Node> properties = ele.selectNodes("property");
                for (Node property : properties) {
                    Element eachProperty = (Element) property;
                    String name = eachProperty.attributeValue("name");
                    String value = eachProperty.attributeValue("value");

                    String setMethodName = "set" + name.substring(0,1).toUpperCase() + name.substring(1);
                    System.out.println("Ready to execute " + setMethodName + " method to inject the data");
                    Method setMethod = aClass.getMethod(setMethodName, String.class);
                    setMethod.invoke(newInstance, value);
                }


                iocContainer.put(id, newInstance);
            }
            System.out.println("IOC container finished initialization");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public Object getBean(String beanId) {
        return iocContainer.get(beanId);
    }
}
