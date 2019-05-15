package com.wizclass.model;

import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * This interface is used to get the Pagina objects stored in one user.
 * @author Raul Alvarado
 *
 */
public interface UserPagesDao extends PagingAndSortingRepository<User, Long> {

}