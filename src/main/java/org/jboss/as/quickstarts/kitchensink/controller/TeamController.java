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



import java.util.HashSet;

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
import org.jboss.as.quickstarts.kitchensink_web.service.MemberUpdate;
import org.jboss.as.quickstarts.kitchensink_web.service.TeamRegistration;
import org.jboss.as.quickstarts.kitchensink_web.service.TeamUpdate;
// The @Model stereotype is a convenience mechanism to make this a request-scoped bean that has an
// EL name
// Read more about the @Model stereotype in this FAQ:
// http://www.cdi-spec.org/faq/#accordion6
@Model
public class TeamController {

    @Inject
    private FacesContext facesContext;

    @Inject
    private TeamRegistration teamRegistration;

    @Inject
    private MemberUpdate memberUpdate;

    @Inject
    private TeamUpdate teamUpdate;

    @Inject
    private TeamRepository teamRepository;

    @Inject
    private MemberRepository memberRepository;

    @Produces
    @Named
    private Team newTeam;

    @Inject
    private Event<Team> teamEventSrc;

    @PostConstruct
    public void initNewTeam() {
        newTeam = new Team();
    }

    public void register() throws Exception {
        try {

            teamRegistration.register(newTeam);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO, "Registered!", "Registration successful");
            facesContext.addMessage(null, m);
//            initNewTeam();
        } catch (Exception e) {
            String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Registration unsuccessful");
            facesContext.addMessage(null, m);
        }
    }

    public void registerAnother(long mem1, long mem2, long mem3, long coa) throws Exception {
        try {
            Member member1 = memberRepository.findById(mem1);
            Member member2 = memberRepository.findById(mem2);
            Member member3 = memberRepository.findById(mem3);
            Member coach = memberRepository.findById(coa);
            validateTeamMemberNumber(member1, member2, member3, coach);
            teamRegistration.registerWithoutFire(newTeam);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO, "Registered!", "Registration successful");
            facesContext.addMessage(null, m);
            member1.setTeam(newTeam);
            memberUpdate.updateWithoutFire(member1);
            member2.setTeam(newTeam);
            memberUpdate.updateWithoutFire(member2);
            member3.setTeam(newTeam);
            memberUpdate.updateWithoutFire(member3);
//            newTeam.setCoach(coach);
//            teamUpdate.update(newTeam);

            teamEventSrc.fire(newTeam);
        } catch (Exception e) {
            String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Registration unsuccessful");
            facesContext.addMessage(null, m);
        }
    }

    public void validateTeamMemberNumber(Member m1, Member m2, Member m3, Member coa) throws Exception {
        HashSet<Long> set = new HashSet<>();
        set.add(m1.getId());
        set.add(m2.getId());
        set.add(m3.getId());
        set.add(coa.getId());
        if (set.size() < 4) throw new Exception("member error");
    }
//
//    public void update(Member m) throws Exception {
//        try {
//            Member upd = repository.findById(m.getId());
//
//            upd.setName(m.getName());
//            upd.setEmail(m.getEmail());
//            memberUpdate.update(m);
//            FacesMessage mes = new FacesMessage(FacesMessage.SEVERITY_INFO, "Updated!", "Update successful");
//            facesContext.addMessage(null, mes);
//        } catch (Exception e) {
//            String errorMessage = getRootErrorMessage(e);
//            FacesMessage mes = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Update unsuccessful");
//            facesContext.addMessage(null, mes);
//        }
//    }
//
//    public void delete(Member m) throws Exception {
//        try {
//            memberDelete.delete(m);
//            FacesMessage mes = new FacesMessage(FacesMessage.SEVERITY_INFO, "Deleted!", "Deletion successful");
//            facesContext.addMessage(null, mes);
//        } catch (Exception e) {
//            String errorMessage = getRootErrorMessage(e);
//            FacesMessage mes = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Deletion unsuccessful");
//            facesContext.addMessage(null, mes);
//        }
//    }

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
