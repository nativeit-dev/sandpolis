/******************************************************************************
 *                                                                            *
 *                    Copyright 2018 Subterranean Security                    *
 *                                                                            *
 *  Licensed under the Apache License, Version 2.0 (the "License");           *
 *  you may not use this file except in compliance with the License.          *
 *  You may obtain a copy of the License at                                   *
 *                                                                            *
 *      http://www.apache.org/licenses/LICENSE-2.0                            *
 *                                                                            *
 *  Unless required by applicable law or agreed to in writing, software       *
 *  distributed under the License is distributed on an "AS IS" BASIS,         *
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *
 *  See the License for the specific language governing permissions and       *
 *  limitations under the License.                                            *
 *                                                                            *
 *****************************************************************************/
package com.sandpolis.core.storage.hibernate;

import java.util.Iterator;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.sandpolis.core.instance.storage.StoreProvider;

/**
 * A persistent {@link StoreProvider} that is backed by a Hibernate session.
 * 
 * @author cilki
 * @since 5.0.0
 */
public class HibernateStoreProvider<E> implements StoreProvider<E> {

	/**
	 * The persistent class.
	 */
	private final Class<E> cls;

	/**
	 * The backing storage for this {@code StoreProvider}.
	 */
	private final EntityManagerFactory emf;

	public HibernateStoreProvider(Class<E> cls, EntityManagerFactory emf) {
		if (cls == null)
			throw new IllegalArgumentException();
		if (emf == null)
			throw new IllegalArgumentException();

		this.cls = cls;
		this.emf = emf;

	}

	@Override
	public void add(E e) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		em.persist(e);
		em.getTransaction().commit();
		em.close();
	}

	@Override
	public E get(Object id) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		try {
			E e = em.find(cls, id);
			em.getTransaction().commit();
			return e;
		} finally {
			em.close();
		}
	}

	@Override
	public E get(String field, Object id) {
		EntityManager em = emf.createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<E> cq = cb.createQuery(cls);
		Root<E> root = cq.from(cls);
		TypedQuery<E> tq = em.createQuery(cq.select(root).where(cb.equal(root.get(field), id)));

		try {
			return tq.getSingleResult();
		} catch (NoResultException e) {
			return null;
		} finally {
			em.close();
		}
	}

	@Override
	public Iterator<E> iterator() {
		return stream().iterator();// TODO leak
	}

	@Override
	public long count() {
		EntityManager em = emf.createEntityManager();

		CriteriaBuilder qb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = qb.createQuery(Long.class);
		cq.select(qb.count(cq.from(cls)));
		try {
			return em.createQuery(cq).getSingleResult();
		} finally {
			em.close();
		}
	}

	@Override
	public Stream<E> stream() {
		EntityManager em = emf.createEntityManager();
		CriteriaQuery<E> cq = em.getCriteriaBuilder().createQuery(cls);
		return em.createQuery(cq.select(cq.from(cls))).getResultStream().onClose(() -> em.close());
	}

	@Override
	public void remove(E e) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		em.remove(e);
		em.getTransaction().commit();
		em.close();
	}

	@Override
	public void clear() {
		EntityManager em = emf.createEntityManager();
		try {
			CriteriaDelete<E> query = em.getCriteriaBuilder().createCriteriaDelete(cls);
			query.from(cls);
			em.createQuery(query).executeUpdate();
		} catch (Exception ignore) {
		} finally {
			em.close();
		}
	}

}