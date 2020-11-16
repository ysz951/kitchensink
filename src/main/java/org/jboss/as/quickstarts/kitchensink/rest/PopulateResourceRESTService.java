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
package org.jboss.as.quickstarts.kitchensink_web.rest;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.DELETE;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.as.quickstarts.kitchensink_web.data.TeamRepository;
import org.jboss.as.quickstarts.kitchensink_web.data.MemberRepository;
import org.jboss.as.quickstarts.kitchensink_web.data.ContestRepository;
import org.jboss.as.quickstarts.kitchensink_web.model.Team;
import org.jboss.as.quickstarts.kitchensink_web.model.Member;
import org.jboss.as.quickstarts.kitchensink_web.model.Contest;
import org.jboss.as.quickstarts.kitchensink_web.service.TeamRegistration;
import org.jboss.as.quickstarts.kitchensink_web.service.MemberRegistration;
import org.jboss.as.quickstarts.kitchensink_web.service.TeamUpdate;
import org.jboss.as.quickstarts.kitchensink_web.service.MemberUpdate;
import org.jboss.as.quickstarts.kitchensink_web.service.ContestUpdate;
import org.jboss.as.quickstarts.kitchensink_web.service.ContestRegistration;
/**
 * JAX-RS Example
 * <p/>
 * This class produces a RESTful service to read/write the contents of the members table.
 */
@Path("/populate")
@RequestScoped
public class PopulateResourceRESTService {

    @Inject
    private Logger log;

    @Inject
    private Validator validator;

    @Inject
    private TeamRepository teamRepository;
    
    @Inject
    private MemberRepository memberRepository;
    
    @Inject
    private ContestRepository contestRepository;
    
    @Inject
    TeamRegistration teamRegistration;
    
    @Inject
    MemberRegistration memberRegistration;
    
    @Inject
    ContestRegistration contestRegistration;
    
    @Inject
    MemberUpdate memberUpdate;
    
    @Inject
    TeamUpdate teamUpdate;
    
    @Inject
    ContestUpdate contestUpdate;
//    
//    @Inject
//    MemberDelete delete;
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response populate() {
    	Date date1 = new Date(2006, 11, 12);
        Date date2 = new Date(2015, 3, 21);
        Date date3 = new Date(2016, 9, 24);
        Response.ResponseBuilder builder = null;
        try {
            Team t1 = createTeam("teamOne");
            Member m1 = createMember("memberOne", "m1@mm.com");
            Member m2 = createMember("memberTwo", "m2@mm.com");
            Member m3 = createMember("memberThree", "m3@mm.com");
        	Member c1 = createMember("coachOne", "coach1@mm.com");
        	Contest contest = createContest(2, date1, "contest", true, date2, date3);
            Contest subcontest = createContest(3, date2, "subcontest", true, date2, date3);
            m1.setTeam(t1);
            memberUpdate.update(m1); 
            m2.setTeam(t1);
            memberUpdate.update(m2);
            m3.setTeam(t1);
            memberUpdate.update(m3);
            t1.setCoach(c1);
            teamUpdate.update(t1);
            
            subcontest.setParentContest(contest);
            contestUpdate.update(subcontest);
            
            builder = Response.ok();
        } catch (Exception e) {
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("error", e.getMessage());
            builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
        }
        return builder.build();
    }
    
    private Member createMember(String name, String email) throws Exception {
    	Member m = new Member();
    	m.setName(name);
    	m.setEmail(email);
    	memberRegistration.register(m);
    	return m;
    }
    
    private Team createTeam(String name) throws Exception {
    	Team t = new Team();
    	t.setName(name);
    	teamRegistration.register(t);
    	return t;
    }
    
    private Contest createContest(int capacity, Date date, String name, boolean registration_allowed, Date registration_from, Date registration_to) throws Exception {
        Contest contest = new Contest();
        contest.setName(name);
        contest.setCapacity(capacity);
        contest.setRegistration_allowed(registration_allowed);
        contest.setRegistration_from(registration_from);
        contest.setRegistration_to(registration_to);
        contest.setDate(date);
        contestRegistration.register(contest);
        return contest;
    }
}
