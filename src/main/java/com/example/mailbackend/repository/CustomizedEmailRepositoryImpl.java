package com.example.mailbackend.repository;

import com.example.mailbackend.dto.FindingEmailDTO;
import com.example.mailbackend.model.Email;
import com.example.mailbackend.model.User;
import com.example.mailbackend.security.SecurityUtils;
import com.example.mailbackend.security.UserPrincipal;
import com.example.mailbackend.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.Map;

public class CustomizedEmailRepositoryImpl implements CustomizedEmailRepository{
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private UserService userService;


    private Query buidHqlQueryFindEmail(FindingEmailDTO payload, Pageable pageable, boolean isCount){

        Map<String, Object> paramMap = new HashMap<>();
        String hqlQuery = "select e from Email e";
        if(isCount){
            hqlQuery = "select count(e) from Email e";
        }
        hqlQuery+=" where 1=1 ";

        // filter email by user.
        UserPrincipal userPrincipal = SecurityUtils.getUserPrincipal();
        long id = userPrincipal.getId();
        User user =userService.findById(id);
        if(!isCount){
            hqlQuery="SELECT e FROM Email e WHERE e.user = :user";
            paramMap.put("user",user);
        }else{
            hqlQuery = "select count(e) FROM Email e WHERE e.user= :user";
            paramMap.put("user",user);
        }


        if (StringUtils.isNotBlank(payload.getText())) {
            hqlQuery += "  and ( lower(e.fromEmail) like :text or  lower(e.subject) like :text )";
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
    public Page<Email> findEmail(FindingEmailDTO payload, Pageable pageable) {
        long total = (long) buidHqlQueryFindEmail(payload,pageable,true).getSingleResult();
        Query query= buidHqlQueryFindEmail(payload,pageable,false);
        return new PageImpl<>(query.getResultList(), pageable, total);
    }
}
