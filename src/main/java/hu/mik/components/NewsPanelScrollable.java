package hu.mik.components;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.VerticalLayout;

import hu.mik.beans.LdapGroup;
import hu.mik.beans.News;
import hu.mik.beans.User;
import hu.mik.enums.ScrollDirection;
import hu.mik.services.LdapService;
import hu.mik.services.NewsService;
import hu.mik.utils.UserUtils;

@SuppressWarnings("serial")
@SpringComponent
@Scope("prototype")
public class NewsPanelScrollable extends AbstractScrollablePanel {
	private LdapGroup ldapGroup;
	private User user;

	private VerticalLayout content;
	private NewsService newsService;
	private LdapService ldapService;
	private UserUtils userUtils;

	@Autowired
	private NewsPanelScrollable(UserUtils userUtils, LdapService ldapService, NewsService newsService) {
		super(ScrollDirection.DOWN);
		this.newsService = newsService;
		this.content = new VerticalLayout();
		this.content.setMargin(false);
		this.setContent(this.content);
		this.ldapService = ldapService;
		this.userUtils = userUtils;

	}

	public NewsPanelScrollable init() {
		this.loadNextPage();
		return this;
	}

	public NewsPanelScrollable init(LdapGroup ldapGroup) {
		this.ldapGroup = ldapGroup;
		this.loadNextPage();
		return this;
	}

	public NewsPanelScrollable init(User user) {
		this.user = user;
		this.loadNextPage();
		return this;
	}

	@Override
	protected void loadNextPage() {
		List<News> pagedResultList;
		if (this.user != null) {
			pagedResultList = this.newsService.getPagedNewsOfUser(this.offset, this.pageSize, this.user);
		} else if (this.ldapGroup != null) {
			pagedResultList = this.newsService.getPagedNewsByLdapGroup(this.offset, this.pageSize, this.ldapGroup);
		} else {
			pagedResultList = this.newsService.findAllPaged(this.offset, this.pageSize);
		}

		this.addConvertedComponents(pagedResultList);
	}

	private void addConvertedComponents(List<News> pagedResultList) {
		if (!pagedResultList.isEmpty()) {
			pagedResultList.forEach(object -> {
				String fullName = this.ldapService.findUserByUsername(object.getUser().getUsername()).getFullName();
				this.content.addComponent(
						new NewsComponent(object, this.userUtils.getLoggedInUser().getDbUser().equals(object.getUser()),
								fullName, this.newsService));
			});
			this.offset += this.pageSize;
		}
	}

	public void changeLdapGroup(LdapGroup ldapGroup) {
		this.content.removeAllComponents();
		this.offset = 0;
		this.ldapGroup = ldapGroup;
		this.loadNextPage();
		this.scrollToTop();
	}

	public void addNews(News news) {
		this.content.addComponent(new NewsComponent(news, true,
				this.userUtils.getLoggedInUser().getLdapUser().getFullName(), this.newsService), 0);
	}

}
