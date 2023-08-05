package com.mylogin.jwt.controller;

import com.mylogin.jwt.entity.RoleEntity;
import com.mylogin.jwt.model.RoleModel;
import com.mylogin.jwt.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RoleController {

    @Autowired
    private RoleService roleService;
    @PostMapping("/roles")
    public RoleModel createRole(@RequestBody RoleModel roleModel) {
        return roleService.createRole(roleModel);
    }

    @GetMapping("/roles")
    public List<RoleModel> getAllRoles() {
        return roleService.getAllRoles();
    }

    @DeleteMapping("/roles/{roleId}")
    public void deleteRole(@PathVariable Long roleId) {
        roleService.deleteRoleById(roleId);
    }
}
