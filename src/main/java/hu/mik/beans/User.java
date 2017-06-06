package hu.mik.beans;

import java.io.File;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PostLoad;
import javax.persistence.PostUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.context.annotation.Scope;
import org.springframework.web.context.annotation.ApplicationScope;
import org.springframework.web.context.annotation.SessionScope;

import com.vaadin.server.FileResource;
import com.vaadin.ui.Image;

import hu.mik.constants.UserConstants;

@Entity
@Table(name="t_user")
public class User {
	@Id
	@Column(name="id")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_GEN")
	@SequenceGenerator(name="SEQ_GEN", sequenceName="s_user", allocationSize=1, initialValue=1)
	private Integer id;
	@Column(name="username")
	private String username;
	@Column(name="passwd")
	private String password;
	@Column(name="enabled")
	private int enabled;
	@Column(name="image")
	private String imageName;
	@OneToMany(mappedBy="newsUser")
	private List<News> news;
	@Transient
	private Image image;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public int getEnabled() {
		return enabled;
	}
	public void setEnabled(int enabled) {
		this.enabled = enabled;
	}
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String image) {
		this.imageName = image;
	}
	public List<News> getNews() {
		return news;
	}
	public void setNews(List<News> news) {
		this.news = news;
	}
	public Image getImage() {
		return image;
	}
	public void setImage(Image image) {
		this.image = image;
	}
	@PostLoad
	public void setUserImage(){
		this.image=new Image(null, new FileResource(new File(UserConstants.PROFILE_PICTURE_LOCATION+this.getImageName())));
	}
	
	
	
	
	
	
	
	
	
}
