<<<<<<< HEAD
//package com.cxl.cloud.dao;
//
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//
//import javax.persistence.TypedQuery;
//import java.util.List;
//import java.util.Map;
//
//public interface DynamicQuery {
//    void save(Object entry);
//
//    void update(Object entry);
//
//    <T> void delete(Class<T> entryClass, Object entryId);
//
//    <T> void delete(Class<T> entryClass, Object[] entryIds);
//
//    /**
//     * 执行jpaSql查询一行
//     *
//     * @param resultClass 查询结果类型
//     * @param jpaSql
//     * @param paramsMap
//     * @param <T>
//     * @return
//     */
//    <T> T querySingleResult(Class<T> resultClass, String jpaSql, @SuppressWarnings("rawtypes") Map paramsMap);
//
//    /**
//     * 执行jpaSql查询List集合
//     *
//     * @param resultClass
//     * @param jpaSql
//     * @param paramsMap
//     * @param <T>
//     * @return
//     */
//    <T> List<T> query(Class<T> resultClass, String jpaSql, @SuppressWarnings("rawtypes") Map paramsMap);
//
//    /**
//     * 执行jpaSql查询数量
//     *
//     * @param jpaSql
//     * @param params
//     * @return 统计条数
//     */
//    Long queryCount(String jpaSql, Object... params);
//
//    /**
//     * 执行jpa的update跟delete
//     *
//     * @param jpaSql
//     * @param params
//     * @return
//     */
//    int executeUpdate(String jpaSql, Object... params);
//
//    /**
//     * 执行nativeSql 查询一行
//     *
//     * @param resultClass
//     * @param jpaSql
//     * @param params
//     * @param <T>
//     * @return
//     */
//    <T> T nativeQuerySingleResult(Class<T> resultClass, String jpaSql, Object... params);
//
//    /**
//     * 执行nativeSql 查询一行（返回一个Object[]数组）
//     *
//     * @param nativeSql
//     * @param params
//     * @param <T>
//     * @return
//     */
//    <T> T nativeQuerySingleResult(String nativeSql, Object... params);
//
//    /**
//     * 执行nativeSql查询List<Object[]数组>
//     *
//     * @param nativeSql
//     * @param params
//     * @param <T>
//     * @return
//     */
//    <T> List<T> query(String nativeSql, Object... params);
//
//    /**
//     * 执行nativeSql查询List<Object[]>
//     *
//     * @param nativeSql
//     * @param params    占位符参数(例如?1)绑定的参数值
//     * @return
//     */
//    <T> List<T> query(Class<T> resultClass, String nativeSql, Object... params);
//
//    /**
//     * 执行分页查询
//     *
//     * @param resultClass
//     * @param pageable
//     * @param nativeSql
//     * @param params
//     * @param <T>
//     * @return
//     */
//    <T> List<T> nativeQueryPagingList(Class<T> resultClass, Pageable pageable, String nativeSql, Object... params);
//
//
//    /**
//     * 执行nativeSql分页查询
//     * @param resultClass 查询结果类型
//     * @param pageable 分页数据
//     * @param nativeSql
//     * @param params 占位符参数(例如?1)绑定的参数值
//     * @return 分页对象
//     */
//    <T> Page<T> nativeQuery(Class<T> resultClass, Pageable pageable, String nativeSql, Object... params);
//    /**
//     * 执行统计查询
//     *
//     * @param nativeSql
//     * @param params
//     * @return
//     */
//    Long nativeQueryCount(String nativeSql, Object... params);
//
//    /**
//     * 执行nativeSql的update,delete操作
//     *
//     * @param nativeSql
//     * @param params
//     * @return
//     */
//    int nativeExecuteUpdate(String nativeSql, Object... params);
//
//    /**
//     * 执行jpql查询
//     *
//     * @param resultClass
//     * @param jpaSql
//     * @param paramsMAp
//     * @param <T>
//     * @return
//     */
//    <T> List<T> directQuery(Class<T> resultClass, String jpaSql, Map<String, Object> paramsMAp);
//
//    /**
//     * 执行jpql查询
//     *
//     * @param resultClass
//     * @param jpaSql
//     * @param paramsMap
//     * @param <T>
//     * @return
//     */
//    <T> T directQuerySingleResult(Class<T> resultClass, String jpaSql, Map<String, Object> paramsMap);
//
//    /**
//     * 执行分页查询
//     *
//     * @param resultClass
//     * @param pageable
//     * @param jpaSql
//     * @param paramsMap
//     * @param <T>
//     * @return
//     */
//    <T> List<T> directQueryPagingList(Class<T> resultClass, Pageable pageable, String jpaSql, Map<String, Object> paramsMap);
//
//    <T> TypedQuery<T> createTypeQueryByJpaSqlString(String jpaSql, Class<T> resultClass);
//
//    /**
//     * 查询对象列表，返回<组合对象>
//     *
//     * @param resultClass
//     * @param nativeSql
//     * @param params
//     * @return T
//     * @Date 2019年1月23日 更新日志
//     * 2019年1月23日 张志朋  首次创建
//     */
//    <T> T nativeQueryModel(Class<T> resultClass, String nativeSql, Object... params);
//}
=======
package com.cxl.cloud.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Map;

