package com.uiotsoft.micro.authorization.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uiotsoft.micro.authorization.dao.AuthorizationDao;
import com.uiotsoft.micro.authorization.dto.AuthorizationInfoDTO;
import com.uiotsoft.micro.authorization.dto.PrivilegeDTO;
import com.uiotsoft.micro.authorization.dto.PrivilegeGroupDTO;
import com.uiotsoft.micro.authorization.dto.PrivilegeQueryParams;
import com.uiotsoft.micro.authorization.dto.PrivilegeTreeDTO;
import com.uiotsoft.micro.authorization.dto.RoleDTO;
import com.uiotsoft.micro.authorization.dto.RoleQueryParams;
import com.uiotsoft.micro.authorization.entity.PrivilegeEntity;
import com.uiotsoft.micro.authorization.entity.PrivilegeGroupEntity;
import com.uiotsoft.micro.authorization.entity.PrivilegeTreeEntity;
import com.uiotsoft.micro.authorization.entity.RoleEntity;
import com.uiotsoft.micro.common.domain.BusinessException;
import com.uiotsoft.micro.common.domain.ErrorCode;
import com.uiotsoft.micro.common.domain.PageRequestParams;
import com.uiotsoft.micro.common.util.VerifyUtil;

/**  
 * 
 * @author:杨小波
 * @date:2018年7月25日  
 */
@Service
@Transactional
public class AuthorizationServiceImpl implements AuthorizationService {
    
    @Autowired
    private AuthorizationDao authorizationDao; 
    
    private static final Logger logger = LoggerFactory
                .getLogger(AuthorizationServiceImpl.class);
    
    
    @Override
    public PrivilegeTreeDTO queryChildPrivilegeTree(String pgroupId) {
        //非空
        if(StringUtils.isBlank(pgroupId)){
            throw new BusinessException(ErrorCode.E_120126);
        }
        //是否正确的long（格式以及是否超过long范围）
        try{
            Long.valueOf(pgroupId);
        }catch (NumberFormatException e) {
            throw new BusinessException(ErrorCode.E_100105);
        }
        
        /*
         * 1.将所有数据（权限+权限组）查询出来放入List
         */
        //查询所有权限
        logger.info("查询需要的权限");
        List<PrivilegeTreeDTO> pList = queryPTree(null);
        //查询所有权限组
        logger.info("查询需要的权限组");
        List<PrivilegeTreeDTO> pgList = PrivilegeTreeDTO.convertEntitysToDTOs(
                    authorizationDao.queryPG());
        if(pList == null || pList.size() == 0){
            throw new BusinessException(ErrorCode.E_120127);
        }
        if(pgList == null || pgList.size() == 0){
            throw new BusinessException(ErrorCode.E_120106);
        }
        pList.addAll(pgList);
        /*
         * 2.构建权限树
         */
        //查找要查询的节点（权限组）
        for(PrivilegeTreeDTO p : pList){
            if( pgroupId.equals(p.getId()) ){
                //查找到之后设置子节点
                p.setChildren(getChild(pgroupId, pList));
                return p;
            }
        }
        
        //未查找到pgroupId对应的节点，则会抛出异常
        throw new BusinessException(ErrorCode.E_120105);
    }

    @Override
    public PrivilegeTreeDTO queryPrivilegeTree(String[] roleCodes) {
        /*
         * 1.查询原始数据，普通list（非树形结构）
         */
        //查询权限
        List<PrivilegeTreeDTO> plist = queryPTree(roleCodes);
        //查询权限组
        List<PrivilegeTreeDTO> pglist = PrivilegeTreeDTO.convertEntitysToDTOs(authorizationDao.queryPG());
        //存储筛选后的权限组（去除无关节点）
        List<PrivilegeTreeDTO> classifyPGList = new ArrayList<>();
        //筛选权限组，去除无用的
        for(PrivilegeTreeDTO p : plist){
            classifyList(p.getParentId(), pglist, classifyPGList);
        }
        /*
         * 2.构建权限树
         */
        //存放全部原始数据（权限+权限组）
        List<PrivilegeTreeDTO> p_pgList = new ArrayList<>();
        p_pgList.addAll(plist);
        p_pgList.addAll(classifyPGList);
        //存放结果
        List<PrivilegeTreeDTO> pTree = new ArrayList<>();
        
        // 找到根节点
        for (int i = 0; i < p_pgList.size(); i++) {
            // 寻找根节点
            if (p_pgList.get(i).getParentId().equals("-1")) {
                pTree.add(p_pgList.get(i));
            }
        }
        // 为根节点设置子菜单，getChild是递归调用的
        for (PrivilegeTreeDTO pt : pTree) {
            pt.setChildren(getChild(pt.getId(), p_pgList));
        }
        
//        logger.info("**-**权限树");
//        logger.info(pTree.get(0).toString());
//        logger.info("**-**权限树结束");
        
        return pTree.get(0);
    }
    
