package hu.mik.services;

import java.util.List;

import hu.mik.beans.Friendship;
import hu.mik.beans.User;

public interface FriendshipService {
	public List<Friendship> findAllByUserId(int userId);

	public Friendship findOne(int userId, int friendId);

	public Friendship saveFriendship(Friendship friendship);

	public void saveFriendship(User userId, User friendId);

	public void deleteFriendship(int userId, int friendId);
}
