package com.devtestejavagermatech.dao;


import java.util.List;

import com.devtestejavagermatech.util.exception.ErroSistema;


public interface CrudDAO<E> {

    public void save(E entidade) throws ErroSistema;

    public List<E> findAll() throws ErroSistema;

    public void update(E entidade) throws ErroSistema;

    public void delete(E entidade) throws ErroSistema;

}