    /**
     * 子函数1（queryPrivilegeTree）
     * 子函数2（queryChildPrivilegeTree）
     * 递归查找子菜单
     * @param id：当前菜单的id
     * @param p_pgList：要查找的list（原始list）
     * @return List<PrivilegeTreeDTO>
     * @author 杨小波
     * @date 2018年07月30日 16:59:41
     */
    public List<PrivilegeTreeDTO> getChild(String id, 
                List<PrivilegeTreeDTO> p_pgList){
        // 子菜单
        List<PrivilegeTreeDTO> childList = new ArrayList<>();
        // 找到传入id的节点的 子节点
        for (PrivilegeTreeDTO p : p_pgList) {
            // 遍历所有节点，将父菜单id与传过来的id比较
            if (p.getParentId().equals(id)) {
                childList.add(p);
            }
        }
        
        // 把子菜单的子菜单再循环一遍
        for (PrivilegeTreeDTO p : childList) {
            //如果还有子菜单，则继续进入if递归
            if (p.isGroup()) {
                p.setChildren(getChild(p.getId(), p_pgList));
            }
        }
        
        // 递归退出条件
        if (childList.size() == 0) {
            return null;
        }
        return childList;
    }
    
    /**
     * 找出需要的权限组（删除无用的）
     * @param id 当前节点id
     * @param list 总list
     * @return PrivilegeTreeDTO
     * @author 杨小波
     * @date 2018年07月30日 18:24:22
     */
    public void classifyList(String pId, List<PrivilegeTreeDTO> list, List<PrivilegeTreeDTO> classifyPGList){
        for(PrivilegeTreeDTO p : list){
            //如果找到父节点，加入结果List，并对父节点继续递归
            if(p.getId().equals(pId)){
                boolean isExist = false;
                for(PrivilegeTreeDTO pt : classifyPGList){
                    if(p.getId().equals(pt.getId())){
                        isExist = true;
                    }
                }
                if(!isExist){
                    classifyPGList.add(p);
                }
                classifyList(p.getParentId(), list, classifyPGList);
            }
        }
    }
    
    /**
     * 子函数（queryPrivilegeTree）
     * 通过角色code查找角色对应的权限List
     */
    public List<PrivilegeTreeDTO> queryPTree(String... roleCodes){
        /*
         * 通过角色code查找角色对应的权限List
         */
        List<PrivilegeTreeEntity> pList = authorizationDao.queryPTByRC(roleCodes);
        if(pList == null || pList.size() == 0){
            logger.info("权限编码对应的权限为空");
            throw new BusinessException(ErrorCode.E_120121);
        }
        
        return PrivilegeTreeDTO.convertEntitysToDTOs(pList);
    }
    
    
    
    
    @Override
    public AuthorizationInfoDTO authorize(String owner) {
        logger.info("[授权]{}", owner);
        //非空校验
        if(StringUtils.isBlank(owner)){
            throw new BusinessException(ErrorCode.E_120122);
        }
        //长度校验
        if(!VerifyUtil.verifyLength(owner, 50)){
            throw new BusinessException(ErrorCode.E_120146);
        }
        //特殊字符校验
//        if(!VerifyUtil.verifyCharacter(owner)){
//            throw new BusinessException(ErrorCode.E_120147);
//        }
        
        AuthorizationInfoDTO dto = new AuthorizationInfoDTO();
        Map<String,String[]> rolePrivilegeMap = new HashMap<>();//角色-权限列表 映射
        /*
         * 1.查询该owner对应的role的list
         */
        if(StringUtils.isBlank(owner)){
            throw new BusinessException(ErrorCode.E_120123);
        }
        List<RoleEntity> roleOfOwner = authorizationDao.queryRoleOfOwner(owner);
        logger.info("[角色拥有者]{}", owner);
        logger.info("[角色拥有着拥有的角色]{}", roleOfOwner);
        if(roleOfOwner != null && roleOfOwner.size() != 0){
            /*
             * 2.循环查找每个role对应的权限List，并将每次返回的list以("role", "priList")封装
             */
            for(RoleEntity role : roleOfOwner){
                List<String> privilegeList = authorizationDao.queryPrivilegesOfRole(role.getId());
                logger.info("[权限]{}",privilegeList);
                String[] priArray = privilegeList.toArray(new String[privilegeList.size()]);
                rolePrivilegeMap.put(role.getCode(), priArray);
            }
            
            /*
             * 3.包装为DTO返回
             */
            dto.setRolePrivilegeMap(rolePrivilegeMap);
        }
        return dto;
    }
    
    

