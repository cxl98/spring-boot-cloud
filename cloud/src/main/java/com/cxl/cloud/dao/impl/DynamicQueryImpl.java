package com.cxl.cloud.dao.impl;

import com.cxl.cloud.dao.DynamicQuery;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.hql.internal.ast.QueryTranslatorImpl;
import org.hibernate.hql.spi.QueryTranslator;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Repository
public class DynamicQueryImpl implements DynamicQuery {

    @Autowired
    private EntityManager manager;

    private Session getHibernateSession() {
        return manager.unwrap(Session.class);
    }

    /**
     * 获取Hibernate的SessionFactory对象
     *
     * @return
     */
    private SessionFactory getHibernateSessionFactory() {
        return getHibernateSession().getSessionFactory();
    }


    @Override
    public void save(Object entry) {
        manager.persist(entry);
    }

    @Override
    public void update(Object entry) {
        manager.merge(entry);
    }

    @Override
    public <T> void delete(Class<T> entryClass, Object entryId) {
        delete(entryClass, new Object[]{entryId});
    }

    @Override
    public <T> void delete(Class<T> entryClass, Object[] entryIds) {
        for (Object id : entryIds) {
            manager.remove(manager.getReference(entryClass, id));
        }
    }

    @Override
    public <T> T querySingleResult(Class<T> resultClass, String jpaSql, Map paramsMap) {
        return createTypedQuery(resultClass, jpaSql, paramsMap).getSingleResult();
    }

    @Override
    public <T> List<T> query(Class<T> resultClass, String jpaSql, Map paramsMap) {
        return createTypedQuery(resultClass, jpaSql, paramsMap).getResultList();
    }

    @Override
    public Long queryCount(String jpaSql, Object... params) {
        jpaSql = StringUtils.substringBefore(jpaSql, "order by");// 去掉order by,提升执行效率

        if (jpaSql.contains("distinct") || jpaSql.contains("group by")) {
            String countSql = generateCountSql(jpaSql);
            Object count = createNativeQuery(countSql, params).getSingleResult();
            return ((Number) count).longValue();
        } else {
            String countSql = generateCountJpaSql(jpaSql);
            return (Long) createQuery(countSql, params).getSingleResult();
        }

    }


