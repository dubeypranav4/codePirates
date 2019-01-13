package Zomato.BeanFactory;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ZomatoBeanFactory implements ApplicationContextAware {


    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        applicationContext=applicationContext;
    }

    public static Object getBean(String beanName)
    {
        return applicationContext.getBean(beanName);
    }
}
