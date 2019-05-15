package com.wizclass.services;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.wizclass.model.User;

/**
 * This interface contains auxiliary methods related to images management.
 * @author Raul Alvarado
 *
 */
public interface UploadService {
	/**
	 * This method creates the 'uploads' directory.
	 * @throws IOException
	 */
	public void init() throws IOException;
	/**
	 * This method removes the 'uploads' directory content.
	 * @throws IOException
	 */
	public void deleteAll() throws IOException;
	/**
	 * This method removes an user´s image permanently.
	 * @param user - this parameter represents the user who owns the image.
	 */
	public void deleteImage(User user);
	/**
	 * This method adds an image to the 'uploads' directory.
	 * @param user - this parameter represents the user who owns the image.
	 * @param picture - this parameter contains the image to be added.
	 * @param flash - this parameter allows to send a personalized message to the view
	 */
	public void addImage(User user, MultipartFile picture, RedirectAttributes flash);
}