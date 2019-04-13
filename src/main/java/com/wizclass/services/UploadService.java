package com.wizclass.services;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.wizclass.model.User;

public interface UploadService {
	public void init() throws IOException;
	public void deleteAll() throws IOException;
	public void deleteImage(User user);
	public void addImage(User user, MultipartFile picture, RedirectAttributes flash);
}
