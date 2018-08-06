package hu.mik.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import hu.mik.beans.Message;

@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class MessageDaoImpl implements MessageDao {

	@PersistenceContext
	EntityManager em;

	//@formatter:off

	@Override
	public List<Message> findAllByUserIDs(int number, int id1, int id2) {
		List<Message> list=new ArrayList<>();
		list=this.em.createQuery("select m from Message m where"
				+ " (m.senderId= :id1 and m.receiverId= :id2)"
				+ " or (m.senderId= :id2 and m.receiverId= :id1)"
				+ " order by m.time desc",
				Message.class)
				.setParameter("id1", id1)
				.setParameter("id2", id2)
				.setFirstResult(0)
				.setMaxResults(number-1)
				.getResultList();
		return list;
	}

	@Override
	public void save(Message message) {
		this.em.persist(message);
	}

	@Override
	public List<Message> findLatestMessagesOfUser(int number, int userId) {
		List<Message> list=new ArrayList<>();
		list=this.em.createQuery("select m from Message m where"
				+ " (m.senderId= :id or m.receiverId= :id)"
				+ " order by m.time desc",
				Message.class)
				.setParameter("id", userId)
				.setFirstResult(0)
				.setMaxResults(number-1)
				.getResultList();
		return list;

	}

	@Override
	public Message findLastByUserIDs(int id1, int id2) {
		Message message;
		try{
			message=this.em.createQuery("select m from Message m where"
					+ " (m.senderId= :id1 and m.receiverId= :id2)"
					+ " or (m.senderId= :id2 and m.receiverId= :id1)"
					+ " order by m.time desc",Message.class)
					.setParameter("id1", id1)
					.setParameter("id2", id2)
					.setFirstResult(0)
					.setMaxResults(1)
					.getSingleResult();
		} catch (Exception e) {
			message = null;
		}
		return message;
	}

	@Override
	public int setAllPreviousSeen(Integer receiverId, Integer senderId) {
		return this.em.createQuery(
				"UPDATE Message m SET seen = 1"
				+ " WHERE m.receiverId = :receiverId"
				+ " AND m.senderId = :senderId"
				+ " AND seen = 0")
		.setParameter("receiverId", receiverId.intValue())
		.setParameter("senderId", senderId.intValue())
		.executeUpdate();

	}

	@Override
	public Long getNumberOfUnseenConversations(Integer userId) {
		return (Long) this.em.createQuery(
				"SELECT COUNT(DISTINCT m.senderId)"
				+ " FROM Message m"
				+ " WHERE m.receiverId = :userId"
				+ " AND m.seen = 0"
				+ " ORDER BY m.id DESC")
		.setParameter("userId", userId.intValue())
		.getResultList().get(0);
	}

}
