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
package org.jboss.as.quickstarts.kitchensink.rest;

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

import org.jboss.as.quickstarts.kitchensink.data.TeamRepository;
import org.jboss.as.quickstarts.kitchensink.data.MemberRepository;
import org.jboss.as.quickstarts.kitchensink.model.Team;
import org.jboss.as.quickstarts.kitchensink.model.Member;
import org.jboss.as.quickstarts.kitchensink.service.TeamRegistration;
import org.jboss.as.quickstarts.kitchensink.service.MemberRegistration;
import org.jboss.as.quickstarts.kitchensink.service.TeamUpdate;
import org.jboss.as.quickstarts.kitchensink.service.MemberUpdate;
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
    TeamRegistration teamRegistration;
    
    @Inject
    MemberRegistration memberRegistration;
    
    @Inject
    MemberUpdate memberUpdate;
    
    @Inject
    TeamUpdate teamUpdate;
//    
//    @Inject
//    MemberDelete delete;
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response populate() {
        Response.ResponseBuilder builder = null;
        try {
            Team t1 = createTeam("teamOne");
            Member m1 = createMember("memberOne", "mmmm@mm.com");
            Member m2 = createMember("membergege", "mggmm@mm.com");
            Member m3 = createMember("membetee", "tgggm@mm.com");
        	Member c1 = createMember("memberTwo", "mccm@mm.com");
            m1.setTeam(t1);
            memberUpdate.update(m1); 
            m2.setTeam(t1);
            memberUpdate.update(m2);
            t1.setCoach(c1);
            teamUpdate.update(t1);
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
}
