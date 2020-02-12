package com.cxl.cloud.dao;

public interface DynamicQuery {
    void save(Object entry);

    void update(Object entry);

    <T> void delete(Class<T> entryClasss,Object entryId);

    <T> void delete(Class<T> entryClass,Object[] entryIds);


}
