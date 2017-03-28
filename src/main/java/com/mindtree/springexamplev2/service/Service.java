package com.mindtree.springexamplev2.service;

import java.util.ArrayList;
import java.util.List;

import com.mindtree.springexamplev2.dao.DAOClass;
import com.mindtree.springexamplev2.dao.DAOClassForDynamo;
import com.mindtree.springexamplev2.dto.DTO;
import com.mindtree.springexamplev2.entity.EntityClass;
import com.mindtree.springexamplev2.entity.EntityClassForDynamo;
import com.mindtree.springexamplev2.exception.DAOException;
import com.mindtree.springexamplev2.exception.ServiceException;
import com.mindtree.springexamplev2.util.Mail;

/**
 * @author M1036004 This is the service class for connecting the controller
 *         class to dao class for database operations
 *
 */
public class Service {
	DAOClass daoClass = new DAOClass();
	DAOClassForDynamo daoClassForDynamo = new DAOClassForDynamo();
	Mail mail = new Mail();

	public boolean addData(DTO dto, String etag) throws ServiceException {

		try {
			/*try {
				mail.sendMail(dto.getEmail());
			} catch (Exception e) {
				// TODO Auto-generated catch block
		e.printStackTrace();
			}*/
			return daoClassForDynamo.addItem(dto,etag);
		} catch (DAOException e) {
			throw new ServiceException(e.getMessage(), e);
		}
	}

	public List<EntityClassForDynamo > viewData() throws ServiceException {
		List<EntityClassForDynamo> entityClassList = null;
		try {
			entityClassList = daoClassForDynamo.viewData();
		} catch (DAOException e) {
			throw new ServiceException(e.getMessage(), e);
		}
		return entityClassList;
	}

	public boolean deleteData(String email , String name) throws ServiceException {
		try {
			return daoClassForDynamo.deleteItem(email, name);
		} catch (DAOException e) {
			throw new ServiceException(e.getMessage(), e);
		}

	}

}