public interface DynamicQuery {
    void save(Object entry);

    void update(Object entry);

    <T> void delete(Class<T> entryClass, Object entryId);

    <T> void delete(Class<T> entryClass, Object[] entryIds);

    /**
     * 执行jpaSql查询一行
     *
     * @param resultClass 查询结果类型
     * @param jpaSql
     * @param paramsMap
     * @param <T>
     * @return
     */
    <T> T querySingleResult(Class<T> resultClass, String jpaSql, @SuppressWarnings("rawtypes") Map paramsMap);

    /**
     * 执行jpaSql查询List集合
     *
     * @param resultClass
     * @param jpaSql
     * @param paramsMap
     * @param <T>
     * @return
     */
    <T> List<T> query(Class<T> resultClass, String jpaSql, @SuppressWarnings("rawtypes") Map paramsMap);

    /**
     * 执行jpaSql查询数量
     *
     * @param jpaSql
     * @param params
     * @return 统计条数
     */
    Long queryCount(String jpaSql, Object... params);

    /**
     * 执行jpa的update跟delete
     *
     * @param jpaSql
     * @param params
     * @return
     */
    int executeUpdate(String jpaSql, Object... params);

    /**
     * 执行nativeSql 查询一行
     *
     * @param resultClass
     * @param jpaSql
     * @param params
     * @param <T>
     * @return
     */
    <T> T nativeQuerySingleResult(Class<T> resultClass, String jpaSql, Object... params);

    /**
     * 执行nativeSql 查询一行（返回一个Object[]数组）
     *
     * @param nativeSql
     * @param params
     * @param <T>
     * @return
     */
    <T> T nativeQuerySingleResult(String nativeSql, Object... params);

    /**
     * 执行nativeSql查询List<Object[]数组>
     *
     * @param nativeSql
     * @param params
     * @param <T>
     * @return
     */
    <T> List<T> query(String nativeSql, Object... params);

    /**
     * 执行nativeSql查询List<Object[]>
     *
     * @param nativeSql
     * @param params    占位符参数(例如?1)绑定的参数值
     * @return
     */
    <T> List<T> query(Class<T> resultClass, String nativeSql, Object... params);

    /**
     * 执行分页查询
     *
     * @param resultClass
     * @param pageable
     * @param nativeSql
     * @param params
     * @param <T>
     * @return
     */
    <T> List<T> nativeQueryPagingList(Class<T> resultClass, Pageable pageable, String nativeSql, Object... params);


    /**
     * 执行nativeSql分页查询
     * @param resultClass 查询结果类型
     * @param pageable 分页数据
     * @param nativeSql
     * @param params 占位符参数(例如?1)绑定的参数值
     * @return 分页对象
     */
    <T> Page<T> nativeQuery(Class<T> resultClass, Pageable pageable, String nativeSql, Object... params);
    /**
     * 执行统计查询
     *
     * @param nativeSql
     * @param params
     * @return
     */
    Long nativeQueryCount(String nativeSql, Object... params);

    /**
     * 执行nativeSql的update,delete操作
     *
     * @param nativeSql
     * @param params
     * @return
     */
    int nativeExecuteUpdate(String nativeSql, Object... params);

    /**
     * 执行jpql查询
     *
     * @param resultClass
     * @param jpaSql
     * @param paramsMAp
     * @param <T>
     * @return
     */
    <T> List<T> directQuery(Class<T> resultClass, String jpaSql, Map<String, Object> paramsMAp);

    /**
     * 执行jpql查询
     *
     * @param resultClass
     * @param jpaSql
     * @param paramsMap
     * @param <T>
     * @return
     */
    <T> T directQuerySingleResult(Class<T> resultClass, String jpaSql, Map<String, Object> paramsMap);

    /**
     * 执行分页查询
     *
     * @param resultClass
     * @param pageable
     * @param jpaSql
     * @param paramsMap
     * @param <T>
     * @return
     */
    <T> List<T> directQueryPagingList(Class<T> resultClass, Pageable pageable, String jpaSql, Map<String, Object> paramsMap);

    <T> TypedQuery<T> createTypeQueryByJpaSqlString(String jpaSql, Class<T> resultClass);

    /**
     * 查询对象列表，返回<组合对象>
     *
     * @param resultClass
     * @param nativeSql
     * @param params
     * @return T
     * @Date 2019年1月23日 更新日志
     * 2019年1月23日 张志朋  首次创建
     */
    <T> T nativeQueryModel(Class<T> resultClass, String nativeSql, Object... params);
}
>>>>>>> 9e674c949047e857516405bf511d169fd491bbf2
