package com.app.blog;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.app.blog.config.AppConstants;
import com.app.blog.entities.Role;
import com.app.blog.repositories.RoleRepo;

@SpringBootApplication
public class BlogApplication {

	@Autowired
	private RoleRepo roleRepo;

	public static void main(String[] args) {
		SpringApplication.run(BlogApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper(){

		return new ModelMapper();
	}

	public void run(String... args) throws Exception {

		try {

			Role admin = new Role();
			admin.setId(AppConstants.ADMIN_USER);
			admin.setName("ADMIN_USER");
	        
			Role normal = new Role();
			normal.setId(AppConstants.NORMAL_USER);
			normal.setName("NORMAL_USER");

			List<Role> roles = List.of(admin, normal);

			this.roleRepo.saveAll(roles);




		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
