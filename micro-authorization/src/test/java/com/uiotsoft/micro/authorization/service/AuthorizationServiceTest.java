package com.uiotsoft.micro.authorization.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.uiotsoft.micro.authorization.AuthorizationServer;
import com.uiotsoft.micro.authorization.dto.AuthorizationInfoDTO;
import com.uiotsoft.micro.authorization.dto.PrivilegeDTO;
import com.uiotsoft.micro.authorization.dto.PrivilegeGroupDTO;
import com.uiotsoft.micro.authorization.dto.PrivilegeQueryParams;
import com.uiotsoft.micro.authorization.dto.PrivilegeTreeDTO;
import com.uiotsoft.micro.authorization.dto.RoleDTO;
import com.uiotsoft.micro.authorization.dto.RoleQueryParams;
import com.uiotsoft.micro.common.domain.ErrorCode;


@ActiveProfiles("dev")
@RunWith(SpringRunner.class)
@SpringBootTest (classes = {AuthorizationServer.class})
public class AuthorizationServiceTest {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthorizationServiceTest.class);
    
    @Autowired
    private AuthorizationService authorizationService;
    
    /**
     * 权限树
     *  void
     * @author 杨小波
     * @date 2018年07月30日 17:15:21
     */
    @Test
    public void testQueryPrivilegeTree(){
        String[] roleCodes = null;
        PrivilegeTreeDTO tree = authorizationService.queryPrivilegeTree(roleCodes);
        System.out.println("******************");
        System.out.println(tree);
        System.out.println("******************");
    }
    
    
    /**
     *  void
     * @author 杨小波
     * @date 2018年07月30日 16:18:51
     */
    @Test
    public void testQueryPTree(){
        String[] roleCodes = {"operations", "admin_1"};
        List<PrivilegeTreeDTO> list = authorizationService.queryPTree(roleCodes);
        System.out.println("******************");
        System.out.println(list.toString());
        System.out.println("******************");
    }
    
    /**
     *  void
     * @author 杨小波
     * @date 2018年07月30日 13:57:56
     */
    @Test
    public void testAuthorize(){
        AuthorizationInfoDTO authorize = authorizationService.authorize("root_1");
        Map<String, String[]> map = authorize.getRolePrivilegeMap();
        Set<String> keySet = map.keySet();
        
        System.out.println("******************");
        System.out.println("角色：" + keySet.toString());
        System.out.println("******************");
    }
    
    /**
     * 绑定角色至拥有者
     *  void
     * @author 杨小波
     * @date 2018年07月28日 11:22:36
     */
    @Test
    public void testBind(){
        String owner = "owner_2";
        String[] roleCodes = {"111", "222", "444"};
        System.out.println("******************");
        //绑定
        authorizationService.bind(owner, roleCodes);
        System.out.println("绑定成功");
        //解绑
        authorizationService.unbind(owner, roleCodes);
        System.out.println("解绑成功");
        System.out.println("******************");
    }
    
    /**
     * 分页条件查询角色
     *  void
     * @author 杨小波
     * @date 2018年07月28日 11:03:16
     */
    @Test
    public void testPageRoleByConditions(){
        System.out.println("******************");
        RoleQueryParams query = new RoleQueryParams();
//        query.setName("ad");
//        query.setCode("admin_code_1");
        Page<RoleDTO> page = authorizationService
                .pageRoleByConditions(query, 1, 5);
        List<RoleDTO> list = page.getContent();
        
        for(RoleDTO dto : list){
            System.out.println(dto.toString());
        }
        System.out.println("******************");
    }
   
    /**
     * 删除一组role
     *  void
     * @author 杨小波
     * @date 2018年07月27日 18:52:37
     */
    @Test
    public void testRemoveRole(){
        System.out.println("******************");
        String[] roleCodes = {"c1", "c2"};
        RoleDTO role1 = new RoleDTO();
        role1.setCode("c1");
        role1.setName("role1");
        RoleDTO role2 = new RoleDTO();
        role2.setCode("c2");
        role2.setName("role2");
        authorizationService.createRole(role1);
        authorizationService.createRole(role2);
        System.out.println("创建角色成功");
        authorizationService.removeRole(roleCodes);
        System.out.println("删除角色成功");
        System.out.println("******************");
    }
    
    /**
     * 修改角色信息（包含权限）
     *  void
     * @author 杨小波
     * @date 2018年07月27日 17:31:06
     */
    @Test
    public void testModifyRole(){
        System.out.println("******************");
        RoleDTO role1 = new RoleDTO();
        role1.setCode("c1");
        role1.setName("role1");
        RoleDTO createRole = authorizationService.createRole(role1);
        System.out.println("创建角色成功：" + createRole);
        
        RoleDTO role = new RoleDTO();
        role.setCode("c1");
        role.setName("用户_test_service");
        authorizationService.modifyRole(role);
        System.out.println("修改角色成功：" + role);
        
        String[] codeStr = {"c1"};
        authorizationService.removeRole(codeStr);
        System.out.println("删除角色成功");
        System.out.println("******************");
    }
    
    /**
     * 新增角色并绑定权限
     *  void
     * @author 杨小波
     * @date 2018年07月27日 16:30:43
     */
    @Test
    public void testCreateRole(){
        System.out.println("******************");
        RoleDTO role1 = new RoleDTO();
        role1.setCode("c1");
        role1.setName("role1");
        RoleDTO roleDTO = authorizationService.createRole(role1);
        System.out.println("创建角色成功：" + roleDTO);
        
        String[] codeStr = {"c1"};
        authorizationService.removeRole(codeStr);
        System.out.println("删除角色成功");
        System.out.println("******************");
    }
    
    /**
     * code查询角色以及权限
     *  void
     * @author 杨小波
     * @date 2018年07月27日 11:10:16
     */
    @Test
    public void testQueryRole(){
        System.out.println("******************");
        RoleDTO role1 = new RoleDTO();
        role1.setCode("c1");
        role1.setName("role1");
        RoleDTO roleDTO = authorizationService.createRole(role1);
        System.out.println("创建角色成功：" + roleDTO);
        
        RoleDTO role = authorizationService.queryRole("c1");
        System.out.println("查询成功：" + role);
        
        String[] codeStr = {"c1"};
        authorizationService.removeRole(codeStr);
        System.out.println("删除角色成功");
        System.out.println("******************");
    }
    
    /**
     * 测试分页条件查询权限
     *  void
     * @author 杨小波
     */
    @Test
    public void testPagePrivilegeByConditions(){
        System.out.println("******************");
        PrivilegeQueryParams query = new PrivilegeQueryParams();
//        query.setName("权");
//        query.setPrivilegeGroupId(1L);
//        query.setCode("qx1");
        Page<PrivilegeDTO> page = authorizationService
                .pagePrivilegeByConditions(query, 1, 4);
        List<PrivilegeDTO> list = page.getContent();
        System.out.println("查询结果：" + list);
        System.out.println("******************");
    }
    
    /**
     * 测试新增权限组
     * @author 杨小波
     * @date 2018年07月25日 18:20:12
     */
    @Test
    public void testCreatePrivilegeGroup(){
        System.out.println("******************");
        PrivilegeGroupDTO privilegeGroup = new PrivilegeGroupDTO();
        privilegeGroup.setParentId(2L);
        privilegeGroup.setName("组" + new Date().getTime());
        privilegeGroup.setSort(12);
        PrivilegeGroupDTO privilegeGroupDTO = authorizationService.createPrivilegeGroup(privilegeGroup);
        System.out.println("新增成功：" + privilegeGroupDTO);
        
        authorizationService.removePrivilegeGroup(privilegeGroupDTO.getId());
        System.out.println("删除成功");
        System.out.println("******************");
    }
    
    /**
     * 测试修改权限组
     * @author 杨小波
     * @date 2018年07月25日 18:18:23 
     */
    @Test
    public void testModifyPrivilegeGroup() {
        System.out.println("******************");
        PrivilegeGroupDTO privilegeGroup = new PrivilegeGroupDTO();
        privilegeGroup.setParentId(2L);
        privilegeGroup.setName("组" + new Date().getTime());
        privilegeGroup.setSort(12);
        PrivilegeGroupDTO privilegeGroupDTO = authorizationService.createPrivilegeGroup(privilegeGroup);
        System.out.println("新增成功：" + privilegeGroupDTO);
        
        PrivilegeGroupDTO privilegeGroup2 = new PrivilegeGroupDTO();
        privilegeGroup2.setId(privilegeGroup.getId());
        privilegeGroup2.setParentId(2L);
        privilegeGroup2.setName("测试service");
        privilegeGroup2.setSort(13);
        authorizationService.modifyPrivilegeGroup(privilegeGroup2);
        System.out.println("修改成功");
        
        authorizationService.removePrivilegeGroup(privilegeGroupDTO.getId());
        System.out.println("删除成功");
        System.out.println("******************");
    }
    
    /**
     * 测试删除权限组
     * @param  
     * @return void
     * @author 杨小波
     * @date 2018年07月25日 19:02:28
     */
    @Test
    public void testRemovePrivilegeGroup(){
        System.out.println("******************");
        PrivilegeGroupDTO privilegeGroup = new PrivilegeGroupDTO();
        privilegeGroup.setParentId(2L);
        privilegeGroup.setName("组" + new Date().getTime());
        privilegeGroup.setSort(12);
        PrivilegeGroupDTO privilegeGroupDTO = authorizationService.createPrivilegeGroup(privilegeGroup);
        System.out.println("新增成功：" + privilegeGroupDTO);
        
        authorizationService.removePrivilegeGroup(privilegeGroupDTO.getId());
        System.out.println("删除成功");
        System.out.println("******************");
    }

    /**
     * 测试查询权限组
     *  void
     * @author 杨小波
     * @date 2018年07月26日 10:43:51
     */
    @Test
    public void testQueryPrivilegeGroup(){
        System.out.println("******************");
        PrivilegeGroupDTO privilegeGroup = new PrivilegeGroupDTO();
        privilegeGroup.setParentId(2L);
        privilegeGroup.setName("组" + new Date().getTime());
        privilegeGroup.setSort(12);
        PrivilegeGroupDTO privilegeGroupDTO = authorizationService.createPrivilegeGroup(privilegeGroup);
        System.out.println("新增成功：" + privilegeGroupDTO);
        
        PrivilegeGroupDTO dto = authorizationService.queryPrivilegeGroup(privilegeGroupDTO.getId());
        System.out.println("查询成功：" + dto);
        
        authorizationService.removePrivilegeGroup(privilegeGroupDTO.getId());
        System.out.println("删除成功");
        System.out.println("******************");
    }
    
    /**
     * 测试查询全部权限组
     *  void
     * @author 杨小波
     * @date 2018年07月26日 14:27:34
     */
    @Test
    public void testQueryAllPrivilegeGroup(){
        System.out.println("******************");
        List<PrivilegeGroupDTO> list = authorizationService.queryAllPrivilegeGroup();
        System.out.println("查询成功");
        for(PrivilegeGroupDTO dto : list){
            System.out.println(dto.toString());
        }
        System.out.println("******************");
    }
    
    /**
     * 测试新增权限
     *  void
     * @author 杨小波
     * @date 2018年07月26日 15:51:37
     */
    @Test
    public void testCreatePrivilege(){
        System.out.println("******************");
        PrivilegeDTO privilege = new PrivilegeDTO();
        privilege.setCode("qx54564481asd");
        privilege.setName("啊啊啊");
        privilege.setPrivilegeGroupId(3L);
        privilege.setStatus(0);
        PrivilegeDTO createPrivilege = authorizationService.createPrivilege(privilege);
        System.out.println("新增权限成功：" + createPrivilege);
        
        String[] code = {"qx54564481asd"};
        authorizationService.removePrivilege(code);
        System.out.println("删除成功");
        System.out.println("******************");
    }
    
    /**
     * 测试通过list<code> 删除权限
     *  void
     * @author 杨小波
     * @date 2018年07月26日 17:56:33
     */
    @Test
    public void testRemovePrivilege(){
        System.out.println("******************");
        PrivilegeDTO privilege = new PrivilegeDTO();
        privilege.setCode("qx54564481asd");
        privilege.setName("啊啊啊");
        privilege.setPrivilegeGroupId(3L);
        privilege.setStatus(0);
        PrivilegeDTO createPrivilege = authorizationService.createPrivilege(privilege);
        System.out.println("新增权限成功：" + createPrivilege);
        
        String[] code = {"qx54564481asd"};
        authorizationService.removePrivilege(code);
        System.out.println("删除成功");
        System.out.println("******************");
    }

    /**
     * 修改权限
     *  void
     * @author 杨小波
     * @date 2018年07月26日 16:42:22
     */
    @Test
    public void testModifyPrivilege(){
        System.out.println("******************");
        PrivilegeDTO privilege = new PrivilegeDTO();
        privilege.setCode("qx54564481asd");
        privilege.setName("啊啊啊");
        privilege.setPrivilegeGroupId(3L);
        privilege.setStatus(0);
        PrivilegeDTO createPrivilege = authorizationService.createPrivilege(privilege);
        System.out.println("新增权限成功：" + createPrivilege);
        
        PrivilegeDTO p = new PrivilegeDTO();
        p.setPrivilegeGroupId(2L);
        p.setCode("qx54564481asd");
        p.setName("测试权限service");
        p.setStatus(1);
        authorizationService.modifyPrivilege(p);
        System.out.println("修改成功");
        
        String[] code = {"qx54564481asd"};
        authorizationService.removePrivilege(code);
        System.out.println("删除成功");
        System.out.println("******************");
    }
    
    /**
     * 测试根据权限code获取权限
     *  void
     * @author 杨小波
     * @date 2018年07月26日 15:28:45
     */
    @Test
    public void testQueryPrivilege(){
        System.out.println("******************");
        PrivilegeDTO dto = authorizationService.queryPrivilege("admin_page_privilege");
        System.out.println("查询成功：" + dto);
        System.out.println("******************");
    }
    
}
