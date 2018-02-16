package hu.mik.dao;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import hu.mik.beans.LdapUser;
import hu.mik.beans.Role;
import hu.mik.beans.User;
import hu.mik.constants.UserConstants;
import hu.mik.services.LdapService;

@Repository
@Transactional(propagation=Propagation.REQUIRED)
public class UserDaoImplement implements UserDao{
	
	@PersistenceContext
	private EntityManager em;
	@Autowired
	LdapService ldapService;

	@Override
	public User save(User user, Role role) {
		if(findByUsername(user.getUsername())==null){
			em.persist(user);
			em.persist(role);
		}
		return findByUsername(user.getUsername());
	}
	
	@Override
	public User save(User user) {
		if(findByUsername(user.getUsername())==null){
			em.persist(user);
		}
		return findByUsername(user.getUsername());
	}
	@Override
	public boolean takenUsername(String username) {
		User user=findByUsername(username);

		if(user==null){
			return false;
		}
		else{
			return true;
		}
	}
	@Override
	public User findByUsername(String username) {
		User user=null;
		try {
			user=em.createQuery("SELECT u FROM User u where u.username= :username", User.class)
					.setParameter("username", username)
					.getSingleResult();
		} catch (Exception e) {
//			user=null;
			LdapUser ldapUser=ldapService.findUserByUsername(username);
			if(ldapUser!=null) {
				user=new User();
				user.setUsername(username);
				user.setImageName(UserConstants.DEFAULT_PROFILE_PICTURE_NAME);
				em.persist(user);			
			}
		}
		return user;
	}
	@Override
	public List<User> findAll() {
		List<User> list=new ArrayList<>();
		list=em.createQuery("SELECT u FROM User u", User.class).getResultList();
		return list;	
		
	}
	@Override
	public User findById(int id) {
		User user;
		try {
			user=em.createQuery("SELECT u FROM User u where u.id= :id", User.class)
					.setParameter("id", id)
					.getSingleResult();
		} catch (Exception e) {
			user=null;
		}
		return user;
	}
	@Override
	public List<User> findAllLike(String username) {
		List<User> list=new ArrayList<>();
		list=em.createQuery("SELECT u FROM User u where UPPER(u.username) LIKE CONCAT('%',:username,'%')", User.class)
				.setParameter("username", username.toUpperCase())
				.getResultList();
		if(list.isEmpty()){
			return null;
		}else{
			return list;
		}
	}
	@Override
	public void saveChanges(User user) {
		em.merge(user);
		
	}




	

}
