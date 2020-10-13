package com.company.enroller.persistence;

import com.company.enroller.model.Meeting;
import org.hibernate.Query;
import org.springframework.stereotype.Component;
import org.hibernate.Session;
import com.company.enroller.model.Participant;
import java.util.Collection;
import org.hibernate.Transaction;
import javax.persistence.JoinColumn;

@Component("meetingService")
public class MeetingService {

	Session session;
    DatabaseConnector connector;

    public MeetingService() {
        session = DatabaseConnector.getInstance().getSession();
    }

    public Collection<Meeting> getAll() {
        String hql = "FROM Meeting";
        Query query = session.createQuery(hql);
        return query.list();
    }
    
    public Meeting findById(long id) {
    	return (Meeting) session.get(Meeting.class, id);
    }
    
    public Meeting add(Meeting meeting) {
		Transaction transaction = this.session.beginTransaction();
		session.save(meeting);
		transaction.commit();
		return meeting;
		}
    
    public Meeting update(Meeting meeting) {
		Transaction transaction = this.session.beginTransaction();
		session.update(meeting);
		transaction.commit();
		return meeting;
	}
    
    public void delete(Meeting meeting) {
		Transaction transaction = this.session.beginTransaction();
		session.delete(meeting);
		transaction.commit();
	}
}
