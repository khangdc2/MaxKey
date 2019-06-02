/**
 * 
 */
package org.maxkey.domain.apps;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Crystal.Sea
 *
 */
@Table(name = "TOKEN_BASED_DETAILS") 
public class TokenBasedDetails  extends Applications {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1717427271305620545L;

	@Id
	@Column
	@GeneratedValue(strategy=GenerationType.AUTO,generator="uuid")
	protected String id;
	/**
	 * 
	 */
	@Column
	private String redirectUri;
	//
	@Column
	private String cookieName;
	@Column
	private String algorithm;
	@Column
	private String algorithmKey;
	@Column
	private String expires;
	
	//
	@Column
	private int uid;
	@Column
	private int username;
	@Column
	private int email;
	@Column
	private int windowsAccount;
	@Column
	private int employeeNumber;
	@Column
	private int departmentId;
	@Column
	private int department;
	
	
	
	public TokenBasedDetails() {
		super();
	}



	public String getRedirectUri() {
		return redirectUri;
	}



	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
	}



	public String getCookieName() {
		return cookieName;
	}



	public void setCookieName(String cookieName) {
		this.cookieName = cookieName;
	}



	public String getAlgorithm() {
		return algorithm;
	}



	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}



	public String getAlgorithmKey() {
		return algorithmKey;
	}



	public void setAlgorithmKey(String algorithmKey) {
		this.algorithmKey = algorithmKey;
	}



	public String getExpires() {
		return expires;
	}



	public void setExpires(String expires) {
		this.expires = expires;
	}



	public int getUid() {
		return uid;
	}



	public void setUid(int uid) {
		this.uid = uid;
	}



	public int getUsername() {
		return username;
	}



	public void setUsername(int username) {
		this.username = username;
	}



	public int getEmail() {
		return email;
	}



	public void setEmail(int email) {
		this.email = email;
	}



	public int getWindowsAccount() {
		return windowsAccount;
	}



	public void setWindowsAccount(int windowsAccount) {
		this.windowsAccount = windowsAccount;
	}



	public int getEmployeeNumber() {
		return employeeNumber;
	}



	public void setEmployeeNumber(int employeeNumber) {
		this.employeeNumber = employeeNumber;
	}



	public int getDepartmentId() {
		return departmentId;
	}



	public void setDepartmentId(int departmentId) {
		this.departmentId = departmentId;
	}



	public int getDepartment() {
		return department;
	}



	public void setDepartment(int department) {
		this.department = department;
	}



	@Override
	public String toString() {
		return "TokenBasedDetails [redirectUri=" + redirectUri + ", cookieName=" + cookieName + ", algorithm="
				+ algorithm + ", algorithmKey=" + algorithmKey + ", expires=" + expires + ", uid=" + uid + ", username="
				+ username + ", email=" + email + ", windowsAccount=" + windowsAccount + ", employeeNumber="
				+ employeeNumber + ", departmentId=" + departmentId + ", department=" + department + "]";
	}


}
