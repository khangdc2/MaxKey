package org.dromara.maxkey.web.permissions.contorller;
/*
 * Copyright [2022] [MaxKey of copyright http://www.maxkey.top]
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.dromara.maxkey.authn.annotation.CurrentUser;
import org.dromara.maxkey.entity.Message;
import org.dromara.maxkey.entity.RolePrivileges;
import org.dromara.maxkey.entity.UserInfo;
import org.dromara.maxkey.persistence.service.HistorySystemLogsService;
import org.dromara.maxkey.persistence.service.RolePrivilegesService;
import org.dromara.maxkey.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping(value={"/permissions/privileges"})
public class RolePrivilegesController {
	static final Logger logger = LoggerFactory.getLogger(RolePrivilegesController.class);
	
	@Autowired
	RolePrivilegesService rolePrivilegesService;
	
	@Autowired
	HistorySystemLogsService systemLog;
	
	@ResponseBody
	@RequestMapping(value={"/update"}, produces = {MediaType.APPLICATION_JSON_VALUE})
	public  ResponseEntity<?> update(
			@RequestBody RolePrivileges rolePrivileges,
			@CurrentUser UserInfo currentUser) {
		logger.debug("-update  : {}" , rolePrivileges);
		//have
		RolePrivileges queryRolePrivileges = 
				new RolePrivileges(
						rolePrivileges.getAppId(),
						rolePrivileges.getRoleId(),
						currentUser.getInstId());
		List<RolePrivileges> roleRolePrivilegesList = rolePrivilegesService.queryRolePrivileges(queryRolePrivileges);
		
		HashMap<String,String >privilegeMap =new HashMap<String,String >();
		for(RolePrivileges rolePrivilege : roleRolePrivilegesList) {
			privilegeMap.put(rolePrivilege.getUniqueId(),rolePrivilege.getId());
		}
		//Maybe insert
		ArrayList<RolePrivileges> newRolePrivilegesList =new ArrayList<RolePrivileges>();
		List<String>resourceIds = StringUtils.string2List(rolePrivileges.getResourceId(), ",");
		HashMap<String,String >newPrivilegesMap =new HashMap<String,String >();
		for(String resourceId : resourceIds) {
		    RolePrivileges newRolePrivilege=new RolePrivileges(
		    		rolePrivileges.getAppId(),
		    		rolePrivileges.getRoleId(),
                    resourceId,
                    currentUser.getInstId());
		    newRolePrivilege.setId(newRolePrivilege.generateId());
		    newPrivilegesMap.put(newRolePrivilege.getUniqueId(), rolePrivileges.getAppId());
		    
		    if(!rolePrivileges.getAppId().equalsIgnoreCase(resourceId) &&
		            !privilegeMap.containsKey(newRolePrivilege.getUniqueId())) {
		    	newRolePrivilegesList.add(newRolePrivilege);
		    }
		}
		
		//delete 
		ArrayList<RolePrivileges> deleteRolePrivilegesList =new ArrayList<RolePrivileges>();
		for(RolePrivileges rolePrivilege : roleRolePrivilegesList) {
           if(!newPrivilegesMap.containsKey(rolePrivilege.getUniqueId())) {
        	   rolePrivilege.setInstId(currentUser.getInstId());
        	   deleteRolePrivilegesList.add(rolePrivilege);
           }
        }
		if (!deleteRolePrivilegesList.isEmpty()) {
			logger.debug("-remove  : {}" , deleteRolePrivilegesList);
			rolePrivilegesService.deleteRolePrivileges(deleteRolePrivilegesList);
		}
		
		if (!newRolePrivilegesList.isEmpty() && rolePrivilegesService.insertRolePrivileges(newRolePrivilegesList)) {
			logger.debug("-insert  : {}" , newRolePrivilegesList);
			return new Message<RolePrivileges>(Message.SUCCESS).buildResponse();
			
		} else {
			return new Message<RolePrivileges>(Message.SUCCESS).buildResponse();
		}
		
	}
	
	@ResponseBody
    @RequestMapping(value={"/get"}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public  ResponseEntity<?> get(
    		@ModelAttribute RolePrivileges rolePrivileges,
    		@CurrentUser UserInfo currentUser) {
        logger.debug("-get  : {}"  , rolePrivileges);
        //have
        RolePrivileges queryRolePrivilege = 
        		new RolePrivileges(
        				rolePrivileges.getAppId(),
        				rolePrivileges.getRoleId(),
        				currentUser.getInstId());
        List<RolePrivileges> rolePrivilegeList = rolePrivilegesService.queryRolePrivileges(queryRolePrivilege);
        
        return new Message<List<RolePrivileges>>(
        		rolePrivilegeList).buildResponse();
	}

	
}
