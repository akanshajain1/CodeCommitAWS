package com.mindtree.springexamplev2.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.mindtree.springexamplev2.dto.DTO;
import com.mindtree.springexamplev2.entity.EntityClass;
import com.mindtree.springexamplev2.exception.DAOException;
import com.mindtree.springexamplev2.util.HibernateUtil;

/**
 * @author M1036004 This is the DAO class for database operations;
 *
 */
public class DAOClass {

	/**
	 * @param dto
	 * @return
	 * @throws DAOException
	 */
	public boolean addData(DTO dto) throws DAOException {
		EntityClass entityClass = new EntityClass(dto.getName(), dto.getLastname(), dto.getEmail(),dto.getPassword());
		Transaction transaction = null;
		boolean result = false;
		try {
			System.out.println("in add dao class");
			Session session = (Session) HibernateUtil.getSessionFactoryObject().openSession();
			System.out.println("2 step:");
			transaction = session.beginTransaction();
			System.out.println("in 3 step");
			session.save(entityClass);
			System.out.println("in 4 step");
			transaction.commit();
			result = true;
			if(session!=null){
				session.close();
			}
		} catch (HibernateException exception) {
			throw new DAOException(exception.getMessage(),exception);
			//exception.printStackTrace();
		}
		return result;

	}

	public List<EntityClass> viewData() throws DAOException {
		// List<DTO> dtoList = new ArrayList<DTO>();
		List<EntityClass> entityClass = new ArrayList<EntityClass>();
		Transaction transaction = null;
		try {

			System.out.println("be");
			Session session = (Session) HibernateUtil.getSessionFactoryObject().openSession();
			transaction = session.beginTransaction();
			System.out.println("be1");
			Query query = session.createQuery("from EntityClass");
			entityClass = query.list();
			DTO dtotemp = null;
			System.out.println("be2");
			// System.out.println(query.list().size());
			System.out.println(
					entityClass.get(0).getName() + entityClass.get(0).getlastname() + entityClass.get(0).getEmail());

			/*
			 * while (query.hasNext()) { Object[] objects = (Object[])
			 * query.next(); System.out.println(objects.length); dtotemp = new
			 * DTO(Integer.parseInt(objects[0].toString()),
			 * objects[1].toString(), objects[2].toString(),
			 * Float.parseFloat(objects[3].toString())); dtoList.add(dtotemp);
			 * System.out.println(objects[0].toString() + " name" +
			 * objects[1].toString()); }
			 */

		} catch (HibernateException exception) {
			throw new DAOException(exception.getMessage(),exception);
			//exception.printStackTrace();
		}
		return entityClass;

	}

	public boolean deleteData(int id) throws DAOException {
		boolean result = false;
		Transaction transaction = null;
		try {
			System.out.println("here");
			Session session = (Session) HibernateUtil.getSessionFactoryObject().openSession();
			transaction = session.beginTransaction();
			EntityClass entityClass = new EntityClass();
			System.out.println("here1");
			entityClass.setId(id);
			System.out.println("here2");
			session.delete(entityClass);
			result = true;
			transaction.commit();
		} catch (HibernateException exception) {
			throw new DAOException("!! unable to deleted ", exception);
		}
		return result;

	}
}
