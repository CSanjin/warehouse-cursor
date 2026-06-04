package com.warehouse.user.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.warehouse.common.result.PageResult;
import com.warehouse.common.result.Result;
import com.warehouse.user.entity.SysPermission;
import com.warehouse.user.service.SysPermissionService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/user/permissions")
public class SysPermissionController {

    @Resource
    private SysPermissionService sysPermissionService;

    @GetMapping("/page")
    public Result<PageResult<SysPermission>> page(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size) {
        Page<SysPermission> page = sysPermissionService.page(new Page<>(current, size));
        return Result.ok(PageResult.of(page.getTotal(), page.getCurrent(), page.getSize(), page.getRecords()));
    }

    @GetMapping("/list")
    public Result<List<SysPermission>> list() {
        return Result.ok(sysPermissionService.list());
    }

    @GetMapping("/{id}")
    public Result<SysPermission> getById(@PathVariable Long id) {
        return Result.ok(sysPermissionService.getById(id));
    }

    @PostMapping
    public Result<Boolean> save(@RequestBody SysPermission permission) {
        return Result.ok(sysPermissionService.save(permission));
    }

    @PutMapping
    public Result<Boolean> update(@RequestBody SysPermission permission) {
        return Result.ok(sysPermissionService.updateById(permission));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.ok(sysPermissionService.removeById(id));
    }
}