    @Override
    public void bind(String owner, String[] roleCodes) {
        if(StringUtils.isBlank(owner)){
            throw new BusinessException(ErrorCode.E_120122);
        }
        //移除旧权限
        Integer effection = authorizationDao.removeOwner_role(owner);
        //如果权限为空则不绑定新权限（用户可能想要解绑全部权限）
        if(roleCodes != null && roleCodes.length != 0){
            //数组转list
            List<String> roleCodesList = Arrays.asList(roleCodes);
            //增加新权限
            authorizationDao.bind(owner, roleCodesList);
            logger.info("[新权限绑定成功]{}", roleCodesList);
        }
    }
    
    public void unbind(String owner, String[] roleCodes) {
        if(StringUtils.isBlank(owner)){
            throw new BusinessException(ErrorCode.E_120122);
        }
        if(roleCodes == null || roleCodes.length == 0){
            throw new BusinessException(ErrorCode.E_120116);
        }
        List<RoleEntity> roleOfOwner = authorizationDao.queryRoleOfOwner(owner);
        if(roleOfOwner == null || roleOfOwner.size() == 0){
            throw new BusinessException(ErrorCode.E_120148);
        }
        Integer unbindResult = authorizationDao.unbind(owner, roleCodes);
        if(unbindResult == 0){
            throw new BusinessException(ErrorCode.E_120149);
        }
    }

    @Override
    public PrivilegeGroupDTO createPrivilegeGroup(PrivilegeGroupDTO privilegeGroup) {
        /*
         * 参数校验
         */
        if(privilegeGroup == null){
            throw new BusinessException(ErrorCode.E_100101);
        }
        //判断必要参数是否为空
        if(privilegeGroup.getParentId() == null || StringUtils.isBlank(privilegeGroup.getName())){
            throw new BusinessException(ErrorCode.E_120103);
        }
        //去除首尾空格
        privilegeGroup.setName(privilegeGroup.getName());
        
        if(privilegeGroup.getName().length() > 50){
            throw new BusinessException(ErrorCode.E_120137);
        }
        
        PrivilegeGroupEntity groupEntity = PrivilegeGroupDTO.convertDTOToEntity(privilegeGroup);
        authorizationDao.createPrivilegeGroup(groupEntity);
        //获取新增数据的id
        Long id = groupEntity.getId();
        //查询插入数据的对象并返回
        PrivilegeGroupEntity privilegeGroupEntity = authorizationDao.queryPrivilegeGroup(id);
        return PrivilegeGroupDTO.convertEntityToDTO(privilegeGroupEntity);
    }

