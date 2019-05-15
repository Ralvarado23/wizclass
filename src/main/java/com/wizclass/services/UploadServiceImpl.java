package com.wizclass.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.wizclass.model.User;

@Service
public class UploadServiceImpl implements UploadService{

	@Override
	public void init() throws IOException {
		Files.createDirectory(Paths.get("uploads"));
	}

	@Override
	public void deleteAll() throws IOException {
		FileSystemUtils.deleteRecursively(Paths.get("uploads").toFile());
	}
	
	public void deleteImage(User user) {
		if(user.getPicture()!=null && user.getPicture().length() > 0) {

			System.out.println("Old picture: " + user.getPicture());
			
			Path rootPath = Paths.get("uploads").resolve(user.getPicture()).toAbsolutePath();
			File oldPicture = rootPath.toFile();
			
			if(oldPicture.exists() && oldPicture.canRead() && !oldPicture.toString().contains("uploads\\avatar.png") && !oldPicture.toString().contains("uploads\\WizclassLogo.png")) {
				System.out.println("foto antigua es: " + oldPicture);
				oldPicture.delete();
				System.out.println("Now picture is " + user.getPicture());
			}
		}
	}
	
	public void addImage(User user, MultipartFile picture, RedirectAttributes flash) {
		String uniqueFilename = UUID.randomUUID().toString() + "_" + picture.getOriginalFilename();
		Path rootPath = Paths.get("uploads").resolve(uniqueFilename);
		
		try {
			Files.copy(picture.getInputStream(), rootPath.toAbsolutePath());
			
			user.setPicture(uniqueFilename);
			System.out.println("New picture asigned: " + user.getPicture());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}