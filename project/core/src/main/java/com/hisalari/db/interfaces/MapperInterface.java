package com.hisalari.db.interfaces;

public interface MapperInterface {

   public <M> M getMapper(Class<M> clazz, String dataBaseId);
}