    @Override
    public void modifyPrivilegeGroup(PrivilegeGroupDTO privilegeGroup) {
        if(privilegeGroup == null){
            throw new BusinessException(ErrorCode.UNKOWN);
        }
        //判断必要参数是否为空
        if(StringUtils.isBlank(privilegeGroup.getName()) || privilegeGroup.getId() == null){
            throw new BusinessException(ErrorCode.E_120103);
        }
        //去除首尾空格
        privilegeGroup.setName(privilegeGroup.getName().trim());
        
        if(privilegeGroup.getName().length() > 50){
            throw new BusinessException(ErrorCode.E_120137);
        }
        //判断要修改的数据是否存在
        PrivilegeGroupEntity entity = authorizationDao.queryPrivilegeGroup(privilegeGroup.getId());
        if(entity == null){
            throw new BusinessException(ErrorCode.E_120105);
        }
        
        //执行修改
        authorizationDao.modifyPrivilegeGroup(PrivilegeGroupDTO.convertDTOToEntity(privilegeGroup));        
    }

    @Override
    public void removePrivilegeGroup(Long id) {
        if(id == null){
            throw new BusinessException(ErrorCode.E_120104);
        }
        //判断权限组下是否有子权限组
        List<PrivilegeGroupEntity> childPG = authorizationDao.queryChildPG(id);
        if(!childPG.isEmpty()){
            throw new BusinessException(ErrorCode.E_120128);
        }
        //判断权限组下是否有权限
        List<PrivilegeEntity> childP = authorizationDao.queryChildrenPrivilege(id);
        if(!childP.isEmpty()){
            throw new BusinessException(ErrorCode.E_120129);
        }
        
        authorizationDao.removePrivilegeGroup(id);
    }

    @Override
    public PrivilegeGroupDTO queryPrivilegeGroup(Long id) {
        if(id == null){
            throw new BusinessException(ErrorCode.E_120104);
        }
        PrivilegeGroupEntity entity = authorizationDao.queryPrivilegeGroup(id);
        //判断查询的信息是否存在
        if(entity == null){
            throw new BusinessException(ErrorCode.E_120105);
        }
        //将entity转换成dto并返回
        return PrivilegeGroupDTO.convertEntityToDTO(entity);
    }

    @Override
    public List<PrivilegeGroupDTO> queryAllPrivilegeGroup() {
        List<PrivilegeGroupEntity> list = authorizationDao.queryAllPrivilegeGroup();
        List<PrivilegeGroupDTO> dtoList = PrivilegeGroupDTO.convertEntitysToDTOs(list);
        return dtoList;
    }

    public List<PrivilegeDTO> queryPrivileges(Long id) {
        if(id == null){
            throw new BusinessException(ErrorCode.E_120104);
        }
        List<PrivilegeEntity> entityList = authorizationDao.queryChildrenPrivilege(id);
        List<PrivilegeDTO> dtoList = PrivilegeDTO.convertEntitysToDTOs(entityList);
        return dtoList;
    }
    
    @Override
    public PrivilegeDTO createPrivilege(PrivilegeDTO privilege) {
        if(privilege == null){
            throw new BusinessException(ErrorCode.E_100101);
        }
        
        //判断必要参数是否为空
        if(privilege.getPrivilegeGroupId() == null || StringUtils.isBlank(privilege.getCode()) || StringUtils.isBlank(privilege.getName())){
            throw new BusinessException(ErrorCode.E_120107);
        }
        
        //去除首尾空格
        privilege.setName(privilege.getName().trim());
        privilege.setCode(privilege.getCode().trim());
        
        if(privilege.getName().length() > 50){
            throw new BusinessException(ErrorCode.E_120138);
        }
        if(privilege.getCode().length() > 50){
            throw new BusinessException(ErrorCode.E_120139);
        }
        /*
         * 重复性判断
         */
        //判断权限编码是否存在
        PrivilegeEntity privilegeEntity = authorizationDao.queryPrivilege(privilege.getCode());
        if(privilegeEntity != null){
            throw new BusinessException(ErrorCode.E_120108);
        }
        //判断权限名称是否存在
        List<PrivilegeEntity> entitysByName = authorizationDao.selectPrivilegeEntityByName(privilege.getName());
        if(!entitysByName.isEmpty()){
            throw new BusinessException(ErrorCode.E_120134);
        }
        PrivilegeEntity convertDTOToEntity = PrivilegeDTO.convertDTOToEntity(privilege);
        authorizationDao.createPrivilege(convertDTOToEntity);
        Long id = convertDTOToEntity.getId();
        if(id == null){
            throw new BusinessException(ErrorCode.E_120136);
        }
        
        PrivilegeEntity selectPrivilegeById = authorizationDao.selectPrivilegeById(id);
        return PrivilegeDTO.convertEntityToDTO(selectPrivilegeById);
    }

