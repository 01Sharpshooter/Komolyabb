package hu.mik.views;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinService;
import com.vaadin.server.WrappedSession;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import hu.mik.beans.FriendRequest;
import hu.mik.beans.User;
import hu.mik.constants.SystemConstants;
import hu.mik.constants.ThemeConstants;
import hu.mik.services.FriendRequestService;
import hu.mik.services.UserService;
import hu.mik.utils.ProfileImageHelper;

@SuppressWarnings("serial")
@ViewScope
@SpringView(name = RequestsView.NAME)
public class RequestsView extends Panel implements View {
	public static final String NAME = "RequestsView";

	@Autowired
	FriendRequestService friendRequestService;
	@Autowired
	UserService userService;

	private User user;
	private HorizontalLayout row = new HorizontalLayout();
	private VerticalLayout rows = new VerticalLayout();
	private HorizontalLayout userDiv;
	private int divsPerRow = 5;

	@Override
	public void enter(ViewChangeEvent event) {
		this.fill();

	}

	private void fill() {
		this.setSizeFull();
		this.setContent(this.rows);
		this.rows.setSizeFull();
		this.rows.addComponent(this.row);
		this.setCaption("Your friend requests:");
		this.row.setHeight(this.getHeight() / this.divsPerRow, this.getHeightUnits());

		WrappedSession session = VaadinService.getCurrentRequest().getWrappedSession();
		String username = (String) session.getAttribute(SystemConstants.SESSION_ATTRIBUTE_LDAP_USER);
		this.user = this.userService.findUserByUsername(username);

		List<FriendRequest> requests = this.friendRequestService.findAllByRequestedId(this.user.getId());
//		List<LdapUser> requestsAsLdap
		int i = 0;
//		requests.stream().map(r -> this.userService.findUserById(r.getRequestedId())).;
		for (FriendRequest request : requests) {
			i++;
			User requestUser = this.userService.findUserById(request.getRequestorId());
			Image image = new Image(null,
					new FileResource(ProfileImageHelper.loadUserImage(requestUser.getImageName())));
			image.setHeight("100%");
			image.addStyleName(ThemeConstants.BORDERED_IMAGE);
			Button nameButton = new Button(requestUser.getUsername(), this::userNameListener);
			nameButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
			nameButton.setSizeFull();
			this.userDiv = new HorizontalLayout();
			this.userDiv.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
			this.userDiv.setHeight("100%");
			this.userDiv.setWidth(this.getWidth() / this.divsPerRow, this.getWidthUnits());
			this.userDiv.addComponent(image);
			this.userDiv.addComponent(nameButton);
			this.userDiv.addStyleName(ThemeConstants.BORDERED);
			this.row.addComponent(this.userDiv);
			if (i == this.divsPerRow) {
				this.row = new HorizontalLayout();
				this.row.setHeight(this.getHeight() / this.divsPerRow, this.getHeightUnits());
				this.rows.addComponent(this.row);
				i = 0;
			}

		}

	}

	private void userNameListener(Button.ClickEvent event) {
//		((MainUI)getUI()).changeToUser(userService.findUserByUsername(event.getButton().getCaption()));
	}

}
