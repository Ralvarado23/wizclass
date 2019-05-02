package com.wizclass.security;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	  @Autowired
	  private BCryptPasswordEncoder passwordEncoder;

	  @Autowired
	  private DataSource dataSource;
	  
	  @Value("${spring.queries.users-query}")
	  private String usersQuery;
	  
	  @Value("${spring.queries.roles-query}")
	  private String rolesQuery;

	  @Override
	  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	    auth.
	      jdbcAuthentication()
	        .usersByUsernameQuery(usersQuery)
	        .authoritiesByUsernameQuery(rolesQuery)
	        .dataSource(dataSource)
	        .passwordEncoder(passwordEncoder);
	  }
	  
	  @Override
	  protected void configure(HttpSecurity http) throws Exception {
	    http
	      .authorizeRequests()
	      	.antMatchers("/").permitAll()
	      	.antMatchers("/login").permitAll()
	      	.antMatchers("/register").permitAll()
	      	.antMatchers("/about").permitAll()
	      	.antMatchers("/contact").permitAll()
	        .antMatchers("/sendMail").permitAll() 
	        .antMatchers("/page/{^[\\\\d]$}/index").permitAll()
	        .antMatchers("/page/{^[\\\\d]$}/oferta_educativa").permitAll()
	        .antMatchers("/page/{^[\\\\d]$}/noticias").permitAll()
	        //.antMatchers("/page/{^[\\\\d]$}/secretaria").permitAll()
	        .antMatchers("/page/{^[\\\\d]$}/calendario_escolar").permitAll()
	        .antMatchers("/page/{^[\\\\d]$}/contacto").permitAll()
	        .antMatchers("/sendMailApp/{^[\\\\d]$}").permitAll()
	        
	        .antMatchers("/adminUsers").hasAuthority("ADMIN")
	        .antMatchers("/user/details/{^[\\\\d]$}").hasAuthority("ADMIN")
	        .antMatchers("/user/updateAdmin/{^[\\\\d]$}").hasAuthority("ADMIN")
	        .antMatchers("/user/pages/{^[\\\\d]$}").hasAuthority("ADMIN")
	        .antMatchers("/user/delete/{^[\\\\d]$}").hasAuthority("ADMIN")
	        .antMatchers("/user/createUserAdmin").hasAuthority("ADMIN")
	        .antMatchers("/page/deleteAdmin/{^[\\\\d]$}").hasAuthority("ADMIN")
	        
	        .anyRequest().authenticated()
	        .and().csrf().disable().formLogin()
	        .loginPage("/login")
	        .usernameParameter("username")
	        .passwordParameter("password")
	        //.defaultSuccessUrl("/", true)
	        .and()
	        .csrf().disable()
	        .headers().frameOptions().disable()
	        .and()
	      .logout()
	        .permitAll()
	        .and()
	      .exceptionHandling().accessDeniedPage("/error403");
	  }
	
	  @Override
	  public void configure(WebSecurity web) throws Exception {
	     web
	      .ignoring()
	       .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/img/**", "/font/**", "/scss/**", "/uploads/**");
	  }
}