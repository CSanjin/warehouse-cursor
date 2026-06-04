package com.warehouse.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.warehouse.common.result.PageResult;
import com.warehouse.common.result.Result;
import com.warehouse.user.entity.SysRole;
import com.warehouse.user.service.SysRoleService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/user/roles")
public class SysRoleController {

    @Resource
    private SysRoleService sysRoleService;

    @GetMapping("/page")
    public Result<PageResult<SysRole>> page(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String roleName) {
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(roleName), SysRole::getRoleName, roleName);
        Page<SysRole> page = sysRoleService.page(new Page<>(current, size), wrapper);
        return Result.ok(PageResult.of(page.getTotal(), page.getCurrent(), page.getSize(), page.getRecords()));
    }

    @GetMapping("/{id}")
    public Result<SysRole> getById(@PathVariable Long id) {
        return Result.ok(sysRoleService.getById(id));
    }

    @PostMapping
    public Result<Boolean> save(@RequestBody SysRole role) {
        return Result.ok(sysRoleService.save(role));
    }

    @PutMapping
    public Result<Boolean> update(@RequestBody SysRole role) {
        return Result.ok(sysRoleService.updateById(role));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.ok(sysRoleService.removeById(id));
    }
}
