package com.company.enroller.persistence;

import java.util.Collection;

import org.hibernate.Query;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;

@Component("meetingService")
public class MeetingService {

	DatabaseConnector connector;

	public MeetingService() {
		connector = DatabaseConnector.getInstance();
	}

	public Collection<Meeting> getAll() {
		String hql = "FROM Meeting";
		Query query = connector.getSession().createQuery(hql);
		return query.list();
	}

	public Meeting findById(long meetingId) {
		String hql = "FROM Meeting as mt where mt.id =" + meetingId;
		Query query = connector.getSession().createQuery(hql);
		return (Meeting) query.uniqueResult();
	}

	public void add(Meeting meeting) {
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().save(meeting);
		transaction.commit();
		
	}

	public void updateMeeting(Meeting meeting) {
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().merge(meeting);
		transaction.commit();
		
	}

}
