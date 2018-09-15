package hu.mik.beans;

import java.util.List;

import javax.naming.Name;

import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import hu.mik.constants.LdapConstants;

@Entry(base = LdapConstants.OU_GROUPS_FULL, objectClasses = { "top", "groupOfNames" })
public class LdapGroup {
	@Id
	private Name id;

	@Attribute(name = "cn")
	private String groupName;
	@Attribute(name = "member")
	private List<Name> listOfMembers;
	@Attribute(name = "member")
	private Name member;

	public Name getId() {
		return this.id;
	}

	public void setId(Name id) {
		this.id = id;
	}

	public String getGroupName() {
		return this.groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public List<Name> getListOfMembers() {
		return this.listOfMembers;
	}

	public void setListOfMembers(List<Name> listOfMembers) {
		this.listOfMembers = listOfMembers;
	}

	@Override
	public String toString() {
		return this.groupName;
	}

}
