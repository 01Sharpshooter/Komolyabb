package hu.mik.beans;

import java.util.List;

import javax.naming.Name;

import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;
import org.springframework.ldap.odm.annotations.Transient;

import hu.mik.constants.LdapConstants;
import lombok.Data;

@Data
@Entry(base = LdapConstants.OU_USERS_FULL, objectClasses = { "top", "inetOrgPerson", "person", "organizationalPerson" })
public class LdapUser {
	@Id
	private Name id;

	@Attribute(name = "uid")
	private String username;
	@Attribute(name = "cn")
	private String fullName;
	@Attribute(name = "mobile")
	private String mobile;
	@Attribute(name = "mail")
	private String mail;
	@Attribute(name = "roomNumber")
	private String roomNumber;
	@Attribute(name = "title")
	private String title;
	@Attribute(name = "departmentNumber")
	private String departmentNumber;
	@Transient
	private List<LdapGroup> ldapGroups;

}
