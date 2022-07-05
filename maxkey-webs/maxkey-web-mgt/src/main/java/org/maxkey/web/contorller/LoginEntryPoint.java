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
 

package org.maxkey.web.contorller;

import java.util.HashMap;

import org.maxkey.authn.AbstractAuthenticationProvider;
import org.maxkey.authn.LoginCredential;
import org.maxkey.authn.jwt.AuthJwt;
import org.maxkey.authn.jwt.AuthTokenService;
import org.maxkey.configuration.ApplicationConfig;
import org.maxkey.entity.Institutions;
import org.maxkey.entity.Message;
import org.maxkey.web.WebConstants;
import org.maxkey.web.WebContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.MediaType;


/**
 * @author Crystal.Sea
 *
 */
@Controller
@RequestMapping(value = "/login")
public class LoginEntryPoint {
	private static Logger _logger = LoggerFactory.getLogger(LoginEntryPoint.class);
	
	@Autowired
	AuthTokenService authTokenService;
	
	@Autowired
  	protected ApplicationConfig applicationConfig;
 	
	@Autowired
	AbstractAuthenticationProvider authenticationProvider ;
	
	/**
	 * init login
	 * @return
	 */
 	@RequestMapping(value={"/get"}, produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> get() {
		_logger.debug("/login.");
		
		HashMap<String , Object> model = new HashMap<String , Object>();
		model.put("isRemeberMe", applicationConfig.getLoginConfig().isRemeberMe());
		Institutions inst = (Institutions)WebContext.getAttribute(WebConstants.CURRENT_INST);
		model.put("inst", inst);
		if(applicationConfig.getLoginConfig().isCaptcha()) {
			model.put("captcha", "true");
		}else {
			model.put("captcha", inst.getCaptchaSupport());
			model.put("captchaType", inst.getCaptchaType());
		}
		model.put("state", authTokenService.genRandomJwt());
		return new Message<HashMap<String , Object>>(model).buildResponse();
	}
 	
 	@RequestMapping(value={"/signin"}, produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> signin( @RequestBody LoginCredential loginCredential) {
 		Message<AuthJwt> authJwtMessage = new Message<AuthJwt>(Message.FAIL);
 		if(authTokenService.validateJwtToken(loginCredential.getState())){
	 		Authentication  authentication  = authenticationProvider.authenticate(loginCredential);
	 		if(authentication != null) {
	 			AuthJwt authJwt = authTokenService.genAuthJwt(authentication);
	 			authJwtMessage = new Message<AuthJwt>(authJwt);
	 		}else {//fail
 				String errorMsg = WebContext.getAttribute(WebConstants.LOGIN_ERROR_SESSION_MESSAGE) == null ? 
						  "" : WebContext.getAttribute(WebConstants.LOGIN_ERROR_SESSION_MESSAGE).toString();
				authJwtMessage.setMessage(Message.FAIL,errorMsg);
				_logger.debug("login fail , message {}",errorMsg);
	 		}
 		}
 		return authJwtMessage.buildResponse();
 	}
 	
}