    @Override
    public int executeUpdate(String jpaSql, Object... params) {
        return createQuery(jpaSql, params).executeUpdate();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T nativeQuerySingleResult(Class<T> resultClass, String jpaSql, Object... params) {
        Query query = createNativeQuery(resultClass, jpaSql, params);
        List<T> list = query.getResultList();
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T nativeQuerySingleResult(String nativeSql, Object... params) {
        Query query = createNativeQuery(null, nativeSql, params);
        List<T> list = query.getResultList();
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> query(String nativeSql, Object... params) {
        Query query = createNativeQuery(null, nativeSql, params);
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> query(Class<T> resultClass, String nativeSql, Object... params) {
        Query query = createNativeQuery(resultClass, nativeSql, params);
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> nativeQueryPagingList(Class<T> resultClass, Pageable pageable, String nativeSql, Object... params) {
        Integer pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        int startPosition = pageNumber * pageSize;
        return createNativeQuery(resultClass, nativeSql, params).setFirstResult(startPosition).setMaxResults(pageSize).getResultList();
    }

    @Override
    public <T> Page<T> nativeQuery(Class<T> resultClass, Pageable pageable, String nativeSql, Object... params) {
        List<T> rows = nativeQueryPagingList(resultClass, pageable, nativeSql, params);
        Long total = nativeQueryCount(nativeSql, params);
        return new PageImpl<>(rows, pageable, total);
    }

    @Override
    public Long nativeQueryCount(String nativeSql, Object... params) {
        nativeSql = StringUtils.substringBefore(nativeSql, "order by"); //去掉order by 提升效率

        Object count = createNativeQuery(nativeSql, params);
        return ((Number) count).longValue();
    }

    @Override
    public int nativeExecuteUpdate(String nativeSql, Object... params) {

        return createNativeQuery(nativeSql, params).executeUpdate();
    }

    @Override
    public <T> List<T> directQuery(Class<T> resultClass, String jpaSql, Map<String, Object> paramsMAp) {
        return getDirectQuery(resultClass, jpaSql, paramsMAp).getResultList();
    }

    @Override
    public <T> T directQuerySingleResult(Class<T> resultClass, String jpaSql, Map<String, Object> paramsMap) {
        try {
            return getDirectQuery(resultClass, jpaSql, paramsMap).getSingleResult();
        } catch (Exception e) {

            return null;
        }
    }

    @Override
    public <T> List<T> directQueryPagingList(Class<T> resultClass, Pageable pageable, String jpaSql, Map<String, Object> paramsMap) {
        Integer pageNumber=pageable.getPageNumber();
        int pageSize=pageable.getPageSize();
        int startPosition=pageNumber*pageSize;
        return getDirectQuery(resultClass,jpaSql,paramsMap).setFirstResult(startPosition).setMaxResults(pageSize).getResultList();
    }

    @Override
    public <T> TypedQuery<T> createTypeQueryByJpaSqlString(String jpaSql, Class<T> resultClass) {
        return this.manager.createQuery(jpaSql,resultClass);
    }
    @SuppressWarnings("unchecked")
    @Override
    public <T> T nativeQueryModel(Class<T> resultClass, String nativeSql, Object... params) {
        Query query=createNativeQuery(nativeSql,params);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(resultClass));
        List<T> list=query.getResultList();
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @SuppressWarnings("unchecked")
    private <T> TypedQuery<T> createTypedQuery(Class<T> resultClass, String jpaSql, @SuppressWarnings("rawtypes") Map paramsMap) {
        TypedQuery<T> query = manager.createNamedQuery(jpaSql, resultClass);
        for (Object key : paramsMap.keySet()) {
            query.setParameter((String) key, paramsMap.get(key));
        }
        return query;
    }

    private Query createNativeQuery(String countSql, Object... params) {
        Query query = manager.createNativeQuery(countSql);
        for (int i = 0, length = params.length; i < length; i++) {
            query.setParameter(i + 1, params[i]);// 与Hiberante不同,jpa query从位置1开始
        }
        return query;
    }

    private String generateCountSql(String jpaSql) {
        return "select count(*) c from (" + jpaSqlToSql(jpaSql) + ") _count";
    }

    private String jpaSqlToSql(String jpaSql) {
        QueryTranslator queryTranslator = new QueryTranslatorImpl(jpaSql, jpaSql, Collections.EMPTY_MAP, (SessionFactoryImplementor) getHibernateSessionFactory());
        queryTranslator.compile(Collections.EMPTY_MAP, false);
        return queryTranslator.getSQLString();
    }

    private String generateCountJpaSql(String jpaSql) {
        return "select count(*) from " + StringUtils.substringAfter(jpaSql, "from");
    }

    private Query createQuery(String countSql, Object... params) {
        Query query = manager.createQuery(countSql);
        for (int i = 0, length = params.length; i < length; i++) {
            query.setParameter(i + 1, params[i]);
        }
        return query;
    }

    private <T> Query createNativeQuery(Class<T> resultClass, String jpaSql, Object[] params) {
        Query query;
        if (resultClass == null) {
            query = manager.createNativeQuery(jpaSql);
        } else {
            query = manager.createNativeQuery(jpaSql, resultClass);
        }

        for (int i = 0, length = params.length; i < length; i++) {
            query.setParameter(i + 1, params[i]);
        }
        return query;
    }

    private <T> TypedQuery<T> getDirectQuery(Class<T> resultClass, String jpaSql, Map<String, Object> paramsMAp) {
        TypedQuery<T> query = manager.createQuery(jpaSql, resultClass);

        for (String item : paramsMAp.keySet()) {
            query.setParameter(item, paramsMAp.get(item));
        }
        return query;
    }
}
