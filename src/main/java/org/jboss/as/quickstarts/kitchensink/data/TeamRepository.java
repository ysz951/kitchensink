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
package org.jboss.as.quickstarts.kitchensink_web.data;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

import org.jboss.as.quickstarts.kitchensink_web.model.Team;

@ApplicationScoped
public class TeamRepository {

    @Inject
    private EntityManager em;
    
    
    public Team findById(Long id) {
        return em.find(Team.class, id);
    }
    
    public List<Team> whereTeam() {
    	CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Team> criteria = cb.createQuery(Team.class);
        Root<Team> team = criteria.from(Team.class);
        criteria.select(team).where(cb.like(team.get("name"), "te"));
        return em.createQuery(criteria).getResultList();
    }
    
    public List<Team> findAllOrderedById() {
    	CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Team> criteria = cb.createQuery(Team.class);
        Root<Team> team = criteria.from(Team.class);
        criteria.select(team).orderBy(cb.asc(team.get("id")));
        return em.createQuery(criteria).getResultList();
    }
}