    @Override
    public void modifyPrivilege(PrivilegeDTO privilege) {
        if(privilege == null){
            throw new BusinessException(ErrorCode.UNKOWN);
        }
        //判断参数
        if(privilege.getPrivilegeGroupId() == null
                || StringUtils.isBlank(privilege.getName())
                || StringUtils.isBlank(privilege.getCode())
                ){
            throw new BusinessException(ErrorCode.E_120111);
        }
        //去除首尾空格
        privilege.setName(privilege.getName().trim());
        privilege.setCode(privilege.getCode().trim());
        
        if(privilege.getName().length() > 50){
            throw new BusinessException(ErrorCode.E_120138);
        }
        
        authorizationDao.modifyPrivilege(PrivilegeDTO
                    .convertDTOToEntity(privilege));

    }

    @Override
    public void removePrivilege(String[] privilegeCodes) {
        if(privilegeCodes == null){
            throw new BusinessException(ErrorCode.UNKOWN);
        }
        if(privilegeCodes.length == 0){
            throw new BusinessException(ErrorCode.E_120112);
        }
        //判断权限是否绑定角色（有则不允许删除）
        List<Integer> listR_Pri = authorizationDao.queryR_Pri(privilegeCodes);
        if(!listR_Pri.isEmpty()){
            throw new BusinessException(ErrorCode.E_120130);
        }
        authorizationDao.removePrivilege(privilegeCodes);
    }

    @Override
    public PrivilegeDTO queryPrivilege(String privilegeCode) {
        if(StringUtils.isBlank(privilegeCode)){
            throw new BusinessException(ErrorCode.E_120109);
        }
        PrivilegeEntity privilege = authorizationDao
                    .queryPrivilege(privilegeCode);
        if(privilege == null){
            throw new BusinessException(ErrorCode.E_120110);
        }
        return PrivilegeDTO.convertEntityToDTO(privilege);
    }

    @Override
    public Page<PrivilegeDTO> pagePrivilegeByConditions(PrivilegeQueryParams query, Integer pageNo, Integer pageSize) {
        //判断页码和每页记录数是否有错误
        if(pageNo < 1){
            throw new BusinessException(ErrorCode.E_120114);
        }
        if(pageSize < 1){
            throw new BusinessException(ErrorCode.E_120115);
        }
        //计算分页参数
        PageRequestParams pageRequest = PageRequestParams.of(pageNo, pageSize);
        //条件查询
        List<PrivilegeEntity> entityList = authorizationDao.pagePrivilegeByConditions(query, pageRequest);
        //判断是否查询到符合条件的信息
        if(entityList == null || entityList.size() == 0){
            throw new BusinessException(ErrorCode.E_120113);
        }
        //查询符合条件的总数
        Long total = authorizationDao.countePrivilegeByConditions(query);
        //封装pageable
        Pageable pageable = PageRequest.of(pageNo-1, pageSize);
        List<PrivilegeDTO> dtoList = PrivilegeDTO.convertEntitysToDTOs(entityList);
        //封装page
        Page<PrivilegeDTO> page = new PageImpl<>(dtoList,pageable,total);
        return page;
    }

