package com.example.mailbackend.repository;

import com.example.mailbackend.dto.FindingUserDTO;
import com.example.mailbackend.model.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.Map;

public class CustomizedUserRepositoryImpl implements CustomizedUserRepository {
    @PersistenceContext
    private EntityManager entityManager;


    private Query buidHqlQueryFindJob(FindingUserDTO payload, Pageable pageable, boolean isCount){

        Map<String, Object> paramMap = new HashMap<>();
        String hqlQuery = "select u from User u";
        if(isCount){
            hqlQuery = "select count(u) from User u";
        }
        hqlQuery+=" where 1=1 ";

        if (StringUtils.isNotBlank(payload.getText())) {
            hqlQuery += "  and ( lower(u.name) like :text or  lower(u.username) like :text )";
            paramMap.put("text", "%"+payload.getText().toLowerCase().trim()+"%");
        }

        if (!isCount && pageable != null && pageable.getSort() != null) {
            String[] properties = {""};
            Sort.Direction[] directions = {Sort.Direction.ASC};
            pageable.getSort().forEach(order -> {
                properties[0] = order.getProperty();
                directions[0] = order.getDirection();
            });
            if (StringUtils.isNotBlank(properties[0])) {
                hqlQuery += " Order by " + properties[0] + " " + directions[0];
            }
        }

        Query query = entityManager.createQuery(hqlQuery);
        for (String key : paramMap.keySet()) {
            query.setParameter(key, paramMap.get(key));
        }
        if (!isCount && pageable != null) {
            Integer pageFrom = pageable.getPageNumber() * pageable.getPageSize();
            Integer pageSize = pageable.getPageSize();
            query.setFirstResult(pageFrom);
            query.setMaxResults(pageSize);

        }
        return query;
    }

    @Override
    public Page<User> findUser(FindingUserDTO payload, Pageable pageable) {
        long total = (long) buidHqlQueryFindJob(payload,pageable,true).getSingleResult();
        Query query= buidHqlQueryFindJob(payload,pageable,false);
        return new PageImpl<>(query.getResultList(), pageable, total);
    }
}
