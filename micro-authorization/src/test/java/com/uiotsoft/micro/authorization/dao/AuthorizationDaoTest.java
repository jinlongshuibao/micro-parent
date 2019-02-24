package com.uiotsoft.micro.authorization.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.management.relation.Role;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.uiotsoft.micro.authorization.AuthorizationServer;
import com.uiotsoft.micro.authorization.dto.PrivilegeGroupDTO;
import com.uiotsoft.micro.authorization.entity.PrivilegeEntity;
import com.uiotsoft.micro.authorization.entity.PrivilegeGroupEntity;
import com.uiotsoft.micro.authorization.entity.PrivilegeTreeEntity;
import com.uiotsoft.micro.authorization.entity.RoleEntity;


@ActiveProfiles("dev")
@RunWith(SpringRunner.class)
@SpringBootTest (classes = {AuthorizationServer.class})
public class AuthorizationDaoTest {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthorizationDaoTest.class);
    
    
    @Autowired
    private AuthorizationDao authorizationDao;
    
    /**
     *  void
     * @author 杨小波
     * @date 2018年07月30日 16:45:41
     */
    @Test
    public void queryPG(){
        List<PrivilegeTreeEntity> list = authorizationDao.queryPG();
        logger.info("**-**");
        logger.info(list.toString());
    }
    
    /**
     * 
     *  void
     * @author 杨小波
     * @date 2018年07月28日 14:58:18
     */
    @Test
    public void queryOwner_role(){
        List<String> strings = authorizationDao.queryOwner_role("owner_1");
        logger.info("****");
        logger.info("" + strings.toString());
    }
    
    /**
     * 
     *  void
     * @author 杨小波
     * @date 2018年07月28日 09:02:20
     */
    @Test
    public void queryPrivilegesByCodearray(){
        String[] codes = {"qx1", "qx2"};
        List<PrivilegeEntity> list = authorizationDao.queryPrivilegesByCodearray(codes);
        logger.info("****");
        logger.info(list.toString());
    }
    
    /**
     * 修改角色信息
     *  void
     * @author 杨小波
     * @date 2018年07月27日 17:20:08
     */
    @Test
    public void modifyRole(){
        RoleEntity role = new RoleEntity();
        role.setId(8L);
        role.setCode("modify_user_code_123");
        role.setName("name_modify_user");
        
        authorizationDao.modifyRole(role);
    }
    
    /**
     * 根据roleid删除role-privilege关系
     *  void
     * @author 杨小波
     * @date 2018年07月27日 17:01:26
     */
    @Test
    public void removePrivilegesByRoleId(){
        authorizationDao.removePrivilegesByRoleId(9L);
    }
    
    /**
     * 测试添加角色并返回id
     *  void
     * @author 杨小波
     * @date 2018年07月27日 16:15:43
     */
    @Test
    public void createRole(){
        RoleEntity role = new RoleEntity();
        role.setCode("user_code_1234");
        role.setName("user45215");
        authorizationDao.createRole(role);
        logger.info("****" + role.getId());
    }
    
    /**
     * 角色绑定权限
     *  void
     * @author 杨小波
     * @date 2018年07月27日 15:10:32
     */
    @Test
    public void roleBindPrivilege(){
        List<PrivilegeEntity> list = new ArrayList<>();
        PrivilegeEntity p1 = new PrivilegeEntity();
        p1.setId(3L);
        list.add(p1);
        
        PrivilegeEntity p2 = new PrivilegeEntity();
        p2.setId(4L);
        list.add(p2);
        
        PrivilegeEntity p3 = new PrivilegeEntity();
        p3.setId(5L);
        list.add(p3);
        authorizationDao.roleBindPrivilege(2L, list);
    }
    
    /**
     * 权限codeList查权限list
     *  void
     * @author 杨小波
     * @date 2018年07月27日 15:09:16
     */
    @Test
    public void queryPrivileges(){
        List<PrivilegeEntity> list = new ArrayList<>();
        PrivilegeEntity p1 = new PrivilegeEntity();
        p1.setCode("qx1");
        list.add(p1);
        
        PrivilegeEntity p2 = new PrivilegeEntity();
        p2.setCode("qx2");
        list.add(p2);
        
        PrivilegeEntity p3 = new PrivilegeEntity();
        p3.setCode("px4");
        list.add(p3);
        
        List<PrivilegeEntity> list2 = authorizationDao.queryPrivileges(list);
        logger.info("****");
        logger.info(list2.toString());
    }
    
    /**
     * code查询role
     *  void
     * @author 杨小波
     * @date 2018年07月27日 10:25:19
     */
    @Test
    public void queryRole(){
        RoleEntity role = authorizationDao.queryRole("user_132_9541");
        logger.info("****");
        logger.info(role.toString());
    }
    
    /**
     * roleId查询权限
     *  void
     * @author 杨小波
     * @date 2018年07月27日 10:33:44
     */
    @Test
    public void queryRolePrivilege(){
        List<PrivilegeEntity> list = authorizationDao.queryRolePrivilege(1L);
        logger.info("****");
        for(PrivilegeEntity p : list){
            logger.info(p.toString());
        }
    }
    
    /**
     * 测试通过list<code> 删除权限
     *  void
     * @author 杨小波
     * @date 2018年07月26日 17:56:33
     */
    @Test
    public void removePrivilege(){
        String[] list = {"5", "7", "9", "11"};
        authorizationDao.removePrivilege(list);
    }
    
    
    /**
     * 测试新增权限组
     * @param  
     * @return void
     * @author 杨小波
     * @date 2018年07月25日 18:58:43
     */
    @Test
    public void testCreatePrivilegeGroup(){
        PrivilegeGroupDTO privilegeGroup = new PrivilegeGroupDTO();
        privilegeGroup.setParentId(1L);
        privilegeGroup.setName("组" + new Date().getTime());
        authorizationDao.createPrivilegeGroup(PrivilegeGroupDTO
                    .convertDTOToEntity(privilegeGroup));
    }
    
    /**
     * 测试修改权限组
     * @param  
     * @return void
     * @author 杨小波
     * @date 2018年07月25日 18:58:34
     */
    @Test
    public void testModifyPrivilegeGroup(){
        PrivilegeGroupDTO privilegeGroup = new PrivilegeGroupDTO();
        privilegeGroup.setId(3L);
        privilegeGroup.setParentId(2L);
        privilegeGroup.setName("测试修改权限组名称");
        authorizationDao.modifyPrivilegeGroup(PrivilegeGroupDTO
                    .convertDTOToEntity(privilegeGroup));
    }
    
    /**
     * 测试删除权限组
     * @param  
     * @return void
     * @author 杨小波
     * @date 2018年07月25日 18:58:15
     */
    @Test
    public void removePrivilegeGroup(){
        authorizationDao.removePrivilegeGroup(11L);
    }

    /**
     * 测试查询全部权限组
     *  void
     * @author 杨小波
     * @date 2018年07月26日 14:27:34
     */
    @Test
    public void queryAllPrivilegeGroup(){
        List<PrivilegeGroupEntity> list = authorizationDao.queryAllPrivilegeGroup();
        
        logger.info("****");
        for(PrivilegeGroupEntity entity : list){
            logger.info(entity.toString());
        }
        logger.info("****");
        
    }
    
    /**
     * 测试新增权限
     *  void
     * @author 杨小波
     * @date 2018年07月26日 15:51:37
     */
    @Test
    public void createPrivilege(){
        PrivilegeEntity privilege = new PrivilegeEntity();
        privilege.setCode("px3");
        privilege.setName("权限5");
        privilege.setPrivilegeGroupId(3L);
        privilege.setStatus(0);
        authorizationDao.createPrivilege(privilege);
    }
    
    /**
     * 修改权限
     *  void
     * @author 杨小波
     * @date 2018年07月26日 16:42:22
     */
    @Test
    public void modifyPrivilege(){
        PrivilegeEntity p = new PrivilegeEntity();
        p.setId(3L);
        p.setPrivilegeGroupId(2L);
        p.setCode("qx_test_1");
        p.setName("测试权限");
        p.setStatus(1);
        authorizationDao.modifyPrivilege(p);
    }
    
    /**
     * 测试根据权限code获取权限
     *  void
     * @author 杨小波
     * @date 2018年07月26日 15:28:45
     */
    @Test
    public void queryPrivilege(){
        PrivilegeEntity entity = authorizationDao.queryPrivilege("qx1");
        logger.info("****");
        logger.info(entity.toString());
        
    }
}