    @Override
    @Transactional
    public RoleDTO createRole(RoleDTO roleDTO) {
        /*
         * 1.判断并创建角色
         */
        if(StringUtils.isBlank(roleDTO.getCode()) ){
            throw new BusinessException(ErrorCode.E_120116);
        }
        if(!VerifyUtil.verifyLength(roleDTO.getCode(), 50) ){
            throw new BusinessException(ErrorCode.E_120141);
        }
        if(!VerifyUtil.verifyCharacter(roleDTO.getCode()) ){
            throw new BusinessException(ErrorCode.E_120144);
        } 
        if(StringUtils.isBlank(roleDTO.getName()) ){
            throw new BusinessException(ErrorCode.E_120119);
        }
        if(!VerifyUtil.verifyLength(roleDTO.getName(), 50) ){
            throw new BusinessException(ErrorCode.E_120140);
        }
        
        //去除首尾空格
        roleDTO.setName(roleDTO.getName().trim());
        roleDTO.setCode(roleDTO.getCode().trim());
        
        //校验角色code是否重复
        List<RoleEntity> roleByCode = authorizationDao.selectRoleByCode(roleDTO.getCode());
        if(!roleByCode.isEmpty()){
            throw new BusinessException(ErrorCode.E_120135);
        }
        
        RoleEntity role = RoleDTO.convertDTOToEntity(roleDTO);
        //新增成功，mybatis自动将id返回并设置在roleEntity中
        authorizationDao.createRole(role);
        logger.info("[角色创建成功]>>{}", role);
        
        Long id = role.getId();
        /*
         * 2.绑定角色权限
         */
        if(roleDTO.getPrivileges() != null && roleDTO.getPrivileges().size() > 0){
        	//此list中仅包含权限编码（前台传入），没有id等信息
            List<PrivilegeEntity> privilegeCodes = PrivilegeDTO.convertDTOsToEntitys(roleDTO.getPrivileges());
            //2.2通过前台传入的一组权限编码查询对应的权限id  
            List<PrivilegeEntity> privilegeList = authorizationDao.queryPrivileges(privilegeCodes);
            if(privilegeList == null || privilegeList.size() == 0){
                throw new BusinessException(ErrorCode.E_120121);
            }
            /*
             * 3.添加权限
             */
            authorizationDao.roleBindPrivilege(role.getId(), privilegeList);
        }
        
        //返回新增的对象
        RoleEntity selectRoleById = authorizationDao.selectRoleById(id);
        return RoleDTO.convertEntityToDTO(selectRoleById);
    }

    @Override
    public void roleBindPrivilege(String roleCode, String[] privilegeCodes) {
        /*
         * 1.通过角色code查询角色信息（id）
         */
        if(StringUtils.isBlank(roleCode)){
            throw new BusinessException(ErrorCode.E_120116);
        }
        RoleEntity roleEntity = authorizationDao.queryRole(roleCode);
        if(roleEntity == null){
            throw new BusinessException(ErrorCode.E_120117);
        }
        
        authorizationDao.removePrivilegesByRoleId(roleEntity.getId());
        
        /*
         * 2.通过一组权限code查询一组权限（id）并绑定。如果权限code为空，可能用户想要解绑权限
         */
        if(privilegeCodes != null && privilegeCodes.length != 0){
            List<PrivilegeEntity> newPrivilegeList = authorizationDao.queryPrivilegesByCodearray(privilegeCodes);
            if(newPrivilegeList == null || newPrivilegeList.size() == 0){
                throw new BusinessException(ErrorCode.E_120121);
            }
            authorizationDao.roleBindPrivilege(roleEntity.getId(), newPrivilegeList);
        }
        
    }

    @Override
    @Transactional
    public void modifyRole(RoleDTO roleDTO) {
        logger.info("[修改角色]{}", roleDTO);
        //非空校验
        if(StringUtils.isBlank(roleDTO.getName())){
            throw new BusinessException(ErrorCode.E_120119);
        }
        if(StringUtils.isBlank(roleDTO.getCode())){
            throw new BusinessException(ErrorCode.E_120116);
        }
        //长度校验
        if(!VerifyUtil.verifyLength(roleDTO.getName(), 50)){
            throw new BusinessException(ErrorCode.E_120140);
        }
        if(!VerifyUtil.verifyLength(roleDTO.getCode(), 50)){
            throw new BusinessException(ErrorCode.E_120141);
        }
        //特殊字符校验
        if(!VerifyUtil.verifyCharacter(roleDTO.getCode())){
            throw new BusinessException(ErrorCode.E_120144);
        }
        
        //去除首尾空格
        roleDTO.setName(roleDTO.getName().trim());
        roleDTO.setCode(roleDTO.getCode().trim());
        
        //执行修改并返回受影响的行数
        Integer effection = authorizationDao.modifyRole(RoleDTO.convertDTOToEntity(roleDTO));
        if(effection == 0){
            logger.info("[角色编码对应的数据不存在]");
            throw new BusinessException(ErrorCode.E_120145);
        }
        //将查询到的id存入，以便返回controller
        RoleEntity role = authorizationDao.queryRole(roleDTO.getCode());
        roleDTO.setId(role.getId());
        //修改权限（如果存在权限code）
        if(roleDTO.getPrivileges() != null && roleDTO.getPrivileges().size() != 0){
            authorizationDao.removePrivilegesByRoleId(role.getId());
            logger.info("[旧权限删除成功]");
        }
        if(roleDTO.getPrivileges().size() > 0){
            List<PrivilegeEntity> privileges = authorizationDao.queryPrivileges(PrivilegeDTO.convertDTOsToEntitys(roleDTO.getPrivileges()));
            authorizationDao.roleBindPrivilege(role.getId(), privileges);
            logger.info("[新权限绑定成功]{}", privileges);
        }
    }

