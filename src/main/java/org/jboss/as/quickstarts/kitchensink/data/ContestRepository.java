/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.as.quickstarts.kitchensink.data;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

import org.jboss.as.quickstarts.kitchensink.model.Contest;

@ApplicationScoped
public class ContestRepository {

    @Inject
    private EntityManager em;
    
    
    public Contest findById(Long id) {
        return em.find(Contest.class, id);
    }
    
    
    public List<Contest> findAllOrderedById() {
    	CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Contest> criteria = cb.createQuery(Contest.class);
        Root<Contest> contest = criteria.from(Contest.class);
        criteria.select(contest).orderBy(cb.asc(contest.get("id")));
        return em.createQuery(criteria).getResultList();
    }
}
