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
package org.jboss.as.quickstarts.kitchensink_web.controller;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.as.quickstarts.kitchensink_web.data.MemberRepository;
import org.jboss.as.quickstarts.kitchensink_web.data.TeamRepository;
import org.jboss.as.quickstarts.kitchensink_web.model.Member;
import org.jboss.as.quickstarts.kitchensink_web.model.Team;
import org.jboss.as.quickstarts.kitchensink_web.service.MemberDelete;
import org.jboss.as.quickstarts.kitchensink_web.service.MemberRegistration;
import org.jboss.as.quickstarts.kitchensink_web.service.MemberUpdate;
// The @Model stereotype is a convenience mechanism to make this a request-scoped bean that has an
// EL name
// Read more about the @Model stereotype in this FAQ:
// http://www.cdi-spec.org/faq/#accordion6
@Model
public class MemberController {

    @Inject
    private FacesContext facesContext;

    @Inject
    private MemberRegistration memberRegistration;

    @Inject
    private MemberDelete memberDelete;

    @Inject
    private MemberUpdate memberUpdate;

    @Inject
    private MemberRepository repository;

    @Produces
    @Named
    private Member newMember;

    @Inject
    private TeamRepository teamRepository;

    @Inject
    private Event<Team> teamEventSrc;

    @PostConstruct
    public void initNewMember() {
        newMember = new Member();
    }

    public void register() throws Exception {
        try {
            memberRegistration.register(newMember);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO, "Registered!", "Registration successful");
            facesContext.addMessage(null, m);
            initNewMember();
        } catch (Exception e) {
            String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Registration unsuccessful");
            facesContext.addMessage(null, m);
        }
    }

    public void update(Member m, long teamId) throws Exception {
    	try {
    		Member upd = repository.findById(m.getId());
    		Team team = teamRepository.findById(teamId);
	        upd.setName(m.getName());
	        upd.setEmail(m.getEmail());
	        upd.setTeam(team);
    		memberUpdate.update(upd);
    		teamEventSrc.fire(team);
    		FacesMessage mes = new FacesMessage(FacesMessage.SEVERITY_INFO, "Updated!", "Update successful");
            facesContext.addMessage(null, mes);
    	} catch (Exception e) {
    		String errorMessage = getRootErrorMessage(e);
            FacesMessage mes = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Update unsuccessful");
            facesContext.addMessage(null, mes);
    	}
    }

    public void delete(Member m) throws Exception {
    	try {
    		memberDelete.delete(m);
    		FacesMessage mes = new FacesMessage(FacesMessage.SEVERITY_INFO, "Deleted!", "Deletion successful");
            facesContext.addMessage(null, mes);
    	} catch (Exception e) {
    		String errorMessage = getRootErrorMessage(e);
            FacesMessage mes = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Deletion unsuccessful");
            facesContext.addMessage(null, mes);
    	}
    }

    private String getRootErrorMessage(Exception e) {
        // Default to general error message that registration failed.
        String errorMessage = "Registration failed. See server log for more information";
        if (e == null) {
            // This shouldn't happen, but return the default messages
            return errorMessage;
        }

        // Start with the exception and recurse to find the root cause
        Throwable t = e;
        while (t != null) {
            // Get the message from the Throwable class instance
            errorMessage = t.getLocalizedMessage();
            t = t.getCause();
        }
        // This is the root cause message
        return errorMessage;
    }

}
