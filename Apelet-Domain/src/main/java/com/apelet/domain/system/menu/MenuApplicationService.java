package com.apelet.domain.system.menu;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.StrUtil;
import com.apelet.common.core.page.AbstractPageQuery;
import com.apelet.common.core.page.PageDTO;
import com.apelet.common.enums.common.StatusEnum;
import com.apelet.common.user.web.SystemLoginUser;
import com.apelet.domain.system.locals.command.AddLocalsCommand;
import com.apelet.domain.system.locals.db.SysLocalsEntity;
import com.apelet.domain.system.locals.db.SysLocalsService;
import com.apelet.domain.system.locals.model.LocalsModel;
import com.apelet.domain.system.locals.model.LocalsModelFactory;
import com.apelet.domain.system.menu.command.AddMenuCommand;
import com.apelet.domain.system.menu.command.UpdateMenuCommand;
import com.apelet.domain.system.menu.db.SysMenuEntity;
import com.apelet.domain.system.menu.db.SysMenuService;
import com.apelet.domain.system.menu.dto.MenuDTO;
import com.apelet.domain.system.menu.dto.MenuDetailDTO;
import com.apelet.domain.system.menu.dto.RouterDTO;
import com.apelet.domain.system.menu.model.MenuModel;
import com.apelet.domain.system.menu.model.MenuModelFactory;
import com.apelet.domain.system.menu.query.MenuQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 菜单应用服务
 * @author valarchie
 */
@Service
@RequiredArgsConstructor
public class MenuApplicationService {

    private final SysMenuService menuService;

    private final SysLocalsService localsService;

    private final MenuModelFactory menuModelFactory;

    private final  LocalsModelFactory localsModelFactory;


    public PageDTO<MenuDTO> getMenuList(MenuQuery query) {
        Page<SysMenuEntity> page = menuService.page(query.toPage(), query.toQueryWrapper());
        List<MenuDTO> records = page.getRecords().stream().map(MenuDTO::new)
                .sorted(Comparator.comparing(MenuDTO::getRank, Comparator.nullsLast(Integer::compareTo)))
                .collect(Collectors.toList());
        return new PageDTO<>(records, page.getTotal());
    }

    public MenuDetailDTO getMenuInfo(Long menuId) {
        SysMenuEntity byId = menuService.getById(menuId);
        return new MenuDetailDTO(byId);
    }

    public List<Tree<Long>> getDropdownList(SystemLoginUser loginUser) {
        List<SysMenuEntity> menuEntityList =
            loginUser.isAdmin() ? menuService.list() : menuService.getMenuListByUserId(loginUser.getUserId());

        return buildMenuTreeSelect(menuEntityList);
    }


    public void addMenu(AddMenuCommand addCommand) {
        MenuModel model = menuModelFactory.create();
        model.loadAddCommand(addCommand);

        // TODO 只允许在页面类型上添加按钮
        // 目前前端不支持嵌套的外链跳转
        model.checkMenuNameUnique();
        model.checkAddButtonInIframeOrOutLink();
        model.checkAddMenuNotInCatalog();
        model.checkExternalLink();

        model.insert();

        LocalsModel localsModel = localsModelFactory.create();
        localsModel.loadLocalsByMenuId(model.getMenuId(), addCommand);
        localsModel.insert();
    }

    public void updateMenu(UpdateMenuCommand updateCommand) {
        MenuModel model = menuModelFactory.loadById(updateCommand.getMenuId());
        model.loadUpdateCommand(updateCommand);

        model.checkMenuNameUnique();
        model.checkAddButtonInIframeOrOutLink();
        model.checkAddMenuNotInCatalog();
        model.checkExternalLink();
        model.checkParentIdConflict();

        model.updateById();
    }


    public void remove(Long menuId) {
        MenuModel menuModel = menuModelFactory.loadById(menuId);

        menuModel.checkHasChildMenus();
        menuModel.checkMenuAlreadyAssignToRole();

        menuModel.deleteById();
    }


    /**
     * 构建前端所需要树结构
     *
     * @param menus 菜单列表
     * @return 树结构列表
     */
    public List<Tree<Long>> buildMenuTreeSelect(List<SysMenuEntity> menus) {
        TreeNodeConfig config = new TreeNodeConfig();
        //默认为id可以不设置
        config.setIdKey("menuId");
        return TreeUtil.build(menus, 0L, config, (menu, tree) -> {
            // 也可以使用 tree.setId(dept.getId());等一些默认值
            tree.setId(menu.getMenuId());
            tree.setParentId(menu.getParentId());
            tree.putExtra("label", menu.getMenuName());
        });
    }


    public List<Tree<Long>> buildMenuEntityTree(SystemLoginUser loginUser) {
        List<SysMenuEntity> allMenus;
        if (loginUser.isAdmin()) {
            allMenus = menuService.list();
        } else {
            allMenus = menuService.getMenuListByUserId(loginUser.getUserId());
        }

        // 传给前端的路由排除掉按钮和停用的菜单
        List<SysMenuEntity> noButtonMenus = allMenus.stream()
            .filter(menu -> !menu.getIsButton())
            .filter(menu-> StatusEnum.ENABLE.getValue().equals(menu.getStatus()))
            .collect(Collectors.toList());

        TreeNodeConfig config = new TreeNodeConfig();
        // 默认为id 可以不设置
        config.setIdKey("menuId");

        return TreeUtil.build(noButtonMenus, 0L, config, (menu, tree) -> {
            // 也可以使用 tree.setId(dept.getId());等一些默认值
            tree.setId(menu.getMenuId());
            tree.setParentId(menu.getParentId());
            // TODO 可以取meta中的rank来排序
//            tree.setWeight(menu.getRank());
            tree.putExtra("entity", menu);
        });

    }


    public List<RouterDTO> buildRouterTree(List<Tree<Long>> trees) {
        List<SysLocalsEntity> localsEntities = localsService.list();
        List<RouterDTO> routers = new LinkedList<>();
        if (CollUtil.isNotEmpty(trees)) {
            for (Tree<Long> tree : trees) {
                SysMenuEntity entity = (SysMenuEntity)tree.get("entity");
                if (entity != null) {
                    Optional<SysLocalsEntity> localsOptional = localsEntities.stream().filter(item -> Objects.equals(item.getMenuId(), entity.getMenuId())).findAny();
                    SysLocalsEntity localsEntity = null;
                    if (localsOptional.isPresent()) {
                        localsEntity = localsOptional.get();
                    }
                    RouterDTO routerDTO = new RouterDTO(entity, localsEntity);
                    List<Tree<Long>> children = tree.getChildren();
                    if (CollUtil.isNotEmpty(children)) {
                        routerDTO.setChildren(buildRouterTree(children));
                    }
                    routers.add(routerDTO);
                }

            }
        }
        return routers;
    }


    public List<RouterDTO> getRouterTree(SystemLoginUser loginUser) {
        List<Tree<Long>> trees = buildMenuEntityTree(loginUser);
        return buildRouterTree(trees);
    }

}