    @Override
    public void removeRole(String[] roleCodes) {
        
        /*
         * 0.根据code批量查询角色id
         */
        if(roleCodes == null || roleCodes.length == 0){
            throw new BusinessException(ErrorCode.E_120116);
        }
        List<RoleEntity> roles = authorizationDao.queryRolesByCodes(roleCodes);
        for(String role: roleCodes){
            logger.info("要删除的角色id:{}", role);
        }
        
        /*
         * 1.判断角色是否绑定有拥有者
         */
        List<String> listO_Role = authorizationDao.queryO_Role(roleCodes);
        if(!listO_Role.isEmpty()){
            throw new BusinessException(ErrorCode.E_120131);
        }
        
        /*
         * 2.根据角色id批量删除角色
         */
        if(roles == null || roles.size() == 0){
            throw new BusinessException(ErrorCode.E_120117);
        }
        authorizationDao.removeRolesByIds(roles);
        
        /*
         * 3.根据角色id批量删除角色-权限关系
         */
        authorizationDao.removeRole_pri(roles);

    }

    @Override
    public RoleDTO queryRole(String roleCode) {
        if(roleCode == null){
            logger.info("角色编码为空");
            throw new BusinessException(ErrorCode.E_120116);
        }
        RoleEntity role = authorizationDao.queryRole(roleCode);
        if(role == null){
            logger.info("查询的角色不存在");
            throw new BusinessException(ErrorCode.E_120117);
        }
        List<PrivilegeEntity> list = authorizationDao.queryRolePrivilege(role.getId());
        //dto转entity
        RoleDTO roleDTO = new RoleDTO();
        roleDTO = RoleDTO.convertEntityToDTO(role);
        roleDTO.setPrivileges(PrivilegeDTO.convertEntitysToDTOs(list));
        return roleDTO;
    }

    @Override
    public List<RoleDTO> queryRole(String... roleCodes) {
        if(roleCodes == null || roleCodes.length == 0){
            logger.info("角色编码为空");
            throw new BusinessException(ErrorCode.E_120116);
        }
        List<RoleEntity> rolesEntity = authorizationDao.queryRolesByCodes(roleCodes);
        if(rolesEntity == null || rolesEntity.size() == 0){
            throw new BusinessException(ErrorCode.E_120117);
        }
        return RoleDTO.convertEntitysToDTOs(rolesEntity);
    }

    @Override
    public Page<RoleDTO> pageRoleByConditions(RoleQueryParams query, Integer pageNo, Integer pageSize) {
        //判断页码和每页记录数是否有错误
        if(pageNo < 1){
            throw new BusinessException(ErrorCode.E_120114);
        }
        if(pageSize < 1){
            throw new BusinessException(ErrorCode.E_120115);
        }
        //计算分页参数
        PageRequestParams pageRequest = PageRequestParams.of(pageNo, pageSize);
        //条件查询
        List<RoleEntity> entityList = authorizationDao.pageRoleByConditions(query, pageRequest);
        //查询符合条件的总数
        Long total = authorizationDao.countRoleByConditions(query);
        //封装pageable
        Pageable pageable = PageRequest.of(pageNo-1, pageSize);
        List<RoleDTO> dtoList = RoleDTO.convertEntitysToDTOs(entityList);
        //封装page
        Page<RoleDTO> page = new PageImpl<>(dtoList,pageable,total);
        return page;
    }

}
