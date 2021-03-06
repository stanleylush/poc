package eu.dzim.guice.fx.service.impl;

import java.net.URL;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import com.google.inject.ConfigurationException;
import com.google.inject.Injector;
import com.google.inject.ProvisionException;

import eu.dzim.guice.fx.service.FXMLLoaderService;
import javafx.fxml.FXMLLoader;

public class FXMLLoaderServiceImpl implements FXMLLoaderService {
	
	@Inject
	private Injector injector;
	
	// post-construct not working out of the box in Guice
	@PostConstruct
	private void postConstruct() {}
	
	@Override
	public FXMLLoader getLoader() {
		FXMLLoader loader = new FXMLLoader();
		loader.setControllerFactory(this::controllerForClass);
		return loader;
	}
	
	@Override
	public FXMLLoader getLoader(URL location) {
		FXMLLoader loader = new FXMLLoader(location);
		loader.setControllerFactory(this::controllerForClass);
		return loader;
	}
	
	@Override
	public FXMLLoader getLoader(URL location, ResourceBundle resourceBundle) {
		FXMLLoader loader = new FXMLLoader(location, resourceBundle);
		loader.setControllerFactory(this::controllerForClass);
		return loader;
	}
	
	private Object controllerForClass(Class<?> clazz) {
		try {
			Object controllerInstance = injector.getInstance(clazz);
			// controllerInstance#someArbitraryMethodCall if necessary
			// will be triggered *before* the initialize method!
			return controllerInstance;
		} catch (ConfigurationException | ProvisionException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	// pre-destroy not working out of the box in Guice
	@PreDestroy
	private void preDestroy() {}
}
