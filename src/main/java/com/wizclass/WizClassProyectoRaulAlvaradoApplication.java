package com.wizclass;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.wizclass.services.UploadService;

/**
 * This is the main class that allows to launch the app
 * @author Raul Alvarado
 *
 */
@SpringBootApplication
public class WizClassProyectoRaulAlvaradoApplication implements CommandLineRunner{

	@Autowired
    BCryptPasswordEncoder passwordEncoder;
	
	private UploadService uploadService;
	
	public WizClassProyectoRaulAlvaradoApplication(UploadService uploadService) {
		this.uploadService = uploadService;
	}
	
	public static void main(String[] args) {
		SpringApplication.run(WizClassProyectoRaulAlvaradoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		//Uncomment to delete all uploads content on load
		/*uploadService.deleteAll();
		uploadService.init();*/
		
		//Uncomment to get coded passwords through console
		//System.out.println(passwordEncoder.encode("user"));
	}
}