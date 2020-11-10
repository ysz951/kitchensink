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
package org.jboss.as.quickstarts.kitchensink.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@SuppressWarnings("serial")
@Entity
@XmlRootElement
@Table
public class Contest implements Serializable{
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    public Long getId() {
        return id;
    }

    @Column(nullable = false)
    private int capacity;

    @Column(nullable = false)
    private Date date;

    @Column(nullable = false)
    private boolean registration_allowed;

    @Column(nullable = false)
    private Date registration_from;

    @Column(nullable = false)
    private Date registration_to;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "contest")
    @JsonIdentityInfo(
            generator = ObjectIdGenerators.PropertyGenerator.class,
            property = "id")
    @JsonIdentityReference(alwaysAsId=true)
    private Set<Team> teams = new HashSet<>();


//    @ManyToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "PARENT_ID")
//    private Contest parentContest;
    @ManyToOne
    @JsonIdentityInfo(
            generator = ObjectIdGenerators.PropertyGenerator.class,
            property = "id")
    @JsonIdentityReference(alwaysAsId=true)
    private Contest parentContest;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "parentContest")
    @JsonIdentityInfo(
            generator = ObjectIdGenerators.PropertyGenerator.class,
            property = "id")
    @JsonIdentityReference(alwaysAsId=true)
    private Set<Contest> subContest = new HashSet<>();

    public Contest getParentContest() {
        return parentContest;
    }

    public void setParentContest(Contest parentContest) {
        this.parentContest = parentContest;
    }

    public Set<Contest> getSubContest() {
        return subContest;
    }

    public void setSubContest(Set<Contest> subContest) {
        this.subContest = subContest;
    }


    public Set<Team> getTeams() {
        return teams;
    }

    public void setTeams(Set<Team> teams) {
        this.teams = teams;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isRegistration_allowed() {
        return registration_allowed;
    }

    public void setRegistration_allowed(boolean registration_allowed) {
        this.registration_allowed = registration_allowed;
    }

    public Date getRegistration_from() {
        return registration_from;
    }

    public void setRegistration_from(Date registration_from) {
        this.registration_from = registration_from;
    }

    public Date getRegistration_to() {
        return registration_to;
    }

    public void setRegistration_to(Date registration_to) {
        this.registration_to = registration_to;
    }


}
