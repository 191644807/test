package spring_boot.spring.boot;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.web.servlet.mvc.Controller;

public class BeanDefinitionRegistryPostProcessorMy 
implements BeanDefinitionRegistryPostProcessor{

	public void postProcessBeanFactory(ConfigurableListableBeanFactory arg0) throws BeansException {
		
		
	}

	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry arg0) throws BeansException {
		arg0.registerBeanDefinition("user", getDeinition(User.class));
	}

	private BeanDefinition getDeinition(Class<User> class1) {
		User user = new User(1,  "zhangfei");
		
		//Controller  g = new Controller (class1);
		Class<?> g = user.getClass();
		
		
		
		return null;
	}
}
