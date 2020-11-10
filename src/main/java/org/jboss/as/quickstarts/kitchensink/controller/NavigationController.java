package org.jboss.as.quickstarts.kitchensink.controller;


import javax.faces.bean.ManagedProperty;


public class NavigationController {

    @ManagedProperty(value = "#{param.pageId}")
    private long pageId;

    public String moveToIndex() {
        return "index";
    }

    public String moveToTeam() {
        return "team";
    }

    public String processIndex() {
        return "index";
    }

    public String processTeam() {
        return "team";
    }

    public String showPage() {
        System.out.println(pageId);
        return "index";
    }

    public long getPageId() {
        return pageId;
    }

    public void setPageId(long pageId) {
        this.pageId = pageId;
    }
}