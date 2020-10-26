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
import org.jboss.as.quickstarts.kitchensink.service.TeamUpdate;
//import org.jboss.as.quickstarts.kitchensink.service.MemberUpdate;
//import org.jboss.as.quickstarts.kitchensink.service.MemberDelete;
/**
 * JAX-RS Example
 * <p/>
 * This class produces a RESTful service to read/write the contents of the members table.
 */
@Path("/teams")
@RequestScoped
public class TeamResourceRESTService {

    @Inject
    private Logger log;

    @Inject
    private Validator validator;

    @Inject
    private TeamRepository repository;
    
    @Inject
    private MemberRepository memberRepository;
    
    @Inject
    TeamRegistration registration;
    
    @Inject
    TeamUpdate update;
//    
//    @Inject
//    MemberDelete delete;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Team> listAllMembers() {
        return repository.findAllOrderedById();
    }

    @GET
    @Path("/{id:[0-9][0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    public Team lookupMemberById(@PathParam("id") long id) {
        Team team = repository.findById(id);
        System.out.println((int) id);
        if (team == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return team;
    }
    
    @GET
    @Path("/whe")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Team> whereTeam() {
        List<Team> team = repository.whereTeam();
        return team;
    }
    
    /**
     * Creates a new member from the values provided. Performs validation, and will return a JAX-RS response with either 200 ok,
     * or with a map of fields, and related errors.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createMember(Team team) {
//    	if (team.getCoach() != null) {
//    		Member m = memberRepository.findById(team.getCoach().getId());
//        	team.setCoach(m);
//    	}
        Response.ResponseBuilder builder = null;
        System.out.println("team name is " + team.getName());
        try {
            // Validates member using bean validation
//            validateMember(member);

            registration.register(team);
//            Member t = lookupMemberById(Long.valueOf(0));
            // Create an "ok" response
            builder = Response.ok();
        } catch (Exception e) {
            // Handle generic exceptions
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("error", e.getMessage());
            builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
        }

        return builder.build();
    }
    
    
    @PUT
    @Path("/{id:[0-9][0-9]*}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateTeam(@PathParam("id") long id, Team t) {

        Response.ResponseBuilder builder = null;
//        Member member = repository.findById(id);
        
        try {
        	Team upd = repository.findById(id);
        	validate(upd);
            upd.setName(t.getName());
            upd.setEditTeam(t.isEditTeam());
            update.update(upd);
            builder = Response.ok();
        } catch (Exception e) {
            // Handle generic exceptions
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("error", e.getMessage());
            builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
        }

        return builder.build();
    }
    
    @PUT
    @Path("/coach/{id:[0-9][0-9]*}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateTeam(@PathParam("id") long id, Member m) {

        Response.ResponseBuilder builder = null;
//        Member member = repository.findById(id);
        Team upd = repository.findById(id);
        Member coach = memberRepository.findById(m.getId());
        upd.setCoach(coach);
        try {
            update.update(upd);
            builder = Response.ok();
        } 
        catch (ValidationException e) {
          // Handle the unique constrain violation
        	Map<String, String> responseObj = new HashMap<>();
        	responseObj.put("team", "Team can't be editted");
        	builder = Response.status(Response.Status.CONFLICT).entity(responseObj);
        }
        catch (Exception e) {
            // Handle generic exceptions
        	Map<String, String> responseObj = new HashMap<>();
            responseObj.put("error", e.getMessage());
            builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
        }

        return builder.build();
    }
//    
//    @DELETE
//    @Path("/{id:[0-9][0-9]*}")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response deleteMember(@PathParam("id") long id) {
//    	System.out.println("delete");
//    	System.out.println((int) id);
//        Response.ResponseBuilder builder = null;
////        Member member = repository.findById(id);
//        Member upd = repository.findById(id);
//        
////        if (member == null) {
////            throw new WebApplicationException(Response.Status.NOT_FOUND);
////        }
//        try {
//            // Validates member using bean validation
////            validateMember(upd);
//
//            delete.delete(upd);
////            Member t = lookupMemberById(Long.valueOf(0));
//            // Create an "ok" response
//            builder = Response.ok();
//        } catch (ConstraintViolationException ce) {
//            // Handle bean validation issues
//            builder = createViolationResponse(ce.getConstraintViolations());
//        } catch (ValidationException e) {
//            // Handle the unique constrain violation
//            Map<String, String> responseObj = new HashMap<>();
//            responseObj.put("email", "Email taken");
//            builder = Response.status(Response.Status.CONFLICT).entity(responseObj);
//        } catch (Exception e) {
//            // Handle generic exceptions
//            Map<String, String> responseObj = new HashMap<>();
//            responseObj.put("error", e.getMessage());
//            builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
//        }
//
//        return builder.build();
//    }
    /**
     * <p>
     * Validates the given Member variable and throws validation exceptions based on the type of error. If the error is standard
     * bean validation errors then it will throw a ConstraintValidationException with the set of the constraints violated.
     * </p>
     * <p>
     * If the error is caused because an existing member with the same email is registered it throws a regular validation
     * exception so that it can be interpreted separately.
     * </p>
     *
     * @param member Member to be validated
     * @throws ConstraintViolationException If Bean Validation errors exist
     * @throws ValidationException If member with the same email already exists
     */
//    private void validateMember(Member member) throws ConstraintViolationException, ValidationException {
//        // Create a bean validator and check for issues.
//        Set<ConstraintViolation<Member>> violations = validator.validate(member);
//
//        if (!violations.isEmpty()) {
//            throw new ConstraintViolationException(new HashSet<>(violations));
//        }
//
//        // Check the uniqueness of the email address
//        if (emailAlreadyExists(member.getEmail())) {
//            throw new ValidationException("Unique Email Violation");
//        }
//    }

    /**
     * Creates a JAX-RS "Bad Request" response including a map of all violation fields, and their message. This can then be used
     * by clients to show violations.
     *
     * @param violations A set of violations that needs to be reported
     * @return JAX-RS response containing all violations
     */
    private Response.ResponseBuilder createViolationResponse(Set<ConstraintViolation<?>> violations) {
        log.fine("Validation completed. violations found: " + violations.size());

        Map<String, String> responseObj = new HashMap<>();

        for (ConstraintViolation<?> violation : violations) {
            responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
        }

        return Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
    }

    /**
     * Checks if a member with the same email address is already registered. This is the only way to easily capture the
     * "@UniqueConstraint(columnNames = "email")" constraint from the Member class.
     *
     * @param email The email to check
     * @return True if the email already exists, and false otherwise
     */
    public void validate(Team t) throws ValidationException{
    	if (!t.isEditTeam()) throw new ValidationException("This team can't be editted");
    }
}
