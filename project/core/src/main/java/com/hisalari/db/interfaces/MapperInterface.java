package com.hisalari.db.interfaces;

public interface MapperInterface<M> {

   public M getMapper(Class<M> clazz, String dataBaseId);
}
