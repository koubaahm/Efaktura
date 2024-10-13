package com.exampleee.workchopsecurite.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exampleee.workchopsecurite.Entity.Role;
import com.exampleee.workchopsecurite.Repository.RoleRepository;



@RestController
@RequestMapping("/role")
public class RoleController {
  @Autowired
 private  RoleRepository roleRepository;
  @GetMapping("/getallroles")
  public List<Role> getallrole(){
	
	  return roleRepository.findAll() ;
	  
  }
}
