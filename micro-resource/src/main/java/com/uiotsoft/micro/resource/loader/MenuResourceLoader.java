package com.uiotsoft.micro.resource.loader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.uiotsoft.micro.resource.dto.MenuDTO;
import com.uiotsoft.micro.resource.service.MenuService;


@Order(1)
@Component
public class MenuResourceLoader implements ResourceLoader {
	
	@Autowired
	private MenuService menuService;

	@Override
	public String getType() {
		return "menu";
	}

	@Override
	public String load(List<String> privileageCodes, String applicationCode) {
		List<MenuDTO> allMenus = menuService.queryMenuByApplicationCode(applicationCode);
		//命中所有叶子节点
		List<MenuDTO> leafs = new ArrayList<>();
		for(MenuDTO menu : allMenus){
			if(privileageCodes.contains(menu.getPrivilegeCode())){
				leafs.add(menu);
			}
		}
		//根据叶子节点向上查找，形成有效节点集合
		Set<MenuDTO> effectiveMenus = new HashSet<>();
		for(MenuDTO leaf : leafs){
			addParentLine(effectiveMenus, leaf, allMenus);
		}
		//根据有效节点集合还原树形结构
		MenuDTO root = findRoot(effectiveMenus);
		if(root == null){
			return new JSONObject().toJSONString();
		}
		return fillTree(root, effectiveMenus).toJSONString();
	}

	private JSONObject fillTree(MenuDTO node,  Set<MenuDTO> effectiveMenus) {
		JSONObject json = (JSONObject)JSONObject.toJSON(node);
		List<MenuDTO> childs = findChilds(node, effectiveMenus);
		JSONArray childsJson = new JSONArray();
		for(MenuDTO child : childs){
			childsJson.add(fillTree(child, effectiveMenus));
		}
		json.put("childs", childsJson);
		return json;
	}

	private void addParentLine(Set<MenuDTO> effectiveMenus, MenuDTO leaf, List<MenuDTO> allMenus) {
		effectiveMenus.add(leaf);
		MenuDTO parent = findParent(leaf, allMenus);
		if(parent != null){
			addParentLine(effectiveMenus, parent, allMenus);
		}
	}

	private MenuDTO findParent(MenuDTO leaf, Collection<MenuDTO> menus) {
		for(MenuDTO m : menus){
			if(m.getId().equals(leaf.getParentId())){
				return m;
			}
		}
		return null;
	}
	
	private List<MenuDTO> findChilds(MenuDTO leaf, Collection<MenuDTO> menus) {
		List<MenuDTO> childs = new ArrayList<>();
		for(MenuDTO m : menus){
			if(leaf.getId().equals(m.getParentId())){
				childs.add(m);
			}
		}
		Collections.sort(childs, new Comparator<MenuDTO>(){
			@Override
			public int compare(MenuDTO o1, MenuDTO o2) {
				Integer s1 = Integer.MAX_VALUE;
				Integer s2 = Integer.MAX_VALUE;
				if(o1.getSort() != null){
					s1 = o1.getSort();
				}
				if(o2.getSort() != null){
					s2 = o2.getSort();
				}	
				return s1.compareTo(s2);
			}
		});
		return childs;
	}

	
	private MenuDTO findRoot(Collection<MenuDTO> menus) {
		for(MenuDTO m : menus){
			if(m.getParentId() == null){
				return m;
			}
		}
		return null;
	}
}
