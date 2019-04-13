package com.wizclass;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.wizclass.services.UploadService;

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
		//Uncomment to delete uploads content on load
		/*uploadService.deleteAll();
		uploadService.init();*/
		
		//System.out.println(passwordEncoder.encode("user"));
		//System.out.println(passwordEncoder.encode("admin"));
	}
}
